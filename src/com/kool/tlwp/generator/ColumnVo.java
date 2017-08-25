/**
 * @PROJECT 
 * @DATE 2017-05-01 08:30:57
 * @AUTHOR LUYU
 */
package com.kool.tlwp.generator;

/**
 * @DESCRIBE
 * @AUTHOR LUYU
 * @DATE 2017-05-01 08:30:57
 *
 */
public class ColumnVo extends ColumnsValue{
	private String javaType;
	private String upJavaName;
	private String lowJavaName;
	
	public ColumnVo() {
		
	}
	
	public ColumnVo(ColumnsValue value) {
		this.tableCatalog=value.tableCatalog;
		this.tableSchema=value.tableSchema;
		this.tableName=value.tableName;
		this.columnName=value.columnName;
		this.ordinalPosition=value.ordinalPosition;
		this.columnDefault=value.columnDefault;
		this.isNullable=value.isNullable;
		this.dataType=value.dataType;
		this.characterMaximumLength=value.characterMaximumLength;
		this.characterOctetLength=value.characterOctetLength;
		this.numericPrecision=value.numericPrecision;
		this.numericScale=value.numericScale;
		this.characterSetName=value.characterSetName;
		this.collationName=value.collationName;
		this.columnType=value.columnType;
		this.columnKey=value.columnKey;
		this.extra=value.extra;
		this.privileges=value.privileges;
		this.columnComment=value.columnComment;
	}
	
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
	public String getUpJavaName() {
		return upJavaName;
	}
	public void setUpJavaName(String upJavaName) {
		this.upJavaName = upJavaName;
	}
	public String getLowJavaName() {
		return lowJavaName;
	}
	public void setLowJavaName(String lowJavaName) {
		this.lowJavaName = lowJavaName;
	}
	
}
