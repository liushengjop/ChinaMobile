package com.my898tel.ui.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.moble.CheckUpdate;
import com.my898tel.util.AppManager;
import com.my898tel.util.Util_File;
import com.my898tel.util.Util_G;
import com.my898tel.util.Util_MemoryStatus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressLint("Recycle")
public class UpdateFragment extends DialogFragment {
    final static String DIALOG_TAG = "UpdateDialog";
    private ProgressBar bar;
    private TextView tv_loading_process;
    private int loading_process = 0;
    private RelativeLayout layoutTotal;
    private RelativeLayout layoutProgress;
    private String fileName = "new.apk";// 强制更新
    private String myFilePath = "york_it/project/ordering/";// 强制更新
    private Button btn_cancle;
    private Button btn_ok;
    private TextView tv_content;
    private boolean isSdcard = true;// 是否保存在sdcard
    private boolean isFristCount = true;// 第一次数这个数
    private boolean downloadWeb = false;// 是否跳转网站
    private TextView tv_title;
    private String SDPATH;
    private String pathName;
    private int progressNew = 0;
    private int progressOld = -1;
    private ScheduledExecutorService scheduledExecutorService;
    private CheckUpdate result;
    private AsycnDownLoadTask asycnDownLoadTask;

    public static UpdateFragment newInstance() {
        UpdateFragment f = new UpdateFragment();
        return f;
    }

