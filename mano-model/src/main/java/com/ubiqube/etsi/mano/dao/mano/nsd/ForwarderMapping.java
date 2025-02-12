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
package com.ubiqube.etsi.mano.dao.mano.nsd;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.ubiqube.etsi.mano.utils.ToStringUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ForwarderMapping implements Serializable {

	/** Serial. */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private String vduName;

	private String forwardingName;

	private String vlName;

	private int vlId;

	private String virtualLinkPort;

	public ForwarderMapping(final String vduName, final int vlId, final String forwardingName, final String vlName, final String virtualLinkPort) {
		this.vduName = vduName;
		this.vlId = vlId;
		this.forwardingName = forwardingName;
		this.vlName = vlName;
		this.virtualLinkPort = virtualLinkPort;
	}

	@Override
	public String toString() {
		return ToStringUtil.toString(this);
	}
}
