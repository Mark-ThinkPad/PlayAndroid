package com.example.httpandjson;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private View view;
    private TextInputEditText groupID, xh, value, xh_q;
    private Button button1, button2;
    private ListView listView;
    private LinkedList<WSN> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home, container,false);
        bindViews();
        button1.setOnClickListener(button1Click);
        button2.setOnClickListener(button2Click);
        return view;
    }

    private void bindViews() {
        groupID = view.findViewById(R.id.groupID);
        xh = view.findViewById(R.id.xh);
        value = view.findViewById(R.id.value);
        xh_q = view.findViewById(R.id.xh_q);
        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        listView = view.findViewById(R.id.wsnList);
    }

    private View.OnClickListener button1Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String groupID_s = Objects.requireNonNull(groupID.getText()).toString().trim();
            final String xh_s = Objects.requireNonNull(xh.getText()).toString().trim();
            final String value_s = Objects.requireNonNull(value.getText()).toString().trim();

            if (groupID_s.equals("") || xh_s.equals("") || value_s.equals("")) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle("Warning")
                        .setMessage("Please enter complete data.")
                        .setPositiveButton("accept", null)
                        .show();
                return;
            }

            new Thread() {
                @Override
                public void run() {
                    try {
                        String path = "http://47.105.216.128/wsn/WebServicedDemo.asmx/postdate?"
                                + "GROUPID=" + URLEncoder.encode(groupID_s, "UTF-8")
                                + "&XH=" + URLEncoder.encode(xh_s, "UTF-8")
                                + "&VALUE=" + URLEncoder.encode(value_s, "UTF-8");
                        URL url = new URL(path);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(5000);
                        int code = con.getResponseCode();
                        if (code == 200) {
                            Message msg = new Message();
                            msg.what = 0;
                            handler1.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            handler1.sendMessage(msg);
                        }
                        con.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 1;
                        handler1.sendMessage(msg);
                    }
                }
            }.start();
        }
    };

    @SuppressLint("HandlerLeak")
    private final Handler handler1 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle("Success")
                        .setMessage("Data upload succeeded.")
                        .setPositiveButton("OK", null)
                        .show();
            } else if (msg.what == 1) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle("Error")
                        .setMessage("Data upload failed.")
                        .setPositiveButton("accept", null)
                        .show();
            }
        }
    };

    private View.OnClickListener button2Click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String xh_qs = Objects.requireNonNull(xh_q.getText()).toString().trim();

            if (xh_qs.equals("")) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle("Warning")
                        .setMessage("Please enter data.")
                        .setPositiveButton("accept", null)
                        .show();
                return;
            }

            new Thread() {
                @Override
                public void run() {
                    try {
                        String path = "http://47.105.216.128/wsn/WebServicedDemo.asmx/getdateXHjson?"
                                + "XH=" + URLEncoder.encode(xh_qs, "UTF-8");
                        URL url = new URL(path);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setConnectTimeout(5000);
                        int code = con.getResponseCode();
                        if (code == 200) {
                            InputStream inputStream = con.getInputStream();
                            String response = read(inputStream);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = response;
                            handler2.sendMessage(msg);
                        } else {
                            Message msg = new Message();
                            msg.what = 1;
                            handler2.sendMessage(msg);
                        }
                        con.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 1;
                        handler2.sendMessage(msg);
                    }
                }
            }.start();
        }
    };


    @SuppressLint("HandlerLeak")
    private final Handler handler2 = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                String json = (String) msg.obj;
                parseJson(json);
                WSNAdapter wsnAdapter = new WSNAdapter(data, getContext());
                listView.setAdapter(wsnAdapter);
                setListViewHeight(listView);
            } else if (msg.what == 1) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle("Error")
                        .setMessage("Data query failed.")
                        .setPositiveButton("accept", null)
                        .show();
            }
        }
    };

    private static String read(InputStream inputStream) {
        try {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Read Input Stream Failed";
        }
    }

    private void parseJson(String json) {
        data = new LinkedList<WSN>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("WSNTEST");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                WSN wsn = new WSN();
                wsn.setGroupID(jsonObject1.getString("GROUPID").trim());
                wsn.setXH(jsonObject1.getString("XH").trim());
                wsn.setValue(jsonObject1.getString("VALUE").trim());
                wsn.setDate(jsonObject1.getString("UPDATETIME").trim());
                data.add(wsn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setListViewHeight(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) { return; }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
