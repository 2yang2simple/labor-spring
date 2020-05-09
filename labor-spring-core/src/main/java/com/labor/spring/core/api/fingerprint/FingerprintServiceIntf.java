package com.labor.spring.core.api.fingerprint;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.labor.spring.core.entity.Fingerprint;
import com.labor.spring.core.entity.FingerprintOnline;

public interface FingerprintServiceIntf {
		
	public Fingerprint save(Fingerprint fingerprint);
	public Fingerprint update(Integer id, Fingerprint entity, boolean ignoreNullProperties);
	public Fingerprint create(String value, String type);
	
//	public FingerprintOnline createOnline(FingerprintOnline fingerprintOnline);
//	public FingerprintOnline updateOnline(Integer foId, FingerprintOnline fingerprintOnline);
	public FingerprintOnline saveOnlineBySeesionId(String sessionId, FingerprintOnline fingerprintOnline, boolean ignoreNullProperties);
//	public FingerprintOnline saveOnlineByAuthValueAndAuthType(String authValue, String authType, FingerprintOnline entity, boolean ignoreNullProperties); 
	public FingerprintOnline saveOnline(Integer userId,
										String fpType,
										String fpValue,
										String authType,
										String authValue, 
										FingerprintOnline entity, 
										boolean ignoreNullProperties);

	
	public void deleteOnlineById(Integer id);
	public void deleteOnline(String sessionId);
//	public void deleteOnline();
	public void deleteOnlineSession();
	public void deleteOnlineByAuthValue(String authValue);
	public void deleteOnlineByAuthValueAndAuthType(String authValue,String authType);
	
	public Fingerprint findById(Integer id);
	public Fingerprint findByValueAndType(String value, String type);

	public List<Fingerprint> findList(Sort sort);
	public List<Fingerprint> findListByValueStartingWith(String value);

    public Page<Fingerprint> findList(Pageable pageable);
	public Page<Fingerprint> findListByValueStartingWith(String value,Pageable pageable);
	
    public Page<FingerprintOnline> findFoList(Pageable pageable);
	public Page<FingerprintOnline> findFoListByValueStartingWith(String value,Pageable pageable);

	public FingerprintOnline findFoBySessionId(String SessionId);
	public FingerprintOnline findFoByAuthCode(String authCode);
	public FingerprintOnline findFoByAuthValue(String value);
	public FingerprintOnline findFoByAuthValueAndAuthType(String value, String type);
}
