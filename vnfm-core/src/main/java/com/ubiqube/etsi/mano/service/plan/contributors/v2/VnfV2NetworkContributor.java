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
package com.ubiqube.etsi.mano.service.plan.contributors.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Priority;

import org.springframework.stereotype.Service;

import com.ubiqube.etsi.mano.dao.mano.ChangeType;
import com.ubiqube.etsi.mano.dao.mano.ResourceTypeEnum;
import com.ubiqube.etsi.mano.dao.mano.VnfInstance;
import com.ubiqube.etsi.mano.dao.mano.VnfLiveInstance;
import com.ubiqube.etsi.mano.dao.mano.VnfPackage;
import com.ubiqube.etsi.mano.dao.mano.VnfVl;
import com.ubiqube.etsi.mano.dao.mano.v2.NetworkTask;
import com.ubiqube.etsi.mano.dao.mano.v2.PlanOperationType;
import com.ubiqube.etsi.mano.dao.mano.v2.VnfBlueprint;
import com.ubiqube.etsi.mano.jpa.VnfLiveInstanceJpa;
import com.ubiqube.etsi.mano.orchestrator.Bundle;
import com.ubiqube.etsi.mano.orchestrator.nodes.Node;
import com.ubiqube.etsi.mano.orchestrator.nodes.vnfm.Network;
import com.ubiqube.etsi.mano.service.VnfBlueprintService;
import com.ubiqube.etsi.mano.service.graph.VnfBundleAdapter;
import com.ubiqube.etsi.mano.service.plan.contributors.v2.vt.NetWorkVt;

/**
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
@Priority(100)
@Service
public class VnfV2NetworkContributor extends AbstractContributorV2Base<NetworkTask, NetWorkVt> {
	private final VnfBlueprintService planService;
	private final VnfLiveInstanceJpa vnfLiveInstanceJpa;

	public VnfV2NetworkContributor(final VnfBlueprintService planService, final VnfLiveInstanceJpa vnfLiveInstanceJpa) {
		super();
		this.planService = planService;
		this.vnfLiveInstanceJpa = vnfLiveInstanceJpa;
	}

	@Override
	public List<NetWorkVt> vnfContribute(final Bundle bundle, final VnfBlueprint parameters) {
		if (PlanOperationType.INSTANTIATE != parameters.getOperation()) {
			return doTerminatePlan(parameters.getVnfInstance());
		}
		final VnfPackage vnfPackage = ((VnfBundleAdapter) bundle).getVnfPackage();
		final Set<VnfVl> vls = vnfPackage.getVnfVl();
		final List<NetWorkVt> ret = new ArrayList<>();
		for (final VnfVl vnfVl : vls) {
			final int num = planService.getNumberOfLiveVl(parameters.getVnfInstance(), vnfVl);
			if (num == 0) {
				final NetworkTask networkTask = createTask(NetworkTask::new);
				networkTask.setAlias(vnfVl.getToscaName());
				networkTask.setChangeType(ChangeType.ADDED);
				networkTask.setToscaName(vnfVl.getToscaName());
				networkTask.setType(ResourceTypeEnum.VL);
				networkTask.setVnfVl(vnfVl);
				ret.add(new NetWorkVt(networkTask));
			}
		}
		return ret;
	}

	private List<NetWorkVt> doTerminatePlan(final VnfInstance vnfInstance) {
		final List<VnfLiveInstance> instances = vnfLiveInstanceJpa.findByVnfInstanceIdAndClass(vnfInstance, NetworkTask.class.getSimpleName());
		return instances.stream().map(x -> {
			final NetworkTask networkTask = createDeleteTask(NetworkTask::new, x);
			networkTask.setAlias(x.getTask().getAlias());
			networkTask.setChangeType(ChangeType.REMOVED);
			networkTask.setToscaName(x.getTask().getToscaName());
			networkTask.setType(ResourceTypeEnum.VL);
			networkTask.setRemovedVnfLiveInstance(x.getId());
			networkTask.setVimResourceId(x.getResourceId());
			networkTask.setVimConnectionId(x.getVimConnectionId());
			networkTask.setVnfVl(((NetworkTask) (x.getTask())).getVnfVl());
			return new NetWorkVt(networkTask);
		}).collect(Collectors.toList());
	}

	@Override
	public Class<? extends Node> getNode() {
		return Network.class;
	}

}
