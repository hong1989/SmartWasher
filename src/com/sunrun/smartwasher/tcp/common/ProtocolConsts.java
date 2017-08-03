package com.sunrun.smartwasher.tcp.common;
/** 
 * @Package: com.sunrun.tcp.common.constants 
 * @ClassName: ProtocolConsts.java 
 * @Description:设备与服务器之间通讯消息协议字段常量类
 * @author: HL 
 * @date: 创建时间：2017年5月31日 下午1:38:47
 * @version: 1.0 
 */
public class ProtocolConsts {

	
	/*数据消息类型常量定义*/
	/**
	 * 开始洗涤
	 */
	public final static byte MSGTYPE_WASH_START=0x01;
	/**
	 * 洗涤完成
	 */
	public final static byte MSGTYPE_WASH_OVER=0x02;
	/**
	 * 心跳包消息类型
	 */
	public final static byte MSGTYPE_HEARTBEAT=0x0F;
	
	/*数据消息类型对应数据包长度常量定义*/
	/**
	 * 数据包最小长度
	 */
	public final static int PACKAGE_MIN_LEN=0x0B;
	/**
	 * 心跳包长度
	 */
	public final static int PACKAGE_HEARTBEAT_LEN=0x0B;
	/**
	 *开始洗涤数据包长度
	 */
	public final static int PACKAGE_WASHRESP_LEN=0x0D;
	
	 /**
     * 本地东八区北京时间时间戳格式
     */
    public static final String LOCAL_DATE_PATTEN="yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 固定头
	 */
	public static final byte[] PACKET_HEADER = { (byte) 0x53, (byte) 0x4A};
	
	/**
	* @ClassName: ProtocolField  
	* @Description: 协议字段在数据包中位置常量枚举类  
	* @author HL  
	* @date 2017年05月31日 下午1:43:33  
	*/
	public static enum ProtocolField {
		
		HEADER(0,2),/*协议数据包同步头0x53 4A开始位和长度*/
		PACKAGE_LEN(2,1),/*数据包长度字节在整个数据包中开始位和长度*/
		DEVICEID(3,6),/*设备ID*/
		MSGTYPE(9,1),/*消息指令字节在整个数据包中开始位和长度*/
		RESERVE(10,2),/*预留位*/
		WASHCHECKCODE(12,1),/*洗涤数据包校验码*/
		HEARTCHECKCODE(10,1);/*心跳包校验码*/
		/**
		 * 协议字段在整个数据包中的开始位置
		 */
		private int pos;
		/**
		 * 协议字段数据的长度
		 */
		private int len;

		private ProtocolField(int pos, int len) {
			this.pos = pos;
			this.len = len;
		}

		public int getPos() {
			return pos;
		}

		public int getLen() {
			return len;
		}
		
	}
	
}
