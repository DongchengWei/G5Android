package com.pageutil;


import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

public class MediaPage extends UiAutomatorTestCase {
	
	public static final int STORAGETITLE = 1;
	public static final int USB1TITLE = 2;
	public static final int USB2TITLE = 3;
	public static final int SDCARDTITLE = 4;
	public static final int FMAMTITLE = 5;
	public static final int BLUETOOTHTITLE = 6;
	public static final int IPODTITLE = 7;
	public static final int AUXTITLE = 8;
	//可通过title判断source
	//中文： FM/AM , 本地存储 ， U盘1 ， U盘2 ，内存卡，蓝牙设备 ， AUX, iPod
	//英文：FM/AM , Storage,  USB1,  USB2, SD Card,Bluetooth,AUX, iPod
	UiObject mediaTitleObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/media_title"));
	UiObject mediasourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/media_source"));
	UiObject nextSourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/media_next"));
	UiObject previousSourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/media_pre"));
	
	//蓝牙设备
	UiObject btSourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_bt"));
	//本地源
	UiObject localSourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_local"));
	//usb源
	UiObject usb1SourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_usb1"));
	UiObject usb2SourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_usb2"));
	//sdsource按钮
	UiObject sdSourceObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_card"));
	//音乐状态栏
	UiObject musicStatusbarObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/rl_media_music_all_statusbar"));
	
	//没有进入播放界面是弹出的选择播放文件类型：音乐、视频、图片
	UiObject musicObj = new UiObject(new UiSelector()//音乐
			.resourceId("com.thundersoft.mediaplayer:id/ts_media_button_item_music_id"));
	//歌名
	UiObject musicTitleObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_title"));
	//艺术家
	UiObject musicArtistObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_artist_name"));
	//专辑
	UiObject musicAlbumObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_ts_media_music_Album_name"));
	//播放时间
	UiObject startTimeObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_start_time"));
	//歌曲总时间
	UiObject endTimeObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_end_time"));
	//上一曲
	UiObject previousButtonObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_previous"));
	//下一曲
	UiObject nextButtonObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_next"));
	//播放暂停按钮
	UiObject playButtonObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_pause"));
	//播放列表按钮
	UiObject playListBtnObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_play_list"));
	//播放模式按钮isEnabled
	UiObject playModeObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/iv_ts_media_btn_play_mode"));
	//播放模式文字
	UiObject playModeTextObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_media_play_mode"));
	
	//FMsource
	UiObject fmAmDeviceObj = new UiObject(new UiSelector()//FM
			.resourceId("com.thundersoft.mediaplayer:id/ts_media_source_button_item_device_radio"));
	
	//判断当前是在那个source
	public int SourceJudge() throws UiObjectNotFoundException {
		String titleStr = mediaTitleObj.getText();
		if (titleStr.equals("Storage") || titleStr.equals("本地存储")) {
			return STORAGETITLE;
		}else if (titleStr.equals("USB1") || titleStr.equals("U盘1")) {
			return USB1TITLE;
		}else if (titleStr.equals("USB2") || titleStr.equals("U盘2")) {
			return USB2TITLE;
		}else if (titleStr.equals("SD Card") || titleStr.equals("内存卡")) {
			return SDCARDTITLE;
		}else if (titleStr.equals("FM/AM")) {
			return FMAMTITLE;
		}else if (titleStr.equals("Bluetooth") || titleStr.equals("蓝牙设备")) {
			return BLUETOOTHTITLE;
		}else if (titleStr.equals("iPod")) {
			return IPODTITLE;
		}else if (titleStr.equals("AUX")) {
			return AUXTITLE;
		}else {
			return 0;
		}
	}
	
	/**
	 * 切换到下一个source
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean nextSource() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (nextSourceObj.click()) {
			isOk = true;
		}
		return isOk;
	}
	/**
	 * 切换到上一个source
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean previousSource() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (previousSourceObj.click()) {
			isOk = true;
		}
		return isOk;
	}
	
	/**
	 * 进入本地音乐，通过点击source方式,如果当前已在本地音乐界面则直接返回true
	 * */
	public boolean intoLocalMusic() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean intoOk = false;
		
