/**
 * Mule Google Drive Cloud Connector
 *
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.google.drive;

import org.mule.modules.google.oauth.invalidation.InvalidationAwareCredential;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;

/**
 * 
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class DefaultGoogleDriveClientFactory implements GoogleDriveClientFactory {

	/**
	 * @see org.mule.module.google.drive.GoogleDriveClientFactory#newClient(java.lang.String, java.lang.String)
	 */
	@Override
	public Drive newClient(String accessToken, String applicationName) {
		Credential credential = new InvalidationAwareCredential(BearerToken.authorizationHeaderAccessMethod());
		credential.setAccessToken(accessToken);
		
		return new Drive.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
						.setApplicationName(applicationName)
						.build();
	}

}
