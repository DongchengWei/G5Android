package com.operation;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.BtTabPage;
import com.pageutil.DialpadPage;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.pageutil.NavBarPage;
import com.pageutil.PhonePage;
import com.pageutil.ReversePage;
import com.pageutil.SettingsPage;
import com.pageutil.ShortcutPage;
import com.pageutil.SmsPage;
import com.pageutil.WifiTabPage;
import com.runutils.RunTestCase;

import android.os.RemoteException;
import android.os.SystemClock;


public class BtMusicOperate extends UiAutomatorTestCase {
	
	HomePage homePage = new HomePage();
	MediaPage mediaPage = new MediaPage();
	NavBarPage navBarPage = new NavBarPage();
	ShortcutPage shortcutPage = new ShortcutPage();
	SettingsPage settingsPage = new SettingsPage();
	WifiTabPage wifiTabPage = new WifiTabPage();
	PhonePage phonePage = new PhonePage();
	ReversePage reversePage = new ReversePage();
	SmsPage smsPage = new SmsPage();
	DialpadPage dialpadPage = new DialpadPage();
	BtTabPage btTabPage = new BtTabPage();
	
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.operation.BtMusicOperate", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator();//返回文件路径
//		String logPathStr = runTestCase.runUiautomator();//返回文件路径
//		new LogUtil().analyseLog(logPathStr);//分析日志
	}
	@Override
	protected void setUp() throws Exception {
		homePage.goBackHome();
		super.setUp();
	}
	/**
	 * 调试用
	 * */
	public void testDebug(){
		try {
			UiDevice.getInstance().wakeUp();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		boolean isPlaying = false;
		isPlaying = dialWhileBtMusicPlaying();
		assertEquals("测试结果：", true, isPlaying);
	}
	
	public boolean btTest() {
		boolean isOk = false;
		BtTabPage btTabPage = new BtTabPage();
		try {
			homePage.intoSettings();
			settingsPage.intoBtTab();
			btTabPage.connectBtDevice("P9", BtTabPage.HFP_ONLY);
			btTabPage.connectBtDevice("helpphone", BtTabPage.ALL_CONNECT);
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}

	
	/**
	 * 蓝牙音乐播放时重启蓝牙开关后进入蓝牙音乐，检查状态
	 * @return
	 * @Date 2017-04-28
	 */
	public boolean turnOffOnBtWhileBtPlaying() {
		boolean isOk = false;
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			mediaPage.intoBtMusic();
			mediaPage.musicDoPlay();
			
			homePage.goBackHome();
			homePage.intoSettings();
			settingsPage.intoBtTab();
			btTabPage.turnOffBT(3000);
			SystemClock.sleep(3000);
			btTabPage.turnOnBT(3000);
			btTabPage.isBTConnectedAll(30000);
			
			navBarPage.clickMedia();
			mediaPage.intoBtMusic();
			if (mediaPage.musicDoPause()) {
				if (mediaPage.musicDoPlay()) {
					if (mediaPage.nextMusic()) { 
						isOk = true;
						Utils.logPrint("turnOffOnBtWhileBtPlaying() pass");
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("turnOffOnBtWhileBtPlaying():" + e.toString());
		}
		
		return isOk;
	}
	/**
	 * 蓝牙音乐播放时关闭蓝牙开关
	 * @return
	 * @Date 2017-04-28
	 */
	public boolean turnOffBtWhileBtPlaying() {
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoFmAmPage()) {
					if (mediaPage.intoBtMusic()) {
						if (mediaPage.musicDoPlay()) {
							homePage.goBackHome();
							if (homePage.intoSettings()) {
								if (settingsPage.intoBtTab()) {
									if (btTabPage.turnOffBT(3000)) {
										navBarPage.clickMedia();
										if (mediaPage.SourceJudge() == MediaPage.FMAMTITLE) {
											isOk = true;
											Utils.logPrint("turnOffBtWhileBtPlaying() pass");
											homePage.goBackHome();
											homePage.intoSettings();
											settingsPage.intoBtTab();
											btTabPage.turnOnBT(3000);
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("turnOffBtWhileBtPlaying():" + e.toString());
		}
		
		return isOk;
	}

	/**
	 * 蓝牙音乐播放时拨打10086后不影响ID3和循环模式
	 * @return
	 * @Date 2017-04-28
	 */
	public boolean dialWhileBtMusicPlaying() {
		boolean isOk = false;
		
		String dialNumStr = "10086";
		String musicTitleStr = "";
		String artistStr = "";
		String albumStr = "";
		String playModeStr = "";
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPlay()) {
						playModeStr = mediaPage.getPlayModeString();
						musicTitleStr = mediaPage.getMusicTitle();
						artistStr = mediaPage.getMusicArtistText();
						albumStr = mediaPage.getMusicAlbumText();
						if (navBarPage.clickPhone()) {
							if (phonePage.intoDialPad()) {
								if (dialpadPage.dialAndEndCallAfterOncall(dialNumStr)) {
									SystemClock.sleep(1000);
									if (navBarPage.clickMedia()) {
										if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
											Utils.logPrint("checking...");
											if (mediaPage.isPlaying()) {
												if (mediaPage.getPlayModeString().equals(playModeStr)) {
													String afterTitleStr = mediaPage.getMusicTitle();
													String afterArtistStr = mediaPage.getMusicArtistText();
													String afterAlbumStr = mediaPage.getMusicAlbumText();
													if (afterAlbumStr.equals(albumStr) && afterArtistStr.equals(artistStr) && afterTitleStr.equals(musicTitleStr)) {
														isOk = true;
														Utils.logPrint("dialWhileBtMusicPlaying() pass");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("dialWhileBtMusicPlaying:" + e.toString());
		}
		
		return isOk;
	}
	
	/**
	 * 蓝牙音乐：音乐播放时播报短信
	 * @return
	 * @Date 2017-04-27
	 */
	public boolean smsVoiceBtMusicPlaying() {
		boolean isOk = false;
		String playModeStr = "";
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPlay()) {
						playModeStr = mediaPage.getPlayModeString();
						if (navBarPage.clickPhone()) {
							if (phonePage.intoMessaging()) {
								if (smsPage.intoFirstChatList()) {
									if (smsPage.playFirstSmsChat()) {
										if (navBarPage.clickMedia()) {
											if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
												if (mediaPage.isPlaying()) {
													//播放模式没有变化
													if (mediaPage.getPlayModeString().equals(playModeStr)) {
														isOk = true;
														Utils.logPrint("smsVoiceBtMusicPlaying() pass");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("smsVoiceBtMusicPlaying:" + e.toString());
		}
		
		return isOk;
	}
	
	/**
	 * 切换蓝牙其他功能界面检查蓝牙音乐是否保持播放且播放模式没有变化
	 * @return
	 * @Date 2017-04-27
	 */
	public boolean switchBtFunctionCheckMusicPlay() {
		boolean isOk = false;
		
		String playModeStr = "";
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPlay()) {
						playModeStr = mediaPage.getPlayModeString();
						homePage.goBackHome();
						homePage.intoPhone();
						if (phonePage.intoRecents()) {
							UiDevice.getInstance().pressBack();
							if (phonePage.intoDialPad()) {
								UiDevice.getInstance().pressBack();
								if (phonePage.intoContact()) {
									UiDevice.getInstance().pressBack();
									if (phonePage.intoMessaging()) {
										if (navBarPage.clickMedia()) {
											if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
												if (mediaPage.isPlaying()) {
													//播放模式没有变化
													if (mediaPage.getPlayModeString().equals(playModeStr)) {
														isOk = true;
														Utils.logPrint("switchBtFunctionCheckMusicPlay() pass");
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("switchBtFunctionCheckMusicPlay:" + e.toString());
		}
		
		return isOk;
	}
	
	/**
	 * 蓝牙音乐：切换倒车不打断蓝牙音乐暂停状态
	 * @return
	 * @Date 2017-04-27
	 */
	public boolean rvcBtMusicKeepPause() {
		boolean isOk = false;
		
		String musicTitleStr = "";
		String artistStr = "";
		String albumStr = "";
		String playModeStr = "";
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPause()) {
						playModeStr = mediaPage.getPlayModeString();
						musicTitleStr = mediaPage.getMusicTitle();
						artistStr = mediaPage.getMusicArtistText();
						albumStr = mediaPage.getMusicAlbumText();
						if (reversePage.intoReversePage()) {
							if (reversePage.exitReversePage()) {
								if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
									if (mediaPage.isPlaying()) {
										if (mediaPage.getPlayModeString().equals(playModeStr)) {
											String afterTitleStr = mediaPage.getMusicTitle();
											String afterArtistStr = mediaPage.getMusicArtistText();
											String afterAlbumStr = mediaPage.getMusicAlbumText();
											if (afterAlbumStr.equals(albumStr) && afterArtistStr.equals(artistStr) && afterTitleStr.equals(musicTitleStr)) {
												isOk = true;
												Utils.logPrint("rvcBtMusicKeepPause() pass");
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("rvcBtMusicKeepPause:" + e.toString());
		}
		
		return isOk;
	}
	
	/**
	 * 蓝牙音乐：切换蓝牙功能界面保持暂停状态
	 * @return 
	 * @Date 2017-04-27
	 */
	public boolean switchBtFunctionCheckMusicPause() {
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPause()) {
						homePage.goBackHome();
						homePage.intoPhone();
						if (phonePage.intoRecents()) {
							UiDevice.getInstance().pressBack();
							if (phonePage.intoDialPad()) {
								UiDevice.getInstance().pressBack();
								if (phonePage.intoContact()) {
									UiDevice.getInstance().pressBack();
									if (phonePage.intoMessaging()) {
										if (navBarPage.clickMedia()) {
											if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
												if (! mediaPage.isPlaying()) {
													isOk = true;
													Utils.logPrint("switchBtFunctionCheckMusicPause() pass");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("switchBtFunctionCheckMusicPause:" + e.toString());
		}
		
		return isOk;
	}
	
	/**
	 * 蓝牙音乐：切换FM后返回蓝牙音乐打断音乐暂停状态
	 * Date:20170427 
	 * */
	public boolean switchFmBtMusicPauseInterrupt() {
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPause()) {
						if (mediaPage.previousSource()) {
							if (mediaPage.SourceJudge() == MediaPage.FMAMTITLE) {
								if (mediaPage.nextSource()) {
									if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
										if (mediaPage.isPlaying()) {
											isOk = true;
											Utils.logPrint("switchFmBtMusicPauseInterrupt() pass");
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("switchFmBtMusicPauseInterrupt:" + e.toString());
		}
		return isOk;
	}
	
	/**
	 * 蓝牙音乐：音乐播放时车机端激活随机模式
	 * */
	public boolean changeToShuffleMode() {
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.changePlayModeTo(MediaPage.SHUFFLE_LIST)) {
						if (mediaPage.changePlayModeTo(MediaPage.SINGLE_REPEAT)) {
							if (mediaPage.changePlayModeTo(MediaPage.ALL_SHUFFLE_REPEAT)) {
								if (mediaPage.changePlayModeTo(MediaPage.ALL_MODE)) {
									if (mediaPage.changePlayModeTo(MediaPage.LIST_REPEAT)) {
										isOk = true;
										Utils.logPrint("changeToShuffleMode() pass");
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("changeToShuffleMode:" + e.toString());
		}
		return isOk;
	}
	
    /**
     * 蓝牙音乐播放时切换wifi开关
     */
	public boolean switchWifiWhenBtPlaying() {
		boolean isOk = false;
		
		try {
			homePage.intoMultimedia();
			mediaPage.intoBtMusic();
			mediaPage.musicDoPlay();
			homePage.goBackHome();
			homePage.intoSettings();
			settingsPage.intoWifiTab();
			wifiTabPage.turnOffWifi();
			navBarPage.clickMedia();
			if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
				if (mediaPage.isPlaying()) {
					homePage.goBackHome();
					homePage.intoSettings();
					settingsPage.intoWifiTab();
					wifiTabPage.turnOnWifi();
					navBarPage.clickMedia();
					if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
						if (mediaPage.isPlaying()) {
							isOk = true;
							Utils.logPrint("switchWifiWhenBtPlaying pass");
						}
					}
				}
			}
			
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("switchWifiWhenBtPlaying:" + e.toString());
		}
		return isOk;
	}
	
    /**
     * 蓝牙音乐播放时修改系统语言
     */
	public boolean changeLanguageWhenBtPlaying() {
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPlay()) {
						homePage.goBackHome();
						if (homePage.intoSettings()) {
							if (settingsPage.intoDisplayTab()) {
								if (settingsPage.intoLanguageSetting()) {
									if (settingsPage.seleteEnglish()) {
										navBarPage.clickMedia();
										if (mediaPage.isPlaying()) {
											UiDevice.getInstance().pressBack();//返回设置的原来页面
											if (settingsPage.intoLanguageSetting()) {
												if (settingsPage.seleteChinese()) {
													isOk = true;
													Utils.logPrint("changeLanguageWhenBtPlaying pass");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("changeLanguageWhenBtPlaying：" + e.toString());
		}
		return isOk;
	}
	
	/**
     * 蓝牙音乐暂停时锁屏不影响暂停状态
     */
	public boolean lockScreenWhenBtPausing(){
		boolean isOk = false;

		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPause()) {
						if (navBarPage.clickShortcut()) {
							if (shortcutPage.lockScreen()) {
								if (shortcutPage.unlockScreen()) {
									if (mediaPage.isPlaying() == false) {
										mediaPage.musicDoPlay();//恢复播放
										isOk = true;
										Utils.logPrint("lockScreenWhenBtPausing pass");
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("lockScreenWhenBtPausing：" + e.toString());
		}
		return isOk;
	}
	
	/**
	 * 蓝牙音乐播放时锁屏并解锁不影响播放状态
	 * */
	public boolean lockScreenWhenBtPlaying(){
		boolean isOk = false;

		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPlay()) {
						if (navBarPage.clickShortcut()) {
							if (shortcutPage.lockScreen()) {
								if (shortcutPage.unlockScreen()) {
									if (mediaPage.isPlaying()) {
										isOk = true;
										Utils.logPrint("lockScreenWhenBtPlaying pass");
									}
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("lockScreenWhenBtPlaying：" + e.toString());
		}
		return isOk;
	}
	
	/**
	 * 测试上下一曲
	 * */
	public boolean btMusicNextPrevious() {
		boolean isOk = false;

		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.nextMusic()) {
						if (mediaPage.previousMusic()) {
							isOk = true;
							Utils.logPrint("btMusicNextPrevious pass");
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("intoBtMusicAutoPlaying：" + e.toString());
		}
		return isOk;
	}
	
	/**
     * 测试进入蓝牙音乐自动播放
     */
	public boolean intoBtMusicAutoPlaying() {
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoFmAmPage()) {
					mediaPage.nextSource();
					if (mediaPage.SourceJudge() == MediaPage.BLUETOOTHTITLE) {
						if (mediaPage.isPlaying()) {
							mediaPage.previousSource();
							if (mediaPage.intoBtMusic()) {
								if (mediaPage.isPlaying()) {
									isOk = true;
									Utils.logPrint("intoMutimedia pass");
								}
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("intoBtMusicAutoPlaying：" + e.toString());
		}
		return isOk;
	}
	
	
    /**
     * 测试检查ID3页面
     */
	public boolean checkID3BtMusic() {
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.isPlaying()) {
						String endTimeStr = mediaPage.getEndTimeText();
						String musicTitleStr = mediaPage.getMusicTitle();
						String artistStr = mediaPage.getMusicArtistText();
						String albumStr = mediaPage.getMusicAlbumText();
						if (!musicTitleStr.isEmpty() && (!endTimeStr.equals("00:00"))) {
							Utils.logPrint("歌曲名：" + musicTitleStr);
							Utils.logPrint("歌手名：" + artistStr);
							Utils.logPrint("专辑名：" + albumStr);
							Utils.logPrint("歌曲总时长：" + endTimeStr);
							isOk = true;
							Utils.logPrint("checkID3BtMusic pass");
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("checkID3BtMusic：" + e.toString());
		}
		return isOk;
	}
	
    /**
     * 测试暂停播放
     * @return 
     */
	public boolean btMusicPausePlay(){
		boolean isOk = false;
		
		try {
			if (homePage.intoMultimedia()) {
				if (mediaPage.intoBtMusic()) {
					if (mediaPage.musicDoPlay()) {
						if (mediaPage.musicDoPause()) {
							if (mediaPage.musicDoPlay()) {
								isOk = true;
								Utils.logPrint("btMusicPausePlay pass");
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("btMusicPausePlay：" + e.toString());
		}
		return isOk;
	}
}
