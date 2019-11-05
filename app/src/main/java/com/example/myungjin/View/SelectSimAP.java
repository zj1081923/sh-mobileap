package com.example.myungjin.withusplanet.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myungjin.withusplanet.DataConnect.SearchAdapter;
import com.example.myungjin.withusplanet.DataConnect.ServerConnect;
import com.example.myungjin.withusplanet.DataConnect.SimData;
import com.example.myungjin.withusplanet.DataConnect.SimSelectInfo;
import com.example.myungjin.withusplanet.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SelectSimAP extends AppCompatActivity {
    private Context mContext = this;

    private List<String> list;      //데이터를 넣은 리스트변수
    private ListView listView;      //검색을 보여줄 리스트변수
    private EditText editSearch;    //검색어 입력할 Input 창
    private SearchAdapter adapter;  //리스트뷰에 연결할 어댑터
    private ArrayList<String> arraylist;
    ServerConnect serverConnect;
    private SimSelectInfo[] iplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sim_ap);
        getSupportActionBar().hide();
        final TextView idtx = (TextView)findViewById(R.id.textView);
        final String userID = getIntent().getStringExtra("id");
        idtx.setText("ID | " + userID);

        serverConnect = new ServerConnect();

        editSearch = (EditText)findViewById(R.id.searchIP);
        listView = (ListView)findViewById(R.id.listView);


        FloatingActionButton Refresh = (FloatingActionButton)findViewById(R.id.refresh);
        Refresh.setOnClickListener(new View.OnClickListener(){ //새로고침 버튼 눌렀을때
             @Override
             public void onClick(View view) {
                 //리스트 생성
                 list.clear();
                 //list = new ArrayList<String>();

                 //검색에 사용할 데이터를 미리 저장
                 try {
                     settingList();
                 } catch (ExecutionException e) {
                     e.printStackTrace();
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 //리스트의 모든 데이터를 arralist에 복사한다.
                 //arraylist = new ArrayList<String>();
                 arraylist.clear();
                 arraylist.addAll(list);

                 //리스트에 연동될 어댑터 생성
                 adapter = new SearchAdapter(list, mContext);


                 //리스트뷰에 어댑터 연결
                 listView.setAdapter(adapter);
             }
        });


        //리스트 생성
        list = new ArrayList<String>();

        //검색에 사용할 데이터를 미리 저장
        try {
            settingList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //리스트의 모든 데이터를 arralist에 복사
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        //리스트에 연동될 어댑터 생성
        adapter = new SearchAdapter(list, this);

        //리스트뷰에 어댑터 연결
        listView.setAdapter(adapter);

        //input창에 검색어 입력시 "addTextChangedListener"이벤트 리스터를 정의
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //input창에 문자를 입력할때마다 호출됨. search 메소드를 호출
                String text = editSearch.getText().toString();
                search(text);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String a = (String)adapterView.getAdapter().getItem(i);
                Intent intentViewActivity;
                String selected_item = list.get(i).toString();
                if(selected_item.equals("웹서버 주소")){
                    intentViewActivity = new Intent(mContext, ViewActivity.class);
                    intentViewActivity.putExtra("ip","203.252.34.237");
                    intentViewActivity.putExtra("port","8080");
                }else{
                    String[] infos = selected_item.split(":");
                    intentViewActivity = new Intent(mContext, SimViewActivity.class);
                    intentViewActivity.putExtra("ip", infos[0]);
                    intentViewActivity.putExtra("port",infos[1]);

                    /*for(float a : mem){
                        System.out.println("mem : "+a);
                    }*/
                }
                intentViewActivity.putExtra("id",userID);
                startActivity(intentViewActivity);

            }
        });



    }

    //검색을 수행하는 메소드
    public void search(String charText){
        //문자 입력시마다 리스트를 지우고 새로 뿌려줌
        list.clear();

        //문자입력이 없을 때는 모든 데이터를 보여줌
        if(charText.length() == 0){
            list.addAll(arraylist);
        }else{ //문자입력할때
            //리스트의 모든 데이터를 검색함
            for(int i=0; i<arraylist.size(); i++){
                //arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어있으면 true 리턴
                if(arraylist.get(i).toLowerCase().contains(charText)){
                    list.add(arraylist.get(i));
                }
            }
        }
        //리스트 데이터가 변경되었으므로 어댑터를 갱신하여 검색된 데이터를 화면에 보여줌
        adapter.notifyDataSetChanged();
    }

    //검색에 사용될 데이터를 리스트에 추가하는 메소드~!~
    private void settingList() throws ExecutionException, InterruptedException {
        Map request = new HashMap();
        request.put("url","/simsim");
        String log = serverConnect.returnReply(request);
        Log.d("iplist log", log);
        Gson gson = new Gson();
        iplist = gson.fromJson(log, SimSelectInfo[].class);
        list.add("웹서버 주소");

        for(SimSelectInfo str : iplist){
            list.add(str.getIp()+":"+str.getPort());
        }



    }
}
