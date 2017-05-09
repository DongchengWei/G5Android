package com.rvc;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.ReversePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;


/* 
 * case: 倒车时隐藏和显示设置栏1000次
 * 命令：uiautomator runtest AutoTest.jar -c com.rvc.ShowHideSettingBar
 * 前提：当前在任意界面
 * 步骤：1.系统进入倒车  2.点击隐藏倒车设置栏  3.点击显示倒车设置栏
 * 		重复执行隐藏和显示倒车设置栏500次
 * 期望：能正常执行隐藏和显示倒车设置栏500次 
 * 其他：测试通过会自动退出倒车，否则不做任何动作。
 * Date:2017-03-29
 * */
public class ShowHideSettingBar extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.rvc.ShowHideSettingBar", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("=====case:RVC show and hide setting bar===========");
		System.out.println("==================================================");
	}
	
	public void testRvcShowHideSettingBar(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, showHideSettingBarTest());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private boolean showHideSettingBarTest() {
		boolean isOk = false;
		ReversePage reversePage = new ReversePage();
		
			
		Utils.logPrint("Enter RVC...");
		if (reversePage.intoReversePage()) {
			Utils.logPrint("Enter RVC ok,show setting bar...");
			
			try {
				boolean keepTesting = true;
				int testCounter = 0;
				int testPassCounter = 0;
				while(keepTesting){
					testCounter ++;
					
					if (reversePage.showRvcSettingBar()) {
						Utils.logPrint("Show ok,hide...");
						if (reversePage.hideRvcSettingBar()) {
							Utils.logPrint("hide ok...");
							testPassCounter ++;
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
						} else {
							Utils.logPrint("hide fail...");
							keepTesting = false;
						}
					} else {
						Utils.logPrint("show fail...");
						keepTesting = false;
					}
					if (testCounter == 1000) {
						if (testPassCounter == testCounter) {
							isOk = true;
							keepTesting = false;//1000次完成则退出测试
							reversePage.exitReversePage();
							Utils.logForResult("Test pass:" + testPassCounter);
						}else {
							Utils.logForResult("Test fail:" + (testCounter - testPassCounter) + " times in " + testCounter);
						}
					}
				}
			} catch (UiObjectNotFoundException e) {
				Utils.logPrint("UiObjectNotFoundException...");
			}
		}
		return isOk;
	}
}
