package com.labor.spring.system.ppp.api.product;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.labor.base.subject.SubjectServiceImpl;
import com.labor.base.subject.SubjectServiceIntf;
import com.labor.common.exception.ServiceException;
import com.labor.common.poi.ExcelUtil;
import com.labor.common.service.ServiceTransactionProxy;
import com.labor.common.util.FileUtil;
import com.labor.common.util.StringUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.core.service.SysconfigConstants;
import com.labor.spring.system.ppp.api.document.DocumentDto;
import com.labor.spring.system.ppp.api.document.DocumentServiceIntf;
import com.labor.spring.system.ppp.api.document.DocumentStatus;
import com.labor.spring.system.ppp.api.gallery.GalleryServiceIntf;
import com.labor.spring.system.ppp.api.oss.ObjectStorageServiceIntf;
import com.labor.spring.system.ppp.api.tag.TagType;
import com.labor.spring.system.ppp.entity.document.Document;
import com.labor.spring.system.ppp.entity.document.DocumentComment;
import com.labor.spring.system.ppp.entity.document.DocumentContent;
import com.labor.spring.system.ppp.entity.gallery.Gallery;
import com.labor.spring.system.ppp.entity.gallery.GalleryImage;
import com.labor.spring.system.ppp.entity.product.Product;
import com.labor.spring.system.ppp.entity.product.ProductDocument;
import com.labor.spring.system.ppp.util.ApplicationProperties;
import com.labor.spring.util.WebUtil;

@RestController
@RequestMapping("/rest/products")
public class ProductRestController {

	@Autowired
	private ApplicationProperties properties;
	@Autowired
	private DocumentServiceIntf documentService;
	@Autowired
	private ProductServiceIntf productService;
	@Autowired
	private GalleryServiceIntf galleryService;
	@Autowired
	private ObjectStorageServiceIntf objectStorageService;

	
	@RequiresPermissions("product:create")
	@RequestMapping(value = {""}, method = RequestMethod.POST)
	public Result create(
					@RequestBody @Valid Product product, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Product ret = productService.createProduct(product);
		return Result.success(ret);
	}
	@RequiresPermissions("product:edit")
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Result updateProduct(
					@PathVariable(value="id") Integer id, 
					@RequestBody @Valid Product product, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Product ret = productService.updateProduct(id,product);
		return Result.success(ret);
	}


	@RequiresPermissions("product:doc:create")
	@RequestMapping(value = {"/{uuid}/docs"}, method = RequestMethod.POST)
	public Result createDocument(
					@PathVariable(value="uuid") String uuid, 
					@RequestBody @Valid DocumentDto documentDto, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Document ret = productService.createDocument(uuid, ProductType.DOCUMENT_DEFAULT,documentDto);
		return Result.success(ret);
	}
	@RequiresPermissions("product:doc:create")
	@RequestMapping(value = {"/{uuid}/galleries"}, method = RequestMethod.POST)
	public Result createGallery(
					@PathVariable(value="uuid") String uuid, 
					@RequestBody @Valid Gallery gallery, 
					BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String message = bindingResult.getFieldError().getDefaultMessage();
	        return Result.failure(ResultCode.FAILURE_PARAM_INVALID,message);
	    }
	    Gallery ret = productService.createGallery(uuid, ProductType.GALLERY_DEFAULT,gallery);
		return Result.success(ret);
	}
	
	@RequestMapping(value = {"/uuid-{uuid}" }, method = RequestMethod.GET)
	public Result findByUuid(
					@PathVariable(value="uuid") String uuid) {
		return Result.success(productService.findByUuid(uuid));
	}
	@RequestMapping(value = {"/name-page-list"}, method = RequestMethod.GET)
	public Result findListByNameLike(
					@RequestParam(value="name", required=false) String name,
					@RequestParam(value="status", required=false) String status,
					@RequestParam(value="sortby", defaultValue="id") String sortby,
					@RequestParam(value="pagesize", required=false) Integer pagesize,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		if(pagesize==null) {
			pagesize = SysconfigConstants.DEFAULT_PAGE_SIZE;
		}
		Pageable pageable = PageRequest.of(page, pagesize, sort);
		name = StringUtil.safeToString(name);
		Page<Product> ret = productService.findListByNameOrPinyinLike(name, pageable);
		return Result.success(ret);
	}

	@RequestMapping(value = {"/{id}/docs/page-list"}, method = RequestMethod.GET)
	public Result findDocumentList(
					@PathVariable(value="id") Integer id,
					@RequestParam(value="sortby", defaultValue="doc_id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.DESC, sortby);
		Pageable pageable = PageRequest.of(page, SysconfigConstants.DEFAULT_PAGE_SIZE, sort);
		Page<Document> ret = productService.findDocumentListByProductId(id, pageable);
		return Result.success(ret);
	}

	@RequestMapping(value = {"/{id}/galleries/page-list"}, method = RequestMethod.GET)
	public Result findGalleryList(
					@PathVariable(value="id") Integer id,
					@RequestParam(value="sortby", defaultValue="ga_id") String sortby,
					@RequestParam(value="page", defaultValue="0") Integer page) {
		Sort sort = new Sort(Sort.Direction.ASC, sortby);
		Pageable pageable = PageRequest.of(page, SysconfigConstants.DEFAULT_PAGE_SIZE, sort);
		Page<Gallery> ret = productService.findGalleryListByProductId(id, pageable);
		return Result.success(ret);
	}	
	
	@RequestMapping(value = {"/{uuid}/cover"}, method = RequestMethod.GET)
	public ResponseEntity<byte[]> findCoverByUuid(
					@PathVariable(value="uuid") String uuid) {
		String fileName = "";
		Gallery gallery = productService.findFirstGalleryByUuid(uuid).orElse(null);
		if (gallery!=null) {
			GalleryImage gi = galleryService.findFistImageByUuid(gallery.getUuid()).orElse(null);
			if (gi!=null) {		
				fileName = gi.getFileName();
			}
		}		
		return objectStorageService.findImageEntityByFileName(fileName,true,true,null,null,null);
	}
	
	@RequestMapping(value = { "/excel" }, method = RequestMethod.GET)
	public void exportExcel(HttpServletResponse response) {

//		ResponseEntity<OutputStream> ret = null;
		
        String[] headRowNames = new String[]{
					        "id",
							"code",
							"name",
							"namePinyin",
							"price",
							"costPrice",
							"marketPrice",
							"currency",
							"size",
							"color",
							"parentId",
							"creationDate",
							"lastUpdateDate"};

        String xlsname = String.valueOf(System.currentTimeMillis());
       
		response.setContentType("APPLICATION/OCTET-STREAM");
		response.setHeader("Content-Disposition", "attachment; filename=" + xlsname + ".xls" );
		OutputStream out = null;
		try {

			List dataList = productService.findListByNameOrPinyinLike("");
			
			out = response.getOutputStream();
			ExcelUtil.export(headRowNames, dataList, out, 5000);
			
//			HttpHeaders headers = new HttpHeaders();
//			headers.add("Content-Disposition", "attachment;filename=" + xlsname + ".xls");
//			HttpStatus statusCode = HttpStatus.OK;
//			ret = new ResponseEntity<OutputStream>(out, headers, statusCode);
			
		} catch (IOException ioe) {
			LogManager.getLogger().error("", ioe);
		} catch (ServiceException se) {
			LogManager.getLogger().error("", se);
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}	
			} catch (IOException ioe) {
				LogManager.getLogger().error("", ioe);
			}
		}

//		return ret;
	}
}
