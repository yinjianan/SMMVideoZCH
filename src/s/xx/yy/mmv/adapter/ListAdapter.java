package s.xx.yy.mmv.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.assist.FailReason;
import com.tandong.sa.zUImageLoader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import s.xx.yy.mmv.bean.Shipin;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class ListAdapter extends BaseAdapter {
	private ArrayList<Shipin> list;
	private Context context;
	private ImageLoader loader;

	public ListAdapter(Context c, ArrayList<Shipin> lists) {
		this.context = c;
		this.list = lists;
		loader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_title = (TextView) convertView
					.findViewById(R.id.tv_title);
			holder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Shipin sp = list.get(position);
		holder.tv_name.setText(sp.getTitle());
		holder.tv_title.setText(sp.getName());
		loader.displayImage(sp.getShowUrl(), holder.iv_img, new ImgLoad(
				holder.iv_img));
		return convertView;
	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_title;
		ImageView iv_img;
	}

	class ImgLoad implements ImageLoadingListener {
		private ImageView iv_img;

		public ImgLoad(ImageView iv) {
			this.iv_img = iv;
		}

		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			iv_img.setBackgroundColor(context.getResources().getColor(
					R.color.white));
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
		public void onLoadingStarted(String arg0, View arg1) {

			iv_img.setBackgroundColor(context.getResources().getColor(
					R.color.white));

			iv_img.setScaleType(ScaleType.CENTER);

			// iv_play.setVisibility(View.GONE);
			iv_img.setImageResource(R.drawable.loading);

		}

		@Override
		public void onLoadingFailed(String s, View view, FailReason failReason) {
			iv_img.setBackgroundColor(context.getResources().getColor(
					R.color.white));
			iv_img.setScaleType(ScaleType.CENTER);
			// iv_play.setVisibility(View.GONE);
			iv_img.setImageResource(R.drawable.loading);
		}
	}

}
