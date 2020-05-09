package com.labor.spring.system.ppp.api.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.core.entity.User;
import com.labor.spring.system.ppp.api.oss.AttachmentServiceIntf;
import com.labor.spring.system.ppp.api.tag.TagRepository;
import com.labor.spring.system.ppp.api.tag.TagServiceIntf;
import com.labor.spring.system.ppp.api.tag.TagType;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentComment;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.document.DocumentTag;
import com.labor.spring.system.ppp.entity.document.DocumentUser;
import com.labor.spring.system.ppp.entity.tag.Tag;
import com.labor.spring.system.ppp.util.HanLPExtractor;
import com.labor.spring.util.WebUtil;
@Service
public class DocumentServiceImpl implements DocumentServiceIntf {
	
	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private DocumentContentRepository documentContentRepository;

	@Autowired
	private DocumentCommentRepository documentCommentRepository;

	@Autowired
	private DocumentTagRepository documentTagRepository;

	@Autowired
	private DocumentUserRepository documentUserRepository;
	
	@Autowired
	private DocumentUserFinderRepository documentUserFinderRepository;

	@Autowired
	private TagServiceIntf tagService;

	//create document
	private Document executeCreateDocument(Document document) {
		Document ret = null;
		if (document!=null) {
			document.setId(null);
			document.setNamePinyin(HanLPExtractor.pinyin(document.getName()));
			document.setUuid(TokenUtil.generateUUID());
			document.setDocStatus(DocumentStatus.OPENED);
			document.setStatus(CommonConstants.ACTIVE);
			ret = documentRepository.save(document);
		}
		return ret;
	}

	//create document content
	private DocumentContent executeCreateContent(Integer docId, DocumentContent content) {
		DocumentContent ret = null;
		if (docId!=null&&docId>0&&content!=null) {
			content.setId(null);
			content.setDocId(docId);
			content.setStatus(CommonConstants.ACTIVE);
			if (content.getHtml()==null) {
				content.setHtml("Write something here...");
			}
			content.setText(content.getHtml().replaceAll("<[^>]*>",""));
			content.setMd5(TokenUtil.md5(content.getHtml()));
			ret = documentContentRepository.save(content);
		}
		return ret;
	}
	
	//create document comment
	private DocumentComment executeCreateComment(Integer docId, DocumentComment comment) {
		DocumentComment ret = null;
		if (docId!=null&&docId>0&&comment!=null) {
			comment.setId(null);
			comment.setDocId(docId);
			comment.setStatus(CommonConstants.ACTIVE);
			comment.setText(comment.getHtml()==null?"":comment.getHtml().replaceAll("<[^>]*>",""));
			ret = documentCommentRepository.save(comment);
		}
		return ret;
	}
	//create document tags
//	private List<DocumentTag> executeCreateUserDefineTagList(Integer docId, List<DocumentTag> tagList) {
//		List<DocumentTag> ret = null;
//		if (docId!=null&&docId>0&&tagList!=null) {
//			ret =  new ArrayList<DocumentTag>();
//			int size = tagList.size();
//			for(int i=0;i<size;i++) {
//				DocumentTag dt = tagList.get(i);
//				Tag tag = tagService.createTag(dt.getTagName(), TagType.DOCUMENT_USERDEFINE);
//				if (tag!=null) {
//					dt.setId(null);
//					dt.setDocId(docId);
//					dt.setTagId(tag.getId());
//					dt.setTagName(tag.getName());
//					dt.setTagType(TagType.DOCUMENT_USERDEFINE);
//					dt = documentTagRepository.save(dt);
//					ret.add(dt);
//				}
//			}
//		}
//		return ret;
//	}
	
	private List<DocumentTag> executeCreateTagList(Integer docId, List<DocumentTag> taglist, String tagtype) {
		List<DocumentTag> ret = null;
		if (taglist!=null) {
			int size = taglist.size();
			ret = new ArrayList<DocumentTag>();
			for(int i=0;i<size;i++) {
				DocumentTag dt = taglist.get(i);
				if(dt!=null&&!StringUtil.isEmpty(dt.getTagName())) {
					if (!StringUtil.isEmpty(tagtype)) {
						dt.setTagType(tagtype);
					} 
					DocumentTag newtag = executeCreateTag(docId,dt.getTagName(),dt.getTagType());
					if (newtag!=null) {
						ret.add(newtag);
					}
				}
			}
		}
		return ret;
	}
	
