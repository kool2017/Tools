-- CREATE TIME:${createTime}
-- TABLE NAME:${tableName}
DROP TABLE IF EXISTS ${tableId};
CREATE TABLE ${tableId} (
	<#list columns as column>
  ${column.id} ${column.type} <#if column.isNullable="N">NOT NULL<#else>default NULL</#if> COMMENT '${column.name}'<#if column_has_next>,</#if>
	</#list>
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
ALTER TABLE ${tableId} ADD PRIMARY KEY(<#list pks as pk>${pk}<#if pk_has_next>,</#if></#list>);