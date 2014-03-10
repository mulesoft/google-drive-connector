/**
 * Mule Google Drive Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.google.drive.model;

import java.util.List;

import org.mule.modules.google.api.model.BaseWrapper;


/**
 * Wrapper for class {@link com.google.api.services.drive.model.About.AdditionalRoleInfo}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class AdditionalRoleInfo extends BaseWrapper<com.google.api.services.drive.model.About.AdditionalRoleInfo> {
	
	public AdditionalRoleInfo() {
		super(new com.google.api.services.drive.model.About.AdditionalRoleInfo());
	}
	
	public AdditionalRoleInfo(com.google.api.services.drive.model.About.AdditionalRoleInfo wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public List<RoleSets> getRoleSets() {
		return RoleSets.valueOf(wrapped.getRoleSets(), RoleSets.class);
	}

	public void setRoleSets(List<RoleSets> roleSets) {
		wrapped.setRoleSets(RoleSets.unwrapp(roleSets, com.google.api.services.drive.model.About.AdditionalRoleInfo.RoleSets.class));
	}

	public String getType() {
		return wrapped.getType();
	}

	public void setType(String type) {
		wrapped.setType(type);
	}
}
