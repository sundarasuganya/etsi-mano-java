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
package com.ubiqube.etsi.mano.vnfm.service.plan.contributors.v3.uow;

import com.ubiqube.etsi.mano.dao.mano.AffinityRule;
import com.ubiqube.etsi.mano.dao.mano.VimConnectionInformation;
import com.ubiqube.etsi.mano.dao.mano.v2.vnfm.AffinityRuleTask;
import com.ubiqube.etsi.mano.orchestrator.Context3d;
import com.ubiqube.etsi.mano.orchestrator.nodes.vnfm.AffinityRuleNode;
import com.ubiqube.etsi.mano.orchestrator.vt.VirtualTaskV3;
import com.ubiqube.etsi.mano.service.vim.Vim;

public class VnfAffinityUowV3 extends AbstractVnfmUowV3<AffinityRuleTask> {

	private final Vim vim;
	private final VimConnectionInformation vimConnectionInformation;

	private final AffinityRuleTask task;

	public VnfAffinityUowV3(final VirtualTaskV3<AffinityRuleTask> task, final Vim vim, final VimConnectionInformation vimConnectionInformation) {
		super(task, AffinityRuleNode.class);
		this.vim = vim;
		this.vimConnectionInformation = vimConnectionInformation;
		this.task = task.getTemplateParameters();
	}

	@Override
	public String execute(final Context3d context) {
		final AffinityRule affinityRule = task.getAffinityRule();
		return vim.createServerGroup(vimConnectionInformation, affinityRule);
	}

	@Override
	public String rollback(final Context3d context) {
		vim.deleteServerGroup(vimConnectionInformation, task.getVimResourceId());
		return null;
	}

}
