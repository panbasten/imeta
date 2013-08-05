package com.panet.imeta.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessagePack", propOrder = { "success", "message" })
public class MessagePack implements java.io.Serializable {

	private static final long serialVersionUID = -7104845465624591448L;

	/**
	 * 代码
	 */
	@XmlElement(required = true)
	protected boolean success;

	/**
	 * 消息
	 */
	@XmlElement(required = true)
	protected String message;

	public MessagePack(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
