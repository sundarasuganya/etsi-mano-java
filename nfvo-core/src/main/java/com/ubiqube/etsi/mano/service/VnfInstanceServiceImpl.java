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
package com.ubiqube.etsi.mano.service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ubiqube.etsi.mano.dao.mano.ExtVirtualLinkDataEntity;
import com.ubiqube.etsi.mano.dao.mano.NsdInstance;
import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.VnfExtCp;
import com.ubiqube.etsi.mano.dao.mano.VnfInstance;
import com.ubiqube.etsi.mano.dao.mano.VnfInstantiatedBase;
import com.ubiqube.etsi.mano.dao.mano.VnfInstantiatedCompute;
import com.ubiqube.etsi.mano.dao.mano.VnfInstantiatedDnsZone;
import com.ubiqube.etsi.mano.dao.mano.VnfInstantiatedExtCp;
import com.ubiqube.etsi.mano.dao.mano.VnfInstantiatedMonitoring;
import com.ubiqube.etsi.mano.dao.mano.VnfInstantiatedStorage;
import com.ubiqube.etsi.mano.dao.mano.VnfInstantiatedVirtualLink;
import com.ubiqube.etsi.mano.dao.mano.VnfLcmOpOccs;
import com.ubiqube.etsi.mano.dao.mano.VnfLiveInstance;
import com.ubiqube.etsi.mano.dao.mano.VnfStorage;
import com.ubiqube.etsi.mano.dao.mano.VnfVl;
import com.ubiqube.etsi.mano.dao.mano.v2.Blueprint;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.exception.NotFoundException;
import com.ubiqube.etsi.mano.jpa.ExtVirtualLinkDataEntityJpa;
import com.ubiqube.etsi.mano.jpa.VnfInstanceJpa;
import com.ubiqube.etsi.mano.jpa.VnfInstantiatedDnsZoneJpa;
import com.ubiqube.etsi.mano.jpa.VnfInstantiedComputeJpa;
import com.ubiqube.etsi.mano.jpa.VnfInstantiedExtCpJpa;
import com.ubiqube.etsi.mano.jpa.VnfInstantiedStorageJpa;
import com.ubiqube.etsi.mano.jpa.VnfInstantiedVirtualLinkJpa;
import com.ubiqube.etsi.mano.jpa.VnfLcmOpOccsJpa;
import com.ubiqube.etsi.mano.jpa.VnfLiveInstanceJpa;

@Service
public class VnfInstanceServiceImpl implements VnfInstanceService {
	private static final Logger LOG = LoggerFactory.getLogger(VnfInstanceServiceImpl.class);

	private final ExtVirtualLinkDataEntityJpa extVirtualLinkDataEntityJpa;

	private final VnfInstantiedComputeJpa vnfInstantiedComputeJpa;

	private final VnfInstantiedVirtualLinkJpa vnfInstantiedVirtualLinkJpa;

	private final VnfInstantiedStorageJpa vnfInstantiedStorageJpa;

	private final VnfInstantiedExtCpJpa vnfInstantiedExtCpJpa;

	private final VnfInstanceJpa vnfInstanceJpa;

	private final VnfLcmOpOccsJpa vnfLcmOpOccsJpa;

	private final GrantService grantService;

	private final VnfLiveInstanceJpa vnfLiveInstanceJpa;

	private final VnfInstantiatedDnsZoneJpa vnInstantiatedDnsZoneJpa;

