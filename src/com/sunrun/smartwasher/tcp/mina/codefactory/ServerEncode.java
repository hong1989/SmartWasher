package com.sunrun.smartwasher.tcp.mina.codefactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunrun.smartwasher.tcp.mina.entity.WashOrder;

/** 
* @Package:com.sunrun.commserver.tcp.mina.codeFactory 
* @ClassName: ServerEncode 
* @Description: 该类为Mina通讯框架的数据编码类
* @author: HL 
* @date: 创建时间：2016年3月29日 下午2:38:40
* @version: 1.0 
*/ 
public class ServerEncode extends ProtocolEncoderAdapter {

	//logback打印日志，打印等级配置在logback.xml文件
	private static Logger logger = LoggerFactory.getLogger(ServerEncode.class); 
	
	@SuppressWarnings("unused")
	private Charset charset = null;

	public ServerEncode(Charset charset) {
		this.charset=charset;
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception {
		IoBuffer responseBuf = IoBuffer.allocate(100).setAutoExpand(true);
		responseBuf.order(ByteOrder.LITTLE_ENDIAN);// 修改此缓冲区的字节顺序,小端模式
		// TODO Auto-generated method stub
		if(message instanceof WashOrder)//下发洗涤指令
		{
			WashOrder washOrder=(WashOrder)message;
			responseBuf.put(washOrder.getHeader());
			responseBuf.put(washOrder.getLength());
			responseBuf.put(washOrder.getDeviceId());
			responseBuf.put(washOrder.getMsgType());
			responseBuf.put(washOrder.getChkCode());
		}
	
		responseBuf.flip();
		logger.debug("send data="+responseBuf.getHexDump());
	    out.write(responseBuf);
	    out.flush();
	    responseBuf.free();
	}

}
