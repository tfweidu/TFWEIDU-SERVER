package com.chujian.minaUtil;

import java.util.HashMap;

import org.apache.mina.core.session.IoSession;

public class SessionManager {

	private static SessionManager mSessionManager;

	private HashMap<String, IoSession> sessionMap = new HashMap<String, IoSession>();

	/**
	 * @return 初始化
	 */
	public static SessionManager getInstace() {
		mSessionManager = new SessionManager();

		return mSessionManager;
	}

	/**
	 * @return
	 * 
	 * 获取Sessionmap
	 * 
	 */
	public HashMap<String, IoSession> getSessionMap() {

		return sessionMap;

	}

	/**
	 * @param strIp
	 * @param mIoSession
	 * 
	 *            增加session
	 */
	public void addSession(String strIp, IoSession mIoSession) {

		sessionMap.put(strIp, mIoSession);

	}

	/**
	 * @param strIp
	 *            删除对应的session
	 * 
	 */
	public void removeSession(String strIp) {

		sessionMap.remove(strIp);

	}

	/**
	 * 清除session
	 */
	public void clearSession() {

		sessionMap.clear();

	}

	/**
	 * @param strIP
	 * @return 获取Session
	 * 
	 */
	public IoSession getSessionByIP(String strIP) {

		return sessionMap.get(strIP);

	}

}
