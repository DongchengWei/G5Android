package com.pageutil;

import java.io.File;
import java.io.FileNotFoundException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.CaptrueUtil;

import android.graphics.Bitmap;
import android.os.SystemClock;

public class CarLifePage extends UiAutomatorTestCase {

	
	String CARLIFEMUSICPLAYING = "/data/local/tmp/CarLifeMusicPlayingPage.bmp";
	String CARLIFEMUSICPAUSE = "/data/local/tmp/CarLifeMusicPausePage.bmp";
	String CARLIFEMUSICPAGE = "/data/local/tmp/CarLifeMusicPage.bmp";
	File carLifeMusicPlayingFile = new File(CARLIFEMUSICPLAYING); 
	File carLifeMusicPauseFile = new File(CARLIFEMUSICPAUSE); 
	File carLifeMusicPageFile = new File(CARLIFEMUSICPAGE); 
	
	//测试前先获取待测机器某些界面的截图
	public boolean takeBmps() throws FileNotFoundException {
		boolean isOk = false;
		//机器不同截图结果可能不同，所以删除旧图
		if (carLifeMusicPlayingFile.exists()) {
			carLifeMusicPlayingFile.delete();
		}
		//默认进入carlife时为播放状态，先截图获取播放状态的播放按钮
		if (UiDevice.getInstance().takeScreenshot(carLifeMusicPlayingFile)) {
			if (clickPausePlayButton()) {			//点击进入暂停状态
				//获取播放页面的子截图（播放按钮）
				Bitmap pauseButton = CaptrueUtil
						.getSubImage(CARLIFEMUSICPLAYING, 589, 405, 108, 135);
				SystemClock.sleep(3000);
				if (carLifeMusicPauseFile.exists()) {
					carLifeMusicPauseFile.delete();	//删除暂停状态旧截图
				}
				//暂停状态新截图
				if (UiDevice.getInstance().takeScreenshot(carLifeMusicPauseFile)) {
					//截图后的按钮
					Bitmap pauseButtonNow = CaptrueUtil
							.getSubImage(CARLIFEMUSICPAUSE, 589, 405, 108, 135);
					//暂停和播放状态两个按钮截图不同则说明截图成功
					if (! CaptrueUtil.sameAs(pauseButton, pauseButtonNow, 0.7)) {
						if (clickPausePlayButton()) {//进入播放
							isOk = true;
						}
					}
					
				}
			}
		}
		return isOk;
	}
	
	/*
	 * 函数功能：判断是否正在播放
	 * 			需要先拷贝暂停和播放时的截图到机器/data/local/tmp目录下，
	 * 			并命名为CarLifeMusicPlayingPage.bmp和
	 * 			CarLifeMusicPausePage.bmp
	 * 返回值：	如果正在播放返回true，否则false
	 * */
	public boolean isPlaying() throws FileNotFoundException {
		boolean isOk = false;
		
		if (carLifeMusicPlayingFile.exists()) {
			if (carLifeMusicPageFile.exists()) {//把旧截图先删除
				carLifeMusicPageFile.delete();
			}
			if (UiDevice.getInstance().takeScreenshot(carLifeMusicPageFile)) {
				
				//获取播放页面的子截图（播放按钮）
				Bitmap pauseButton = CaptrueUtil
						.getSubImage(CARLIFEMUSICPLAYING, 589, 405, 108, 135);
				
				//截图后的按钮
				Bitmap pauseButtonNow = CaptrueUtil
						.getSubImage(CARLIFEMUSICPAGE, 589, 405, 108, 135);

				if (CaptrueUtil.sameAs(pauseButton, pauseButtonNow, 0.7)) {
					isOk = true;
				}
			}
		}
		
		return isOk;
	}
	
	/*
	 * 函数功能：判断是否正在播放
	 * 			需要先拷贝暂停和播放时的截图到机器/data/local/tmp目录下，
	 * 			并命名为CarLifeMusicPlayingPage.bmp和
	 * 			CarLifeMusicPausePage.bmp
	 * 返回值：	如果正在播放返回true，否则false
	 * */
	public boolean isPausing() throws FileNotFoundException {
		boolean isOk = false;
		
		if (carLifeMusicPauseFile.exists()) {
			if (carLifeMusicPageFile.exists()) {//把旧截图先删除
				carLifeMusicPageFile.delete();
			}
			if (UiDevice.getInstance().takeScreenshot(carLifeMusicPageFile)) {
				
				//获取播放页面的子截图（播放按钮）
				Bitmap playingButton = CaptrueUtil
						.getSubImage(CARLIFEMUSICPAUSE, 589, 405, 108, 135);
				//截图后的按钮
				Bitmap playingButtonNow = CaptrueUtil
						.getSubImage(CARLIFEMUSICPAGE, 589, 405, 108, 135);
				
				if (CaptrueUtil.sameAs(playingButton, playingButtonNow, 0.7)) {
					isOk = true;
				}
			}
		}
		
		return isOk;
	}
	
	/*
	 * 函数功能：获取歌名的位图数据
	 * 参数：	截图路径
	 * 返回值：	歌名区域的位图数据
	 * */
	public Bitmap getMusicTitleBitmap(String path) throws FileNotFoundException {
		
		return CaptrueUtil.getSubImage(path, 150, 100, 145, 160);
	}
	
	/*
	 * 截图到某个路径,如果已存在先删除旧截图
	 * */
	public boolean takeScreenShotTo(String path) {
		boolean isOk = false;
		
		File screenShotFile = new File(path);
		if (screenShotFile.exists()) {
			screenShotFile.delete();
		}
		if (UiDevice.getInstance().takeScreenshot(screenShotFile)) {
			isOk = true;
		}
		return isOk;
	}
	
	//点击下一曲
	public boolean clickNextMusic() {
		if (UiDevice.getInstance().click(1068, 471)) {
			return true;
		} else {
			return false;
		}
	}
	
	//点击暂停/播放按钮
	public boolean clickPausePlayButton() {
		if (UiDevice.getInstance().click(644, 465)) {
			return true;
		} else {
			return false;
		}
	}
	
	//对比两个位图是否一致
	public static boolean isBitmapTheSame(Bitmap bitmap1, Bitmap bitmap2, double percent) {
		boolean isOk = false;
		if (CaptrueUtil.sameAs (bitmap1, bitmap2, percent)) {
			isOk = true;
		}
		
		return isOk;
	}
	
	//点击退出
	public boolean clickExit() {
		if (UiDevice.getInstance().click(208, 413)) {
			return true;
		} else {
			return false;
		}
	}
	//点击carlife主界面的CarLife
	public boolean clickCarLife() {
		if (UiDevice.getInstance().click(1096, 368)) {
			return true;
		} else {
			return false;
		}
	}
}
