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
package com.ubiqube.etsi.mano.nfvo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ubiqube.etsi.mano.dao.mano.config.RemoteSubscription;
import com.ubiqube.etsi.mano.dao.mano.vnflcm.VnfLcmNotification;
import com.ubiqube.etsi.mano.exception.NotFoundException;
import com.ubiqube.etsi.mano.jpa.RemoteSubscriptionJpa;
import com.ubiqube.etsi.mano.nfvo.jpa.VnfLcmNotificationJpa;

/**
 *
 * @author olivier
 *
 */
@Service
public class VnfLcmNotificationService {

	private static final String EVENT_RECEIVED_ID = "Event received: {} => Id: {}";
	private static final String UNABLE_TO_FIND_NOTIFICATION_EVENT = "Unable to find notification event ";
	private static final String UNABLE_TO_FIND_NOTIFICATION_EVENT_IN_DATABASE = "Unable to find notification event {} in database.";
	private static final Logger LOG = LoggerFactory.getLogger(VnfLcmNotificationService.class);
	private final RemoteSubscriptionJpa remoteSubscriptionJpa;

	private final VnfLcmNotificationJpa vnfLcmJpa;

	public VnfLcmNotificationService(final RemoteSubscriptionJpa remoteSubscriptionJpa, final VnfLcmNotificationJpa vnfLcmJpa) {
		this.remoteSubscriptionJpa = remoteSubscriptionJpa;
		this.vnfLcmJpa = vnfLcmJpa;
	}

	public void onCreationNotification(final VnfLcmNotification event, final String version) {
		final Optional<RemoteSubscription> subscription = remoteSubscriptionJpa.findByRemoteSubscriptionId(event.getSubscriptionId());
		if (subscription.isEmpty()) {
			LOG.warn(UNABLE_TO_FIND_NOTIFICATION_EVENT_IN_DATABASE, event.getSubscriptionId());
			throw new NotFoundException(UNABLE_TO_FIND_NOTIFICATION_EVENT + event.getSubscriptionId());
		}
		event.setNfvoId(subscription.get().getRemoteServerId());
		final VnfLcmNotification newEvent = vnfLcmJpa.save(event);
		LOG.info(EVENT_RECEIVED_ID, newEvent.getNfvoId(), newEvent.getId());
	}

	public void onDeletionNotification(final VnfLcmNotification event, final String version) {
		final Optional<RemoteSubscription> subscription = remoteSubscriptionJpa.findByRemoteSubscriptionId(event.getSubscriptionId());
		if (subscription.isEmpty()) {
			LOG.warn(UNABLE_TO_FIND_NOTIFICATION_EVENT_IN_DATABASE, event.getSubscriptionId());
			throw new NotFoundException(UNABLE_TO_FIND_NOTIFICATION_EVENT + event.getSubscriptionId());
		}
		event.setNfvoId(subscription.get().getRemoteServerId());
		final VnfLcmNotification newEvent = vnfLcmJpa.save(event);
		LOG.info(EVENT_RECEIVED_ID, newEvent.getNfvoId(), newEvent.getId());
	}

	public void onVnfLcmOpOccsNotification(final VnfLcmNotification event, final String version) {
		final Optional<RemoteSubscription> subscription = remoteSubscriptionJpa.findByRemoteSubscriptionId(event.getSubscriptionId());
		if (subscription.isEmpty()) {
			LOG.warn(UNABLE_TO_FIND_NOTIFICATION_EVENT_IN_DATABASE, event.getSubscriptionId());
			throw new NotFoundException(UNABLE_TO_FIND_NOTIFICATION_EVENT + event.getSubscriptionId());
		}
		event.setNfvoId(subscription.get().getRemoteServerId());
		final VnfLcmNotification newEvent = vnfLcmJpa.save(event);
		LOG.info(EVENT_RECEIVED_ID, newEvent.getNfvoId(), newEvent.getId());
	}

}
