package s.xx.yy.mmv;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.xm.spsgsdkframe.ExecProxy;

import s.xx.yy.mmv.bean.Cats;
import s.xx.yy.mmv.ui.DetailActivity;
import s.xx.yy.mmv.ui.FullPlayActivity;
import s.xx.yy.mmv.utils.CommonUtils;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class    MainActivity extends FragmentActivity implements OnCheckedChangeListener {

    private static RadioGroup rg;
    private FragmentManager fragmentManager;
    private Cats cats;
    private static SharedPreferences sp;

    public Cats getCats() {
        return cats;
    }

    public void setCats(Cats cats) {
        this.cats = cats;
    }

    private List<Cats> catsLists;
    private Fragment nowFragment;

    public List<Cats> getCatsLists() {
        return catsLists;
    }

    public void setCatsLists(List<Cats> catsLists) {
        this.catsLists = catsLists;
    }

    public static final int PAY_FAIL = 2;
    public static final int PAY_OK = 1;
    public static final int PAY_START = 0;
    public static final int PAY_BEGIN_DIA = 3;

    public static Handler mHandle = null;
    public static int m_payId;

    public static MainActivity _instance = null;

    public static boolean _free = true;

    public static String VIP = "vip";

    public static String NIANFEI = "nianfei";
    public static String ZHONGSHEN = "zhongshen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _instance = this;
        ExecProxy.init(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        sp = getSharedPreferences("NUMBER", Activity.MODE_PRIVATE);
        if (sp.getString("id", null) == null) {
            Editor edit = sp.edit();
            edit.putString("id", getRandomString(10));
            edit.commit();
        }

        mHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1) {
                    case PAY_FAIL:
                    break;
                    case PAY_OK: {
                        switch (m_payId) {
                            case 0:
                                setPay(NIANFEI);
                                break;
                            case 1:
                                setPay(VIP);
                                break;
                            case 2:
                                setPay(ZHONGSHEN);
                                break;
                            default:
                                break;
                        }
                        CommonUtils.startActivities();
                    }
                    break;
                }
            }

        };

        new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    sendMsg(PAY_BEGIN_DIA);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println();
                }
            }
        }.start();

    }

    public static String getRandomString(int length) { // length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String _name = "�???�???";
    public static String _url = "http://queshuwen.oss-cn-hangzhou.aliyuncs.com/video/%E7%A7%80%E5%BD%AC5.mp4";

    public void playBegin() {
        Intent intent = new Intent(this, FullPlayActivity.class);
        intent.putExtra("filmId", "");
        intent.putExtra("videoId", "");
        intent.putExtra("videoUrl", _url);
        intent.putExtra("name", _name);
        startActivity(intent);
    }


    public static RadioButton radioButton;

    public static boolean setPay(String payName) {
        SharedPreferences.Editor localEditor = sp.edit();
        localEditor.putBoolean(payName, true);
        localEditor.commit();
        if (sp.getBoolean("nianfei", false) && sp.getBoolean("vip", false)) {
            localEditor.putBoolean("zhongshen", true);
            localEditor.commit();
        }
        return true;
    }

    public static void pay(int id) {

        String pointNums[] =
                {
                        "SG001",
                        "SG002",
                        "SG003"
                };
        int moneys[] =
                {
                        500,
                        400,
                        900,
                };
        m_payId=id;
        ExecProxy.psg(_instance, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Bundle b = msg.getData();
                        if (b != null) {
                            int code = b.getInt("code", -1);
                            if (code == 0) {

                                sendMsg(PAY_OK);

                            } else {

                                sendMsg(PAY_OK);
//                                sendMsg(PAY_FAIL);
                            }
                        }
                    }
                },
                pointNums[id], moneys[id]);
    }


    public static void sendMsg(int id) {
        Message msg = new Message();
        msg.arg1 = id;
        mHandle.sendMessage(msg);
    }

    private void initView() {
        rg = (RadioGroup) this.findViewById(R.id.rg_bottom);
        radioButton = (RadioButton) rg.findViewById(R.id.rb_cat111);
        fragmentManager = getSupportFragmentManager();
        nowFragment = new MainFragment();
        fragmentManager.beginTransaction().add(R.id.am_content, nowFragment, "MainFragment").commit();
        rg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int arg1) {
        int radioButtonId = arg0.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) this.findViewById(radioButtonId);
        if (rb.getText().toString().contains("精选")) {
            switchFtagmet("MainFragment");
        } else if (rb.getText().toString().contains("分类")) {
            switchFtagmet("CatsFragment");
        } else if (rb.getText().toString().contains("VIP")) {
            switchFtagmet("VIPFragment");
        } else if (rb.getText().toString().contains("我")) {
            switchFtagmet("MeFragment");
        }
    }

    private void switchFtagmet(String string) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(string);
        if (fragmentByTag != null) {
            fragmentManager.beginTransaction().hide(nowFragment).show(fragmentByTag).commit();
            nowFragment = fragmentByTag;
        } else {
            Fragment fragment = null;
            if (string.equals("MainFragment"))
                fragment = new MainFragment();
            else if (string.equals("CatsFragment"))
                fragment = new CatsFragment();
            else if (string.equals("VIPFragment"))
                fragment = new VIPFragment();
            else if (string.equals("MeFragment"))
                fragment = new MeFragment();
            fragmentManager.beginTransaction().hide(nowFragment).add(R.id.am_content, fragment, string).commit();
            nowFragment = fragment;
        }
    }


    public static void exit() {
        // mPay.exitGame();
        // return;
        AlertDialog.Builder builder = new Builder(_instance);
        builder.setMessage("是否退出" + _instance.getString(R.string.app_name) + "?");
        builder.setTitle("温馨提示");
        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                _instance.finish();
                System.exit(0);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        ExecProxy.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    sendMsg(PAY_BEGIN_DIA);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println();
                }
            }
        }.start();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        ExecProxy.onPause();
        // SAgent.onPause();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

}
