/**
 * @PROJECT 
 * @DATE 2017-08-23 21:15:05
 * @AUTHOR LUYU
 */
package com.kool.tlwp.excel;

import java.util.ArrayList;

/**
 * @DESCRIBE
 * @AUTHOR LUYU
 * @DATE 2017-08-23 21:15:05
 *
 */
public class TableBean {
	private String createTime;
	private String tableId;
	private String tableName;
	private ArrayList<ColumnBean> columns;
	private ArrayList<String> pks;

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public ArrayList<ColumnBean> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<ColumnBean> columns) {
		this.columns = columns;
	}

	public ArrayList<String> getPks() {
		return pks;
	}

	public void setPks(ArrayList<String> pks) {
		this.pks = pks;
	}
	
}
