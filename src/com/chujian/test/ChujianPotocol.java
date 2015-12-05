package com.chujian.test;

/**
 * @author qin
 *
 *         通信协议
 */
public class ChujianPotocol {

	/**
	 * 请求码0x01和相应码0x02
	 */
	private byte tagRes;

	/**
	 * 传输的数据类型 0x01:String 0x02:JsonString
	 */
	private byte dataType;

	/**
	 * 数据长度
	 */
	private String length;

	/**
	 * 数据集
	 */
	private String data;

	public String toString() {
		return String.valueOf(tagRes) + String.valueOf(dataType) + length
				+ data;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	/**
	 * @return 请求，相应状态
	 * 
	 */
	public byte getTagRes() {
		return tagRes;
	}

	/**
	 * @param tagRes
	 *            设置请求相应状态 *
	 */
	public void setTagRes(byte tagRes) {
		this.tagRes = tagRes;
	}

	/**
	 * @return 获取数据类型 *
	 */
	public byte getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            设置数据类型
	 * 
	 */
	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return 获取传输数据
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            设置传输数据
	 */
	public void setData(String data) {
		this.data = data;
	}

}
