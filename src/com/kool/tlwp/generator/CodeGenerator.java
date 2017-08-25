/**
 * @PROJECT 
 * @DATE 2017-04-29 16:01:25
 * @AUTHOR LUYU
 */
package com.kool.tlwp.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.kool.tlwp.exception.AppException;
import com.kool.tlwp.util.DateUtils;
import com.kool.tlwp.util.PropertyUtils;
import com.kool.tlwp.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @DESCRIBE
 * @AUTHOR LUYU
 * @DATE 2017-04-29 16:01:25
 *
 */
public class CodeGenerator {

	public static void main(String[] args) throws AppException, IOException,
			TemplateException {
		String tempDir = System.getProperty("user.dir")+"\\src\\main\\resources\\template";
		String descDir = "F:\\project\\generator\\source";
		String pkg = "com.kool.tlwp";
		boolean isExt = true;
		
		Properties prop = PropertyUtils.getProp("generator.properties");
		tempDir = prop.getProperty(PropertyUtils.G_KEY_GEN_TEMPLATE_PATH);
		if (StringUtils.isEmpty(tempDir)||tempDir.equals("NULL")) {
			tempDir = StringUtils.getProjectRoot()+"\\template";
		}
		descDir = prop.getProperty(PropertyUtils.G_KEY_GEN_FILE_PATH);
		if (StringUtils.isEmpty(descDir)||descDir.equals("NULL")) {
			descDir = StringUtils.getProjectRoot();
		}
		pkg = prop.getProperty(PropertyUtils.G_KEY_PACKAGE);
		if (StringUtils.isEmpty(pkg)||pkg.equals("NULL")) {
			pkg = "com.kool";
		}
		String sIsExt = prop.getProperty(PropertyUtils.G_KEY_IS_EXTENDS);
		if (StringUtils.isEmpty(sIsExt)||sIsExt.equals("NULL")) {
			sIsExt = "TRUE";
		}
		isExt = Boolean.valueOf(sIsExt);
		String tables = prop.getProperty(PropertyUtils.G_KEY_TABLES);
		if (StringUtils.isEmpty(tables)||tables.equals("NULL")) {
			return;
		}
		String[] listTable = tables.split(";");
		for (int i = 0; i < listTable.length; i++) {
			String table = listTable[i];
			generate(table, tempDir, descDir, pkg, isExt);
		}
	}
	
