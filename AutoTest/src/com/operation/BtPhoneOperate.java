/**
 * @Author：Tonsen
 * @Email ：Dongcheng.Wei@desay-svautomotive.com
 * @Date  ：2017-05-04
 */
package com.operation;

import java.io.IOException;


import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.otherutils.Utils;
import com.pageutil.BtTabPage;
import com.pageutil.DialpadPage;
import com.pageutil.HardKeyAction;
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

/** 
* @author 作者 E-mail: Dongcheng.Wei@desay-svautomotive.com
* @version 创建时间：2017年5月4日 上午11:55:04 
* 类说明 : 蓝牙电话相关的case
*/
/**
 * @author uidq0460
 *
 */
public class BtPhoneOperate extends UiAutomatorTestCase {
	
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
				 "com.operation.BtPhoneOperate", "", "3");  
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
	 * uiautomator runtest AutoTest.jar -c com.operation.BtPhoneOperate &
	 * */
	public void testDebug(){
		
		try {
			UiDevice.getInstance().wakeUp();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		boolean isOk = false;
		isOk = recall();
		
//		String currentTestStr = Utils.getCurrentMethodName();//当前方法名
//		//fail则打mark,截取log,并保存log到指定位置
//		Utils.markAndSaveLogs(isOk, Utils.failStepStr, currentTestStr);//以方法名命名case文件夹
		assertEquals("测试结果：", true, isOk);
	}
	
	/**
	 * 蓝牙拨号：车机重拨功能
	 * TC_ST16081569837
	 * @return
	 * @Date 2017-05-05
	 */
	public boolean recall() {
		boolean isOk = false;
		String inputStr = "10086";//待输入字符串
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			navBarPage.clickPhone();
			phonePage.intoDialPad();
			
			if (dialpadPage.dialAndEndImmediately(inputStr)) {
				if (dialpadPage.dialStart()) {//第一次点击拨号会输入最近号码
					String textStrNow = dialpadPage.getInputNumbers();
					if (textStrNow.equals(inputStr)) {//最近拨打的号码应该是10086
						if (dialpadPage.dialStart()) {//重拨
							if (dialpadPage.isOnDial()) {//进入正在拨号界面
								isOk = true;
								dialpadPage.endCall();
							}
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 输入号码后按音乐硬按键再进入拨号，输入框被清空，重新输入新数字
	 * TC_ST16081569815
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean inputNumPressHardkeyMusic() {
		boolean isOk = false;
		String inputStr = "5";//待输入字符串
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			dialpadPage.inputNumbers(inputStr);
			String textStr = dialpadPage.getInputNumbers();
			if (textStr.equals(inputStr)) {//判断输入的是否正确
				HardKeyAction.pressMusic();
				if (mediaPage.SourceJudge() == MediaPage.FMAMTITLE) {
					navBarPage.clickPhone();
					phonePage.intoDialPad();
					String textStrNow = dialpadPage.getInputNumbers();
					if (textStrNow==null || textStrNow.equals("")) {
						dialpadPage.inputNumbers(inputStr);
						if (inputStr.equals(dialpadPage.getInputNumbers())) {
							isOk = true;
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 输入号码后按返回然后切换到蓝牙音乐播放再进入拨号，输入框被清空，重新输入新数字
	 * TC_ST16081569814
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean inputNumSwitchBtMusic() {
		boolean isOk = false;
		String inputStr = "5";//待输入字符串
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			dialpadPage.inputNumbers(inputStr);
			String textStr = dialpadPage.getInputNumbers();
			if (textStr.equals(inputStr)) {//判断输入的是否正确
				UiDevice.getInstance().pressBack();
				navBarPage.clickMedia();
				SystemClock.sleep(1000);
				mediaPage.intoBtMusic();
				if (mediaPage.isPlaying()) {
					navBarPage.clickPhone();
					phonePage.intoDialPad();
					String textStrNow = dialpadPage.getInputNumbers();
					if (textStrNow==null || textStrNow.equals("")) {
						dialpadPage.inputNumbers(inputStr);
						if (inputStr.equals(dialpadPage.getInputNumbers())) {
							isOk = true;
						}
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 输入号码后切换到短信界面再进入拨号，输入框被清空，重新输入新数字
	 * TC_ST16081569812
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean inputNumSwitchSms() {
		boolean isOk = false;
		String inputStr = "5";//待输入字符串
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			dialpadPage.inputNumbers(inputStr);
			String textStr = dialpadPage.getInputNumbers();
			if (textStr.equals(inputStr)) {//判断输入的是否正确
				navBarPage.clickPhone();
				phonePage.intoMessaging();
				navBarPage.clickPhone();
				phonePage.intoDialPad();
				String textStrNow = dialpadPage.getInputNumbers();
				if (textStrNow==null || textStrNow.equals("")) {
					dialpadPage.inputNumbers(inputStr);
					if (inputStr.equals(dialpadPage.getInputNumbers())) {
						isOk = true;
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 输入号码后切换到通话记录界面再进入拨号，输入框被清空，重新输入新数字
	 * TC_ST16081569811
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean inputNumSwitchRecent() {
		boolean isOk = false;
		String inputStr = "5";//待输入字符串
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			dialpadPage.inputNumbers(inputStr);
			String textStr = dialpadPage.getInputNumbers();
			if (textStr.equals(inputStr)) {//判断输入的是否正确
				navBarPage.clickPhone();
				phonePage.intoRecents();
				navBarPage.clickPhone();
				phonePage.intoDialPad();
				String textStrNow = dialpadPage.getInputNumbers();
				if (textStrNow==null || textStrNow.equals("")) {
					dialpadPage.inputNumbers(inputStr);
					if (inputStr.equals(dialpadPage.getInputNumbers())) {
						isOk = true;
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 输入号码后切换到联系人界面再进入拨号，输入框被清空，重新输入新数字
	 * TC_ST16081569810
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean inputNumSwitcContact() {
		boolean isOk = false;
		String inputStr = "5";//待输入字符串
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			dialpadPage.inputNumbers(inputStr);
			String textStr = dialpadPage.getInputNumbers();
			if (textStr.equals(inputStr)) {//判断输入的是否正确
				navBarPage.clickPhone();
				phonePage.intoContact();
				navBarPage.clickPhone();
				phonePage.intoDialPad();
				String textStrNow = dialpadPage.getInputNumbers();
				if (textStrNow==null || textStrNow.equals("")) {
					dialpadPage.inputNumbers(inputStr);
					if (inputStr.equals(dialpadPage.getInputNumbers())) {
						isOk = true;
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 输入号码后按返回键再进入，输入框被清空，重新输入新数字
	 * TC_ST16081569809
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean inputNumPressBack() {
		boolean isOk = false;
		String inputStr = "5";//待输入字符串
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			dialpadPage.inputNumbers(inputStr);
			String textStr = dialpadPage.getInputNumbers();
			if (textStr.equals(inputStr)) {//判断输入的是否正确
				UiDevice.getInstance().pressBack();
				phonePage.intoDialPad();
				String textStrNow = dialpadPage.getInputNumbers();
				if (textStrNow==null || textStrNow.equals("")) {
					dialpadPage.inputNumbers(inputStr);
					if (inputStr.equals(dialpadPage.getInputNumbers())) {
						isOk = true;
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 拨打不同长度的电话号码
	 * TC_ST16081569828,拨打一位号码，拨号无效
	 * TC_ST16081569829,三位号码
	 * TC_ST16081569830,7位	  
	 * TC_ST16081569831,11位
	 * TC_ST16081569832,15
	 * TC_ST16081569833,30
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean btDialLongAndShortNumber() {
		boolean isOk = false;
		String dial1Str = "1";//TC_ST16081569828,拨打一位号码，拨号无效
		String dial123Str = "123";//TC_ST16081569829，三位号码	  
		String dialNum7Str = "1234567";//TC_ST16081569830，7位
		String dialNum11Str = "12345678901";//TC_ST16081569831，11位
		String dialNum15Str = "123456789012345";//TC_ST16081569832，15
		String dialNum30Str = "123456789012345678901234567890";//TC_ST16081569833,30
		String resultStr = "";
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			dialpadPage.dialANumber(dial1Str);
			resultStr = dial1Str;
			if (dialpadPage.waitInputGone(3000) == false) {
				resultStr = dial123Str;
				UiDevice.getInstance().pressBack();
				phonePage.intoDialPad();
				if (dialpadPage.dialAbnormalNumber(dial123Str)) {
					resultStr = dialNum7Str;
					if (dialpadPage.dialAbnormalNumber(dialNum7Str)) {
						resultStr = dialNum11Str;
						if (dialpadPage.dialAbnormalNumber(dialNum11Str)) {
							resultStr = dialNum15Str;
							dialpadPage.dialANumber(dialNum15Str);
							if (dialpadPage.waitInputGone(3000) == false) {
								UiDevice.getInstance().pressBack();
								phonePage.intoDialPad();
								resultStr = dialNum15Str;
								dialpadPage.dialANumber(dialNum30Str);
								if (dialpadPage.waitInputGone(3000) == false) {
									isOk = true;
								}
							}
						}
					}
				}
			}
			if (isOk == false) {
				Utils.logPrint("fail:" + resultStr);
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 拨打特殊字符的号码
	 * TC_ST16081569834//输入一串字符数字，以数字开头
	 * TC_ST16081569835//输入一串字符*#
	 * TC_ST16081569836//输入一串字符数字，以字符开头
	 * @return
	 * @Date 2017-05-04
	 */
	public boolean btDialAbnormalNumber() {
		boolean isOk = false;
		String dialNumStr = "*#10086";//输入一串字符数字，以字符开头
		String dialSsStr = "*#";	  //输入一串字符*#
		String dial10086sStr = "10086*#";//输入一串字符数字，以数字开头
		try {
			homePage.intoMultimedia();
			mediaPage.intoFmAmPage();
			homePage.goBackHome();
			homePage.intoPhone();
			phonePage.intoDialPad();
			if (dialpadPage.dialAbnormalNumber(dialNumStr)) {
				if (dialpadPage.dialAbnormalNumber(dialSsStr)) {
					if (dialpadPage.dialAbnormalNumber(dial10086sStr)) {
						isOk = true;
					}
				}
			}
		} catch (UiObjectNotFoundException e) {
			e.printStackTrace();
		}
		return isOk;
	}
}
