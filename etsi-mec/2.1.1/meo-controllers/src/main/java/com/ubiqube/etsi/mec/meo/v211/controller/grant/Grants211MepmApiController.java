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
package com.ubiqube.etsi.mec.meo.v211.controller.grant;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.ubiqube.etsi.mano.dao.mano.GrantResponse;
import com.ubiqube.etsi.mano.exception.BadRequestException;
import com.ubiqube.etsi.mec.controller.grant.AppGrantController;
import com.ubiqube.etsi.mec.meo.v211.model.grant.Grant;
import com.ubiqube.etsi.mec.meo.v211.model.grant.GrantRequest;

import ma.glasnost.orika.MapperFacade;

/**
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
@RestController
public class Grants211MepmApiController implements Grants211MepmApi {
	private final AppGrantController appGrantController;

	private final MapperFacade mapper;

	public Grants211MepmApiController(final AppGrantController _appGrantController, final MapperFacade _mapper) {
		appGrantController = _appGrantController;
		mapper = _mapper;
	}

	@Override
	public ResponseEntity<Grant> grantGET(final String grantId) {
		final GrantResponse grants = appGrantController.findById(UUID.fromString(grantId));
		if (!grants.getAvailable().equals(Boolean.TRUE)) {
			return ResponseEntity.accepted().build();
		}
		final Grant jsonGrant = mapper.map(grants, Grant.class);
		return ResponseEntity.ok(jsonGrant);
	}

	@Override
	public ResponseEntity<Grant> grantPOST(final GrantRequest grantRequest) {
		final GrantResponse obj = mapper.map(grantRequest, GrantResponse.class);
		checkGrant(obj);
		final GrantResponse resp = appGrantController.post(obj);
		final URI location = linkTo(methodOn(Grants211MepmApi.class).grantGET(resp.getId().toString())).withSelfRel().toUri();
		return ResponseEntity.accepted().location(location).build();
	}

	private static void checkGrant(final GrantResponse obj) {
		// Optional.ofNullable(obj.getVnfInstance()).orElseThrow(() -> new
		// BadRequestException("Unable to find App instance: " + obj.getVnfInstance()));
		Optional.ofNullable(obj.getVnfdId()).orElseThrow(() -> new BadRequestException("Unable to find Appd Id: " + obj.getVnfdId()));
		// Optional.ofNullable(obj.getVnfLcmOpOccs()).orElseThrow(() -> new
		// BadRequestException("Unable to find App LCM OP OCC: " +
		// obj.getVnfLcmOpOccs()));
	}

}
