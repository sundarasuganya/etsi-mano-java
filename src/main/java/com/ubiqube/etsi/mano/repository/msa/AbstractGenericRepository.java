package com.ubiqube.etsi.mano.repository.msa;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubiqube.api.entities.repository.RepositoryElement;
import com.ubiqube.api.exception.ServiceException;
import com.ubiqube.api.interfaces.repository.RepositoryService;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.exception.NotAcceptableException;
import com.ubiqube.etsi.mano.exception.NotFoundException;
import com.ubiqube.etsi.mano.grammar.AstBuilder;
import com.ubiqube.etsi.mano.grammar.JsonFilter;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.repository.CrudRepository;

/**
 * A Generic implementation of classical CRUD action around a repository. XXX:
 * This class should use lowDriver.
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 * @param <T>
 */
public abstract class AbstractGenericRepository<T> implements CrudRepository<T>, BinaryRepository {
	private static final String ETSI_MANO = "etsi-mano";
	private static final String NCROOT = "ncroot";
	private static final Logger LOG = LoggerFactory.getLogger(AbstractGenericRepository.class);
	private final ObjectMapper mapper;
	protected final JsonFilter jsonFilter;
	private final RepositoryService repositoryService;

	public AbstractGenericRepository(final ObjectMapper _mapper, final RepositoryService _repositoryService, final JsonFilter _jsonFilter) {
		repositoryService = _repositoryService;
		mapper = _mapper;
		jsonFilter = _jsonFilter;
	}

	abstract String setId(T _entity);

	protected String computePath(final String _id) {
		final StringBuilder sb = new StringBuilder(getRoot());
		sb.append('/').append(_id);
		return sb.toString();
	}

	private void mkdir(final String uri) {
		try {
			if (!repositoryService.exists(uri)) {
				repositoryService.addDirectory(uri, "", ETSI_MANO, NCROOT);
			}
		} catch (final ServiceException e) {
			throw new GenericException(e);
		}
	}

	abstract String getRoot();

	@SuppressWarnings("unchecked")
	@Override
	public final T get(final String _id) {
		final String uri = computePath(_id) + '/' + getFilename();
		LOG.debug("Loading ID: {}", _id);
		verify(uri);
		final RepositoryElement repositoryElement = repositoryService.getElement(uri);
		final byte[] repositoryContent = repositoryService.getRepositoryElementContent(repositoryElement);

		try {
			final String content = new String(repositoryContent, StandardCharsets.UTF_8);
			return (T) mapper.readValue(content, getClazz());
		} catch (final IOException e) {
			throw new GenericException(e);
		}
	}

	abstract String getFilename();

	abstract Class<?> getClazz();

	@Override
	public void delete(final String _id) {
		final String uri = computePath(_id);
		verify(uri);
		final RepositoryElement repositoryElement = repositoryService.getElement(uri);
		repositoryService.deleteRepositoryElement(repositoryElement, NCROOT);
	}

	@Override
	public T save(final T _entity) {
		final String saveId = setId(_entity);
		final String dir = computePath(saveId);
		mkdir(dir);
		final String uri = dir + '/' + getFilename();
		try {
			final String str = mapper.writeValueAsString(_entity);
			LOG.info("Creating entity @ {}", uri);
			repositoryService.addFile(uri, ETSI_MANO, ETSI_MANO, str, NCROOT);
		} catch (IOException | ServiceException e) {
			throw new GenericException(e);
		}

		return _entity;
	}

	@Override
	public void storeObject(final String _id, final String _filename, final Object _object) {
		final StringBuilder path = new StringBuilder(computePath(_id));
		verify(path.toString());
		path.append('/').append(_filename);
		try {
			repositoryService.addFile(path.toString(), ETSI_MANO, ETSI_MANO, mapper.writeValueAsString(_object), NCROOT);
		} catch (final ServiceException | JsonProcessingException e) {
			throw new GenericException(e);
		}
	}

	@Override
	public final <T, U extends Class> T loadObject(@NotNull final String _id, final String _filename, final U t) {
		final StringBuilder path = new StringBuilder(computePath(_id));
		verify(path.toString());
		path.append('/').append(_filename);
		final RepositoryElement repositoryElement = repositoryService.getElement(path.toString());
		if (null == repositoryElement) {
			LOG.error("Unable to find path: {}", path.toString());
		}
		final byte[] repositoryContent = repositoryService.getRepositoryElementContent(repositoryElement);

		try {
			return (T) mapper.readValue(repositoryContent, t);
		} catch (final IOException e) {
			throw new GenericException(e);
		}

	}

	@Override
	public void storeBinary(final String _id, final String _filename, final InputStream _stream) {
		final StringBuilder path = new StringBuilder(computePath(_id));
		verify(path.toString());
		path.append('/').append(_filename);

		try {
			repositoryService.addFile(path.toString(), ETSI_MANO, ETSI_MANO, StreamUtils.copyToByteArray(_stream), NCROOT);
		} catch (ServiceException | IOException e) {
			throw new GenericException(e);
		}
	}

	@Override
	public List<T> query(final String filter) {
		List<String> listFilesInFolder;
		try {
			listFilesInFolder = repositoryService.doSearch(getRoot(), getFilename());
		} catch (final ServiceException e) {
			throw new GenericException(e);
		}
		final AstBuilder astBuilder = new AstBuilder(filter);
		final ArrayList<T> ret = new ArrayList<>();
		for (final String entry : listFilesInFolder) {
			final String path = entry.substring((getRoot() + '/').length());
			final File file = new File(path);
			LOG.info("Retreiving: {}", file.getParent());
			final T repoObject = get(file.getParent());
			if (jsonFilter.apply(repoObject, astBuilder)) {
				ret.add(repoObject);
			}
		}
		return ret;
	}

	@Override
	public byte[] getBinary(final String _id, final String _filename) {
		final StringBuilder path = new StringBuilder(computePath(_id));
		path.append('/').append(_filename);
		final RepositoryElement repositoryElement = repositoryService.getElement(path.toString());
		if (null == repositoryElement) {
			throw new NotFoundException("Element " + path + " not found.");
		}
		return repositoryService.getRepositoryElementContent(repositoryElement);
	}

	@Override
	public byte[] getBinary(final String _id, final String _filename, final int min, final Long max) {
		// We should ask for an API.
		final byte[] repositoryContent = getBinary(_id, _filename);
		if (min >= repositoryContent.length) {
			throw new NotAcceptableException("Could not retreive a min > lenght of file.");
		}
		return Arrays.copyOfRange(repositoryContent, min, max == null ? repositoryContent.length - min : max.intValue());
	}

	@Override
	public void delete(@NotNull final String _id, @NotNull final String _filename) {
		// XXX:repositoryService.de
	}

	protected void verify(final String _uri) {
		try {
			if (!repositoryService.exists(_uri)) {
				throw new NotFoundException("Object not found " + _uri);
			}
		} catch (final ServiceException e) {
			throw new GenericException(e);
		}
	}
}
