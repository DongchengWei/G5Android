package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.LogUtil;
import com.otherutils.Utils;
import com.pageutil.BtTabPage;
import com.pageutil.HomePage;
import com.pageutil.SettingsPage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;


/* 
 * case: 蓝牙连接断开   100次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.BtConnectDisconnect
 * 前提：蓝牙已匹配并连接过一次，手机端允许读短信和联系人权限，
 * 		当前已匹配列表只有一个设备,当前未连接任何协议
 * 步骤：进入蓝牙设置页，点击该设备，连接成功点击该蓝牙设备，断开蓝牙连接
 * 期望：能正常断开重连 100次
 * 其他：设置蓝牙名称和测试次数,每个-e对应一个参数
 * uiautomator runtest AutoTest.jar -e DeviceName Nexus -e TestTimes 200 -c com.bt.BtConnectDisconnect
 * */
public class BtConnectDisconnect extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.BtConnectDisconnect", "", "3");  
		runTestCase.setDebug(false);  
		String logPathStr = runTestCase.runUiautomator();//返回文件路径
		new LogUtil().analyseLog(logPathStr);//分析日志
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:BT connect and disconnect============");
		System.out.println("==================================================");
	}
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, btConnectDisconnectTest());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//
	private boolean btConnectDisconnectTest() {
		
		boolean isTestPass = false;
		try {
			
			//获取测试设备名称
			String deviceNameStr = "";
			Bundle devicesNameBundle = getParams();//获取参数
			if (devicesNameBundle.getString("DeviceName") != null) {
				deviceNameStr = devicesNameBundle.getString("DeviceName");
			} else {
				deviceNameStr = "华为P9";
			}
			Utils.logPrint("deviceNameStr = " + deviceNameStr);
			
			//获取测试次数
			long testTimes = 0;
			if (devicesNameBundle.getString("TestTimes") != null) {
				String testTimesStr = devicesNameBundle.getString("TestTimes");
				testTimes = Long.parseLong(testTimesStr);
			} else {
				testTimes = 100;
			}
			Utils.logPrint("testTimes = " + testTimes);
			
			
			HomePage homePage = new HomePage();
			SettingsPage settingsPage = new SettingsPage();
			BtTabPage btTabPage = new BtTabPage();
//			MediaPage mediaPage = new MediaPage();
			
//			homePage.goBackHome();
//			homePage.intoMultimedia();
//			mediaPage.intoLocalMusic();
			
			homePage.goBackHome();		//home
			homePage.intoSettings();	//settings
			settingsPage.intoBtTab();	//btsetting
			
			
			boolean keepTesting = true;
			long testCounter = 0;
			long testPassCounter = 0;
			while(keepTesting){
				testCounter ++;
				
				//连接
				if (btTabPage.btConnectFirstDeviceAllByName(deviceNameStr,60000)) {
					Utils.logPrint("connect ok ...");
					SystemClock.sleep(10000);
					//断开
					if (btTabPage.btDisconnectFirstDeviceAllByName(deviceNameStr,60000)) {
						Utils.logPrint("disconnect ok ...");
						SystemClock.sleep(10000);
						testPassCounter ++;
						Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					} else {
						Utils.logPrint("disconnect fail ...");
//						keepTesting = false;
					}
				}else {
					Utils.logPrint("connect fail,exit test...");
//					keepTesting = false;
				}
				if (testCounter == testTimes) {
					if (testPassCounter == testCounter) {
						isTestPass = true;//测试通过
					}
					keepTesting = false;
					Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException, test fail...");
		}
		return isTestPass;
	}	
	
}
