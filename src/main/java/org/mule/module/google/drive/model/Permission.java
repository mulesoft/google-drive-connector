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
import java.util.Map;

import org.mule.modules.google.api.model.BaseWrapper;

/**
 * Wrapper for class {@link com.google.api.services.drive.model.Permission}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class Permission extends BaseWrapper<com.google.api.services.drive.model.Permission>{

	public Permission() {
		super(new com.google.api.services.drive.model.Permission());
	}
	
	public Permission(com.google.api.services.drive.model.Permission wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public List<String> getAdditionalRoles() {
		return wrapped.getAdditionalRoles();
	}

	public void setAdditionalRoles(List<String> additionalRoles) {
		wrapped.setAdditionalRoles(additionalRoles);
	}

	public String getAuthKey() {
		return wrapped.getAuthKey();
	}

	public void setAuthKey(String authKey) {
		wrapped.setAuthKey(authKey);
	}

	public String getEtag() {
		return wrapped.getEtag();
	}

	public void setEtag(String etag) {
		wrapped.setEtag(etag);
	}

	public String getId() {
		return wrapped.getId();
	}

	public void setId(String id) {
		wrapped.setId(id);
	}

	public final Map<String, Object> getUnknownKeys() {
		return wrapped.getUnknownKeys();
	}

	public String getName() {
		return wrapped.getName();
	}

	public final void setUnknownKeys(Map<String, Object> unknownFields) {
		wrapped.setUnknownKeys(unknownFields);
	}

	public void setName(String name) {
		wrapped.setName(name);
	}

	public String getPhotoLink() {
		return wrapped.getPhotoLink();
	}

	public void setPhotoLink(String photoLink) {
		wrapped.setPhotoLink(photoLink);
	}

	public String getRole() {
		return wrapped.getRole();
	}

	public void setRole(String role) {
		wrapped.setRole(role);
	}

	public String getSelfLink() {
		return wrapped.getSelfLink();
	}

	public void setSelfLink(String selfLink) {
		wrapped.setSelfLink(selfLink);
	}

	public String getType() {
		return wrapped.getType();
	}

	public void setType(String type) {
		wrapped.setType(type);
	}

	public String getValue() {
		return wrapped.getValue();
	}

	public void setValue(String value) {
		wrapped.setValue(value);
	}

	public Boolean getWithLink() {
		return wrapped.getWithLink();
	}

	public void setWithLink(Boolean withLink) {
		wrapped.setWithLink(withLink);
	}
	
	
}
