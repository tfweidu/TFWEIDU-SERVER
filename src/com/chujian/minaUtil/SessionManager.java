package com.chujian.minaUtil;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

public class SessionManager {

	private static SessionManager mSessionManager;

	private HashMap<String, IoSession> sessionMap = new HashMap<String, IoSession>();

	/**
	 * @return ��ʼ��
	 */
	public static SessionManager getInstace() {
		mSessionManager = new SessionManager();

		return mSessionManager;
	}

	/**
	 * @return
	 * 
	 * ��ȡSessionmap
	 * 
	 */
	public HashMap<String, IoSession> getSessionMap() {

		return sessionMap;

	}

	/**
	 * @param strIp
	 * @param mIoSession
	 * 
	 *            ����session
	 */
	public void addSession(String strIp, IoSession mIoSession) {

		sessionMap.put(strIp, mIoSession);

	}

	/**
	 * @param strIp
	 *            ɾ����Ӧ��session
	 * 
	 */
	public void removeSession(String strIp) {

		sessionMap.remove(strIp);

	}

	/**
	 * ���session
	 */
	public void clearSession() {

		sessionMap.clear();

	}

	/**
	 * @param strIP
	 * @return ��ȡSession
	 * 
	 */
	public IoSession getSessionByIP(String strIP) {

		return sessionMap.get(strIP);

	}

}
