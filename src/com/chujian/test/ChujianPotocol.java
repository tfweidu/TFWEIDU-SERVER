package com.chujian.test;

/**
 * @author qin
 *
 *         ͨ��Э��
 */
public class ChujianPotocol {

	/**
	 * ������0x01����Ӧ��0x02
	 */
	private byte tagRes;

	/**
	 * ������������� 0x01:String 0x02:JsonString
	 */
	private byte dataType;

	/**
	 * ���ݳ���
	 */
	private String length;

	/**
	 * ���ݼ�
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
	 * @return ������Ӧ״̬
	 * 
	 */
	public byte getTagRes() {
		return tagRes;
	}

	/**
	 * @param tagRes
	 *            ����������Ӧ״̬ *
	 */
	public void setTagRes(byte tagRes) {
		this.tagRes = tagRes;
	}

	/**
	 * @return ��ȡ�������� *
	 */
	public byte getDataType() {
		return dataType;
	}

	/**
	 * @param dataType
	 *            ������������
	 * 
	 */
	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return ��ȡ��������
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data
	 *            ���ô�������
	 */
	public void setData(String data) {
		this.data = data;
	}

}
