package com.my898tel.ui.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.my898tel.R;
import com.my898tel.config.MyUri;
import com.my898tel.moble.PhoneInfo;
import com.my898tel.ui.BaseActivity;
import com.my898tel.ui.widget.QuickAlphabeticBar;
import com.my898tel.util.pinyin.PinYin;

public class MsgContact extends BaseActivity {

    private ListView listView;

    private QuickAlphabeticBar alpha;

    private AdatperMsgContact adatper;

    /**
     * 搜索内容
     */
    private EditText et_search_content;

    private LinkedHashMap<String, ArrayList<PhoneInfo>> maps = null;

    private ImageButton ib_delete;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.fragment_contact_main_new);

        listView = (ListView) findViewById(R.id.listView);

        alpha = (QuickAlphabeticBar) findViewById(R.id.fast_scroller);

        ib_delete = (ImageButton) findViewById(R.id.ib_delete);
        ib_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search_content.setText("");
            }
        });

        et_search_content = (EditText) findViewById(R.id.et_search_content);

        et_search_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String content = et_search_content.getText().toString();

                if (TextUtils.isEmpty(content)) {
                    ib_delete.setVisibility(View.GONE);
                    init();
                } else {
                    ib_delete.setVisibility(View.VISIBLE);
                    search(et_search_content.getText().toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        asyncQueryHandler = new MyAsyncQueryHandler(MsgContact.this
                .getContentResolver());

        setTitleAndRight(R.string.select_contact, R.string.complete);

        ib_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adatper != null) {
                    Intent intent = new Intent(MsgContact.this, MessageWrite.class);
                    ArrayList<PhoneInfo> phones = new ArrayList<PhoneInfo>();

                    for (int i = 0; i < adatper.mList.size(); i++) {
                        PhoneInfo phone = adatper.mList.get(i);
                        if (phone.getIs_check() == 1) {
                            phones.add(phone);
                        }
                    }
                    intent.putExtra("phones", phones);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        init();
    }

//
//    @Override
//    public void setTopValue() {
//        super.setTopValue();
//        tv_title.setText(R.string.select_contact);
//        ib_right.setImageResource(R.drawable.btn_sure_bg);
//        ib_right.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (adatper != null) {
//
//                    Intent intent = new Intent(MsgContact.this, MessageWrite.class);
//                    ArrayList<PhoneInfo> phones = new ArrayList<PhoneInfo>();
//
//                    for (int i = 0; i < adatper.mList.size(); i++) {
//                        PhoneInfo phone = adatper.mList.get(i);
//                        if (phone.getIs_check() == 1) {
//                            phones.add(phone);
//                        }
//                    }
//                    intent.putExtra("phones", phones);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                }
//            }
//        });
//
//        ib_left.setVisibility(View.VISIBLE);
//        ib_left.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MsgContact.this, MessageWrite.class);
//                ArrayList<PhoneInfo> phones = new ArrayList<PhoneInfo>();
//
//                intent.putExtra("phones", phones);
//                setResult(RESULT_OK, intent);
//                finish();
//                finish();
//            }
//        });
//    }

    public void init() {
        String[] projection = {"_id", "display_name", "data1", "sort_key"};
        asyncQueryHandler.startQuery(INIT, null, Uri.parse(MyUri.ALL_PHONE),
                projection, null, null, "sort_key COLLATE LOCALIZED asc");

    }

    public void search(String condition) {
        String[] projection = {"_id", "display_name", "data1", "sort_key"};

        String selection = Phone.NUMBER + " like '%" + condition + "%' or "
                + Phone.DISPLAY_NAME + " like '%" + condition + "%' or "
                + "sort_key" + " like '%" + PinYin.getPinYin(condition) + "%'";
        asyncQueryHandler.startQuery(SEARCH, null, Uri.parse(MyUri.ALL_PHONE),
                projection, selection, null, "sort_key COLLATE LOCALIZED asc");
    }

    @Override
    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
        // TODO Auto-generated method stub
        super.onQueryComplete(token, cookie, cursor);

        if (maps == null)
            maps = new LinkedHashMap<String, ArrayList<PhoneInfo>>();
        else
            maps.clear();

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                PhoneInfo phone = new PhoneInfo();
                cursor.moveToPosition(i);
                String contactId = cursor.getString(0);

                String name = cursor.getString(1);
                String number = cursor.getString(2);
                String sortKey = cursor.getString(3);
                phone.setDisplay_name(name);
                phone.setSort_key(sortKey);
                if (number.startsWith("+86")) {
                    phone.setDate1(number.substring(3));
                } else {
                    phone.setDate1(number);
                }

                if (maps.containsKey(name)) {
                    maps.get(name).add(phone);
                } else {
                    ArrayList<PhoneInfo> list = new ArrayList<PhoneInfo>();
                    list.add(phone);
                    maps.put(name, list);
                }
            }
            if (maps.size() > 0) {
                ArrayList<String> keyList = initValue(maps);

                adatper = new AdatperMsgContact(maps,
                        et_search_content.getText().toString(), MsgContact.this);
                listView.setAdapter(adatper);

            }
        } else {
            listView.setAdapter(null);
        }
    }

    public ArrayList<String> initValue(LinkedHashMap<String, ArrayList<PhoneInfo>> maps) {
        alphaIndexer = new HashMap<String, Integer>();

        //迭代所有联系人
        Iterator<Entry<String, ArrayList<PhoneInfo>>>
                iterator = maps.entrySet().iterator();
        //显示名称的KEY 同一个人有不同的手机号码
        ArrayList<String> keyList = new ArrayList<String>();
        int i = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<PhoneInfo>> entry =
                    (Entry<String, ArrayList<PhoneInfo>>) iterator.next();
            keyList.add(entry.getKey());
            String sort_key = entry.getValue().get(0).getSort_key();
            String name = getAlpha(sort_key);
            if (!alphaIndexer.containsKey(name)) {
                alphaIndexer.put(name, i);
            }
            i++;

        }


        sections = new String[keyList.size()];
        keyList.toArray(sections);

        alpha.setAlphaIndexer(alphaIndexer);

        alpha.init(MsgContact.this);
        alpha.setListView(listView);
        alpha.setHight(alpha.getHeight());
        alpha.setVisibility(View.VISIBLE);
        return keyList;
    }
}
