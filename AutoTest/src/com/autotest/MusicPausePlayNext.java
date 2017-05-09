package com.autotest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.RemoteException;
import android.util.Log;



/* 
 * case: 暂停-播放-下一曲
 * 命令：uiautomator runtest AutoTest.jar -c com.autotest.MusicPausePlayNext
 * 前提：已插入U盘且U盘中有音乐文件
 * 步骤：优先播放U盘1中的音乐，若U盘1没有插上，则播放U盘2,若U盘2没有插上则播放内存卡，
 *		没有内存卡则播放本地，都没有则退出测试。
 *        	测试前请先把循环模式设置为全部循环（列表循环）
 *        	执行测试程序后，会自动进行播放-暂停-播放-下一曲的操作直到
 *        	有异常则退出测试并截图到相应路径（路径请查看输出的log）。
 * 期望：暂停-播放-下一曲不出现异常
 * 注意事项：尽量播放曲目正常的歌曲，每首歌曲时间超过一分钟。歌曲数量尽量超过5首。
 * */
public class MusicPausePlayNext extends UiAutomatorTestCase {
	
	public static final String LOCAL_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_local";
	public static final String USB1_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_usb1";
	public static final String USB2_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_usb2";
	public static final String SD_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_card";
	public static final String FM_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_radio";
	public static final String BT_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_bt";
	public static final String IPOD_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_ipod";
	public static final String AUX_SOURCE = "com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_aux";
	
	//播放界面常用ID常量
	public static final String MUSICTITLE = "com.thundersoft.mediaplayer:id/tv_ts_media_music_title";
	public static final String MUSIC_START_TIME = "com.thundersoft.mediaplayer:id/tv_start_time";
	public static final String MUSIC_END_TIME = "com.thundersoft.mediaplayer:id/tv_end_time";
	public static final String NEXT_SONG = "com.thundersoft.mediaplayer:id/iv_ts_media_btn_next";
	public static final String PREVIOUS_SONG = "com.thundersoft.mediaplayer:id/iv_ts_media_btn_previous";
	//所有音乐
	public static final String ALLMUSIC = "com.thundersoft.mediaplayer:id/rl_media_music_all_music";
	
