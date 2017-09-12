<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${pkg}.dao.Base${upTumTabNam}Dao">
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

	<select id="select" parameterType="${pkg}.entity.${upTumTabNam}Entity"
		resultMap="BaseResultMap">
		select
		<include refid="Base_Column_List" />
		from ${tabNam} where 1=1	
		<if test="_parameter!=null">	
			<#list columns as col>	
			<if test="isInit${col.upJavaName}">
				<choose>
					<when test="${col.lowJavaName} != null">
						and ${col.columnName} = #${"\{"}${col.lowJavaName}}
					</when>
					<otherwise>
						and ${col.columnName} IS NULL
					</otherwise>	
				</choose>
			</if>
			</#list>
		</if>
		<#if hasPk>
		order by
		<include refid="Primary_Key_List" />
		</#if>
	</select>
	<#if hasPk>
	
	<select id="selectByPK" parameterType="${pkg}.entity.${upTumTabNam}Entity"
		resultMap="BaseResultMap">
		select
		<include refid="Base_Column_List" />
		from ${tabNam}
		where 
		<#list pkList as pk>
		${pk.columnName} = #${"\{"}${pk.lowJavaName}}<#if pk_has_next> AND </#if>
		</#list>
		order by
		<include refid="Primary_Key_List" />
	</select>
	</#if>
	<#if hasPk>
	
	<delete id="delete" parameterType="${pkg}.entity.${upTumTabNam}Entity">
		delete from ${tabNam}
		where
		<#list pkList as pk>
		${pk.columnName} = #${"\{"}${pk.lowJavaName}}<#if pk_has_next> AND </#if>
		</#list>
	</delete>
	</#if>
	
	<insert id="insert" parameterType="${pkg}.entity.${upTumTabNam}Entity">
		insert into ${tabNam}
		<trim prefix="(" suffix=")" suffixOverrides=",">		
		<#list columns as col>
			<if test="${col.lowJavaName} != null">
				${col.columnName},
			</if>		
		</#list>
		</trim>
		<trim prefix="values (" suffix=")" suffixOverrides=",">
		<#list columns as col>
			<if test="${col.lowJavaName} != null">
				#${"\{"}${col.lowJavaName}},
			</if>	
		</#list>
		</trim>
	</insert>
	
	<update id="updates">
		update ${tabNam}
		<set>
		<#list columns as col>
			<if test="value.${col.lowJavaName} != null">
				${col.columnName} = #${"\{"}value.${col.lowJavaName}},
			</if>
		</#list>
		</set>
		where
		<#list columns as col>
		<if test="conition.${col.lowJavaName} != null">
			${col.columnName} =
			#${"\{"}conition.${col.lowJavaName}},
		</if>
		</#list>
	</update>
	<#if hasPk>
	
	<update id="update" parameterType="${pkg}.entity.${upTumTabNam}Entity">
		update ${tabNam}
		<set>
		<#list columns as col>
			<if test="${col.lowJavaName} != null">
				${col.columnName} = #${"\{"}${col.lowJavaName}},
			</if>
		</#list>
		</set>
		where 
		<#list pkList as pk>
		${pk.columnName} = #${"\{"}${pk.lowJavaName}}<#if pk_has_next> AND </#if>
		</#list>
	</update>	
	</#if>
	
	<select id="selectPage" resultMap="BaseResultMap">
		select 
		<include refid="Base_Column_List" />
		from
		${tabNam} where 1=1
		<if test="${lowTumTabNam}!=null">
			<#list columns as col>
			<if test="${lowTumTabNam}.isInit${col.upJavaName}">
				<choose>
					<when test="${lowTumTabNam}.${col.lowJavaName} != null">
						and ${col.columnName} = #${"\{"}${lowTumTabNam}.${col.lowJavaName}}
					</when>
					<otherwise>
						and ${col.columnName} IS NULL
					</otherwise>	
				</choose>
			</if>
			</#list>
		</if>
		order by
		<include refid="Primary_Key_List" />
		limit #${"\{"}offset},#${"\{"}pageSize}
	</select>
	
	<select id="selectTotal" parameterType="${pkg}.entity.${upTumTabNam}Entity" resultType="Integer">
		select count(*) totalRecord from ${tabNam}
		where 1=1
		<if test="_parameter!=null">
			<#list columns as col>
			<if test="isInit${col.upJavaName}">
				<choose>
					<when test="${col.lowJavaName} != null">
						and ${col.columnName} = #${"\{"}${col.lowJavaName}}
					</when>
					<otherwise>
						and ${col.columnName} IS NULL
					</otherwise>	
				</choose>
			</if>
			</#list>
		</if>
	</select>
</mapper>  