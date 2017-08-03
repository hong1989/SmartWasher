package com.sunrun.smartwasher.tcp.mina.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sunrun.smartwasher.tcp.common.DataUtils;
import com.sunrun.smartwasher.tcp.mina.entity.HeartBeat;
import com.sunrun.smartwasher.tcp.mina.entity.WashAnswer;

/** 
* @Package:com.sunrun.commserver.tcp.mina.handler 
* @ClassName: ServerHandler 
* @Description: 该类为Mina通讯框架的IoHandler业务逻辑处理派生类，主要负责数据的收发，通道创建、打开、空闲和关闭事件驱动响应,多线程
* @author: HL 
* @date: 创建时间：2017年05月31日 下午2:39:40
* @version: 1.0 
*/ 
public class ServerHandler extends IoHandlerAdapter {
	
	
	//logback打印日志，打印等级配置在logback.xml文件
	private static Logger logger = LoggerFactory.getLogger(ServerHandler.class); 
	

	/**
	 * 设备ID和通道对应关系
	 */
	public  static  ConcurrentMap<String, IoSession> deviceIoMap = new ConcurrentHashMap<String, IoSession>();
	
	/**
	* 覆盖父类方法
	* Title: sessionCreated
	* Description: 这个方法当一个Session 对象被创建的时候被调用。对于TCP连接来说，连接被接受的时候调用
	* @param session 这个接口用于表示Server端与Client端的连接句柄，可以通过write方法给客户端发送数据
	* @throws Exception  
	* @see org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache.mina.core.session.IoSession)
	*/
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}
	
	/**
	* 覆盖父类方法
	* Title: sessionOpened
	* Description: 这个方法在连接被打开时调用，它总是在sessionCreated()方法之后被调用。对于TCP来说，它是在连接被建立之后调用，你可以在这里执行一些认证操作、发送数据等。
	* @param session 这个接口用于表示Server端与Client端的连接句柄，可以通过write方法给客户端发送数据
	* @throws Exception  
	* @see org.apache.mina.core.service.IoHandlerAdapter#sessionOpened(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		//super.sessionOpened(session);
	}
	
	/**
	* 覆盖父类方法
	* Title: messageReceived
	* Description: 接收到消息时调用的方法。
	* @param session 这个接口用于表示Server端与Client端的连接句柄，可以通过write方法给客户端发送数据
	* @param message 一般情况下，message 是一个IoBuffer 类，如果使用了协议编解码器，那么可以强制转换为需要的类型。
	* @throws Exception  
	* @see org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache.mina.core.session.IoSession, java.lang.Object)
	 */
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		// TODO Auto-generated method stub
		//super.messageReceived(session, message);
	    if(message instanceof HeartBeat){//心跳包，20秒内无任何数据上来，才会发送
			//暂时不需要处理，直接忽略
	    	logger.debug("心跳包");	
	    	HeartBeat heartBeat=(HeartBeat)message;
			//session.write(heartBeat);
			String sn=DataUtils.bytesToHexString(heartBeat.getDeviceId(),1);
			checkDeviceOnline(sn,session);
		}
		else if(message instanceof WashAnswer){//RFID数据
			logger.info("洗涤响应数据");
			WashAnswer washAnswer=(WashAnswer)message;
			//add push code
		}
	}

	/**
	* 覆盖父类方法
	* Title: sessionIdle
	* Description: 这个方法在IoSession的读和写某个通道进入空闲状态时调用
	* @param session 这个接口用于表示Server端与Client端的连接句柄
	* @param status 标示读通道和写通道是否出现空闲
	* @throws Exception  
	* @see org.apache.mina.core.service.IoHandlerAdapter#sessionIdle(org.apache.mina.core.session.IoSession, org.apache.mina.core.session.IdleStatus)
	 */
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		//super.sessionIdle(session, status);
		session.close(true);//超出空闲等待时间，关闭session通道
	}
	
	/**
	* 覆盖父类方法
	* Title: sessionClosed
	* Description: 对于TCP来说，连接被关闭时，调用这个方法。
	* @param session 这个接口用于表示Server端与Client端的连接句柄
	* @throws Exception   
	* @see org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		//super.sessionClosed(session);
		//设备离线，将设备ID对应通道从MAP表移除
		synchronized(ServerHandler.deviceIoMap)
		{
			Iterator<Map.Entry<String,IoSession>> ite_deviceiomap = deviceIoMap.entrySet().iterator();
	 		while (ite_deviceiomap.hasNext()) {
	 			Map.Entry<String,IoSession> mapentry_deviceiomap =ite_deviceiomap.next();
	 			String sn =  mapentry_deviceiomap.getKey().toString();
	 			String value = mapentry_deviceiomap.getValue().toString();
	 			if(value.equals(session.toString())){
	 				deviceIoMap.remove(sn);
	 				//RedisOnline redisOnline=new RedisOnline(sn,(int)ProtocolConsts.DEVICE_OFFLINE);//设备离线
	 				//redisMng.pushOnlineList(redisOnline);
	 				//String time=new SimpleDateFormat(ProtocolConsts.LOCAL_DATE_PATTEN).format(new Date());//时间
	 				//RedisGatewayLog redisGatewayLog=new RedisGatewayLog(sn, (int)ProtocolConsts.DEVICE_OFFLINE, "设备离线", time);
	 				//redisMng.pushGatewayLogList(redisGatewayLog);
	 				break;
	 			}
	 		}  
		}	
	}
	
	/**
	* 覆盖父类方法
	* Title: exceptionCaught
	* Description: 这个方法在Mina通讯 自身出现异常时回调，一般这里是关闭IoSession。
	* @param session 这个接口用于表示Server端与Client端的连接句柄
	* @param cause
	* @throws Exception  
	* @see org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache.mina.core.session.IoSession, java.lang.Throwable)
	 */
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		//super.exceptionCaught(session, cause);
		logger.error("ServerHandler-exceptionCaught", cause);
	}
	
	/**
	* @author: HL
	* @date: 2017年6月15日 上午8:42:33
	* @function: checkDeviceOnline  
	* @Description: 检测设备之前否已经上线，若刚上线，则加入MAP
	* @param: @param devicemark 设备描述符
	* @param: @param session 设备ID所对应的通道
	* @return: void
	* @throws
	 */
	private   void checkDeviceOnline(String sn,IoSession session)
	{
		synchronized (ServerHandler.deviceIoMap) {
			
			IoSession ss=ServerHandler.getDeviceIoMap().get(sn);
			if(null==ss||!ss.isConnected()||!ss.toString().equals(session.toString()))
			{  
				logger.info(session.toString());
				ServerHandler.getDeviceIoMap().put(sn, session);//关联设备ID和通道
				//RedisOnline redisOnline=new RedisOnline(sn,(int)ProtocolConsts.DEVICE_ONLINE);//设备在线
				//redisMng.pushOnlineList(redisOnline);
				//String time=new SimpleDateFormat(ProtocolConsts.LOCAL_DATE_PATTEN).format(new Date());//时间
				//RedisGatewayLog redisGatewayLog=new RedisGatewayLog(sn, (int)ProtocolConsts.DEVICE_ONLINE, "设备上线", time);
				//redisMng.pushGatewayLogList(redisGatewayLog);
			}	
		}
	}
	
	public static  ConcurrentMap<String, IoSession> getDeviceIoMap() {
		return deviceIoMap;
	}

	public static  void setDeviceIoMap(ConcurrentMap<String, IoSession> deviceIoMap) {
		ServerHandler.deviceIoMap = deviceIoMap;
	}
	
}
