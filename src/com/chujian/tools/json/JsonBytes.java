package com.chujian.tools.json;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * 封装byte数组，用作本地对象交换，不参与传输和解析过程
 * 
 * @author wenhua.li
 * 
 */
public class JsonBytes extends JsonValue {

	private byte[] value = null;

	public JsonBytes() {
	}

	public JsonBytes(byte[] val) {
		value = val;
	}

	public byte[] getValue() {
		return value;
	}

	@Override
	public String toJsonString() {
		if (value == null) {
			return null;
		}
		return Base64.base64Encode(value);
	}

	protected static JsonBytes parseBytes(String s) {
		return new JsonBytes(Base64.base64Decode(s));
	}

	@Override
	protected void read(DataInputStream dis) throws IOException {
		int n = dis.readInt();
		if (n > -1) {
			value = new byte[n];
			dis.readFully(value);
		}
	}

	@Override
	protected void write(DataOutputStream dos) throws IOException {
		dos.writeByte(TYPE_BYTES);
		if (value != null) {
			dos.writeInt(value.length);
			dos.write(value);
		} else {
			dos.writeInt(-1);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

}
