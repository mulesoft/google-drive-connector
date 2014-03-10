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

import com.google.api.services.drive.model.File.ImageMediaMetadata.Location;


/**
 * Wrapper for class {@link com.google.api.services.drive.model.File.ImageMediaMetadata}
 * to make it DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class ImageMediaMetadata extends BaseWrapper<com.google.api.services.drive.model.File.ImageMediaMetadata> {
	
	public ImageMediaMetadata() {
		super(new com.google.api.services.drive.model.File.ImageMediaMetadata());
	}
	
	public ImageMediaMetadata(com.google.api.services.drive.model.File.ImageMediaMetadata wrapped) {
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

	public Float getAperture() {
		return wrapped.getAperture();
	}

	public void setAperture(Float aperture) {
		wrapped.setAperture(aperture);
	}

	public String getCameraMake() {
		return wrapped.getCameraMake();
	}

	public void setCameraMake(String cameraMake) {
		wrapped.setCameraMake(cameraMake);
	}

	public String getCameraModel() {
		return wrapped.getCameraModel();
	}

	public void setCameraModel(String cameraModel) {
		wrapped.setCameraModel(cameraModel);
	}

	public String getColorSpace() {
		return wrapped.getColorSpace();
	}

	public void setColorSpace(String colorSpace) {
		wrapped.setColorSpace(colorSpace);
	}

	public String getDate() {
		return wrapped.getDate();
	}

	public void setDate(String date) {
		wrapped.setDate(date);
	}

	public Float getExposureBias() {
		return wrapped.getExposureBias();
	}

	public void setExposureBias(Float exposureBias) {
		wrapped.setExposureBias(exposureBias);
	}

	public String getExposureMode() {
		return wrapped.getExposureMode();
	}

	public void setExposureMode(String exposureMode) {
		wrapped.setExposureMode(exposureMode);
	}

	public Float getExposureTime() {
		return wrapped.getExposureTime();
	}

	public void setExposureTime(Float exposureTime) {
		wrapped.setExposureTime(exposureTime);
	}

	public Boolean getFlashUsed() {
		return wrapped.getFlashUsed();
	}

	public void setFlashUsed(Boolean flashUsed) {
		wrapped.setFlashUsed(flashUsed);
	}

	public Float getFocalLength() {
		return wrapped.getFocalLength();
	}

	public void setFocalLength(Float focalLength) {
		wrapped.setFocalLength(focalLength);
	}

	public Integer getHeight() {
		return wrapped.getHeight();
	}

	public void setHeight(Integer height) {
		wrapped.setHeight(height);
	}

	public Integer getIsoSpeed() {
		return wrapped.getIsoSpeed();
	}

	public void setIsoSpeed(Integer isoSpeed) {
		wrapped.setIsoSpeed(isoSpeed);
	}

	public String getLens() {
		return wrapped.getLens();
	}

	public void setLens(String lens) {
		wrapped.setLens(lens);
	}

	public Location getLocation() {
		return wrapped.getLocation();
	}

	public void setLocation(Location location) {
		wrapped.setLocation(location);
	}

	public Float getMaxApertureValue() {
		return wrapped.getMaxApertureValue();
	}

	public void setMaxApertureValue(Float maxApertureValue) {
		wrapped.setMaxApertureValue(maxApertureValue);
	}

	public String getMeteringMode() {
		return wrapped.getMeteringMode();
	}

	public void setMeteringMode(String meteringMode) {
		wrapped.setMeteringMode(meteringMode);
	}

	public Integer getRotation() {
		return wrapped.getRotation();
	}

	public void setRotation(Integer rotation) {
		wrapped.setRotation(rotation);
	}

	public String getSensor() {
		return wrapped.getSensor();
	}

	public void setSensor(String sensor) {
		wrapped.setSensor(sensor);
	}

	public Integer getSubjectDistance() {
		return wrapped.getSubjectDistance();
	}

	public void setSubjectDistance(Integer subjectDistance) {
		wrapped.setSubjectDistance(subjectDistance);
	}

	public String getWhiteBalance() {
		return wrapped.getWhiteBalance();
	}

	public void setWhiteBalance(String whiteBalance) {
		wrapped.setWhiteBalance(whiteBalance);
	}

	public Integer getWidth() {
		return wrapped.getWidth();
	}

	public void setWidth(Integer width) {
		wrapped.setWidth(width);
	}
}
