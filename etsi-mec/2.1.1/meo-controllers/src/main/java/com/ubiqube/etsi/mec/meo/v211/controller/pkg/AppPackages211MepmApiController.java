/**
 *     Copyright (C) 2019-2020 Ubiqube.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mec.meo.v211.controller.pkg;

import static com.ubiqube.etsi.mano.Constants.getSingleField;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ubiqube.etsi.mano.dao.mano.OperationalStateType;
import com.ubiqube.etsi.mano.dao.mec.pkg.AppPkg;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.service.ManoSearchResponseService;
import com.ubiqube.etsi.mec.controller.pkg.AppPackageMeoController;
import com.ubiqube.etsi.mec.meo.v211.model.pkg.AppD;
import com.ubiqube.etsi.mec.meo.v211.model.pkg.AppPkgInfo;
import com.ubiqube.etsi.mec.meo.v211.model.pkg.AppPkgInfoModifications;
import com.ubiqube.etsi.mec.meo.v211.model.pkg.CreateAppPkg;

import ma.glasnost.orika.MapperFacade;

@RestController
@ExposesResourceFor(AppPkgInfo.class)
public class AppPackages211MepmApiController implements AppPackages211MepmApi {
	private static final String APP_SEARCH_DEFAULT_EXCLUDE_FIELDS = "checksum,softwareImages,additionalArtifacts,userDefinedData";

	private static final Set<String> APP_SEARCH_MANDATORY_FIELDS = new HashSet<>(Arrays.asList("id"));

	private final AppPackageMeoController appPackageMeoController;

	private final MapperFacade mapper;

	private final EntityLinks entityLinks;

	private final ManoSearchResponseService searchService;

	public AppPackages211MepmApiController(final AppPackageMeoController _appPackageMeoController, final MapperFacade _mapper, final EntityLinks _entityLinks, final ManoSearchResponseService _searchService) {
		appPackageMeoController = _appPackageMeoController;
		mapper = _mapper;
		entityLinks = _entityLinks;
		searchService = _searchService;
	}

	@Override
	public ResponseEntity<Void> appPackageDELETE(final String appPkgId) {
		appPackageMeoController.deleteById(UUID.fromString(appPkgId));
		return ResponseEntity.noContent().build();
	}

	@Override
	public ResponseEntity<AppPkgInfo> appPackageGET(final String appPkgId) {
		final AppPkg appPkg = appPackageMeoController.findById(UUID.fromString(appPkgId));
		final AppPkgInfo appPkgInfo = mapper.map(appPkg, AppPkgInfo.class);
		createSelfLink(appPkgInfo);
		return ResponseEntity.ok(appPkgInfo);
	}

	private void createSelfLink(final AppPkgInfo re) {
		final Link selfLink = entityLinks.linkToItemResource(AppPkgInfo.class, re.getAppDId());
		// re.add(selfLink);
	}

	@Override
	public ResponseEntity<AppPkgInfoModifications> appPackagePATCH(@Valid final AppPkgInfoModifications body, final String appPkgId) {
		appPackageMeoController.setOperationState(UUID.fromString(appPkgId), OperationalStateType.fromValue(body.getOperationState().toString()));
		return ResponseEntity.ok(mapper.map(body, AppPkgInfoModifications.class));
	}

	@Override
	public ResponseEntity appPackagesGET(final MultiValueMap<String, String> requestParams, final Pageable pageable) {
		final String filter = getSingleField(requestParams, "filter");
		final List<AppPkg> list = appPackageMeoController.query(filter);
		return searchService.search(requestParams, AppPkgInfo.class, APP_SEARCH_DEFAULT_EXCLUDE_FIELDS, APP_SEARCH_MANDATORY_FIELDS, list, AppPkgInfo.class, this::createSelfLink);
	}

	@Override
	public ResponseEntity<AppPkgInfo> appPackagesPOST(@Valid final CreateAppPkg body) {
		AppPkg appPkg = mapper.map(body, AppPkg.class);
		appPkg = appPackageMeoController.save(appPkg);
		final AppPkgInfo mapped = mapper.map(appPkg, AppPkgInfo.class);
		createSelfLink(mapped);
		return ResponseEntity.ok(mapped);
	}

	@Override
	public ResponseEntity<Resource> appPkgGetContent(final String appPkgId) {
		return appPackageMeoController.getPackage(UUID.fromString(appPkgId));
	}

	@Override
	public ResponseEntity<AppD> appPkgIdGetAppd(final String appPkgId, @Valid final String filter, @Valid final String allFields, @Valid final String fields, @Valid final String excludeFields, @Valid final String excludeDefault) {
		final AppPkg appPkg = appPackageMeoController.getAppd(UUID.fromString(appPkgId));
		return ResponseEntity.ok(mapper.map(appPkg, AppD.class));
	}

	@Override
	public ResponseEntity<Void> appPkgPutContent(final String appPkgId, final MultipartFile file) {
		try {
			appPackageMeoController.store(UUID.fromString(appPkgId), file.getBytes());
		} catch (final IOException e) {
			throw new GenericException(e);
		}
		return ResponseEntity.noContent().build();
	}

}
