package s.xx.yy.mmv.adapter;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.tandong.sa.json.Gson;
import com.tandong.sa.json.GsonBuilder;
import com.tandong.sa.json.JsonArray;
import com.tandong.sa.json.JsonElement;
import com.tandong.sa.json.JsonObject;
import com.tandong.sa.json.JsonParser;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.zUImageLoader.core.assist.FailReason;
import com.tandong.sa.zUImageLoader.core.listener.ImageLoadingListener;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import s.xx.yy.mmv.MainActivity;
import s.xx.yy.mmv.base.BaseApplication;
import s.xx.yy.mmv.bean.Cats;
import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.utils.NetUtils;
import s.xx.yy.mmv.view.NoScrollGridView;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class MainAdapter extends BaseAdapter {
    private List<Cats> list;
    private FragmentActivity context;

    public MainAdapter(FragmentActivity c, List<Cats> lists) {
        this.context = c;
        this.list = lists;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final Cats cats = list.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_main, null);
            holder = new ViewHolder();
            holder.tv_more = (TextView) convertView.findViewById(R.id.tv_more);
            holder.tv_cat = (TextView) convertView.findViewById(R.id.tv_cat);
            holder.gv_main = (NoScrollGridView) convertView.findViewById(R.id.im_gv);
            getdata(cats, holder.gv_main);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_cat.setText(cats.getName());
        holder.tv_more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MainActivity activity = (MainActivity) context;
                activity.radioButton.setChecked(true);
                activity.setCats(cats);
            }
        });
        return convertView;
    }

    private void getdata(final Cats cats, final NoScrollGridView gv_main) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "11");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        params.put("specialId", cats.getSpecialId());
        params.put("pageNo", "1");
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        dealData(new String(bytes), gv_main, cats);
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    }
                });
    }

    protected void dealData(String response, NoScrollGridView gv_main, Cats cats) {
        Gson gson = new GsonBuilder().create();
        Log.e("dealData",response);
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        if (object.get("list") != null) {
            JsonArray jsonArray = object.get("list").getAsJsonArray();
            List<Shipin> shipins = new ArrayList<Shipin>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                Shipin shipin = gson.fromJson(jsonElement.getAsJsonObject(), Shipin.class);
                shipins.add(shipin);
            }
            MyGVAdapter myAdapter = new MyGVAdapter(shipins, context);
            gv_main.setAdapter(myAdapter);
        } else {
            getdata(cats, gv_main);
        }
    }

    class ViewHolder {
        TextView tv_cat;
        TextView tv_more;
        NoScrollGridView gv_main;
    }


}
