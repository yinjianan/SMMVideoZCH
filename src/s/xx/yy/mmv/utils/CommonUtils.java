package s.xx.yy.mmv.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import s.xx.yy.mmv.MainActivity;
import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.ui.DetailActivity;
import s.xx.yy.mmv.view.SystemBarTintManager;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.tandong.sa.zUImageLoader.core.DisplayImageOptions;
import com.tandong.sa.zUImageLoader.core.assist.ImageScaleType;

public class CommonUtils {
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static String getWifiIp(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress) + "";
    }

    private static String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }

    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getCurrentNetType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA
                    || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS
                    || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g";
            }
        }
        return type;

    }

    public static long getTotalMemorySize(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine
                    .indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getProvidersName(Context context) {
        try {
            String ProvidersName = null;
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            // 返回唯一的用户ID;就是这张卡的编号神马的
            String IMSI = tm.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            if (IMSI != null) {
                System.out.println(IMSI);
                if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                    ProvidersName = "中国移动";
                    ProvidersName = "1";
                } else if (IMSI.startsWith("46001")) {
                    ProvidersName = "中国联通";
                    ProvidersName = "2";
                } else if (IMSI.startsWith("46003")) {
                    ProvidersName = "中国电信";
                    ProvidersName = "3";
                }
                return ProvidersName;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "中国移动";
    }

    public static String getImsi(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSubscriberId();
    }

    public static void initStatus(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = context.getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(context);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.status);
            // tintManager.setNavigationBarTintEnabled(true);
            // tintManager.setNavigationBarTintResource(R.color.white);
        }
    }

    public static Shipin shipin;
    public static Context context;

    public static void startActivities() {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("shipin", shipin);
        context.startActivity(intent);
    }

    public static void pay(Shipin shipin, Context context) {
        CommonUtils.shipin = shipin;
        CommonUtils.context = context;
        SharedPreferences sp = context.getSharedPreferences("NUMBER", Activity.MODE_PRIVATE);
        if (sp.getBoolean("zhongshen", false)) {
            startActivities();
        } else {
            if (shipin.getVipFlag().equals("1")) {
                if (sp.getBoolean("nianfei", false)) {
                    showDialog(true);
                } else {
                    showDialog(false);
                }
            } else {
                if (sp.getBoolean("nianfei", false)) {
                    startActivities();
                } else {
                    showDialog(false);
                }
            }
        }
    }

    private static void showDialog(boolean isPutong) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (isPutong) {
            dialog.setContentView(R.layout.dialogtip_zhongshen);
            dialog.findViewById(R.id.dt_button_zhongshen).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.pay(1);
                    dialog.dismiss();
                }
            });
        } else {
            dialog.setContentView(R.layout.dialogtip);
            dialog.findViewById(R.id.dt_button_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.pay(0);
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.dt_button_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.pay(2);
                    dialog.dismiss();
                }
            });
        }
        dialog.findViewById(R.id.at_iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public static DisplayImageOptions createNoRoundedDisplayImageOptions(int defaultIconResId, boolean cacheOnDisk) {
        return new DisplayImageOptions.Builder().showImageOnLoading(defaultIconResId).showImageForEmptyUri(defaultIconResId).showImageOnFail(defaultIconResId).cacheInMemory(true).cacheOnDisk(cacheOnDisk).considerExifParams(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
    }
}
