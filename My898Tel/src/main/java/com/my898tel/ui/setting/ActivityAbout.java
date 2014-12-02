package com.my898tel.ui.setting;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.ui.BaseActivity;

/**
 * Created by shengliu on 14-9-26.
 */
public class ActivityAbout extends BaseActivity {


    private RelativeLayout relat_01;

    private ImageView iv_01;
    private TextView tv_01;

    private ImageView iv_line1;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_about);

//        for (int i = 1;i<=1;i++){
//            addListener(i);
//        }

        findViewById(R.id.relat_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setTitleNoRightBtn(R.string.about);
    }

    public void addListener(final int number){
       final int tv_id = getResources().getIdentifier("tv_0"+number,"id",getPackageName());
        final int iv_id =  getResources().getIdentifier("iv_0"+number,"id",getPackageName());
        final int line_id =  getResources().getIdentifier("iv_line"+number,"id",getPackageName());
       int relat_id = getResources().getIdentifier("relat_0"+number,"id",getPackageName());
        findViewById(relat_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView tv = (TextView) findViewById(tv_id);
                ImageView iv = (ImageView)findViewById(iv_id);



                if(number == 4 || number == 8){
                    if(tv.getVisibility() == View.VISIBLE){
                        tv.setVisibility(View.GONE);
                        iv.setImageResource(R.drawable.down);
                    }else{
                        tv.setVisibility(View.VISIBLE);
                        iv.setImageResource(R.drawable.up);
                    }
                }else{
                    ImageView iv_line = (ImageView)findViewById(line_id);
                    if(tv.getVisibility() == View.VISIBLE){
                        tv.setVisibility(View.GONE);
                        iv_line.setVisibility(View.VISIBLE);
                    }else{
                        tv.setVisibility(View.VISIBLE);
                        iv_line.setVisibility(View.GONE);
                    }

                }

            }
        });
    }
}
