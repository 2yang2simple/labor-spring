package com.labor.spring.system.ppp.api.product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.product.Product;
import com.labor.spring.system.ppp.entity.product.ProductDocument;

public interface ProductServiceIntf {
	
	public Product createProduct(Product entity);
	public Product updateProduct(Integer id, Product entity);
	
	public Document createDocument(Integer productId, String pdType, DocumentDto documentDto);
	public Document createDocument(String productUuid, String pdType, DocumentDto documentDto);
	public Gallery createGallery(Integer productId, String pgType, Gallery gallery);
	public Gallery createGallery(String productUuid, String pgType, Gallery gallery);

	public ProductDto findById(Integer id);
	public ProductDto findByUuid(String uuid);

	public Optional<Product> findProductById(Integer id);
	public Optional<Product> findProductByUuid(String uuid);
	
	public Page<Product> findListByNameOrPinyinLike(String name, Pageable pageable);
	public Page<Document> findDocumentListByProductId(Integer productId, Pageable pageable);
	public Page<Gallery> findGalleryListByProductId(Integer productId, Pageable pageable);
	
	public List<Product> findListByNameOrPinyinLike(String name);
	
	public Optional<Gallery> findFirstGalleryByUuid(String uuid);

}
