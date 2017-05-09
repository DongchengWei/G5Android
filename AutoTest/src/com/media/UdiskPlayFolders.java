package com.media;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.pageutil.MediaPage;
import com.pageutil.MuiscSeletePage;
import com.runutils.RunTestCase;

import android.os.RemoteException;


/* 
 * case: 播放U盘里所有文件夹下的歌曲
 * 命令：uiautomator runtest AutoTest.jar -c com.media.UdiskPlayFolders
 * 前提：U盘已插上（U盘1或者u盘2，或者sdcard）
 * 步骤：1.进入u盘       2.点击文件夹       3.依次播放每个文件夹里面所有歌曲
 * 期望：能正常播放完每个文件夹中的歌曲 
 * 其他：若遇到文件时间过小则退出测试，播放完毕退出测试，出现其他异常退出测试
 * 完成日期：2017-03-27
 * */
public class UdiskPlayFolders extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.media.UdiskPlayFolders", "", "3");  
		runTestCase.setDebug(false);  
		runTestCase.runUiautomator(); 
	} 
	
	public void testUdiskPlayFolders(){
		try{
			CaseInfo();
			getUiDevice().wakeUp();
			Utils.stopRunningWatcher();
			
			//进入u盘播放
			assertEquals(true, udiskPlayFoldersTest());
			
			UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private boolean udiskPlayFoldersTest() {
		boolean isOk = false;
		
		HomePage homePage = new HomePage();
		MediaPage mediaPage = new MediaPage();
		MuiscSeletePage musicSeletePage = new MuiscSeletePage();
		homePage.goBackHome();
		try {
			homePage.intoMultimedia();
			if (mediaPage.intoUsbMusicSeletePage()) {
				int folderCountInt = musicSeletePage.getFolderCount();
				Utils.logPrint("Folders: " + folderCountInt);
				if (folderCountInt>0) {
					for (int i = 0; i < folderCountInt; i++) {
						if (musicSeletePage.seleteFolder(i)) {
							if(musicSeletePage.finishPlayingThisFolder()){
								if (i == folderCountInt-1) {//是最后一个文件夹
									isOk = true;
								}else {
									mediaPage.intoUsbMusicSeletePage();
								}
							}else {
								Utils.logPrint("folder (" + (i+1) + ") finish play fail : "
											+ Utils.getNowTime());
							}
						}
					}
				}else {
					Utils.logPrint("Folders are 0!test fail: " + Utils.getNowTime());
				}
			}else {
				Utils.logPrint("intoUsbMusicSeletePage fail: " + Utils.getNowTime());
			}
		} catch (UiObjectNotFoundException e) {
			Utils.logPrint("UiObjectNotFoundException,test fail");
		}
		
		return isOk;
	}
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("==================================================");
		System.out.println("=========G5Android AutoTest v0.0.1================");
		System.out.println("========case:udisk play folders test==============");
		System.out.println("==================================================");
	}
}
