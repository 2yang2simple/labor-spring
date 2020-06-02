package com.labor.spring.system.ppp.api.product;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;

import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.system.ppp.HanLPExtractor;
import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.api.document.DocumentServiceIntf;
import com.labor.spring.system.ppp.api.gallery.GalleryServiceIntf;
import com.labor.spring.system.ppp.api.product.ProductRepository;
import com.labor.spring.system.ppp.api.tag.TagType;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.document.DocumentTag;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.product.Product;
import com.labor.spring.system.ppp.entity.product.ProductDocument;
import com.labor.spring.system.ppp.entity.product.ProductGallery;
import com.labor.spring.util.IgnorePropertiesUtil;

@Service
public class ProductServiceImpl implements ProductServiceIntf {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductGalleryRepository productGalleryRepository;
	@Autowired
	private ProductDocumentRepository productDocumentRepository;
	@Autowired
	private ProductDocumentFinderRepository productDocumentFinderRepository;
	@Autowired
	private ProductGalleryFinderRepository productGalleryFinderRepository;
	@Autowired
	private DocumentServiceIntf documentService;
	@Autowired
	private GalleryServiceIntf galleryService;

	private Product executeCreateProduct(Product entity) {
		Product ret = null;
		if (entity!=null) {
			
			entity.setId(null);
			
			entity.setNamePinyin(HanLPExtractor.pinyin(entity.getName()));
			entity.setStatus(CommonConstants.ACTIVE);
			entity.setProductStatus(ProductStatus.OPENED);
			entity.setUuid(TokenUtil.generateUUID());
//			entity.setCode("P"+TokenUtil.generateDateNum());
			Example<Product> example =Example.of(new Product());
			long pcount = productRepository.count(example);
			entity.setCode("PD"+StringUtil.prefixZero(Long.toString(pcount+1), 5));
			ret = productRepository.save(entity);
		}
		return ret;
	}
	
	private Product executeUpdateProduct(Integer id, Product entity, boolean ignoreNullProperties) {
		Product newEntity = entity;
		Optional<Product> optional = productRepository.findOneById(id);
		if (optional.isPresent()){
			if (ignoreNullProperties) {
				newEntity = optional.get();
				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(entity));
			}
			if(!StringUtil.isEmpty(entity.getName())) {
				newEntity.setNamePinyin(HanLPExtractor.pinyin(newEntity.getName()));
			}
			return productRepository.save(newEntity);
		}
		return null;
	}	

	@Override
	@Transactional
	public Product createProduct(Product entity) {
		return executeCreateProduct(entity);
	}
	
	@Override
	@Transactional
	public Product updateProduct(Integer id, Product entity) {
		return executeUpdateProduct(id,entity,true);
	}
	
	@Override
	@Transactional
	public Document createDocument(String productUuid, String pdType, DocumentDto documentDto) {
		Optional<Product> optional = productRepository.findOneByUuid(productUuid);
		return createDocument(optional.get().getId(),pdType,documentDto);
	}
	
	@Override
	@Transactional
	public Document createDocument(Integer productId, String pdType, DocumentDto documentDto) {
		Document ret = null;

		Optional<Product> optional = productRepository.findOneById(productId);
		if (optional.isPresent()){
			Product product = optional.get();
			ret = documentService.create(documentDto);
			//create a product tag to the document.
			DocumentTag dt = null;
			dt = documentService.createTag(ret.getId(), product.getCode(), TagType.DOCUMENT_PRODUCT);
			//create product document map.
			ProductDocument pd = new ProductDocument();
			pd.setDocId(ret.getId());
			pd.setProductId(product.getId());
			pd.setPdType(pdType);
			pd.setPdStatus(ProductStatus.DOCUMENT_OPENED);
			ProductDocument newpd = productDocumentRepository.save(pd);
			if (ret==null||dt==null||newpd==null) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
				LogManager.getLogger().error("create product document error, rollback.");
				return null;
			}
		}
		return ret;
	}
	@Override
	@Transactional
	public Gallery createGallery(String productUuid, String pgType, Gallery gallery) {
		Optional<Product> optional = productRepository.findOneByUuid(productUuid);
		return createGallery(optional.get().getId(),pgType,gallery);
	}
	@Override
	@Transactional
	public Gallery createGallery(Integer productId, String pgType, Gallery gallery) {
		Gallery ret = null;

		Optional<Product> optional = productRepository.findOneById(productId);
		if (optional.isPresent()){
			Product product = optional.get();
			ret = galleryService.create(gallery);

			//create product gallery map.
			ProductGallery pg = new ProductGallery();
			pg.setGaId(ret.getId());
			pg.setProductId(product.getId());
			pg.setPgType(pgType);
			pg.setPgStatus(ProductStatus.GALLERY_OPENED);
			ProductGallery newpg = productGalleryRepository.save(pg);
			if (ret==null||newpg==null) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
				LogManager.getLogger().error("create product gallery error, rollback.");
				return null;
			}
		}
		return ret;
	}
	
	
	@Override
	public ProductDto findById(Integer id) {
		Product product = productRepository.findOneById(id).orElse(null);
		if (product==null) {
			return null;
		}
		return findByUuid(product.getUuid());
	}
	@Override
	public ProductDto findByUuid(String uuid) {
		ProductDto ret = new ProductDto();
		Product product = productRepository.findOneByUuid(uuid).orElse(null);
		if (product==null) {
			return null;
		}
		ret.setProduct(product);
		Pageable docpageable = PageRequest.of(0, SysconfigConstants.DEFAULT_PAGE_SIZE, new Sort(Sort.Direction.DESC, "doc_id"));
		Page<Document> documentList = productDocumentFinderRepository.findDocumentList(product.getId(),docpageable);
		ret.setDocumentList(documentList);

		Pageable gapageable = PageRequest.of(0, SysconfigConstants.DEFAULT_PAGE_SIZE, new Sort(Sort.Direction.ASC, "ga_id"));
		Page<Gallery> galleryList = productGalleryFinderRepository.findGalleryList(product.getId(), CommonConstants.ACTIVE, gapageable);
		ret.setGalleryList(galleryList);
		return ret;
	}

	@Override
	public Optional<Product> findProductById(Integer id) {
		return productRepository.findOneById(id);
	}
	@Override
	public Optional<Product> findProductByUuid(String uuid){
		return productRepository.findOneByUuid(uuid);
	}

	@Override
	public Page<Product> findListByNameOrPinyinLike(String name, Pageable pageable) {
		return productRepository.findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(name, name, pageable);
	}
	@Override
	public List<Product> findListByNameOrPinyinLike(String name){
		return productRepository.findByNameContainingIgnoreCaseOrNamePinyinContainingIgnoreCase(name, name);
		}
	@Override
	public Page<Document> findDocumentListByProductId(Integer productId, Pageable pageable) {
		return productDocumentFinderRepository.findDocumentList(productId, pageable);
	}

	@Override
	public Page<Gallery> findGalleryListByProductId(Integer productId, Pageable pageable) {
		return productGalleryFinderRepository.findGalleryList(productId, CommonConstants.ACTIVE, pageable);
	}
	
	@Override
	public Optional<Gallery> findFirstGalleryByUuid(String uuid){
		return productGalleryFinderRepository.findFistGallery(productRepository.findOneByUuid(uuid).get().getId(), CommonConstants.ACTIVE);
	}
}
