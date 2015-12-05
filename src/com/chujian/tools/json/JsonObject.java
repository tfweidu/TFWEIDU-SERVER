package com.chujian.tools.json;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author wenhua.li 2008-12-30 <br>
 *         Json format of Ojbect.
 */
public final class JsonObject extends JsonValue {

	private HashMap<String, JsonValue> ht = new HashMap<String, JsonValue>();

	public String[] getKeys() {
		int len = ht.size();
		String[] ret = new String[len];
		ht.keySet().toArray(ret);
		return ret;
	}

	/**
	 * Get a JsonValue by key
	 * 
	 * @param name
	 *            a String of key
	 * @return a JsonValue
	 */
	public JsonValue getJsonValue(String name) {
		Object obj = ht.get(name);
		if (obj != null) {
			return (JsonValue) obj;
		}
		return null;
	}

	public String getString(String name) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonString) {
				return ((JsonString) val).getValue();
			} else {
				System.err.println("The value is not a JsonString! " + name
						+ ":" + val);
			}
		} else {
			// System.out.println(name + " :String is not exist!");
		}
		return null;
	}

	public JsonObject getJsonObject(String name) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonObject) {
				return (JsonObject) val;
			} else {
				System.err.println("The value is not a JsonObject! " + name
						+ ":" + val);
			}
		} else {
			// System.out.println(name + " :JsonObject is not exist!");
		}
		return null;
	}

	public JsonArray getJsonArray(String name) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonArray) {
				return (JsonArray) val;
			} else {
				System.err.println("The value is not a JsonArray! " + name
						+ ":" + val);
			}
		} else {
			// System.out.println(name + " :JsonArray is not exist!");
		}
		return null;
	}

	public long getNum(String name) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonNum) {
				return Long.valueOf(((JsonNum) val).getValue());
			} else if (val instanceof JsonString) {
				String num = ((JsonString) val).getValue();
				try {
					return Long.valueOf(num);
				} catch (Exception e) {
					return 0;
				}
			} else {
				System.err.println("The value is not a JsonNum! " + name + ":"
						+ val);
			}
		} else {
			// System.out.println(name + " :Num is not exist!");
		}
		return 0;
	}

	public long getNum(String name, long defaultValue) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonNum) {
				return Long.valueOf(((JsonNum) val).getValue());
			} else if (val instanceof JsonString) {
				String num = ((JsonString) val).getValue();
				try {
					return Long.valueOf(num);
				} catch (Exception e) {
					return defaultValue;
				}

			} else {
				System.err.println("The value is not a JsonNum! " + name + ":"
						+ val);
			}
		} else {
			// System.out.println(name + " :Num is not exist!");
		}
		return defaultValue;
	}

	public void setNum(String name, long num) {
		ht.put(name, new JsonNum(num));
	}

	public double getNumDouble(String name) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonNum) {
				return Double.valueOf(((JsonNum) val).getValue());
			} else {
				System.err.println("The value is not a JsonNum! " + name + ":"
						+ val);
			}
		} else {
			// System.out.println(name + " :Num is not exist!");
		}
		return 0;
	}

	public boolean getBool(String name) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonBool) {
				return ((JsonBool) val).getValue();
			} else {
				System.err.println("The value is not a JsonBool! " + name + ":"
						+ val);
			}
		} else {
			// System.out.println(name + " :Bool is not exist!");
		}
		return false;
	}

	public byte[] getBytes(String name) {
		JsonValue val = getJsonValue(name);
		if (val != null) {
			if (val instanceof JsonBytes) {
				return ((JsonBytes) val).getValue();
			} else {
				System.err.println("The value is not a JsonBytes! " + name
						+ ":" + val);
			}
		} else {
			// System.out.println(name + " :Bool is not exist!");
		}
		return null;
	}

	public boolean containsKey(String name) {
		return ht.containsKey(name);
	}

	/**
	 * Add a map of a key and a value
	 * 
	 * @param name
	 *            key of map
	 * @param obj
	 *            value of map
	 */
	public void put(String name, JsonValue value) {
		if (value != null) {
			ht.put(name, value);
		}
	}

	public void put(String name, long value) {
		ht.put(name, new JsonNum(value));
	}

	public void put(String name, double value) {
		ht.put(name, new JsonNum(value));
	}

	public void put(String name, String value) {
		if (value != null) {
			ht.put(name, new JsonString(value));
		}
	}

	public void put(String name, boolean value) {
		ht.put(name, new JsonBool(value));
	}

	public void put(String name, byte[] value) {
		if (value != null) {
			ht.put(name, new JsonBytes(value));
		}
	}

	public void remove(Object key) {
		if (key != null) {
			ht.remove(key);
		}
	}

	/**
	 * Get size of JsonObject
	 * 
	 * @return a int
	 */
	public int size() {
		return ht.size();
	}

	@Override
	public String toJsonString() {
		StringBuffer sb = new StringBuffer();
		sb.append('{');
		if (ht.size() > 0) {
			String[] keys = getKeys();
			for (int i = 0; i < keys.length; i++) {
				JsonValue obj = ht.get(keys[i]);
				sb.append('"').append(keys[i]).append('"').append(':')
						.append(obj.toJsonString()).append(',');
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Parse a String to a JsonObject.
	 * 
	 * @param s
	 *            a String
	 * @return a JsonObject
	 */
	public static JsonObject parseObject(String s) {
		if (s == null) {
			return null;
		}
		JsonObject ret = new JsonObject();
		if (s.equals("")) {
			return ret;
		}
		final int len = s.length();
		char curChar = 0;
		int sIdx = -1, eIdx;
		while (true) {
			while (++sIdx < len && (curChar = s.charAt(sIdx)) < 33)
				;
			if (sIdx < len) {
				if (curChar == '"') {
					sIdx++;
					eIdx = sIdx;
					while (true) {
						eIdx = s.indexOf('"', eIdx);
						if (eIdx > -1) {
							if (s.charAt(eIdx - 1) != '\\') {
								break;
							} else {
								eIdx++;
							}
						}
					}
					if (eIdx > -1) {
						String name = s.substring(sIdx, eIdx);
						name = JsonString.parseString(name).getValue();
						sIdx = eIdx;
						while (++sIdx < len && (curChar = s.charAt(sIdx)) < 33)
							;
						if (curChar == ':') {
							eIdx = sIdx++;
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
								if (sIdx == eIdx) {
									ret.put(name, new JsonNull());
								} else {
									String value = s.substring(sIdx, eIdx);
									ret.put(name, parseValue(value));
									sIdx = eIdx;
								}
							} else {
								return null;
							}
						} else {
							System.err
									.println("':' is expected between name and value!"
											+ name);
						}
					} else {
						System.err.println("'\"' is expected after name at"
								+ sIdx);
					}
				} else {
					System.err
							.println("'\"' is expected before name at" + sIdx);
				}
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
			String key = dis.readUTF();
			JsonValue value = readObject(dis);
			ht.put(key, value);
		}
	}

	@Override
	protected void write(DataOutputStream dos) throws IOException {
		dos.writeByte(TYPE_OBJECT);
		int n = ht.size();
		dos.writeInt(n);
		Iterator<Entry<String, JsonValue>> iter = ht.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, JsonValue> entry = iter.next();
			dos.writeUTF(entry.getKey());
			writeObject(entry.getValue(), dos);
		}
	}

	@Override
	public String toString() {
		return String.valueOf(ht);
	}

	public void clear() {
		ht.clear();
	}
}
