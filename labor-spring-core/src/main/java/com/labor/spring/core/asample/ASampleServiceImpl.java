package com.labor.spring.core.asample;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.labor.common.constants.CommonConstants;
import com.labor.common.exception.ServiceException;
import com.labor.common.util.StringUtil;
import com.labor.common.util.TokenUtil;
import com.labor.spring.util.IgnorePropertiesUtil;

/***
1.不要在接口上声明@Transactional ，而要在具体类的方法上使用 @Transactional 注解，否则注解可能无效。
2.不要图省事，将@Transactional放置在类级的声明中，放在类声明，会使得所有方法都有事务。故@Transactional应该放在方法级别，不需要使用事务的方法，就不要放置事务，比如查询方法。否则对性能是有影响的。
3.使用了@Transactional的方法，对同一个类里面的方法调用， @Transactional无效。比如有一个类Test，它的一个方法A，A再调用Test本类的方法B（不管B是否public还是private），但A没有声明注解事务，而B有。则外部调用A之后，B的事务是不会起作用的。（经常在这里出错）
4.使用了@Transactional的方法，只能是public，@Transactional注解的方法都是被外部其他类调用才有效，故只能是public。道理和上面的有关联。故在 protected、private 或者 package-visible 的方法上使用 @Transactional 注解，它也不会报错，但事务无效。
5.经过在ICORE-CLAIM中测试，效果如下：
A.抛出受查异常XXXException，事务会回滚。
B.抛出运行时异常NullPointerException，事务会回滚。
C.Quartz中，execute直接调用加了@Transactional方法，可以回滚；间接调用，不会回滚。（即上文3点提到的）
D.异步任务中，execute直接调用加了@Transactional方法，可以回滚；间接调用，不会回滚。（即上文3点提到的）
E.在action中加上@Transactional，不会回滚。切记不要在action中加上事务。
F.在service中加上@Transactional，如果是action直接调该方法，会回滚，如果是间接调，不会回滚。（即上文3提到的）
G.在service中的private加上@Transactional，事务不会回滚。
------------------------------------

findXXX: connect to db;
getXXX:  no connect to db

findBy : return object
findListBy : return list

 *
 */

@Service
public class ASampleServiceImpl implements ASampleServiceIntf {

	@Autowired
	private ASampleRepository asampleRepository;

	/**
	 * !!!!!
	 * this method need to validate the parameters which to be unique
	 * checking the data already exist when create new;
	 */
	@Override
	@Transactional
	public ASample create(ASample entity) {
		ASample ret = null;
		if (entity!=null) {
			ASample tmp = new ASample();
			/***** [name for ex.] need to unique; START */
			tmp.setName(entity.getName());
			tmp.setStatus(CommonConstants.ACTIVE);
			Example<ASample> example =Example.of(tmp);
			long count = asampleRepository.count(example);
			if (count>0) {
				throw new ServiceException("the Name exists."); 
			}
			/***** [name for ex.] need to unique; END */
			entity.setId(null);
			entity.setStatus(CommonConstants.ACTIVE);
			entity.setUuid(TokenUtil.generateUUID());
			return asampleRepository.save(entity);
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
	public ASample update(ASample entity) {
		ASample ret = null;
		if (entity!=null&entity.getId()!=null&&entity.getId()>0) {
			ASample tmp = new ASample();
			/***** [name for ex.] need to unique; START */
			tmp.setName(entity.getName());
			tmp.setStatus(CommonConstants.ACTIVE);
			Example<ASample> example =Example.of(tmp);
			List<ASample> list = asampleRepository.findAll(example);
			if (list==null||list.size()==0) {
				tmp = null;
			} else if (list.size()==1) {
				tmp = list.get(0);
			} else {
				throw new ServiceException("the Name exists."); 
			}
			if (tmp!=null&&!entity.getId().equals(tmp.getId())) {
				throw new ServiceException("the Name exists."); 
			}
			/***** [name for ex.] need to unique; END */
			
			ret = asampleRepository.save(entity);
		}
		return ret;
	}

	@Override
	@Transactional
	public ASample update(Integer id, ASample entity, boolean ignoreNullProperties) {
		ASample newEntity = entity;
		Optional<ASample> optional = asampleRepository.findOneById(id);
		if (optional.isPresent()){
			if (ignoreNullProperties) {
				newEntity = optional.get();
				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(entity));
			}
			return asampleRepository.save(newEntity);
		}
		return null;
	}	

	@Override
	@Transactional
	public ASample save(ASample entity, boolean ignoreNullProperties) {
		ASample newEntity = entity;
		Optional<ASample> optional = asampleRepository.findOneById(entity.getId());
		if (optional.isPresent()){
			if (ignoreNullProperties) {
				newEntity = optional.get();
				BeanUtils.copyProperties(entity,newEntity,IgnorePropertiesUtil.getNullPropertyNames(entity));
			} 
		} else {
			newEntity.setStatus(CommonConstants.ACTIVE);
		}
		return asampleRepository.save(newEntity);
	}
	
	public static void main(String[] args) {
		ASample fo1 = new ASample();
		ASample fo2 = new ASample();
		fo1.setDescription(null);
		fo1.setName("hehe");
		fo2.setDescription("description");
		fo2.setName("name");
		BeanUtils.copyProperties(fo1,fo2,IgnorePropertiesUtil.getNullPropertyNames(fo1));
		System.err.println(fo2.getDescription()+"|"+fo2.getName());
		BeanUtils.copyProperties(fo1,fo2);
		System.err.println(fo2.getDescription()+"|"+fo2.getName());
	}
	
	
	@Override
	public Long countByExample(ASample entity){
		Example<ASample> example =Example.of(entity);
		return asampleRepository.count(example);
	}
	
	@Override
	public Optional<ASample> findById(Integer id) {
		return asampleRepository.findOneById(id);
	}

	@Override
	public Optional<ASample> findByExample(ASample entity){
		Example<ASample> example =Example.of(entity);
		return asampleRepository.findOne(example);
	}
	
	@Override
	public Optional<ASample> findByNameAndStatus(String name, String status){
		return asampleRepository.findOneByNameAndStatusIgnoreCase(name, status);
	}
	
	@Override
	public List<ASample> findList(Sort sort) {
		return asampleRepository.findAll(sort);
	}
	
	@Override
	public List<ASample> findListByExample(ASample entity){
		Example<ASample> example =Example.of(entity);
		return asampleRepository.findAll(example);
	}
	
	@Override
	public Page<ASample> findList(Pageable pageable) {
		return asampleRepository.findAll(pageable);
	}
	
	@Override
	public Page<ASample> findListByExample(ASample entity, Pageable pageable){
		Example<ASample> example =Example.of(entity);
		return asampleRepository.findAll(example, pageable); 
	}
	@Override
	public Page<ASample> findListByNameStartingWith(String name,Pageable pageable) {
		if (StringUtil.isEmpty(name)) {
			return asampleRepository.findAll(pageable);
		}
		return asampleRepository.findByNameStartingWith(name,pageable);
	}

}
