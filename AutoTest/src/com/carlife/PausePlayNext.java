package com.carlife;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.CarLifePage;
import com.runutils.RunTestCase;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.os.SystemClock;

/* 
 * case: Carlife暂停-播放-下一曲   1000次
 * 命令：uiautomator runtest AutoTest.jar -c com.carlife.PausePlayNext
 * 前提：连接carlife 播放carlife音乐(当前在播放状态，歌曲循环模式为全部循环)
 * 步骤：1.carlife音乐播放界面点击暂停软键       2.点击播放软键       3.点击下一曲的软键
 * 期望：能正常暂停-播放-下一曲   1000次 
 * 其他：需要先把
 * */
public class PausePlayNext extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.carlife.PausePlayNext", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testDemo(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			pausePlayNextTest();		//
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private boolean pausePlayNextTest() {
		boolean isTestPass = false;
		CarLifePage carLifeMusicPage = new CarLifePage();
		Bitmap beforeBitmap;
		Bitmap afterBitmap;
		String beforePathStr = "/data/local/tmp/baforeMusic.bmp";
		String afterPathStr = "/data/local/tmp/afterMusic.bmp";
		try {
			boolean keepTesting = true;
			int testCounter = 0;
			int testPassCounter = 0;
			Utils.logPrint("Prepair file...");
			if (carLifeMusicPage.takeBmps()) {
				Utils.logPrint("Prepair ok,start test...");
				while(keepTesting){
					testCounter ++;
					
					//获取歌名位图
					carLifeMusicPage.takeScreenShotTo(beforePathStr);
					beforeBitmap = carLifeMusicPage.getMusicTitleBitmap(beforePathStr);
					
					if (carLifeMusicPage.isPlaying()) {
						Utils.logPrint("is Playing,pause...");
						carLifeMusicPage.clickPausePlayButton();//点击暂停
						SystemClock.sleep(3000);
						if (carLifeMusicPage.isPausing()) {
							Utils.logPrint("is paused, replay...");
							carLifeMusicPage.clickPausePlayButton();//点击播放
							SystemClock.sleep(3000);
							if (carLifeMusicPage.isPlaying()) {
								Utils.logPrint("Replay ok,next song...");
								carLifeMusicPage.clickNextMusic();
								SystemClock.sleep(10000);
								
								//获取歌名位图
								carLifeMusicPage.takeScreenShotTo(afterPathStr);
								afterBitmap = carLifeMusicPage.getMusicTitleBitmap(afterPathStr);
								if (! CarLifePage.isBitmapTheSame(beforeBitmap, afterBitmap, 0.7)) {
									testPassCounter ++;
									Utils.logPrint("Next song Ok...");
									Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
									sleep(3000);
								} else {
									Utils.logPrint("the same song,next song fail..." + Utils.getNowTime());
									keepTesting = false;
								}
							}else {
								Utils.logPrint("replay fail..." + Utils.getNowTime());
								keepTesting = false;
							}
						}else {
							Utils.logPrint("pause fail..." + Utils.getNowTime());
							keepTesting = false;
						}
					} else {
						Utils.logPrint("is not Playing,test fail..." + Utils.getNowTime());
						keepTesting = false;
					}
					if (testCounter == 1000) {
						if (testPassCounter == testCounter) {
							isTestPass = true;//测试通过
						}
						keepTesting = false;
						Utils.logForResult("Test Pass:" + testPassCounter + " times,Total Test:" + testCounter);
					}
				}
			}else {
				Utils.logPrint("takeBmps fail..." + Utils.getNowTime());
			}
		} catch (FileNotFoundException e) {
			Utils.logPrint("FileNotFoundException,test fail..." + Utils.getNowTime());
		}
		return isTestPass;
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("=========case:CarLife pause play next=============");
		System.out.println("==================================================");
	}
}
