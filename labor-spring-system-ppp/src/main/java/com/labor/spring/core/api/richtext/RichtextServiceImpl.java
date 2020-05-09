package com.labor.spring.core.api.richtext;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;

@Service
public class RichtextServiceImpl implements RichtextServiceIntf{
	
	@Autowired
	private RichtextRepository richtextRepository;
	
	@Override
	@Transactional
	public Richtext save(Richtext richtext) {
		if (richtext!=null) {
			if (StringUtil.isEmpty(richtext.getHtml())) {
				richtext.setText("");
			} else {
				richtext.setText(richtext.getHtml().replaceAll("<[^>]*>",""));
			}
		}
		return richtextRepository.save(richtext);
	}
	
	@Override
	@Transactional
	public Richtext create(Richtext richtext) {
		Richtext ret = null;
		if (richtext!=null) {
			/***** [name for ex.] need to unique; START */
			Richtext tmp = new Richtext();
			tmp.setName(richtext.getName());
			tmp.setStatus(CommonConstants.ACTIVE);
			Example<Richtext> example =Example.of(tmp);
			long count = richtextRepository.count(example);
			if (count>0) {
				throw new RuntimeException("the Name exists.");
			}
			/***** [name for ex.] need to unique; END */

			richtext.setId(null);
			richtext.setStatus(CommonConstants.ACTIVE);
			richtext.setUuid(TokenUtil.generateUUID());
			if (StringUtil.isEmpty(richtext.getHtml())) {
				richtext.setText("");
			} else {
				richtext.setText(richtext.getHtml().replaceAll("<[^>]*>",""));
			}
			return richtextRepository.save(richtext);
		}
		return ret;
	}
	/**
	 * !!!!!
	 * this method need to validate the parameters [key for ex.] which to be unique
	 * checking the data already exist and the data is itself when update;
	 */
	@Override
	@Transactional
	public Richtext update(Richtext richtext) {
		Richtext ret = null;
		if (richtext!=null&richtext.getId()!=null&&richtext.getId()>0) {
			/***** [name for ex.] need to unique; START */
			Richtext tmp = new Richtext();
			tmp.setName(richtext.getName());
			tmp.setStatus(CommonConstants.ACTIVE);
			Example<Richtext> example =Example.of(tmp);
			List<Richtext> list = richtextRepository.findAll(example);
			if (list==null||list.size()==0) {
				tmp = null;
			} else if (list.size()==1) {
				tmp = list.get(0);
			} else {
				throw new ServiceException("the Name exists."); 
			}
			if (tmp!=null&&!richtext.getId().equals(tmp.getId())) {
				throw new ServiceException("the Name exists."); 
			}
			/***** [name for ex.] need to unique; END */
			
			if (StringUtil.isEmpty(richtext.getHtml())) {
				richtext.setText("");
			} else {
				richtext.setText(richtext.getHtml().replaceAll("<[^>]*>",""));
			}
			ret = richtextRepository.save(richtext);
		}
		return ret;
	}
	
	@Override
	public List<Richtext> findList(Sort sort){
		return richtextRepository.findAll(sort);
	}
	@Override
	public List<Richtext> findListByNameStartingWith(String name){
		return richtextRepository.findByNameStartingWith(name);
	}
	@Override
	public Richtext findByUuid(String uuid) {
		return richtextRepository.findByUuid(uuid);
	}
	@Override
	public Richtext findByName(String name) {
		return richtextRepository.findByNameAndStatus(name,CommonConstants.ACTIVE);
	}
	@Override
	public Richtext findById(Integer id) {
		return richtextRepository.findById(id);
	}
}
