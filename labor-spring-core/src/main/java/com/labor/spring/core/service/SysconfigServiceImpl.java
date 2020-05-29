package com.labor.spring.core.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.StringUtil;
import com.labor.spring.core.entity.Sysconfig;

@Service
public class SysconfigServiceImpl implements SysconfigServiceIntf{
	@Autowired
	private SysconfigRepository sysconfigRepository;
	
	@Override
	@Transactional
	public Sysconfig save(Sysconfig sysconfig) {	
		Sysconfig sc = sysconfigRepository.findByKeyAndStatusIgnoreCase(sysconfig.getKey(),CommonConstants.ACTIVE);
		if (sc!=null&&sc.getId()!=null&&sc.getId()>0&&!sc.getId().equals(sysconfig.getId())) {
			throw new ServiceException("the key exists.");
		}
		return sysconfigRepository.save(sysconfig);
	}
	
	@Override
	@Transactional
	public String createDefaultSysconfig() {
		LogManager.getLogger().info("************createDefaultSysconfig***************");
		int count = 0;
		String str = this.findValueByKey(SysconfigConstants.KEY_PAGE_SIZE);
		if (StringUtil.isEmpty(str)){
			//create one
			Sysconfig tmp = new Sysconfig();
			tmp.setKey(SysconfigConstants.KEY_PAGE_SIZE);
			tmp.setValue(String.valueOf(SysconfigConstants.DEFAULT_PAGE_SIZE));
			tmp.setStatus(CommonConstants.ACTIVE);
			sysconfigRepository.save(tmp);
			count++;
		} 
			
		str=this.findValueByKey(SysconfigConstants.KEY_USER_STATUS);
		if (StringUtil.isEmpty(str)) {
			Sysconfig tmp = new Sysconfig();
			tmp.setKey(SysconfigConstants.KEY_USER_STATUS);
			tmp.setValue(String.valueOf(SysconfigConstants.DEFAULT_USER_STATUS));
			tmp.setStatus(CommonConstants.ACTIVE);
			tmp.setDescription("Every signed up user need to be opened by admin if set to 0.");
			sysconfigRepository.save(tmp);
			count++;
		}
		
		str=this.findValueByKey(SysconfigConstants.KEY_FINGERPRINT_STATUS);
		if (StringUtil.isEmpty(str)) {
			Sysconfig tmp = new Sysconfig();
			tmp.setKey(SysconfigConstants.KEY_FINGERPRINT_STATUS);
			tmp.setValue(String.valueOf(SysconfigConstants.DEFAULT_FINGERPRINT_STATUS));
			tmp.setStatus(CommonConstants.ACTIVE);
			tmp.setDescription("Every client need to be opened by admin if set to 0.");
			sysconfigRepository.save(tmp);
			count++;
		} 
		return String.valueOf(count);
	}	
	
	@Override
	public String initialization() {
		LogManager.getLogger().info("************initSysconfig2Constants***************");
		int count = 0;
		String str = this.findValueByKey(SysconfigConstants.KEY_PAGE_SIZE);
		if (!StringUtil.isEmpty(str)&&StringUtil.isNumeric(str)) {
			SysconfigConstants.DEFAULT_PAGE_SIZE
				=Integer.valueOf(str).intValue();
			count++;
		}
		str=this.findValueByKey(SysconfigConstants.KEY_USER_STATUS);
		if (!StringUtil.isEmpty(str)) {
			SysconfigConstants.DEFAULT_USER_STATUS = str;
			count++;
		}
		str=this.findValueByKey(SysconfigConstants.KEY_FINGERPRINT_STATUS);
		if (!StringUtil.isEmpty(str)) {
			SysconfigConstants.DEFAULT_FINGERPRINT_STATUS = str;
			count++;
		}
		return String.valueOf(count);
	}
	
	@Override
	public Sysconfig findById(Long id) {
		return sysconfigRepository.findById(id).orElse(null);
	}
	@Override
	public List<Sysconfig> findList(Sort sort){
		return sysconfigRepository.findAll(sort);
	}
	@Override
	public String findValueByKey(String key){
		Sysconfig sysconfig = sysconfigRepository.findByKeyAndStatusIgnoreCase(key,CommonConstants.ACTIVE);
		if (sysconfig!=null) {
			return StringUtil.safeToString(sysconfig.getValue());
		} else {
			return null;
		}
	}
	@Override
	public List<Sysconfig> findListByKeyStartingWith(String key) {
		return sysconfigRepository.findByKeyStartingWith(key);
	}
	

}
