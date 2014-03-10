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
import java.util.Map;

import org.mule.modules.google.api.datetime.DateTime;
import org.mule.modules.google.api.model.BaseWrapper;

/**
 * Wrapper for class {@link com.google.api.services.drive.model.File}
 * to make it DataMapper friendly
 * 
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class File extends BaseWrapper<com.google.api.services.drive.model.File> {
	
	public File() {
		super(new com.google.api.services.drive.model.File());
	}
	
	public File(com.google.api.services.drive.model.File wrapped) {
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

	public String getAlternateLink() {
		return wrapped.getAlternateLink();
	}

	public void setAlternateLink(String alternateLink) {
		wrapped.setAlternateLink(alternateLink);
	}

	public DateTime getCreatedDate() {
		return new DateTime(wrapped.getCreatedDate());
	}

	public void setCreatedDate(DateTime createdDate) {
		wrapped.setCreatedDate(createdDate.getWrapped());
	}

	public String getDescription() {
		return wrapped.getDescription();
	}

	public void setDescription(String description) {
		wrapped.setDescription(description);
	}

	public String getDownloadUrl() {
		return wrapped.getDownloadUrl();
	}

	public void setDownloadUrl(String downloadUrl) {
		wrapped.setDownloadUrl(downloadUrl);
	}

	public Boolean getEditable() {
		return wrapped.getEditable();
	}

	public void setEditable(Boolean editable) {
		wrapped.setEditable(editable);
	}

	public String getEmbedLink() {
		return wrapped.getEmbedLink();
	}

	public void setEmbedLink(String embedLink) {
		wrapped.setEmbedLink(embedLink);
	}

	public String getEtag() {
		return wrapped.getEtag();
	}

	public void setEtag(String etag) {
		wrapped.setEtag(etag);
	}

	public Boolean getExplicitlyTrashed() {
		return wrapped.getExplicitlyTrashed();
	}

	public void setExplicitlyTrashed(Boolean explicitlyTrashed) {
		wrapped.setExplicitlyTrashed(explicitlyTrashed);
	}

	public Map<String, String> getExportLinks() {
		return wrapped.getExportLinks();
	}

	public void setExportLinks(Map<String, String> exportLinks) {
		wrapped.setExportLinks(exportLinks);
	}

	public String getFileExtension() {
		return wrapped.getFileExtension();
	}

	public void setFileExtension(String fileExtension) {
		wrapped.setFileExtension(fileExtension);
	}

	public Long getFileSize() {
		return wrapped.getFileSize();
	}

	public void setFileSize(Long fileSize) {
		wrapped.setFileSize(fileSize);
	}

	public String getIconLink() {
		return wrapped.getIconLink();
	}

	public void setIconLink(String iconLink) {
		wrapped.setIconLink(iconLink);
	}

	public String getId() {
		return wrapped.getId();
	}

	public void setId(String id) {
		wrapped.setId(id);
	}

	public ImageMediaMetadata getImageMediaMetadata() {
		return new ImageMediaMetadata(wrapped.getImageMediaMetadata());
	}

	public void setImageMediaMetadata(ImageMediaMetadata imageMediaMetadata) {
		wrapped.setImageMediaMetadata(imageMediaMetadata.wrapped());
	}

	public IndexableText getIndexableText() {
		return new IndexableText(wrapped.getIndexableText());
	}

	public void setIndexableText(IndexableText indexableText) {
		wrapped.setIndexableText(indexableText.wrapped());
	}

	public Labels getLabels() {
		return new Labels(wrapped.getLabels());
	}

	public void setLabels(Labels labels) {
		wrapped.setLabels(labels.wrapped());
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

	public DateTime getLastViewedByMeDate() {
		return new DateTime(wrapped.getLastViewedByMeDate());
	}

	public void setLastViewedByMeDate(DateTime lastViewedByMeDate) {
		wrapped.setLastViewedByMeDate(lastViewedByMeDate.getWrapped());
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

	public DateTime getModifiedByMeDate() {
		return new DateTime(wrapped.getModifiedByMeDate());
	}

	public void setModifiedByMeDate(DateTime modifiedByMeDate) {
		wrapped.setModifiedByMeDate(modifiedByMeDate.getWrapped());
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

	public List<String> getOwnerNames() {
		return wrapped.getOwnerNames();
	}

	public void setOwnerNames(List<String> ownerNames) {
		wrapped.setOwnerNames(ownerNames);
	}

	public List<User> getOwners() {
		return User.valueOf(wrapped.getOwners(), User.class);
	}

	public void setOwners(List<User> owners) {
		wrapped.setOwners(User.unwrapp(owners, com.google.api.services.drive.model.User.class));
	}

	public List<ParentReference> getParents() {
		return ParentReference.valueOf(wrapped.getParents(), ParentReference.class);
	}

	public void setParents(List<ParentReference> parents) {
		wrapped.setParents(ParentReference.unwrapp(parents, com.google.api.services.drive.model.ParentReference.class));
	}

	public Long getQuotaBytesUsed() {
		return wrapped.getQuotaBytesUsed();
	}

	public void setQuotaBytesUsed(Long quotaBytesUsed) {
		wrapped.setQuotaBytesUsed(quotaBytesUsed);
	}

	public Boolean getShared() {
		return wrapped.getShared();
	}

	public void setShared(Boolean shared) {
		wrapped.setShared(shared);
	}

	public DateTime getSharedWithMeDate() {
		return new DateTime(wrapped.getSharedWithMeDate());
	}

	public void setSharedWithMeDate(DateTime sharedWithMeDate) {
		wrapped.setSharedWithMeDate(sharedWithMeDate.getWrapped());
	}

	public Thumbnail getThumbnail() {
		return new Thumbnail(wrapped.getThumbnail());
	}

	public void setThumbnail(Thumbnail thumbnail) {
		wrapped.setThumbnail(thumbnail.wrapped());
	}

	public String getThumbnailLink() {
		return wrapped.getThumbnailLink();
	}

	public void setThumbnailLink(String thumbnailLink) {
		wrapped.setThumbnailLink(thumbnailLink);
	}

	public String getTitle() {
		return wrapped.getTitle();
	}

	public void setTitle(String title) {
		wrapped.setTitle(title);
	}

	public Permission getUserPermission() {
		return new Permission(wrapped.getUserPermission());
	}

	public void setUserPermission(Permission userPermission) {
		wrapped.setUserPermission(userPermission.wrapped());
	}

	public String getWebContentLink() {
		return wrapped.getWebContentLink();
	}

	public void setWebContentLink(String webContentLink) {
		wrapped.setWebContentLink(webContentLink);
	}

	public String getWebViewLink() {
		return wrapped.getWebViewLink();
	}

	public void setWebViewLink(String webViewLink) {
		wrapped.setWebViewLink(webViewLink);
	}

	public Boolean getWritersCanShare() {
		return wrapped.getWritersCanShare();
	}

	public void setWritersCanShare(Boolean writersCanShare) {
		wrapped.setWritersCanShare(writersCanShare);
	}
}
