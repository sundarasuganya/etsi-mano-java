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
package com.ubiqube.etsi.mano.vnfm.v431.controller.vnflcm;

import static com.ubiqube.etsi.mano.uri.ManoWebMvcLinkBuilder.linkTo;
import static com.ubiqube.etsi.mano.uri.ManoWebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import com.ubiqube.etsi.mano.vnfm.fc.vnflcm.VnfLcmSubscriptionFrontController;
import com.ubiqube.etsi.mano.vnfm.v431.model.vnflcm.LccnSubscription;
import com.ubiqube.etsi.mano.vnfm.v431.model.vnflcm.LccnSubscriptionLinks;
import com.ubiqube.etsi.mano.vnfm.v431.model.vnflcm.LccnSubscriptionRequest;
import com.ubiqube.etsi.mano.vnfm.v431.model.vrqan.Link;

@RestController
public class VnfLcmSubscriptions431Sol003Controller implements VnfLcmSubscriptions431Sol003Api {

	private final VnfLcmSubscriptionFrontController frontController;

	public VnfLcmSubscriptions431Sol003Controller(final VnfLcmSubscriptionFrontController frontController) {
		this.frontController = frontController;
	}

	@Override
	public ResponseEntity<List<LccnSubscription>> subscriptionsGet(final MultiValueMap<String, String> requestParams, @Valid final String nextpageOpaqueMarker) {
		return frontController.search(requestParams, nextpageOpaqueMarker, LccnSubscription.class, VnfLcmSubscriptions431Sol003Controller::makeLinks);
	}

	@Override
	public ResponseEntity<LccnSubscription> subscriptionsPost(@Valid final LccnSubscriptionRequest body) {
		return frontController.create(body, LccnSubscription.class, VnfLcmSubscriptions431Sol003Controller::makeLinks, VnfLcmSubscriptions431Sol003Controller::getSelfLink);
	}

	@Override
	public ResponseEntity<Void> subscriptionsSubscriptionIdDelete(final String subscriptionId) {
		return frontController.deleteById(subscriptionId);
	}

	@Override
	public ResponseEntity<LccnSubscription> subscriptionsSubscriptionIdGet(final String subscriptionId) {
		return frontController.findById(subscriptionId, LccnSubscription.class, VnfLcmSubscriptions431Sol003Controller::makeLinks);
	}

	private static String getSelfLink(final LccnSubscription subscription) {
		return linkTo(methodOn(VnfLcmSubscriptions431Sol003Api.class).subscriptionsSubscriptionIdGet(subscription.getId())).withSelfRel().getHref();
	}

	private static void makeLinks(final LccnSubscription subs) {
		final LccnSubscriptionLinks links = new LccnSubscriptionLinks();
		final Link self = new Link();
		self.setHref(linkTo(methodOn(VnfLcmSubscriptions431Sol003Api.class).subscriptionsSubscriptionIdGet(subs.getId())).withSelfRel().getHref());
		links.setSelf(self);
		subs.setLinks(links);
	}
}
