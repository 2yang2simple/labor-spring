package com.labor.spring.core.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.GoogleAuthenticator;
import com.labor.common.util.SnowflakeIdWorker;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.bean.Result;
import com.labor.spring.bean.ResultCode;
import com.labor.spring.constants.WebConstants;
import com.labor.spring.core.entity.Fingerprint;
import com.labor.spring.core.entity.User;
import com.labor.spring.core.entity.UserFingerprint;
import com.labor.spring.core.entity.UserHistory;
import com.labor.spring.core.entity.UserPassword;
import com.labor.spring.core.entity.UserRole;
import com.labor.spring.util.WebUtil;


@Service
public class UserServiceImpl implements UserServiceIntf{

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserHistoryRepository userHistroyRepository;
	@Autowired
	private UserFingerprintRepository userFingerprintRepository;
	@Autowired
	private UserPasswordRepository userPasswordRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	public static final String PWD_SALT = "1686";
	
	@Override
	@Transactional
	public User save(User user) {	
	    return userRepository.save(user);
	}
	@Override
	@Transactional
	public User update(User user) {	
		User ret = new User();
		// only for update
		if(user!=null&&user.getId()>0) {
			ret = userRepository.findById(user.getId()).orElse(null);
			if (ret!=null) {	
				String checkmsg = checkExisted(user);
				if ("none".equals(checkmsg)) {			
					//save current to history;
					UserHistory userHistory = new UserHistory();
					userHistory.setUserid(ret.getId());
					userHistory.setUuid(ret.getUuid());
					userHistory.setSno(ret.getSno());
					userHistory.setWeixin(ret.getWeixin());
					userHistory.setCellPhone(ret.getCellPhone());
					userHistory.setEmail(ret.getEmail());
					userHistory.setName(ret.getName());
					userHistory.setRealName(ret.getRealName());
					userHistory.setRealNameEn(ret.getRealNameEn());
					userHistory.setDescription(ret.getDescription());
					userHistory.setStatus(ret.getStatus());
					userHistroyRepository.save(userHistory);
					
					//can not change status except using saveUserStatus()
					user.setStatus(ret.getStatus());
					//can not change sno;
					user.setSno(ret.getSno());
					//can not change pwdmodify except using createpasword();
					user.setPwdmodify(ret.getPwdmodify());
					ret = userRepository.save(user);
				}
			}
		}
	    return ret;
	}
    @Override
	@Transactional
    public User updatePasswordModifyCode(Long userid, String pwdmodify) {
    	User ret = null;
    	User user = userRepository.findById(userid).orElse(null);
    	if (user!=null) {
    		user.setPwdmodify(pwdmodify);
    		ret = userRepository.save(user);
    	}
    	return ret;
    }
    @Override
	@Transactional
    public User updateStatusByUuid(String uuid, String status) {
    	User ret = null;
    	User user = userRepository.findByUuidIgnoreCase(uuid);
    	if (user!=null) {
    		user.setStatus(status);
    		ret = userRepository.save(user);
    	}
    	return ret;
    }
	
