/**
 * @PROJECT TLWP1.0
 * @DATE 2016-12-19 23:12:21
 * @AUTHOR LUYU
 */
package com.kool.tlwp.generator;


/**
 * @DESCRIBE 表结构映射类
 * @AUTHOR LUYU
 * @DATE 2016-12-19 23:12:21
 *
 */
public class ColumnsValue {
	protected String tableCatalog;
	protected String tableSchema;
	protected String tableName;
	protected String columnName;
	protected double ordinalPosition;
	protected String columnDefault;
	protected String isNullable;
	protected String dataType;
	protected double characterMaximumLength;
	protected double characterOctetLength;
	protected double numericPrecision;
	protected double numericScale;
	protected String characterSetName;
	protected String collationName;
	protected String columnType;
	protected String columnKey;
	protected String extra;
	protected String privileges;
	protected String columnComment;

	public String getTableCatalog() {
		return tableCatalog;
	}

	public void setTableCatalog(String tableCatalog) {
		this.tableCatalog = tableCatalog;
	}

	public String getTableSchema() {
		return tableSchema;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public double getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(double ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public double getCharacterMaximumLength() {
		return characterMaximumLength;
	}

	public void setCharacterMaximumLength(double characterMaximumLength) {
		this.characterMaximumLength = characterMaximumLength;
	}

	public double getCharacterOctetLength() {
		return characterOctetLength;
	}

	public void setCharacterOctetLength(double characterOctetLength) {
		this.characterOctetLength = characterOctetLength;
	}

	public double getNumericPrecision() {
		return numericPrecision;
	}

	public void setNumericPrecision(double numericPrecision) {
		this.numericPrecision = numericPrecision;
	}

	public double getNumericScale() {
		return numericScale;
	}

	public void setNumericScale(double numericScale) {
		this.numericScale = numericScale;
	}

	public String getCharacterSetName() {
		return characterSetName;
	}

	public void setCharacterSetName(String characterSetName) {
		this.characterSetName = characterSetName;
	}

	public String getCollationName() {
		return collationName;
	}

	public void setCollationName(String collationName) {
		this.collationName = collationName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getPrivileges() {
		return privileges;
	}

	public void setPrivileges(String privileges) {
		this.privileges = privileges;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	/**
	 * @DESCRIBE 将数据库字段类型转为java类型
	 * @AUTHOR LUYU
	 * @DATE 2016-12-21 21:38:52
	 *
	 * @param dataType
	 * @return
	 */
	public static String transferType(String dataType) {
		String type = null;
		if ("VARCHAR".equalsIgnoreCase(dataType)
				|| ("CHAR".equalsIgnoreCase(dataType))) {
			type = "String";
		} else if ("INT".equalsIgnoreCase(dataType)) {
			type = "int";
		} else if ("DOUBLE".equalsIgnoreCase(dataType)) {
			type = "double";
		} else if ("DATE".equalsIgnoreCase(dataType)) {
			type = "java.sql.Date";
		} else if ("TIME".equalsIgnoreCase(dataType)) {
			type = "java.sql.Time";
		} else if ("TIMESTAMP".equalsIgnoreCase(dataType)) {
			type = "java.sql.Timestamp";
		} else if ("DECIMAL".equalsIgnoreCase(dataType)) {
			type = "java.math.BigDecimal";
		} else{
			type = "Object";
		}
		return type;
	}

	/**
	 * @DESCRIBE 将数据库字段类型转为ResultSet支持 类型
	 * @AUTHOR LUYU
	 * @DATE 2017-02-19 17:37:39
	 *
	 * @param dataType
	 * @return
	 */
	public static String transferRSType(String dataType) {
		String type = null;
		if ("VARCHAR".equalsIgnoreCase(dataType)
				|| ("CHAR".equalsIgnoreCase(dataType))) {
			type = "String";
		} else if ("INT".equalsIgnoreCase(dataType)) {
			type = "Integer";
		} else if ("DOUBLE".equalsIgnoreCase(dataType)) {
			type = "Double";
		} else if ("DATE".equalsIgnoreCase(dataType)) {
			type = "Date";
		} else if ("TIME".equalsIgnoreCase(dataType)) {
			type = "Time";
		} else if ("TIMESTEMP".equalsIgnoreCase(dataType)) {
			type = "Timestamp";
		} else if ("DECIMAL".equalsIgnoreCase(dataType)) {
			type = "BigDecimal";
		} else{
			type = "Object";
		}
		return type;
	}
}
