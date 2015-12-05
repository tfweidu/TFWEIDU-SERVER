package com.chujian.minaUtil;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.chujian.test.ChujianPotocol;

/**
 * @author qin ±àÂë
 */
public class MessageProtocalEncoder extends ProtocolEncoderAdapter {

	/**
	 * ×Ö·û¼¯
	 */
	private Charset charset = null;

	public MessageProtocalEncoder(Charset mCharset) {

		this.charset = mCharset;

	}

	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {

		ChujianPotocol mChujianPL = (ChujianPotocol) message;

		System.out.println("mChujianPL.toString()" + mChujianPL.toString());

		CharsetEncoder mCharsetEncode = charset.newEncoder();

		IoBuffer mIobf = IoBuffer.allocate(1024).setAutoExpand(true);

		byte tagEncode = mChujianPL.getTagRes();

		byte dataTypeEncode = mChujianPL.getDataType();

		String length = mChujianPL.getLength();

		String dataEncode = mChujianPL.getData();

		mIobf.put(tagEncode);
		mIobf.putString("\n", mCharsetEncode);

		mIobf.put(dataTypeEncode);
		mIobf.putString("\n", mCharsetEncode);

		mIobf.putString(length + "\n", mCharsetEncode);

		mIobf.putString(dataEncode, mCharsetEncode);

		mIobf.flip();

		out.write(mIobf);

	}

}
