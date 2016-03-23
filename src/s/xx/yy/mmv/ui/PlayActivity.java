package s.xx.yy.mmv.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import s.xx.yy.mmv.base.BaseActivity;
import s.xx.yy.mmv.base.BaseApplication;
import s.xx.yy.mmv.constants.Conf;
import s.xx.yy.mmv.utils.CommonUtils;
import s.xx.yy.mmv.utils.NetUtils;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.VideoView;

import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;


public class PlayActivity extends BaseActivity implements OnClickListener {
    private Intent intent;
    private TextView tv_content, play_time, total_time;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();
        CommonUtils.initStatus(this);
        getData();
    }

    private void initView() {
        tv_content = (TextView) findViewById(R.id.tv_content);
        play_time = (TextView) findViewById(R.id.play_time);
        total_time = (TextView) findViewById(R.id.total_time);
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        video = (VideoView) findViewById(R.id.video);
        play_btn = (ImageView) findViewById(R.id.play_btn);
        iv_full = (ImageView) findViewById(R.id.iv_full);
        intent = getIntent();
        mBottomView = findViewById(R.id.bottom_layout);
        mTopView = findViewById(R.id.top_layout);
        play_btn.setOnClickListener(this);
        iv_full.setOnClickListener(this);
        seekbar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        video.setVideoURI(Uri.parse(intent.getExtras().getString("videoUrl")));
        videoUrl = intent.getExtras().getString("videoUrl");
        videoName = intent.getExtras().getString("name");
        tv_content.setText(intent.getExtras().getString("content"));
        video.start();
        video.requestFocus();
        filmId = intent.getExtras().getString("filmId");
        videoId = intent.getExtras().getString("videoId");
        video.setOnPreparedListener(new OnPreparedListener() {
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
        video.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play_btn.setImageResource(R.drawable.video_btn_down);
                play_time.setText("00:00");
                seekbar.setProgress(0);
            }
        });
        video.setOnTouchListener(mTouchListener);
    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("actionName", "8");
        params.put("encrypt", "none");
        params.put("imsi", Conf.IMSI);
        params.put("fileId", filmId);
        params.put("videoId", videoId);
        JSONObject object = new JSONObject(params);
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("paramMap", object.toString());
        NetUtils.getInstance().post(Conf.BASE_URL, new RequestParams(params2),
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    }
                });

    }

    private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {

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
    private Runnable hideRunnable = new Runnable() {

        @Override
        public void run() {
            showOrHide();
        }
    };
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (video.getCurrentPosition() > 0) {
                        play_time.setText(formatTime(video.getCurrentPosition()));
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

    @SuppressLint("SimpleDateFormat")
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

    private float mLastMotionX;
    private float mLastMotionY;
    private int startX;
    private int startY;
    private int threshold;
    private boolean isClick = true;
    private OnTouchListener mTouchListener = new OnTouchListener() {

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

    private class AnimationImp implements AnimationListener {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.iv_full:
                Intent intent = new Intent(this, FullPlayActivity.class);
                intent.putExtra("filmId", filmId);
                intent.putExtra("videoId", videoId);
                intent.putExtra("videoUrl", videoUrl);
                intent.putExtra("name", videoName);
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