	/**
	 * 
	 * @DESCRIBE 生成Dao.java
	 * @DATE 2017-04-29 16:02:05
	 * @param isExt 
	 * @throws AppException
	 * @throws IOException
	 * @throws TemplateException
	 *
	 */
	public static void generate(String tabNam, String templateDir,
			String descDir, String pkg, boolean isExt) throws AppException, IOException,
			TemplateException {
		System.out.println("开始生成 "+tabNam+" 表");
		StringUtils.debug("开始生成 "+tabNam+" 表");
		descDir = descDir+"\\"+DateUtils.getCurrentTimeString(DateUtils.FORMAT_DATE_WITHOUT_SPLIT);
		System.out.println("生成文件路径："+descDir);
		StringUtils.debug("生成文件路径："+descDir);
		// 根据表名，读取表结构
		Connection connection = getConnection();

		ColumnsDao dao = new ColumnsDao(connection);
		ColumnsValue condition = new ColumnsValue();
		condition.setTableName(tabNam);
		List<ColumnsValue> listRet = null;
		try {
			listRet = dao.select(condition);
		} catch (SQLException e) {
			StringUtils.debug("查询表结构时，数据库异常");
			throw new AppException("查询表结构时，数据库异常");
		}

		// freemarker配置
		Configuration configuration = new Configuration(
				Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		configuration.setDirectoryForTemplateLoading(new File(templateDir));
		configuration.setDefaultEncoding("UTF-8");
		configuration.setObjectWrapper(new DefaultObjectWrapper(
				Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS));

		// 获取或创建一个模版。
		Template template = configuration.getTemplate("BaseDao.ftl");
		// 生成文件到目标文件夹下
		HashMap<String, Object> map = getBaseDaoMap(tabNam, pkg, listRet);
		Writer writer = getBaseDaoWriter(tabNam, getDaoDir(descDir,pkg));
		template.process(map, writer);

		template = configuration.getTemplate("Entity.ftl");
		map = getEntityMap(tabNam, pkg, listRet);
		writer = getEntityWriter(tabNam, getEntityDir(descDir,pkg));
		template.process(map, writer);

		template = configuration.getTemplate("BaseXml.ftl");
		map = getBaseXmlMap(tabNam, pkg, listRet);
		writer = getBaseXmlWriter(tabNam, getDaoDir(descDir,pkg));
		template.process(map, writer);

		if (isExt) {
			System.out.println("生成扩展文件...");
			StringUtils.debug("生成扩展文件...");
			template = configuration.getTemplate("Dao.ftl");
			map = getDaoMap(tabNam, pkg, listRet);
			writer = getDaoWriter(tabNam, getDaoDir(descDir,pkg));
			template.process(map, writer);
			
			template = configuration.getTemplate("Xml.ftl");
			map = getXmlMap(tabNam, pkg, listRet);
			writer = getXmlWriter(tabNam, getDaoDir(descDir,pkg));
			template.process(map, writer);
			
		}
		System.out.println("完成");
		StringUtils.debug("完成");
	}

	/**
	 * @DESCRIBE 
	 * @DATE 2017年8月25日 下午11:12:23
	 *
	 * @param descDir
	 * @param pkg
	 * @return
	 */
	private static String getEntityDir(String descDir, String pkg) {
		String dir = descDir+"\\"+pkg.replace('.', '\\')+"\\entity";
		File fDir = new File(dir);
		if (fDir.exists() == false) {
			fDir.mkdirs();
		}
		return dir;
	}

	/**
	 * @DESCRIBE 
	 * @DATE 2017年8月25日 下午11:08:30
	 *
	 * @param descDir
	 * @param pkg
	 * @return
	 */
	private static String getDaoDir(String descDir, String pkg) {
		String dir = descDir+"\\"+pkg.replace('.', '\\')+"\\dao";
		File fDir = new File(dir);
		if (fDir.exists() == false) {
			fDir.mkdirs();
		}
		return dir;
	}

	/**
	 * @DESCRIBE 
	 * @DATE 2017-05-01 14:31:04
	 *
	 * @param tabNam
	 * @param descDir
	 * @return
	 * @throws IOException 
	 */
	private static Writer getXmlWriter(String tabNam, String descDir) throws IOException {
		String fileName = StringUtils.transferToHumpName(tabNam, true);
		fileName = fileName + "Dao.xml";
		String descPath = descDir + "\\" + fileName;
		File fDesc = new File(descPath);
		Writer writer = new FileWriter(fDesc);
		return writer;
	}

	/**
	 * @DESCRIBE 
	 * @DATE 2017-05-01 14:31:02
	 *
	 * @param tabNam
	 * @param pkg
	 * @param listRet
	 * @return
	 */
	private static HashMap<String, Object> getXmlMap(String tabNam, String pkg,
			List<ColumnsValue> listRet) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pkg", pkg);
		map.put("upTumTabNam", StringUtils.transferToHumpName(tabNam, true));
		map.put("lowTumTabNam", StringUtils.transferToHumpName(tabNam, false));
		map.put("tabNam", tabNam);

		ArrayList<ColumnVo> list = new ArrayList<ColumnVo>();
		ArrayList<ColumnVo> pkList = new ArrayList<ColumnVo>();

		boolean hasPk = false;
		for (ColumnsValue value : listRet) {
			value.setDataType(value.getDataType().toUpperCase());
			ColumnVo col = new ColumnVo(value);
			col.setJavaType(ColumnsValue.transferType(value.getDataType()));
			col.setUpJavaName(StringUtils.transferToHumpName(
					value.getColumnName(), true));
			col.setLowJavaName(StringUtils.transferToHumpName(
					value.getColumnName(), false));
			list.add(col);

			if ("PRI".equalsIgnoreCase(value.getColumnKey())) {
				hasPk = true;
				pkList.add(col);
			}
		}
		map.put("columns", list);
		map.put("hasPk", hasPk);
		map.put("pkList", pkList);
		return map;
	}

