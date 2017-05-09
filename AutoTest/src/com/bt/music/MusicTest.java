package com.bt.music;

import java.io.IOException;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.LogUtil;
import com.otherutils.Utils;
import com.pageutil.HomePage;
import com.runutils.RunTestCase;
import com.operation.*;

import android.os.RemoteException;

/**
 * 蓝牙音乐常规case,可以完全自动化的
 * 1.需要用支持播放列表和循环模式的手机连接测试，如iphone和三星部分手机。
 * 2. uiautomator runtest AutoTest.jar -c com.bt.music.MusicTest &
 * 包括BtMusicFunctionTest和一部分的BtMusicConflictTest
 * 
 * Case完成情况：
 * 
 * 2017-05-05
 * 拨打特殊字符的电话号码
 * 18.TC_ST16081569834//输入一串字符数字，以数字开头
 * 19.TC_ST16081569835//输入一串字符*#
 * 20.TC_ST16081569836//输入一串字符数字，以字符开头
 * 拨打不同长度的电话号码
 * 21.TC_ST16081569828,拨打一位号码，拨号无效
 * 22.TC_ST16081569829,三位号码
 * 23.TC_ST16081569830,7位	  
 * 24.TC_ST16081569831,11位
 * 25.TC_ST16081569832,15
 * 26.TC_ST16081569833,30
 * 
 * 2017-04-28
 * 17.TC_ST16081569110:蓝牙音乐：音乐播放时重启开关
 * 16.TC_ST16081569109:蓝牙音乐：音乐播放时关闭蓝牙开关
 * 15.TC_ST16081569096:蓝牙音乐：音乐播放时车机端拨号
 * 
 * 2017-04-27
 * 14.TC_ST16081569088:蓝牙音乐：音乐播放时播报短信
 * 13.TC_ST16081569078:蓝牙音乐：音乐播放时切换蓝牙功能界面
 * 1.TC_ST16081569048:蓝牙音乐：切换倒车打断蓝牙音乐暂停状态
 * 2.TC_ST16081569047:蓝牙音乐：切换蓝牙功能界面不打断蓝牙音乐暂停状态
 * 3.TC_ST16081569046:蓝牙音乐：切换FM后返回蓝牙音乐打断音乐暂停状态
 * 4.TC_ST16081568980:蓝牙音乐播放时车机端激活随机模式
 *   TC_ST16081568983:蓝牙音乐播放时车机端激活单曲循环模式
 *   TC_ST16081568984:蓝牙音乐播放时车机端激活全部循环模式
 * 5.TC_ST17031090858:蓝牙音乐播放时切换wifi开关
 * 6.TC_ST17031090857:蓝牙音乐播放时修改系统语言
 * 7.TC_ST16081568963:启动蓝牙音乐
 *   TC_ST16081569070:启动蓝牙音乐的两种方式。
 * 8.TC_ST16081568964:蓝牙音乐检查ID3
 * 9.TC_ST16081568965:蓝牙音乐暂停
 *   TC_ST16081568967:蓝牙音乐播放
 * 10.TC_ST16081568969:蓝牙音乐下一曲
 *    TC_ST16081568970:蓝牙音乐上一曲
 * 11.TC_ST16081569076:蓝牙音乐播放时锁屏
 * 12.TC_ST16081569077:蓝牙音乐暂停时锁屏
 * */
public class MusicTest extends UiAutomatorTestCase {
	//调用自动运行cmd命令
	public static void main(String[] args) throws IOException {  
		 
		RunTestCase runTestCase=new RunTestCase("AutoTest",  
				 "com.bt.music.MusicTest", "", "3");  
		runTestCase.setDebug(false);  
		String logPathStr = runTestCase.runUiautomator();//返回文件路径
		new LogUtil().analyseLog(logPathStr);//分析日志
	} 
	//输出版本信息
	public static void CaseInfo(){
		System.out.println("=================Bt Music cases===================");
	}
    
	BtMusicOperate btMusicOperate = new BtMusicOperate();
	BtPhoneOperate btPhoneOperate = new BtPhoneOperate();
	HomePage homePage = new HomePage();
	
	/**
	 * setUp每个case执行之前都会执行
	 * */
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		try {
			UiDevice.getInstance().wakeUp();
			Utils.clickConfirmWatcher();
			CaseInfo();
			homePage.goBackHome();
			Utils.logPrint("setUp():wakup-UiWatcher-backhome");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
		UiDevice.getInstance().removeWatcher("stopRunning"); //移除监视器
		Utils.logPrint("tearDown():stop UiWatcher");
	}

	
	
	
	/**
	 * 拨打不同长度的电话号码
	 * 21.TC_ST16081569828,拨打一位号码，拨号无效
	 * 22.TC_ST16081569829,三位号码
	 * 23.TC_ST16081569830,7位	  
	 * 24.TC_ST16081569831,11位
	 * 25.TC_ST16081569832,15
	 * 26.TC_ST16081569833,30
	 * @Date 2017-05-05
	 */
	public void testBtDialLongAndShortNumber() {
		boolean testPass = false;
		final String caseStr = "TC_ST16081569828,TC_ST16081569829,TC_ST16081569830,TC_ST16081569831,TC_ST16081569832,TC_ST16081569833:拨打不同长度的电话号码";
		Utils.logPrint("拨打特殊字符的电话号码");
		testPass = btPhoneOperate.btDialLongAndShortNumber();
		
		assertEquals(caseStr, true, testPass);
	}
	
