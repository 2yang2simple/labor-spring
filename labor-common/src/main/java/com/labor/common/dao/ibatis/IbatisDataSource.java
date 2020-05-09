package com.labor.common.dao.ibatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
import com.labor.common.constants.ConfigConstants;

public class IbatisDataSource {
	
	private static SqlMapClient sqlMapper;
	private static Logger log = LogManager.getLogger(IbatisDataSource.class);
	static {
		try {
			log.info("*****************IbatisDataSource initialize.****************************");
			Reader reader = Resources.getResourceAsReader(ConfigConstants.IBATIS_SQLMAPCONFIG_FILE);
			sqlMapper = SqlMapClientBuilder.buildSqlMapClient(reader);
			reader.close(); 
		} catch (Exception e) {
			// Fail fast.
			log.error(e.getMessage());
			throw new RuntimeException("Something bad happened while building the SqlMapClient instance." + e, e);
		}
	}
	public static SqlMapClient getSqlMapper() {
		return sqlMapper;
	}
}

