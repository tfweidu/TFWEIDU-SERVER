package com.chujian.minaUtil;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * @author qin ±à½âÂë¹¤³§
 */
public class MinaCodecFactory implements ProtocolCodecFactory {

	private MessageProtocalDecoder mDecoder;
	private MessageProtocalEncoder mEncoder;

	public MinaCodecFactory() {
		this(Charset.defaultCharset());
	}

	public MinaCodecFactory(Charset mCharset) {

		mEncoder = new MessageProtocalEncoder(mCharset);
		mDecoder = new MessageProtocalDecoder(mCharset);

	}

	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception {
		return mDecoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception {
		return mEncoder;
	}

}
