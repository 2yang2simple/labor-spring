package com.labor.spring.core.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.entity.Fingerprint;
import com.labor.spring.core.entity.FingerprintOnline;
import com.labor.spring.core.entity.User;
import com.labor.spring.util.IgnorePropertiesUtil;


@Service
public class FingerprintServiceImpl implements FingerprintServiceIntf{
	@Autowired
	private FingerprintRepository fingerprintRepository;
	@Autowired
	private FingerprintOnlineRepository fingerprintOnlineRepository;
	
	@Override
	@Transactional
	public Fingerprint save(Fingerprint fingerprint) {	
		return fingerprintRepository.save(fingerprint);
	}

	@Override
	@Transactional
	public Fingerprint update(Long id, Fingerprint entity, boolean ignoreNullProperties) {
		Fingerprint newEntity = entity;
		Fingerprint fp = fingerprintRepository.findById(id).orElse(null);
		if (fp!=null){
			if (ignoreNullProperties) {
				newEntity = fp;
				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(fp));
			}
			return fingerprintRepository.save(newEntity);
		}
		return null;
	}

	@Override
	@Transactional
	public Fingerprint create(String value, String type) {
		Fingerprint ret = fingerprintRepository.findByValueAndType(value, type);
		if (ret == null) {
			Fingerprint fingerprint= new Fingerprint();
			fingerprint.setType(type);
			fingerprint.setValue(value);
			fingerprint.setStatus(SysconfigConstants.DEFAULT_FINGERPRINT_STATUS);
			ret = fingerprintRepository.save(fingerprint);
		}
		return ret;
	}
	
//	@Override
//	@Transactional
//	public FingerprintOnline createOnline(FingerprintOnline fingerprintOnline) {	
//		return fingerprintOnlineRepository.save(fingerprintOnline);
//	}

//	@Override
//	@Transactional
//	public FingerprintOnline updateOnline(Long foId, FingerprintOnline fingerprintOnline) {
//		fingerprintOnline.setId(foId);
//		return fingerprintOnlineRepository.save(fingerprintOnline);
//	}

	@Override
	@Transactional
	public FingerprintOnline saveOnline(Long userId,
										String fpType,
										String fpValue,
										String authType,
										String authValue, 
										FingerprintOnline entity, 
										boolean ignoreNullProperties) {
		FingerprintOnline newEntity = entity;
		FingerprintOnline optional = null;
	
		optional = fingerprintOnlineRepository.findByAuthValue(authValue);
		if (optional==null){
			//FP_TYPE_CANVAS is not unique; web 
			if(StringUtil.isEqualedTrimLower(fpType, WebConstants.FP_TYPE_CANVAS)) {
				optional = fingerprintOnlineRepository.findByUserIdAndFpTypeAndFpValueAndAuthType(userId, fpType, fpValue,authType);
			//fpvalue and fptype defined a unique device
			} else {
				optional = fingerprintOnlineRepository.findByFpValueAndFpType(fpValue, fpType);
			}
		}
		//create new;
		if (optional==null){
			newEntity.setId(null);
			newEntity.setStatus(CommonConstants.ACTIVE);
			
		} else {
			if (ignoreNullProperties) {
				newEntity = optional;
				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(entity));
			} 
		}
//		newEntity.setFpValue(fpValue);
//		newEntity.setFpType(fpType);
//		newEntity.setAuthValue(authValue);
//		newEntity.setAuthType(authType);
		return fingerprintOnlineRepository.save(newEntity);
	}
