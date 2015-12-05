package com.chujian.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.chujian.minaUtil.MinaCodecFactory;

public class MinaServer {

	public static void main(String[] args) {

		NioSocketAcceptor acceptor = new NioSocketAcceptor();

		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();

		chain.addLast("codec", new ProtocolCodecFilter(new MinaCodecFactory(
				Charset.forName("UTF-8"))));

		acceptor.setHandler(new ServerHandler());

		int bindPort = 9988;
		
		try {
			acceptor.bind(new InetSocketAddress(bindPort));
		} catch (IOException e) {
			System.out.println("Mina Server start for error!" + bindPort);
			e.printStackTrace();
		}
		System.out.println("Mina Server run done! on port:" + bindPort);
	}
}