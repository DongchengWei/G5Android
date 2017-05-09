package com.otherutils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

	/*
	 * 获取配置信息
	 * 返回map
	 * */
	//"a.properties"
	@SuppressWarnings("unchecked")
	public Map<String, String> getProperties(String pathStr) {
		Properties prop = new Properties();
		try {
          //读取属性文件a.properties
         InputStream in = new BufferedInputStream (new FileInputStream(pathStr));
         prop.load(in);     //加载属性列表
         @SuppressWarnings("rawtypes")
         Map<String, String> mapProperties = new HashMap<String, String>((Map) prop);
         in.close();
         return mapProperties;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	
	public void setProperties(String pathStr, String keyStr, String valueStr, boolean isAdd) {
		Properties prop = new Properties();
		try {
          ///保存属性到b.properties文件
          FileOutputStream oFile = new FileOutputStream(pathStr, isAdd);//true表示追加打开
          prop.setProperty(keyStr, valueStr);
          prop.store(oFile, "The New properties file");
          oFile.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