	/**
	 * 拨打特殊字符的电话号码
	 * 18.TC_ST16081569834//输入一串字符数字，以数字开头
	 * 19.TC_ST16081569835//输入一串字符*#
	 * 20.TC_ST16081569836//输入一串字符数字，以字符开头
	 * @Date 2017-05-05
	 */
	public void testBtDialAbnormalNumber() {
		boolean testPass = false;
		final String caseStr = "TC_ST16081569834,TC_ST16081569835,TC_ST16081569836:拨打特殊字符的电话号码";
		Utils.logPrint("TC_ST16081569834:拨打特殊字符的电话号码");
		Utils.logPrint("TC_ST16081569835:拨打特殊字符的电话号码");
		Utils.logPrint("TC_ST16081569836:拨打特殊字符的电话号码");
		testPass = btPhoneOperate.btDialAbnormalNumber();
		assertEquals(caseStr, true, testPass);
	}	
	
	/**
	 * 17.TC_ST16081569110:蓝牙音乐：音乐播放时重启开关
	 * @Date 2017-04-28
	 */
	public void testTurnOffOnBtWhileBtPlaying() {
		boolean testPass = false;
		final String caseStr = "TC_ST16081569110:蓝牙音乐：音乐播放时重启开关";
		Utils.logPrint(caseStr);
		testPass = btMusicOperate.turnOffOnBtWhileBtPlaying();
		assertEquals(caseStr, true, testPass);
	}
	
	/**
	 * 16.TC_ST16081569109:蓝牙音乐：音乐播放时关闭蓝牙开关
	 * @Date 2017-04-28
	 */
	public void testTurnOffBtWhileBtPlaying() {
		boolean testPass = false;
		final String caseStr = "TC_ST16081569109:蓝牙音乐：音乐播放时关闭蓝牙开关";
		Utils.logPrint(caseStr);
		testPass = btMusicOperate.turnOffBtWhileBtPlaying();
		assertEquals(caseStr, true, testPass);
	}
	
	/**
	 * 15.TC_ST16081569096:蓝牙音乐：音乐播放时车机端拨号
	 * @Date 2017-04-28
	 */
	public void testDialWhileBtMusicPlaying() {
		boolean testPass = false;
		final String caseStr = "TC_ST16081569096:蓝牙音乐：音乐播放时车机端拨号";
		Utils.logPrint(caseStr);
		testPass = btMusicOperate.dialWhileBtMusicPlaying();
		assertEquals(caseStr, true, testPass);
	}
	
	/**
	 * 14.TC_ST16081569088:蓝牙音乐：音乐播放时播报短信
	 * @Date 2017-04-27
	 */
	public void testSmsVoiceBtMusicPlaying() {
		boolean testPass = false;
		final String caseStr = "TC_ST16081569088:蓝牙音乐：音乐播放时播报短信";
		Utils.logPrint(caseStr);
		testPass = btMusicOperate.smsVoiceBtMusicPlaying();
		assertEquals(caseStr, true, testPass);
	}
	
	/**
	 * 13.TC_ST16081569078:蓝牙音乐：音乐播放时切换蓝牙功能界面
	 * @Date 2017-04-27
	 */
	public void testSwitchBtFunctionCheckMusicPlay() {
		boolean testPass = false;
		final String caseStr = "TC_ST16081569078:蓝牙音乐：音乐播放时切换蓝牙功能界面";
		Utils.logPrint(caseStr);
		testPass = btMusicOperate.switchBtFunctionCheckMusicPlay();
		assertEquals(caseStr, true, testPass);
	}
	
	/**
	 * 1.TC_ST16081569048:蓝牙音乐：切换倒车打断蓝牙音乐暂停状态
	 * @Date 2017-04-27
	 */
	public void testRvcBtMusicKeepPause() {
		boolean testPass = false;
		Utils.logPrint("TC_ST16081569048:蓝牙音乐：切换倒车不打断蓝牙音乐暂停状态");
		testPass = btMusicOperate.rvcBtMusicKeepPause();
		assertEquals("切换倒车不打断蓝牙音乐暂停状态：", true, testPass);
	}
	/**
	 * 2.TC_ST16081569047:蓝牙音乐：切换蓝牙功能界面不打断蓝牙音乐暂停状态
	 * @Date 2017-04-27
	 */
	public void testSwitchBtFunctionCheckMusicPause() {
		boolean testPass = false;
		Utils.logPrint("TC_ST16081569047:蓝牙音乐：切换蓝牙功能界面不打断蓝牙音乐暂停状态");
		testPass = btMusicOperate.switchBtFunctionCheckMusicPause();
		assertEquals("切换蓝牙功能界面不打断蓝牙音乐暂停状态：", true, testPass);
	}
	
