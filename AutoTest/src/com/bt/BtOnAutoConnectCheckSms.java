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


/* 
 * Case: 蓝牙开关切换，打开后自动连接检查短信协议    循环100次
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.BtOnAutoConnectCheckSms
 * 前提：1. 已有蓝牙连接记录，当前蓝牙处于关闭状态。
 * 步骤：多次反复的执行  蓝牙打开 - 自动连接-检查短信协议 - 关闭      循环100次
 * 其他：设置蓝牙名称和测试次数,每个-e对应一个参数
 * uiautomator runtest AutoTest.jar -e TestTimes 10000 -c com.bt.BtOnAutoConnectCheckSms &
 * */
public class BtOnAutoConnectCheckSms extends UiAutomatorTestCase {
	
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.BtOnAutoConnectCheckSms", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			btOnAutoConnectCheckSmsTest();		//压力测试：打开关闭蓝牙开关
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean btOnAutoConnectCheckSmsTest() {
		
		boolean isTestPass = false;
		
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		BtTabPage btTabPage = new BtTabPage();
		
		homePage.goBackHome();
		try {
			homePage.intoSettings();
			settingsPage.intoBtTab();
		} catch (UiObjectNotFoundException e1) {
			Utils.logPrint("UiObject not found");
		}

		PhonePage phonePage = new PhonePage();
		
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
		Utils.logPrint("TestTimes = " + testTimes);
		
		
		boolean keepTesting = true;
		int testCounter = 0;
		int passCounter = 0;
		while(keepTesting) {
			testCounter ++;
			try {
				if (! btTabPage.isBTOn()) {//蓝牙是关闭状态，则打开
					Utils.logPrint("turn on BT");
					SystemClock.sleep(5000);//蓝牙开关关闭后需要有点延迟才能直接再点击打开
					if (btTabPage.turnOnBT(3000)) {
						Utils.logPrint("turn on BT OK,check connect...");
						btTabPage.waitPairedListExists(30000);
						if (btTabPage.isBTConnectedAll(60000)) {//蓝牙连接成功
							Utils.logPrint("BT connect OK,check SMS...");
//							sleep(5000);//检查短信
							homePage.goBackHome();
							homePage.intoPhone();
							if (phonePage.intoMessaging()) {
								Utils.logPrint("Check SMS ok,turn off bt...");
								//返回蓝牙设置界面关闭蓝牙
								homePage.goBackHome();
								homePage.intoSettings();
								settingsPage.intoBtTab();
								if (btTabPage.turnOffBT(3000)) {
									passCounter ++;
									Utils.logPrint("Turn off BT OK");
									Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
								}else {
									Utils.logPrint("Turn off BT fail");
									keepTesting = false;
								}
							} else {
								Utils.logPrint("intoMessaging fail");
								keepTesting = false;
							}
						} else {
							Utils.logPrint("BT Connect fail");
							keepTesting = false;
							Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
						}
					}else {
						Utils.logPrint("turn on BT fail");
						keepTesting = false;
						Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
					}
				}else {
					Utils.logPrint("BT is not off,turn off...");
					if (btTabPage.turnOffBT(3000)) {
						Utils.logPrint("turn off ok");
					}else {
						Utils.logPrint("turn off fail");
					}
				}
				
				if (testCounter == testTimes) {
					if (passCounter == testTimes) {
						isTestPass = true;
					}
					Utils.logPrint("Finish test ...");
					Utils.logForResult("Test Pass:" + testCounter + " times,Total Test:" + testCounter);
					keepTesting = false;
				}
			} catch (UiObjectNotFoundException e) {
				Utils.logPrint("UiObjectNotFoundException");
				keepTesting = false;
			}
		}
		Utils.logPrint("exit test by yourself!");
		return isTestPass;
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("===case:BT turn on auto connect check sms=========");
		System.out.println("==================================================");
	}
}
