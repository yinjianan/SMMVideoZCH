package s.xx.yy.mmv;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tandong.sa.json.Gson;
import com.tandong.sa.json.GsonBuilder;
import com.tandong.sa.json.JsonArray;
import com.tandong.sa.json.JsonElement;
import com.tandong.sa.json.JsonObject;
import com.tandong.sa.json.JsonParser;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.zUImageLoader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.ui.DetailActivity;
import s.xx.yy.mmv.utils.CommonUtils;
import s.xx.yy.mmv.utils.NetUtils;
import s.xx.yy.mmv.view.HorizontialListView;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class VIPFragment extends Fragment {

    private List<Shipin> lists = new ArrayList<Shipin>();
    LayoutInflater mInflater;
    private Myadapter myadapter;
    private SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        View view = inflater.inflate(R.layout.fragment_cat, null);
        HorizontialListView listView = (HorizontialListView) view.findViewById(R.id.fc_hl_list);
        sp = getActivity().getSharedPreferences("NUMBER", Activity.MODE_PRIVATE);
        myadapter = new Myadapter();
        listView.setAdapter(myadapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonUtils.pay(lists.get(position),getActivity());
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "10");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        dealMovies(new String(bytes));
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    }
                });
    }

    protected void dealMovies(String response) {
        Gson gson = new GsonBuilder().create();
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray = object.get("list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonElement jsonElement = jsonArray.get(i);
            Shipin shipin = gson.fromJson(jsonElement.getAsJsonObject(), Shipin.class);
            lists.add(shipin);
        }
        myadapter.notifyDataSetChanged();
    }

    class Myadapter extends BaseAdapter {
        private ImageLoader loader;

        public Myadapter() {
            loader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.vip_item, null);
                holder = new ViewHolder();
                holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(lists.get(position).getName());
            if (sp.getBoolean("movie_" + position, false))
                loader.displayImage(lists.get(position).getVipUnlock(), holder.iv_img);
            else
                loader.displayImage(lists.get(position).getVipLock(), holder.iv_img);
            return convertView;
        }

    }

    class ViewHolder {
        TextView tv_name;
        ImageView iv_img;
    }
}