	public VnfInstanceServiceImpl(final ExtVirtualLinkDataEntityJpa _extVirtualLinkDataEntityJpa, final VnfInstantiedComputeJpa _vnfInstantiedComputeJpa, final VnfInstantiedVirtualLinkJpa _vnfInstantiedVirtualLinkJpa, final VnfInstantiedExtCpJpa _vnfInstantiedExtCpJpa, final VnfInstantiedStorageJpa _vnfInstantiedStorageJpa, final VnfInstanceJpa _vnfInstanceJpa, final VnfLcmOpOccsJpa _vnfLcmOpOccsJpa, final GrantService _grantService, final VnfLiveInstanceJpa _vnfLiveInstance, final VnfInstantiatedDnsZoneJpa _vnInstantiatedDnsZoneJpa) {
		extVirtualLinkDataEntityJpa = _extVirtualLinkDataEntityJpa;
		vnfInstantiedComputeJpa = _vnfInstantiedComputeJpa;
		vnfInstantiedVirtualLinkJpa = _vnfInstantiedVirtualLinkJpa;
		vnfInstantiedExtCpJpa = _vnfInstantiedExtCpJpa;
		vnfInstantiedStorageJpa = _vnfInstantiedStorageJpa;
		vnfInstanceJpa = _vnfInstanceJpa;
		vnfLcmOpOccsJpa = _vnfLcmOpOccsJpa;
		grantService = _grantService;
		vnfLiveInstanceJpa = _vnfLiveInstance;
		vnInstantiatedDnsZoneJpa = _vnInstantiatedDnsZoneJpa;
	}

	@Override
	public List<ExtVirtualLinkDataEntity> getAllExtVirtualLinks(final VnfInstance vnfInstance) {
		return extVirtualLinkDataEntityJpa.findByVnfInstance(vnfInstance);
	}

	@Override
	public int getNumberOfLiveInstance(final VnfInstance vnfInstance, final VnfCompute vnfCompute) {
		return vnfLiveInstanceJpa.countByVnfInstanceAndVduId(vnfInstance, vnfCompute.getId());
	}

	@Override
	public Deque<VnfLiveInstance> getLiveComputeInstanceOf(final VnfInstance vnfInstance, final VnfCompute vnfCompute) {
		return vnfLiveInstanceJpa.findByVduIdAndVnfInstance(vnfCompute.getId(), vnfInstance).stream().collect(Collectors.toCollection(ArrayDeque::new));
	}

	@Override
	public int getNumberOfLiveVl(final VnfInstance vnfInstance, final VnfVl x) {
		return vnfLiveInstanceJpa.countByVnfInstanceAndVduId(vnfInstance, x.getId());
	}

	@Override
	public int getNumberOfLiveExtCp(final VnfInstance vnfInstance, final VnfExtCp extCp) {
		return vnfLiveInstanceJpa.countByVnfInstanceAndVduId(vnfInstance, extCp.getId());
	}

	@Override
	public VnfInstantiatedCompute save(final VnfInstantiatedCompute vnfInstantiedCompute) {
		return vnfInstantiedComputeJpa.save(vnfInstantiedCompute);
	}

	@Override
	public VnfInstantiatedVirtualLink save(final VnfInstantiatedVirtualLink vnfInstantiedVirtualLink) {
		return vnfInstantiedVirtualLinkJpa.save(vnfInstantiedVirtualLink);
	}

	@Override
	public VnfInstantiatedExtCp save(final VnfInstantiatedExtCp vnfInstantiedExtCp) {
		return vnfInstantiedExtCpJpa.save(vnfInstantiedExtCp);
	}

	@Override
	public VnfInstantiatedStorage save(final VnfInstantiatedStorage vs) {
		return vnfInstantiedStorageJpa.save(vs);
	}

	@Override
	public int getNumberOfLiveStorage(final VnfInstance vnfInstance, final VnfStorage x) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<VnfInstantiatedCompute> getLiveComputeInstanceOf(final VnfInstance vnfInstance) {
		return vnfInstantiedComputeJpa.findByLiveInstanceOfVnfInstance(vnfInstance);
	}

	@Override
	public List<VnfInstantiatedExtCp> getLiveExtCpInstanceOf(final VnfInstance vnfInstance) {
		return vnfInstantiedExtCpJpa.findByLiveInstanceOfVnfInstance(vnfInstance);
	}

