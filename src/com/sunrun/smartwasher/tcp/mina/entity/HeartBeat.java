package com.sunrun.smartwasher.tcp.mina.entity;

import java.io.Serializable;

/** 
 * @Package: com.sunrun.tcp.mina.entity 
 * @ClassName: HeartBeat.java 
 * @Description:设备心跳包数据实体类
 * @author: HL 
 * @date: 创建时间：2017年6月1日 下午3:07:53
 * @version: 1.0 
 */
public class HeartBeat implements Serializable{

	/**  
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)  
	*/  
	private static final long serialVersionUID = 1L;
	/**
	 * 固定的同步头：0x53 4A
	 */
	private byte[] header;
	/**
	 * 整个数据包长度
	 */
	private byte length;
	/**
	 * 设备ID
	 */
	private byte[] deviceId;
	/**
	 * 数据包消息命令类型
	 */
	private byte msgType;
	/**
	 * 校验码
	 */
	private byte chkCode;
	
	public HeartBeat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HeartBeat(byte[] header, byte length, byte[] deviceId, byte msgType,
			byte chkCode) {
		super();
		this.header = header;
		this.length = length;
		this.deviceId = deviceId;
		this.msgType = msgType;
		this.chkCode = chkCode;
	}

	public byte[] getHeader() {
		return header;
	}

	public void setHeader(byte[] header) {
		this.header = header;
	}

	public byte getLength() {
		return length;
	}

	public void setLength(byte length) {
		this.length = length;
	}

	public byte[] getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(byte[] deviceId) {
		this.deviceId = deviceId;
	}

	public byte getMsgType() {
		return msgType;
	}

	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}

	public byte getChkCode() {
		return chkCode;
	}

	public void setChkCode(byte chkCode) {
		this.chkCode = chkCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
