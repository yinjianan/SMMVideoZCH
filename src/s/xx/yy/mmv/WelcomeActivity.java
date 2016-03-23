package s.xx.yy.mmv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.umeng.analytics.game.UMGameAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.utils.CommonUtils;
import s.xx.yy.mmv.utils.NetUtils;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;


public class WelcomeActivity extends Activity {
    private String phoneNum, imsi, imei, ip, mac, model, mode, system, memory,
            service, manufacturers, iccid;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        if(isIllegalModified(this))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("尊敬的用户，您正在使用的是已经被非法破解后的客户端，请下载官方版本，谢谢您的支持");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.create().show();
        }
        init();
        UMGameAgent.init(this);
    }

    public static boolean isIllegalModified(Context context) {
        String packageName = context.getPackageName();
        if (!"ssmc.nsss.scS.mdsa.ieeq.mhdu".equals(packageName)) {
            return true;
        }
        String original = "308203353082021da0030201020204109fa828300d06092a864886f70d01010b0500304b3110300e060355040713076e616e6a696e673111300f060355040a13086e6577647265616d3111300f060355040b13086e6577647265616d3111300f060355040313086e6577647265616d301e170d3136303331363032353230395a170d3431303331303032353230395a304b3110300e060355040713076e616e6a696e673111300f060355040a13086e6577647265616d3111300f060355040b13086e6577647265616d3111300f060355040313086e6577647265616d30820122300d06092a864886f70d01010105000382010f003082010a0282010100ad6e82287be4227a7fb4c7762c266e21834380f2de7bd2560eee9bf07339f40a353647cc2594078d0f3a7b32689711375e747f130a01daf4c2042fe4aebd67c74c1eeb2609812a0d07cc3ad34c9de1c339743091d8231ebac96fc5219e731cca54a0363ad0327797137e8881daa749ab76187e1ec6698e7a301505ee6a822c776849909cb047252aa4902e7dd55be75c1dc18c86fd5435903d4356880d4eb25a9cd4f4430a54750b8603e92fc20f9dfad6b7564fd7e475b3415ddea0ac83e7194f3f9f5587e57f1c30b3ef9341db7d6db0c1ef513eb513420c5089dc70c6f736ae6c56e249ce3125d97f83d3d993b46d1e5f9aeb21f5c73700bd382e0926e46f0203010001a321301f301d0603551d0e04160414281a66869e7da5ab42d56f15e8a95f3265593b88300d06092a864886f70d01010b050003820101000cc58650b06cd9e48d75cc9f63fb9525ffa8d920ac72595bb7014a1820ec1cb2ae82d409f7f0fde0379d57137a0effc7e52a23a036c36c31682cf324af46b134b4ca4b91f86ce7bcb43caafbb2758a58124f572166a7264d0530bc142b71bf1f3a8d6877a1848e1a4650ab3ee79d955ae334a562a5fe038e5a512ea52d813c6a0ab489c85a36f897e1e6bd82bc537513191468c36078d2d45dc7be867d9a28756e427e9ecb29f49c264f5ada66252a1ea15564b8c485c466c3c544726415100d528b63e650681de7d9267f87d6c1c564d1907e4e45d5b8fbe724e56292d2c46f35045eff43d929004ca216b5f41d3751f9183d6fca1efe67a6dc275bacbb903d";
        PackageInfo pis;
        try {
            pis = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] sigs = pis.signatures;
            String sig = new String(sigs[0].toChars());
            return !original.equalsIgnoreCase(sig);
//            return false;
        } catch (PackageManager.NameNotFoundException e) { // TODO Auto-generated catch block
        }
        return true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        UMGameAgent.onResume(this);
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        UMGameAgent.onPause(this);
    }


    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "1");
        params.put("encrypt", "none");
        params.put("phoneNum", phoneNum);
        params.put("imsi", imsi);
        params.put("imei", imei);
        params.put("ip", ip);
        params.put("mac", mac);
        params.put("model", model);
        params.put("mode", mode);
        params.put("system", system);
        params.put("memory", memory);
        params.put("service", service);
        params.put("manufacturers", manufacturers);
        params.put("iccid", iccid);
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        dealData(new String(bytes));
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        Toast.makeText(WelcomeActivity.this, "网络请求出错，请检查网络连接",
                                Toast.LENGTH_LONG).show();
                        Conf.BASE_URL = Conf.BASE_URL1;
                        if (i++ < 2)
                            getData();
                        else
                            finish();
                    }
                });
    }


    private void dealData(String response) {
        JSONObject object = null;
        try {
            object = new JSONObject(response);
            MainActivity._name = object.getString("name");
            MainActivity._url = object.getString("videoUrl");
        } catch (JSONException e) {
            MainActivity._name = "前戏";
            MainActivity._url = "http://queshuwen.oss-cn-hangzhou.aliyuncs.com/video%2F%E8%BD%BD%E5%85%A5%E5%8A%A8%E7%94%BB.mp4";
            e.printStackTrace();
        }

    }

    private void init() {
        TelephonyManager tm = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        phoneNum = tm.getLine1Number();
        if (phoneNum == null) {
            Toast.makeText(this, "您的手机没有SIM卡，暂时无法使用", Toast.LENGTH_LONG)
                    .show();
            //return;
            phoneNum = "0";
        }
        //imsi = "";
        imsi = tm.getSubscriberId();
        Conf.IMSI = imsi;
//		if(phoneNum==null)
//		{
//			if (phoneNum.length() == 0) {
//				phoneNum = imsi;
//			}
//		}

        if (imsi == null) {
            imsi = "";
            Conf.IMSI = "";
            System.out.println("");
        }

        imei = tm.getDeviceId();
        if (CommonUtils.isWifi(this)) {
            ip = CommonUtils.getWifiIp(this);
        } else {
            ip = CommonUtils.getLocalIpAddress();
        }

        mac = CommonUtils.getLocalMacAddressFromWifiInfo(this);
        model = android.os.Build.MODEL;
        mode = CommonUtils.getCurrentNetType(this);
        system = android.os.Build.VERSION.RELEASE;
        memory = CommonUtils.getTotalMemorySize(this) + "";
        service = CommonUtils.getProvidersName(this);
        manufacturers = android.os.Build.MANUFACTURER;
        iccid = tm.getSimSerialNumber();

        getData();
    }
}
