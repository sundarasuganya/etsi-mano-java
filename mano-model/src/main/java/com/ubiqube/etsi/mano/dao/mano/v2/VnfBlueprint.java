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
package com.ubiqube.etsi.mano.dao.mano.v2;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.DocumentId;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

import com.ubiqube.etsi.mano.dao.mano.AuditListener;
import com.ubiqube.etsi.mano.dao.mano.BlueZoneGroupInformation;
import com.ubiqube.etsi.mano.dao.mano.ExtManagedVirtualLinkDataEntity;
import com.ubiqube.etsi.mano.dao.mano.ExtVirtualLinkDataEntity;
import com.ubiqube.etsi.mano.dao.mano.OperateChanges;
import com.ubiqube.etsi.mano.dao.mano.VimConnectionInformation;
import com.ubiqube.etsi.mano.dao.mano.VnfInstance;
import com.ubiqube.etsi.mano.dao.mano.ZoneInfoEntity;
import com.ubiqube.etsi.mano.dao.mano.dto.VnfLcmResourceChanges;
import com.ubiqube.etsi.mano.dao.mano.vnfi.ChangeExtVnfConnRequest;
import com.ubiqube.etsi.mano.dao.mano.vnfm.RejectedLcmCoordination;
import com.ubiqube.etsi.mano.dao.mano.vnfm.VnfLcmCoordination;
import com.ubiqube.etsi.mano.dao.mano.vnfm.VnfPkgChange;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
@Getter
@Setter
@Entity
@Indexed
@EntityListeners(AuditListener.class)
public class VnfBlueprint extends AbstractBlueprint<VnfTask, VnfInstance> {
	/** Serial. */
	private static final long serialVersionUID = 1L;

	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@ManyToOne
	@JoinColumn
	private VnfInstance vnfInstance;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn
	private Set<VnfTask> tasks = new HashSet<>();

	@Valid
	@ManyToMany(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	private Set<VimConnectionInformation> vimConnections;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "grants")
	private Set<ZoneInfoEntity> zones;

	@Valid
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<BlueZoneGroupInformation> zoneGroups;

	@FullTextField
	private String grantsRequestId;

	@Embedded
	@IndexedEmbedded
	private BlueprintParameters parameters = new BlueprintParameters();

	@Embedded
	@IndexedEmbedded
	private OperateChanges operateChanges = new OperateChanges();

	/**
	 * Only for views and mapping.
	 */
	@Transient
	private transient Object operationParams;

	@Transient
	private transient VnfLcmResourceChanges resourceChanges;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private ChangeExtVnfConnRequest changeExtVnfConnRequest;
	// 3.3.1
	private String vnfSnapshotInfoId;
	// 3.3.1
	private VnfPkgChange modificationsTriggeredByVnfPkgChange;
	// 3.5.1
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<VnfLcmCoordination> lcmCoordinations;
	// 3.5.1
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<RejectedLcmCoordination> rejectedLcmCoordinations;
	// 3.5.1
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> warnings;

	@Override
	public void addTask(final VnfTask task) {
		if (null == tasks) {
			tasks = new HashSet<>();
		}
		tasks.add(task);
	}

	@Override
	public VnfInstance getInstance() {
		return vnfInstance;
	}

	@Override
	public void addVimConnection(final VimConnectionInformation vimConnection) {
		if (this.vimConnections == null) {
			this.vimConnections = new LinkedHashSet<>();
		}
		this.vimConnections.add(vimConnection);
	}

	@Override
	public void addExtManagedVirtualLinks(final Set<ExtManagedVirtualLinkDataEntity> extManagedVirtualLinks) {
		if (null == parameters.getExtManagedVirtualLinks()) {
			this.parameters.setExtManagedVirtualLinks(new LinkedHashSet<>());
		}
		if (null == extManagedVirtualLinks) {
			return;
		}
		this.parameters.getExtManagedVirtualLinks().addAll(extManagedVirtualLinks);
	}

	@Override
	public void addExtVirtualLinks(final Set<ExtVirtualLinkDataEntity> extVirtualLinks) {
		if (this.parameters.getExtVirtualLinkInfo() == null) {
			this.parameters.setExtVirtualLinkInfo(new LinkedHashSet<>());
		}
		this.parameters.getExtVirtualLinkInfo().addAll(extVirtualLinks);
	}

}
