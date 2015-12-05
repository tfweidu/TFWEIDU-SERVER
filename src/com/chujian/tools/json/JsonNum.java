package com.chujian.tools.json;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author wenhua.li 2008-12-30 <br>
 *         Json format of Integer.
 */
public final class JsonNum extends JsonValue {

	// public float value = 0;
	private String value = "0"; // for cldc 1.0

	public JsonNum() {
	}

	public JsonNum(long val) {
		value = String.valueOf(val);
	}

	public JsonNum(double val) {
		value = String.valueOf(val);
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toJsonString() {
		return String.valueOf(value);
	}

	/**
	 * Parse a String to a JsonNum.
	 * 
	 * @param s
	 *            a String
	 * @return a JsonNum
	 */
	protected static JsonNum parseNum(String s) {
		JsonNum ret = new JsonNum();
		try {
			// ret.value = Float.parseFloat(s);
			ret.value = s; // for cldc 1.0
		} catch (NumberFormatException e) {
			System.err.println("Number cast error!\n" + s);
		}
		return ret;
	}

	@Override
	protected void read(DataInputStream dis) throws IOException {
		value = dis.readUTF();
	}

	@Override
	protected void write(DataOutputStream dos) throws IOException {
		dos.writeByte(TYPE_NUM);
		dos.writeUTF(value);
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
