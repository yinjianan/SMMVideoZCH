package s.xx.yy.mmv.base;

import s.xx.yy.mmv.constants.Conf;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.tandong.sa.zUImageLoader.cache.disc.impl.UnlimitedDiscCache;
import com.tandong.sa.zUImageLoader.cache.disc.naming.Md5FileNameGenerator;
import com.tandong.sa.zUImageLoader.cache.memory.MemoryCache;
import com.tandong.sa.zUImageLoader.cache.memory.impl.LRULimitedMemoryCache;
import com.tandong.sa.zUImageLoader.cache.memory.impl.LruMemoryCache;
import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.ImageLoader;
import com.tandong.sa.zUImageLoader.core.ImageLoaderConfiguration;
import com.tandong.sa.zUImageLoader.core.assist.QueueProcessingType;
import com.tandong.sa.zUImageLoader.utils.StorageUtils;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {

		super.onCreate();
		initImagloader(getApplicationContext());
		Conf.LOG_MODE = true;// 调试输出日志模式
	}


	private void initImagloader(Context context) {
		int memoryCacheSize;

		MemoryCache memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			int memClass = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 8) * 1024 * 1024; // 1/8 of app memory
															// limit
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCache(memoryCache)
				.discCache(
						new UnlimitedDiscCache(StorageUtils
								.getOwnCacheDirectory(context,
										"MMVideo/ImageCache")))

				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)

				.defaultDisplayImageOptions(getDefaultImageOptions())
				// .enableLogging() // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public DisplayImageOptions getDefaultImageOptions() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory().cacheOnDisc()
				.showStubImage(R.drawable.loading)
				// .displayer(new RoundedBitmapDisplayer(5))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return defaultOptions;
	}
}