		if (mediaTitleObj.exists()) {
			if (SourceJudge() == STORAGETITLE) {
				intoOk = true;
			} else {
				if (mediasourceObj.click()) {
					if (localSourceObj.click()) {
						if (musicObj.click()) {//音乐视频图片三个选项选择音乐
							if (musicStatusbarObj.click()) {//点击正在播放的曲目状态栏
								if (SourceJudge() == STORAGETITLE) {
									intoOk = true;
								}
							}
						}
					}
				}
			}
		} else {
			UiDevice.getInstance().pressBack();
			if (mediasourceObj.click()) {
				if (localSourceObj.click()) {
					if (musicObj.click()) {//音乐视频图片三个选项选择音乐
						if (musicStatusbarObj.click()) {//点击正在播放的曲目状态栏
							if (SourceJudge() == STORAGETITLE) {
								intoOk = true;
							}
						}
					}
				}
			}
		}
		return intoOk;
	}
	/**
	 * 进入蓝牙音乐，通过点击source方式,如果当前已在蓝牙音乐界面则直接返回true
	 * */
	public boolean intoBtMusic() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean intoOk = false;
		
		if (mediaTitleObj.exists()) {
			if (SourceJudge() == BLUETOOTHTITLE) {
				intoOk = true;
			} else {
				if (mediasourceObj.click()) {
					if (btSourceObj.click()) {
						if (SourceJudge() == BLUETOOTHTITLE) {
							intoOk = true;
						}
					}
				}
			}
		} else {
			if (musicStatusbarObj.exists()) {
				if (musicStatusbarObj.click()) {//点击正在播放的曲目状态栏
					if (mediasourceObj.click()) {
						if (btSourceObj.click()) {
							if (SourceJudge() == BLUETOOTHTITLE) {
								intoOk = true;
							}
						}
					}
				}
			} 
		}
		return intoOk;
	}
	
	
	/**
	 * 判断是否正在播放
	 * */
	public boolean isPlaying() throws UiObjectNotFoundException {
		boolean isOk = false;
		if (startTimeObj.waitForExists(3000)) {
			sleep(1000);
			String startTimeStr = startTimeObj.getText();
			sleep(2000);
			if (! startTimeStr.equals(startTimeObj.getText())) {
				isOk = true;
			}
		}
		
		return isOk;
	}

	/**
	 * 获取音乐总时长
	 * */
	public int getMusicTotalTime() throws UiObjectNotFoundException {
		
		int timeCounter = 0;
		
		if (endTimeObj.exists()) {
			String sourceStr = endTimeObj.getText();
			String[] sourceStrArray = sourceStr.split(":");
			if (sourceStrArray.length < 3) {//分钟
				int minInt = Integer.parseInt(sourceStrArray[0]);
				int secInt = Integer.parseInt(sourceStrArray[1]);
				timeCounter = minInt * 60 + secInt;
			} else {
				timeCounter = 1200;
			}
		}
		return timeCounter;
	}
	
	/**
	 * 下一曲
	 * */
	public boolean nextMusic() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		
		if (musicTitleObj.exists()) {
			String musicTitleStr = musicTitleObj.getText();
			Utils.logPrint("musicTitleStr:" + musicTitleStr);
			if (nextButtonObj.waitForExists(3000)) {
				if (nextButtonObj.click()) {
					SystemClock.sleep(3000);
					String nextStr = musicTitleObj.getText();
					Utils.logPrint("nextStr:" + nextStr);
					if (! musicTitleStr.equals(nextStr)) {
						isOk = true;
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 上一曲
	 * */
	public boolean previousMusic() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (musicTitleObj.exists()) {
			SystemClock.sleep(3000);
			String startTimeStr = startTimeObj.getText();
			int startTimeInt = Utils.timeStringToSecond(startTimeStr);
			Utils.logPrint("before：" + startTimeInt);
			if (previousButtonObj.click()) {
				sleep(2000);
				int preTime = Utils.timeStringToSecond(startTimeObj.getText());
				Utils.logPrint("after pre time：" + preTime);
				if (preTime < startTimeInt) {
					isOk = true;
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 点击下一曲按钮
	 * */
	public boolean clickNextButton() throws UiObjectNotFoundException {
		
		boolean isOk = false;
		
		if (nextButtonObj.exists()) {
			if (nextButtonObj.click()) {
				isOk = true;
			}
		}
		
		return isOk;
	}
	
	/**
	 * 点击 播放/暂停
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean clickPlayPause() throws UiObjectNotFoundException {
		return playButtonObj.click();
	}
	
	/**
	 * 播放,如果本来是播放状态则直接返回true，否则点击播放
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean musicDoPlay() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (isPlaying()) {
			isOk = true;
		} else {
			clickPlayPause();
			if (isPlaying()) {
				isOk = true;
			}
		}
		return isOk;
	}
	/**
	 * 暂停,如果本来是暂停状态则直接返回true，否则点击暂停
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean musicDoPause() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		if (isPlaying() == false) {
			isOk = true;
		} else {
			clickPlayPause();//暂停
			if (isPlaying() == false) {//暂停成功
				isOk = true;
			}
		}
		return isOk;
	}
	
	/**
	 * 进入SD Card音乐
	 * */
	public boolean intoSdMusic() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean intoOk = false;
		if (musicStatusbarObj.exists()) {
			if (musicStatusbarObj.click()) {//点击正在播放的曲目状态栏
				if (SourceJudge() == SDCARDTITLE) {
					intoOk = true;
				}
			}
		} else {
			if (mediasourceObj.click()) {
				if (sdSourceObj.click()) {
					if (musicObj.click()) {//音乐视频图片三个选项选择音乐
						if (musicStatusbarObj.click()) {//点击正在播放的曲目状态栏
							if (SourceJudge() == SDCARDTITLE) {
								intoOk = true;
							}
						}
					}
				}
			}
			
		}
		return intoOk;
	}
	
	
	/**
	 * 进入usb1音乐
	 * */
	public boolean intoUsb1Music() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean intoOk = false;
		
		boolean backOk = false;
		if (! mediasourceObj.exists()) {
			if (UiDevice.getInstance().pressBack()) {
				backOk = true;
			}
		} else {
			backOk = true;
		}
		
		if (backOk && mediasourceObj.click()) {
			if (usb1SourceObj.isEnabled()) {//usb1已插上则默认进入usb1
				usb1SourceObj.click();
				if (musicObj.click()) {//音乐视频图片三个选项选择音乐
					if (musicStatusbarObj.click()) {//点击正在播放的曲目状态栏
						if (SourceJudge() == USB1TITLE) {
							intoOk = true;
						}
					}
				}
			}
		}
		return intoOk;
	}
	
	/**
	 * 进入usb2音乐
	 * */
	public boolean intoUsb2Music() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean intoOk = false;
		
		boolean backOk = false;
		if (! mediasourceObj.exists()) {
			if (UiDevice.getInstance().pressBack()) {
				backOk = true;
			}
		} else {
			backOk = true;
		}
		
		if (backOk && mediasourceObj.click()) {
			if (usb2SourceObj.isEnabled()) {//usb1已插上则默认进入usb1
				usb2SourceObj.click();
				if (musicObj.click()) {//音乐视频图片三个选项选择音乐
					if (musicStatusbarObj.click()) {//点击正在播放的曲目状态栏
						if (SourceJudge() == USB2TITLE) {
							intoOk = true;
						}
					}
				}
			}
		}
		return intoOk;
	}
	/**
	 * 进入USB音乐的选择文件夹页面，usb1或者2，或者sdcard,都插着默认进入usb1
	 * */
	UiObject allFolderObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_media_music_all_folder"));
	public boolean intoUsbMusicSeletePage() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean intoOk = false;
		
		boolean backOk = false;
		if (! mediasourceObj.exists()) {
			if (UiDevice.getInstance().pressBack()) {
				backOk = true;
			}
		} else {
			backOk = true;
		}
		if (backOk && mediasourceObj.click()) {//USB1
			if (usb1SourceObj.isEnabled()) {//usb1已插上则默认进入usb1
				usb1SourceObj.click();
				if (musicObj.click()) {//音乐视频图片三个选项选择音乐
					if (allFolderObj.click()) {//点击“文件夹”
						intoOk = true;
					}
				}
			} else if (usb2SourceObj.isEnabled()) {//USB2
				usb2SourceObj.click();
				if (musicObj.click()) {//音乐视频图片三个选项选择音乐
					if (allFolderObj.click()) {//点击“文件夹”
						intoOk = true;
					}
				}
			}
		}
		return intoOk;
	}
	
	/**
	 * 进入所有音乐选择第一首开始播放
	 * */
	UiObject allMusicObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/rl_media_music_all_music"));
	UiObject musicLabelObj = new UiObject(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/tv_kindlabel"));
	public boolean startPlayAllMusic() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		
		boolean backOk = false;
		if (! mediasourceObj.exists()) {
			if (UiDevice.getInstance().pressBack()) {
				backOk = true;
			}
		} else {
			backOk = true;
		}
		if (backOk && mediasourceObj.click()) {//USB1
			if (usb1SourceObj.isEnabled()) {//usb1已插上则默认进入usb1
				usb1SourceObj.click();
				musicObj.click();
				if (allMusicObj.waitForExists(60000)) {//音乐视频图片三个选项选择音乐
					allMusicObj.click();//点击“所有音乐”
					if (musicLabelObj.waitForExists(120000)) {//20分钟
						if (musicLabelObj.click()) {
							isOk = true;
						}
					}
				}
			} else if (usb2SourceObj.isEnabled()) {//USB2
				usb2SourceObj.click();
				musicObj.click();
				if (allMusicObj.waitForExists(60000)) {//音乐视频图片三个选项选择音乐
					allMusicObj.click();//点击“所有音乐”
					if (musicLabelObj.waitForExists(120000)) {//20分钟
						if (musicLabelObj.click()) {
							isOk = true;
						}
					}
				}
			}
		}
		return isOk;
	}
	
	/**
	 * 进入FM/AM页
	 * */
	public boolean intoFmAmPage() throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean intoFmOk = false;
		if (! mediasourceObj.exists()) {
			UiDevice.getInstance().pressBack();
		}
		if (SourceJudge() == FMAMTITLE) {
			intoFmOk = true;
		} else {
			if (mediasourceObj.click()) {
				if (fmAmDeviceObj.click()) {
					intoFmOk = true;
				}
			}
		}
		return intoFmOk;
	}
	
	/**
	 * 获取歌曲总时长
	 * @return String
	 * */
	public String getEndTimeText() throws UiObjectNotFoundException {
		return endTimeObj.getText();
	}
	
	/**
	 * 获取歌曲名
	 * */
	public String getMusicTitle() throws UiObjectNotFoundException {
		String musicTitleStr = "";
		if (musicTitleObj.exists()) {
			musicTitleStr = musicTitleObj.getText();
		}
		
		return musicTitleStr;
	}
	
	/**
	 * 获取艺术家
	 * @throws UiObjectNotFoundException 
	 * */
	public String getMusicArtistText() throws UiObjectNotFoundException {
		return musicArtistObj.getText();
	}
	
	/**
	 * 获取专辑名称
	 * @return String
	 * @throws UiObjectNotFoundException 
	 * */
	public String getMusicAlbumText() throws UiObjectNotFoundException {
		return musicAlbumObj.getText();
	}
	
	
	/**
	 * 获取播放模式状态的字符串
	 * @return 播放模式的text
	 * @throws UiObjectNotFoundException
	 * @Date 2017-04-27
	 */
	public String getPlayModeString() throws UiObjectNotFoundException {
		String playModeStr = "";
		
		if (playModeTextObj.exists()) {
			playModeStr = playModeTextObj.getText();
		}
		
		return playModeStr;
	}
	
	public static final int SHUFFLE_LIST = 1;
	public static final int SINGLE_REPEAT = 2;
	public static final int LIST_REPEAT = 3;
	public static final int ALL_MODE = 4;
	public static final int ALL_SHUFFLE_REPEAT = 5;
	/**
	 * 改变播放模式
	 * 列表随机,
	 * @return boolean 成功返回true
	 * @param  int playMode
	 * SHUFFLE_MODE：列表随机 Shuffle list
	 * ：单曲循环 Single repeat
	 * ：列表循环 list repeat
	 * ：所有音乐 All
	 * ：所有随机循环 All Shuffle repeat
	 * @throws UiObjectNotFoundException 
	 * */
	public boolean changePlayModeTo(int playMode) throws UiObjectNotFoundException {
		Utils.getCurrentMethodName();
		boolean isOk = false;
		
		String targetModeCnStr = "";
		String targetModeEnStr = "";
		switch (playMode) {
		case SHUFFLE_LIST:
			targetModeCnStr = "列表随机";
			targetModeEnStr = "Shuffle list";
			break;
		case SINGLE_REPEAT:
			targetModeCnStr = "单曲循环";
			targetModeEnStr = "Single repeat";
			break;
		case LIST_REPEAT:
			targetModeCnStr = "列表循环";
			targetModeEnStr = "list repeat";
			break;
		case ALL_MODE:
			targetModeCnStr = "所有音乐";
			targetModeEnStr = "ALL";
			break;
		case ALL_SHUFFLE_REPEAT:
			targetModeCnStr = "所有随机循环";
			targetModeEnStr = "All Shuffle repeat";
			break;

		default:
			break;
		}
		
		if (playModeObj.isEnabled()) {
			String modeStr = getPlayModeString();
			if (modeStr.equals(targetModeCnStr) || modeStr.equals(targetModeEnStr)) {
				isOk = true;
			} else {
				int times = 0;
				while(times < 5){
					playModeObj.click();
					SystemClock.sleep(1000);
					modeStr = getPlayModeString();
					if (modeStr.equals(targetModeCnStr) || modeStr.equals(targetModeEnStr)) {
						isOk = true;
						times = 10;
					}
				}
			}
		} else {
			Utils.logPrint("循环模式按钮不可点击，请确认...");
		}
		return isOk;
	}
}
