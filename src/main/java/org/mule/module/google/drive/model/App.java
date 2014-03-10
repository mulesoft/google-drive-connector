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
 * A wrapper for clas {@link com.google.api.services.drive.model.App}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class App extends BaseWrapper<com.google.api.services.drive.model.App>{
	
	public App() {
		super(new com.google.api.services.drive.model.App());
	}
	
	public App(com.google.api.services.drive.model.App wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public Boolean getAuthorized() {
		return wrapped.getAuthorized();
	}

	public void setAuthorized(Boolean authorized) {
		wrapped.setAuthorized(authorized);
	}

	public List<Icons> getIcons() {
		return Icons.valueOf(wrapped.getIcons(), Icons.class);
	}

	public void setIcons(List<Icons> icons) {
		wrapped.setIcons(Icons.unwrapp(icons, com.google.api.services.drive.model.App.Icons.class));
	}

	public String getId() {
		return wrapped.getId();
	}

	public void setId(String id) {
		wrapped.setId(id);
	}

	public Boolean getInstalled() {
		return wrapped.getInstalled();
	}

	public void setInstalled(
			Boolean installed) {
		wrapped.setInstalled(installed);
	}

	public String getName() {
		return wrapped.getName();
	}

	public void setName(String name) {
		wrapped.setName(name);
	}

	public String getObjectType() {
		return wrapped.getObjectType();
	}

	public void setObjectType(String objectType) {
		wrapped.setObjectType(objectType);
	}

	public List<String> getPrimaryFileExtensions() {
		return wrapped.getPrimaryFileExtensions();
	}

	public void setPrimaryFileExtensions(List<String> primaryFileExtensions) {
		wrapped.setPrimaryFileExtensions(primaryFileExtensions);
	}

	public List<String> getPrimaryMimeTypes() {
		return wrapped.getPrimaryMimeTypes();
	}

	public void setPrimaryMimeTypes(List<String> primaryMimeTypes) {
		wrapped.setPrimaryMimeTypes(primaryMimeTypes);
	}

	public String getProductUrl() {
		return wrapped.getProductUrl();
	}

	public void setProductUrl(String productUrl) {
		wrapped.setProductUrl(productUrl);
	}

	public List<String> getSecondaryFileExtensions() {
		return wrapped.getSecondaryFileExtensions();
	}

	public void setSecondaryFileExtensions(List<String> secondaryFileExtensions) {
		wrapped.setSecondaryFileExtensions(secondaryFileExtensions);
	}

	public List<String> getSecondaryMimeTypes() {
		return wrapped.getSecondaryMimeTypes();
	}

	public void setSecondaryMimeTypes(List<String> secondaryMimeTypes) {
		wrapped.setSecondaryMimeTypes(secondaryMimeTypes);
	}

	public Boolean getSupportsCreate() {
		return wrapped.getSupportsCreate();
	}

	public void setSupportsCreate(Boolean supportsCreate) {
		wrapped.setSupportsCreate(supportsCreate);
	}

	public Boolean getSupportsImport() {
		return wrapped.getSupportsImport();
	}

	public void setSupportsImport(Boolean supportsImport) {
		wrapped.setSupportsImport(supportsImport);
	}

	public Boolean getUseByDefault() {
		return wrapped.getUseByDefault();
	}

	public void setUseByDefault(Boolean useByDefault) {
		wrapped.setUseByDefault(useByDefault);
	}
}
