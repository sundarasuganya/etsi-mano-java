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
package com.ubiqube.parser.tosca;

import com.ubiqube.parser.tosca.convert.TimeConverter;
import com.ubiqube.parser.tosca.scalar.Time;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class OrikaTimeConverter extends BidirectionalConverter<Time, Time> {
	private final TimeConverter conv = new TimeConverter();

	@Override
	public Object convert(final Object source, final Type<? extends Object> destinationType, final MappingContext mappingContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time convertTo(final Time source, final Type<Time> destinationType, final MappingContext mappingContext) {
		return conv.convert(source.toString());
	}

	@Override
	public Time convertFrom(final Time source, final Type<Time> destinationType, final MappingContext mappingContext) {
		return conv.convert(source.toString());
	}

}
