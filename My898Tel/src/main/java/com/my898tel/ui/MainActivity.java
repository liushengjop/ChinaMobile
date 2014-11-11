package com.my898tel.ui;

import java.util.ArrayList;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.mobstat.StatService;
import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.config.MyUri;
import com.my898tel.myinterface.Exit;
import com.my898tel.ui.call.FragmentCallN;
import com.my898tel.ui.call.FragmentCallRecord;
import com.my898tel.ui.contact.FragmentContact;
import com.my898tel.ui.message.FragmentMessage;
import com.my898tel.ui.setting.ActivitySetting;
import com.my898tel.util.AppManager;
import com.my898tel.util.ObserverContact;
import com.my898tel.util.StatWrapper;
import com.my898tel.util.Utils;

/**
 * 程序主界面
 * 
 * @author liusheng
 */
public class MainActivity extends FragmentActivity implements Exit {
	/**
	 * 选项卡
	 */
	private FragmentTabHost mTabHost;

    private ViewPager vp;


	private Class<?>[] classTags = { FragmentCallN.class, FragmentCallRecord.class , FragmentMessage.class,FragmentContact.class};
	private Integer[] images = { R.drawable.call_bg,R.drawable.call_log_bg,R.drawable.message_bg, R.drawable.contact_bg};

	private AsyncQueryHandler asyncQueryHandler;

	boolean isExit;


    /**
     * 标题栏
     */
    protected TextView tv_title;
    /**
     * 余额
     */
    protected TextView tv_over;


    /**
     * 右侧按钮
     */
    protected Button ib_right;

    private ImageButton ib_left;


    private int currIndex = 0;// 当前页卡编号

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);

        findViewById(R.id.ib_left).setVisibility(View.INVISIBLE);
        vp = (ViewPager)findViewById(R.id.pager);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_over = (TextView)findViewById(R.id.tv_over);
        ib_right = (Button)findViewById(R.id.ib_right);
        ib_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ActivitySetting.class);
                startActivity(intent);
            }
        });

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

		mTabHost.setup(this, getSupportFragmentManager(), R.id.pager);

        mTabHost.setOnTabChangedListener(new TabHostListener());
		for (int i = 0; i < classTags.length; i++) {
			View view = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.tab_widget_item, null);
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			iv_icon.setImageResource(images[i]);
			TabSpec tabSec = mTabHost.newTabSpec(classTags[i].toString());
			tabSec.setIndicator(view);
			mTabHost.addTab(tabSec, classTags[i], null);
		}

		initPager();
		ObserverContact smsObserver = new ObserverContact(MainActivity.this,
				null);
		getContentResolver().registerContentObserver(
				Uri.parse(MyUri.ALL_PHONE), true, smsObserver);
		smsObserver.init();

		// 百度推送
		boolean isConnected = PushManager.isConnected(MainActivity.this);
		boolean isPushEnabled = PushManager.isPushEnabled(MainActivity.this);

		if (!isConnected) {
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY,
					Utils.getMetaValue(MainActivity.this, "api_key"));
		} else {
			if (!isPushEnabled) {
				PushManager.startWork(getApplicationContext(),
						PushConstants.LOGIN_TYPE_API_KEY,
						Utils.getMetaValue(MainActivity.this, "api_key"));
			}
		}
		// 百度移动运用统计

		StatService.setAppChannel(this, "test1", true);
		// 设置每次启动session的间隔失效时间，可以不设置默认30S
		// 测试时，可以使用1秒钟session过期，这样不断的间隔1S启动退出会产生大量日志。
		StatService.setSessionTimeOut(1);
		// 是否进行DEBUG
		StatService.setDebugOn(false);
		
//		vp.setPageTransformer(true,new MyAnimation());
	
	
		AppManager.getAppManager().addActivity(this);
	}


    private void initPager() {
      final  ArrayList<Fragment> list = new ArrayList<Fragment>();
        FragmentCallN p1 = new FragmentCallN();
        FragmentCallRecord p2 = new FragmentCallRecord();
        FragmentMessage p3 = new FragmentMessage();
        FragmentContact p4 = new FragmentContact();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        vp.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),list));
        vp.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });


        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                mTabHost.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), R.string.str_lable_01,
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			AppManager.getAppManager().AppExit(getApplicationContext());
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}

	};
	
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		if (intent.getBooleanExtra("call", false)) {
			UIApplication.callPhone(getApplicationContext());
			mTabHost.setCurrentTabByTag(classTags[1].toString());
		}

		
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatWrapper.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatWrapper.onPause(this);
	}


    private class TabHostListener implements TabHost.OnTabChangeListener {
        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            vp.setCurrentItem(position);

            switch (position){
                case  0:
                    tv_title.setText(R.string.dial);
                    break;
                case 1:
                    tv_title.setText(R.string.call_log);
                    break;
                case 2:
                    tv_title.setText(R.string.message);
                    break;
                case 3:
                    tv_title.setText(R.string.addresslist);
                    break;
            }
        }
    }


    public class  MyAnimation implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;
        @Override
        public void transformPage(View view, float v) {
            int pageWidth = view.getWidth();

            if(v<-1){
                view.setAlpha(0);
            }else if(v<=0){
                view.setAlpha(1);
                view.setTranslationX(0);
                view.setScaleX(1);
                view.setScaleY(1);

            }else if(v<=1){
                view.setAlpha(1 - v);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -v);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(v));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            }else{
                view.setAlpha(0);
            }
        }


//        private static final float MIN_SCALE = 0.85f;
//        private static final float MIN_ALPHA = 0.5f;
//
//        public void transformPage(View view, float position) {
//            int pageWidth = view.getWidth();
//            int pageHeight = view.getHeight();
//
//            if (position < -1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                view.setAlpha(0);
//
//            } else if (position <= 1) { // [-1,1]
//                // Modify the default slide transition to shrink the page as well
//                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
//                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
//                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
//                if (position < 0) {
//                    view.setTranslationX(horzMargin - vertMargin / 2);
//                } else {
//                    view.setTranslationX(-horzMargin + vertMargin / 2);
//                }
//
//                // Scale the page down (between MIN_SCALE and 1)
//                view.setScaleX(scaleFactor);
//                view.setScaleY(scaleFactor);
//
//                // Fade the page relative to its size.
//                view.setAlpha(MIN_ALPHA +
//                        (scaleFactor - MIN_SCALE) /
//                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));
//
//            } else { // (1,+Infinity]
//                // This page is way off-screen to the right.
//                view.setAlpha(0);
//            }
//        }
    }
}
