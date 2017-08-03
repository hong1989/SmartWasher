package com.sunrun.smartwasher.tcp.redis.entity.base;

import java.io.Serializable;

/** 
 * @Package: com.sunrun.smartwasher.tcp.redis.entity.base 
 * @ClassName: RedisWasherLogBase.java 
 * @Description:REDIS实体层，主要是缓存设备日志数据
 * @author: HL 
 * @date: 创建时间：2017年08月02日 下午2:44:46
 * @version: 1.0 
 */
public abstract class RedisWasherLogBase implements Serializable{

	/**  
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)  
	*/  
	private static final long serialVersionUID = 1L;
	/**
	 * 设备ID
	 */
	private String id;
	/**
	 * 设备日志类型：0-离线 1-上线
	 */
	private Integer type;
	/**
	 * 日志
	 */
	private String log;
	/**
	 * 日志时间
	 */
	private String time;
	
	public RedisWasherLogBase(String id, Integer type, String log, String time) {
		super();
		this.id = id;
		this.type = type;
		this.log = log;
		this.time = time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
