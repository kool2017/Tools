/**
 * @PROJECT 
 * @DATE 2017-04-26 22:43:35
 * @AUTHOR LUYU
 */
package com.kool.tlwp.exception;

/**
 * @DESCRIBE
 * @AUTHOR LUYU
 * @DATE 2017-04-26 22:43:35
 *
 */
public class AppException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errCod;
	private String errMsg;
	
	public AppException(String errCod){
		this.errCod = errCod;
		this.errMsg = errCod;
	}
	
	/**
	 * @param string
	 * @param e
	 */
	public AppException(String errCod, Exception e) {
		this.setStackTrace(e.getStackTrace());
		this.errCod = errCod;
		this.errMsg = errCod;
		
	}

	public String getMessage() {
		String msg = "";
		if (errCod != null && errMsg != null) {
			msg = errCod+" "+errMsg;
		}
		return msg;
	}
}
