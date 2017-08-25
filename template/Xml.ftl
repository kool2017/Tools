<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${pkg}.dao.${upTumTabNam}Dao">
	<resultMap id="BaseResultMap" type="${pkg}.entity.${upTumTabNam}Entity">
		<#list columns as col>
			<#if col.columnKey == "PRI">
		<id column="${col.columnName}" property="${col.lowJavaName}" />
			<#else>
		<result column="${col.columnName}" property="${col.lowJavaName}" />
			</#if>
		</#list>
	</resultMap>

	<sql id="Base_Column_List">
		<#list columns as col>
			${col.columnName}<#if col_has_next>, </#if>			
		</#list>
	</sql>
	<#if hasPk>
	
	<sql id="Primary_Key_List">
		<#list pkList as pk>${pk.columnName}<#if pk_has_next>, </#if></#list>
	</sql>
	</#if>
</mapper>  