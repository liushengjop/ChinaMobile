package com.my898tel.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.my898tel.UIApplication;

public class Util_G {
    public static void debug(int data) {
        System.out.println(data);
    }

    public static void debug(Class<?> clas, String message) {
        // TODO Auto-generated method stub
        Log.d(clas.getSimpleName(), message);
    }

    private static Toast myToast = null;

    /**
     * 显示Toast
     */
    public static void DisplayToast_(String str, int length) {
        // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();//会累计
        if (myToast == null) {
            myToast = new Toast(UIApplication.getInstance());
        }
        myToast.setDuration(length);
        myToast.show();
        // 实例化一个Toast对象
    }

    public static void DisplayToast(int resId, int length) {
        // TODO Auto-generated method stub
        if (myToast == null) {
            myToast = new Toast(UIApplication.getInstance());
        }
        myToast.setText(UIApplication.getInstance().getString(resId));
        myToast.setDuration(length);
        myToast.show();
    }

    /**
     * 获取资源文件的字符串
     */
    public static String getString(int resId) {
        return UIApplication.getInstance().getResources().getString(resId);
    }

    /**
     * @param path        图片路径
     * @param screenWidth 规定宽度
     * @param newHeight   规定高度
     * @param quality     压缩质量
     */
    public static byte[] CompressBitmap(String path, int maxWidth, int maxHeight, int quality) {
        // get the picture from location
        Bitmap bitmap = null;
        try {
            /*-----长宽---*/
            BitmapFactory.Options opts = new BitmapFactory.Options();
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            int desWidth = 0;
            int desHeight = 0;
            // 缩放比例
            double ratio = 0.0;
            if (maxWidth > 0 && maxHeight > 0) {// 固定宽高等比缩放
                if (srcWidth > srcHeight) {
                    ratio = srcWidth / maxWidth;
                    desWidth = maxWidth;
                    desHeight = (int) (srcHeight / ratio);
                } else {
                    ratio = srcHeight / maxHeight;
                    desHeight = maxHeight;
                    desWidth = (int) (srcWidth / ratio);
                }
                // 设置输出宽度、高度
                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inSampleSize = (int) (ratio) + 1;
                newOpts.inJustDecodeBounds = false;
                newOpts.outWidth = desWidth;
                newOpts.outHeight = desHeight;
                bitmap = BitmapFactory.decodeFile(path, newOpts);
            } else {
                if (maxWidth > 0 && maxWidth <= srcWidth) {
                    ratio = srcWidth / maxWidth;
                    desWidth = maxWidth;
                    desHeight = (int) ((maxWidth * srcHeight) / srcWidth);
                } else if (maxHeight > 0 && maxHeight <= srcHeight) {
                    ratio = srcHeight / maxHeight;
                    desHeight = maxHeight;
                    desWidth = (int) (maxHeight * srcWidth / srcHeight);
                }
                if ((maxWidth > 0 && srcWidth > maxWidth) || (maxHeight > 0 && srcHeight > maxHeight)) {
                    BitmapFactory.Options newOpts = new BitmapFactory.Options();
                    newOpts.inSampleSize = (int) (ratio) + 1;
                    newOpts.inJustDecodeBounds = false;
                    newOpts.outWidth = desWidth;
                    newOpts.outHeight = desHeight;
                    bitmap = BitmapFactory.decodeFile(path, newOpts);
                } else {
                    bitmap = BitmapFactory.decodeFile(path);
                }
            }

			/*-----质量---*/
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            Boolean didItWork = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
            if (didItWork) {
                final byte[] ba = outStream.toByteArray();
                outStream.close();
                return ba;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 图片截取
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap cutImage_(Bitmap bitmap, int screenWidth, int newHeight) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = screenWidth;
            if (width < screenWidth) {
                return bitmap;
            }
            // calculate the scale
            float scaleWidth = ((float) newWidth) / width;
            @SuppressWarnings("unused")
            float scaleHeight = ((float) newHeight) / height;
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the Bitmap
            // matrix.postScale(scaleWidth, scaleHeight);
            matrix.postScale(scaleWidth, scaleWidth);
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, newHeight, matrix, true);
            // Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
            // screenWidth, 100);
            return resizedBitmap;
        }
        return null;
    }

