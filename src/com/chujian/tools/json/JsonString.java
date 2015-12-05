package com.chujian.tools.json;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author wenhua.li 2008-12-30 <br>
 *         Json format of String.
 */
public final class JsonString extends JsonValue {

	private String value = null;

	public JsonString(String val) {
		value = val;
	}

	public JsonString() {
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toJsonString() {
		if (value == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append('"');
		int idx = 0;
		final int len = value.length();
		char ch = 0;
		while (idx < len) {
			ch = value.charAt(idx);
			switch (ch) {
			case '\"':
				sb.append('\\').append('\"');
				break;
			case '\\':
				sb.append('\\').append('\\');
				break;
			case '/':
				sb.append('\\').append('/');
				break;
			case '\b':
				sb.append('\\').append('b');
				break;
			case '\f':
				sb.append('\\').append('f');
				break;
			case '\n':
				sb.append('\\').append('n');
				break;
			case '\r':
				sb.append('\\').append('r');
				break;
			case '\t':
				sb.append('\\').append('t');
				break;
			default:
				sb.append(ch);
			}
			idx++;
		}
		sb.append('"');
		return sb.toString();
	}

	// public static void main(String[] args) {
	// String s = "\\\\1\\\"2\\/3\\b4\\f5\\n6\\r7\\t8\\u4e2d9\\x2e";
	// System.out.println(s);
	// System.out.println(parseString(s));
	// }

	/**
	 * Parse a String to a JsonString.
	 * 
	 * @param s
	 *            a String
	 * @return a JsonString
	 */
	protected static JsonString parseString(String s) {
		StringBuffer sb = new StringBuffer();
		int idx = 0;
		final int len = s.length();
		char ch = 0;
		while (idx < len) {
			ch = s.charAt(idx);
			if (ch == '\\' && ++idx < len) {
				ch = s.charAt(idx);
				switch (ch) {
				case '"':
					sb.append('\"');
					break;
				case '\\':
					sb.append('\\');
					break;
				case '/':
					sb.append('/');
					break;
				case 'b':
					sb.append('\b');
					break;
				case 'f':
					sb.append('\f');
					break;
				case 'n':
					sb.append('\n');
					break;
				case 'r':
					sb.append('\r');
					break;
				case 't':
					sb.append('\t');
					break;
				case 'u':
					if (idx + 5 <= len) {
						int c = Integer.parseInt(s.substring(idx + 1, idx + 5),
								16);
						sb.append((char) c);
						idx += 4;
					}
					break;
				case 'x':
					if (idx + 3 <= len) {
						int c = Integer.parseInt(s.substring(idx + 1, idx + 3),
								16);
						sb.append((char) c);
						idx += 2;
					}
					break;
				default:
					sb.append(ch);
				}
			} else {
				sb.append(ch);
			}
			idx++;
		}
		JsonString ret = new JsonString();
		ret.value = sb.toString();
		return ret;
	}

	@Override
	protected void read(DataInputStream dis) throws IOException {
		int n = dis.readInt();
		if (n > -1) {
			byte[] b = new byte[n];
			dis.readFully(b);
			value = new String(b, "UTF-8");
		}
	}

	@Override
	protected void write(DataOutputStream dos) throws IOException {
		dos.writeByte(TYPE_STRING);
		if (value != null) {
			byte[] b = value.getBytes("UTF-8");
			dos.writeInt(b.length);
			dos.write(b);
		} else {
			dos.writeInt(-1);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}
}
