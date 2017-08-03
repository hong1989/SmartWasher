package com.sunrun.smartwasher.tcp.mina.codefactory;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sunrun.smartwasher.tcp.common.ProtocolConsts;
import com.sunrun.smartwasher.tcp.mina.entity.HeartBeat;
import com.sunrun.smartwasher.tcp.mina.entity.WashAnswer;

/** 
* @Package:com.sunrun.commserver.tcp.mina.codeFactory 
* @ClassName: ServerDecode 
* @Description: 该类为Mina通讯框架的数据解码类
* @author: HL 
* @date: 创建时间：2016年3月29日 下午2:37:40
* @version: 1.0 
*/ 
public class ServerDecode extends CumulativeProtocolDecoder {
	
	//logback打印日志，打印等级配置在logback.xml文件
	private static Logger logger = LoggerFactory.getLogger(ServerDecode.class); 

	@SuppressWarnings("unused")
	private final Charset charset;
	
	//初始化字符集为当前虚拟机默认的字符编码格式的CharSet
	public ServerDecode() {
		this(Charset.defaultCharset());
	}

	public ServerDecode(Charset charset) {
		this.charset = charset;
	}

	/**
	* 覆盖父类方法
	* Title: doDecode
	* Description: 数据解码方法，当客户端有数据发过来时，该方法会先调用对数据进行解码处理
	* @param session 这个接口用于表示Server端与Client端的连接句柄
	* @param in 客户端数据读入Buffer
	* @param out 将解码完的数据调用write方法转发出去，Mina框架下的IoHandler业务处理类中的messageReceived方法就会接受到
	* @return 返回true或false
	* @throws Exception  
	* @see org.apache.mina.filter.codec.CumulativeProtocolDecoder#doDecode(org.apache.mina.core.session.IoSession, org.apache.mina.core.buffer.IoBuffer, org.apache.mina.filter.codec.ProtocolDecoderOutput)
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		// TODO Auto-generated method stub
		try {		
			/*首先将接受的数据转换成 小端模式(网络数据传输一般为大端模式，而我们win系统下一般为小端模式)*/
			in.order(ByteOrder.LITTLE_ENDIAN); //ByteOrder.BIG_ENDIAN
			logger.debug("recv data="+in.getHexDump());
			/*数据包有效长度未满足最小值，结束本次数据包解码*/
			if(in.remaining() < ProtocolConsts.PACKAGE_MIN_LEN)
			{
				return false;
			}
			else 
			{
				/*doDecode进来，IoBuffer中存在的数据包总长度*/
				/*标记当前数据包buffer的位置，若后面数据不完整则直接通过in.reset()返回当前位置*/
				in.mark();
				byte[] header=new byte[ProtocolConsts.ProtocolField.HEADER.getLen()];
				in.get(header);
				/*获取有效数据包长度*/
				byte lenght=in.get();
				/*检验数据包头效验码、数据包长度是否合法有效*/
				if(Arrays.equals(header, ProtocolConsts.PACKET_HEADER) 
				  && (lenght == ProtocolConsts.PACKAGE_HEARTBEAT_LEN ||lenght == ProtocolConsts.PACKAGE_WASHRESP_LEN) )
				{
					in.reset(); // IoBuffer position回到原来标记的地方 
					/*未接受到一个完整的数据包，则不读取本次IoBuffer中数据*/
					if(in.remaining() < lenght)
					{
						return false;
					}
					else 
					{
						byte[] data=new byte[lenght];
						in.get(data);
						if(XORCheck(data))//数据异或校验
						{
							return packageBodyDecode(in,out,data);
						}
						else /*数据包效验码不通过，直接丢弃整个IoBuffer中数据*/
						{
							in.position(in.limit());
							return false;
						}
					}		
				}
				else /*数据包效验码不通过，直接丢弃整个IoBuffer中数据*/
				{
					in.position(in.limit());
					return false;
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("ServerDecode-doDecode", e);
			in.position(in.limit());
			return false;
		}
		
	}
	
	/**
	* @author: HL
	* @date: 2017年8月2日 下午3:39:28
	* @function: packageBodyDecode  
	* @Description: 数据包体解析
	* @param: @param in
	* @param: @param out
	* @param: @param data 数据
	* @param: @return
	* @return: boolean
	* @throws
	 */
	private boolean packageBodyDecode(IoBuffer in, ProtocolDecoderOutput out,byte[] data)
	{
		try {
			/*固定头*/
			byte[] header=new byte[ProtocolConsts.ProtocolField.HEADER.getLen()];
			System.arraycopy(data, ProtocolConsts.ProtocolField.HEADER.getPos(), header, 0,header.length);
			/*获取有效数据包长度*/
			byte length=data[ProtocolConsts.ProtocolField.PACKAGE_LEN.getPos()];
			/*获取设备ID*/
			byte [] deviceId=new byte[ProtocolConsts.ProtocolField.DEVICEID.getLen()];
			System.arraycopy(data, ProtocolConsts.ProtocolField.DEVICEID.getPos(), deviceId, 0,deviceId.length);
			/*获取消息类型*/
			byte msgType=data[ProtocolConsts.ProtocolField.MSGTYPE.getPos()];
			
			switch (msgType)
			{
				case ProtocolConsts.MSGTYPE_WASH_START://开始洗涤
				case ProtocolConsts.MSGTYPE_WASH_OVER://洗涤结束
					if(ProtocolConsts.PACKAGE_WASHRESP_LEN==length)//检测数据包是否合法
					{
						/*预留数据*/
						byte[] reserve=new byte[ProtocolConsts.ProtocolField.RESERVE.getLen()];
						System.arraycopy(data, ProtocolConsts.ProtocolField.RESERVE.getPos(), reserve, 0,reserve.length);
						/*效验码*/
						byte chkCode=data[ProtocolConsts.ProtocolField.WASHCHECKCODE.getPos()];
						WashAnswer washAnswer=new WashAnswer(header, length, deviceId, msgType, reserve, chkCode);
						out.write(washAnswer);
					}
					break;				
				case ProtocolConsts.MSGTYPE_HEARTBEAT://心跳包
					if(ProtocolConsts.PACKAGE_HEARTBEAT_LEN==length)//检测数据包长度是否合法
					{
						/*效验码*/
						byte chkCode=data[ProtocolConsts.ProtocolField.HEARTCHECKCODE.getPos()];
						HeartBeat heartBeat=new HeartBeat(header, length, deviceId, msgType, chkCode);
						out.write(heartBeat);
					}
					break;
				default:break;
			}	
			//判断buffer中是否还有足够的消息可以读取解析
			if(in.remaining() >= ProtocolConsts.PACKAGE_MIN_LEN)
				return true;
			else
				return false;	

		} catch (Exception e) {
			// TODO: handle exception
			logger.error("ServerDecode-packageBodyDecode", e);
			return false;
		}
	}
	
	/**
	* @author: HL
	* @date: 2017年8月2日 上午10:21:38
	* @function: XORCheck  
	* @Description: 对数据数据进行异或校验
	* @param: @param data
	* @param: @return 从固定头异或到预留数据，若和效验码一致，说明包有效，返回true，否则返回false
	* @return: boolean
	* @throws
	 */
	private boolean XORCheck(byte[] data)
	{
		boolean result=false;
		try 
		{
			byte xorValue=0x00;
			byte chkcode=data[data.length-1];
			for(int i=0;i<data.length-1;i++)
				xorValue=(byte) (data[i]^xorValue);
			if(xorValue==chkcode)
				result=true;
		} catch (Exception e) {
			// TODO: handle exception
			result=false;
		}
		return result;
	}
}