    /**
     * **
     *
     * @param fm
     * @param result
     * @param isForceUpdate 当为可选更新时是否更新  status 2 强制  1 可选 0 不升级
     */
    public void show(FragmentManager fm, CheckUpdate result, boolean isForceUpdate) {
        this.result = result;
        if (result != null /*&& result.getKey() != 200 && result.getKey() != 0*/ && !TextUtils.isEmpty(result.url)) {
//            long timeLast = Util_UserInfo.getServerTime();
            long tmpLocalMilli = new Date().getTime() / 1000;
            // 如果强制更新就按时间判断
            if (result.status == 1 /*&& (isForceUpdate || (tmpLocalMilli - timeLast >MathExtend.multiply(24,60,60)))*/) {// 可选更新
                Fragment arg0 = fm.findFragmentByTag(DIALOG_TAG);
                if (arg0 != null) {
                    try {
                        fm.beginTransaction().remove(arg0);
                    } catch (Exception e) { // TODO: handle exception
                        e.printStackTrace();
                    }
                }
                show(fm, DIALOG_TAG);
            } else if (result.status == 2) {// 强制更新
                Fragment arg0 = fm.findFragmentByTag(DIALOG_TAG);
                if (arg0 != null) {
                    try {
                        fm.beginTransaction().remove(arg0);
                    } catch (Exception e) { //
                        e.printStackTrace();
                    }
                }
                show(fm, DIALOG_TAG);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setStyle(R.style.SampleTheme, R.style.SampleTheme);
        if (Util_File.isSDCardExit()) {// SD卡可用
            SDPATH = Environment.getExternalStorageDirectory().toString();
            isSdcard = true;
        } else {
            SDPATH = UIApplication.getInstance().getCacheDir().getAbsolutePath();
        }
    }

    /**
     * 换行切换任务
     *
     * @author Administrator 不是UI縣城
     */
    private class ScrollTask implements Runnable {
        public void run() {
            if (isFristCount) {
                progressNew = loading_process;
                isFristCount = false;
            } else {
                progressOld = loading_process;
                isFristCount = true;
            }
            if (progressNew == progressOld) {
                asycnDownLoadTask.cancel(true);
                mHandler.sendEmptyMessage(0);
                scheduledExecutorService.shutdown();
                downloadWeb = true;
            }
        }
    }

    private final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            btn_cancle.setText(R.string.str_public_know);
            tv_content.setText(R.string.str_logo_update_fail_02);
            tv_title.setText(R.string.str_logo_programe_update);
            layoutTotal.setVisibility(View.VISIBLE);
            layoutProgress.setVisibility(View.GONE);
            btn_cancle.setVisibility(View.VISIBLE);
            btn_ok.setVisibility(View.GONE);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub\
        View view = inflater.inflate(R.layout.layout_update, container, false);
        tv_content = (TextView) view.findViewById(R.id.id_load_data_textView_content);
        btn_ok = (Button) view.findViewById(R.id.id_update_btn_ok);
        btn_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                Util_UserInfo.SaveServerTime(result.getUpdateTime());
                layoutTotal.setVisibility(View.GONE);
                layoutProgress.setVisibility(View.VISIBLE);
                tv_title.setText(R.string.str_logo_update_progress);
                if (Util_G.isFastDoubleClick()) {
                    return;
                }
                asycnDownLoadTask = new AsycnDownLoadTask();
                asycnDownLoadTask.execute(result.url);
            }
        });
        tv_title = (TextView) view.findViewById(R.id.id_update_textView_titile);
        btn_cancle = (Button) view.findViewById(R.id.id_update_btn_cancle);
        btn_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (scheduledExecutorService != null) {
                    scheduledExecutorService.shutdown();
                }
                if (downloadWeb) {
                    try {
                        String url = "http://www.xiaoluwangluo.com";
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch (Exception e) {
                        Util_G.DisplayToast(R.string.wraning05, Toast.LENGTH_LONG);
                        e.printStackTrace();
                    }
                    dismiss();
                    AppManager.getAppManager().AppExit(getActivity());
                } else {
//                    Util_UserInfo.SaveServerTime(result.getUpdateTime());
                    if (result.status == 2) {
                        AppManager.getAppManager().AppExit(getActivity());
                    }
                    dismiss();
                }
            }
        });
        layoutTotal = (RelativeLayout) view.findViewById(R.id.id_update_relativeLayout01_total);
        layoutProgress = (RelativeLayout) view.findViewById(R.id.id_update_relativeLayout3_progress);
        bar = (ProgressBar) view.findViewById(R.id.id_update_progressBar_progress);
        tv_loading_process = (TextView) view.findViewById(R.id.id_update_tv_progress);
        tv_content.setText(result.desc);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        setCancelable(false);
        if (result.status == 2) {
            btn_cancle.setText(R.string.str_public_quit);
        }
    }

    /**
     * *
     * 异步下载线程
     *
     * @author herozhou1314
     */
    class AsycnDownLoadTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            // 当Activity显示出来后，每25秒钟切换一次图片显示
            scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 25, TimeUnit.SECONDS);
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(arg0[0]);
            HttpResponse response;
            try {
                response = client.execute(get);
                HttpEntity entity = response.getEntity();
                float length = entity.getContentLength();
                if (Util_File.isSDCardExit()) {
                    if (length > Util_MemoryStatus.getFreeSizeSD() && Util_MemoryStatus.getFreeSizeSD() > 0) {
                        SDPATH = UIApplication.getInstance().getCacheDir().getAbsolutePath();
                        isSdcard = false;
                        if (length > Util_MemoryStatus.getAvailableExternalMemorySize()) {
                            return 1;
                        }
                    }
                } else {
                    isSdcard = false;
                    if (length > Util_MemoryStatus.getFreeSize()) {
                        return 1;
                    }
                }
                pathName = SDPATH + File.separator + (isSdcard ? myFilePath : "") + fileName;
//                LogUtils.LOGD("pathName-->>"+pathName);
                InputStream is = entity.getContent();
                FileOutputStream fileOutputStream = null;
                if (is != null) {
                    creatSDDir(File.separator + (isSdcard ? myFilePath : ""));
                    File file = new File(pathName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fileOutputStream = new FileOutputStream(file);
                    byte[] buf = new byte[512];
                    int ch = -1;
                    float count = 0;
                    while ((ch = is.read(buf)) != -1) {
                        fileOutputStream.write(buf, 0, ch);
                        count += ch;
                        publishProgress((int) ((int) (count * 100 / length)));
                    }
                }
                fileOutputStream.flush();
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                return 4;
            }
            return 2;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            loading_process = values[0];
            bar.setProgress(loading_process);
            tv_loading_process.setText(getString(R.string.str_logo_update_progress_loading, loading_process + "%"));
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            switch (result) {
                case 1:
                    scheduledExecutorService.shutdown();
                    tv_title.setText(R.string.str_logo_programe_update);
                    layoutTotal.setVisibility(View.VISIBLE);
                    layoutProgress.setVisibility(View.GONE);
                    btn_cancle.setVisibility(View.VISIBLE);
                    btn_ok.setVisibility(View.GONE);
                    btn_cancle.setText(R.string.str_public_know);
                    tv_content.setText(R.string.str_logo_update_fail_01);
                    break;
                case 2: {
                    scheduledExecutorService.shutdown();
                    dismiss();
                    // chmod 755 /* 755 权限是对apk自身应用具有所有权限， 对组和其他用户具有读和执行权限 */
                    File file = new File(pathName);
                    if (file.exists()) {
                        if (!Util_File.isSDCardExit() || !isSdcard) {
                            try {
                                // String cmd = "chmod 777 " + pathName;
                                String permission = "755";
                                String filesCom = "chmod " + permission + " " + file.getParentFile().getAbsolutePath();
                                // 下载的文件apk为android的应用程序 需要具有x的权限，也就是可执行的权限，才能安装
                                String apkComm = "chmod " + permission + " " + file.getAbsolutePath();
                                Runtime runtime = Runtime.getRuntime();
                                runtime.exec(filesCom);
                                runtime.exec(apkComm);
                                // Runtime.getRuntime().exec(cmd);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Intent it = new Intent(Intent.ACTION_VIEW);
                        it.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(it);
                        AppManager.getAppManager().AppExit(getActivity());
                    } else {
                        mHandler.sendEmptyMessage(0);
                        scheduledExecutorService.shutdown();
                        downloadWeb = true;
                    }
                    break;
                }
                case 4: {
                    mHandler.sendEmptyMessage(0);
                    scheduledExecutorService.shutdown();
                    downloadWeb = true;
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * @param dirName
     */
    public File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdirs();
        return dir;
    }
}
