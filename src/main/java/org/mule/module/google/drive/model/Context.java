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
 * Wrapper for class {@link com.google.api.services.drive.model.Comment.Context} to make it
 * DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class Context extends BaseWrapper<com.google.api.services.drive.model.Comment.Context> {

	public Context() {
		super(new com.google.api.services.drive.model.Comment.Context());
	}
	
	public Context(com.google.api.services.drive.model.Comment.Context wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
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
	
}
