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

import org.mule.modules.google.api.model.BaseWrapper;

/**
 * Wrapper for class {@link com.google.api.services.drive.model.About.MaxUploadSizes}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class MaxUploadSizes extends BaseWrapper<com.google.api.services.drive.model.About.MaxUploadSizes> {
	
	public MaxUploadSizes() {
		super(new com.google.api.services.drive.model.About.MaxUploadSizes());
	}
	
	public MaxUploadSizes(com.google.api.services.drive.model.About.MaxUploadSizes wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public Long getSize() {
		return wrapped.getSize();
	}

	public void setSize(Long size) {
		wrapped.setSize(size);
	}

	public String getType() {
		return wrapped.getType();
	}

	public void setType(String type) {
		wrapped.setType(type);
	}
	
	

}
