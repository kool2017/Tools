/**
 * @PROJECT 
 * @DATE 2017-03-22 19:43:43
 * @AUTHOR LUYU
 */
package com.kool.tlwp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.kool.tlwp.exception.AppException;


/**
 * @DESCRIBE 
 * @AUTHOR LUYU
 * @DATE 2017-03-22 19:43:43
 *
 */
public class PropertyUtils {

	public static final Logger LOGGER = Logger.getLogger(PropertyUtils.class);
	
	/**
	 * tlwp.properties
	 */
	public static final String T_KEY_DEV_MODE="DEV_MODE";
	public static final String T_KEY_DEV_LOG="DEV_LOG";
	public static final String T_KEY_CREATE_TABLE_SQL_TEMPLATE="CREATE_TABLE_SQL_TEMPLATE";
	public static final String T_KEY_EXCEL_PATH="EXCEL_PATH";
	public static final String T_KEY_TO_DIR="TO_DIR";
	
	/**
	 * generator.properties
	 */
	public static final String G_KEY_URL="URL";
	public static final String G_KEY_DRIVER="DRIVER";
	public static final String G_KEY_USER="USER";
	public static final String G_KEY_PWD="PWD";
	public static final String G_KEY_GEN_FILE_PATH="GEN_FILE_PATH";
	public static final String G_KEY_GEN_TEMPLATE_PATH="GEN_TEMPLATE_PATH";
	public static final String G_KEY_PACKAGE="PACKAGE";
	public static final String G_KEY_IS_EXTENDS="IS_EXTENDS";
	public static final String G_KEY_TABLES="TABLES";

	public static Properties getSysPropFromRoot() throws IOException{
		FileInputStream fis = new FileInputStream(new File(StringUtils.getProjectRoot()+"\\tlwp.properties"));
		Properties prop = new Properties();
		prop.load(new InputStreamReader(fis, "UTF-8"));
		return prop;
	}

	public static Properties getSysPropFromSrc() throws IOException{
		InputStream input = PropertyUtils.class.getResourceAsStream("/tlwp.properties");
		Properties prop = new Properties();
		prop.load(new InputStreamReader(input, "UTF-8"));
		return prop;
	}
	
	public static String getDevMode() throws IOException{
		return getSysPropFromRoot().getProperty(T_KEY_DEV_MODE);
	}
	
	public static String getDevLog() throws IOException{
		return getSysPropFromRoot().getProperty(T_KEY_DEV_LOG);
	}
	
	public static String getSysValue(String key) throws IOException{
		return getSysPropFromRoot().getProperty(key);
	}
	
	public static String getValue(String propName, String key) throws AppException, IOException {
		return getProp(propName).getProperty(key);
	}
	
	public static Properties getProp(String propName) throws AppException, IOException{
		if (propName == null || propName.trim().length()<=0) {
			throw new AppException("文件名不能为空");
		}
		if (propName.charAt(0)!= '/') {
			propName = "/"+propName;
		}
		InputStream input = new FileInputStream(new File(StringUtils.getProjectRoot()+propName));
		Properties prop = new Properties();
		prop.load(input);
		return prop;
		
	}
	
	public static void main(String[] args) throws AppException, IOException {
		Properties prop = getProp("generator.properties");
		System.out.println(prop.get("TABLES"));
	}
	
}
