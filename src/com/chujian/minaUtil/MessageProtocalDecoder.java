package com.chujian.minaUtil;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderAdapter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.chujian.test.ChujianPotocol;

public class MessageProtocalDecoder extends CumulativeProtocolDecoder {

	private Charset mCharset;

	private AttributeKey CONTEXT = new AttributeKey(getClass(), "context");

	public MessageProtocalDecoder(Charset mCharset) {

		this.mCharset = mCharset;

	}

	@Override
	public boolean doDecode(IoSession session, IoBuffer buffer,
			ProtocolDecoderOutput out) throws Exception {

		System.out.println("解码");

		Context ctx = getContext(session);

		CharsetDecoder cd = mCharset.newDecoder();

		int matchCount = ctx.getMatchCount();
		int line = ctx.getLine();

		IoBuffer mIoBuffer = ctx.innerIoButter;

		byte tagDecode = ctx.getTag();
		byte dataTypeDecode = ctx.getDataType();

		String lengthDecode = ctx.getLength();
		String dataDecode = ctx.getDatdStr();

		while (buffer.hasRemaining()) {

			byte bTemp = buffer.get();

			matchCount++;

			mIoBuffer.put(bTemp);

			if (line < 3 && bTemp == 10) {
				if (line == 0) {

					mIoBuffer.flip();

					tagDecode = mIoBuffer.get();

					matchCount = 0;
					mIoBuffer.clear();
					ctx.setTag(tagDecode);

					System.out.println(String.valueOf(tagDecode));

				}
				if (line == 1) {

					mIoBuffer.flip();

					dataTypeDecode = mIoBuffer.get();

					matchCount = 0;
					mIoBuffer.clear();
					ctx.setDataType(dataTypeDecode);

					System.out.println(String.valueOf(dataTypeDecode));

				}

				if (line == 2) {

					mIoBuffer.flip();

					lengthDecode = mIoBuffer.getString(matchCount, cd);

					lengthDecode = lengthDecode.substring(0,
							lengthDecode.length() - 1);

					ctx.setLength(lengthDecode);

					mIoBuffer.clear();

					matchCount = 0;

					System.out.println(lengthDecode);

				}

				line++;

			} else if (line == 3) {

				if (matchCount == Integer.valueOf(lengthDecode)) {

					mIoBuffer.flip();

					dataDecode = mIoBuffer.getString(matchCount, cd);

					ctx.setDatdStr(dataDecode);

					ctx.setMatchCount(matchCount);

					ctx.setLine(line);

					System.out.println(dataDecode);

					break;

				}

				ctx.setMatchCount(matchCount);

				ctx.setLine(line);

			}
		}

		if (ctx.getLine() == 3
				&& Integer.valueOf(ctx.getLength()) == ctx.getMatchCount()) {

			ChujianPotocol mChujianPL = new ChujianPotocol();

			mChujianPL.setTagRes(tagDecode);
			mChujianPL.setDataType(dataTypeDecode);
			mChujianPL.setLength(lengthDecode);
			mChujianPL.setData(dataDecode);

			out.write(mChujianPL);

			ctx.reset();

			return true;

		} else {
			return false;
		}

	}

	/**
	 * @param session
	 * @return 获取私有类
	 * 
	 */
	private Context getContext(IoSession session) {
		Context mContext = (Context) session.getAttribute(CONTEXT);

		if (mContext == null) {
			mContext = new Context();
			session.setAttribute(CONTEXT, mContext);
		}

		return mContext;
	}

	/**
	 * @author qin 私有类，相当于类变量
	 */
	private class Context {

		private IoBuffer innerIoButter;

		private byte tag;

		private byte dataType;

		private String length;

		private String datdStr;

		public Context() {

			innerIoButter = IoBuffer.allocate(1024).setAutoExpand(true);

		}

		private int matchCount = 0;

		private int line = 0;

		public String getLength() {
			return length;
		}

		public void setLength(String length) {
			this.length = length;
		}

		public byte getTag() {
			return tag;
		}

		public void setTag(byte tag) {
			this.tag = tag;
		}

		public byte getDataType() {
			return dataType;
		}

		public void setDataType(byte dataType) {
			this.dataType = dataType;
		}

		public String getDatdStr() {
			return datdStr;
		}

		public void setDatdStr(String datdStr) {
			this.datdStr = datdStr;
		}

		public int getMatchCount() {
			return matchCount;
		}

		public void setMatchCount(int matchCount) {
			this.matchCount = matchCount;
		}

		public int getLine() {
			return line;
		}

		public void setLine(int line) {
			this.line = line;
		}

		public void reset() {

			this.innerIoButter.clear();
			this.tag = 0x01;
			this.dataType = 0x01;
			this.datdStr = "";
			this.length = "";

			this.matchCount = 0;
			this.line = 0;

		}

	}

}
