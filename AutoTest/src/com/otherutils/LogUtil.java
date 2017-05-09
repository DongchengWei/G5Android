/**
 * 调试文件，未用到
 * **/

package com.otherutils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
	
//	public String makeLogDir() {
//		String startTestTimeStr = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
//		File outputLogsFile = new File("/sdcard/" + startTestTimeStr);
//		if (! outputLogsFile.exists()) {
//			outputLogsFile.mkdirs();
//		}
//		String outputlogPathStr = "/sdcard/" + startTestTimeStr + "/UaLogs.txt";
//			
//		try {
//			PrintStream ps = new PrintStream(new FileOutputStream(outputlogPathStr));
//			System.setOut(ps); 
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}  
//		
//		return outputlogPathStr;
//	}
	
	//从输出的日志中分析结果
	public void analyseLog(String logPathStr) {
		//存放结果
//		List<String> devicesList = Collections.synchronizedList(new ArrayList<String>());
		File file=new File(logPathStr + "/UaLogs.txt");//源日志文件
		String testResultStr = "";
		String costTimeStr = "";
		String caseCounterStr = "0";
        try {
            FileReader fr=new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);
            String lineStr = "";
            while((lineStr = bf.readLine())!= null )
            {
            	  if (lineStr.startsWith("INSTRUMENTATION_STATUS: test=")) {  
                      saveToFile("运行用例名称："+getTest(lineStr), logPathStr + "/report.log", false);  
                  }  
            	  if (lineStr.startsWith("junit.framework.AssertionFailedError")) {  
            		  saveToFile("断言错误信息："+lineStr, logPathStr + "/report.log", false);
            	  }  
                  if (lineStr.startsWith("INSTRUMENTATION_STATUS: current")) {  
                	  caseCounterStr = getCurrent(lineStr);
                      saveToFile("正在运行第" + caseCounterStr + "个用例！", logPathStr + "/report.log", false);
                  }
                  if (lineStr.startsWith("Time:")) {
                	  costTimeStr = lineStr;
                  }
                  if (lineStr.startsWith("Tests run:")) {
                	  testResultStr = lineStr;
                  }
                  if (lineStr.startsWith("INSTRUMENTATION_STATUS_CODE:")) {  
                      if (getCode(lineStr).equalsIgnoreCase("-1")) {  
                          saveToFile("\n"+"---------------运行状态：运行错误！"+"\n", logPathStr + "/report.log", false);  
                      }else if (getCode(lineStr).equalsIgnoreCase("-2")) {  
                          saveToFile("\n"+"---------------运行状态：断言错误！"+"\n", logPathStr + "/report.log", false);  
                      }else {  
                          saveToFile("运行状态：运行OK！", logPathStr + "/report.log", false);  
                      }  
                  }  
            }
            if (testResultStr.equals("")) {
            	testResultStr = "Tests run: " + caseCounterStr + ",  Failures: 0,  Errors: 0";
            }
            String resultStr = testResultStr + "\r\n" + costTimeStr + "s";
            saveToFile("本次测试结果：" + resultStr, logPathStr + "/report.log", true);
            System.out.println("Log analyse finished!");
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File reader出错");
        }
	}
	
	/**
	 * 保存到文件
	 * 参数： 内容，路径，保存后是否关闭文件
	 * */
	public void saveToFile(String text,String path,boolean isClose) {
        File file=new File(path);       
        BufferedWriter bf=null;
        try {
            FileOutputStream outputStream=new FileOutputStream(file,true);
            OutputStreamWriter outWriter=new OutputStreamWriter(outputStream);
            bf=new BufferedWriter(outWriter);
            bf.append(text);
            bf.newLine();
            bf.flush();

            if(isClose){
                bf.close();
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
	
	//获取有用信息的方法
    public String getTest(String text) {  
        return text.substring(29, text.length());  
    }  
    public String getCode(String text) {  
        return text.substring(29, text.length());  
    }  
    public String getCurrent(String text) {  
        return text.substring(32, text.length());  
    }  
    public String getNow() {//获取当前时间  
        Date time = new Date();  
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String c = now.format(time);  
        return c;  
        }  
}
