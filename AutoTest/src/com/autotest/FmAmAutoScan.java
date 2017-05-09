package com.autotest;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.FmAmPage;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.util.Log;


/*
 * case:电台自动扫描200次
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.FmAmAutoScan
 * 前提：1.扫描模式开关处在自动状态，FM/AM选择开关处在FM状态
 * 步骤： 点击扫描按钮，完成一遍扫描，再次点击扫描按钮，循环200次
 * 期望：能正常执行电台自动扫描200次
 * FM：搜索不到电台时，会回到87.5并停止扫描，
 * 		搜索到电台时会保存电台并停在已扫描到信号比较强的电台
 * AM：搜索不到电台时，会回到初始位置522并停止扫描，
 * 		搜索到电台时，会保存保存电台并停在初始位置522
 * */
public class FmAmAutoScan extends UiAutomatorTestCase {
	
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.autotest.FmAmAutoScan", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			fmAmAutoScanTest();		//压力测试：启动/退出app
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	private void fmAmAutoScanTest() {
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		FmAmPage fmAmPage = new FmAmPage();
		
		try {
			homePage.goBackHome();
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			Utils.logPrint("Switch to auto scan and FM");
			//切换到FM自动扫描模式
			if (fmAmPage.switchToAutoScan() && fmAmPage.switchToFmMode()) {
				
				boolean keepTesting = true;
				int testCounter = 0;
				int passCounter = 0;
				while(keepTesting){
					testCounter ++;
					try {
						String beforeScanFreq = fmAmPage.getFrequencyText();//开始自动扫描前频率
						Utils.logPrint("Before scan Frequency: " + beforeScanFreq);
						fmAmPage.scanStart();//开始扫描
						Utils.logPrint("Start Scan...");
						//直到停止扫描，否则一直对比
						boolean isScanning = true;
						int timeCounter = 0;
						while(isScanning){
							sleep(10000);
							String nowFrequencyStr = fmAmPage.getFrequencyText();
							Utils.logPrint("Result Frequency: " + nowFrequencyStr);
							if (nowFrequencyStr.equals(beforeScanFreq) 
									|| fmAmPage.isNowFrequencyButtonExists(nowFrequencyStr)) {
								passCounter ++;
								isScanning = false;
								Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							} else {
								sleep(10000);
								timeCounter ++;
								if (timeCounter == 12) {//120秒都没有结果则该次测试失败退出循环进行下次测试
									Utils.logPrint("Scan fail...");
									isScanning = false;
								}
							}
						}
						if (testCounter == 200) {
							Utils.logPrint("Test finished");
							Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
							keepTesting = false;
						}
					} catch (UiObjectNotFoundException e) {
						Utils.logPrint("Scan fail...");
						keepTesting = false;
						Utils.logPrint("UiObject not found in scaning");
					}
				}
			}else {
				Utils.logPrint("Switch to auto scan or FM fail");
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObject not found");
		}
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:FM/AM auto scan pressure test========");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","========case:FM/AM auto scan pressure test========");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
