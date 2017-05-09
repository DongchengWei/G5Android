package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MultiWinPage;
import com.pageutil.NavBarPage;
import com.pageutil.SettingsPage;
import com.pageutil.SysTabPage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.os.SystemClock;


/* 
 * case: 多窗口模式下播报蓝牙短信50次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.MultiWinPlaySms
 * 前提：已连接蓝牙全部协议，已下载短信且短信有会话内容，
 * 步骤：1.进入系统设置打开多窗口开关。2.点击侧边栏进入媒体播放。
 * 		3.点击侧边栏进入电话通讯，进入短信会话 。4.循环播放某条短信50次。
 * 期望：多窗口模式下能正常播报蓝牙短信50次 
 * 其他：
 * Date:2017-03-31
 * */
public class MultiWinPlaySms extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.MultiWinPlaySms", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("======case:MultiWin play sms voice test===========");
		System.out.println("==================================================");
	}
	
	public void testMultiWinPlaySms(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, multiWinPlaySmsTest());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private boolean multiWinPlaySmsTest() {
		boolean isOk = false;
		
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		SysTabPage sysTabPage = new SysTabPage();
		NavBarPage navBarPage = new NavBarPage();
		MultiWinPage multiWinPage = new MultiWinPage();
		
		homePage.goBackHome();
		try {
			homePage.intoSettings();
			settingsPage.intoSystemTab();
			if (sysTabPage.turnOffMultiWin(3000)) {//需要先关闭再打开多窗口才复位
				if (sysTabPage.turnOnMultiWin(3000)) {
					if (navBarPage.clickMedia()) {
						SystemClock.sleep(500);
						if (navBarPage.clickPhone()) {
							SystemClock.sleep(500);
							if (multiWinPage.intoSmsMultiWinPage()) {
								if (multiWinPage.intoFirstSmsChar()) {
									
									boolean keepTesting = true;
									int testCounter = 0;
									int testPassCounter = 0;
									while(keepTesting){
										testCounter ++;
										
										if (multiWinPage.playFirstSmsVoice()) {
											testPassCounter ++;
											Utils.logPrint("Play OK...");
											Utils.logForResult("Test Pass:" 
													+ testPassCounter + " times,Total Test:" + testCounter);
										}
										if (testCounter == 50) {
											if (testPassCounter == testCounter) {
												isOk = true;
												keepTesting = false;
												//测试通过则关闭多窗口开关
												homePage.goBackHome();
												homePage.intoSettings();
												settingsPage.intoSystemTab();
												if (sysTabPage.turnOffMultiWin(3000)) {
													Utils.logForResult("Turn off multi win in end ok...");
												} else {
													Utils.logForResult("Turn off multi win in end fail...");
												}
												Utils.logForResult("Test pass:" + testPassCounter);
											}else {
												Utils.logForResult("Test fail:" + (testCounter - testPassCounter) + " times in " + testCounter);
											}
										}
									}
								} else {
									Utils.logForResult("intoFirstSmsChar fail...");
								}
							} else {
								Utils.logForResult("intoSmsMultiWinPage fail...");
							}
						} else {
							Utils.logForResult("click phone fail...");
						}
					} else {
						Utils.logForResult("click media fail...");
					}
				} else {
					Utils.logForResult("turn on multi win switch fail...");
				}
			} else {
				Utils.logForResult("turn off multi win switch fail...");
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException fail...");
		}
		return isOk;
	}
	
}
