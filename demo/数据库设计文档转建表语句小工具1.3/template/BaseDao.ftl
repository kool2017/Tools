package ${pkg}.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import ${pkg}.entity.${upTumTabNam}Entity;


@Repository(value="Base${upTumTabNam}Dao")
public interface Base${upTumTabNam}Dao {

	<#if hasPk>
	public ${upTumTabNam}Entity selectByPK(${upTumTabNam}Entity ${lowTumTabNam});

	</#if>
	public List<${upTumTabNam}Entity> select(${upTumTabNam}Entity ${lowTumTabNam});

	<#if hasPk>
	public void delete(${upTumTabNam}Entity ${lowTumTabNam});

	</#if>
	public void deletes(${upTumTabNam}Entity ${lowTumTabNam});

	<#if hasPk>
	public void update(${upTumTabNam}Entity ${lowTumTabNam});

	</#if>
	public void updates(@Param("value") ${upTumTabNam}Entity value,
			@Param("condition") ${upTumTabNam}Entity condition);

	public void insert(${upTumTabNam}Entity ${lowTumTabNam});
	
	public List<${upTumTabNam}Entity> selectPage(@Param("${lowTumTabNam}") ${upTumTabNam}Entity ${lowTumTabNam}, @Param("offset") int offset, @Param("pageSize") int pageSize);
	
	public int selectTotal(${upTumTabNam}Entity ${lowTumTabNam});
}
