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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.lifecycle.Start;
import org.mule.api.annotations.oauth.OAuth2;
import org.mule.api.annotations.oauth.OAuthAccessToken;
import org.mule.api.annotations.oauth.OAuthAccessTokenIdentifier;
import org.mule.api.annotations.oauth.OAuthAuthorizationParameter;
import org.mule.api.annotations.oauth.OAuthConsumerKey;
import org.mule.api.annotations.oauth.OAuthConsumerSecret;
import org.mule.api.annotations.oauth.OAuthInvalidateAccessTokenOn;
import org.mule.api.annotations.oauth.OAuthPostAuthorization;
import org.mule.api.annotations.oauth.OAuthProtected;
import org.mule.api.annotations.oauth.OAuthScope;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.module.google.drive.model.About;
import org.mule.module.google.drive.model.App;
import org.mule.module.google.drive.model.Change;
import org.mule.module.google.drive.model.Comment;
import org.mule.module.google.drive.model.CommentReply;
import org.mule.module.google.drive.model.File;
import org.mule.module.google.drive.model.Permission;
import org.mule.module.google.drive.model.Revision;
import org.mule.module.google.drive.model.stream.StreamContent;
import org.mule.modules.google.AbstractGoogleOAuthConnector;
import org.mule.modules.google.AccessType;
import org.mule.modules.google.ForcePrompt;
import org.mule.modules.google.IdentifierPolicy;
import org.mule.modules.google.api.pagination.PaginationUtils;
import org.mule.modules.google.oauth.invalidation.OAuthTokenExpiredException;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Copy;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.Patch;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.ChangeList;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.CommentList;
import com.google.api.services.drive.model.CommentReplyList;
import com.google.api.services.drive.model.FileList;

/**
 * Google Drive Cloud connector.
 * This connector covers almost all the Google Drive API v2 using OAuth2 for authentication.
 *
 * @author mariano.gonzalez@mulesoft.com
 */
@Connector(name="google-drive", schemaVersion="1.0", friendlyName="Google Calendars", minMuleVersion="3.4", configElementName="config-with-oauth")
@OAuth2(
		authorizationUrl = "https://accounts.google.com/o/oauth2/auth",
		accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
		accessTokenRegex="\"access_token\"[ ]*:[ ]*\"([^\\\"]*)\"",
		expirationRegex="\"expires_in\"[ ]*:[ ]*([\\d]*)",
		refreshTokenRegex="\"refresh_token\"[ ]*:[ ]*\"([^\\\"]*)\"",
		authorizationParameters={
				@OAuthAuthorizationParameter(name="access_type", defaultValue="online", type=AccessType.class, description="Indicates if your application needs to access a Google API when the user is not present at the browser. " + 
											" Use offline to get a refresh token and use that when the user is not at the browser. Default is online", optional=true),
				@OAuthAuthorizationParameter(name="force_prompt", defaultValue="auto", type=ForcePrompt.class, description="Indicates if google should remember that an app has been authorized or if each should ask authorization every time. " + 
											" Use force to request authorization every time or auto to only do it the first time. Default is auto", optional=true)
		}
)
public class GoogleDriveConnector extends AbstractGoogleOAuthConnector {
	
	public static final String NEXT_PAGE_TOKEN = "GoogleDrive_NEXT_PAGE_TOKEN";
	
	/**
     * The OAuth2 consumer key 
     */
    @Configurable
    @OAuthConsumerKey
    private String consumerKey;

    /**
     * The OAuth2 consumer secret 
     */
    @Configurable
    @OAuthConsumerSecret
    private String consumerSecret;
    
    /**
     * Application name registered on Google API console
     */
    @Configurable
    @Optional
    @Default("Mule-GoogleDriveConnector/1.0")
    private String applicationName;
    
    /**
     * The OAuth scopes you want to request
     */
    @OAuthScope
    @Configurable
    @Optional
    @Default(
    		USER_PROFILE_SCOPE + " " 
    		+ DriveScopes.DRIVE + " "
    		+ DriveScopes.DRIVE_APPS_READONLY + " "
    		+ DriveScopes.DRIVE_FILE + " "
    		+ DriveScopes.DRIVE_METADATA_READONLY)
    private String scope;
    
    /**
     * This policy represents which id we want to use to represent each google account.
     * 
     * PROFILE means that we want the google profile id. That means, the user's primary key in google's DB.
     * This is a long number represented as a string.
     * 
     * EMAIL means you want to use the account's email address
     */
    @Configurable
    @Optional
    @Default("EMAIL")
    private IdentifierPolicy identifierPolicy = IdentifierPolicy.EMAIL;
    
    /**
     * Factory to instantiate the underlying google client.
     * Usually you don't need to override this. Most common
     * use case of a custom value here is testing.
     */
    @Configurable
    @Optional
    private GoogleDriveClientFactory clientFactory;
    
    @OAuthAccessToken
    private String accessToken;
    
    /**
	 * The google api client
	 */
	private Drive client;
	
