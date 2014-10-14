package com.my898tel.ui.call;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.my898tel.R;
import com.my898tel.UIApplication;
import com.my898tel.ui.BaseFragment;

/**
 * Created by liusheng on 14-9-20.
 */
public class FragmentCallN extends BaseFragment implements  View.OnClickListener {

    /**拨号内容*/
    private TextView tv_call_number;

    /**删除拨号内容按钮*/
    private ImageButton ib_delete;

    private ImageButton ib_1,ib_2,ib_3,ib_4,ib_5,ib_6,ib_7,ib_8,ib_9,ib_0,ib_x,ib_j;

    /**呼叫按钮*/
    private ImageButton ib_call;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_call,container,false);

        tv_call_number = (TextView)view.findViewById(R.id.tv_call_number);
        ib_delete      = (ImageButton)view.findViewById(R.id.ib_delete);

        ib_0           =(ImageButton)view.findViewById(R.id.ib_0);
        ib_1           =(ImageButton)view.findViewById(R.id.ib_1);
        ib_2           =(ImageButton)view.findViewById(R.id.ib_2);
        ib_3           =(ImageButton)view.findViewById(R.id.ib_3);
        ib_4           =(ImageButton)view.findViewById(R.id.ib_4);
        ib_5           =(ImageButton)view.findViewById(R.id.ib_5);
        ib_6           =(ImageButton)view.findViewById(R.id.ib_6);
        ib_7           =(ImageButton)view.findViewById(R.id.ib_7);
        ib_8           =(ImageButton)view.findViewById(R.id.ib_8);
        ib_9           =(ImageButton)view.findViewById(R.id.ib_9);
        ib_x           =(ImageButton)view.findViewById(R.id.ib_x);
        ib_j           =(ImageButton)view.findViewById(R.id.ib_j);
        ib_call        =(ImageButton)view.findViewById(R.id.ib_call);

        setListener();
        return view;
    }

    @Override
    public void setListener() {
        ib_delete.setOnClickListener(this);
        ib_0.setOnClickListener(this);
        ib_1.setOnClickListener(this);
        ib_2.setOnClickListener(this);
        ib_3.setOnClickListener(this);
        ib_4.setOnClickListener(this);
        ib_5.setOnClickListener(this);
        ib_6.setOnClickListener(this);
        ib_7.setOnClickListener(this);
        ib_8.setOnClickListener(this);
        ib_9.setOnClickListener(this);
        ib_x.setOnClickListener(this);
        ib_j.setOnClickListener(this);
        ib_call.setOnClickListener(this);

        tv_call_number.setTag("");

        tv_call_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = tv_call_number.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ib_delete.setVisibility(View.INVISIBLE);
                }else{
                    ib_delete.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_delete:
                deleteContent();
                break;
            case R.id.ib_0:
                addContent("0",R.drawable.zero);
                break;
            case R.id.ib_1:
                addContent("1",R.drawable.one);
                break;
            case R.id.ib_2:
                addContent("2",R.drawable.two);
                break;
            case R.id.ib_3:
                addContent("3",R.drawable.three);
                break;
            case R.id.ib_4:
                addContent("4",R.drawable.four);
                break;
            case R.id.ib_5:
                addContent("5",R.drawable.five);
                break;
            case R.id.ib_6:
                addContent("6",R.drawable.six);
                break;
            case R.id.ib_7:
                addContent("7",R.drawable.seven);
                break;
            case R.id.ib_8:
                addContent("8",R.drawable.eight);
                break;
            case R.id.ib_9:
                addContent("9",R.drawable.nine);
                break;
            case R.id.ib_x:
                addContent("*",R.drawable.x);
                break;
            case R.id.ib_j:
                addContent("#",R.drawable.j);
                break;
            case R.id.ib_call:

                UIApplication.getInstance().callPhone(getActivity(),(String)tv_call_number.getTag());
                tv_call_number.setText("");
                tv_call_number.setTag("");
                break;
        }
    }

    public void deleteContent(){
        Spannable spannable = tv_call_number.getEditableText();
        ImageSpan[] imageSpan =  spannable.getSpans(0, spannable.length(), ImageSpan.class);
        tv_call_number.setText("");
        for(int i=0;i<imageSpan.length-1;i++){
            SpannableString spanStr = new SpannableString("1");
            spanStr.setSpan(imageSpan[i], spanStr.length()-1, spanStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_call_number.append(spanStr);
        }


        //原先值
        String tagValue = (String)tv_call_number.getTag();

        if(TextUtils.isEmpty(tagValue)){
            tagValue = tagValue.substring(0,tagValue.length()-1);
            tv_call_number.setTag(tagValue);
        }




    }

    public void addContent(String content,int resoureID){
        String  value = String.valueOf(tv_call_number.getTag()) +content;
        tv_call_number.setTag(value);

        ImageSpan span = new ImageSpan(getActivity(), resoureID);
        SpannableString spanStr = new SpannableString("1");
        spanStr.setSpan(span, spanStr.length()-1, spanStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_call_number.append(spanStr);

    }
}