//
//	@Override
//	@Transactional
//	public FingerprintOnline saveOnlineByAuthValueAndAuthType(String authValue, String authType, FingerprintOnline entity, boolean ignoreNullProperties) {
//		FingerprintOnline newEntity = entity;
//		FingerprintOnline optional = fingerprintOnlineRepository.findByAuthValueAndAuthType(authValue,authType);
//		if (optional!=null){
//			if (ignoreNullProperties) {
//				newEntity = optional;
//				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(entity));
//			} 
//		} else {
//			newEntity.setStatus(CommonConstants.ACTIVE);
//		}
//		newEntity.setAuthValue(authValue);
//		newEntity.setAuthType(authType);
//		return fingerprintOnlineRepository.save(newEntity);
//	}

	@Override
	@Transactional
	public FingerprintOnline saveOnlineBySeesionId(String sessionId, FingerprintOnline entity, boolean ignoreNullProperties) {
		FingerprintOnline newEntity = entity;
		FingerprintOnline optional = fingerprintOnlineRepository.findBySessionId(sessionId);
		if (optional!=null){
			if (ignoreNullProperties) {
				newEntity = optional;
				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(entity));
			} 
		} else {
			newEntity.setId(null);
			newEntity.setStatus(CommonConstants.ACTIVE);
		}
		newEntity.setAuthType(null);
		newEntity.setAuthValue(null);
		newEntity.setSessionId(sessionId);
		return fingerprintOnlineRepository.save(newEntity);
	}
	


	@Override
	@Transactional
	public void deleteOnlineById(Long id) {
		fingerprintOnlineRepository.deleteById(id);
	}
	
	@Override
	@Transactional
	public void deleteOnline(String sessionId) {
		fingerprintOnlineRepository.deleteBySessionId(sessionId);
	}
//	@Override
//	@Transactional
//	public void deleteOnline() {
//		fingerprintOnlineRepository.deleteAll();
//	}	
	@Override
	@Transactional
	public void deleteOnlineSession() {
		fingerprintOnlineRepository.deleteOnlineSession();
	}

	@Override
	@Transactional
	public void deleteOnlineByAuthValueAndAuthType(String authValue,String authType) {
		fingerprintOnlineRepository.deleteByAuthValueAndAuthType(authValue,authType);
	}

	@Override
	@Transactional
	public void deleteOnlineByAuthValue(String authValue) {
		fingerprintOnlineRepository.deleteByAuthValue(authValue);
	}
	
	@Override
	public Fingerprint findById(Long id) {
		return fingerprintRepository.findById(id).orElse(null);
	}
	@Override
	public Fingerprint findByValueAndType(String value, String type){
		return fingerprintRepository.findByValueAndType(value,type);
	}
	
	@Override
	public List<Fingerprint> findList(Sort sort){
		return fingerprintRepository.findAll(sort);
	}
	@Override
	public List<Fingerprint> findListByValueStartingWith(String value) {
		return fingerprintRepository.findByValueStartingWith(value);
	}
	
	@Override
    public Page<Fingerprint> findList(Pageable pageable){
		return fingerprintRepository.findAll(pageable);
	}
	@Override
	public Page<Fingerprint> findListByValueStartingWith(String value,Pageable pageable) {
		return fingerprintRepository.findByValueStartingWith(value,pageable);
	}

	@Override
    public Page<FingerprintOnline> findFoList(Pageable pageable){
		return fingerprintOnlineRepository.findAll(pageable);
	}
	@Override
	public Page<FingerprintOnline> findFoListByValueStartingWith(String value,Pageable pageable) {
		return fingerprintOnlineRepository.findByFpValueStartingWith(value,pageable);
	}
	@Override
	public FingerprintOnline findFoBySessionId(String SessionId) {
		return fingerprintOnlineRepository.findBySessionId(SessionId);
	}
	@Override
	public FingerprintOnline findFoByAuthCode(String authCode) {
		return fingerprintOnlineRepository.findByAuthCode(authCode);
	}
	@Override
	public FingerprintOnline findFoByAuthValue(String value) {
		return fingerprintOnlineRepository.findByAuthValue(value);
	}
	@Override
	public FingerprintOnline findFoByAuthValueAndAuthType(String value, String type) {
		return fingerprintOnlineRepository.findByAuthValueAndAuthType(value, type);
	}
}
