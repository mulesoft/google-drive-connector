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
 * Wrapper for class {@link com.google.api.services.drive.model.File.IndexableText}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class IndexableText extends BaseWrapper<com.google.api.services.drive.model.File.IndexableText> {
	
	public IndexableText() {
		super(new com.google.api.services.drive.model.File.IndexableText());
	}
	
	public IndexableText(com.google.api.services.drive.model.File.IndexableText wrapped) {
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

	public String getText() {
		return wrapped.getText();
	}

	public void setText(String text) {
		wrapped.setText(text);
	}
}
