package com.bt;

import java.io.IOException;


import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.runutils.RunTestCase;

import android.os.RemoteException;


/* 
 * case: Carlife暂停-播放-下一曲   1000次
 * 命令：uiautomator runtest AutoTest.jar -c com.carlife.PausePlayNext
 * 前提：连接carlife 播放carlife音乐(当前在播放状态，歌曲循环模式为全部循环)
 * 步骤：1.carlife音乐播放界面点击暂停软键       2.点击播放软键       3.点击下一曲的软键
 * 期望：能正常暂停-播放-下一曲   1000次 
 * 其他：
 * */
public class BtMusicTest extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.BtMusicTest", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	public void intoMusicAutoPlay(){
		try{
//			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			//进入蓝牙音乐自动播放音乐
			assertEquals(true, intoBtMusicAutoPlay());
			assertEquals(true, nextBtMusic());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	//蓝牙音乐下一曲测试
	private boolean nextBtMusic() {
		boolean intoOk = false;
		
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		homePage.goBackHome();
		try {
			homePage.intoMultimedia();
			if (mediaPage.nextMusic()) {
				intoOk = true;
				Utils.logPrint("nextMusic test pass");
			}			
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail");
		}
		
		return intoOk;
	}

	//进入蓝牙音乐自动播放蓝牙音乐
	private boolean intoBtMusicAutoPlay() {
		boolean intoOk = false;
		
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		homePage.goBackHome();
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.isPlaying()) {
						intoOk = true;
						Utils.logPrint("test pass");
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail");
		}
		
		return intoOk;
	}
}
