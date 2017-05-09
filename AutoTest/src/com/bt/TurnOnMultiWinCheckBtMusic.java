package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.LogUtil;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.pageutil.NavBarPage;
import com.pageutil.SettingsPage;
import com.pageutil.SysTabPage;
import com.runutils.RunTestCase;

import android.os.Bundle;
import android.os.RemoteException;

/* 
 * case: 打开多窗口模式后从FM切换到蓝牙音乐检查蓝牙音乐标题是否仍显示FM   100次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.BtConnectDisconnect
 * 前提：蓝牙已连接，多窗口模式关闭状态，当前在FM界面
 * 步骤：打开多窗口开关，进入FM，切换到蓝牙音乐，检查蓝牙音乐标题
 * 		 - 关闭多窗口 - 进入FM - 打开多窗口 - 进入FM - 切换到蓝牙音乐 - 检查标题
 * 期望：正确执行 100次
 * 其他：设置测试次数,每个-e对应一个参数
 * uiautomator runtest AutoTest.jar -e TestTimes 200 -c com.bt.TurnOnMultiWinCheckBtMusic
 * */
public class TurnOnMultiWinCheckBtMusic extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.TurnOnMultiWinCheckBtMusic", "", "3");  
		runTestCase.setDebug(false);  
		String logPathStr = runTestCase.runUiautomator();//返回文件路径
		new LogUtil().analyseLog(logPathStr);//分析日志
	} 
	public void testDemo(){
		try{
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			assertEquals(true, turnOnMultiWinCheckBtMusicTest());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private boolean turnOnMultiWinCheckBtMusicTest() {
		boolean isTestPass = false;
		
		Bundle devicesNameBundle = getParams();//获取参数
		//获取测试次数
		long testTimes = 0;
		if (devicesNameBundle.getString("TestTimes") != null) {
			String testTimesStr = devicesNameBundle.getString("TestTimes");
			testTimes = Long.parseLong(testTimesStr);
		} else {
			testTimes = 1;
		}
		Utils.logPrint("testTimes = " + testTimes);
		
		HomePage homePage = new HomePage();
		SettingsPage settingsPage = new SettingsPage();
		MediaPage mediaPage = new MediaPage();
		NavBarPage navBarPage = new NavBarPage();
		SysTabPage sysTabPage = new SysTabPage();
		
		try {
			boolean keepTesting = true;
			long testCounter = 0;
			long testPassCounter = 0;
			while(keepTesting){
				testCounter ++;
				
				homePage.goBackHome();
				homePage.intoMultimedia();
				mediaPage.intoFmAmPage();
				homePage.goBackHome();
				homePage.intoSettings();
				settingsPage.intoSystemTab();
				sysTabPage.turnOnMultiWin(3000);
				navBarPage.clickMedia();
				sleep(500);
				if (mediaPage.intoBtMusic()) {
					testPassCounter ++;
					Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					homePage.goBackHome();
					homePage.intoSettings();
					settingsPage.intoSystemTab();
					sysTabPage.turnOffMultiWin(3000);
				} else {
					Utils.logPrint("intoBtMusic() fail ...");
					keepTesting = false;
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
			Utils.logPrint("turnOnMultiWinCheckBtMusicTest fail..." + e.toString());
		}
		
		return isTestPass;
	}
}
