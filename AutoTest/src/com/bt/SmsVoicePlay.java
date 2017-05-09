package com.bt;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.PhonePage;
import com.pageutil.SmsPage;

import android.os.RemoteException;
import android.util.Log;


/* 
 * Case: 短信播报50次
 * 命令：uiautomator runtest AutoTest.jar -c com.bt.SmsVoicePlay
 * 前提：1. 车机已连接蓝牙全部协议，已下载短信且短信有会话内容。
 * 步骤：多次反复的执行  短信播报     循环50次
 * 其他：
 * */
public class SmsVoicePlay extends UiAutomatorTestCase {
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			smsVoicePlayTest();		//压力测试：启动/退出app
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void smsVoicePlayTest() {
		// TODO Auto-generated method stub
		HomePage homePage = new HomePage();
		try {
			homePage.goBackHome();
			homePage.intoPhone();
			PhonePage phonePage = new PhonePage();
			SmsPage smsPage = new SmsPage();
			if (phonePage.intoMessaging()) {
				if (smsPage.intoFirstChatList()) {
					
					boolean keepTesting = true;
					int testCounter = 0;
					int passCounter = 0;
					while(keepTesting) {
						testCounter ++;
						
						Utils.logPrint("Play sms voice...");
						if (smsPage.playFirstSmsChat()) {
							passCounter ++;
							Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
						} else {
							keepTesting = false;//退出测试
							Utils.logPrint("Play sms voice fail...");
							Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
						}
						if (testCounter == 50) {
							keepTesting = false;//退出测试
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException");
		}
		
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("=======case:Sms voice play pressure test==========");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","========case:BT SmsVoicePlay test=================");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
