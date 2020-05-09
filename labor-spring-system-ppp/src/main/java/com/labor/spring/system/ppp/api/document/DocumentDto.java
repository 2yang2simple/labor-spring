package com.labor.spring.system.ppp.api.document;

import java.util.List;

import com.labor.spring.core.entity.User;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentComment;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.document.DocumentTag;
import com.labor.spring.system.ppp.entity.oss.Attachment;

public class DocumentDto {
	
	private Integer id;
	
	private String name;
	
	private String docStatus;
	
	private String tagName;
	
	private String tagType;
	
	private String userid;
	
	private User creator;
	
	private Document document;
	
	private DocumentContent content;
	
	private List<DocumentContent> contentList;
	
	private List<DocumentComment> commentList;
	
	private List<User> userList;
	
	private List<DocumentTag> tagList;
	
	private List<Attachment> attachmentList;

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public DocumentContent getContent() {
		return content;
	}

	public void setContent(DocumentContent content) {
		this.content = content;
	}

	public List<DocumentComment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<DocumentComment> commentList) {
		this.commentList = commentList;
	}

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public List<DocumentTag> getTagList() {
		return tagList;
	}

	public void setTagList(List<DocumentTag> tagList) {
		this.tagList = tagList;
	}

	public List<Attachment> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<Attachment> attachmentList) {
		this.attachmentList = attachmentList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<DocumentContent> getContentList() {
		return contentList;
	}

	public void setContentList(List<DocumentContent> contentList) {
		this.contentList = contentList;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	
	

}
