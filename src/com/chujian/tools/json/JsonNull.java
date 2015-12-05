package com.chujian.tools.json;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JsonNull extends JsonValue {

	@Override
	public String toJsonString() {
		return "null";
	}

	@Override
	protected void read(DataInputStream dis) throws IOException {

	}

	@Override
	protected void write(DataOutputStream dos) throws IOException {
		dos.writeByte(TYPE_NULL);
	}

	@Override
	public String toString() {
		return String.valueOf("null");
	}
}
