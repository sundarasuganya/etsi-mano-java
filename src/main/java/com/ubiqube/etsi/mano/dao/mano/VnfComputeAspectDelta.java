package com.ubiqube.etsi.mano.dao.mano;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class VnfComputeAspectDelta implements Serializable {
	/** Serial. */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private String aspectName;

	private String deltaName;

	private Integer numberOfInstances;

	@ManyToOne
	private VnfCompute vnfCompute;

	private int level;

	public VnfComputeAspectDelta() {
		// Nothing.
	}

	public VnfComputeAspectDelta(final String aspectName, final String deltaName, final Integer numberOfInstances, final int _level) {
		super();
		this.aspectName = aspectName;
		this.deltaName = deltaName;
		this.numberOfInstances = numberOfInstances;
		level = _level;
	}

	public UUID getId() {
		return id;
	}

	public void setId(final UUID id) {
		this.id = id;
	}

	public String getAspectName() {
		return aspectName;
	}

	public void setAspectName(final String aspectName) {
		this.aspectName = aspectName;
	}

	public String getDeltaName() {
		return deltaName;
	}

	public void setDeltaName(final String deltaName) {
		this.deltaName = deltaName;
	}

	public Integer getNumberOfInstances() {
		return numberOfInstances;
	}

	public void setNumberOfInstances(final Integer numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
	}

	public VnfCompute getVnfCompute() {
		return vnfCompute;
	}

	public void setVnfCompute(final VnfCompute vnfCompute) {
		this.vnfCompute = vnfCompute;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

}
