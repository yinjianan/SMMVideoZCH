package s.xx.yy.mmv.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import s.xx.yy.mmv.adapter.MyGVAdapter;
import s.xx.yy.mmv.bean.Shipin;
import s.xx.yy.mmv.bean.Videos;
import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.utils.CommonUtils;
import s.xx.yy.mmv.utils.NetUtils;
import s.xx.yy.mmv.view.NoScrollGridView;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class DetailActivity extends FragmentActivity implements OnClickListener {
    private TextView tv_back;
    private ArrayList<Videos> lists;
    private String content = "";
    public static DetailActivity _instance;
    private ImageView iv_play;
    private TextView iv_content;
    private ImageLoader loader;
    private Shipin shipin;
    private List<Shipin> shipins = new ArrayList<Shipin>();
    ;
    private NoScrollGridView ad_agv;
    private MyGVAdapter adapter;
    private TextView ad_tv_name;
    private boolean shibo;
    private TextView ad_tv_shibo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        _instance = this;
        shibo = getIntent().getBooleanExtra("shibo", false);
        initView();
        getData();
    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "11");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        params.put("specialId", shipin.getSpecialId());
        params.put("pageNo", "1");
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        dealData1(new String(bytes));
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    }
                });
        params = new HashMap<String, String>();
        params.put("actionName", "4");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        params.put("filmId", shipin.getFilmId());
        object = new JSONObject(params);
        params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        dealData(new String(bytes));
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    }
                });
    }

    private void dealData1(String response) {
        Gson gson = new GsonBuilder().create();
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        if (object.get("list") == null)
            getData();
        else {
            JsonArray jsonArray = object.get("list").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                Shipin shipin = gson.fromJson(jsonElement.getAsJsonObject(), Shipin.class);
                if (shipins.size() < 4)
//                    if (!shipin.getFilmId().equals(this.shipin.getFilmId()))
                    shipins.add(shipin);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private void dealData(String response) {
        lists = new ArrayList<Videos>();
        Gson gson = new GsonBuilder().create();
        JsonObject object = new JsonParser().parse(response).getAsJsonObject();
        if (object.get("list") == null)
            getData();
        else {
            JsonArray jsonArray = object.get("list").getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonElement jsonElement = jsonArray.get(i);
                Videos videos = gson.fromJson(jsonElement.getAsJsonObject(), Videos.class);
                lists.add(videos);
            }
        }
    }

    private void initView() {
        loader = ImageLoader.getInstance();
        tv_back = (TextView) findViewById(R.id.tv_back);
        iv_play = (ImageView) findViewById(R.id.ad_iv_play);
        iv_content = (TextView) findViewById(R.id.ad_tv_content);
        ad_agv = (NoScrollGridView) findViewById(R.id.ad_ngv);
        ad_tv_name = (TextView) findViewById(R.id.ad_tv_name);
        adapter = new MyGVAdapter(shipins, this);
        ad_agv.setAdapter(adapter);
        iv_play.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        shipin = (Shipin) getIntent().getSerializableExtra("shipin");
        loader.displayImage(shipin.getShowUrl(), iv_play);
        ad_tv_name.setText(shipin.getName());
        iv_content.setText(shipin.getContent());
    }

    public static int _position = 0;

    public void play() {
        if (lists.get(0).getVideoType().equals("1")) {
            findViewById(R.id.ad_rl_play).setVisibility(View.GONE);
            initView2();
        } else if (lists.get(0).getVideoType().equals("2")) {
            Uri uri = Uri.parse(lists.get(0).getVideoUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }


    private TextView play_time, total_time;
    private VideoView video;
    private String filmId, videoId, videoUrl, videoName;
    private MediaController mc;
    private ImageView play_btn, iv_full;
    private SeekBar seekbar;
    private View mBottomView;
    private View mTopView;
    // 视频播放时间
    private int playTime;
    private static final int HIDE_TIME = 5000;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (video.getCurrentPosition() > 0) {
                        play_time.setText(formatTime(video.getCurrentPosition()));
                        if (shibo) {
                            if (video.getCurrentPosition()/1000 < 20) {
                                ad_tv_shibo.setText("剩余播放时间" + (20 -video.getCurrentPosition()/1000) + "秒");
                            }else {
                                if (video.isPlaying()) {
                                    video.pause();
                                    CommonUtils.pay(shipin, DetailActivity.this);
                                    ad_tv_shibo.setText("剩余播放时间0秒");
                                    play_btn.setImageResource(R.drawable.video_btn_down);
                                }
                            }
                        }
                        int progress = video.getCurrentPosition() * 100
                                / video.getDuration();
                        seekbar.setProgress(progress);
                        if (video.getCurrentPosition() > video.getDuration() - 100) {
                            play_time.setText("00:00");
                            seekbar.setProgress(0);
                        }
                        seekbar.setSecondaryProgress(video.getBufferPercentage());
                    } else {
                        play_time.setText("00:00");
                        seekbar.setProgress(0);
                    }

                    break;
                case 2:
                    showOrHide();
                    break;

                default:
                    break;
            }
        }
    };
    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };

    private void showOrHide() {
        if (mTopView.getVisibility() == View.VISIBLE) {
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_top);
            animation.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mTopView.setVisibility(View.GONE);
                }
            });
            mTopView.startAnimation(animation);

            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_leave_from_bottom);
            animation1.setAnimationListener(new AnimationImp() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    super.onAnimationEnd(animation);
                    mBottomView.setVisibility(View.GONE);
                }
            });
            mBottomView.startAnimation(animation1);
        } else {
            mTopView.setVisibility(View.VISIBLE);
            mTopView.clearAnimation();
            Animation animation = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_top);
            mTopView.startAnimation(animation);

            mBottomView.setVisibility(View.VISIBLE);
            mBottomView.clearAnimation();
            Animation animation1 = AnimationUtils.loadAnimation(this,
                    R.anim.option_entry_from_bottom);
            mBottomView.startAnimation(animation1);
            mHandler.removeCallbacks(hideRunnable);
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }
    }

    private class AnimationImp implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    private String formatTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        long min = time / (1000 * 60);
        if (min > 60) {
            long m = (min - ((min / 60) * 60));
            long s = ((time / 1000) - (min * 60));
            String mm = m + "";
            String ss = s + "";
            if (m < 10) {
                mm = "0" + m;
            }
            if (s < 10) {
                ss = "0" + s;
            }
            return (min / 60) + ":" + mm + ":" + ss;
        } else if (min >= 1 && min <= 60) {
            // return min + ":" + ((time / 1000) - (min * 60));
            return formatter.format(new Date(time));
        } else if (min < 1) {
            // return "00" + (time / 1000);
            return formatter.format(new Date(time));
        } else {
            // return "00";
            return formatter.format(new Date(time));
        }
        // return formatter.format(new Date(time));
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mHandler.postDelayed(hideRunnable, HIDE_TIME);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            mHandler.removeCallbacks(hideRunnable);
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            if (fromUser) {
                int time = progress * video.getDuration() / 100;
                video.seekTo(time);
            }
        }
    };

    private void initView2() {
        findViewById(R.id.ad_rl_video).setVisibility(View.VISIBLE);
        play_time = (TextView) findViewById(R.id.play_time);
        total_time = (TextView) findViewById(R.id.total_time);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        video = (VideoView) findViewById(R.id.video);
        ad_tv_shibo = (TextView) findViewById(R.id.ad_tv_shibo);
        play_btn = (ImageView) findViewById(R.id.play_btn);
        iv_full = (ImageView) findViewById(R.id.iv_full);
        mBottomView = findViewById(R.id.bottom_layout);
        mTopView = findViewById(R.id.top_layout);
        play_btn.setOnClickListener(this);
        iv_full.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        videoUrl = lists.get(0).getVideoUrl();
        video.setVideoURI(Uri.parse(videoUrl));
        videoName = lists.get(0).getName();
        video.start();
        video.requestFocus();
        filmId = lists.get(0).getFilmId();
        videoId = lists.get(0).getVideoId();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                video.start();
                if (playTime != 0) {
                    video.seekTo(playTime);
                }

                mHandler.removeCallbacks(hideRunnable);
                mHandler.postDelayed(hideRunnable, HIDE_TIME);
                total_time.setText(formatTime(video.getDuration()));
                Log.i("info",
                        video.getDuration() + ","
                                + formatTime(video.getDuration()));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(1);
                    }
                }, 0, 1000);
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play_btn.setImageResource(R.drawable.video_btn_down);
                play_time.setText("00:00");
                seekbar.setProgress(0);
            }
        });
        video.setOnTouchListener(mTouchListener);
    }

    private float mLastMotionX;
    private float mLastMotionY;
    private int startX;
    private int startY;
    private int threshold;
    private boolean isClick = true;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final float x = event.getX();
            final float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastMotionX = x;
                    mLastMotionY = y;
                    startX = (int) x;
                    startY = (int) y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mLastMotionX;
                    float deltaY = y - mLastMotionY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);
                    mLastMotionX = x;
                    mLastMotionY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(x - startX) > threshold
                            || Math.abs(y - startY) > threshold) {
                        isClick = false;
                    }
                    mLastMotionX = 0;
                    mLastMotionY = 0;
                    startX = (int) 0;
                    if (isClick) {
                        showOrHide();
                    }
                    isClick = true;
                    break;

                default:
                    break;
            }
            return true;
        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ad_iv_play:
                play();
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_full:
                Intent intent = new Intent(this, FullPlayActivity.class);
                intent.putExtra("filmId", filmId);
                intent.putExtra("videoId", videoId);
                intent.putExtra("videoUrl", videoUrl);
                intent.putExtra("name", videoName);
                intent.putExtra("shipin",shipin);
                if(shibo)
                    intent.putExtra("shibo", true);
                startActivity(intent);
                break;
            case R.id.play_btn:
                if (video.isPlaying()) {
                    video.pause();
                    play_btn.setImageResource(R.drawable.video_btn_down);
                } else {
                    video.start();
                    play_btn.setImageResource(R.drawable.video_btn_on);
                }
                break;
            default:
                break;
        }

    }


}