	/**
	 * 3.TC_ST16081569046:蓝牙音乐：切换FM后返回蓝牙音乐打断音乐暂停状态
	 * Date:20170427 
	 * */
	public void testSwitchFmBtMusicPauseInterrupt() {
		boolean testPass = false;
		Utils.logPrint("TC_ST16081569046:蓝牙音乐：切换FM后返回蓝牙音乐打断音乐暂停状态");
		testPass = btMusicOperate.switchFmBtMusicPauseInterrupt();
		assertEquals("切换FM后返回蓝牙音乐打断音乐暂停状态：", true, testPass);
	}
	
	/**
	 * 4.TC_ST16081568980:蓝牙音乐播放时车机端激活随机模式
	 *   TC_ST16081568983:蓝牙音乐播放时车机端激活单曲循环模式
	 *   TC_ST16081568984:蓝牙音乐播放时车机端激活全部循环模式
	 * */
	public void testBtChangeToShuffleMode() {
		boolean testPass = false;
		Utils.logPrint("TC_ST16081568980,TC_ST16081568983,TC_ST16081568984蓝牙音乐播放时车机端改变播放模式");
		testPass = btMusicOperate.changeToShuffleMode();
		assertEquals("蓝牙音乐播放时车机端改变播放模式：", true, testPass);
	}
	/**
	 * 5.TC_ST17031090858：蓝牙音乐播放时切换wifi开关
	 * */
	public void testSwitchWifiWhenBtPlaying(){
		boolean isPlaying = false;
		Utils.logPrint("TC_ST17031090858：蓝牙音乐播放时切换wifi开关");
		isPlaying = btMusicOperate.switchWifiWhenBtPlaying();
		assertEquals("蓝牙音乐播放时切换wifi开关结果：", true, isPlaying);
	}
	
	/**
	 * 6.TC_ST17031090857：蓝牙音乐播放时修改系统语言
	 * */
	public void testChangeLanguageWhenBtPlaying(){
		boolean isPlaying = false;
		Utils.logPrint("TC_ST17031090857：蓝牙音乐播放时改变系统语言");
		isPlaying = btMusicOperate.changeLanguageWhenBtPlaying();
		assertEquals("蓝牙音乐播放时修改系统语言结果：", true, isPlaying);
	}
	
	/**
	 * 7.TC_ST16081568963,TC_ST16081569070:启动蓝牙音乐的两种方式
	 * */
	public void testIntoBtMusicPlay(){
		Utils.logPrint("TC_ST16081568963,TC_ST16081569070:启动蓝牙音乐的两种方式。");
		boolean isPlaying = btMusicOperate.intoBtMusicAutoPlaying();
		assertEquals("进入蓝牙音乐结果：", true, isPlaying);
	}
	
	/**
	 * 8.TC_ST16081568964:检查ID3
	 * */
	public void testCheckID3() {
		Utils.logPrint("TC_ST16081568964：蓝牙音乐检查ID3");
		boolean testResult = btMusicOperate.checkID3BtMusic();
		assertEquals("检查ID3结果：", true, testResult);
	}
	/**
	 * 9.TC_ST16081568965,TC_ST16081568967：蓝牙音乐暂停/播放
	 * */
	public void testBtMusicPausePlay() {
		boolean testResult = false;
		Utils.logPrint("TC_ST16081568965,TC_ST16081568967：蓝牙音乐暂停/播放");
		testResult = btMusicOperate.btMusicPausePlay();
		assertEquals("蓝牙音乐暂停播放：", true, testResult);
	}
	/**
	 * 10.TC_ST16081568969,TC_ST16081568970：蓝牙音乐下一曲上一曲
	 * */
	public void testBtMusicNextPrevious() {
		Utils.logPrint("TC_ST16081568969,TC_ST16081568970：蓝牙音乐下一曲上一曲");
		boolean testResult = btMusicOperate.btMusicNextPrevious();
		assertEquals("蓝牙音乐下一曲上一曲：", true, testResult);
	}
	/**
	 * 11.TC_ST16081569076：蓝牙音乐播放时锁屏
	 * */
	public void testLockScreenWhenBtPlaying() {
		Utils.logPrint("TC_ST16081569076：蓝牙音乐播放时锁屏");
		boolean testResult = btMusicOperate.lockScreenWhenBtPlaying();
		assertEquals("蓝牙音乐播放时锁屏并解锁：", true, testResult);
	}
	/**
	 * 12.TC_ST16081569077：蓝牙音乐暂停时锁屏
	 * */
	public void testLockScreenWhenBtPausing() {
		Utils.logPrint("TC_ST16081569077：蓝牙音乐暂停时锁屏");
		boolean testResult = btMusicOperate.lockScreenWhenBtPlaying();
		assertEquals("蓝牙音乐暂停时锁屏并解锁：", true, testResult);
	}
}






