package com.labor.spring.system.ppp.api.document;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.labor.spring.core.entity.User;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentComment;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.document.DocumentTag;
import com.labor.spring.system.ppp.entity.tag.Tag;

public interface DocumentServiceIntf {
	
	public Document create(DocumentDto documentDto);
	public Document createDocument(Document document);
	public DocumentComment createComment(Integer docId, DocumentComment comment);
	public DocumentContent createContent(Integer docId, DocumentContent content);
	public DocumentTag createTag(Integer docId, Integer tagId, String tagName);
	public DocumentTag createTag(Integer docId, String tagName, String tagType);
	public List<DocumentTag> createTagList(Integer docId, List<DocumentTag> tagList, String tagType);
	public List<User> createUserList(Integer docId, List<User> userList);
	
	public Document update(Integer docId, DocumentDto documentDto);
	public Document updateDocument(Integer docId, Document document);
	public DocumentComment updateComment(Integer docId, Integer commentId, DocumentComment comment);
	public DocumentContent updateContent(Integer docId, Integer contentId, DocumentContent content);
	public List<DocumentTag> updateTagList(Integer docId, List<DocumentTag> tagList, String tagType);
	public List<User> updateUserList(Integer docId, List<User> userList);
	
	public DocumentDto findByUuid(String uuid);
	public DocumentDto findById(Integer id);
	
	public Optional<Document> findDocumentByUuid(String uuid);
	public Optional<Document> findDocumentById(Integer id);
	public Optional<DocumentContent> findContentById(Integer docId, Integer contentId);
	public Optional<User> findCreator(Integer docId);
	
	public List<Document> findDocumentListByExample(Document entity);
	public List<Document> findDocumentListByExample(Document document, Sort sort);
	public List<DocumentContent> findContentListByDocid(Integer docId);
	public List<DocumentContent> findContentVersionsByDocid(Integer docId);
	public List<DocumentComment> findCommentListByDocid(Integer docId);
	
	public Page<Document> findDocumentListByExample(Document document, Pageable pageable);
	public Page<Document> findDocumentListByNameLike(String name, Pageable pageable);
	public Page<Document> findDocumentListByNameOrPinyinLike(String name, Pageable pageable);
	public Page<Document> findDocumentListByCreatedBy(String userid, Pageable pageable);
	public Page<Document> findDocumentListByTagName(String tagName, Pageable pageable);

	
	public Long countDocumentByExample(Document entity);
	
}
