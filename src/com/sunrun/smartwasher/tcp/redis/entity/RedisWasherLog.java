package com.sunrun.smartwasher.tcp.redis.entity;

import com.sunrun.smartwasher.tcp.redis.entity.base.RedisWasherLogBase;

/** 
 * @Package: com.sunrun.smartwasher.tcp.redis.entity 
 * @ClassName: RedisWasherLog.java 
 * @Description:REDIS实体层，主要是缓存设备日志数据
 * @author: HL 
 * @date: 创建时间：2017年08月02日 下午2:48:02
 * @version: 1.0 
 */
public class RedisWasherLog extends RedisWasherLogBase {
	
	/**  
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)  
	*/  
	private static final long serialVersionUID = 1L;

	public RedisWasherLog(String id, Integer type, String log, String time) {
		super(id, type, log, time);
		// TODO Auto-generated constructor stub
	}

}
