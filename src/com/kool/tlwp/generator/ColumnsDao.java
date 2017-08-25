/**
 * @PROJECT TLWP1.0
 * @DATE 2016-12-19 22:56:53
 * @AUTHOR LUYU
 */
package com.kool.tlwp.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.kool.tlwp.exception.AppException;
import com.kool.tlwp.util.StringUtils;

/**
 * @DESCRIBE 表结构DAO类
 * @AUTHOR LUYU
 * @DATE 2016-12-19 22:56:53
 *
 */
public class ColumnsDao {
	protected Connection connection = null;
	protected static Logger logger = Logger.getLogger("DAO");

	public ColumnsDao() {
	}

	public ColumnsDao(Connection connection) {
		this.connection = connection;
	}

	public List<ColumnsValue> select(ColumnsValue condition) throws SQLException,
			AppException {
		if (null == condition) {
			throw new AppException("condition could not be null!");
		}
		List<ColumnsValue> listRet = new ArrayList<ColumnsValue>();

		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT * FROM information_schema.COLUMNS ");
		sbSql.append(" WHERE 1=1 ");

		if (!StringUtils.isEmpty(condition.getTableName())) {
			sbSql.append(" AND TABLE_NAME = ? ");
		}

		String sql = sbSql.toString();
		logger.debug(sql);

		PreparedStatement state;
		state = connection.prepareStatement(sql);
		int i = 1;

		if (!StringUtils.isEmpty(condition.getTableName())) {
			state.setString(i, condition.getTableName());
			i++;
		}

		ResultSet rs = state.executeQuery();
		while (rs.next()) {
			ColumnsValue result = new ColumnsValue();
			result.setTableCatalog(rs.getString("TABLE_CATALOG"));
			result.setTableSchema(rs.getString("TABLE_SCHEMA"));
			result.setTableName(rs.getString("TABLE_NAME"));
			result.setColumnName(rs.getString("COLUMN_NAME"));
			result.setOrdinalPosition(rs.getDouble("ORDINAL_POSITION"));
			result.setColumnDefault(rs.getString("COLUMN_DEFAULT"));
			result.setIsNullable(rs.getString("IS_NULLABLE"));
			result.setDataType(rs.getString("DATA_TYPE"));
			result.setCharacterMaximumLength(rs
					.getDouble("CHARACTER_MAXIMUM_LENGTH"));
			result.setCharacterOctetLength(rs
					.getDouble("CHARACTER_OCTET_LENGTH"));
			result.setNumericPrecision(rs.getDouble("NUMERIC_PRECISION"));
			result.setNumericScale(rs.getDouble("NUMERIC_SCALE"));
			result.setCharacterSetName(rs.getString("CHARACTER_SET_NAME"));
			result.setCollationName(rs.getString("COLLATION_NAME"));
			result.setColumnType(rs.getString("COLUMN_TYPE"));
			result.setColumnKey(rs.getString("COLUMN_KEY"));
			result.setExtra(rs.getString("EXTRA"));
			result.setPrivileges(rs.getString("PRIVILEGES"));
			result.setColumnComment(rs.getString("COLUMN_COMMENT"));

			listRet.add(result);
		}

		return listRet;
	}

	/**
	 * @DESCRIBE 获取主键
	 * @AUTHOR LUYU
	 * @DATE 2017-02-06 22:09:46
	 *
	 * @param sTableName
	 * @return
	 * @throws AppException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List getPK(String sTableName) throws AppException {
		if (StringUtils.isEmpty(sTableName)) {
			throw new AppException("condition could not be null!");
		}
		List listRet = new ArrayList();

		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT COLUMN_NAME FROM information_schema.COLUMNS ");
		sbSql.append(" WHERE TABLE_NAME = ? ");
		sbSql.append(" AND COLUMN_KEY = 'PRI' ");

		String sql = sbSql.toString();
		logger.debug(sql);

		try {
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, sTableName);

			ResultSet rs = state.executeQuery();
			
			while (rs.next()) {
				listRet.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(),e);
			throw new AppException("数据库错误:"+e.getMessage());
		}

		return listRet;
	}
}
