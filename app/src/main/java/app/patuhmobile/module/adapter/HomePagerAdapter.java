package app.patuhmobile.module.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.patuhmobile.R;
import app.patuhmobile.module.Home.Cerita.CeritaFragment;
import app.patuhmobile.module.Home.Panutan.PanutanFragment;

/**
 * Created by Fahmi Hakim on 25/05/2018.
 * for SERA
 */

public class HomePagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 2;

    private Context mContext;

    public HomePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CeritaFragment();
            case 1:
                return new PanutanFragment();
            default:
                return new PanutanFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.title_cerita);
            case 1:
                return mContext.getString(R.string.title_panutan);
            default:
                return null;
        }
    }



}
