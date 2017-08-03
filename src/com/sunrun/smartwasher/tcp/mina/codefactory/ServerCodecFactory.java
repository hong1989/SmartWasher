package com.sunrun.smartwasher.tcp.mina.codefactory;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/** 
* @Package:com.sunrun.commserver.tcp.mina.codeFactory 
* @ClassName: ServerCodecFactory 
* @Description: 该类为Mina通讯框架的编解码工厂类，以便在过滤器中对数据进行编解码处理
* @author: HL 
* @date: 创建时间：2016年05月31日 下午2:36:40
* @version: 1.0 
*/ 
public class ServerCodecFactory implements ProtocolCodecFactory{
	
	private static final Charset charset = Charset.forName("UTF-8");
	/**
	* 覆盖父类方法
	* Title: getDecoder
	* Description: 向ProtocolCodecFilter过滤器返回数据解码类
	* @param session 这个接口用于表示Server端与Client端的连接句柄
	* @return 返回数据解码类
	* @throws Exception  
	* @see org.apache.mina.filter.codec.ProtocolCodecFactory#getDecoder(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return new ServerDecode(charset);
	}
	/**
	* 覆盖父类方法
	* Title: getEncoder
	* Description: 向ProtocolCodecFilter过滤器返回数据编码类
	* @param session 这个接口用于表示Server端与Client端的连接句柄
	* @return 返回数据编码类
	* @throws Exception  
	* @see org.apache.mina.filter.codec.ProtocolCodecFactory#getEncoder(org.apache.mina.core.session.IoSession)
	 */
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		return new ServerEncode(charset);
	}

}
