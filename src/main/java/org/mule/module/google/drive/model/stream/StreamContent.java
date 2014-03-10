/**
 * Mule Google Drive Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.google.drive.model.stream;

import java.io.IOException;
import java.io.InputStream;

import com.google.api.client.http.AbstractInputStreamContent;

/**
 * Implementation of {@link com.google.api.client.http.AbstractInputStreamContent} that accepts input streams
 * 
 * @see com.google.api.client.http.AbstractInputStreamContent
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class StreamContent extends AbstractInputStreamContent {

	private InputStream in;
	
	public StreamContent(String type, InputStream in) {
		super(type);
		this.in = in;
	}
	
	@Override
	public long getLength() throws IOException {
		return this.in.available();
	}

	@Override
	public boolean retrySupported() {
		return true;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.in;
	}

}
