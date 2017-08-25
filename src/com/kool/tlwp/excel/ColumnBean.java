/**
 * @PROJECT 
 * @DATE 2017-08-23 21:16:07
 * @AUTHOR LUYU
 */
package com.kool.tlwp.excel;

/**
 * @DESCRIBE
 * @AUTHOR LUYU
 * @DATE 2017-08-23 21:16:07
 *
 */
public class ColumnBean {
	private String id;
	private String humpName;
	private String name;
	private String type;
	private String isKey;
	private String isNullable;
	private String comment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHumpName() {
		return humpName;
	}

	public void setHumpName(String humpName) {
		this.humpName = humpName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsKey() {
		return isKey;
	}

	public void setIsKey(String isKey) {
		this.isKey = isKey;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
