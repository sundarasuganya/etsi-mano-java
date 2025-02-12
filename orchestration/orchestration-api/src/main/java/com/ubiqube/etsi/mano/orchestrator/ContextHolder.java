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
package com.ubiqube.etsi.mano.orchestrator;

import java.util.UUID;

import com.ubiqube.etsi.mano.orchestrator.nodes.Node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author olivier
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContextHolder {
	/**
	 * Live instance ID
	 */
	private UUID liveInstanceId;

	private Class<? extends Node> type;

	private String name;

	private int rank;

	private String resourceId;

	private String vimConnectionId;

}
