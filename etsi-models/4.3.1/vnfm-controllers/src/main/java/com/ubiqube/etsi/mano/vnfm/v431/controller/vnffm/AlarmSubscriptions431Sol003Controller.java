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
package com.ubiqube.etsi.mano.vnfm.v431.controller.vnffm;

import static com.ubiqube.etsi.mano.uri.ManoWebMvcLinkBuilder.linkTo;
import static com.ubiqube.etsi.mano.uri.ManoWebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import com.ubiqube.etsi.mano.vnfm.fc.vnffm.FaultMngtSubscriptionsFrontController;
import com.ubiqube.etsi.mano.vnfm.v431.model.vnffm.FmSubscription;
import com.ubiqube.etsi.mano.vnfm.v431.model.vnffm.FmSubscriptionLinks;
import com.ubiqube.etsi.mano.vnfm.v431.model.vnffm.FmSubscriptionRequest;
import com.ubiqube.etsi.mano.vnfm.v431.model.vrqan.Link;

@RestController
public class AlarmSubscriptions431Sol003Controller implements AlarmSubscriptions431Sol003Api {
	private final FaultMngtSubscriptionsFrontController faultMngtSubscriptionsFrontController;

	public AlarmSubscriptions431Sol003Controller(final FaultMngtSubscriptionsFrontController faultMngtSubscriptionsFrontController) {
		this.faultMngtSubscriptionsFrontController = faultMngtSubscriptionsFrontController;
	}

	@Override
	public ResponseEntity<List<FmSubscription>> subscriptionsGet(final MultiValueMap<String, String> requestParams, @Valid final String nextpageOpaqueMarker) {
		return faultMngtSubscriptionsFrontController.search(requestParams, FmSubscription.class, AlarmSubscriptions431Sol003Controller::makeLinks);
	}

	@Override
	public ResponseEntity<FmSubscription> subscriptionsPost(@Valid final FmSubscriptionRequest fmSubscriptionRequest) {
		return faultMngtSubscriptionsFrontController.create(fmSubscriptionRequest, FmSubscription.class, AlarmSubscriptions431Sol003Controller::makeLinks, AlarmSubscriptions431Sol003Controller::makeSelf);
	}

	@Override
	public ResponseEntity<Void> subscriptionsSubscriptionIdDelete(final String subscriptionId) {
		return faultMngtSubscriptionsFrontController.delete(subscriptionId);
	}

	@Override
	public ResponseEntity<FmSubscription> subscriptionsSubscriptionIdGet(final String subscriptionId) {
		return faultMngtSubscriptionsFrontController.findById(subscriptionId, FmSubscription.class, AlarmSubscriptions431Sol003Controller::makeLinks);
	}

	private static void makeLinks(final FmSubscription subscription) {
		final FmSubscriptionLinks links = new FmSubscriptionLinks();
		final Link link = new Link();
		link.setHref(linkTo(methodOn(AlarmSubscriptions431Sol003Api.class).subscriptionsSubscriptionIdGet(subscription.getId())).withSelfRel().getHref());
		links.setSelf(link);
		subscription.setLinks(links);
	}

	private static String makeSelf(final FmSubscription subscription) {
		return linkTo(methodOn(AlarmSubscriptions431Sol003Api.class).subscriptionsSubscriptionIdGet(subscription.getId())).withSelfRel().getHref();
	}
}
