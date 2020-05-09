package com.labor.common.dao.jdbc;

import com.labor.common.constants.ConfigConstants;
import com.labor.common.util.FileUtil;
import com.labor.common.util.StringUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class JdbcDataSource {
	
	private static final Logger log = LogManager.getLogger(JdbcDataSource.class);
	private static Map dbConfig = new HashMap();
	private static final String DM = "Drivermanager";
	private static final String DS = "Datasource";
	private static final String connName = "default";
	
	static {
		log.info("*****************JdbcDataSource initialize.****************************");
		initialize();
		
	}
	
	//get defualt connection name
	public static Connection createConnection() throws Exception{
		return createConnection(connName);
	}
	/**
	 * get a connection via the conn name form the dbconfig.
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	private static Connection createConnection(String name) throws Exception{
		Connection conn = null;
		if (!dbConfig.containsKey(name)) {
			throw new Exception("connectDB error:" + name+" not found in dbconfig");
		}
		Properties pro = (Properties)dbConfig.get(name);
		String type = (String) pro.get("type");
		if (DS.equalsIgnoreCase(type)){
			conn = createDSConnection(pro);
		}else if (DM.equalsIgnoreCase(type)) {
			conn = createDMConnection(pro);
		} 
		
		return conn;
	}

	/**
	 * get a connection via datasource jndi connection
	 */
	private static Connection createDSConnection(Properties pro)
			throws SQLException,NamingException {
		DataSource dataSource = null;
		String ds = (String)pro.getProperty("datasource");
		Context ctx = new InitialContext();
		dataSource = (DataSource) ctx.lookup(ds);
		return dataSource.getConnection();
	}
	
	/**
	 * get a connection via drivermanager jdbc connection
	 */
	private static Connection createDMConnection(Properties pro)
			throws SQLException, ClassNotFoundException,IllegalAccessException,InstantiationException{
		String dbURL = (String) pro.get("url");
		String user = (String) pro.get("user");
		String password = (String) pro.get("password");
		String driver = (String) pro.get("driver");
		Class.forName(driver).newInstance();
		return DriverManager.getConnection(dbURL, user, password);
	}

	/**
	 * init the hashmap-dbConfig from the dbconfig.xml
	 * dbConfig contains key(conn name) - value(properties).
	 * ex.
	 * <conn name="conn_tfdiskdb">
			<!--java:comp/env/jdbc/TFDISKDB-->
			<type value="datasource" />
			<datasource value="java:comp/env/jdbc/TFDISKDB" />
			
			<!--jdbc:db2://localhost:3306/TFDISKDB-->
			<!-- type value="drivermanager" />
			<host value="sztddsmaj.mfg.cn.hgst.com" />
			<port value="50002" />
			<user value="diskweb" />
			<password value="one1one" />
			<db value="TFDISKDB" />
			<driver value="com.ibm.db2.jcc.DB2Driver" /-->
		</conn>
	 */
	private static void initialize() {
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			InputStream is = FileUtil.loadFile(ConfigConstants.JDBC_DBCONFIG_FILE,
					JdbcDataSource.class);

			Element root = builder.parse(is).getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("conn");

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Element node = (Element) nodeList.item(i);
				String name = node.getAttribute("name");
				String type = getAttr(node, "type");
				if (name == null) {
					log.error("dbconfig.xml connName is null!");
					continue;
				}
								
				if (DM.equalsIgnoreCase(type)) {
					
					String host = StringUtil.defaultString(getAttr(node, "host"),"localhost");
					int port = StringUtil.defaultInt(getAttr(node, "port"), 3306);
					String db = StringUtil.defaultString(getAttr(node, "db"), "test");
					String user = StringUtil.defaultString(getAttr(node, "user"),"root");
					String password = StringUtil.defaultString(getAttr(node, "password"), "");
					//String driver = StringUtil.defaultString(getAttr(node,"driver"), "COM.ibm.db2.jdbc.net.DB2Driver");
					String driver = StringUtil.defaultString(getAttr(node,"driver"), "com.ibm.db2.jcc.DB2Driver");
					String dburl="";
					if (driver.indexOf("db2")!=-1){
						dburl = "jdbc:db2://" + host + ":" + port + "/" + db;
					} else if (driver.indexOf("mysql")!=-1) {
						dburl = "jdbc:mysql://" + host + ":" + port + "/" + db;
					} else if (driver.indexOf("oracle")!=-1){
						dburl = "jdbc:oracle://" + host + ":" + port + "/" + db;
					} else {
						dburl = "jdbc:unknow://" + host + ":" + port + "/" + db;
					}
					
					dburl = dburl.trim();
					Properties pro = new Properties();
					pro.put("type", DM);
					pro.put("url", dburl);
					pro.put("driver", driver);
					pro.put("user", user);
					pro.put("password", password);
					dbConfig.put(name, pro);
					log.info("init "+name+":"+dburl);
					
				} else if(DS.equalsIgnoreCase(type)){

					String datasource = StringUtil.defaultString(getAttr(node,"datasource"), "");
					Properties pro = new Properties();
					pro.put("type", DS);
					pro.put("datasource", datasource);
					dbConfig.put(name, pro);	
					log.info("init "+name+":"+datasource);
				}
				
			}
		} catch (Exception e) {
			log.error("InitDB error:", e);
		}
	}

	//xml operation
	private static String getAttr(Element node, String attrName) {
		NodeList paraList = node.getElementsByTagName(attrName);
		if (paraList.getLength() <= 0)
			return null;
		return ((Element) paraList.item(0)).getAttribute("value");
	}
}