	private List<DocumentTag> executeCreateStringTagList(Integer docId, List<String> taglist, String tagtype) {
		List<DocumentTag> ret = null;
		if (taglist!=null) {
			int size = taglist.size();
			ret = new ArrayList<DocumentTag>();
			for(int i=0;i<size;i++) {
				String tagname = taglist.get(i);
				if(!StringUtil.isEmpty(tagname)) {
					DocumentTag newtag = executeCreateTag(docId,tagname,tagtype);
					if (newtag!=null) {
						ret.add(newtag);
					}
				}
			}
		}
		return ret;
	}
	
	private DocumentTag executeCreateTag(Integer docId, String tagname, String tagtype) {
		DocumentTag ret = null;
		if (StringUtil.isEmpty(tagtype)) {
			tagtype = TagType.DOCUMENT_USERDEFINE;
		} 
		Tag tag = tagService.createTag(tagname,tagtype);
		if (tag!=null) {
			DocumentTag newdt = new DocumentTag();
			newdt.setDocId(docId);
			newdt.setTagId(tag.getId());
			newdt.setTagName(tag.getName());
			newdt.setTagType(tag.getType());
			ret = documentTagRepository.save(newdt);	
		}
		return ret;
	}
	
	//create document users
	private List<User> executeCreateUserList(Integer docId, List<User> userList) {
		List<User> ret = null;
		if (docId!=null&&docId>0&&userList!=null) {
			ret =  new ArrayList<User>();
			int size = userList.size();
			for(int i=0;i<size;i++) {
				User user = userList.get(i);
				DocumentUser du = new DocumentUser();
				du.setDocId(docId);
				du.setUserId(user.getId());
				documentUserRepository.save(du);
				ret.add(user);//user object should be validate by web 
			}
		}
		return ret;
	}
	
	
	//update document
	private Document executeUpdateDocument(Integer docId, Document document) {
		Document ret = null;
		if (docId!=null&&docId>0&&document!=null&&document.getId()!=null&&document.getId()>0) {
			document.setId(docId);
			document.setNamePinyin(HanLPExtractor.pinyin(document.getName()));
			ret = documentRepository.save(document);
		}
		return ret;
	}
	//update document content
	private DocumentContent executeUpdateContent(Integer docId, Integer contentId, DocumentContent content) {
		DocumentContent ret = null;
		if (docId!=null&&docId>0&&contentId!=null&&contentId>0&&content!=null) {
			content.setDocId(docId);
			content.setId(contentId);
			if (content.getHtml()==null) {
				content.setHtml("Write something here...");
			}
			content.setText(content.getHtml().replaceAll("<[^>]*>",""));
			content.setMd5(TokenUtil.md5(content.getHtml()));
			ret = documentContentRepository.save(content);
		}
		return ret;
	}
	//update document certain comment;
	private DocumentComment executeUpdateComment(Integer docId, Integer commentId, DocumentComment comment) {
		DocumentComment ret = null;
		if (docId!=null&&docId>0&&commentId!=null&&commentId>0&&comment!=null) {
			comment.setDocId(docId);
			comment.setId(commentId);
			comment.setText(comment.getHtml()==null?"":comment.getHtml().replaceAll("<[^>]*>",""));
			ret = documentCommentRepository.save(comment);
		}
		return ret;
	}
	//update document tags
//	private List<DocumentTag> executeUpdateUserDefineTagList(Integer docId, List<DocumentTag> tagList) {
//		List<DocumentTag> ret = null;
//		if(tagList!=null&&tagList.size()>0) {
//			documentTagRepository.deleteByDocIdAndTagType(docId,TagType.DOCUMENT_USERDEFINE);
//			ret = executeCreateUserDefineTagList(docId,tagList);
//		}
//		return ret;
//		
//	}
	private List<DocumentTag> executeUpdateTagList(Integer docId, List<DocumentTag> tagList, String tagType) {
		List<DocumentTag> ret = null;
		if(tagList!=null&&tagList.size()>0&&!StringUtil.isEmpty(tagType)) {
			documentTagRepository.deleteByDocIdAndTagType(docId,tagType);
			ret = executeCreateTagList(docId,tagList,tagType);
		}
		return ret;
	}
	
