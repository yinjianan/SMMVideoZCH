package s.xx.yy.mmv.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class ProtocolActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_protocol);
		TextView title = (TextView) findViewById(R.id.title);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type", 0);
		if (type == 1) {
			title.setText("个人账号");
			findViewById(R.id.ap_ll_pnumber_1).setVisibility(View.VISIBLE);
			findViewById(R.id.ap_ll_pnumber_2).setVisibility(View.VISIBLE);
			TextView number = (TextView) findViewById(R.id.ap_tv_number);
			number.setText(intent.getStringExtra("number"));
		} else if (type == 2) {
			title.setText("用户协议");
			findViewById(R.id.ap_sv_protocol).setVisibility(View.VISIBLE);
		} else if (type == 3) {
			title.setText("关于我们");
			findViewById(R.id.ap_rl_about).setVisibility(View.VISIBLE);
		}
		findViewById(R.id.ap_iv_back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
