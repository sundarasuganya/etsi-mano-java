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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import com.ubiqube.etsi.mano.dao.mano.VnfVl;

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
public class DnsZoneTask extends VnfTask {

	/** Serial. */
	private static final long serialVersionUID = 1L;

	private String domainName;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	private VnfVl vnfVl;

	@Override
	public VnfTask copy() {
		final DnsZoneTask t = new DnsZoneTask();
		super.copy(t);
		t.setDomainName(domainName);
		t.setVnfVl(vnfVl);
		return t;
	}

}