	//update document users
	private List<User> executeUpdateUserList(Integer docId, List<User> userList) {
		List<User> ret = null;
		if(userList!=null&&userList.size()>0) {
			documentUserRepository.deleteByDocId(docId);
			ret = executeCreateUserList(docId,userList);
		}
		return ret;
	}
		
	@Override
	@Transactional(isolation= Isolation.DEFAULT,propagation= Propagation.REQUIRED,rollbackFor = Exception.class)
	public Document create(DocumentDto documentDto) {
		Document ret = null;
		if(documentDto==null) {
			return ret;
		}
		Integer docId = 0;
		Document document  = executeCreateDocument(documentDto.getDocument());
		if (document!=null) {
			docId = document.getId();
		}
		if (docId>0) {
			DocumentContent content = executeCreateContent(docId, documentDto.getContent());
			executeCreateTagList(docId, documentDto.getTagList(), documentDto.getTagType());
			executeCreateUserList(docId, documentDto.getUserList());
			
//			if (content!=null) {
//				List<String> hlist = HanLPExtractor.phrase(content.getText());
//				if (hlist==null) {
////					hlist = HanLPExtractor.keyword(content.getText(), 5);
//				}
//				executeCreateStringTagList(docId,hlist,TagType.DOCUMENT_SYSTEMEXTRACT);
//			}
			
		}
		return document;
	}
	
	@Override
	@Transactional
	public Document createDocument(Document document) {
		return executeCreateDocument(document);
	}
	
	@Override
	@Transactional
	public DocumentComment createComment(Integer docId, DocumentComment comment) {
		return executeCreateComment(docId, comment);
	}

	@Override
	@Transactional
	public DocumentContent createContent(Integer docId, DocumentContent content) {
		return executeCreateContent(docId, content);
	}
	
	@Override
	@Transactional
	public DocumentTag createTag(Integer docId, Integer tagId, String tagName) {
		DocumentTag dt = new DocumentTag();
		dt.setDocId(docId);
		dt.setTagId(tagId);
		dt.setTagName(tagName);
		return documentTagRepository.save(dt);
	}
	
	@Override
	@Transactional
	public DocumentTag createTag(Integer docId, String tagName, String tagType) {
		return this.executeCreateTag(docId, tagName, tagType);
	}
	
	@Override
	@Transactional
	public List<DocumentTag> createTagList(Integer docId, List<DocumentTag> tagList, String tagType) {
		return executeCreateTagList(docId, tagList, tagType);
	}
	@Override
	@Transactional
	public List<User> createUserList(Integer docId, List<User> userList) {
		return executeCreateUserList(docId, userList);
	}
	
	
	@Override
	@Transactional
	public Document update(Integer docId, DocumentDto documentDto) {
		Document ret = null;
		if(docId!=null&&docId>0&&documentDto!=null) {
			ret = executeUpdateDocument(docId,documentDto.getDocument());
			executeUpdateContent(docId,documentDto.getContent().getId(),documentDto.getContent());
			executeUpdateTagList(docId,documentDto.getTagList(),documentDto.getTagType());
			executeUpdateUserList(docId,documentDto.getUserList());
		}
		
		return ret;
	}
	
	@Override
	@Transactional
	public Document updateDocument(Integer docId, Document document) {
		return executeUpdateDocument(docId,document);
	}
	
	@Override
	@Transactional
	public DocumentContent updateContent(Integer docId, Integer contentId, DocumentContent content) {
		return executeUpdateContent(docId,contentId,content);
	}
	
	@Override
	@Transactional
	public DocumentComment updateComment(Integer docId, Integer commentId, DocumentComment comment) {
		return executeUpdateComment(docId,commentId,comment);
	}
	
	@Override
	@Transactional
	public List<DocumentTag> updateTagList(Integer docId, List<DocumentTag> tagList, String tagType) {
		return executeUpdateTagList(docId,tagList,tagType);
	}
	
	@Override
	@Transactional
	public List<User> updateUserList(Integer docId, List<User> userList) {
		return executeUpdateUserList(docId,userList);
	}
	
