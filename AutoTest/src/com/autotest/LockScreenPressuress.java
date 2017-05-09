package com.autotest;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;

/* 
 * case: 锁屏-解锁   1000次
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.LockScreenPressuress
 * 前提：车机已开机，当前未锁屏，快捷栏中显示有锁屏图标
 * 步骤：返回home界面 - 点击快捷栏的锁屏 - 点击屏幕解锁 - （锁屏-解锁循环1000次）
 * 期望：能正常锁屏-解锁   1000次
 * 其他：修改测试次数需要在指令中加入 “-e TestTimes + 测试次数”
 * 		如下：
 * uiautomator runtest AutoTest.jar -e TestTimes 10 -c com.autotest.LockScreenPressuress
 * */
public class LockScreenPressuress extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.autotest.LockScreenPressuress", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:LockScreenPressuress test============");
		System.out.println("==================================================");
	}
	public void testLockScreenPressuress(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			lockScreenPressuressTest();		//
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean lockScreenPressuressTest() {
		boolean testPass = false;
		
		HomePage homePage = new HomePage();
		homePage.goBackHome();
		if (homePage.isFirstShortcutBarPage()) {
			Utils.logPrint("lock screen icon exists,lock...");
			try {
			
				boolean keepTesting = true;
				int testCounter = 0;
				int testPassCounter = 0;
				int testTimesLimit = 0;
				
				Bundle testTimes = getParams();//获取参数
				if (testTimes.getString("TestTimes") != null) {
					String testTimeStr = testTimes.getString("TestTimes");
					testTimesLimit = Integer.parseInt(testTimeStr);
				} else {//默认测试1000次
					testTimesLimit = 1000;
				}
				Utils.logPrint("testTimesLimit=" + testTimesLimit);
				
				while(keepTesting){
					testCounter ++;
					if (homePage.lockScreenInHomePage()) {
						Utils.logPrint("lock ok,unlock...");
						if (homePage.unlockScreenInHomePage()) {
							Utils.logPrint("unlock ok,test pass");
							testPassCounter ++;
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
						} else {
							Utils.logPrint("unlock fail,exit test...");
							keepTesting = false;
						}
						if (testCounter == testTimesLimit) {//1000次
							keepTesting = false;
							Utils.logPrint("test finished");
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
							if (testCounter == testPassCounter) {
								testPass = true;
							}
						}
					} else {
						Utils.logPrint("lock fail,exit test...");
						keepTesting = false;
					}
				}
			} catch (UiObjectNotFoundException e) {
				Utils.logPrint("UiObjectNotFoundException fail");
			}
		} else {
			Utils.logPrint("lock screen icon not exists fail");
		}
		return testPass;
	}
}
