package com.ubiqube.etsi.mano.repository.jpa;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.validation.constraints.NotNull;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.repository.ContentManager;
import com.ubiqube.etsi.mano.repository.NamingStrategy;

public abstract class AbstractBinaryRepository implements BinaryRepository {
	private final ContentManager contentManager;
	private final ObjectMapper jsonMapper;

	private final NamingStrategy namingStrategy;

	public AbstractBinaryRepository(final ContentManager contentManager, final ObjectMapper jsonMapper, final NamingStrategy _namingStrategy) {
		super();
		this.contentManager = contentManager;
		this.jsonMapper = jsonMapper;
		namingStrategy = _namingStrategy;
	}

	protected void mkdir(final String _id) {
		final Path path = namingStrategy.getRoot(getFrontClass(), _id);
		contentManager.mkdir(path);
	}

	@Override
	public final void storeObject(final String _id, final String _filename, final Object _object) {
		try {
			final String str = jsonMapper.writeValueAsString(_object);
			storeBinary(_id, _filename, new ByteArrayInputStream(str.getBytes()));
		} catch (final JsonProcessingException e) {
			throw new GenericException(e);
		}
	}

	@Override
	public final void storeBinary(final String _id, final String _filename, final InputStream _stream) {
		final Path path = namingStrategy.getRoot(getFrontClass(), _id, _filename);
		contentManager.store(path, _stream);
	}

	@Override
	public final byte[] getBinary(final String _id, final String _filename) {
		return getBinary(_id, _filename, 0, null);
	}

	@Override
	public final byte[] getBinary(final String _id, final String _filename, final int min, final Long max) {
		final Path path = namingStrategy.getRoot(getFrontClass(), _id, _filename);
		final InputStream os = contentManager.load(path, min, max);
		try {
			return IOUtils.toByteArray(os);
		} catch (final IOException e) {
			throw new GenericException(e);
		}
	}

	@Override
	public final <T, U extends Class> T loadObject(@NotNull final String _id, final String _filename, final U t) {
		final byte[] content = getBinary(_id, _filename, 0, null);
		try {
			return (T) jsonMapper.readValue(content, t);
		} catch (final IOException e) {
			throw new GenericException(e);
		}
	}

	protected abstract Class getFrontClass();
}