	@Override
	public DocumentDto findByUuid(String uuid) {
		Document doc = documentRepository.findOneByUuid(uuid).get();
		return findById(doc.getId());
	}
	
	@Override
	public DocumentDto findById(Integer id){
		DocumentDto ret = new DocumentDto();
		Document doc = documentRepository.findOneById(id).get();
		Integer docId = doc.getId();
		DocumentContent dctt = documentContentRepository.findFirstByDocIdOrderByIdDesc(docId);

		List<DocumentComment> dcmtList = documentCommentRepository.findByDocIdOrderByIdAsc(docId);
		List<String> types = new ArrayList<String>();
		types.add(TagType.DOCUMENT_USERDEFINE);
		types.add(TagType.DOCUMENT_SYSTEMEXTRACT);
		types.add(TagType.DOCUMENT_PROJECT);
		types.add(TagType.DOCUMENT_PRODUCT);
		
		List<DocumentTag> dtagList = documentTagRepository.findByDocIdAndTagTypeIn(docId,types);
		List<User> duserList = documentUserFinderRepository.findUserListByDocId(docId);
		Optional<User> creator = documentUserFinderRepository.findCreatorByDocId(docId);
		List<DocumentContent> dcttList = documentContentRepository.findVersionsByDocIdOrderByIdDesc(docId);
		
		ret.setId(docId);
		ret.setName(doc.getName());
		ret.setDocument(doc);
		ret.setContent(dctt);
		ret.setContentList(dcttList);
		ret.setCommentList(dcmtList);
		ret.setTagList(dtagList);
		ret.setUserList(duserList);
		ret.setCreator(creator.orElse(new User()));
		return ret;
	}
	@Override
	public Optional<User> findCreator(Integer docId){
		return documentUserFinderRepository.findCreatorByDocId(docId);
	}
	
	@Override
	public Optional<DocumentContent> findContentById(Integer docId, Integer contentId){
		return documentContentRepository.findOneByDocIdAndId(docId,contentId);
	}

	@Override
	public List<DocumentContent> findContentListByDocid(Integer docId){
		return documentContentRepository.findByDocIdOrderByIdDesc(docId);
	}

	@Override
	public List<DocumentContent> findContentVersionsByDocid(Integer docId){
		return documentContentRepository.findVersionsByDocIdOrderByIdDesc(docId);
	}
	@Override
	public List<DocumentComment> findCommentListByDocid(Integer docId){
		return documentCommentRepository.findByDocIdOrderByIdAsc(docId);
	}

	@Override
	public Optional<Document> findDocumentByUuid(String uuid){
		return documentRepository.findOneByUuid(uuid);
	}

	@Override
	public Optional<Document> findDocumentById(Integer id){
		return documentRepository.findOneById(id);
	}
	
	@Override
	public List<Document> findDocumentListByExample(Document entity){
		Example<Document> example =Example.of(entity);
		return documentRepository.findAll(example);
	}
	
	@Override
	public List<Document> findDocumentListByExample(Document document, Sort sort){
		Example<Document> example =Example.of(document);
		return documentRepository.findAll(example, sort);
	}
	
	@Override
	public Page<Document> findDocumentListByExample(Document document, Pageable pageable){
		Example<Document> example =Example.of(document);
		return documentRepository.findAll(example, pageable);
	}

	@Override
	public Page<Document> findDocumentListByNameLike(String name, Pageable pageable) {
		return documentRepository.findByNameContainingIgnoreCase(name, pageable);
	}
	@Override
	public Page<Document> findDocumentListByNameOrPinyinLike(String name, Pageable pageable){
		return documentRepository.findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(name, name, pageable);
	}
	@Override
	public Page<Document> findDocumentListByCreatedBy(String userid, Pageable pageable){
		return documentRepository.findByCreatedBy(userid, pageable);
	}
	@Override
	public Page<Document> findDocumentListByTagName(String tagName, Pageable pageable){
		return documentRepository.findByTagName(tagName, pageable);
	}

	@Override
	public Long countDocumentByExample(Document entity){
		Example<Document> example =Example.of(entity);
		return documentRepository.count(example);
	}
}
