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
 * Wrapper for class {@link com.google.api.services.drive.model.About}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class About extends BaseWrapper<com.google.api.services.drive.model.About> {

	public About() {
		super(new com.google.api.services.drive.model.About());
	}
	
	public About(com.google.api.services.drive.model.About wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public List<AdditionalRoleInfo> getAdditionalRoleInfo() {
		return AdditionalRoleInfo.valueOf(wrapped.getAdditionalRoleInfo(), AdditionalRoleInfo.class);
	}

	public void setAdditionalRoleInfo(List<AdditionalRoleInfo> additionalRoleInfo) {
		wrapped.setAdditionalRoleInfo(AdditionalRoleInfo.unwrapp(additionalRoleInfo, com.google.api.services.drive.model.About.AdditionalRoleInfo.class));
	}

	public String getDomainSharingPolicy() {
		return wrapped.getDomainSharingPolicy();
	}

	public void setDomainSharingPolicy(String domainSharingPolicy) {
		wrapped.setDomainSharingPolicy(domainSharingPolicy);
	}

	public String getEtag() {
		return wrapped.getEtag();
	}

	public void setEtag(String etag) {
		wrapped.setEtag(etag);
	}

	public List<ExportFormats> getExportFormats() {
		return ExportFormats.valueOf(wrapped.getExportFormats(), ExportFormats.class);
	}

	public void setExportFormats(List<ExportFormats> exportFormats) {
		wrapped.setExportFormats(ExportFormats.unwrapp(exportFormats, com.google.api.services.drive.model.About.ExportFormats.class));
	}

	public List<ImportFormats> getImportFormats() {
		return ImportFormats.valueOf(wrapped.getImportFormats(), ImportFormats.class);
	}

	public void setImportFormats(List<ImportFormats> importFormats) {
		wrapped.setImportFormats(ImportFormats.unwrapp(importFormats, com.google.api.services.drive.model.About.ImportFormats.class));
	}

	public Boolean getIsCurrentAppInstalled() {
		return wrapped.getIsCurrentAppInstalled();
	}

	public void setIsCurrentAppInstalled(Boolean isCurrentAppInstalled) {
		wrapped.setIsCurrentAppInstalled(isCurrentAppInstalled);
	}

	public Long getLargestChangeId() {
		return wrapped.getLargestChangeId();
	}

	public void setLargestChangeId(Long largestChangeId) {
		wrapped.setLargestChangeId(largestChangeId);
	}

	public List<MaxUploadSizes> getMaxUploadSizes() {
		return MaxUploadSizes.valueOf(wrapped.getMaxUploadSizes(), MaxUploadSizes.class);
	}

	public void setMaxUploadSizes(List<MaxUploadSizes> maxUploadSizes) {
		wrapped.setMaxUploadSizes(MaxUploadSizes.unwrapp(maxUploadSizes, com.google.api.services.drive.model.About.MaxUploadSizes.class));
	}

	public String getName() {
		return wrapped.getName();
	}

	public void setName(String name) {
		wrapped.setName(name);
	}

	public String getPermissionId() {
		return wrapped.getPermissionId();
	}

	public void setPermissionId(String permissionId) {
		wrapped.setPermissionId(permissionId);
	}

	public Long getQuotaBytesTotal() {
		return wrapped.getQuotaBytesTotal();
	}

	public void setQuotaBytesTotal(Long quotaBytesTotal) {
		wrapped.setQuotaBytesTotal(quotaBytesTotal);
	}

	public Long getQuotaBytesUsed() {
		return wrapped.getQuotaBytesUsed();
	}

	public void setQuotaBytesUsed(Long quotaBytesUsed) {
		wrapped.setQuotaBytesUsed(quotaBytesUsed);
	}

	public Long getQuotaBytesUsedAggregate() {
		return wrapped.getQuotaBytesUsedAggregate();
	}

	public void setQuotaBytesUsedAggregate(Long quotaBytesUsedAggregate) {
		wrapped.setQuotaBytesUsedAggregate(quotaBytesUsedAggregate);
	}

	public Long getQuotaBytesUsedInTrash() {
		return wrapped.getQuotaBytesUsedInTrash();
	}

	public void setQuotaBytesUsedInTrash(Long quotaBytesUsedInTrash) {
		wrapped.setQuotaBytesUsedInTrash(quotaBytesUsedInTrash);
	}

	public Long getRemainingChangeIds() {
		return wrapped.getRemainingChangeIds();
	}

	public void setRemainingChangeIds(Long remainingChangeIds) {
		wrapped.setRemainingChangeIds(remainingChangeIds);
	}

	public String getRootFolderId() {
		return wrapped.getRootFolderId();
	}

	public void setRootFolderId(String rootFolderId) {
		wrapped.setRootFolderId(rootFolderId);
	}
	
	
}
