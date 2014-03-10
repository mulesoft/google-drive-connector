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

import com.google.api.services.drive.Drive;

/**
 * 
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public interface GoogleDriveClientFactory {
	
	public Drive newClient(String accessToken, String applicationName);

}
