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

import org.mule.modules.google.api.datetime.DateTime;
import org.mule.modules.google.api.model.BaseWrapper;


/**
 * Wrapper for class
 * {@link com.google.api.services.drive.model.CommentReply} to make it
 * DataMapper friendly
 * @author mariano.gonzalez@mulesoft.com
 *
 */
public class CommentReply extends BaseWrapper<com.google.api.services.drive.model.CommentReply> {

	public CommentReply() {
		super(new com.google.api.services.drive.model.CommentReply());
	}
	
	public CommentReply(com.google.api.services.drive.model.CommentReply wrapped) {
		super(wrapped);
	}

	public String toString() {
		return wrapped.toString();
	}

	public String toPrettyString() {
		return wrapped.toPrettyString();
	}

	public User getAuthor() {
		return new User(wrapped.getAuthor());
	}

	public void setAuthor(User author) {
		wrapped.setAuthor(author.wrapped());
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

	public String getReplyId() {
		return wrapped.getReplyId();
	}

	public void setReplyId(String replyId) {
		wrapped.setReplyId(replyId);
	}

	public String getVerb() {
		return wrapped.getVerb();
	}

	public void setVerb(String verb) {
		wrapped.setVerb(verb);
	}
}
