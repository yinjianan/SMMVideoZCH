package s.xx.yy.mmv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import s.xx.yy.mmv.ui.ProtocolActivity;

import android.view.ViewGroup;
import android.widget.TextView;

import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;

public class MeFragment extends Fragment implements OnClickListener {

    private String number;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, null);
        view.findViewById(R.id.pNumber).setOnClickListener(this);
        view.findViewById(R.id.pProtocol).setOnClickListener(this);
        view.findViewById(R.id.about).setOnClickListener(this);
        view.findViewById(R.id.tousuTel).setOnClickListener(this);
        SharedPreferences sp = getActivity().getSharedPreferences("NUMBER", Activity.MODE_PRIVATE);
        TextView tvNumber = (TextView) view.findViewById(R.id.fm_tv_number);
        number = sp.getString("id", "0000000000");
        tvNumber.setText(number);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), ProtocolActivity.class);
        switch (v.getId()) {
            case R.id.pNumber:
                intent.putExtra("type", 1);
                intent.putExtra("number", number);
                startActivity(intent);
                break;
            case R.id.pProtocol:
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.about:
                intent.putExtra("type", 3);
                startActivity(intent);
                break;
            case R.id.tousuTel:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("客服QQ：3156539030\n投诉受理时间：工作日09:00-18:00");
                builder.setTitle("投诉热线");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
            default:
                break;
        }
    }

}
