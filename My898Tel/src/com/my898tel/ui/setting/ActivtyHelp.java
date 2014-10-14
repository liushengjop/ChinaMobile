package com.my898tel.ui.setting;

import android.os.Bundle;

import com.my898tel.R;
import com.my898tel.ui.BaseActivity;

/**
 * Created by shengliu on 14-9-26.
 */
public class ActivtyHelp extends BaseActivity {

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_help);
        setTitleNoRightBtn(R.string.help);
    }
}
