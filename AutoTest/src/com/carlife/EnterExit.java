package com.carlife;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.AppsPage;
import com.pageutil.CarLifePage;
import com.pageutil.HomePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.os.SystemClock;


/* 
 * case: Carlife进入退出   500次
 * 命令：uiautomator runtest AutoTest.jar -c com.carlife.EnterExit
 * 前提：连接carlife（连接数据线后手机端进入CarLife主界面，
 * 		车机端不用进入直接执行命令）
 * 步骤：1.系统进入carlife  2.系统退出carlife
 * 期望：能正常进入退出carlife500次 
 * 其他：
 * Date:2017-03-27
 * */
public class EnterExit extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.carlife.EnterExit", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testCarLifeEnterExit(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			enterExitTest();
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean enterExitTest() {
		boolean isOk = false;
		
		HomePage homePage = new HomePage();
		
		AppsPage appsPage = new AppsPage();
		CarLifePage carLifePage = new CarLifePage();
		String carLifeStr = "CarLife";
		try {
			homePage.goBackHome();
			homePage.intoApps();
			int firstIntoState = 0;//用来判断是否第一次进入carlife
			
			appsPage.launchOneAppByName(carLifeStr);
			sleep(10000);//splash界面停大概5秒
			if (carLifePage.clickExit()) {
				//不是第一次进carlife
				if (appsPage.isAppExists(carLifeStr)) {
					firstIntoState = 1;//
					Utils.logPrint("firstIntoState: " + firstIntoState);
				} else {//是第一次进carlife
					UiDevice.getInstance().pressBack();//返回carlife主界面
					if (carLifePage.clickCarLife()) {  //点击carlife
						firstIntoState = 2;
						SystemClock.sleep(1000);
						carLifePage.clickExit();
						Utils.logPrint("firstIntoState: " + firstIntoState);
					} 
				}
			}
			
			
			boolean keepTesting = true;
			int testCounter = 0;
			int testPassCounter = 0;
			while(keepTesting){
				testCounter ++;
				
				Utils.logPrint("Launch CarLife...");
				appsPage.launchOneAppByName(carLifeStr);
				sleep(10000);//splash界面停大概5秒
				if ((firstIntoState != 0) && carLifePage.clickExit()) {
					if (appsPage.isAppExists(carLifeStr)) {
						Utils.logPrint("CarLife exit ok...");
						testPassCounter ++;
						Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					}else {
						Utils.logPrint("app not exists fail..." + Utils.getNowTime());
					}
				}else {
					Utils.logPrint("clickExit fail..." + Utils.getNowTime());
					keepTesting = false;
				}
				if (testCounter == 500) {
					keepTesting = false;
					if (testPassCounter == testCounter) {
						Utils.logPrint("test pass: 500 times");
					}else {
						Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail..." + Utils.getNowTime());
		}
		return isOk;
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("======case:CarLife enter and exit next============");
		System.out.println("==================================================");
	}
}
