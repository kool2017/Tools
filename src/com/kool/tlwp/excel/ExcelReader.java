/**
 * @PROJECT 
 * @DATE 2017-08-23 20:47:34
 * @AUTHOR LUYU
 */
package com.kool.tlwp.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.kool.tlwp.util.DateUtils;
import com.kool.tlwp.util.PropertyUtils;
import com.kool.tlwp.util.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @DESCRIBE excel读取工具，提供2个功能：1.读取excel内容；2.读取excel数据库设计文档并转成建表语句
 * @AUTHOR LUYU
 * @DATE 2017-08-23 20:47:34
 *
 */
public class ExcelReader {

	public static void main(String[] args) {
		BasicConfigurator.configure();
		String excel = "F:\\project\\项目文档\\BaseFrame1.0\\设计文档\\总体设计\\数据库设计.xlsx";
		String toDir = "F:\\project\\项目文档\\BaseFrame1.0\\设计文档\\总体设计";
		try {
			excel = PropertyUtils.getSysValue(PropertyUtils.T_KEY_EXCEL_PATH);
			toDir = PropertyUtils.getSysValue(PropertyUtils.T_KEY_TO_DIR);
			if (StringUtils.isEmpty(excel)||excel.equals("NULL")) {
				excel = StringUtils.getProjectRoot()+"\\excel.xlsx";
			}
			if (StringUtils.isEmpty(toDir)||toDir.equals("NULL")) {
				toDir = StringUtils.getProjectRoot();
			}
			excelToSql(excel, toDir);
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append(e.getMessage());
			sb.append("\r\n");
			StackTraceElement[] es = e.getStackTrace();
			for (int i = 0; i < es.length; i++) {
				StackTraceElement element = es[i];
				sb.append(element.toString());
				sb.append("\r\n");
			}
			StringUtils.debug(sb.toString());
		}
	}
	
	/**
	 * 
	 * @DESCRIBE 读取excel内容
	 * @DATE 2017年8月25日 下午9:04:30
	 *
	 * @param excelPath 文件路径
	 * @param sheetBegin 需要读取的sheet起始序号，0开始
	 * @param sheetEnd 需要读取的sheet末尾序号
	 * @param rowBegin 需要读取的开始行序号，0开始
	 * @param rowEnd 需要读取的末尾行序号
	 * @param cellBegin 需要读取的单元格开始序号，0开始
	 * @param cellEnd 需要读取的单元格末尾序号
	 * @return
	 * @throws IOException
	 */
	public static List<List<List<String>>> getExcelValue(String excelPath,
			int sheetBegin,int sheetEnd, int rowBegin, int rowEnd, int cellBegin, int cellEnd) throws IOException{
	
		Workbook book = getBook(excelPath);
		List<List<List<String>>> listSheet = new ArrayList<List<List<String>>>();
		for (int i = 0; i < book.getNumberOfSheets(); i++) {
			List<List<String>> sheet = getSheetValue(book, i, rowBegin, rowEnd, cellBegin, cellEnd);
			listSheet.add(sheet);
		}
		book.close();
		return listSheet;
	}

	/**
	 * 
	 * @DESCRIBE 读取excel数据库设计文档并转成建表语句
	 * @DATE 2017年8月25日 下午9:01:42
	 *
	 * @param excelPath excel数据库设计文档路径
	 * @param toDir 生成sql文件的路径
	 * @throws Exception
	 */
	public static void excelToSql(String excelPath, String toDir) throws Exception {
		List<List<List<String>>> listSheet = getExcelValue(excelPath, 0, 65535, 0, 65535, 0, 65535);
		print(listSheet);
		for (List<List<String>> sheet : listSheet) {
			TableBeanToSql(sheetValueToTableBean(sheet), toDir);
		}
	}

	/**
	 * @DESCRIBE 打印读取的excel内容到debug日志
	 * @DATE 2017-08-23 22:34:32
	 *
	 * @param listSheet
	 */
	private static void print(List<List<List<String>>> listSheet) {
		StringBuilder sb = new StringBuilder();
		for (List<List<String>> sheet : listSheet) {
			for (List<String> row : sheet) {
				for (String cell : row) {
					sb.append(cell);
					sb.append("\t");
				}
				sb.append("\r\n");
			}
			sb.append("--------------------------------------\r\n");
		}
		StringUtils.debug(sb.toString());
	}
	
