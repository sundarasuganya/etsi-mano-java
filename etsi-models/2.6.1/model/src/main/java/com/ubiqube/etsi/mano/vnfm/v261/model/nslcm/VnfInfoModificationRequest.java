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

/*
 * SOL003 - VNF Lifecycle Management interface
 * SOL003 - VNF Lifecycle Management interface definition  IMPORTANT: Please note that this file might be not aligned to the current version of the ETSI Group Specification it refers to. In case of discrepancies the published ETSI Group Specification takes precedence.  In clause 4.3.2 of ETSI GS NFV-SOL 003 v2.4.1, an attribute-based filtering mechanism is defined. This mechanism is currently not included in the corresponding OpenAPI design for this GS version. Changes to the attribute-based filtering mechanism are being considered in v2.5.1 of this GS for inclusion in the corresponding future ETSI NFV OpenAPI design. Please report bugs to https://forge.etsi.org/bugzilla/buglist.cgi?component=Nfv-Openapis&list_id=61&product=NFV&resolution=
 *
 * OpenAPI spec version: 1.1.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.ubiqube.etsi.mano.vnfm.v261.model.nslcm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ubiqube.etsi.mano.common.v261.model.VimConnectionInfo;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * This type represents attribute modifications for an \&quot;Individual VNF
 * instance\&quot; resource, i.e. modifications to a resource representation
 * based on the \&quot;VnfInstance\&quot; data type. The attributes of
 * \&quot;VnfInstance\&quot; that can be modified according to the provisions in
 * clause 5.5.2.2 are included in the \&quot;VnfInfoModificationRequest\&quot;
 * data type. The \&quot;VnfInfoModificationRequest\&quot; data type shall
 * comply with the provisions defined in table 5.5.2.12-1.
 */
@Schema(description = "This type represents attribute modifications for an \"Individual VNF instance\" resource, i.e. modifications to a resource representation based on the \"VnfInstance\" data type. The attributes of \"VnfInstance\" that can be modified according to the provisions in clause 5.5.2.2 are included in the \"VnfInfoModificationRequest\" data type. The \"VnfInfoModificationRequest\" data type shall comply with the provisions defined in table 5.5.2.12-1. ")
@Validated
public class VnfInfoModificationRequest {
	@JsonProperty("vnfInstanceName")
	private String vnfInstanceName = null;

	@JsonProperty("vnfInstanceDescription")
	private String vnfInstanceDescription = null;

	@JsonProperty("vnfPkgId")
	private String vnfPkgId = null;

	@JsonProperty("vnfConfigurableProperties")
	private Map<String, String> vnfConfigurableProperties = null;

	@JsonProperty("metadata")
	private Map<String, String> metadata = null;

	@JsonProperty("extensions")
	private Map<String, String> extensions = null;

	@JsonProperty("vimConnectionInfo")
	private List<VimConnectionInfo> vimConnectionInfo = null;

	public VnfInfoModificationRequest vnfInstanceName(final String vnfInstanceName) {
		this.vnfInstanceName = vnfInstanceName;
		return this;
	}

	/**
	 * New value of the \"vnfInstanceName\" attribute in \"VnfInstance\", or
	 * \"null\" to remove the attribute.
	 *
	 * @return vnfInstanceName
	 **/
	@Schema(description = "New value of the \"vnfInstanceName\" attribute in \"VnfInstance\", or \"null\" to remove the attribute.       ")
	public String getVnfInstanceName() {
		return vnfInstanceName;
	}

	public void setVnfInstanceName(final String vnfInstanceName) {
		this.vnfInstanceName = vnfInstanceName;
	}

	public VnfInfoModificationRequest vnfInstanceDescription(final String vnfInstanceDescription) {
		this.vnfInstanceDescription = vnfInstanceDescription;
		return this;
	}

	/**
	 * New value of the \"vnfInstanceDescription\" attribute in \"VnfInstance\", or
	 * \"null\" to remove the attribute.
	 *
	 * @return vnfInstanceDescription
	 **/
	@Schema(description = "New value of the \"vnfInstanceDescription\" attribute in \"VnfInstance\", or \"null\" to remove the attribute.   ")
	public String getVnfInstanceDescription() {
		return vnfInstanceDescription;
	}

	public void setVnfInstanceDescription(final String vnfInstanceDescription) {
		this.vnfInstanceDescription = vnfInstanceDescription;
	}

	public VnfInfoModificationRequest vnfPkgId(final String vnfPkgId) {
		this.vnfPkgId = vnfPkgId;
		return this;
	}

	/**
	 * New value of the \&quot;vnfPkgId\&quot; attribute in
	 * \&quot;VnfInstance\&quot;. The value \&quot;null\&quot; is not permitted.
	 *
	 * @return vnfPkgId
	 **/
	@JsonProperty("vnfPkgId")
	@Schema(description = "New value of the \"vnfPkgId\" attribute in \"VnfInstance\". The value \"null\" is not permitted.   ")
	public String getVnfPkgId() {
		return vnfPkgId;
	}

