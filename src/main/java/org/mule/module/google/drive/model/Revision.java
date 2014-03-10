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

import org.mule.modules.google.api.datetime.DateTime;
import org.mule.modules.google.api.model.BaseWrapper;

/**
 * Wrapper for class {@link com.google.api.services.drive.model.Revision}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class Revision extends BaseWrapper<com.google.api.services.drive.model.Revision> {
	
	public Revision() {
		super(new com.google.api.services.drive.model.Revision());
	}
	
	public Revision(com.google.api.services.drive.model.Revision wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public String getDownloadUrl() {
		return wrapped.getDownloadUrl();
	}

	public void setDownloadUrl(String downloadUrl) {
		wrapped.setDownloadUrl(downloadUrl);
	}

	public String getEtag() {
		return wrapped.getEtag();
	}

	public void setEtag(String etag) {
		wrapped.setEtag(etag);
	}

	public Map<String, String> getExportLinks() {
		return wrapped.getExportLinks();
	}

	public void setExportLinks(Map<String, String> exportLinks) {
		wrapped.setExportLinks(exportLinks);
	}

	public Long getFileSize() {
		return wrapped.getFileSize();
	}

	public void setFileSize(Long fileSize) {
		wrapped.setFileSize(fileSize);
	}

	public String getId() {
		return wrapped.getId();
	}

	public void setId(String id) {
		wrapped.setId(id);
	}

	public User getLastModifyingUser() {
		return new User(wrapped.getLastModifyingUser());
	}

	public void setLastModifyingUser(User lastModifyingUser) {
		wrapped.setLastModifyingUser(lastModifyingUser.wrapped());
	}

	public String getLastModifyingUserName() {
		return wrapped.getLastModifyingUserName();
	}

	public void setLastModifyingUserName(String lastModifyingUserName) {
		wrapped.setLastModifyingUserName(lastModifyingUserName);
	}

	public String getMd5Checksum() {
		return wrapped.getMd5Checksum();
	}

	public void setMd5Checksum(String md5Checksum) {
		wrapped.setMd5Checksum(md5Checksum);
	}

	public String getMimeType() {
		return wrapped.getMimeType();
	}

	public void setMimeType(String mimeType) {
		wrapped.setMimeType(mimeType);
	}

	public DateTime getModifiedDate() {
		return new DateTime(wrapped.getModifiedDate());
	}

	public void setModifiedDate(DateTime modifiedDate) {
		wrapped.setModifiedDate(modifiedDate.getWrapped());
	}

	public String getOriginalFilename() {
		return wrapped.getOriginalFilename();
	}

	public void setOriginalFilename(String originalFilename) {
		wrapped.setOriginalFilename(originalFilename);
	}

	public Boolean getPinned() {
		return wrapped.getPinned();
	}

	public void setPinned(Boolean pinned) {
		wrapped.setPinned(pinned);
	}

	public Boolean getPublishAuto() {
		return wrapped.getPublishAuto();
	}

	public void setPublishAuto(Boolean publishAuto) {
		wrapped.setPublishAuto(publishAuto);
	}

	public Boolean getPublished() {
		return wrapped.getPublished();
	}

	public void setPublished(Boolean published) {
		wrapped.setPublished(published);
	}

	public String getPublishedLink() {
		return wrapped.getPublishedLink();
	}

	public void setPublishedLink(String publishedLink) {
		wrapped.setPublishedLink(publishedLink);
	}

	public Boolean getPublishedOutsideDomain() {
		return wrapped.getPublishedOutsideDomain();
	}

	public void setPublishedOutsideDomain(Boolean publishedOutsideDomain) {
		wrapped.setPublishedOutsideDomain(publishedOutsideDomain);
	}
}