	/**
	 * Initializes the connector. if no clientFactory was provided, then a default
	 * {@link org.mule.module.google.calendar.DefaultGoogleCalendarClientFactory.DefaultGoogleCalendarClientFactor}
	 * wil be used instead
	 */
	@Start
	public void init() {
		if (this.clientFactory == null) {
			this.clientFactory = new DefaultGoogleDriveClientFactory();
		}
	}
	
	@OAuthAccessTokenIdentifier
	public String getAccessTokenId() {
		return this.identifierPolicy.getId(this);
	}
	
	@OAuthPostAuthorization
	public void postAuth() {
		this.client = this.clientFactory.newClient(this.getAccessToken(), this.getApplicationName());
	}
	
	/**
	 * Returns the metadata of the file with the matching id
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:get-file-metadata}
	 * 
	 * @param fileId the id of the file you want
	 * @param updateViewedDate Whether to update the view date after successfully retrieving the file
	 * @return an instance of {@link org.mule.module.google.drive.model.File}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File getFileMetadata(String fileId, @Optional @Default("false") boolean updateViewedDate) throws IOException {
		return new File(this.client.files().get(fileId).setUpdateViewedDate(updateViewedDate).execute());
	}
	
	/**
	 * downloads the contents of a file and returns them as an input stream
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:download-file}
	 * 
	 * @param file an instance of {@link org.mule.module.google.drive.model.File}. The downloadUrl attribute cannot be blank
	 * @return an instance of {@link java.io.InputStream}
	 * @throws IOException in case of connection issues 
	 * @throws IllegalArgumentException if file is null or its downloadUrl property is blank
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public InputStream downloadFile(@Optional @Default("#[payload]") File file) throws IOException {
	    
		if (file == null) {
	    	throw new IllegalArgumentException("file cannot be null");
	    }
		
		return this.download(file.getDownloadUrl());
	}
	
	/**
	 * Uploads a file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:upload}
	 * 
	 * @param file an instance of {@link org.mule.module.google.drive.model.File} holding the file's metadata
	 * @param mimeType the mimeType for the file being uploaded
	 * @param contentStream an input stream holding the file's content
	 * @param ocr Whether to attempt OCR on .jpg, .png, .gif, or .pdf uploads.
	 * @param ocrLanguage If ocr is true, hints at the language to use. Valid values are ISO 639-1 codes.
	 * @param convert Whether to convert this file to the corresponding Google Docs format
	 * @param pinned Whether to pin the head revision of the uploaded file.
	 * @param timedTextLanguage The language of the timed text.
	 * @param timedTextTrackName The timed text track name.
	 * @param indexContent Whether to use the content as indexable text
	 * @return an instance of {@link org.mule.module.google.drive.model.File} representing the newly created file
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File upload(
			@Optional @Default("#[payload]") File file,
			String mimeType,
			InputStream contentStream,
			@Optional @Default("false") boolean ocr,
			@Optional @Default("en") String ocrLanguage,
			@Optional @Default("false") boolean convert,
			@Optional @Default("false") boolean pinned,
			@Optional String timedTextLanguage,
			@Optional String timedTextTrackName,
			@Optional @Default("false") boolean indexContent) throws IOException {
		
		AbstractInputStreamContent mediaContent = new StreamContent(mimeType, contentStream);
	    Insert command = this.client.files().insert(file.wrapped(), mediaContent)
	    		.setOcr(ocr)
	    		.setOcrLanguage(ocrLanguage)
	    		.setConvert(convert)
	    		.setUseContentAsIndexableText(indexContent)
	    		.setPinned(pinned);
    	
    	if (timedTextLanguage != null) {
    		command.setTimedTextLanguage(timedTextLanguage);
    	}
    	
    	if (timedTextTrackName != null) {
    		command.setTimedTextTrackName(timedTextTrackName);
    	}
    	
		return new File(command.execute());
	}
	
	/**
	 * Makes a partial update to the File's metadata. You provide an instance of
	 * {@link org.mule.module.google.drive.model.File} with a valid id and new
	 * values for certain properties. This processor will update those properties only.
	 * This operation is ideal for simple operations such as rename in which you don't want/need
	 * to get the whole object. You can just make a File with id and title, leaving the rest of the
	 * properties in null.
	 * 
	 * Notice that this processor has no way of guessing which properties to include in the patch.
	 * You need to provide a consistent list of fields
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:patch-file}
	 * 
	 * @param file an instance of {@link org.mule.module.google.drive.model.File} holding the file's new values
	 * @param fields the list of fields to include in the patch operation
	 * @param ocr Whether to attempt OCR on .jpg, .png, .gif, or .pdf uploads.
	 * @param ocrLanguage If ocr is true, hints at the language to use. Valid values are ISO 639-1 codes.
	 * @param convert Whether to convert this file to the corresponding Google Docs format
	 * @param pinned Whether to pin the head revision of the uploaded file.
	 * @param timedTextLanguage The language of the timed text.
	 * @param timedTextTrackName The timed text track name.
	 * @param newRevision Whether a blob upload should create a new revision.
	 * 			If not set or false, the blob data in the current head revision is replaced.
	 * 			If true, a new blob is created as head revision, and previous revisions are preserved
	 * 			(causing increased use of the user's data storage quota)
	 * @param indexContent Whether to use the content as indexable text
	 * @param updateViewedDate Whether to update the view date after successfully updating the file
	 * @param setModifiedDate Whether to set the modified date with the supplied modified date
	 * @return an instance of {@link org.mule.module.google.drive.model.File} representing the file's new state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File patchFile(
			@Optional @Default("#[payload]") File file,
			List<String> fields,
			@Optional @Default("false") boolean ocr,
			@Optional @Default("en") String ocrLanguage,
			@Optional @Default("false") boolean convert,
			@Optional @Default("false") boolean pinned,
			@Optional String timedTextLanguage,
			@Optional String timedTextTrackName,
			@Optional @Default("false") boolean newRevision,
			@Optional @Default("false") boolean indexContent,
			@Optional @Default("false") boolean updateViewedDate,
			@Optional @Default("false") boolean setModifiedDate) throws IOException {
		
		Patch command = this.client.files().patch(file.getId(), file.wrapped())
				.setOcr(ocr)
	    		.setOcrLanguage(ocrLanguage)
	    		.setConvert(convert)
	    		.setPinned(pinned)
	    		.setSetModifiedDate(setModifiedDate)
	    		.setNewRevision(newRevision)
	    		.setUpdateViewedDate(updateViewedDate);
		
		if (timedTextLanguage != null) {
    		command.setTimedTextLanguage(timedTextLanguage);
    	}
    	
    	if (timedTextTrackName != null) {
    		command.setTimedTextTrackName(timedTextTrackName);
    	}
    	
    	command.setFields(this.toString(fields));
    	return new File(command.execute());
	}
	
	/**
	 * Updates the File's metadata. You provide an instance of
	 * {@link org.mule.module.google.drive.model.File} with a valid id and new
	 * property values
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:update-file}
	 * 
	 * @param file an instance of {@link org.mule.module.google.drive.model.File} holding the file's new values
	 * @param ocr Whether to attempt OCR on .jpg, .png, .gif, or .pdf uploads.
	 * @param ocrLanguage If ocr is true, hints at the language to use. Valid values are ISO 639-1 codes.
	 * @param convert Whether to convert this file to the corresponding Google Docs format
	 * @param pinned Whether to pin the head revision of the uploaded file.
	 * @param timedTextLanguage The language of the timed text.
	 * @param timedTextTrackName The timed text track name.
	 * @param newRevision Whether a blob upload should create a new revision.
	 * 			If not set or false, the blob data in the current head revision is replaced.
	 * 			If true, a new blob is created as head revision, and previous revisions are preserved
	 * 			(causing increased use of the user's data storage quota)
	 * @param indexContent Whether to use the content as indexable text
	 * @param updateViewedDate Whether to update the view date after successfully updating the file
	 * @param setModifiedDate Whether to set the modified date with the supplied modified date
	 * @return an instance of {@link org.mule.module.google.drive.model.File} representing the file's new state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File updateFile(
			@Optional @Default("#[payload]") File file,
			@Optional @Default("false") boolean ocr,
			@Optional @Default("en") String ocrLanguage,
			@Optional @Default("false") boolean convert,
			@Optional @Default("false") boolean pinned,
			@Optional String timedTextLanguage,
			@Optional String timedTextTrackName,
			@Optional @Default("false") boolean newRevision,
			@Optional @Default("false") boolean indexContent,
			@Optional @Default("false") boolean updateViewedDate,
			@Optional @Default("false") boolean setModifiedDate) throws IOException {
		
		Update command = this.client.files().update(file.getId(), file.wrapped())
				.setOcr(ocr)
	    		.setOcrLanguage(ocrLanguage)
	    		.setConvert(convert)
	    		.setPinned(pinned)
	    		.setSetModifiedDate(setModifiedDate)
	    		.setNewRevision(newRevision)
	    		.setUpdateViewedDate(updateViewedDate);
		
		if (timedTextLanguage != null) {
    		command.setTimedTextLanguage(timedTextLanguage);
    	}
    	
    	if (timedTextTrackName != null) {
    		command.setTimedTextTrackName(timedTextTrackName);
    	}
    	
    	return new File(command.execute());
    	
		
	}
	
	/**
	 * Deletes the file of the given id
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:delete-file}
	 * 
	 * @param fileId the id of the file you want to delete
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public void deleteFile(String fileId) throws IOException {
		this.client.files().delete(fileId).execute();
	}
	
	/**
	 * Lists the user's files.
	 * 
	 * For supporting google's paging mechanism, the next page token is stored on the message property
     * &quot;GoogleDrive_NEXT_PAGE_TOKEN&quot;. If there isn't a next page, then the property is removed
     * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-files}
	 * 
	 * @param message the current mule message
	 * @param maxResults The maximum number of replies to include in the response, used for paging. 
	 * @param query Query string for searching files.
	 * @param pageToken The continuation token, used to page through large result sets
	 * @return a list with instances of {@link org.mule.module.google.drive.model.File}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	@Inject
	public List<File> listFiles(
			MuleMessage message,
			@Optional @Default("100") int maxResults,
			@Optional String query,
    		@Optional @Default("#[flowVars['GoogleDrive_NEXT_PAGE_TOKEN']]") String pageToken) throws IOException {
		
		FileList response = this.client.files().list()
							.setMaxResults(maxResults)
							.setPageToken(pageToken)
							.setQ(query)
							.execute();
		
		PaginationUtils.savePageToken(NEXT_PAGE_TOKEN, response.getNextPageToken(), message);
		return File.valueOf(response.getItems(), File.class);
	}
	
	/**
	 * Creates a copy of the specified file.
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:copy-file}
	 * 
	 * @param sourceId the id of the file you want to copy
	 * @param copiedFile an instance of {@link org.mule.module.google.drive.model.File} with the metadata of the target file
	 * @param ocr Whether to attempt OCR on .jpg, .png, .gif, or .pdf uploads.
	 * @param ocrLanguage If ocr is true, hints at the language to use. Valid values are ISO 639-1 codes.
	 * @param convert Whether to convert this file to the corresponding Google Docs format
	 * @param pinned Whether to pin the head revision of the uploaded file.
	 * @param timedTextLanguage The language of the timed text.
	 * @param timedTextTrackName The timed text track name.
	 * @return an instance of {@link org.mule.module.google.drive.model.File} representing the target file 
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File copyFile(
			String sourceId,
			@Optional @Default("#[payload]") File copiedFile,
			@Optional @Default("false") boolean ocr,
			@Optional @Default("en") String ocrLanguage,
			@Optional @Default("false") boolean convert,
			@Optional @Default("false") boolean pinned,
			@Optional String timedTextLanguage,
			@Optional String timedTextTrackName
			) throws IOException {
		
		
		
		Copy command = this.client.files().copy(sourceId, copiedFile.wrapped())
				.setOcr(ocr)
	    		.setOcrLanguage(ocrLanguage)
	    		.setConvert(convert)
	    		.setPinned(pinned);
		
		if (timedTextLanguage != null) {
    		command.setTimedTextLanguage(timedTextLanguage);
    	}
    	
    	if (timedTextTrackName != null) {
    		command.setTimedTextTrackName(timedTextTrackName);
    	}
    	
    	return new File(command.execute());
	}
	
	/**
	 * Set the file's updated time to the current server time
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:touch}
	 * 
	 * @param fileId the id of the file you want touched
	 * @return an instance of {@link org.mule.module.google.drive.model.File} with the file's updated state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File touch(String fileId) throws IOException {
		return new File(this.client.files().touch(fileId).execute());
	}
	
	/**
	 * Moves a file to the trash.
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:trash-file}
	 * 
	 * @param fileId the id of the file you want trashed
	 * @return an instance of {@link org.mule.module.google.drive.model.File} with the file's updated state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File trashFile(String fileId) throws IOException {
		return new File(this.client.files().trash(fileId).execute());
	}
	
	/**
	 * Restores a file from the trash back to its old position
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:restore-file}
	 * 
	 * @param fileId the id of the file you want restored
	 * @return an instance of {@link org.mule.module.google.drive.model.File} with the file's updated state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public File restoreFile(String fileId) throws IOException {
		return new File(this.client.files().untrash(fileId).execute());
	}
	
	/**
	 * Gets the information about the current user along with Drive API settings
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:about}
	 * 
	 * @return an instance of {@link org.mule.module.google.drive.model.About}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public About about() throws IOException {
		return new About(this.client.about().get().execute());
	}
	
	/**
	 * Lists the changes for a user
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-changes}
	 * 
	 * @param message the current mule message
	 * @param includeDeleted Whether to include deleted items
	 * @param includeSubscribed Whether to include shared files and public files the user has opened. When set to false, the list will include owned files plus any shared or public files the user has explictly added to a folder in Drive
	 * @param maxResults Maximum number of changes to return.
	 * @param pageToken The continuation token, used to page through large result sets. 
	 * @param startChangeId Change ID to start listing changes from.
	 * @return a list with instance of {@link org.mule.module.google.drive.model.Change}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	@Inject
	public List<Change> listChanges(
			MuleMessage message,
			@Optional @Default("true") boolean includeDeleted,
			@Optional @Default("true") boolean includeSubscribed,
			@Optional @Default("100") int maxResults,
			@Optional @Default("#[flowVars['GoogleDrive_NEXT_PAGE_TOKEN']]") String pageToken,
			@Optional Long startChangeId
			) throws IOException {
		
		ChangeList response = this.client.changes().list()
									.setIncludeDeleted(includeDeleted)
									.setIncludeSubscribed(includeSubscribed)
									.setMaxResults(maxResults)
									.setPageToken(pageToken)
									.setStartChangeId(startChangeId)
									.execute();
		
		PaginationUtils.savePageToken(NEXT_PAGE_TOKEN, response.getNextPageToken(), message);
		
		return Change.valueOf(response.getItems(), Change.class);
	}
	
	/**
	 * Returns a particular change by its id
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:get-change}
	 * 
	 * @param changeId the id of the change you want
	 * @return an instance of {@link org.mule.module.google.drive.model.Change}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Change getChange(String changeId) throws IOException {
		return new Change(this.client.changes().get(changeId).execute());
	}
	
	/**
	 * Inserts a file into a folder.
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:insert-file-in-folder}
	 * 
	 * @param fileId  the id of the file you want to insert
	 * @param folderId the id of the folder you want to insert the file into
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public void insertFileInFolder(String fileId, @Optional @Default("root") String folderId) throws IOException {
		ChildReference child = new ChildReference();
		child.setId(fileId);
		this.client.children().insert(folderId, child).execute();
	}
	
	/**
	 * Removes a file from a folder.
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:delete-file-from-folder}
	 * 
	 * @param fileId the id of the file
	 * @param folderId The ID of the folder.
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public void deleteFileFromFolder(String fileId, @Optional @Default("root") String folderId) throws IOException {
		ChildReference child = new ChildReference();
		child.setId(fileId);
		this.client.children().delete(folderId, fileId).execute();
	}
	
	/**
	 * Returns a list with the ids of the files that are under the given folder
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-files-in-folder}
	 * 
	 * @param message the current mule message
	 * @param folderId The ID of the folder. To list all files in the root folder, use the alias root as the value for folderId.
	 * @param maxResults Maximum number of children to return.
	 * @param query Query string for searching children
	 * @return a list of strings with the matching file ids
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	@Inject
	public List<String> listFilesInFolder(
			MuleMessage message,
			@Optional @Default("root") String folderId,
			@Optional @Default("100") int maxResults,
			@Optional String query) throws IOException {
		
		ChildList list = this.client.children().list(folderId)
						.setMaxResults(maxResults)
						.setQ(query)
						.execute();
		
		List<ChildReference> refs = list.getItems();
		
		if (CollectionUtils.isEmpty(refs)) {
			return new ArrayList<String>();
		} else {
			PaginationUtils.savePageToken(NEXT_PAGE_TOKEN, list.getNextPageToken(), message);
			List<String> result = new ArrayList<String>(refs.size());
			for (ChildReference r : refs) {
				result.add(r.getId());
			}
			
			return result;
		}
	}
	
	/**
	 * Lists a file's permissions
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-permissions}
	 * 
	 * @param fileId the id of the file which permissions you want
	 * @return a list with instances of {@link org.mule.module.google.drive.model.Permission}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public List<Permission> listPermissions(String fileId) throws IOException {
		return Permission.valueOf(this.client.permissions().list(fileId).execute().getItems(), Permission.class);
	}
	
	/**
	 * Deletes a permission from a file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:delete-permission}
	 * 
	 * @param fileId the id of the file owning the permission to be deleted
	 * @param permissionId the id of the permission to be deleted
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public void deletePermission(String fileId, String permissionId) throws IOException {
		this.client.permissions().delete(fileId, permissionId).execute();
	}
	
	/**
	 * Gets a particular permission from a file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:get-permission}
	 * 
	 * @param fileId the id of the file owning the permission you want
	 * @param permissionId the id of the permission you want
	 * @return an instance of {@link org.mule.module.google.drive.model.Permission}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Permission getPermission(String fileId, String permissionId) throws IOException {
		return new Permission(this.client.permissions().get(fileId, permissionId).execute());
	}
	
	/**
	 * Inserts a permission for a file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:insert-permission}
	 * 
	 * @param fileId the id of the file that the permission is created for
	 * @param permission an instance of {@link org.mule.module.google.drive.model.Permission}
	 * @param emailMessage A custom message to include in notification emails.
	 * @param sendNotificationEmails Whether to send notification emails when sharing to users or groups.
	 * @return an instance of {@link org.mule.module.google.drive.model.Permission} representing the newly created permission
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Permission insertPermission(
			String fileId,
			@Optional @Default("#[payload]") Permission permission,
			@Optional String emailMessage,
			@Optional @Default("true") boolean sendNotificationEmails
			) throws IOException {
		
		return new Permission(this.client.permissions().insert(fileId, permission.wrapped())
								.setEmailMessage(emailMessage)
								.setSendNotificationEmails(sendNotificationEmails)
								.execute());
	}
	
	/**
	 * Updates a permission
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:update-permission}
	 * 
	 * @param fileId the id of the file owning the permission
	 * @param permission an instance of {@link org.mule.module.google.drive.model.Permission} with the updated data
	 * @return an instance of {@link org.mule.module.google.drive.model.Permission.Permission} with the updated object state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Permission updatePermission(String fileId, @Optional @Default("#[payload]") Permission permission) throws IOException {
		return new Permission(this.client.permissions().update(fileId, permission.getId(), permission.wrapped()).execute());
	}
	
	
	
	
	/**
	 * Makes a partial update to a permission. You provide an instance of
	 * {@link org.mule.module.google.drive.model.Permission} with a valid id and new
	 * values for certain properties. This processor will update those properties only.
	 * This operation is ideal for simple operations such as rename in which you don't want/need
	 * to get the whole object. 
	 * 
	 * Notice that this processor has no way of guessing which properties to include in the patch.
	 * You need to provide a consistent list of fields
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:patch-permission}
	 * 
	 * @param fileId the id of the file owning the permission
	 * @param permission an instance of {@link org.mule.module.google.drive.model.Permission} with the updated data
	 * @param fields the list of fields to include in the patch operation 
	 * @return an instance of {@link org.mule.module.google.drive.model.Permission} with the patched state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Permission patchPermission(String fileId, @Optional @Default("#[payload]") Permission permission, List<String> fields) throws IOException {
		return new Permission(this.client.permissions().patch(
						fileId, permission.getId(), permission.wrapped())
					.setFields(this.toString(fields))
					.execute());
	}
	
	/**
	 * downloads the contents of a revision and returns them as an input stream
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:download-revision}
	 * 
	 * @param revision an instance of {@link org.mule.module.google.drive.model.Revision}. The downloadUrl attribute cannot be blank
	 * @return an instance of {@link java.io.InputStream}
	 * @throws IOException in case of connection issues 
	 * @throws IllegalArgumentException if revision is null or its downloadUrl property is blank
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public InputStream downloadRevision(@Optional @Default("#[payload]") Revision revision) throws IOException {
		if (revision == null) {
			throw new IllegalArgumentException("Revision cannot be null");
		}
		
		return this.download(revision.getDownloadUrl());
	}
	
	/**
	 * Gets a particular revision from a file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:get-revision}
	 * 
	 * @param fileId the id of the file from which a revision you want
	 * @param revisionId the id of the revision you want
	 * @return an instance of {@link org.mule.module.google.drive.model.Revision}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Revision getRevision(String fileId, String revisionId) throws IOException {
		return new Revision(this.client.revisions().get(fileId, revisionId).execute());
	}
	
	/**
	 * Removes a particular revision from a file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:delete-revision}
	 * 
	 * @param fileId the id of the file from which a revision you want deleted
	 * @param revisionId the id of the revision you want to delete
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public void deleteRevision(String fileId, String revisionId) throws IOException {
		this.client.revisions().delete(fileId, revisionId).execute();
	}
	
	/**
	 * Lists a file's revisions
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-revisions}
	 * 
	 * @param fileId the id of the file which revisions you want
	 * @return a list with instances of {@link org.mule.module.google.drive.model.Revision}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public List<Revision> listRevisions(String fileId) throws IOException {
		return Revision.valueOf(this.client.revisions().list(fileId).execute().getItems(), Revision.class);
	}
	
	/**
	 * Makes a partial update to a Revision's metadata. You provide an instance of
	 * {@link org.mule.module.google.drive.model.Revision} with a valid id and new
	 * values for certain properties. This processor will update those properties only.
	 * 
	 * This operation is ideal for simple operations such as rename in which you don't want/need
	 * to get the whole object. You can just make a Revision with id and title, leaving the rest of the
	 * properties in null.
	 * 
	 * Notice that this processor has no way of guessing which properties to include in the patch.
	 * You need to provide a consistent list of fields
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:patch-revision}
	 * 
	 * @param fileId the id of the file owning the revision to be patched
	 * @param revision an instance of {@link org.mule.module.google.drive.model.Revision} with the fields you want to patch and the id
	 * @param fields the list of fields to include in the patch operation
	 * @return an instance of {@link org.mule.module.google.drive.model.Revision} with the full patched state
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Revision patchRevision(String fileId, @Optional @Default("#[payload]") Revision revision, List<String> fields) throws IOException {
		return new Revision(this.client.revisions().patch(
								fileId, revision.getId(),
								revision.wrapped())
							.setFields(this.toString(fields))
							.execute());
	}
	
	/**
	 * Updates a revision's metadata
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:update-revision}
	 * 
	 * @param fileId the id of the file owning the revision to be updated
	 * @param revision an instance of {@link org.mule.module.google.drive.model.Revision} with the new state
	 * @return an instance of {@link org.mule.module.google.drive.model.Revision} with the full updated state
	 * @throws IOException in case of connection issues
	 */
	public Revision updateRevision(String fileId, @Optional @Default("#[payload]") Revision revision) throws IOException {
		return new Revision(this.client.revisions().update(fileId, revision.getId(), revision.wrapped()).execute());
	}
	
