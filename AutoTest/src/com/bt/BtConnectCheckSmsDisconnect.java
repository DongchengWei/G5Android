package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.BtTabPage;
import com.pageutil.HomePage;
import com.pageutil.PhonePage;
import com.pageutil.SettingsPage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;


/* 
 * case: 连接蓝牙后检查短信协议是否连接，如果连接断开重连再检查  100次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.BtConnectCheckSmsDisconnect
 * 前提：蓝牙已匹配并连接过一次，手机端允许读短信和联系人权限，当前已匹配列表只有一个设备
 * 步骤：进入蓝牙设置页，点击该设备，连接成功后进入短信页面，短信页面存在则返回蓝牙设置界
 * 		面，点击该蓝牙设备，断开蓝牙连接
 * 期望：断开重连后短信协议能正常连接，100次
 * 其他：设置蓝牙名称和测试次数,每个-e对应一个参数
 * uiautomator runtest AutoTest.jar -e TestTimes 200 -c com.bt.BtConnectCheckSmsDisconnect
 * */

public class BtConnectCheckSmsDisconnect extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.BtConnectCheckSmsDisconnect", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.clickConfirmWatcher();
			
			btConnectCheckSmsDisconnectTest();		//
			
			UiDevice.getInstance().removeWatcher("confirmStopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private boolean btConnectCheckSmsDisconnectTest() {
		
		boolean isTestPass = false;
		try {
			HomePage homePage = new HomePage();
			SettingsPage settingsPage = new SettingsPage();
			BtTabPage btTabPage = new BtTabPage();
			PhonePage phonePage = new PhonePage();
			
			String deviceNameStr = "";
			Bundle devicesNameBundle = getParams();//获取参数
			if (devicesNameBundle.getString("DeviceName") != null) {
				deviceNameStr = devicesNameBundle.getString("DeviceName");
			} else {
				deviceNameStr = "华为P9";
			}
			Utils.logPrint("deviceNameStr = " + deviceNameStr);
			
			homePage.goBackHome();//home
			homePage.intoSettings();//settings
			settingsPage.intoBtTab();//btsetting
			
			
			boolean keepTesting = true;
			int testCounter = 0;
			int testPassCounter = 0;
			
			
			//获取测试次数
			long testTimes = 0;
			if (devicesNameBundle.getString("TestTimes") != null) {
				String testTimesStr = devicesNameBundle.getString("TestTimes");
				testTimes = Long.parseLong(testTimesStr);
			} else {
				testTimes = 100;
			}
			Utils.logPrint("TestTimes = " + testTimes);
			
			while(keepTesting){
				testCounter ++;
				
				Utils.logPrint("connect to " + deviceNameStr);
				if (btTabPage.btConnectFirstDeviceAllByName(deviceNameStr, 60000)) {
					Utils.logPrint("connect ok ...");
					SystemClock.sleep(10000);
					homePage.goBackHome();
					homePage.intoPhone();
					if (phonePage.intoMessaging()) {
						Utils.logPrint("map connect ok ...");
						
						homePage.goBackHome();
						homePage.intoSettings();
						settingsPage.intoBtTab();
						if (btTabPage.btDisconnectFirstDeviceAllByName(deviceNameStr,60000)) {
							testPassCounter ++;
							Utils.logPrint("disconnect ok ...");
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
							sleep(10000);
						} else {
							Utils.logPrint("disconnect fail ...");
//							keepTesting = false;
						}
					}else {
						Utils.logPrint("map connect fail ...");
						keepTesting = false;
					}
				} else {
					Utils.logPrint("connect fail ...");
//					keepTesting = false;
				}
				if (testCounter == testTimes) {
					if (testPassCounter == testCounter) {
						isTestPass = true;
					}
					Utils.logPrint("Finish test ...");
					Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					keepTesting = false;
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail ...");
		}
		return isTestPass;
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("====case:BT connect check sms and disconnect======");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","====case:BT connect check sms and disconnect======");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
