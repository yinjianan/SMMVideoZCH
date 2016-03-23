package s.xx.yy.mmv.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tandong.sa.json.Gson;
import com.tandong.sa.json.GsonBuilder;
import com.tandong.sa.json.JsonArray;
import com.tandong.sa.json.JsonElement;
import com.tandong.sa.json.JsonObject;
import com.tandong.sa.json.JsonParser;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import s.xx.yy.mmv.adapter.ListAdapter;
import s.xx.yy.mmv.base.BaseActivity;
import s.xx.yy.mmv.base.BaseApplication;
import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.utils.CommonUtils;
import s.xx.yy.mmv.utils.NetUtils;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class SearchActivity extends BaseActivity implements OnClickListener {

    private TextView tv_back, tv_search;
    private EditText et_search;
    private ListView lv;
    private ListAdapter adapter;
    private ArrayList<Shipin> list = new ArrayList<Shipin>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        CommonUtils.initStatus(this);
        initView();

    }

    private void initView() {
        tv_back = (TextView) this.findViewById(R.id.tv_back);
        tv_search = (TextView) this.findViewById(R.id.tv_search);
        lv = (ListView) this.findViewById(R.id.lv);
        et_search = (EditText) this.findViewById(R.id.et_search);
        tv_back.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        adapter = new ListAdapter(this, list);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Shipin sp = list.get(arg2);
                Intent intent = new Intent(SearchActivity.this,
                        DetailActivity.class);
                intent.putExtra("filmId", sp.getFilmId());
                intent.putExtra("showUrl", sp.getShowUrl());
                intent.putExtra("name", sp.getName());
                intent.putExtra("type", sp.getType());
                intent.putExtra("actor", sp.getActor());
                intent.putExtra("director", sp.getDirector());
                intent.putExtra("content", sp.getContent());
                SearchActivity.this.startActivity(intent);

            }
        });
    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "7");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        params.put("word", et_search.getText().toString().trim());
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        dealData(new String(bytes));
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    }
                });
    }

    private void dealData(String response) {
        Gson gson = new GsonBuilder().create();
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray = object.get("list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonElement = jsonArray.get(i);
            Shipin shipin = gson.fromJson(jsonElement.getAsJsonObject(), Shipin.class);
            list.add(shipin);
        }
        list.clear();
        if (jsonArray.size() == 0) {
            Toast.makeText(this, "搜索结果为空", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                this.finish();
                break;
            case R.id.tv_search:
                hideSoftKeyboard(et_search);
                if (et_search.getText().toString().trim().length() == 0) {
                    Toast.makeText(this, "搜索关键字不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                getData();
                break;
            default:
                break;
        }
    }
}
