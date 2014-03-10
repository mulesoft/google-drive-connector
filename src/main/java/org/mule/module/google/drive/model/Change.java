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
 * Wrapper for class {@link com.google.api.services.drive.model.Change}
 * to make it DataMapper friendly
 * 
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class Change extends BaseWrapper<com.google.api.services.drive.model.Change>{

	public Change() {
		super(new com.google.api.services.drive.model.Change());
	}
	
	public Change(com.google.api.services.drive.model.Change wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public Boolean getDeleted() {
		return wrapped.getDeleted();
	}

	public void setDeleted(Boolean deleted) {
		wrapped.setDeleted(deleted);
	}

	public File getFile() {
		return new File(wrapped.getFile());
	}

	public void setFile(File file) {
		wrapped.setFile(file.wrapped());
	}

	public String getFileId() {
		return wrapped.getFileId();
	}

	public void setFileId(String fileId) {
		wrapped.setFileId(fileId);
	}

	public Long getId() {
		return wrapped.getId();
	}

	public void setId(Long id) {
		wrapped.setId(id);
	}

}
