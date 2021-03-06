package com.otherutils;  
  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import android.graphics.Bitmap;  
import android.graphics.BitmapFactory;  
  
public class CaptrueUtil {  
    
	/**
	 * 函数功能：对比两个路径下的Bitmap截图是否一致
	 * 参数	  ：	path1:图片一的路径，path2图片2的路径
	 * 返回值：	boolean,对比结果相同则为true，否则为false
	 * */
    public static boolean sameAs (String path1, String path2) throws FileNotFoundException {  
        boolean res = false;  
        FileInputStream fis1 = new FileInputStream(path1);  
        Bitmap bitmap1  = BitmapFactory.decodeStream(fis1);  
          
        FileInputStream fis2 = new FileInputStream(path2);  
        Bitmap bitmap2  = BitmapFactory.decodeStream(fis2);  
          
        res = sameAs(bitmap1,bitmap2);  
      
        return res;  
              
    }  
     
	/**
	 * 函数功能：对比两个路径下的Bitmap截图相似度是否达到某个百分比
	 * 参数	  ：	path1:图片一的路径，path2图片2的路径，
	 * 			percent为百分比(0 - 1.0)
	 * 返回值：	boolean,对比结果相同则为true，否则为false
	 * */
    public static boolean sameAs (String path1, String path2,double percent) throws FileNotFoundException {  
        FileInputStream fis1 = new FileInputStream(path1);  
        Bitmap bitmap1  = BitmapFactory.decodeStream(fis1);  
          
        FileInputStream fis2 = new FileInputStream(path2);  
        Bitmap bitmap2  = BitmapFactory.decodeStream(fis2);  
          
        return sameAs(bitmap1,bitmap2,percent);  
              
    }  
     
	/**
	 * 函数功能：对比两个Bitmap截图相似度是否达到某个百分比
	 * 参数	  ：	bitmap1:图片一的路径，bitmap1图片2的路径，
	 * 			percent为百分比(0 - 1.0)
	 * 返回值：	boolean,对比结果相同则为true，否则为false
	 * */
    public static boolean sameAs (Bitmap bitmap1, Bitmap bitmap2, double percent) {  
        if(bitmap1.getHeight() != bitmap2.getHeight())  
            return false;  
          
        if(bitmap1.getWidth() != bitmap2.getWidth())  
            return false;  
          
        if(bitmap1.getConfig() != bitmap2.getConfig())  
            return false;  
  
        int width = bitmap1.getWidth();  
        int height = bitmap2.getHeight();  
  
        int numDiffPixels = 0;  
           
         for (int y = 0; y < height; y++) {  
           for (int x = 0; x < width; x++) {  
             if (bitmap1.getPixel(x, y) != bitmap2.getPixel(x, y)) {  
               numDiffPixels++;  
             }  
           }  
         }  
         double numberPixels = height * width;  
         double diffPercent = numDiffPixels / numberPixels;  
         return percent <= 1.0D - diffPercent;  
    }  
    
	/**
	 * 函数功能：对比两个Bitmap截图是否一致
	 * 参数	  ：	bitmap1:图片一的路径，bitmap1图片2的路径
	 * 返回值：	boolean,对比结果相同则为true，否则为false
	 * */
    public static boolean sameAs (Bitmap bmp1, Bitmap bmp2) throws FileNotFoundException {  
        boolean res = false;  
          
        res = bmp1.sameAs(bmp2);  
          
        return res;       
    }  
     
	/**
	 * 函数功能：从某个路径根据x,y坐标以及长宽高获取截图的子截图
	 * 参数	  ：	path:源截图的路径，xy为坐标，width和height为宽和高
	 * 返回值：	Bitmap返回子截图
	 * */
    public static Bitmap getSubImage(String path,int x,int y,int width,int height) throws FileNotFoundException {  
          
        FileInputStream fis = new FileInputStream(path);  
        Bitmap bitmap  = BitmapFactory.decodeStream(fis);  
                  
        Bitmap res = Bitmap.createBitmap(bitmap, x, y, width, height);  
          
        return res;  
          
    }  
}  