package com.my898tel.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my898tel.R;


/**
 * Created by liusheng on 14-9-15.
 */
public class DialogCustomeFragment extends DialogFragment {

    /**
     * 标题
     */
    private TextView tv_title;

    /**
     * 信息内容
     */
    private TextView tv_message;

    private Button btn_01;

    private Button btn_02;

    private Button btn_03;

    private LinearLayout linear_custom_view;

    private Builder builder;

    private LinearLayout linear_02;


    //防止构造 只能通过builder来创建
    public DialogCustomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(R.style.SampleTheme, R.style.SampleTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_custome_fragment, container, false);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        linear_custom_view = (LinearLayout)view.findViewById(R.id.linear_custom_view);
        btn_01 = (Button) view.findViewById(R.id.btn_01);
        btn_02 = (Button) view.findViewById(R.id.btn_02);
        btn_03 = (Button) view.findViewById(R.id.btn_03);

        linear_02 = (LinearLayout)view.findViewById(R.id.linear_02);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        apply(builder);
    }

    public void setBuilder(Builder builder){
        this.builder = builder;
    }

    private void apply(final Builder builder) {
        //标题
        if (TextUtils.isEmpty(builder.title)) {
            tv_title.setVisibility(View.GONE);
        } else {
            tv_title.setText(builder.title);
        }
        //信息内容
        if (TextUtils.isEmpty(builder.message)) {
            tv_message.setVisibility(View.GONE);
        } else {
            tv_message.setText(builder.message);
        }
        //自定义视图
        if(builder.view != null){
            linear_custom_view.addView(builder.view,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        //按钮1
        if (TextUtils.isEmpty(builder.btn1)) {
            btn_01.setText(getString(R.string.sure));
            btn_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            return;
        } else {
            btn_01.setText(builder.btn1);
            btn_01.setVisibility(View.VISIBLE);
            btn_01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (builder.listener1 == null)
                        dismiss();
                    else
                        builder.listener1.onClick(getDialog());
                }
            });
        }

        //按钮2
        if (TextUtils.isEmpty(builder.btn2)) {
            btn_02.setVisibility(View.GONE);
            linear_02.setVisibility(View.GONE);
            return;
        } else {
            btn_02.setVisibility(View.VISIBLE);
            btn_02.setText(builder.btn2);
            btn_02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (builder.listener2 == null)
                        dismiss();
                    else
                        builder.listener2.onClick(getDialog());
                }
            });
        }

        //按钮3
        if (TextUtils.isEmpty(builder.btn3)) {
            btn_03.setVisibility(View.GONE);
            return;
        } else {
            btn_03.setText(builder.btn3);
            btn_03.setVisibility(View.VISIBLE);
            btn_03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (builder.listener3 == null)
                        dismiss();
                    else
                        builder.listener3.onClick(getDialog());
                }
            });
        }


    }

    public static class Builder {

        private Listener listener1;

        private Listener listener2;

        private Listener listener3;

        private String btn1;

        private String btn2;

        private String btn3;

        private View view;

        private String message;

        private String title;

        private DialogCustomeFragment dialogCustomeFragment;

        public Builder() {
        }

        public void setTitle(String title){
            this.title = title;
        }

        public void setMessage(String message){
            this.message = message;
        }


        public void setBtn1(String btnText, Listener onclickListener) {
            btn1 = btnText;
            listener1 = onclickListener;
        }

        public void setBtn2(String btnText, Listener onclickListener) {
            if(TextUtils.isEmpty(btn1)){
                setBtn1(btnText,onclickListener);
                return;
            }else{
                btn2 = btnText;
                listener2 = onclickListener;
            }
        }

        public void setBtn3(String btnText, Listener onclickListener) {
            if(TextUtils.isEmpty(btn1)){
                setBtn1(btnText,onclickListener);
                return;
            }else if(TextUtils.isEmpty(btn2)){
                setBtn2(btnText, onclickListener);
                return;
            }else{
                btn3 = btnText;
                listener3 = onclickListener;
            }
        }

        public void setView(View view){
            this.view = view;
        }

        public void create() {
            dialogCustomeFragment = new DialogCustomeFragment();
            dialogCustomeFragment.setBuilder(this);
        }

        public void show(android.support.v4.app.FragmentManager manager, String tag) {
            if (dialogCustomeFragment == null) {
                create();
            }
            dialogCustomeFragment.show(manager, tag);
        }


    }

    public interface Listener {
        public void onClick(Dialog dialog);
    }
}
