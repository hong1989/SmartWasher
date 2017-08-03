package com.sunrun.smartwasher.tcp.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** 
 * @Package: com.sunrun.bing.common.util 
 * @ClassName: DataUtils.java 
 * @Description:
 * @author: HL 
 * @date: 创建时间：2016年6月15日 上午9:01:59
 * @version: 1.0 
 */
public class DataUtils {
	
	private static Logger logger = LoggerFactory.getLogger(DataUtils.class);  
	/**
	* @author: HL
	* @date: 2016年6月22日 上午11:05:46
	* @function: str2HexStr  
	* @Description: 将字符转成ASCII码对应的16进制表示 如：字符‘A’->41(0x41=65)
	* @param: @param str
	* @param: @return
	* @return: 返回16进制字符串
	* @throws
	 */
	public static String str2HexStr(String str) {    
        char[] chars = "0123456789ABCDEF".toCharArray();    
        StringBuilder sb = new StringBuilder("");  
        byte[] bs = str.getBytes();    
        int bit;    
        for (int i = 0; i < bs.length; i++) {    
            bit = (bs[i] & 0x0f0) >> 4;    
            sb.append(chars[bit]);    
            bit = bs[i] & 0x0f;    
            sb.append(chars[bit]);    
        }    
        return sb.toString();    
    }

	/**
	* @author: HL
	* @date: 2016年6月22日 上午11:06:02
	* @function: bytesToHexString  
	* @Description:  将byte数组转成16进制字符串表示形式，每个16进制之间加‘-’连接
	* @param: @param switchMac
	* @param: @param type 0-大端模式  高位在前，低位在后 1-小端模式 低位在前，高位在后
	* @param: @return
	* @return: 返回16进制String
	* @throws
	 */
	 public static String bytesToHexString(byte[] switchMac,int type) 
	 {
			StringBuilder stringBuilder = new StringBuilder();
			if (switchMac == null || switchMac.length <= 0) {
				return null;
			}
			if(0==type)
			{
				for (int i = 0; i < switchMac.length; i++) {
					int v = switchMac[i] & 0xFF;
					String hv = Integer.toHexString(v);
					if (hv.length() < 2) {
						stringBuilder.append(0);
					}
					stringBuilder.append(hv);
				}
			}
			else {
				for (int i = switchMac.length-1; i >=0; i--) {
					int v = switchMac[i] & 0xFF;
					String hv = Integer.toHexString(v);
					if (hv.length() < 2) {
						stringBuilder.append(0);
					}
					stringBuilder.append(hv);	
				}
			}
			return stringBuilder.toString().toUpperCase();
	}
	 
	 /**
	 * @author: HL
	 * @date: 2016年12月6日 上午10:11:53
	 * @function: getDevMarkHexString  
	 * @Description: 将设备序列号转成16进制字符显示
	 * @param: @param switchMark 设备唯一标示符字节数组,6字节长度
	 * @param: @param endpos switchMark数组最后一个有效位数据
	 * @param: @return
	 * @return: String
	 * @throws
	  */
	 public static String getDevMarkHexString(byte[] switchMark,int endpos)
	 {
	 	StringBuilder stringBuilder = new StringBuilder();
		if (switchMark == null || switchMark.length <= 0) {
			return null;
		}
		for (int i = endpos; i >=0; i--) {
			int v = switchMark[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);	
		}
		return stringBuilder.toString().toUpperCase();
	 }
	 /**
	 * @author: HL
	 * @date: 2016年6月22日 上午11:06:23
	 * @function: input2byte  
	 * @Description:  将InputStream对象转成byte数组
	 * @param: @param inStream
	 * @param: @return
	 * @param: @throws IOException
	 * @return: 返回byte数据
	 * @throws
	  */
	 public static final byte[] input2byte(InputStream inStream)  
	            throws IOException {  
	        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
	        byte[] buff = new byte[100];  
	        int rc = 0;  
	        while ((rc = inStream.read(buff, 0, 100)) > 0) {  
	            swapStream.write(buff, 0, rc);  
	        }  
	        byte[] in2b = swapStream.toByteArray();  
	        return in2b;  
	    }  
	 