	/**
	 * Lists a user's installed apps
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-apps}
	 * 
	 * @return a list with instances of {@link org.mule.module.google.drive.model.App}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public List<App> listApps() throws IOException {
		return App.valueOf(this.client.apps().list().execute().getItems(), App.class);
	}
	
	/**
	 * Gets a specific app installed in the user's account
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:get-app}
	 * 
	 * @param appId the id of the app you want
	 * @return an instance of {@link org.mule.module.google.drive.model.App}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public App getApp(String appId) throws IOException {
		return new App(this.client.apps().get(appId).execute());
	}
	
	/**
	 * Gets a comment by id
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:get-comment}
	 * 
	 * @param fileId the id of the file owning the comment you want
	 * @param commentId the id of the comment you want
	 * @param includeDeleted If true, this will succeed when retrieving a deleted comment, and will include any deleted replies
	 * @return an instance of {@link org.mule.module.google.drive.model.Comment}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Comment getComment(String fileId, String commentId, @Optional @Default("false") boolean includeDeleted) throws IOException {
		return new Comment(this.client.comments().get(fileId, commentId).setIncludeDeleted(includeDeleted).execute());
	}
	
	/**
	 * Lists all comments for a file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-comments}
	 * 
	 * @param message the current mule message
	 * @param fileId the id of the file which comments you want
	 * @param includeDeleted If true, all comments and replies, including deleted comments and replies (with content stripped) will be returned.
	 * @param maxResults The maximum number of discussions to include in the response, used for paging. Acceptable values are 0 to 100, inclusive.
	 * @param pageToken The continuation token, used to page through large result sets
	 * @param updatedMin Only discussions that were updated after this timestamp will be returned. Formatted as an RFC 3339 timestamp.
	 * @return a list with instances of {@link org.mule.module.google.drive.model.Comment}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	@Inject
	public List<Comment> listComments(
			MuleMessage message,
			String fileId,
			@Optional @Default("false") boolean includeDeleted,
			@Optional @Default("100") int maxResults,
			@Optional @Default("#[flowVars['GoogleDrive_NEXT_PAGE_TOKEN']]") String pageToken,
			@Optional String updatedMin
			) throws IOException {
		
		CommentList response = this.client.comments().list(fileId)
										.setIncludeDeleted(includeDeleted)
										.setMaxResults(maxResults)
										.setPageToken(pageToken)
										.setUpdatedMin(updatedMin)
										.execute();
		

		PaginationUtils.savePageToken(NEXT_PAGE_TOKEN, response.getNextPageToken(), message);
		return Comment.valueOf(response.getItems(), Comment.class);
	}
	
	/**
	 * Inserts a new comment for a given file
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:insert-comment}
	 * 
	 * @param fileId the if of the file you want to comment on
	 * @param comment an instance of  {@link org.mule.module.google.drive.model.Comment} with the comments data
	 * @return an instance of {@link org.mule.module.google.drive.model.Comment} with the state of the new comment on the server 
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Comment insertComment(String fileId, @Optional @Default("#[payload]") Comment comment) throws IOException {
		return new Comment(this.client.comments().insert(fileId, comment.wrapped()).execute());
	}
	
	/**
	 * Updates a comment
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:update-comment}
	 * 
	 * @param fileId the id of the file owning the comment you want to update
	 * @param comment an instance of {@link org.mule.module.google.drive.model.Comment} carrying updated state
	 * @return an instance of {@link org.mule.module.google.drive.model.Comment} with the updated state on the server
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Comment updateComment(String fileId, @Optional @Default("#[payload]") Comment comment) throws IOException {
		return new Comment(this.client.comments().update(fileId, comment.getCommentId(), comment.wrapped()).execute());
	}
	
	/**
	 * 
	 * Makes a partial update to a comment. You provide an instance of
	 * {@link org.mule.module.google.drive.model.Comment} with a valid id and new
	 * values for certain properties. This processor will update those properties only.
	 * 
	 * This operation is ideal for simple operations such as simply changing the content
	 * in which you don't want/need to get the whole object.
	 * You can just make a Comment with id and content, leaving the rest of the
	 * properties in null.
	 * 
	 * Notice that this processor has no way of guessing which properties to include in the patch.
	 * You need to provide a consistent list of fields
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:patch-comment}
	 * 
	 * @param fileId the id of the file owning the comment to be patched
	 * @param comment an instance of {@link org.mule.module.google.drive.model.Comment} with the updated fields
	 * @param fields the list of fields to include in the patch operation
	 * @return an instance of {@link org.mule.module.google.drive.model.Comment} with the fully updated state 
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public Comment patchComment(String fileId, @Optional @Default("#[payload]") Comment comment, List<String> fields) throws IOException {
		return new Comment(this.client.comments().patch(fileId, comment.getCommentId(), comment.wrapped()).setFields(this.toString(fields)).execute());
	}
	
	/**
	 * Deletes a given comment by id
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:delete-comment}
	 * 
	 * @param fileId the id of the file that owns the comment you want gone
	 * @param commentId the id of the comment you want to delete
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public void deleteComment(String fileId, String commentId) throws IOException {
		this.client.comments().delete(fileId, commentId).execute();
	}
	
	/**
	 * Gets a comment reply by id
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:get-comment-reply}
	 * 
	 * @param fileId the id of the file on which your comment has been replied on
	 * @param commentId the id of the comment which reply you want
	 * @param replyId the id of the reply you want
	 * @param includeDeleted If true, this will succeed when retrieving a deleted comment, and will include any deleted replies
	 * @return an instance of {@link org.mule.module.google.drive.model.CommentReply}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public CommentReply getCommentReply(String fileId, String commentId, String replyId, @Optional @Default("false") boolean includeDeleted) throws IOException {
		return new CommentReply(this.client.replies().get(fileId, commentId, replyId).setIncludeDeleted(includeDeleted).execute());
	}
	
	/**
	 * Lists all replies for a given comment
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:list-comment-replies}
	 * 
	 * @param message the current mule message
	 * @param fileId the id of the file on which your comment has been replied on
	 * @param commentId the id of the comment which reply you want
	 * @param includeDeleted If true, all comments and replies, including deleted comments and replies (with content stripped) will be returned.
	 * @param maxResults The maximum number of replies to include in the response, used for paging. Acceptable values are 0 to 100.
	 * @param pageToken The continuation token, used to page through large result sets
	 * @return a list with instances of {@link org.mule.module.google.drive.model.org.mule.module.google.drive.model.CommentReply}
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	@Inject
	public List<CommentReply> listCommentReplies(
			MuleMessage message,
			String fileId,
			String commentId, 
			@Optional @Default("false") boolean includeDeleted,
			@Optional @Default("100") int maxResults,
			@Optional @Default("#[flowVars['GoogleDrive_NEXT_PAGE_TOKEN']]") String pageToken
			) throws IOException {
		
		CommentReplyList response = this.client.replies().list(fileId, commentId)
										.setIncludeDeleted(includeDeleted)
										.setMaxResults(maxResults)
										.setPageToken(pageToken)
										.execute();
		

		PaginationUtils.savePageToken(NEXT_PAGE_TOKEN, response.getNextPageToken(), message);
		return CommentReply.valueOf(response.getItems(), CommentReply.class);
	}
	
	/**
	 * Inserts a new reply for a given comment
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:insert-comment-reply}
	 * 
	 * @param fileId the id of the file on which your comment has been replied on
	 * @param commentId the id of the comment you want to reply on
	 * @param commentReply an instance of {@link org.mule.module.google.drive.model.CommentReply} with the reply's state
	 * @return an instance of {@link org.mule.module.google.drive.model.CommentReply} with the state of the new reply on the server 
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public CommentReply insertCommentReply(String fileId, String commentId, @Optional @Default("#[payload]") CommentReply commentReply) throws IOException {
		return new CommentReply(this.client.replies().insert(fileId, commentId, commentReply.wrapped()).execute());
	}
	
	/**
	 * Updates a comment reply
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:update-comment-reply}
	 * 
	 * @param fileId the id of the file owning the comment which reply you want to update
	 * @param commentId the id of the comment which reply you want updated
	 * @param commentReply an instance of {@link org.mule.module.google.drive.model.CommentReply} with the reply's new state
	 * @return an instance of {@link org.mule.module.google.drive.model.CommentReply} with the updated state on the server
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public CommentReply updateCommentReply(String fileId, String commentId, @Optional @Default("#[payload]") CommentReply commentReply) throws IOException {
		return new CommentReply(this.client.replies().update(fileId, commentId, commentReply.getReplyId(), commentReply.wrapped()).execute());
	}
	
	/**
	 * 
	 * Makes a partial update to a comment reply. You provide an instance of
	 * {@link org.mule.module.google.drive.model.CommentReply} with a valid id and new
	 * values for certain properties. This processor will update those properties only.
	 * 
	 * This operation is ideal for simple operations such as simply changing the content
	 * in which you don't want/need to get the whole object.
	 * You can just make a CommentReply with id and content, leaving the rest of the
	 * properties in null.
	 * 
	 * Notice that this processor has no way of guessing which properties to include in the patch.
	 * You need to provide a consistent list of fields
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:patch-comment-reply}
	 * 
	 * @param fileId the id of the file owning the comment which reply you want to update
	 * @param commentId the id of the comment which reply you want updated
	 * @param commentReply an instance of {@link org.mule.module.google.drive.model.CommentReply} with the reply's new state
	 * @param fields the list of fields to include in the patch operation
	 * @return an instance of {@link org.mule.module.google.drive.model.CommentReply} with the updated state on the server 
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public CommentReply patchCommentReply(String fileId, String commentId, @Optional @Default("#[payload]") CommentReply commentReply, List<String> fields) throws IOException {
		return new CommentReply(this.client.replies().patch(fileId, commentId, commentReply.getReplyId(), commentReply.wrapped()).setFields(this.toString(fields)).execute());
	}
	
	/**
	 * Deletes a reply from a given comment
	 * 
	 * {@sample.xml ../../../doc/GoogleDriveConnector.xml.sample google-drive:delete-comment-reply}
	 * 
	 * @param fileId the id of the file owning the comment which reply you want to deleted
	 * @param commentId the id of the comment which reply you want deleted
	 * @param replyId the id of the reply you want to delete
	 * @throws IOException in case of connection issues
	 */
	@Processor
    @OAuthProtected
	@OAuthInvalidateAccessTokenOn(exception=OAuthTokenExpiredException.class)
	public void deleteCommentReply(String fileId, String commentId, String replyId) throws IOException {
		this.client.replies().delete(fileId, commentId, replyId).execute();
	}
	
	
	private InputStream download(String downloadUrl) throws IOException {
		if (StringUtils.isBlank(downloadUrl)) {
			throw new IllegalArgumentException("Download url is null or blank");
		}
		
		return this.client.getRequestFactory().buildGetRequest(new GenericUrl(downloadUrl)).execute().getContent();
	}
	
	private String toString(List<String> list) {
		StringBuilder builder = new StringBuilder();
		
		for (String value : list) {
			if (builder.length() > 0) {
				builder.append(',');
			}
			
			builder.append(value);
		}
		
		return builder.toString();
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public IdentifierPolicy getIdentifierPolicy() {
		return identifierPolicy;
	}

	public void setIdentifierPolicy(IdentifierPolicy identifierPolicy) {
		this.identifierPolicy = identifierPolicy;
	}

	public GoogleDriveClientFactory getClientFactory() {
		return clientFactory;
	}

	public void setClientFactory(GoogleDriveClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