	/**
	 * @DESCRIBE 
	 * @DATE 2017-05-01 14:26:02
	 *
	 * @param tabNam
	 * @param descDir
	 * @return
	 * @throws IOException 
	 */
	private static Writer getDaoWriter(String tabNam, String descDir) throws IOException {
		String fileName = StringUtils.transferToHumpName(tabNam, true);
		fileName = fileName + "Dao.java";
		String descPath = descDir + "\\" + fileName;
		File fDesc = new File(descPath);
		Writer writer = new FileWriter(fDesc);
		return writer;
	}

	/**
	 * @DESCRIBE 
	 * @DATE 2017-05-01 14:26:00
	 *
	 * @param tabNam
	 * @param pkg
	 * @param listRet
	 * @return
	 */
	private static HashMap<String, Object> getDaoMap(String tabNam, String pkg,
			List<ColumnsValue> listRet) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pkg", pkg);
		map.put("upTumTabNam", StringUtils.transferToHumpName(tabNam, true));
		return map;
	}

	/**
	 * @DESCRIBE
	 * @DATE 2017-05-01 08:51:39
	 *
	 * @param tabNam
	 * @param descDir
	 * @return
	 * @throws IOException
	 */
	private static Writer getBaseXmlWriter(String tabNam, String descDir)
			throws IOException {
		String fileName = StringUtils.transferToHumpName(tabNam, true);
		fileName = "Base" + fileName + "Dao.xml";
		String descPath = descDir + "\\" + fileName;
		File fDesc = new File(descPath);
		Writer writer = new FileWriter(fDesc);
		return writer;
	}

	/**
	 * @DESCRIBE
	 * @DATE 2017-05-01 08:51:36
	 *
	 * @param tabNam
	 * @param pkg
	 * @param listRet
	 * @return
	 */
	private static HashMap<String, Object> getBaseXmlMap(String tabNam,
			String pkg, List<ColumnsValue> listRet) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pkg", pkg);
		map.put("upTumTabNam", StringUtils.transferToHumpName(tabNam, true));
		map.put("lowTumTabNam", StringUtils.transferToHumpName(tabNam, false));
		map.put("tabNam", tabNam);

		ArrayList<ColumnVo> list = new ArrayList<ColumnVo>();
		ArrayList<ColumnVo> pkList = new ArrayList<ColumnVo>();

		boolean hasPk = false;
		for (ColumnsValue value : listRet) {
			value.setDataType(value.getDataType().toUpperCase());
			ColumnVo col = new ColumnVo(value);
			col.setJavaType(ColumnsValue.transferType(value.getDataType()));
			col.setUpJavaName(StringUtils.transferToHumpName(
					value.getColumnName(), true));
			col.setLowJavaName(StringUtils.transferToHumpName(
					value.getColumnName(), false));
			list.add(col);

			if ("PRI".equalsIgnoreCase(value.getColumnKey())) {
				hasPk = true;
				pkList.add(col);
			}
		}
		map.put("columns", list);
		map.put("hasPk", hasPk);
		map.put("pkList", pkList);
		return map;
	}

	/**
	 * @DESCRIBE
	 * @DATE 2017-04-29 17:46:01
	 *
	 * @param tabNam
	 * @param descDir
	 * @return
	 * @throws IOException
	 */
	private static Writer getEntityWriter(String tabNam, String descDir)
			throws IOException {
		String fileName = StringUtils.transferToHumpName(tabNam, true);
		fileName = fileName + "Entity.java";
		String descPath = descDir + "\\" + fileName;
		File fDesc = new File(descPath);
		Writer writer = new FileWriter(fDesc);
		return writer;
	}

	/**
	 * @DESCRIBE
	 * @DATE 2017-04-29 17:45:05
	 *
	 * @param tabNam
	 * @param pkg
	 * @param listRet
	 * @return
	 */
	private static HashMap<String, Object> getEntityMap(String tabNam,
			String pkg, List<ColumnsValue> listRet) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pkg", pkg);
		map.put("upTumTabNam", StringUtils.transferToHumpName(tabNam, true));
		map.put("lowTumTabNam", StringUtils.transferToHumpName(tabNam, false));
		ArrayList<ColumnVo> list = new ArrayList<ColumnVo>();
		for (ColumnsValue value : listRet) {
			ColumnVo col = new ColumnVo(value);
			col.setJavaType(ColumnsValue.transferType(value.getDataType()));
			col.setUpJavaName(StringUtils.transferToHumpName(
					value.getColumnName(), true));
			col.setLowJavaName(StringUtils.transferToHumpName(
					value.getColumnName(), false));
			list.add(col);
		}
		map.put("columns", list);
		return map;
	}

	/**
	 * @DESCRIBE
	 * @DATE 2017-04-29 17:44:05
	 *
	 * @param tabNam
	 * @param descDir
	 * @return
	 * @throws IOException
	 */
	private static Writer getBaseDaoWriter(String tabNam, String descDir)
			throws IOException {
		String fileName = StringUtils.transferToHumpName(tabNam, true);
		fileName = "Base" + fileName + "Dao.java";
		String descPath = descDir + "\\" + fileName;
		File fDesc = new File(descPath);
		Writer writer = new FileWriter(fDesc);
		return writer;
	}

	/**
	 * @DESCRIBE
	 * @DATE 2017-04-29 17:43:23
	 *
	 * @param tabNam
	 * @param pkg
	 * @return
	 */
	private static HashMap<String, Object> getBaseDaoMap(String tabNam,
			String pkg, List<ColumnsValue> listRet) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("pkg", pkg);
		map.put("upTumTabNam", StringUtils.transferToHumpName(tabNam, true));
		map.put("lowTumTabNam", StringUtils.transferToHumpName(tabNam, false));

		boolean hasPk = false;
		for (ColumnsValue value : listRet) {
			if ("PRI".equalsIgnoreCase(value.getColumnKey())) {
				hasPk = true;
				break;
			}
		}
		map.put("hasPk", hasPk);
		return map;
	}

	/**
	 * @DESCRIBE
	 * @DATE 2017-04-29 16:20:52
	 *
	 * @return
	 * @throws AppException
	 */
	private static Connection getConnection() throws AppException {
		Connection connection = null;
		try {
			String sUrl = "jdbc:mysql://localhost:3306/tlwp";
			String sNam = "test";
			String sPwd = "test";
			String sDriver = "com.mysql.jdbc.Driver";

			Properties prop = PropertyUtils.getProp("generator.properties");
			sUrl = prop.getProperty(PropertyUtils.G_KEY_URL);
			sDriver = prop.getProperty(PropertyUtils.G_KEY_DRIVER);
			sNam = prop.getProperty(PropertyUtils.G_KEY_USER);
			sPwd = prop.getProperty(PropertyUtils.G_KEY_PWD);

			Class.forName(sDriver);

			connection = DriverManager.getConnection(sUrl, sNam, sPwd);
		} catch (ClassNotFoundException e) {
			StringUtils.debug("Create connection failed :"
					+ e.getMessage());
			throw new AppException("Create connection failed :"
					+ e.getMessage());
		} catch (SQLException e) {
			StringUtils.debug("Create connection failed :"
					+ e.getMessage());
			throw new AppException("Create connection failed :"
					+ e.getMessage());
		} catch (IOException e) {
			StringUtils.debug("Create connection failed :"
					+ e.getMessage());
			throw new AppException("Create connection failed :"
					+ e.getMessage());
		}
		return connection;
	}
}
