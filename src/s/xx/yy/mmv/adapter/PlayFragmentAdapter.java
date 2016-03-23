package s.xx.yy.mmv.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PlayFragmentAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> list;

	public PlayFragmentAdapter(FragmentManager fm, ArrayList<Fragment> list) {
		super(fm);
		this.list = list;

	}

	public PlayFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
