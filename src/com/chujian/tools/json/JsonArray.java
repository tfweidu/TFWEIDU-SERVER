package com.chujian.tools.json;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author wenhua.li 2008-12-30 <br>
 *         The JsonArray is container of JsonValue items.
 */
public final class JsonArray extends JsonValue {

	private Vector<JsonValue> vec = new Vector<JsonValue>();

	/**
	 * Copies the components of this JsonArray into the specified JsonValue
	 * array.
	 * 
	 * @param objs
	 */
	public void copyInto(JsonValue[] objs) {
		if (objs != null) {
			vec.copyInto(objs);
		}
	}

	public void clear() {
		vec.removeAllElements();
	}

	/**
	 * 获取对象数组
	 * 
	 * @return 对象数组
	 */
	public JsonValue[] getValue() {
		JsonValue[] arr = new JsonValue[vec.size()];
		vec.copyInto(arr);
		return arr;
	}

	public JsonValue get(int idx) {
		return vec.get(idx);
	}

	/**
	 * Add a JsonValue instance
	 * 
	 * @param obj
	 */
	public void add(JsonValue obj) {
		vec.addElement(obj);
	}

	public void add(JsonValue obj, int index) {
		vec.insertElementAt(obj, index);
	}

	public void add(String s) {
		add(new JsonString(s));
	}

	/**
	 * Get size of JsonArray
	 * 
	 * @return a int
	 */
	public int size() {
		return vec.size();
	}

	@Override
	public String toJsonString() {
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		if (vec.size() > 0) {
			Enumeration<JsonValue> e = vec.elements();
			while (e.hasMoreElements()) {
				JsonValue obj = e.nextElement();
				sb.append(obj.toJsonString()).append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * parse a String to a JsonArray
	 * 
	 * @param s
	 *            a String
	 * @return a JsonArray
	 */
	protected static JsonArray parseArray(String s) {
		if (s == null) {
			return null;
		}
		JsonArray ret = new JsonArray();
		if (s.equals("")) {
			return ret;
		}
		final int len = s.length();
		int sIdx = -1, eIdx;
		while (true) {
			while (++sIdx < len && s.charAt(sIdx) < 33)
				;
			eIdx = sIdx - 1;
			int count1 = 0, count2 = 0;
			boolean quote = false;
			while (++eIdx < len) {
				char ch = s.charAt(eIdx);
				if (ch == '{' && !quote) {
					count1++;
				} else if (ch == '}' && !quote) {
					count1--;
				} else if (ch == '[' && !quote) {
					count2++;
				} else if (ch == ']' && !quote) {
					count2--;
				} else if (ch == '"') {
					quote = !quote;
				} else if (ch == '\\') {
					eIdx++;
				} else if (ch == ',' && !quote) {
					if (count1 == 0 && count2 == 0) {
						break;
					}
				}
			}
			if (eIdx <= len) {
				String str = s.substring(sIdx, eIdx);
				ret.add(parseValue(str));
				sIdx = eIdx;
			} else {
				break;
			}
		}
		return ret;
	}

	@Override
	protected void read(DataInputStream dis) throws IOException {
		int n = dis.readInt();
		for (int i = 0; i < n; i++) {
			vec.add(readObject(dis));
		}
	}

	@Override
	protected void write(DataOutputStream dos) throws IOException {
		dos.writeByte(TYPE_ARRAY);
		int n = vec.size();
		dos.writeInt(n);
		for (int i = 0; i < n; i++) {
			writeObject(vec.elementAt(i), dos);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(vec);
	}
}