	String startTestTimeStr = "";
	public void testDemo(){
		try{
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			MusicPausePlayNextTest();		//媒体音乐暂停-播放-下一曲
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void MusicPausePlayNextTest() {
		CaseInfo();
		startTestTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());	
		Utils.logPrint("Start Test:" + startTestTimeStr);
		File capPicture;
		String savePicPath = "/storage/usb0/";
		String targetSouceIdStr = LOCAL_SOURCE;
		try {
			UiDevice.getInstance().pressHome();
			new UiObject(new UiSelector().resourceId("com.android.launcher:id/ts_home_button_item_media_id")).click();
			UiObject mediaSourceObj = new UiObject(new UiSelector()	//souce按钮
					.resourceId("com.thundersoft.mediaplayer:id/media_source"));
			mediaSourceObj.click();
			
			UiObject usb1Obj = new UiObject(new UiSelector()//U盘1
					.resourceId(USB1_SOURCE));
			UiObject usb2Obj = new UiObject(new UiSelector()//U盘1
					.resourceId(USB2_SOURCE));
			UiObject sdcardObj = new UiObject(new UiSelector()//U盘1
					.resourceId(SD_SOURCE));
			if (usb1Obj.isEnabled()) {
				savePicPath = "/storage/usb0/";
				targetSouceIdStr = USB1_SOURCE;
				Utils.logPrint("USB1_SOURCE:");
			}else if (usb2Obj.isEnabled()) {
				savePicPath = "/storage/usb1/";
				targetSouceIdStr = USB2_SOURCE;
				Utils.logPrint("USB2_SOURCE:");
			}else if (sdcardObj.isEnabled()) {
				savePicPath = "/storage/sdcard1/";
				targetSouceIdStr = SD_SOURCE;
				Utils.logPrint("SD_SOURCE:");
			}else {
				savePicPath = "/sdcard/Pictures/";
				targetSouceIdStr = LOCAL_SOURCE;
				Utils.logPrint("LOCAL_SOURCE:");
			}
			
			
			UiObject targetSourceObj = new UiObject(new UiSelector()//targetSouce
					.resourceId(targetSouceIdStr));
			UiObject musicIdObj = new UiObject(new UiSelector()		//source-targetsource-music
					.resourceId("com.thundersoft.mediaplayer:id/ts_media_button_item_music_id"));
			UiObject allMusicObj = new UiObject(new UiSelector()	//所有音乐
					.resourceId(ALLMUSIC));
			targetSourceObj.click();
			musicIdObj.click();				//into music 
			allMusicObj.click();			//click folder
			new UiObject(new UiSelector()	//所有音乐第一首开始播放
					.resourceId("com.thundersoft.mediaplayer:id/iv_folder")).click();
			
			UiObject pauseBtnObj = new UiObject(new UiSelector()	//暂停/播放
					.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause"));
			UiObject startTimeObj = new UiObject(new UiSelector()	//已播放时间
					.resourceId(MUSIC_START_TIME));
			UiObject nextBtnObj = new UiObject(new UiSelector()		//下一首
					.resourceId(NEXT_SONG));
			UiObject musicTitleObj = new UiObject(new UiSelector()
					.resourceId(MUSICTITLE));
			String startTimeStr = "00:00";
			
			String pauseStartStr;
			String pauseEndStr;
			String startPlayStr;
//			String nextMusicTitleStr;
			String nowTimeStr;
			String musicTitleStr;
			Utils.logPrint("start play...");
			
			boolean keepPlaying = true;
			int songsCounter = 1;
			while (keepPlaying) {
				musicTitleStr = musicTitleObj.getText();
				Utils.logPrint(">>Songs Counter:" + songsCounter + ",Music Title:<" + musicTitleStr + ">");
				sleep(2000);
				if (!startTimeStr.equals(startTimeObj.getText())) {//正在播放
					Utils.logPrint("auto play OK,pause...");
					pauseBtnObj.click();//暂停
					sleep(2000);
					pauseStartStr = startTimeObj.getText();
					sleep(2000);
					pauseEndStr = startTimeObj.getText();
					if (pauseStartStr.equals(pauseEndStr)) {//暂停成功
						Utils.logPrint("Pause OK,play...");
						pauseBtnObj.click();//播放
						sleep(2000);
						startPlayStr = startTimeObj.getText();//点击播放2s后的时间
						if (!startPlayStr.equals(pauseEndStr)) {//时间变化
							Utils.logPrint("Pause then play OK,next song...");
							nextBtnObj.click();
							songsCounter ++;
//							nextMusicTitleStr = musicTitleObj.getText();
//							if (!nextMusicTitleStr.equals(musicTitleStr)) {//下一首成功（通过歌曲总时间判断）
//								TestResource.logAndPrint("next song OK");
//							}else {
//								TestResource.logAndPrint("next song fail,takescreenshort...");
//								nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
//								TestResource.logAndPrint("Save capture to " + savePicPath + "NextSongFail-" + nowTimeStr + ".png");
//								capPicture = new File(savePicPath + "NextSongFail-" + nowTimeStr + ".png");
//								getUiDevice().takeScreenshot(capPicture);
//								keepPlaying = false;
//							}
						}else {
							Utils.logPrint("pause then play fail,takescreenshort...");
							nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
							Utils.logPrint("Start Test:" + startTestTimeStr + ", end test:" + nowTimeStr);
							Utils.logPrint("Save capture to " + savePicPath + "PauseThenPlayFail-" + nowTimeStr + ".png");
							capPicture = new File(savePicPath + "PauseThenPlayFail-" + nowTimeStr + ".png");
							getUiDevice().takeScreenshot(capPicture);
							keepPlaying = false;
						}
					}else {
						Utils.logPrint("pause fail,takescreenshort...");
						nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
						Utils.logPrint("Start Test:" + startTestTimeStr + ", end test:" + nowTimeStr);
						Utils.logPrint("Save capture to " + savePicPath + "PauseFail-" + nowTimeStr + ".png");
						capPicture = new File(savePicPath + "PauseFail-" + nowTimeStr + ".png");
						getUiDevice().takeScreenshot(capPicture);
						keepPlaying = false;
					}
				}else {
					Utils.logPrint("auto play fail,takescreenshort...");
					nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
					Utils.logPrint("Start Test:" + startTestTimeStr + ", end test:" + nowTimeStr);
					Utils.logPrint("Save capture to " + savePicPath + "NextSongFail-" + nowTimeStr + ".png");
					capPicture = new File(savePicPath + "AutoPlayFail-" + nowTimeStr + ".png");
					getUiDevice().takeScreenshot(capPicture);
					keepPlaying = false;
				}
			}
		}catch (UiObjectNotFoundException e) {
			String nowTimeStr = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
			if (new UiObject(new UiSelector().resourceId("com.android.launcher:id/ts_home_button_item_media_id")).exists()) {
				Utils.logPrint("You exit test!");
				Utils.logPrint("Start Test:" + startTestTimeStr + ", end test:" + nowTimeStr);
			}else {
				Utils.logPrint("Start Test:" + startTestTimeStr + ", end test:" + nowTimeStr);
				Utils.logPrint("capture to " + savePicPath + "UiObjectNotFoundException-" + nowTimeStr + ".png");
				capPicture = new File(savePicPath + "UiObjectNotFoundException-" + nowTimeStr + ".png");
				getUiDevice().takeScreenshot(capPicture);
				Utils.logPrint("MusicPausePlayNext fail: UiObjectNotFoundException");
			}
		}
	}

	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("=========case:music pause play next===============");
		System.out.println("==================================================");
		Log.d("BLUETOOTHAUTOTEST","=========G5Android AutoTest v0.0.1================");
		Log.d("BLUETOOTHAUTOTEST","=========case:music pause play next===============");
		Log.d("BLUETOOTHAUTOTEST","==================================================");
	}
}
