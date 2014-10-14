package com.my898tel.ui.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.myinterface.PassData;
import com.my898tel.ui.BaseActivity;
import com.my898tel.ui.dialog.DialogSelectMoney;

/**
 * 中国移动充值卡充值
 *
 * @author liusheng
 */
public class ActivityChinaMoble extends BaseActivity {


    private Button btn_select_money;

    private EditText et_tel;

    private EditText et_cardno;

    private EditText et_pwd;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_china_moble);

        et_tel = (EditText)findViewById(R.id.et_tel);
        et_cardno = (EditText)findViewById(R.id.et_cardno);
        et_cardno.setFilters(new InputFilter[]{new InputFilter.LengthFilter(17)});
        et_pwd = (EditText)findViewById(R.id.et_pwd);
        et_pwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
		btn_select_money = (Button)findViewById(R.id.btn_select_money);
		btn_select_money.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogSelectMoney dialogSelectMoney = new DialogSelectMoney();
                dialogSelectMoney.setPassData(new PassData<String>() {
                    @Override
                    public void passData(String t) {
                        btn_select_money.setText(t);
                    }
                });
                dialogSelectMoney.show(getSupportFragmentManager(), DialogSelectMoney.class.getName());
            }
        });


        setTitleNoRightBtn(R.string.china_moble_title);
    }

    public void showWeb(View view){
        String tel = et_tel.getText().toString();
        String money = btn_select_money.getText().toString();
        String cardno = et_cardno.getText().toString();
        String pwd = et_pwd.getText().toString();
        if(TextUtils.isEmpty(tel)){
            showMessage(getString(R.string.plz_input_moble));
            return;
        }


        if(TextUtils.isEmpty(cardno) || cardno.length() < 17){
            showMessage(getString(R.string.plz_input_17_card));
            return;
        }

        if(TextUtils.isEmpty(pwd) || pwd.length() < 18){
            showMessage(getString(R.string.plz_input_18_pwd));
            return;
        }
        if(TextUtils.isEmpty(money)){
            showMessage(getString(R.string.plz_select_money));
            return;
        }

        money = money.substring(0,money.length()-1);

        Intent intent = new Intent(ActivityChinaMoble.this,ActivityWebView.class);
//        amp;cardno=x&amp;passwd=x&cardmoney=x&type=1
        intent.putExtra("url",getString(R.string.url02,tel)+"&amp;cardno="+cardno+"&amp;passwd"+
        pwd+"&cardmoney="+money+"&type=1");
        startActivity(intent);
        finish();
    }

//	@Override
//	public void setTopValue() {
//		super.setTopValue();
//		tv_title.setText();
//		ib_left.setVisibility(View.VISIBLE);
//		ib_right.setVisibility(View.INVISIBLE);
//		ib_left.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
//	}

}
