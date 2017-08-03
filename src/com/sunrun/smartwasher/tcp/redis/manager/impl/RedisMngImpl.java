package com.sunrun.smartwasher.tcp.redis.manager.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sunrun.smartwasher.tcp.redis.dao.RedisDao;
import com.sunrun.smartwasher.tcp.redis.manager.RedisMng;

/** 
 * @Package: com.sunrun.tcp.redis.manager.impl 
 * @ClassName: RedisMngImpl.java 
 * @Description:Redis数据业务层，从Redis提取数据存到db
 * @author: HL 
 * @date: 创建时间：2017年4月28日 下午3:39:42
 * @version: 1.0 
 */
@Service
public class RedisMngImpl implements RedisMng {

	//logback打印日志，打印等级配置在logback.xml文件
	private static Logger logger = LoggerFactory.getLogger(RedisMngImpl.class); 
	
	//redis dao层
	@Autowired
	private RedisDao redisdao;
	
}