	 /**
	 * @author: HL
	 * @date: 2016年7月7日 下午3:25:01
	 * @function: utc2LocalTime  
	 * @Description: 用于将GPS模块的UTC时间戳转成东八区本地北京时间
	 * @param: @param utcTime  GPS定位模块传过来的UTC时间
	 * @param: @param utcTimePatten UTC时间格式
	 * @param: @param localTimePatten  返回的本地北京时间格式
	 * @return: 返回本地东八区北京时间 Date对象 
	 * @throws
	  */
	 public static Date utc2LocalTime(String utcTime, String utcTimePatten,String localTimePatten) 
	 {
  		  SimpleDateFormat utcFormater = new SimpleDateFormat(utcTimePatten);
  		  utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
  		  Date gpsUTCDate = null;
  		  Date localGMTDate=null;
  		  try 
  		  {
  		   gpsUTCDate = utcFormater.parse(utcTime);
  		   SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
 		   localFormater.setTimeZone(TimeZone.getDefault());
 		   String localTime = localFormater.format(gpsUTCDate.getTime());
 		   localGMTDate=localFormater.parse(localTime);
  		   
  		  } catch (ParseException e) {
  		    e.printStackTrace();
  		  }  		 
  		  return localGMTDate;
  	 }
	 
	 public static String Date2LocalTime(Date date,String localTimePatten)
	 {
		 SimpleDateFormat localFormater = new SimpleDateFormat(localTimePatten);
		 String localTime = localFormater.format(date.getTime());
 		 return localTime;
	 }
	 
	 /**
	 * @author: HL
	 * @date: 2017年6月16日 下午2:21:50
	 * @function: isRegularRptCode  
	 * @Description: TODO(这里用一句话描述这个方法的作用)  
	 * @param: @param regEx 正则表达式
	 * @param: @param rptCode  需要验证的字符串
	 * @param: @return
	 * @return: boolean 格式正确返回true 否则返回false
	 * @throws
	  */
	 public static boolean isRegularRptCode(String regEx,String rptCode)
	{
		Pattern pattern=Pattern.compile(regEx);
		Matcher matcher=pattern.matcher(rptCode);
		return matcher.matches();	
	}
	 
	 /**
	 * @author: HL
	 * @date: 2016年6月22日 上午11:06:53
	 * @function: printByteToHexStr  
	 * @Description:  将byte数组按照16进制形式打印
	 * @param: @param data
	 * @return: void
	 * @throws
	  */
	 public static void printByteToHexStr(byte [] data)
	 {
		 for(int i=0;i<data.length;i++)
		{
			int v = data[i] & 0xFF;
			String hex=Integer.toHexString(v);
			if(hex.length()==1)
				hex="0x0"+hex;
			else {
				hex="0x"+hex;
			}
			logger.debug(hex+" ");
		}
		 logger.debug("\n");
	 } 
	 
	 /**
	 * @author: HL
	 * @date: 2016年11月24日 上午11:11:33
	 * @function: intToBytes  
	 * @Description: 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
	 * @param: @param res 要转换的int值
	 * @param: @return
	 * @return: byte[] 返回byute数组
	 * @throws
	  */
	 public static byte[] intToBytes(int res) {  
		 byte[] targets = new byte[4];  
		   
		 targets[0] = (byte) (res & 0xff);// 最低位   
		 targets[1] = (byte) ((res >> 8) & 0xff);// 次低位   
		 targets[2] = (byte) ((res >> 16) & 0xff);// 次高位   
		 targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。   
		 return targets;   
	}   
	 
	 /**
	 * @author: HL
	 * @date: 2016年11月24日 上午11:12:46
	 * @function: bytesToInt  
	 * @Description: TODO(这里用一句话描述这个方法的作用)  
	 * @param: @param res 要转换的byte数组
	 * @param: @return
	 * @return: int 返回int值
	 * @throws
	  */
	 public static Integer bytesToInt(byte[] res) {   
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000   
		  
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或   
		| ((res[2] << 24) >>> 8) | (res[3] << 24);   
		return targets;   
	}   
	
}
