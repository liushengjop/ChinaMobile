package com.my898tel.ui.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


import com.my898tel.R;
import com.my898tel.config.MyUri;
import com.my898tel.moble.PhoneInfo;
import com.my898tel.ui.SearchBaseFragment;
import com.my898tel.ui.widget.QuickAlphabeticBar;
import com.my898tel.util.pinyin.PinYin;
import com.tjerkw.slideexpandable.library.SlideExpandableListAdapter;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 联系人
 *
 * @author liusheng
 */
public class FragmentContact extends SearchBaseFragment {

    private ListView listView;

    private FragmentContactAdatper mAdatper;

    private QuickAlphabeticBar alpha;

    /**
     * 搜索内容
     */
    private EditText et_search_content;

    private LinkedHashMap<String, ArrayList<PhoneInfo>> maps = null;

    private ImageButton ib_delete;

    private TextView fast_position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_main, container,
                false);

        view.findViewById(R.id.tv_title);

        fast_position = (TextView)view.findViewById(R.id.fast_position);
        listView = (ListView) view.findViewById(R.id.listView);

        alpha = (QuickAlphabeticBar) view.findViewById(R.id.fast_scroller);

        ib_delete = (ImageButton) view.findViewById(R.id.ib_delete);
        ib_delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                et_search_content.setText("");
            }
        });

        et_search_content = (EditText) view.findViewById(R.id.et_search_content);


        et_search_content.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

        asyncQueryHandler = new MyAsyncQueryHandler(getActivity()
                .getContentResolver());



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        et_search_content.setText("");
        init();
    }


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
                phone.setSort_key(getSortKey(sortKey));
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

                if (token == INIT) {
                    mAdatper = new FragmentContactAdatper(maps, keyList, getActivity());
                    listView.setAdapter(mAdatper);
//                    		new SlideExpandableListAdapter(
//                    				mAdatper,
//                                    R.id.expandable_toggle_button,
//                                    R.id.expandable
//                                ));

                } else {
                	
                	listView.setAdapter(new AdatperContactSearch(maps,
                            et_search_content.getText().toString(), getActivity()));

//                    		new SlideExpandableListAdapter(
//                    				new AdatperContactSearch(maps,
//                                            et_search_content.getText().toString(), getActivity()),
//                                    R.id.expandable_toggle_button,
//                                    R.id.expandable
//                                ));

                	
                }

            }
        } else {
            listView.setAdapter(null);
        }
        super.onQueryComplete(token,cookie,cursor);
    }

    private String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        }
        return "#";
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

        alpha.init(getActivity());
        alpha.setInitView(fast_position);
        alpha.setListView(listView);
        alpha.setHight(alpha.getHeight());
        alpha.setVisibility(View.VISIBLE);
        return keyList;
    }





}