	@Override
	public List<VnfInstantiatedStorage> getLiveStorageInstanceOf(final VnfInstance vnfInstance) {
		return vnfInstantiedStorageJpa.findByLiveInstanceOfVnfInstance(vnfInstance);
	}

	@Override
	public List<VnfInstantiatedVirtualLink> getLiveVirtualLinkInstanceOf(final VnfInstance vnfInstance) {
		return vnfInstantiedVirtualLinkJpa.findByLiveInstanceOfVnfInstance(vnfInstance);
	}

	@Override
	public List<VnfInstantiatedDnsZone> getLiveDnsZoneInstanceOf(final VnfInstance vnfInstance) {
		return vnInstantiatedDnsZoneJpa.findByLiveInstanceOfVnfInstance(vnfInstance);
	}

	@Override
	public VnfInstance findBVnfInstanceyVnfPackageId(final NsdInstance nsdInstance, final UUID vnfPackageId) {
		return vnfInstanceJpa.findByVnfPkg_IdAndNsInstance_Id(vnfPackageId, nsdInstance.getId()).orElseThrow(() -> new NotFoundException("Could not find vnf=" + vnfPackageId + " nsInstance=" + nsdInstance.getId()));
	}

	@Override
	public VnfInstance findById(final UUID id) {
		return vnfInstanceJpa.findById(id).orElseThrow(() -> new NotFoundException("Could not find " + id));
	}

	@Override
	public VnfInstance save(final VnfInstance vnfInstance) {
		return vnfInstanceJpa.save(vnfInstance);
	}

	@Override
	@Transactional
	public void delete(final UUID vnfInstanceId) {
		final VnfInstance vnfInstance = vnfInstanceJpa.findById(vnfInstanceId).orElseThrow(() -> new NotFoundException("Vnf Instance " + vnfInstanceId + " not found."));
		final Set<VnfLcmOpOccs> lcmOpOccs = vnfLcmOpOccsJpa.findByVnfInstance(vnfInstance);
		lcmOpOccs.forEach(x -> {
			LOG.info("Deleting LcmOpOccs {}", x.getId());
			vnfLcmOpOccsJpa.delete(x);
		});
		vnfInstanceJpa.deleteById(vnfInstanceId);
	}

	@Override
	public VnfLiveInstance findLiveInstanceByInstantiated(final UUID id) {
		return vnfLiveInstanceJpa.findByVnfInstantiatedBaseId(id);
	}

	@Override
	public VnfLiveInstance save(final VnfLiveInstance vli) {
		return vnfLiveInstanceJpa.save(vli);
	}

	@Override
	public Optional<VnfLiveInstance> findLiveInstanceById(final UUID removedInstantiated) {
		return vnfLiveInstanceJpa.findById(removedInstantiated);
	}

	@Override
	public void deleteLiveInstanceById(final UUID id) {
		vnfLiveInstanceJpa.deleteById(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends VnfInstantiatedBase> T save(final T y) {
		if (y instanceof VnfInstantiatedCompute) {
			return (T) vnfInstantiedComputeJpa.save((VnfInstantiatedCompute) y);
		} else if (y instanceof VnfInstantiatedExtCp) {
			return (T) vnfInstantiedExtCpJpa.save((VnfInstantiatedExtCp) y);
		} else if (y instanceof VnfInstantiatedStorage) {
			return (T) vnfInstantiedStorageJpa.save((VnfInstantiatedStorage) y);
		} else if (y instanceof VnfInstantiatedVirtualLink) {
			return (T) vnfInstantiedVirtualLinkJpa.save((VnfInstantiatedVirtualLink) y);
		} else if (y instanceof VnfInstantiatedMonitoring) {
			return y;
		}
		throw new GenericException("Unknown class type: " + y.getClass());
	}

	@Override
	public Deque<VnfLiveInstance> getLiveComputeInstanceOf(final Blueprint plan, final VnfCompute vnfCompute) {
		// TODO
		return new ArrayDeque<>();
	}

}
