package ${pkg}.entity;

import java.util.HashMap;
import java.util.Map;
import com.kool.tlwp.entity.BaseEntity;

public class ${upTumTabNam}Entity extends BaseEntity {
	<#list columns as col>
	protected ${col.javaType} ${col.lowJavaName};
	protected boolean isInit${col.upJavaName};
	</#list>

	<#list columns as col>
	public boolean getIsInit${col.upJavaName}() {
		return this.isInit${col.upJavaName};
	}

	</#list>	
	<#list columns as col>
	public ${col.javaType} get${col.upJavaName}() {
		return ${col.lowJavaName};
	}

	public void set${col.upJavaName}(${col.javaType} ${col.lowJavaName}) {
		this.${col.lowJavaName} = ${col.lowJavaName};
		this.isInit${col.upJavaName} = true;
	}

	</#list>
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		<#list columns as col>
		if (isInit${col.upJavaName}) {
			map.put("${col.lowJavaName}", formatString(${col.lowJavaName}));
		}
		</#list>

		return map;
	}
}
