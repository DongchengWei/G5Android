package com.media;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.os.SystemClock;


/* 
 * case: 播放U盘里所有的歌曲
 * 命令：uiautomator runtest AutoTest.jar -c com.media.PlayUdiskSongs
 * 前提：U盘已插上（U盘1或者u盘2）,循环模式设置为列表循环
 * 步骤：1.进入u盘； 2.点击音乐；3.点击所有音乐 ；4.选第一首歌开始播放
 * 		5.正常播放     6.下一曲
 * 期望：能正常播放所有的歌曲 
 * 其他：若遇到文件时间超过15分钟、下一曲后歌曲不能播放（播放时间不变化），
 * 		则截图保存，然后继续下一曲播放。（截图路径： /sdcard/Pictures）
 * 完成日期：2017-04-05
 * */
public class PlayUdiskSongs extends UiAutomatorTestCase {
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.media.PlayUdiskSongs", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:PlayUdiskSongs test=================");
		System.out.println("==================================================");
	}
	
	public void testUdiskPlaySongs(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.clickConfirmWatcher();
			
			//进入u盘播放
			udiskPlaySongsTest();
			
			UiDevice.getInstance().removeWatcher("confirmStopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void udiskPlaySongsTest() {
		
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		homePage.goBackHome();
		int theSameSongCounter = 0;
		int testCounter = 0;
		int songsCounter = 0;
		int playFailCounter = 0;
		int uiObjectNotFoundCounter = 0;
		int longTimeMusic = 0;//记录歌曲总时长过长的歌曲
		try {
			Utils.logPrint("start...");
			if (homePage.intoMultimedia()) {
				Utils.logPrint("intoMutimedia ok...");
				if (mediaPage.startPlayAllMusic()) {
					Utils.logPrint("startPlayAllMusic ok...");
					
					String saveCapPathStr = "/sdcard/Pictures/";
					String musicTitleStr = mediaPage.getMusicTitle();
					
						boolean keepTesting = true;
						while(keepTesting){
							try {
								testCounter ++;				//已循环数量自加
								Utils.logPrint("Music Title:<" + musicTitleStr + ">");
								if (mediaPage.isPlaying()) {//正在播放
									songsCounter ++;		//已播放成功数量自加
									int musicTimeInt = mediaPage.getMusicTotalTime();
									if (musicTimeInt>900) {//播放总时间显示超过15分钟为异常，截图
										longTimeMusic ++;
										Utils.logPrint("AbnormalMusicTime:" + longTimeMusic + ",MusicTitle:" + musicTitleStr);
										Utils.takeScreenshotToPath(saveCapPathStr, longTimeMusic + ".longtime_" + musicTitleStr + ".png");
									}
									Utils.logPrint("SameMusicCounter:" + theSameSongCounter + ",AbnormalMusicTime:" + longTimeMusic);
									Utils.logPrint("Pass:" + songsCounter + ",Fail:" + playFailCounter + ",Total Test:" + testCounter);
									if (mediaPage.clickNextButton()) {//下一曲
										SystemClock.sleep(3000);
										String nextMusicTitleStr = mediaPage.getMusicTitle();//获取下一曲后的歌曲名
										if (nextMusicTitleStr.equals(musicTitleStr)) {//歌曲名相同有可能下一曲没有成功
											theSameSongCounter ++;			//记录可能没有下一曲成功的数量，并输出歌曲名，截图
											Utils.logPrint("Same Music Counter:" + theSameSongCounter + ",Title:" + musicTitleStr);
											Utils.takeScreenshotToPath(saveCapPathStr, theSameSongCounter + ".samemusic_" + musicTitleStr + ".png");
										} else {
											musicTitleStr = nextMusicTitleStr;//下一曲成功，记录当前歌曲名
										}
									}
								} else {//没有播放成功的歌曲
									playFailCounter ++;
									Utils.logPrint("playFailCounter:" + playFailCounter + ",Title:" + musicTitleStr);
									Utils.takeScreenshotToPath(saveCapPathStr, playFailCounter + ".playFailCounter_" + musicTitleStr + ".png");
									mediaPage.clickNextButton();//没有播放成功，则截图记录后下一曲继续测试
								}
							} catch (UiObjectNotFoundException e) {
								if (homePage.isHomePageExists()) {
									Utils.logPrint("You exit test by yourself...");
									keepTesting = false;
								} else {
									uiObjectNotFoundCounter ++;
									if (uiObjectNotFoundCounter > 10) {
										keepTesting = false;
									}
									Utils.logPrint("UiObjectNotFoundException:");
									Utils.takeScreenshotToPath(saveCapPathStr, playFailCounter + ".UiObjectNotFoundException.png");
								}
							}
						}
				} else {
					Utils.logPrint("startPlayAllMusic fail...");
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException fail...");
		}
	}
	
	
}
