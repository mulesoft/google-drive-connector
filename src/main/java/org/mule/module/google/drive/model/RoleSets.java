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
 * Wrapper for class {@link com.google.api.services.drive.model.About.AdditionalRoleInfo.RoleSets}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class RoleSets extends BaseWrapper<com.google.api.services.drive.model.About.AdditionalRoleInfo.RoleSets>{

	public RoleSets() {
		super(new com.google.api.services.drive.model.About.AdditionalRoleInfo.RoleSets());
	}
	
	public RoleSets(com.google.api.services.drive.model.About.AdditionalRoleInfo.RoleSets wrapped) {
		super(wrapped);
	}

	public List<String> getAdditionalRoles() {
		return wrapped.getAdditionalRoles();
	}

	public String getPrimaryRole() {
		return wrapped.getPrimaryRole();
	}

	public void setAdditionalRoles(List<String> additionalRoles) {
		wrapped.setAdditionalRoles(additionalRoles);
	}

	public void setPrimaryRole(String primaryRole) {
		wrapped.setPrimaryRole(primaryRole);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}
}
