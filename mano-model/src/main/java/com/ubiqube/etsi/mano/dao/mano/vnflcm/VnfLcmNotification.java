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
package com.ubiqube.etsi.mano.dao.mano.vnflcm;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.ubiqube.etsi.mano.dao.mano.ExtVirtualLinkInfoEntity;
import com.ubiqube.etsi.mano.dao.mano.LcmAffectedVnfc;
import com.ubiqube.etsi.mano.dao.mano.common.FailureDetails;
import com.ubiqube.etsi.mano.dao.mano.v2.OperationStatusType;
import com.ubiqube.etsi.mano.dao.mano.v2.PlanOperationType;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author olivier
 *
 */
@Setter
@Getter
@Entity
public class VnfLcmNotification {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private UUID nfvoId;

	private String notificationType;

	private String subscriptionId;

	private OffsetDateTime timeStamp;

	private String vnfInstanceId;

	private OperationStatusType operationState;

	private PlanOperationType operation;

	private Boolean isAutomaticInvocation;

	private String vnfLcmOpOccId;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<LcmAffectedVnfc> affectedVnfcs;

	// private List<AffectedVirtualLink> affectedVirtualLinks

	// private List<AffectedVirtualStorage> affectedVirtualStorages

	// private VnfInfoModifications changedInfo
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<ExtVirtualLinkInfoEntity> changedExtConnectivity;

	private FailureDetails error;
}
