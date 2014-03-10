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

/**
 * Wrapper for class {@link com.google.api.services.drive.model.ParentReference}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class ParentReference extends BaseWrapper<com.google.api.services.drive.model.ParentReference> {
	
	public ParentReference() {
		super(new com.google.api.services.drive.model.ParentReference());
	}
	
	public ParentReference(com.google.api.services.drive.model.ParentReference wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public String getId() {
		return wrapped.getId();
	}

	public void setId(String id) {
		wrapped.setId(id);
	}

	public Boolean getIsRoot() {
		return wrapped.getIsRoot();
	}

	public void setIsRoot(Boolean isRoot) {
		wrapped.setIsRoot(isRoot);
	}

	public String getParentLink() {
		return wrapped.getParentLink();
	}

	public void setParentLink(String parentLink) {
		wrapped.setParentLink(parentLink);
	}

	public String getSelfLink() {
		return wrapped.getSelfLink();
	}

	public void setSelfLink(String selfLink) {
		wrapped.setSelfLink(selfLink);
	}

	public final Map<String, Object> getUnknownKeys() {
		return wrapped.getUnknownKeys();
	}

	public final void setUnknownKeys(Map<String, Object> unknownFields) {
		wrapped.setUnknownKeys(unknownFields);
	}
	
	

}
