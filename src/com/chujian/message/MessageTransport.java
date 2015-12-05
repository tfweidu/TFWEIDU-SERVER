package com.chujian.message;

public class MessageTransport {

	public static synchronized MessageTransport parseString(String str) {
		MessageTransport ms = new MessageTransport();

		String[] temp = str.split(":");

		ms.setFromId(temp[1]);

		ms.setToId(temp[3]);

		ms.setDataStr(temp[5]);

		return ms;
	}

	private String fromId;

	private String toId;

	private String dataStr;

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getDataStr() {
		return dataStr;
	}

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}

}
