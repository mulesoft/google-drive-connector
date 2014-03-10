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
 * Wrapper for class {@link com.google.api.services.drive.model.About.ExportFormats}
 * to make it DataMapper friendly
 * 
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class ExportFormats extends BaseWrapper<com.google.api.services.drive.model.About.ExportFormats> {
	
	public ExportFormats() {
		super(new com.google.api.services.drive.model.About.ExportFormats());
	}
	
	public ExportFormats(com.google.api.services.drive.model.About.ExportFormats wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public String getSource() {
		return wrapped.getSource();
	}

	public void setSource(String source) {
		wrapped.setSource(source);
	}

	public List<String> getTargets() {
		return wrapped.getTargets();
	}

	public void setTargets(List<String> targets) {
		wrapped.setTargets(targets);
	}
	
	

}
