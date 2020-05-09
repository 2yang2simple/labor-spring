package com.labor.spring.system.ppp.api.product;

import java.util.List;

import org.springframework.data.domain.Page;

import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.product.Product;

public class ProductDto {

	private Product product;
	
	private Page<Document> documentList;
	
	private Page<Gallery> galleryList;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Page<Document> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(Page<Document> documentList) {
		this.documentList = documentList;
	}

	public Page<Gallery> getGalleryList() {
		return galleryList;
	}

	public void setGalleryList(Page<Gallery> galleryList) {
		this.galleryList = galleryList;
	}
	


}
