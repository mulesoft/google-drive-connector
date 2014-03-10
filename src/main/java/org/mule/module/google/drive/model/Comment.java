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

import org.mule.modules.google.api.datetime.DateTime;
import org.mule.modules.google.api.model.BaseWrapper;

/**
 * Wrapper for class {@link com.google.api.services.drive.model.Comment}
 * to make it DataMapper friendly
 *  
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class Comment extends BaseWrapper<com.google.api.services.drive.model.Comment> {

	public Comment() {
		super(new com.google.api.services.drive.model.Comment());
	}
	
	public Comment(com.google.api.services.drive.model.Comment wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public String getAnchor() {
		return wrapped.getAnchor();
	}

	public void setAnchor(String anchor) {
		wrapped.setAnchor(anchor);
	}

	public User getAuthor() {
		return new User(wrapped.getAuthor());
	}

	public void setAuthor(User author) {
		wrapped.setAuthor(author.wrapped());
	}

	public String getCommentId() {
		return wrapped.getCommentId();
	}

	public void setCommentId(String commentId) {
		wrapped.setCommentId(commentId);
	}

	public String getContent() {
		return wrapped.getContent();
	}

	public void setContent(String content) {
		wrapped.setContent(content);
	}

	public DateTime getCreatedDate() {
		return new DateTime(wrapped.getCreatedDate());
	}

	public void setCreatedDate(DateTime createdDate) {
		wrapped.setCreatedDate(createdDate.getWrapped());
	}

	public Boolean getDeleted() {
		return wrapped.getDeleted();
	}

	public void setDeleted(Boolean deleted) {
		wrapped.setDeleted(deleted);
	}

	public String getFileId() {
		return wrapped.getFileId();
	}

	public void setFileId(String fileId) {
		wrapped.setFileId(fileId);
	}

	public String getFileTitle() {
		return wrapped.getFileTitle();
	}

	public void setFileTitle(String fileTitle) {
		wrapped.setFileTitle(fileTitle);
	}

	public String getHtmlContent() {
		return wrapped.getHtmlContent();
	}

	public void setHtmlContent(String htmlContent) {
		wrapped.setHtmlContent(htmlContent);
	}

	public DateTime getModifiedDate() {
		return new DateTime(wrapped.getModifiedDate());
	}

	public void setModifiedDate(DateTime modifiedDate) {
		wrapped.setModifiedDate(modifiedDate.getWrapped());
	}

	public List<CommentReply> getReplies() {
		return CommentReply.valueOf(wrapped.getReplies(), CommentReply.class);
	}

	public void setReplies(List<CommentReply> replies) {
		wrapped.setReplies(CommentReply.unwrapp(replies, com.google.api.services.drive.model.CommentReply.class));
	}

	public String getStatus() {
		return wrapped.getStatus();
	}

	public void setStatus(String status) {
		wrapped.setStatus(status);
	}
	
	public Context getContext() {
		return new Context(this.wrapped.getContext());
	}
	
	public void setContext(Context context) {
		this.wrapped.setContext(context.wrapped());
	}
}
