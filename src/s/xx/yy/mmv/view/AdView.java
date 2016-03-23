package s.xx.yy.mmv.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;


import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.assist.FailReason;
import com.tandong.sa.zUImageLoader.core.listener.ImageLoadingListener;

import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.ui.DetailActivity;
import s.xx.yy.mmv.utils.CommonUtils;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class AdView implements OnClickListener {

    private View view;
    private Context c;
    private Shipin shipin;
    private TextView tv_shadow;
    private ImageView iv_img;
    private ImageLoader loader;
    private int position;

    public AdView(Context context, Shipin shipin, int i) {
        this.c = context;
        this.shipin = shipin;
        position = i;
    }

    public View onCreateView() {
        view = View.inflate(c, R.layout.item_ad, null);
        iv_img = (ImageView) view.findViewById(R.id.iv_img);
        tv_shadow = (TextView) view.findViewById(R.id.tv_shadow);
        view.setOnClickListener(this);
        loader = ImageLoader.getInstance();
        pareseData();
        return view;
    }

    private void pareseData() {
        loader.displayImage(shipin.getShowUrl(), iv_img, new ImgLoad(iv_img));
        tv_shadow.setText(shipin.getTitle());
    }

    @Override
    public void onClick(View arg0) {
        if (position == 0) {
            Intent intent = new Intent(c, DetailActivity.class);
            intent.putExtra("shipin", shipin);
            intent.putExtra("shibo", true);
            c.startActivity(intent);
        } else
            CommonUtils.pay(shipin, c);
    }

    class ImgLoad implements ImageLoadingListener {
        private ImageView iv_img;

        public ImgLoad(ImageView iv) {
            this.iv_img = iv;
        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {
            iv_img.setBackgroundColor(c.getResources().getColor(R.color.white));
            iv_img.setScaleType(ScaleType.CENTER);
            // iv_play.setVisibility(View.GONE);
            iv_img.setImageResource(R.drawable.loading);

        }

        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
            iv_img.setImageBitmap(arg2);
            iv_img.setScaleType(ScaleType.CENTER_CROP);
            // iv_play.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            iv_img.setBackgroundColor(c.getResources().getColor(R.color.white));
            iv_img.setScaleType(ScaleType.CENTER);
            // iv_play.setVisibility(View.GONE);
            iv_img.setImageResource(R.drawable.loading);

        }

        @Override
        public void onLoadingStarted(String arg0, View arg1) {

            iv_img.setBackgroundColor(c.getResources().getColor(R.color.white));

            iv_img.setScaleType(ScaleType.CENTER);

            // iv_play.setVisibility(View.GONE);
            iv_img.setImageResource(R.drawable.loading);

        }
    }
}
