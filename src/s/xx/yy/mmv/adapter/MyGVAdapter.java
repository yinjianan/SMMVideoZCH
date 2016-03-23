package s.xx.yy.mmv.adapter;

import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.tandong.sa.tools.AssistTool;
import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.assist.FailReason;
import com.tandong.sa.zUImageLoader.core.listener.ImageLoadingListener;

import s.xx.yy.mmv.CatsFragment;
import s.xx.yy.mmv.MainActivity;
import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.ui.DetailActivity;
import s.xx.yy.mmv.utils.CommonUtils;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class MyGVAdapter extends BaseAdapter {
    private List<Shipin> shipins;
    private Fragment fragment;
    private ImageLoader loader;
    private Context context;
    private SharedPreferences sp;
    private static DisplayImageOptions noRoundedDisplayImageOptions;

    public MyGVAdapter(List<Shipin> shipins, Fragment fragment) {
        this.shipins = shipins;
        this.fragment = fragment;
        noRoundedDisplayImageOptions = CommonUtils.createNoRoundedDisplayImageOptions(R.drawable.loading, true);
        this.context = fragment.getActivity();
        loader = ImageLoader.getInstance();
        sp = context.getSharedPreferences("NUMBER", Activity.MODE_PRIVATE);
    }

    public MyGVAdapter(List<Shipin> shipins, Context context) {
        this.shipins = shipins;
        this.context = context;
        loader = ImageLoader.getInstance();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SPViewHolder spHolder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_main_gridview, null);
            spHolder = new SPViewHolder();
            spHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            spHolder.im_tv_title = (TextView) convertView.findViewById(R.id.im_tv_title);
            spHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            spHolder.im_iv_1 = (ImageView) convertView.findViewById(R.id.im_iv_1);
            spHolder.im_iv_2 = (ImageView) convertView.findViewById(R.id.im_iv_2);
            spHolder.img_iv_vp = (ImageView) convertView.findViewById(R.id.img_iv_vp);
            convertView.setTag(spHolder);
        } else {
            spHolder = (SPViewHolder) convertView.getTag();
        }
        final Shipin shipin = shipins.get(position);
        spHolder.tv_name.setText(shipin.getName());
        spHolder.im_tv_title.setText(shipin.getTitle());
        if (shipin.getVipFlag()!=null&&shipin.getVipFlag().equals("1")) {
            spHolder.img_iv_vp.setVisibility(View.VISIBLE);
        }
        loader.displayImage(shipin.getImgUrl(), spHolder.iv_img,noRoundedDisplayImageOptions);
        loader.displayImage(shipin.getLabel1(), spHolder.im_iv_1,noRoundedDisplayImageOptions);
        loader.displayImage(shipin.getLabel2(), spHolder.im_iv_2,noRoundedDisplayImageOptions);
        convertView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtils.pay(shipin,context);
            }
        });
        if (fragment instanceof CatsFragment) {
            AssistTool assistTool=new AssistTool(context);

            spHolder.iv_img.setLayoutParams(new RelativeLayout.LayoutParams(-1, assistTool.dip2px(context,110)));
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return shipins.get(position);
    }

    @Override
    public int getCount() {
        return shipins.size();
    }

    class SPViewHolder {
        ImageView iv_img;
        TextView tv_name;
        ImageView im_iv_1;
        ImageView im_iv_2;
        TextView im_tv_title;
        ImageView img_iv_vp;
    }

}
