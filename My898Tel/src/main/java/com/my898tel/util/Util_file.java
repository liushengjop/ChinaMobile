package com.my898tel.util;

import java.io.ByteArrayOutputStream;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.my898tel.Config;
import com.my898tel.UIApplication;

public class Util_File {

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

    public static String getIsRunApplication(Context context) {
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
     *
     * @return boolean
     */
    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 保存文件文件到目录
     *
     * @param context
     * @return 文件保存的目录
     */
    public static File setMkdir(Context context) {
        String filePath;
        if (checkSDCard()) {
            filePath = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName();
        } else {
            filePath = context.getCacheDir().getAbsolutePath() + File.separator + context.getPackageName();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            boolean b = file.mkdirs();
        }
        return file;
    }


    private static String appFilePathInSDCard = null;// 当前应用所有文件存储的SDCard绝对路径（前提：如果文件系统定在SD卡，否则null）

    /**
     * 注：务必在MyApplication里调用
     * <p/>
     * 校对：如config设置文件保存在SD卡，但SD卡不存在情况下，强置bFileOperationInSDCard = false;
     */
    public static void checkFileOperationInSDCard() {
        if (Config.bFileOperationInSDCard && !isSDCardExit()) {
            Config.bFileOperationInSDCard = false;
        }
        if (Config.bFileOperationInSDCard && appFilePathInSDCard == null) {
            // 组织好路径
            appFilePathInSDCard = Config.fileOperationInSDCardPath;
            File file = new File(appFilePathInSDCard);
            if (!file.exists()) {
                file.mkdirs();
            }
            debug("appFilePathInSDCard:" + appFilePathInSDCard);
        }
    }

    public static String getAppFilePathInSDCard() {
        return appFilePathInSDCard;
    }

    /**
     * 检查SD卡是否存在
     */
    public static boolean isSDCardExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 手机系统文件：【data】
     *
     * @param fileName ：文件名，并非路径
     * @param MODE
     * @return
     */
    private static FileOutputStream getStreamFileOutput(String fileName, int MODE) {
        try {
            return UIApplication.getInstance().openFileOutput(fileName, MODE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 手机系统文件：【data】
     *
     * @param fileName ：文件名，并非路径
     * @return
     */
    private static FileInputStream getSystemFileInputStream(String fileName) {
        try {
            return UIApplication.getInstance().openFileInput(fileName);
        } catch (Exception e) {
            return null;
        }
    }

    private static String fileSavePath = null;

    private static File getCurProjectFileInSDCard(String fileName) {
        try {
            File path = new File(appFilePathInSDCard);
            if (!path.exists()) {
                path.mkdirs();
            }
            File file = new File(Util_G.strAddStr(appFilePathInSDCard, fileName));
            fileSavePath = null;
            fileSavePath = Util_G.strAddStr(appFilePathInSDCard, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFileSavePath() {

        return fileSavePath;
    }

    /**
     * 手机SD卡文件：【SD CARD】
     *
     * @param fileName ：文件名，并非路径
     * @param MODE
     * @return
     */
    private static FileOutputStream getSDCardFileOutput(String fileName, int MODE) {
        try {
            return new FileOutputStream(getCurProjectFileInSDCard(fileName), MODE == Context.MODE_APPEND ? true : false);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 手机SD卡文件：【SD CARD】
     *
     * @param fileName ：文件名，并非路径
     * @return
     */
    private static FileInputStream getSDCardFileInputStream(String fileName) {

        try {
            return new FileInputStream(getCurProjectFileInSDCard(fileName));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param data     : byte[]
     * @param fileName : 文件名称，并不是文件路径，不能包含路径分隔符“/” ，如果文件不存在，Android
     *                 会自动创建它。创建的文件保存在/data/data/<package
     *                 title>/files目录，真机需要root才能看到；
     * @param MODE     : Context.MODE_PRIVATE = 0 Context.MODE_APPEND = 32768 :
     *                 系统、SDCard 都适用； Context.MODE_WORLD_READABLE = 1
     *                 Context.MODE_WORLD_WRITEABLE = 2
     */
    public static boolean writeFile(byte[] data, String fileName, int MODE) {

        if (data == null) return false;
        FileOutputStream outStream = null;
        try {
            if (Config.bFileOperationInSDCard) {
                outStream = getSDCardFileOutput(fileName, MODE);
            }
            outStream.write(data);
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (outStream != null) outStream.close();
            } catch (Exception e) {
            }
        }
        return true;
    }

//    public static boolean writeJsonFile(Object data, String fileName, int MODE) {
//        if (data == null) return false;
//        FileOutputStream outStream = null;
//        try {
//            if (Config.bFileOperationInSDCard) {
//                outStream = getSDCardFileOutput(fileName, MODE);
//            }
//            Gson mGson = new Gson();
//            outStream.write(mGson.toJson(data).getBytes());
//        } catch (Exception e) {
//            return false;
//        } finally {
//            try {
//                if (outStream != null) outStream.close();
//            } catch (Exception e) {
//            }
//        }
//        return true;
//    }

    public static boolean writeFileByPath(byte[] data, String fileName_Path, int MODE) {

        if (data == null) return false;
        FileOutputStream outStream = null;
        try {
            File file = new File(fileName_Path);
            // System.out.println("fileName_Path-->>"+fileName_Path);
            if (!file.exists()) {
                file.createNewFile();
            }
            outStream = new FileOutputStream(file, MODE == Context.MODE_APPEND ? true : false);
            outStream.write(data);
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (outStream != null) outStream.close();
            } catch (Exception e) {
            }
        }
        return true;
    }

    /**
     * @param data     : String
     * @param fileName : 文件名称，不能包含路径分隔符“/” ，如果文件不存在，Android
     *                 会自动创建它。创建的文件保存在/data/data/<package title>/files目录
     * @param MODE     : Context.MODE_PRIVATE = 0 Context.MODE_APPEND = 32768
     *                 Context.MODE_WORLD_READABLE = 1 Context.MODE_WORLD_WRITEABLE =
     *                 2
     */
    public static boolean writeFile(String data, String fileName, int MODE) {
        // Util_G.debug("writeFile(), fileName="+fileName+", "+"content="+data);
        return writeFile(Util_G.utf8Encode(data), fileName, MODE);
    }

    /**
     * @param fileName ：文件名，并非路径
     * @return
     */
    public static byte[] readFile(String fileName) {

        byte[] d = null;
        FileInputStream inStream = null;
        try {
            if (Config.bFileOperationInSDCard) {
                inStream = getSDCardFileInputStream(fileName);
            } else {
                inStream = getSystemFileInputStream(fileName);
            }

            d = Util_G.getByteArrayFromInputstream(inStream, -1);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (inStream != null) inStream.close();
            } catch (Exception e) {
            }
        }
        return d;
    }

    /**
     * UTF-8格式
     *
     * @param fileName ：文件名，并非路径
     * @return
     */
    public static String readFile2String(String fileName) {
        return Util_G.utf8Decode(readFile(fileName));
    }

    private static void debug(String str) {
        System.out.println(str);
    }
    /**
     * @param fileName ：文件名，并非路径
     * @return
     */
    public static byte[] readImage(String fileName, int imgLen) {

        byte[] d = null;
        FileInputStream inStream = null;
        try {
            // if(Config.bImagesOperationInSDCard){
            inStream = new FileInputStream(fileName);
            ;
            // }else{
            // inStream = getSystemFileInputStream(fileName);
            // }
            d = Util_G.getByteArrayFromInputstream(inStream, imgLen + 100);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (inStream != null) inStream.close();
            } catch (Exception e) {
            }
        }
        return d;
    }

    /**
     * 直接保存bitmap
     *
     * @param bitName
     * @param mBitmap
     */
    public static void saveMyBitmap(String bitName, Bitmap mBitmap) {
        // mBitmap.get
        if (isSDCardExit()) {
            File file = new File(Config.fileOperationInSDCardPath + bitName + Config.IMAGE_FORMATE);
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deletefile(String delpath) throws FileNotFoundException, IOException {
        try {
            File file = new File(delpath);
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\ " + filelist[i]);
                    if (!delfile.isDirectory()) delfile.delete();
                    else if (delfile.isDirectory()) deletefile(delpath + "\\ " + filelist[i]);
                }
                file.delete();
            }
        } catch (FileNotFoundException e) {
        }
    }

    /**
     * 删除文件夹
     *
     * @param folderPath String 文件夹路径及名称 如c:/fqf
     *                   String
     * @return boolean
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 删除文件夹里面的所有文件
     *
     * @param path String 文件夹路径 如 c:/fqf
     */
    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }

    /**
     * @return
     */
    public static String getAssetsFileString(String fileName) {
        try {
            InputStream inputStream = UIApplication.getInstance().getAssets().open(fileName);

            return readTextFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * *
     *
     * @param inputStream
     * @return
     */
    public static String readTextFile(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        }
        return outputStream.toString();
    }
}
