package com.ubiqube.etsi.mano.utils;

/**
 * This class allows to handle the range from the HTTP request header.
 */
public class RangeHeader {

	/**
	 * The unit of the range.
	 */
	public enum Unit {
		BYTES;
	}

	private final Unit unit;

	private final int from;

	private Integer to = null;

	/**
	 * RangeHeader class constructor.
	 *
	 * @param unit the unit of the range (e.g: BYTES)
	 * @param from the start value of the range
	 * @param to   the end value of the range
	 */
	public RangeHeader(final Unit _unit, final int _from, final Integer _to) {
		this.unit = _unit;
		this.from = _from;
		this.to = _to;
	}

	public RangeHeader(final String range) {
		final String[] tokens = range.replace("Range: ", "").split("=");
		unit = Unit.valueOf(tokens[0].toUpperCase());
		final String[] fromTo = tokens[1].split("-");
		from = Integer.parseInt(fromTo[0]);
		if (fromTo.length > 1) {
			to = Integer.decode(fromTo[1]);
		}
	}

	/**
	 * Get range unit.
	 *
	 * @return the unit of the range (e.g: BYTES)
	 */
	public Unit getUnit() {

		return unit;
	}

	/**
	 * Get the start value of range.
	 *
	 * @return the start value of range.
	 */
	public int getFrom() {

		return from;
	}

	/**
	 * Get the end value of the range.
	 *
	 * @return the end value the range.
	 *
	 */
	public Integer getTo() {
		return to;
	}

	/**
	 * Get the RangeHeader object instance.
	 *
	 * @param range the value of
	 * @return the RangeHeader object instantiated with range.
	 */
	public static RangeHeader fromValue(final String range) {
		if (range == null) {
			return null;
		}
		final String[] tokens = range.replace("Range: ", "").split("=");
		final Unit unit = Unit.valueOf(tokens[0].toUpperCase());
		final String[] fromTo = tokens[1].split("-");
		final int from = Integer.parseInt(fromTo[0]);
		final Integer to = Integer.decode(fromTo[1]);
		return new RangeHeader(unit, from, to);
	}

	/**
	 * Get the String value of the RangeHeader object.
	 *
	 * @return the string value of the RangeHeader object
	 */
	@Override
	public String toString() {
		return String.format("Range: %s=%d-%d", unit.name().toLowerCase(), from, to);
	}

	public String getContentRange(final int length) {
		final StringBuilder sb = new StringBuilder("bytes ");
		sb.append(from).append('-').append(to);
		sb.append('/').append(length);
		return sb.toString();
	}

}