	//only way to create a user;
    @Override
	@Transactional
    public User create(String name, String sno, 
    					String googleSecretKey, 
    					String cellPhone, String weixin, String email,
    					String pwd, String uuid, String status) {
    	User ret = null;
//    	User newuser = new User();
//
//    	if(!StringUtil.isEmpty(cellPhone)) {
//	    	ret = userRepository.findByCellPhoneIgnoreCase(cellPhone);
//	    	if (ret!=null&&ret.getId()>0){
//	    		return null;
//	    	}
//    	}
//    	if(!StringUtil.isEmpty(weixin)) {
//	    	ret = userRepository.findByWeixinIgnoreCase(weixin);
//	    	if (ret!=null&&ret.getId()>0){
//	    		return null;
//	    	}
//    	}
//    	if(!StringUtil.isEmpty(email)) {
//	    	ret = userRepository.findByEmailIgnoreCase(email);
//	    	if (ret!=null&&ret.getId()>0){
//	    		return null;
//	    	}
//    	}
//    	
//    	if (StringUtil.isEmpty(name)) {
//    		name = TokenUtil.generateUNum();
//    	}
//    	if (StringUtil.isEmpty(sno)) {
//    		//created by singup in contorller;
//    		sno = WebUtil.nextSno(); 
//    	}
//    	if (StringUtil.isEmpty(googleSecretKey)) {
//    		googleSecretKey = GoogleAuthenticator.generateSecretKey();
//    	}
//    	ret = userRepository.findByNameIgnoreCase(name);
//    	if (ret!=null&&ret.getId()>0){
//    		return null;
//    	}
//    	ret = userRepository.findByGoogleSecretKeyIgnoreCase(googleSecretKey);
//    	if (ret!=null&&ret.getId()>0){
//    		return null;
//    	}
//    	ret = userRepository.findBySnoIgnoreCase(sno);
//    	if (ret!=null&&ret.getId()>0){
//    		return null;
//    	}
//    	//create long id
//    	newuser.setId(TokenUtil.generateLongID());
//		newuser.setName(name);
//		newuser.setSno(sno);
//		//e50ad1f2da5443f988af5f43d90f50c6
//		newuser.setUuid(TokenUtil.generateUUID(uuid));
//		newuser.setStatus(status);
//		newuser.setGoogleSecretKey(googleSecretKey);
//		newuser.setCellPhone(cellPhone);
//		newuser.setWeixin(weixin);
//		newuser.setEmail(email);
//		ret = userRepository.save(newuser);
//		
//		if(!StringUtil.isEmpty(pwd)) {
////			UserPassword newup = new UserPassword();
////			newup.setUserid(ret.getId());
////			newup.setPwdmd5(TokenUtil.md5(user.getUuid()+PWD_SALT+TokenUtil.md5(pwd)));
////			newup.setStatus(CommonConstants.ACTIVE);
////			userPasswordRepository.save(newup);
//			createPassword(ret.getId(),pwd);
//		}

    	return ret;
    }
    /**
     * password tips:
     * 1,https request;                ----as-is: no    
     * 2,transfer password with rsa;   ----as-is: md5 with timestamp 
     * 3,BCrypt password with salt     ----as-is: md5 with salt;
     */
    //create a new password by md5(original);
    @Override
	@Transactional
    public boolean createPassword(Long userid, String pwd) {
		User user = userRepository.findById(userid).orElse(null);
		if (user==null||pwd==null) {
			LogManager.getLogger().error("pwd is null");
			return false;
		}
		LogManager.getLogger().debug("getUuid:"+user.getUuid());
    	UserPassword newup = new UserPassword();
		newup.setUserid(userid);
		/****
		 *  pwdmd5 = md5(md5(uuid+salt)+md5(pwd))
		 */
		String pwdmd5 = TokenUtil.md5(createPasswordSalt(user.getUuid())+TokenUtil.md5(pwd));
		newup.setPwdmd5(pwdmd5);
		newup.setStatus(CommonConstants.ACTIVE);
		userPasswordRepository.save(newup);
		
		//clear the user modify code;
		user.setPwdmodify("");
		userRepository.save(user);
		
		//inactive all the rememberme of this user;
		userFingerprintRepository.updateRemembermeByUserid(CommonConstants.INACTIVE, userid);
		return true;
    }
    @Override
    public String createPasswordSalt(String saltseed) {
    	String ret = null;
    	if (StringUtil.isEmpty(saltseed)) {
    		throw new ServiceException("saltseed is null");
    	}
    	/****
		 *  salt = md5(seed+pwd_salt)
		 */
    	ret = TokenUtil.md5(saltseed+PWD_SALT);
    	return ret;
    }
    @Override
    public String createPasswordSaltByAccount(String account) {
    	String ret = null;
    	String saltseed = "faked.";
		User user = findByName(account);
		if (user==null||StringUtil.isEmpty(user.getUuid())) {
			user = findByCellPhone(account);
		}
		if (user!=null&&!StringUtil.isEmpty(user.getUuid())) {
			saltseed = user.getUuid();
		}
		ret = createPasswordSalt(saltseed);
    	return ret;
    }
    @Override
	@Transactional
    public String createPasswordModifyCode(String uuid) {
    	String ret = "";
    	User user = userRepository.findByUuidIgnoreCase(uuid);
    	if (user!=null) {
    		ret = TokenUtil.generateUUID();
    		user.setPwdmodify(ret);
    		userRepository.save(user);
    	}
    	return ret;
    }

