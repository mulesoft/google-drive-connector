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
 * Wrapper for class {@link com.google.api.services.drive.model.File.Labels}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 */
public class Labels extends BaseWrapper<com.google.api.services.drive.model.File.Labels> {
	
	public Labels() {
		super(new com.google.api.services.drive.model.File.Labels());
	}
	
	public Labels(com.google.api.services.drive.model.File.Labels wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public final Map<String, Object> getUnknownKeys() {
		return wrapped.getUnknownKeys();
	}

	public final void setUnknownKeys(Map<String, Object> unknownFields) {
		wrapped.setUnknownKeys(unknownFields);
	}

	public Boolean getHidden() {
		return wrapped.getHidden();
	}

	public void setHidden(Boolean hidden) {
		wrapped.setHidden(hidden);
	}

	public Boolean getRestricted() {
		return wrapped.getRestricted();
	}

	public void setRestricted(Boolean restricted) {
		wrapped.setRestricted(restricted);
	}

	public Boolean getStarred() {
		return wrapped.getStarred();
	}

	public void setStarred(Boolean starred) {
		wrapped.setStarred(starred);
	}

	public Boolean getTrashed() {
		return wrapped.getTrashed();
	}

	public void setTrashed(Boolean trashed) {
		wrapped.setTrashed(trashed);
	}

	public Boolean getViewed() {
		return wrapped.getViewed();
	}

	public void setViewed(Boolean viewed) {
		wrapped.setViewed(viewed);
	}
}