	/**
	 * 
	 * @DESCRIBE 表结构数据转sql文件，并保存到目标文件夹
	 * @DATE 2017年8月25日 下午9:10:23
	 *
	 * @param tableBean
	 * @param toDir
	 * @throws IOException
	 * @throws Exception
	 */
	private static void TableBeanToSql(TableBean tableBean, String toDir) throws IOException, Exception {
		String templatePath = PropertyUtils.getSysValue(PropertyUtils.T_KEY_CREATE_TABLE_SQL_TEMPLATE);
		if (StringUtils.isEmpty(templatePath)||templatePath.equals("NULL")) {
			templatePath = StringUtils.getProjectRoot()+"\\template";
		}
		toDir = toDir+"\\"+DateUtils.getCurrentTimeString(DateUtils.FORMAT_DATE_WITHOUT_SPLIT);
		File fDir = new File(toDir);
		if (fDir.exists() == false) {
			fDir.mkdirs();
		}
		String filePath = toDir+"\\"+tableBean.getTableId()+".sql";
		Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setDirectoryForTemplateLoading(new File(templatePath));
		Template t = cfg.getTemplate("table.ftl");
		FileWriter fw = new FileWriter(new File(filePath),false);
		t.process(tableBean, fw);
		
	}

	/**
	 * 
	 * @DESCRIBE excel一页内容转成表结构数据
	 * @DATE 2017年8月25日 下午9:11:17
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 */
	private static TableBean sheetValueToTableBean(List<List<String>> list) throws Exception {
		TableBean tableBean = new TableBean();
		String createTime = DateUtils.getCurrentTimeString(DateUtils.FORMAT_TIMESTAMP);
		String tableId = list.get(0).get(1);
		String tableName = list.get(0).get(2);
		tableBean.setCreateTime(createTime);
		tableBean.setTableId(tableId);
		tableBean.setTableName(tableName);

		ArrayList<ColumnBean> columns = new ArrayList<ColumnBean>();
		ArrayList<String> pks = new ArrayList<String>();
		for (int i = 2; i < list.size(); i++) {
			List<String> listCell = list.get(i);
			if (listCell.get(0) == null || listCell.get(0).equals("")) {
				continue;
			}
			String colId = listCell.get(0);
			String colHumpName = listCell.get(1);
			String colName = listCell.get(2);
			String colType = listCell.get(3);
			String colIsNullable = listCell.get(4);
			String colIsKey = listCell.get(5);
			String colComment = listCell.get(6);

			ColumnBean column = new ColumnBean();
			column.setId(colId);
			column.setHumpName(colHumpName);
			column.setName(colName);
			column.setType(colType);
			column.setIsNullable(colIsNullable);
			column.setIsKey(colIsKey);
			column.setComment(colComment);

			columns.add(column);
			
			if ("Y".equals(colIsKey)) {
				pks.add(colId);
			}
		}
		tableBean.setColumns(columns);
		tableBean.setPks(pks);

		return tableBean;
	}

	/**
	 * 
	 * @DESCRIBE 获取excel的一页数据
	 * @DATE 2017年8月25日 下午9:07:57
	 *
	 * @param book
	 * @param sheetIndex
	 * @param rowBegin
	 * @param rowEnd
	 * @param cellBegin
	 * @param cellEnd
	 * @return
	 * @throws IOException
	 */
	private static List<List<String>> getSheetValue(Workbook book,
			int sheetIndex, int rowBegin, int rowEnd, int cellBegin, int cellEnd)
			throws IOException {
		Sheet sheet = book.getSheetAt(sheetIndex);
		List<List<String>> listRow = new ArrayList<List<String>>();
		if (rowEnd > sheet.getLastRowNum()) {
			rowEnd = sheet.getLastRowNum();
		}
		for (int i = rowBegin; i <= rowEnd; i++) {
			Row row = sheet.getRow(i);
			List<String> listCell = new ArrayList<String>();
			if (cellEnd > row.getLastCellNum()) {
				cellEnd = row.getLastCellNum();
			}
			for (int j = 0; j < cellEnd; j++) {
				Cell cell = row.getCell(j);
				String cellValue = getCellValue(cell);
				listCell.add(cellValue);
			}
			listRow.add(listCell);
		}

		return listRow;
	}

	/**
	 * @DESCRIBE 读取excel文件，处理xls和xlsx兼容性问题，根据文件后缀返回不同的Workbook实例
	 * @DATE 2017-08-23 22:24:33
	 *
	 * @param excelPath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private static Workbook getBook(String excelPath)
			throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(new File(excelPath));
		Workbook book = null;
		if (excelPath.endsWith("xls")) {
			book = new HSSFWorkbook(fis);
		} else if (excelPath.endsWith("xlsx")) {
			book = new XSSFWorkbook(fis);
		}
		return book;
	}

	/**
	 * @DESCRIBE 转单元格数据的格式，暂时只需要String类型
	 * @DATE 2017-08-23 21:35:42
	 *
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell) {
		cell.setCellType(CellType.STRING);
		return String.valueOf(cell.getStringCellValue());
	}
}
