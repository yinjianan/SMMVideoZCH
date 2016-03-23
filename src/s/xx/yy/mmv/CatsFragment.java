package s.xx.yy.mmv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import java.util.List;
import java.util.Map;

import s.xx.yy.mmv.adapter.MyGVAdapter;
import s.xx.yy.mmv.base.BaseApplication;
import s.xx.yy.mmv.bean.Cats;
import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.ui.DetailActivity;
import s.xx.yy.mmv.utils.CommonUtils;
import s.xx.yy.mmv.utils.NetUtils;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class CatsFragment extends Fragment {
    private PullToRefreshListView lv;
    private MyGVAdapter adapter;
    private ArrayList<Shipin> shipins = new ArrayList<Shipin>();
    private View view;
    private List<Cats> catsLists = new ArrayList<Cats>();
    private LayoutInflater inflater;
    private int i = 1;
    private int j = 0;
    private MainActivity activity;
    private RadioGroup group;
    private Cats cats;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.activity_list, null);
        initView();
        getList();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (activity.getCats() != null && group != null) {
                cats = activity.getCats();
                for (int ii = 0; ii < catsLists.size(); ii++) {
                    if (catsLists.get(ii).getName().equals(cats.getName())) {
                        group.check(ii);
                        break;
                    }
                }
            }
        }
    }

    private void getListleft() {
        activity = (MainActivity) getActivity();
        if (activity.getCats() != null) {
            cats = activity.getCats();
            for (int ii = 0; ii < catsLists.size(); ii++) {
                if (catsLists.get(ii).getName().equals(cats.getName())) {
                    j = ii;
                    break;
                }
            }
        } else
            cats = catsLists.get(j);
        group = (RadioGroup) view.findViewById(R.id.al_rg_type);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(-1, -2);
        for (int i = 0; i < catsLists.size(); i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.cf_rg_item, null);
            radioButton.setId(i);
            radioButton.setText(catsLists.get(i).getName());
            radioButton.setLayoutParams(layoutParams);
            group.addView(radioButton);
        }
        final MainActivity activity = (MainActivity) getActivity();
        group.check(j);
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                j = checkedId;
                shipins.clear();
                cats = catsLists.get(j);
                getListRight(1);
                i = 1;
            }
        });
    }

    private void getList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "6");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        dealData2(new String(bytes));
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    }
                });
    }

    protected void dealData2(String response) {
        Gson gson = new GsonBuilder().create();
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray = object.get("list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonElement = jsonArray.get(i);
            Cats cats = gson.fromJson(jsonElement.getAsJsonObject(), Cats.class);
            catsLists.add(cats);
        }
        getListleft();
        getListRight(1);
    }

    private void getListRight(final int i) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "11");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        params.put("specialId", cats.getSpecialId());
        params.put("pageNo", "" + i);
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
            shipins.add(shipin);
        }
        adapter.notifyDataSetChanged();
    }

    private void initView() {

        lv = (PullToRefreshListView) view.findViewById(R.id.lv);
        adapter = new MyGVAdapter(shipins, this);
        lv.setAdapter(adapter);
        lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                getListRight(++i);
            }
        });
        lv.setRefreshing(false);
    }


}
