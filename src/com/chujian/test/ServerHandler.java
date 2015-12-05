package com.chujian.test;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.chujian.javabean.MessageOutlineBean;
import com.chujian.javabean.UserFriendOutlineBean;
import com.chujian.javabean.UserFriendOutlineYesNoBean;
import com.chujian.javabean.UserInfoBean;
import com.chujian.message.MessageTransport;
import com.chujian.minaUtil.HibernateSessionFactoryUtil;
import com.chujian.minaUtil.SessionManager;
import com.chujian.minaUtil.Util;
import com.chujian.tools.json.JsonObject;

public class ServerHandler extends IoHandlerAdapter {

	private int count = 0;

	private SessionManager mSessionManager = SessionManager.getInstace();

	private HashMap<String, IoSession> sessionMap = new HashMap<String, IoSession>();

	private MessageTransport msTrans;

	private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 200,
			TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

	private int msgTypeLogin = 0; //
	private int msgTypeLocation = 1;
	private int msgTypeAddFriend = 2;
	private int msgTypeDeleteFriend = 3;
	private int msgTypeSearch = 4;

	private byte b1 = 0x01;
	private byte b2 = 0x02;

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);

		System.out.println("新用户连接");

	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionOpened(session);

		System.out.println("第" + count + "用户连接,address:"
				+ session.getRemoteAddress());

		count++;

	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		super.messageReceived(session, message);

		System.out.println(session.getRemoteAddress().toString() + "接收到客户端消息");

		ChujianPotocol mt = (ChujianPotocol) message;
		System.out.println(mt.toString());
		System.out.println(mt.getData());

		JsonObject js = JsonObject.parseObject(mt.getData());

		// 根据传递的消息类型处理

		switch (mt.getTagRes()) {

		case 1:// 处理请求

			System.out.println("处理请求");

			switch (mt.getDataType()) {

			case 1:// 处理消息类型,也是一json字符串
					// 格式为：{"fromId":"","toId":"","content":""}

				System.out.println("处理消息字符串");

				JsonObject msjson = JsonObject.parseObject(mt.getData());

				String fromId = msjson.getString("fromId");

				String toId = msjson.getString("toId");

				String content = msjson.getString("content");

				IoSession transSession = sessionMap.get(toId);

				if (transSession == null) {

					MessageOutlineBean mOutBean = new MessageOutlineBean();

					mOutBean.setFromId(fromId);
					mOutBean.setToId(toId);
					mOutBean.setContent(content);
					mOutBean.setTime(Util.getSysTime());

					Session sessionToSaveMessage = HibernateSessionFactoryUtil
							.currentSessionFactory().openSession();

					Transaction tx = null;

					try {

						tx = sessionToSaveMessage.beginTransaction();

						sessionToSaveMessage.save(mOutBean);

						tx.commit();

					} catch (Exception e) {

						e.printStackTrace();
						tx.rollback();

					} finally {
						if (sessionToSaveMessage != null) {
							sessionToSaveMessage.close();
						}

						HibernateSessionFactoryUtil.currentSessionFactory()
								.close();
					}

					System.out.println("未在线");
					return;
				}

				ChujianPotocol transCJ = new ChujianPotocol();

				transCJ.setTagRes(b2);
				transCJ.setDataType(b1);
				transCJ.setLength(mt.getLength());
				transCJ.setData(mt.getData());

				if (transSession.isConnected()) {
					transSession.write(transCJ);
				} else {
					System.out.println("session 未打开");
				}

				break;

			case 2:// 处理JSON类型,第一个键值对为json的类型 格式为:{"msgType":"",……}

				int typeMsg = (int) js.getNum("msgType");

				switch (typeMsg) {

				case 0:

					String UserPhone = js.getString("userPhone");

					// 数据库操作

					System.out.println("UserPhone:" + UserPhone);

					if (UserPhone.trim().equals("18292880826")
							|| UserPhone.trim().equals("18292880820")) {

						// -----------

						// SessionFactory sessionFactory = null;
						// Configuration configuration = new Configuration()
						// .configure();
						//
						// ServiceRegistry serviceRegistry = new
						// ServiceRegistryBuilder()
						// .applySettings(configuration.getProperties())
						// .buildServiceRegistry();
						//
						// sessionFactory = configuration
						// .buildSessionFactory(serviceRegistry);

						Session dbSession = HibernateSessionFactoryUtil
								.currentSessionFactory().openSession();

						Transaction tx = null;
						try {

							tx = dbSession.beginTransaction();

							UserInfoBean user = new UserInfoBean();

							user.setUserPhone(UserPhone);
							user.setUserPwd(js.getString("userPwd"));

							if (user.getUserBirthday() == null) {
								user.setUserBirthday("2015-11-4");
							}
							if (user.getUserNickname() == null) {
								user.setUserNickname("初见");
							}
							if (user.getUserSex() == null) {
								user.setUserSex("boy");
							}

							dbSession.save(user);

							tx.commit();

						} catch (Exception e) {
							e.printStackTrace();
							tx.rollback();
						} finally {

							if (dbSession != null) {
								if (dbSession.isOpen()) {
									dbSession.close();
								}
							}

							HibernateSessionFactoryUtil.currentSessionFactory()
									.close();

						}
						// -----------------

						sessionMap.put(UserPhone, session);

						session.setAttribute("UserPhone", UserPhone);

						System.out.println("登录成功");

						ChujianPotocol loginSucess = new ChujianPotocol();

						byte tagByte = 0x02;// 相应数据
						byte jsonByte = 0x02;// json格式

						JsonObject dataJson = new JsonObject();

						dataJson.put("msgType", "0");
						dataJson.put("result", "true");

						String dataStr = dataJson.toJsonString();

						loginSucess.setTagRes(tagByte);
						loginSucess.setDataType(jsonByte);

						loginSucess.setLength(String.valueOf(dataStr
								.getBytes(Charset.forName("UTF-8")).length));

						loginSucess.setData(dataStr);

						System.out.println("loginSucess.toString()"
								+ loginSucess.toString());

						session.write(loginSucess);
					} else {
						System.out.println("一个客户端登录失败");
					}

					break;
				case 1:// 添加好友请求

					String fromUserPhone = js.getString("fromUser");
					String toUserPhone = js.getString("toUser");

					IoSession addFriendSession = sessionMap.get(toUserPhone);

					if (addFriendSession == null) {

						UserFriendOutlineBean mOutFriendBean = new UserFriendOutlineBean();

						mOutFriendBean.setUserPhone(fromUserPhone);
						mOutFriendBean.setFriendPhone(toUserPhone);

						Session sessionToSaveFriendAsk = HibernateSessionFactoryUtil
								.currentSessionFactory().openSession();

						Transaction tx = null;

						try {

							tx = sessionToSaveFriendAsk.beginTransaction();

							sessionToSaveFriendAsk.save(mOutFriendBean);

							tx.commit();

						} catch (Exception e) {

							e.printStackTrace();
							tx.rollback();

						} finally {
							if (sessionToSaveFriendAsk != null) {
								sessionToSaveFriendAsk.close();
							}

							HibernateSessionFactoryUtil.currentSessionFactory()
									.close();
						}

						System.out.println("未在线");
						return;
					}

					ChujianPotocol transFriendAskCJ = new ChujianPotocol();

					transFriendAskCJ.setTagRes(b2);
					transFriendAskCJ.setDataType(b2);

					JsonObject addFriendAsk = new JsonObject();
					addFriendAsk.put("msgType", "1");
					addFriendAsk.put("fromUser", fromUserPhone);
					addFriendAsk.put("toUser", toUserPhone);

					transFriendAskCJ
							.setLength(String.valueOf(addFriendAsk
									.toJsonString().getBytes(
											Charset.forName("UTF-8")).length));
					transFriendAskCJ.setData(addFriendAsk.toJsonString());

					if (addFriendSession.isConnected()) {
						addFriendSession.write(transFriendAskCJ);
					} else {
						System.out.println("session 未打开");
					}

					break;
				case 2:// 确认添加好友 发送者为系统提醒

					String fromUserPhoneIs = js.getString("fromUser");
					String toUserPhoneIs = js.getString("toUser");
					String flagAddFriYesNo = js.getString("flag");

					IoSession modifyFriendSession = sessionMap.get(toUserPhone);

					if (modifyFriendSession == null) {

						UserFriendOutlineYesNoBean mOutFriendYesNoBean = new UserFriendOutlineYesNoBean();

						mOutFriendYesNoBean.setUserPhone(fromUserPhone);
						mOutFriendYesNoBean.setFriendPhone(toUserPhone);
						mOutFriendYesNoBean.setFlag(flagAddFriYesNo);

						Session sessionToSaveFriendYesNo = HibernateSessionFactoryUtil
								.currentSessionFactory().openSession();

						Transaction tx = null;

						try {

							tx = sessionToSaveFriendYesNo.beginTransaction();

							sessionToSaveFriendYesNo.save(mOutFriendYesNoBean);

							tx.commit();

						} catch (Exception e) {

							e.printStackTrace();
							tx.rollback();

						} finally {
							if (sessionToSaveFriendYesNo != null) {
								sessionToSaveFriendYesNo.close();
							}

							HibernateSessionFactoryUtil.currentSessionFactory()
									.close();
						}

						System.out.println("未在线");
						return;
					}

					ChujianPotocol transFriendAskYesNo = new ChujianPotocol();

					transFriendAskYesNo.setTagRes(b2);
					transFriendAskYesNo.setDataType(b2);

					JsonObject addFriendAsk = new JsonObject();
					addFriendAsk.put("msgType", "1");
					addFriendAsk.put("fromUser", fromUserPhone);
					addFriendAsk.put("toUser", toUserPhone);

					transFriendAskCJ
							.setLength(String.valueOf(addFriendAsk
									.toJsonString().getBytes(
											Charset.forName("UTF-8")).length));
					transFriendAskCJ.setData(addFriendAsk.toJsonString());

					if (addFriendSession.isConnected()) {
						addFriendSession.write(transFriendAskCJ);
					} else {
						System.out.println("session 未打开");
					}

					break;
				case 3:
					break;
				case 4:
					break;

				}

				break;
			default:
				break;

			}

			break;
		case 2:// 处理相应

			break;

		default:
			break;

		}

		// msTrans = MessageTransport.parseString(mt.getData());// 线程安全
		//
		// mSessionManager.addSession(msTrans.getFromId(), session);
		//
		// threadPool.execute(new DispMessage());

		// session.write(mt);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);

		System.out.println("消息已经传送到客户端");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);

		System.out.println("一个客户端已经关闭");

		sessionMap.remove((String) session.getAttribute("UserPhone"));

		System.out.println("sessionMap.size()" + sessionMap.size());

	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		super.sessionIdle(session, status);

		System.out.println("连接空闲");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);

		System.out.println("其他方法抛出异常");

	}

}
