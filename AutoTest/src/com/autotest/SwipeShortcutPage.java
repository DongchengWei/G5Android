package com.autotest;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.ShortcutPage;

import android.os.RemoteException;
import android.util.Log;


/* 
 * case: 快捷启动栏页面左右各页滑动500次
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.SwipeShortcutPage
 * 前提：添加快捷方式，使快捷启动页面有两页或以上
 * 步骤：步骤：Home页点击影音娱乐，点击导航栏的快捷应用图标，
 * 		向左滑动页面，向右滑动页面，左右页面滑动循环500次
 * 期望：能正常执行快捷页面左右各页滑动 500次
 * 其他：
 * */
public class SwipeShortcutPage extends UiAutomatorTestCase {
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			swipeShortcutPageTest();		//压力测试：快捷栏左右滑动压力测试
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}	
	private void swipeShortcutPageTest() {
		HomePage homePage = new HomePage();
		ShortcutPage shortcutPage = new ShortcutPage();
		homePage.goBackHome();
		try {
			homePage.intoMultimedia();
			sleep(500);
			if (shortcutPage.intoShortcutPage()) {
				//滑动到下一页后第一个app名称
				//String firstPageFirstAppNameStr = mediaPage.getFirstAppNameBeforeSwipe();
				boolean keepTesting = true;
				int testCounter = 0;
				int passCounter = 0;
				while(keepTesting){
					testCounter ++;
					try {
						String firstPageFirstAppNameStr = shortcutPage.getFirstAppNameBeforeSwipe();
						String afterNextAppNameStr = shortcutPage.swipeToNextPageAndGetFirstAppName();
						String secondNextAppNameStr = shortcutPage.swipeToNextPageAndGetFirstAppName();
						
						Utils.logPrint("exit test please back home page...");
						Utils.logPrint("Swipe next...");
						int exitCounter = 0;//用来统计滑动到尾页的滑动次数，滑动超过5次则不再滑动
						boolean keepSwipe = true;
						while((!afterNextAppNameStr.equals(secondNextAppNameStr)) && (keepSwipe == true)){
							afterNextAppNameStr = secondNextAppNameStr;
							secondNextAppNameStr = shortcutPage.swipeToNextPageAndGetFirstAppName();
							exitCounter++;
							if (exitCounter == 5) {
								keepSwipe = false;
								Utils.logPrint("Too many pages,stop swipe next,swipe prev...");
							}
						}
						
						afterNextAppNameStr = shortcutPage.swipeToPrevPageAndGetFirstAppName();
						secondNextAppNameStr = shortcutPage.swipeToPrevPageAndGetFirstAppName();
						Utils.logPrint("Swipe prev...");
						while((!afterNextAppNameStr.equals(secondNextAppNameStr)) && (keepSwipe == true)){
							afterNextAppNameStr = secondNextAppNameStr;
							secondNextAppNameStr = shortcutPage.swipeToPrevPageAndGetFirstAppName();
							exitCounter++;
							if (exitCounter == 5) {
								keepSwipe = false;
								Utils.logPrint("Too many pages,stop swipe next,swipe prev...");
							}
						}
						if (firstPageFirstAppNameStr.equals(secondNextAppNameStr)) {
							passCounter ++;
							Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + testCounter);
						}
					} catch (UiObjectNotFoundException e) {
						if (homePage.isHomePageExists() == true) {
							keepTesting = false; //退出测试
							Utils.logPrint("You exit test by yourself!");
							Utils.logForResult("Test Pass:" + passCounter + " times,Total Test:" + (testCounter-1));
						}else {
							Utils.logPrint("UiObject not found,test fail:" + Utils.getNowTime());
						}
					}
					
				}//while
			}
			
			
			
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("====case:Shortcuts add and delete pressure test===");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","====case:Shortcuts add and delete pressure test===");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
