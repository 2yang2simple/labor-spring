package com.labor.spring.core.api.dictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.labor.common.constants.CommonConstants;
import com.labor.common.util.StringUtil;


@Service
public class DictionaryServiceImpl implements DictionaryServiceIntf{
	
	@Autowired
	private DictionaryRepository dictionaryRepository;
	
	@Override
	@Transactional
	public String createTopDictionarys() {
		LogManager.getLogger().info("************createDefaultTops***************");
		int count = 0;
		HashMap<String,String> set = DictionaryConstants.TOP_DICTIONARY;
		Iterator it = set.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String code = (String)entry.getKey();
			String name = (String)entry.getValue();
			Dictionary dc = new Dictionary();
			dc.setCode(code);
			Example<Dictionary> example =Example.of(dc);
			Optional<Dictionary> tmp = dictionaryRepository.findOne(example);
			if (tmp.isPresent()) {
				dc.setId(tmp.get().getId());
			}
			dc.setName(name);
			dc.setStatus(CommonConstants.ACTIVE);
			dictionaryRepository.save(dc);
			count = count+1;

		}
		return String.valueOf(count);
	}
	@Override
	@Transactional
	public Dictionary create(Dictionary dictionary) {
		Dictionary ret = null;
		if (dictionary!=null&&dictionary.getParentid()!=null&&dictionary.getParentid()>0) {
			Dictionary tmp = new Dictionary();
			Example<Dictionary> example =Example.of(tmp);
			long count = dictionaryRepository.count(example);
			String code = "D"+StringUtil.prefixZero(Long.toString(count+1), 5);
			dictionary.setStatus(CommonConstants.ACTIVE);
			dictionary.setCode(code);
			ret = dictionaryRepository.save(dictionary);
		}
		return ret;
	}
	@Override
	@Transactional
	public Dictionary update(Dictionary dictionary) {
		Dictionary ret = null;
		if (dictionary!=null&dictionary.getId()!=null&&dictionary.getId()>0) {
			//check if update the code;
			if (StringUtil.isEmpty(dictionary.getCode())) {
				LogManager.getLogger().error("update null code");
			} else {
				Dictionary tmp = new Dictionary();
				tmp.setCode(dictionary.getCode());
				tmp.setStatus(CommonConstants.ACTIVE);
				Example<Dictionary> example =Example.of(tmp);
				tmp = dictionaryRepository.findOne(example).orElse(dictionary);
				if (dictionary.getId().equals(tmp.getId())) {
					LogManager.getLogger().debug("update");
					return dictionaryRepository.save(dictionary);
				} else {
					LogManager.getLogger().error("update false");
				}
						
			}

		}
		return ret;
	}
	
	@Override
	public List<Dictionary> findListByExample(Dictionary dictionary){
		Example<Dictionary> example =Example.of(dictionary);
		return dictionaryRepository.findAll(example);
	}
	
	@Override
	public Page<Dictionary> findListByExample(Dictionary dictionary, Pageable pageable){
		Example<Dictionary> example =Example.of(dictionary);
		return dictionaryRepository.findAll(example, pageable); 
	}

	@Override
	public Long countByExample(Dictionary dictionary){
		Example<Dictionary> example =Example.of(dictionary);
		return dictionaryRepository.count(example);
	}

	@Override
	public Optional<Dictionary> findByExample(Dictionary dictionary){
		Example<Dictionary> example =Example.of(dictionary);
		return dictionaryRepository.findOne(example);
	}
	
	@Override
	public List<Dictionary> findSubList(String code, String status){
		return dictionaryRepository.findByParentCodeAndStatus(code, status);
	}
	@Override
	public List<Dictionary> findSubList(){
		return dictionaryRepository.findByParentidIsNotNullOrderByParentidDesc();
	}
	@Override
	public List<Dictionary> findTopList(){
		return dictionaryRepository.findByParentidIsNullOrderByIdDesc();
	}

	@Override
	public Optional<Dictionary> findById(Integer id) {
		return dictionaryRepository.findOneById(id);
	}

}
