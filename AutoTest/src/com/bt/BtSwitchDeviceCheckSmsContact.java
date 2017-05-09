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


/**
 * Case: 切换连接蓝牙设备后检查短信和通讯录协议 100次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.BtSwitchDeviceCheckSmsContact
 * 前提：已匹配列表中有两个已匹配的蓝牙设备，且已允许访问手机短信和通讯录
 * 步骤：多次反复的执行     切换连接手机设备后检查短信和通讯录协议
 * 其他：默认蓝牙设备名称为P9和helpphone, 修复蓝牙名称和测试次数,每个-e对应一个参数,
 * uiautomator runtest AutoTest.jar -e FirstDevice P9 -e SecondDevice helpphone 
 * -e TestTimes 10000 -c com.bt.BtSwitchDeviceCheckSmsContact &
 * Date:2017-04-27
 * */
public class BtSwitchDeviceCheckSmsContact extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.BtSwitchDeviceCheckSmsContact", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator();//返回文件路径
//		String logPathStr = runTestCase.runUiautomator();//返回文件路径
		//new LogUtil().analyseLog(logPathStr);//分析日志
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("======case:BtSwitchDeviceCheckSmsContact==========");
		System.out.println("==================================================");
	}
	public void testDemo(){
		try {
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, btSwitchDeviceCheckSmsContactTest());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private boolean btSwitchDeviceCheckSmsContactTest() {
		boolean isTestPass = false;
		
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		BtTabPage btTabPage = new BtTabPage();
		PhonePage phonePage = new PhonePage();
		
		
		String firstDeviceStr = "";
		String secondDeviceStr = "";
		Bundle devicesNameBundle = getParams();//获取参数
		if (devicesNameBundle.getString("FirstDevice") != null) {
			firstDeviceStr = devicesNameBundle.getString("FirstDevice");
		} else {
			firstDeviceStr = "P9";
		}
		Utils.logPrint("firstDeviceName = " + firstDeviceStr);
		
		if (devicesNameBundle.getString("SecondDevice") != null) {
			secondDeviceStr = devicesNameBundle.getString("SecondDevice");
		} else {
			secondDeviceStr = "helpphone";
		}
		Utils.logPrint("secondDeviceName = " + secondDeviceStr);
		
		//获取测试次数
		long testTimes = 0;
		if (devicesNameBundle.getString("TestTimes") != null) {
			String testTimesStr = devicesNameBundle.getString("TestTimes");
			testTimes = Long.parseLong(testTimesStr);
		} else {
			testTimes = 100;
		}
		Utils.logPrint("TestTimes = " + testTimes);
		
		
		try {
			boolean keepTesting = true;
			int testCounter = 0;
			int passCounter = 0;
			while(keepTesting) {
				testCounter ++;
				
				homePage.goBackHome();		//home
				homePage.intoSettings();
				settingsPage.intoBtTab();	//btsetting
				if (btTabPage.connectBtDevice(firstDeviceStr, BtTabPage.ALL_CONNECT)) {
					homePage.goBackHome();
					homePage.intoPhone();
					if (phonePage.intoContact()) {
						getUiDevice().pressBack();
						sleep(1000);
						if (phonePage.intoMessaging()) {
							homePage.goBackHome();		//home
							homePage.intoSettings();
							settingsPage.intoBtTab();	//btsetting
							if (btTabPage.connectBtDevice(secondDeviceStr, BtTabPage.ALL_CONNECT)) {
								homePage.goBackHome();
								homePage.intoPhone();
								if (phonePage.intoContact()) {
									getUiDevice().pressBack();
									sleep(1000);
									if (phonePage.intoMessaging()) {
										passCounter ++;
										Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
									} else {
										Utils.logPrint(secondDeviceStr + " intoMessaging() fail");
										keepTesting = false;
									}
								} else {
									Utils.logPrint(secondDeviceStr + "intoContact() fail");
									keepTesting = false;
								}
							}
						} else {
							Utils.logPrint(firstDeviceStr + "intoMessaging() fail");
							keepTesting = false;
						}
					} else {
						Utils.logPrint(firstDeviceStr + "intoContact() fail");
						keepTesting = false;
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
			}//while
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("btSwitchDeviceCheckSmsContactTest:" + e.toString());
		}	//settings
		
		
		return isTestPass;
	}
}
