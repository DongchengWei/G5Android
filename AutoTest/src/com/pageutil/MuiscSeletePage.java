package com.pageutil;

import com.android.uiautomator.core.UiCollection;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;

import android.os.SystemClock;

public class MuiscSeletePage extends UiAutomatorTestCase {

	//所有文件夹的集合，通过这个可以获取文件夹数量
	UiCollection musicFolderCol = new UiCollection(new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/gv_music_folder"));
	//grid view选择器，通过这个选择文件夹某个文件夹
	UiSelector sGvFolder = new UiSelector()
			.resourceId("com.thundersoft.mediaplayer:id/gv_music_folder");
	
	
	//获取文件夹数量
	public int getFolderCount() throws UiObjectNotFoundException {
		int foldersCounter = 0;
		if (musicFolderCol.exists()) {
			foldersCounter = musicFolderCol.getChildCount();
		}
		return foldersCounter;
	}
	
	//点击第folerCount个文件夹播放
	public boolean seleteFolder(int folerCount) throws UiObjectNotFoundException {
		boolean isOk = false;
		
		UiSelector layoutForder = sGvFolder.childSelector(new UiSelector()
				.className("android.widget.RelativeLayout").index(folerCount));
		UiObject folderSeletedObj = new UiObject(layoutForder);
		if (folderSeletedObj.exists()) {
			if (folderSeletedObj.click()) {
				isOk = true;
			}
		}
		return isOk;
	}
	
	//通过下一曲，播放10秒，下一曲的方式播放完一个文件夹里的歌曲
	public boolean finishPlayingThisFolder() throws UiObjectNotFoundException {
		boolean isFinished = false;
		
		MediaPage mediaPage = new MediaPage();
		String firstMusicTitleStr = mediaPage.getMusicTitle();
		String beforeMusicTitleStr,afterMusicTitleStr;
		beforeMusicTitleStr = firstMusicTitleStr;
		
		boolean keepTesting = true;
		while((isFinished == false) && (keepTesting == true)){
			//下一曲
			Utils.logPrint("First Music: " + firstMusicTitleStr);
			
			if (mediaPage.clickNextButton()) {
				SystemClock.sleep(3000);//等待5s
				if (mediaPage.isPlaying()) {
					afterMusicTitleStr = mediaPage.getMusicTitle();
					Utils.logPrint("Playing: " + afterMusicTitleStr);
					//下一曲后的歌名与第一首歌相同说明播放完毕
					if (afterMusicTitleStr.equals(firstMusicTitleStr)) {
						isFinished = true;
						keepTesting = false;
					} else if (!afterMusicTitleStr.equals(beforeMusicTitleStr)) {
						beforeMusicTitleStr = afterMusicTitleStr;
					}else {
						Utils.logPrint("next music fail:" + Utils.getNowTime());
						keepTesting = false;
					}
					
				} else {
					Utils.logPrint("is not playing fail:" 
							+ mediaPage.getMusicTitle() + Utils.getNowTime());
					keepTesting = false;
				}
			} else {
				Utils.logPrint("click next fail:" + Utils.getNowTime());
				keepTesting = false;
			}
		}
		
		return isFinished;
	}
	
}
