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
package com.ubiqube.etsi.mano.vnfm.service.plan.contributors.v2;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ubiqube.etsi.mano.dao.mano.ResourceTypeEnum;
import com.ubiqube.etsi.mano.dao.mano.VnfPackage;
import com.ubiqube.etsi.mano.dao.mano.v2.VnfBlueprint;
import com.ubiqube.etsi.mano.dao.mano.v2.vnfm.SecurityGroupTask;
import com.ubiqube.etsi.mano.orchestrator.SclableResources;
import com.ubiqube.etsi.mano.orchestrator.nodes.vnfm.SecurityGroupNode;
import com.ubiqube.etsi.mano.vnfm.jpa.VnfLiveInstanceJpa;

/**
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
@Service
public class SecurityGroupContributor extends AbstractContributorV3Base<SecurityGroupTask> {

	public SecurityGroupContributor(final VnfLiveInstanceJpa vnfLiveInstanceJpa) {
		super(vnfLiveInstanceJpa);
	}

	@Override
	public List<SclableResources<SecurityGroupTask>> contribute(final VnfPackage bundle, final VnfBlueprint parameters) {
		return bundle.getSecurityGroups().stream().map(x -> {
			final SecurityGroupTask task = createTask(SecurityGroupTask::new);
			task.setType(ResourceTypeEnum.SECURITY_GROUP);
			task.setToscaName(x.getToscaName());
			task.setSecurityGroup(x);
			return create(SecurityGroupNode.class, task.getToscaName(), 1, task, parameters.getInstance());
		}).toList();
	}

}