    @Override
	@Transactional
    public String createUserRoles(Long userId, List<UserRole> list) {
    	String ret = null;
    	int count = 0;
		if (list!=null&&list.size()>0){
			int size = list.size();
			userRoleRepository.deleteByUserid(userId);
			for (int i=0;i<size;i++) {
				UserRole ur = (UserRole)list.get(i);
				userRoleRepository.save(ur);
				count = count+1;
			}
			ret = String.valueOf(count);
		}
		return ret;
    }
    @Override
	@Transactional
    public UserFingerprint createUserFingerprint(String value, String type, Long fpid, 
    											Long userid, String rememberMe) {
    	UserFingerprint ret = null;
    	ret = userFingerprintRepository.findFirstByFpidAndUseridOrderByIdDesc(fpid,userid);
    	rememberMe = (rememberMe==null)?CommonConstants.INACTIVE:rememberMe;
    	//if exist , no create;
    	if (ret!=null){
    		userFingerprintRepository.updateRemembermeByValueAndType(CommonConstants.INACTIVE, ret.getValue(), ret.getType());
    		//update
    		ret.setRememberMe(rememberMe);
    		ret.setDescription(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
    		ret = userFingerprintRepository.save(ret);
    		return ret;
    	}
    	UserFingerprint ufp = new UserFingerprint();
    	ufp.setUserid(userid);
    	ufp.setFpid(fpid);
		ufp.setValue(value);
		ufp.setType(type);
		ufp.setRememberMe(rememberMe);
		ufp.setStatus(CommonConstants.ACTIVE);
		ret = userFingerprintRepository.save(ufp);
    	return ret;
    }

	@Override
    public String checkExisted (User user) {
    	User dbuser = new User();
    	if (!StringUtil.isEmpty(user.getName())) {
	    	dbuser = userRepository.findByNameIgnoreCase(user.getName());
	    	if (dbuser!=null&&dbuser.getId()!=null&&dbuser.getId()>0&&!dbuser.getId().equals(user.getId())) {
	    		LogManager.getLogger().debug("xxxxx:"+dbuser.getId()+"|"+user.getId());
	    		return user.getName()+" exist.-1";
	    	}
    	}
    	if (!StringUtil.isEmpty(user.getCellPhone())) {
	    	dbuser = userRepository.findByCellPhoneIgnoreCase(user.getCellPhone());
	    	if (dbuser!=null&&dbuser.getId()!=null&&dbuser.getId()>0&&!dbuser.getId().equals(user.getId())) {
	    		return user.getCellPhone()+" exist.-2";
	    	}
    	}
    	if (!StringUtil.isEmpty(user.getEmail())) {
	    	dbuser = userRepository.findByEmailIgnoreCase(user.getEmail());
	    	if (dbuser!=null&&dbuser.getId()!=null&&dbuser.getId()>0&&!dbuser.getId().equals(user.getId())) {
	    		return user.getEmail()+"4 exist.-4";
	    	}
    	}
    	if (!StringUtil.isEmpty(user.getWeixin())) {
	    	dbuser = userRepository.findByWeixinIgnoreCase(user.getWeixin());
	    	if (dbuser!=null&&dbuser.getId()!=null&&dbuser.getId()>0&&!dbuser.getId().equals(user.getId())) {
	    		return user.getWeixin()+" exist.-3";
	    	}
    	}
    	return "none";
    }
	
	@Override
	public List<User> findList(Sort sort) {
		return userRepository.findAll(sort);
	}

	@Override
	public Page<User> findList(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	@Override
	public User findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}
	@Override
	public User findByUuid(String uuid) {
		return userRepository.findByUuidIgnoreCase(uuid);
	}
	@Override
	public User findByName(String name) {
		return userRepository.findByNameIgnoreCase(name);
	}
	@Override
	public User findBySno(String sno) {
		return userRepository.findBySnoIgnoreCase(sno);
	}
	@Override
	public User findByWeixin(String weixin) {
		return userRepository.findByWeixinIgnoreCase(weixin);
	}
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmailIgnoreCase(email);
	}
	@Override
	public User findByCellPhone(String cellPhone) {
		return userRepository.findByCellPhoneIgnoreCase(cellPhone);
	}
	@Override
	public User findByPwdmodify(String pwdmodify) {
		return userRepository.findByPwdmodifyIgnoreCase(pwdmodify);
	}
	