    /**
     * byte 转换为 kb MB
     */
    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP).floatValue();
        if (returnValue > 1)
            return (returnValue + "  MB ");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP).floatValue();
        return (returnValue + "  KB ");
    }

    /**
     * 加粗字体
     *
     * @param resId
     * @return
     */
    public static Spanned setTextColor(int resId, String... str) {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        for (String string : str) {
            sb.append(string);
        }
        return Html.fromHtml("<font color='#000000'>" + UIApplication.getInstance().getString(resId) + "</font>" + sb.toString());
    }

    /**
     * 加粗字体
     *
     * @param resId
     * @return
     */
    public static Spanned setFontBold(int resId, String... str) {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        for (String string : str) {
            sb.append(string);
        }
        return Html.fromHtml("<font color='#000000'><b><big>" + UIApplication.getInstance().getString(resId) + "</big></b></font>" + sb.toString());
    }

    /**
     * 加粗字体
     *
     * @param resId
     * @return
     */
    public static Spanned setFontBold(String str, String... str1) {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        for (String string : str1) {
            sb.append(string);
        }
        return Html.fromHtml("<font color='#000000'><b><big>" + str + "</big></b></font>" + sb.toString());
    }

    /**
     * 加粗字体
     *
     * @param resId
     * @return
     */
    public static Spanned setFontBold(String str) {
        // TODO Auto-generated method stub
        return Html.fromHtml("<font color='#000000'><b><big>" + str + "</big></b></font>");
    }

    /**
     * 加粗字体
     *
     * @param resId
     * @return
     */
    public static Spanned setFontBold(int resId) {
        // TODO Auto-generated method stub
        return Html.fromHtml("<font color='#000000'><b><big>" + UIApplication.getInstance().getString(resId) + "</big></b></font>");
    }

    public static String strAddStr(String... strings) {
        StringBuffer sb = new StringBuffer();
        for (String string : strings) {
            sb.append(string);
        }
        return sb.toString();
    }

    public static String strAddStr(int... strings) {
        StringBuffer sb = new StringBuffer();
        for (int string : strings) {
            sb.append(string);
        }
        return sb.toString();
    }

    public static byte[] stringArray2byteArray(String[] content) {

        if (content == null || content.length <= 0)
            return null;

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(byteOut);
        byte[] data = null;
        try {

            for (int i = 0, content_size = content.length; i < content_size; i++) {
                if (content[i] == null) {
                    dOut.writeUTF("null");
                } else {
                    dOut.writeUTF(content[i]);
                }
            }
            data = byteOut.toByteArray();
            dOut.close();
            byteOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return data;
    }

    public final static String utf8Decode(byte[] utf8_bytes) {
        try {
            return (new String(utf8_bytes, "UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }

    public final static byte[] utf8Encode(String utf8_str) {
        try {
            return (utf8_str.getBytes("UTF-8"));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param ins
     * @param MAXLEN 默认值-1
     * @return
     */
    public static byte[] getByteArrayFromInputstream(InputStream ins, int MAXLEN) {

        if (MAXLEN == -1)
            MAXLEN = 30000;

        try {

            byte[] charset = new byte[MAXLEN];
            int ch = ins.read();
            int length = 0;
            while (ch != -1) {
                charset[length] = (byte) ch;
                ch = ins.read();
                length++;
            }
            byte[] xmlCharArray = new byte[length];
            System.arraycopy(charset, 0, xmlCharArray, 0, length);

            return (xmlCharArray);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static String replace(String content, String olds, String news) {
        int index = 0;
        while (true) {
            index = content.indexOf(olds, index);
            if (index == -1)
                break;
            content = content.substring(0, index) + news + content.substring(index + olds.length());
            index += news.length();
        }
        return content;
    }

    public static int dip2px(float dipValue) {
        Context context = UIApplication.getInstance();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);

    }

    public static int px2dip(float pxValue) {
        Context context = UIApplication.getInstance();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Bitmap getBitmapFromID(int R_ID) {
        Bitmap bmp = ((BitmapDrawable) UIApplication.getInstance().getResources().getDrawable(R_ID)).getBitmap();
        return bmp;
    }

    public static Bitmap getBitmapFromID_1(int R_ID) {
        Bitmap bitmap = BitmapFactory.decodeResource(UIApplication.getInstance().getResources(), R_ID);
        return bitmap;
    }

    public static int getImageHeightFromID(int R_ID) {
        Bitmap bitmap = getBitmapFromID(R_ID);
        return bitmap.getHeight();
    }

    public static int getImageWeightFromID(int R_ID) {
        Bitmap bitmap = getBitmapFromID(R_ID);
        return bitmap.getWidth();
    }

    public static Bitmap getImageForBytes(byte[] btm) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(btm, 0, btm.length);
        return bitmap;

    }

    public static boolean isNullStr(String str) {
        if (str == null || str.length() <= 0)
            return true;
        return false;
    }

    public static String getDateTime() {
        Calendar calendar = Calendar.getInstance();

        String time = Util_G.strAddStr(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
        return time;
    }

    // 获取标准时间
    public static String getLoactionTime() {
        java.util.Date current = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(current);

    }

    // 获取标准时间
    public static String getLoactionTime(String str) {
        SimpleDateFormat sdfTimeb = new SimpleDateFormat("MM/dd EE HH:mm");
        SimpleDateFormat sdfTimeb1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdfTimeb1.parse(str);
            return sdfTimeb.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public static String getSwitchTime(String str) {
        SimpleDateFormat sdfTimeb = new SimpleDateFormat("yyyy'年'MM'月'dd'日'");
        SimpleDateFormat sdfTimeb1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdfTimeb1.parse(str);
            return sdfTimeb.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    // 获取标准时间
    public static String getSwitchTime01(String str) {
        SimpleDateFormat sdfTimeb = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat sdfTimeb1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = sdfTimeb1.parse(str);
            return sdfTimeb.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    private static URL url;
    private static HttpURLConnection con;
    private static int state = -1;

    /**
     * 功能：检测当前URL是否可连接或是否有效, 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
     *
     * @param urlStr 指定URL网络地址
     * @return URL
     */
    public synchronized static boolean isConnect(String urlStr) {
        int counts = 0;
        if (urlStr == null || urlStr.length() <= 0) {
            return false;
        }
        while (counts < 2) {
            try {
                url = new URL(urlStr);
                con = (HttpURLConnection) url.openConnection();
                state = con.getResponseCode();
                if (state == 200) {
                    return true;
                }
                break;
            } catch (Exception ex) {
                counts++;
                urlStr = null;
                continue;
            }
        }
        return false;
    }

    public static String getResult(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        jsonStr = jsonObject.getString("responseStatus");
        // Util_G.debug("responseStatus---"+jsonObject.toString());
        return jsonStr;
    }

    /**
     * 压缩图片，宽度固定
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap CompressBitmap_(String path, int w, int h, int quality) {
        try {
            Bitmap bitmapOrg = BitmapFactory.decodeFile(path);
            int srcWidth = bitmapOrg.getWidth();
            int srcHeight = bitmapOrg.getHeight();
            System.out.println("AFTER. Height: " + srcHeight + " Width: " + srcWidth);
            int newWidth = w;
            int newHeight = h;
            if (srcWidth >= w) {
                // calculate the scale
                float scaleWidth = ((float) newWidth) / srcWidth;
                @SuppressWarnings("unused")
                float scaleHeight = ((float) newHeight) / srcHeight;
                // create a matrix for the manipulation
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleWidth);
                bitmapOrg = Bitmap.createBitmap(bitmapOrg, 0, 0, srcWidth, srcHeight, matrix, true);
            }
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            Boolean didItWork = bitmapOrg.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
            if (didItWork) {
                System.out.println("AFTER. Height: " + bitmapOrg.getHeight() + " Width: " + bitmapOrg.getWidth());
                final byte[] ba = outStream.toByteArray();
                System.out.println("file--->>><<>" + bytes2kb(outStream.size()));
                outStream.close();
                return Util_G.getImageForBytes(ba);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片截取
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap cutImage(Bitmap bitmap, int screenWidth, int newHeight) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = screenWidth;
            if (width < screenWidth) {
                return bitmap;
            }
            // calculate the scale
            float scaleWidth = ((float) newWidth) / width;
            @SuppressWarnings("unused")
            float scaleHeight = ((float) newHeight) / height;
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the Bitmap
            // matrix.postScale(scaleWidth, scaleHeight);
            matrix.postScale(scaleWidth, scaleWidth);
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, newHeight, matrix, true);
            // Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
            // screenWidth, 100);
            return resizedBitmap;
        }
        return null;
    }

    /**
     * 压缩图片，宽度固定
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {

        // load the origial Bitmap
        Bitmap BitmapOrg = bitmap;
        if (BitmapOrg != null) {
            int width = BitmapOrg.getWidth();
            int height = BitmapOrg.getHeight();
            int newWidth = w;
            int newHeight = h;
            if (width <= w) {
                return bitmap;
            }
            // calculate the scale
            float scaleWidth = ((float) newWidth) / width;
            @SuppressWarnings("unused")
            float scaleHeight = ((float) newHeight) / height;
            // create a matrix for the manipulation
            Matrix matrix = new Matrix();
            // resize the Bitmap
            // matrix.postScale(scaleWidth, scaleHeight);
            matrix.postScale(scaleWidth, scaleWidth);
            // if you want to rotate the Bitmap
            // matrix.postRotate(45);
            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width, height, matrix, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
            // make a Drawable from Bitmap to allow to set the Bitmap
            // to the ImageView, ImageButton or what ever
            // return new BitmapDrawable(resizedBitmap);
            return resizedBitmap;
        }
        return null;
    }

    /**
     * 根据Uri字符串获取图片名字
     *
     * @param uri
     * @return
     */
    public static String getNameFromUri(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1, uri.length());
    }

    /* 根据自定义的名字获取定义在strings中的数据库 */
    public static String[] getRessouseArray(String stringArrayName) {
        String[] planets = null;
        try {
            int id = UIApplication.getInstance().getResources().getIdentifier(stringArrayName, "array", UIApplication.getInstance().getPackageName());
            planets = UIApplication.getInstance().getResources().getStringArray(id);
        } catch (Exception e) {
            System.out.println("get Array String error!!!");
        }
        return planets;

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static boolean isExtStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * get the version
     *
     * @param context
     * @return
     */
    public static String getVersion(Context context) {
        String version;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA).versionName;
        } catch (NameNotFoundException e) {
            version = "UnknownVersion";
        }
        return version;
    }

    @SuppressWarnings("resource")
    public static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    public static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * *
     * get the current sdk version
     *
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    /**
     * whether it is int
     *
     * @param strInt
     * @return
     */
    public static boolean isNumeric(String strInt) {
        if (strInt == null || strInt == "")
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(strInt);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 深拷贝 Model 需要实现Serializable接口，可以序列化
     * <p/>
     * 对拷贝后的引用的修改，还能影响原来的对象
     * <p/>
     * 对现在对象的修改不会影响原有的对象
     *
     * @param src
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("rawtypes")
    static public List deepCopy(List src) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(byteOut);
        out.writeObject(src);
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream in = new ObjectInputStream(byteIn);
        List dest = (List) in.readObject();
        return dest;
    }

    // 登录判断
    public static boolean isEmail(String email) {
        if (!email.contains("@")) {
            return false;
        }
        String str = email.substring(email.indexOf("@") + 1, email.length());
        if (str.contains(".")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 比较两个可能为null的字符串
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean nullEqual(String s1, String s2) {
        if (s1 == null)
            return TextUtils.isEmpty(s2);// s2="";
        else
            return s1.equals(s2);
    }

    /**
     * whether it is Chinese Character
     *
     * @param strInt
     * @return
     */
    public static boolean isChineseCharacter(String str) {
        Pattern pattern = Pattern.compile("^[\u4e00-\u9fa5]*$");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    /**
     * 快速点击 防止快速点击
     */
    private static long lastClickTime;

    /**
     * 快速点击 防止快速点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 队列比较
     *
     * @param <T>
     * @param a
     * @param b
     * @return
     */
    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    /**
     * This sets the maximum length in characters of an EditText view. Since the
     * max length must be done with a filter, this method gets the current
     * filters. If there is already a length filter in the view, it will replace
     * it, otherwise, it will add the max length filter preserving the other
     *
     * @param view
     * @param length
     */
    public static void setMaxLength(EditText view, int length) {
        InputFilter curFilters[];
        InputFilter.LengthFilter lengthFilter;
        int idx;
        lengthFilter = new InputFilter.LengthFilter(length);
        curFilters = view.getFilters();
        if (curFilters != null) {
            for (idx = 0; idx < curFilters.length; idx++) {
                if (curFilters[idx] instanceof InputFilter.LengthFilter) {
                    curFilters[idx] = lengthFilter;
                    return;
                }
            }
            // since the length filter was not part of the list, but
            // there are filters, then add the length filter
            InputFilter newFilters[] = new InputFilter[curFilters.length];
            System.arraycopy(curFilters, 0, newFilters, 0, curFilters.length);
            newFilters[curFilters.length] = lengthFilter;
        } else {
            view.setFilters(new InputFilter[]{lengthFilter});
        }
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        String expression2 = "^\\(?(\\d{2})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 提取text中的电话和手机号码
     *
     * @param text
     * @return
     */
    public static String pickUpPhoneNumber(String text) {
        Pattern pattern = Pattern.compile("(?<!\\d)(?:(?:1[35]\\d{9})|(?:0[1-9]\\d{1,2}-?\\d{7,8}))(?!\\d)");
        Matcher matcher = pattern.matcher(text);
        StringBuffer bf = new StringBuffer(64);
        while (matcher.find()) {
            bf.append(matcher.group()).append(",");
        }
        int len = bf.length();
        if (len > 0) {
            bf.deleteCharAt(len - 1);
        }
        return bf.toString();
    }

    /**
     * Hidden
     */
    public static void hideSoftInput(Activity activity, View view) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Hidden
     */
    public static void showSoftInput(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static String getAbsoluteImagePath(Activity mContext, Uri uri) {
        // 这里开始的第二部分，获取图片的路径：
        // String[] proj = { MediaStore.Images.Media.DATA };
        // // 好像是android多媒体数据库的封装接口，具体的看Android文档
        // Cursor cursor = managedQuery(originalUri, proj, null,
        // null, null);
        // // 按我个人理解 这个是获得用户选择的图片的索引值
        // int column_index = cursor
        // .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        // // 将光标移至开头 ，这个很重要，不小心很容易引起越界
        // cursor.moveToFirst();
        // // 最后根据索引值获取图片路径
        // imagePath = cursor.getString(column_index);
        // can post image
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.managedQuery(uri, proj, // Which columns to
                // return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by title)

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String res = cursor.getString(column_index);
        try {
            // 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        } catch (Exception e) {

        }
        return res;
    }

    /**
     * 对外提供getMD5(String)方法
     */
    public static boolean isEquals(String val1, String val12) {
        if (val1 == null) {
            return val12 == null || val12.length() == 0 ? true : false;
        } else {
            return val1.equals(val12);
        }
    }

    /**
     * 对外提供getMD5(String)方法
     */
    public static String getMD5(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(val.getBytes());
        byte[] m = md5.digest();// 加密
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < m.length; i++) {
            sb.append(m[i]);
        }
        return sb.toString();
    }
}