	public void setVnfPkgId(final String vnfPkgId) {
		this.vnfPkgId = vnfPkgId;
	}

	public VnfInfoModificationRequest vnfConfigurableProperties(final Map<String, String> vnfConfigurableProperties) {
		this.vnfConfigurableProperties = vnfConfigurableProperties;
		return this;
	}

	/**
	 * Modifications of the \&quot;vnfConfigurableProperties\&quot; attribute in
	 * \&quot;VnfInstance\&quot;. If present, these modifications shall be applied
	 * according to the rules of JSON Merge PATCH (see IETF RFC 7396).
	 *
	 * @return vnfConfigurableProperties
	 **/
	@JsonProperty("vnfConfigurableProperties")
	@Schema(description = "Modifications of the \"vnfConfigurableProperties\" attribute in \"VnfInstance\". If present, these modifications shall be applied according to the rules of JSON Merge PATCH (see IETF RFC 7396). ")
	public Map<String, String> getVnfConfigurableProperties() {
		return vnfConfigurableProperties;
	}

	public void setVnfConfigurableProperties(final Map<String, String> vnfConfigurableProperties) {
		this.vnfConfigurableProperties = vnfConfigurableProperties;
	}

	public VnfInfoModificationRequest metadata(final Map<String, String> metadata) {
		this.metadata = metadata;
		return this;
	}

	/**
	 * Modifications of the \&quot;metadata\&quot; attribute in
	 * \&quot;VnfInstance\&quot;. If present, these modifications shall be applied
	 * according to the rules of JSON Merge PATCH (see IETF RFC 7396).
	 *
	 * @return metadata
	 **/
	@JsonProperty("metadata")
	@Schema(description = "Modifications of the \"metadata\" attribute in \"VnfInstance\". If present, these modifications shall be applied according to the rules of JSON Merge PATCH (see IETF RFC 7396). ")
	public Map<String, String> getMetadata() {
		return metadata;
	}

	public void setMetadata(final Map<String, String> metadata) {
		this.metadata = metadata;
	}

	public VnfInfoModificationRequest extensions(final Map<String, String> extensions) {
		this.extensions = extensions;
		return this;
	}

	/**
	 * Modifications of the \&quot;extensions\&quot; attribute in
	 * \&quot;VnfInstance\&quot;. If present, these modifications shall be applied
	 * according to the rules of JSON Merge PATCH (see IETF RFC 7396).
	 *
	 * @return extensions
	 **/
	@JsonProperty("extensions")
	@Schema(description = "Modifications of the \"extensions\" attribute in \"VnfInstance\". If present, these modifications shall be applied according to the rules of JSON Merge PATCH (see IETF RFC 7396). ")
	public Map<String, String> getExtensions() {
		return extensions;
	}

	public void setExtensions(final Map<String, String> extensions) {
		this.extensions = extensions;
	}

	public VnfInfoModificationRequest vimConnectionInfo(final List<VimConnectionInfo> vimConnectionInfo) {
		this.vimConnectionInfo = vimConnectionInfo;
		return this;
	}

	public VnfInfoModificationRequest addVimConnectionInfoItem(final VimConnectionInfo vimConnectionInfoItem) {
		if (this.vimConnectionInfo == null) {
			this.vimConnectionInfo = new ArrayList<>();
		}
		this.vimConnectionInfo.add(vimConnectionInfoItem);
		return this;
	}

	/**
	 * New content of certain entries in the \&quot;vimConnectionInfo\&quot;
	 * attribute array in \&quot;VnfInstance\&quot;, as defined below this table.
	 *
	 * @return vimConnectionInfo
	 **/
	@JsonProperty("vimConnectionInfo")
	@Schema(description = "New content of certain entries in the \"vimConnectionInfo\" attribute array in \"VnfInstance\", as defined below this table. ")
	public List<VimConnectionInfo> getVimConnectionInfo() {
		return vimConnectionInfo;
	}

	public void setVimConnectionInfo(final List<VimConnectionInfo> vimConnectionInfo) {
		this.vimConnectionInfo = vimConnectionInfo;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("class VnfInfoModificationRequest {\n");

		sb.append("    vnfInstanceName: ").append(toIndentedString(vnfInstanceName)).append("\n");
		sb.append("    vnfInstanceDescription: ").append(toIndentedString(vnfInstanceDescription)).append("\n");
		sb.append("    vnfPkgId: ").append(toIndentedString(vnfPkgId)).append("\n");
		sb.append("    vnfConfigurableProperties: ").append(toIndentedString(vnfConfigurableProperties)).append("\n");
		sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
		sb.append("    extensions: ").append(toIndentedString(extensions)).append("\n");
		sb.append("    vimConnectionInfo: ").append(toIndentedString(vimConnectionInfo)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(final java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}
}