    @Override
	public User findFirstByValueAndType(String value,String type) {
		User ret = null;
		UserFingerprint userFingerprint 
			= userFingerprintRepository.findFirstByValueAndTypeOrderByLastUpdateDateDesc(value,type);
		if (userFingerprint!=null) {
			ret = userRepository.findById(userFingerprint.getUserid()).orElse(null);
			if (ret!=null) {
				if(CommonConstants.ACTIVE.equals(userFingerprint.getRememberMe())) {
					ret.setDescription("|remembermechecked");
				}else {
					ret.setDescription("|remembermeunchecked");
				}
			}
		}
		return ret;
	}
    @Override
    public String findPwdmd5(Long userid) {
    	String ret = "";
    	UserPassword up = userPasswordRepository.findFirstByUseridOrderByIdDesc(userid);
    	if (up!=null) {
    		ret = up.getPwdmd5();
    	}
    	return ret;
    }
    @Override
    public String findMaxSno() {
    	return userRepository.findMaxSno();
    }
    @Override
    public String findSecretKey(String uuid) {
    	return userRepository.findGoogleSecretKeyByUuid(uuid);
    }
	@Override
	public List<User> findListByNameStartingWith(String name) {
		return userRepository.findByNameStartingWith(name);
	}
	@Override
	public Page<User> findListByNameStartingWith(String name,Pageable pageable) {
		return userRepository.findByNameStartingWith(name,pageable);
	}
	@Override
	public Page<User> findListByNameOrPhoneLike(String nameOrPhone,Pageable pageable) {
		return userRepository.findByNameContainingIgnoreCaseOrCellPhoneContainingIgnoreCase(nameOrPhone,nameOrPhone,pageable);
	}

	@Override
    public boolean validateUserPassword(Long userid, String salt, String saltpwdmd5) {
		String pwdmd5 = findPwdmd5(userid);
		if (!StringUtil.isEmpty(pwdmd5)) {
			if(TokenUtil.isValidatedDateSaltingToken(salt,saltpwdmd5,pwdmd5)) {
				return true;
			}
		}
		LogManager.getLogger().error("password is empty;");
		return false;
    	
    }
	
	@Override
    public boolean validateUserAuthcode(String uuid, String authcode) {
		String sk = userRepository.findGoogleSecretKeyByUuid(uuid);
		if (!StringUtil.isEmpty(sk)) {
			if (GoogleAuthenticator.validateCode(authcode, sk)) {
				return true;
			}
		}
		LogManager.getLogger().error("secret key is empty;");
		return false;
    	
    }
	@Override
	public boolean validateUserRememberme(Long userid, String fpValue, String fpType, String rememberMe) {
		UserFingerprint ufp = userFingerprintRepository.findFirstByValueAndTypeOrderByLastUpdateDateDesc(fpValue, fpType);
		if ( ufp!= null) {
			if (CommonConstants.ACTIVE.equals(rememberMe)) {
				UserFingerprint uf = userFingerprintRepository.findFirstByFpidAndUseridOrderByIdDesc(ufp.getFpid(), userid);
				if (uf!=null) {
					if (CommonConstants.ACTIVE.equals(uf.getRememberMe())) {
						return true;
					}
				}
			}
		}
		return false;
    }

}
