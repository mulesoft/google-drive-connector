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

import java.util.Map;

import org.mule.modules.google.api.model.BaseWrapper;

import com.google.api.services.drive.model.User.Picture;


/**
 * Wrapper for class {@link com.google.api.services.drive.model.User}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class User extends BaseWrapper<com.google.api.services.drive.model.User>{

	public User() {
		super(new com.google.api.services.drive.model.User());
	}
	
	public User(com.google.api.services.drive.model.User wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public String getDisplayName() {
		return wrapped.getDisplayName();
	}

	public void setDisplayName(String displayName) {
		wrapped.setDisplayName(displayName);
	}

	public Boolean getIsAuthenticatedUser() {
		return wrapped.getIsAuthenticatedUser();
	}

	public void setIsAuthenticatedUser(Boolean isAuthenticatedUser) {
		wrapped.setIsAuthenticatedUser(isAuthenticatedUser);
	}

	public String getPermissionId() {
		return wrapped.getPermissionId();
	}

	public void setPermissionId(String permissionId) {
		wrapped.setPermissionId(permissionId);
	}

	public Picture getPicture() {
		return wrapped.getPicture();
	}

	public void setPicture(Picture picture) {
		wrapped.setPicture(picture);
	}

	public final Map<String, Object> getUnknownKeys() {
		return wrapped.getUnknownKeys();
	}

	public final void setUnknownKeys(Map<String, Object> unknownFields) {
		wrapped.setUnknownKeys(unknownFields);
	}
}
