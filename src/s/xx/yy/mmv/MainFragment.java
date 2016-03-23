package s.xx.yy.mmv;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
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

import s.xx.yy.mmv.adapter.MainAdapter;
import s.xx.yy.mmv.bean.Cats;
import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.ui.SearchActivity;
import s.xx.yy.mmv.utils.NetUtils;
import s.xx.yy.mmv.view.AdView;
import s.xx.yy.mmv.view.AutoScrollViewPager;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class MainFragment extends Fragment
        implements OnPageChangeListener, OnRefreshListener<ListView>, OnLastItemVisibleListener, OnClickListener {

    private AutoScrollViewPager view_pager;
    private EditText et_search;
    private ArrayList<View> views = new ArrayList<View>();
    private PagerAdapter adAdapter;
    private RadioGroup rg;
    private PullToRefreshListView mPullRefreshListView;
    private MainAdapter adapter;
    private View headView;
    private List<Cats> catsLists = new ArrayList<Cats>();
    private List<Cats> catsListsShort = new ArrayList<Cats>();
    private int page = 1;
    private LayoutInflater inflater;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_main, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshListView) getView().findViewById(R.id.pull_refresh_list);
        et_search = (EditText) getView().findViewById(R.id.et_search);
        mPullRefreshListView.setOnRefreshListener(this);
        mPullRefreshListView.setOnLastItemVisibleListener(this);
        headView = View.inflate(getActivity(), R.layout.head_main, null);
        mPullRefreshListView.getRefreshableView().addHeaderView(headView);
        et_search.setOnClickListener(this);
        view_pager = (AutoScrollViewPager) headView.findViewById(R.id.view_pager);
        rg = (RadioGroup) getView().findViewById(R.id.rg);
        adAdapter = new MyViewPagerAdapter(views);
        view_pager.setAdapter(adAdapter);
        view_pager.setOnPageChangeListener(this);
        view_pager.setInterval(3000);
        view_pager.startAutoScroll();
        adapter = new MainAdapter(getActivity(), catsListsShort);
        mPullRefreshListView.setAdapter(adapter);
        mPullRefreshListView.setRefreshing(false);
    }

    private void getShortList(int page) {
        for (int i = (page - 1) * 2; i < page * 2; i++) {
            if (i < catsLists.size())
                catsListsShort.add(catsLists.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    private void getAds() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "2");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        String s = new String(bytes);
                        dealAds(s);
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

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
                        dealData(new String(bytes));
                        mPullRefreshListView.onRefreshComplete();
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        mPullRefreshListView.onRefreshComplete();
                    }
                });
    }

    protected void dealData(String response) {
        Gson gson = new GsonBuilder().create();
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray = object.get("list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonElement = jsonArray.get(i);
            Cats cats = gson.fromJson(jsonElement.getAsJsonObject(), Cats.class);
            catsLists.add(cats);
        }
        MainActivity activity = (MainActivity) getActivity();
        if (activity.getCatsLists() == null)
            activity.setCatsLists(catsLists);
        getShortList(page);
    }

    private void dealAds(String response) {
        Gson gson = new GsonBuilder().create();
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray = object.get("list").getAsJsonArray();
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(-2, -2);
        layoutParams.setMargins(10, 0, 10, 0);
        for (int i = 0; i < jsonArray.size(); i++) {
            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.ad_radiogroup_item, null);
            JsonElement jsonElement = jsonArray.get(i);
            Shipin shipin = gson.fromJson(jsonElement.getAsJsonObject(), Shipin.class);
            View av = new AdView(getActivity(), shipin,i).onCreateView();
            views.add(av);
            radioButton.setLayoutParams(layoutParams);
            rg.addView(radioButton);
        }
        RadioButton rb = (RadioButton) rg.getChildAt(0);
        rb.setChecked(true);
        adAdapter.notifyDataSetChanged();
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> mListViews;

        public MyViewPagerAdapter(ArrayList<View> mListViews) {
            this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));// 删除页卡
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
            container.addView(mListViews.get(position), 0);// 添加页卡
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();// 返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;// 官方提示这样写
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        try {
            RadioButton rb = (RadioButton) rg.getChildAt(position);
            rb.setChecked(true);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onLastItemVisible() {
        getShortList(++page);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
        catsLists.clear();
        views.clear();
        getAds();
        getList();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
