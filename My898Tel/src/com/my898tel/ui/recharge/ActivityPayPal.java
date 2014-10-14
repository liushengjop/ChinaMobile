package com.my898tel.ui.recharge;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.my898tel.R;
import com.my898tel.myinterface.PassData;
import com.my898tel.ui.BaseActivity;
import com.my898tel.ui.dialog.DialogSelectMoney;

/**
 * Created by shengliu on 14-10-6.
 */
public class ActivityPayPal extends BaseActivity {

    private Button btn_select_money;

    private EditText et_tel;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_paypal);
        setTitleNoRightBtn(R.string.paypal_recharge);
        et_tel = (EditText) findViewById(R.id.et_tel);
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
    }

    public void showWeb(View view){
        String tel = et_tel.getText().toString();
        String money = btn_select_money.getText().toString();

        if(TextUtils.isEmpty(tel)){
            showMessage(getString(R.string.plz_input_moble));
            return;
        }
        if(TextUtils.isEmpty(money)){
            showMessage(getString(R.string.plz_select_money));
            return;
        }
        money = money.substring(0,money.length()-1);

        Intent intent = new Intent(ActivityPayPal.this,ActivityWebView.class);
//        &total_fee=X
        intent.putExtra("url",getString(R.string.url03,tel)+"&total_fee="+money+"&pay_bank=directPay&alibody=note");
        startActivity(intent);

        finish();
    }
}
