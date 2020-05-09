package com.labor.spring.system.ppp.entity.gallery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.labor.common.util.StringUtil;
import com.labor.spring.base.AbstractEntity;
import com.labor.spring.core.GlobalInfo;
import com.labor.spring.util.WebUtil;
@Entity
@Table(name = "tbl_gallery_image") 
public class GalleryImage extends AbstractEntity implements Serializable {

	private static final long serialVersionUID = 3657024975550343690L;
	public static final String DOWNLOAD_URL = "/rest/galleries/images/";

	@Id
    @GeneratedValue 
    @Column(name="gi_id")
    private Integer id;

    @Column(name="gi_order")
    private Integer order;
    
    @Column(name="gi_name")
    private String name;

    @Column(name="gi_namepinyin")
    private String namePinyin;

    @Column(name="gi_caption")
    private String caption;

    @Column(name="gi_url",updatable = false)
    private String url;
    
    @Column(name="ga_id")
    private Integer gaId;

    @Column(name="oh_id")
    private Integer ohId;

    @Column(name="oh_filename")
    private String fileName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamePinyin() {
		return namePinyin;
	}

	public void setNamePinyin(String namePinyin) {
		this.namePinyin = namePinyin;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Integer getGaId() {
		return gaId;
	}

	public void setGaId(Integer gaId) {
		this.gaId = gaId;
	}

	public Integer getOhId() {
		return ohId;
	}

	public void setOhId(Integer ohId) {
		this.ohId = ohId;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getUrl() {
		if(StringUtil.isEmpty(url)) {
			return GlobalInfo.CONTEXT_PATH + DOWNLOAD_URL + getUuid() + "/entity";
		} else {
			return url;
		}
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
}
