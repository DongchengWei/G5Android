package com.bt;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.pageutil.PhonePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.os.SystemClock;


/* 
 * case: 连续拨打客服电话后马上挂断   100次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.DailEndCallService
 * 前提：已连接蓝牙全部协议
 * 步骤：进入蓝牙音乐，蓝牙音乐自动播放，home-phone，重复拨打客服电话-马上挂断
 * 期望：能正常拨打挂断 100次（挂断后蓝牙音乐不会无声）
 * 其他：
 * */
public class DailEndCallService extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.DailEndCallInContact", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:BT DailEndCallService================");
		System.out.println("==================================================");
	}
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			btDailEndCallInContactTest();		//
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private boolean btDailEndCallInContactTest() {
		boolean isTestPass = false;
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		PhonePage phonePage = new PhonePage();
		homePage.goBackHome();
		try {
			homePage.intoMultimedia();
			mediaPage.intoBtMusic();
				if (mediaPage.isPlaying()) {
					homePage.goBackHome();
					homePage.intoPhone();
					
					boolean keepTesting = true;
					int testCounter = 0;
					int testPassCounter = 0;
					while(keepTesting){
						testCounter ++;
						
						phonePage.dialService();
						if (phonePage.endCall()) {//成功挂断电话
							Utils.logPrint("test pass");
							SystemClock.sleep(10000);
							testPassCounter ++;
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
						}
						if (testCounter == 100) {//100次
							keepTesting = false;
							Utils.logPrint("test finished");
							Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
						}
					}
				}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail");
		}
		
		return isTestPass;
	}
}
