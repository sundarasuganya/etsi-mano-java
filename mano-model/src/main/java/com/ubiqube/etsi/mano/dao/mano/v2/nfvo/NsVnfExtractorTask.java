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
package com.ubiqube.etsi.mano.dao.mano.v2.nfvo;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;

import com.ubiqube.etsi.mano.dao.mano.config.Servers;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author olivier
 *
 */
@Entity
@Getter
@Setter
public class NsVnfExtractorTask extends NsTask {
	/** Serial. */
	private static final long serialVersionUID = 1L;

	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
	private Servers server;

	private String nsdId;

	@Override
	public NsTask copy() {
		final NsVnfExtractorTask task = new NsVnfExtractorTask();
		super.copy(task);
		task.setServer(server);
		task.setNsdId(nsdId);
		return task;
	}
}
