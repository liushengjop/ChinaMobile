package com.my898tel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

import android.content.Context;
import android.os.Environment;

public class Util_file {

	public static void saveIsRunApplication(Context context, String flag) {
		File dir = setMkdir(context);

		File isRunFile = new File(dir, "isRunApplication.txt");
			
		OutputStreamWriter osw = null;

		try {
			osw = new OutputStreamWriter(new FileOutputStream(isRunFile),
					"UTF-8");
			osw.write(flag);
			osw.flush();
			osw.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static String getIsRunApplication(Context context){
		String filecontent = "true";
		
		File dir = setMkdir(context);

		File isRunFile = new File(dir, "isRunApplication.txt");
		
		  FileInputStream fis = null;   
		  CharBuffer cb;   
		  InputStreamReader isr;   
	        try {   
	            fis = new FileInputStream(isRunFile);  
	            cb = CharBuffer.allocate(fis.available());   
	            isr = new InputStreamReader(fis, "utf-8");   
	                if (cb != null) {   
	                   isr.read(cb);   
	                }   
	                filecontent = new String(cb.array());   
	                isr.close();   
	            
	        } catch (Exception e1) {   
	            e1.printStackTrace();   
	            return filecontent;   
	        }   
	  
	        
	        return filecontent.toString();
	}

	
	   /** 
     * 检验SDcard状态 
     * @return boolean 
     */  
    public static boolean checkSDCard()  
    {  
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))  
        {  
            return true;  
        }else{  
            return false;  
        }  
    }  
    /** 
     * 保存文件文件到目录 
     * @param context 
     * @return  文件保存的目录 
     */  
    public static File setMkdir(Context context)  
    {  
        String filePath;  
        if(checkSDCard())  
        {  
            filePath = Environment.getExternalStorageDirectory()+File.separator+context.getPackageName();  
        }else{  
            filePath = context.getCacheDir().getAbsolutePath()+File.separator+context.getPackageName();  
        }  
        File file = new File(filePath);  
        if(!file.exists())  
        {  
            boolean b = file.mkdirs();  
        }  
        return file;  
    }  
}
