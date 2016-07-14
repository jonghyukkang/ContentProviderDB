package kang.contentapp.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.SpannableString;

import kang.contentapp.Fragment.ContentFragment;
import kang.contentapp.Fragment.FavoriteFragment;

/**
 * Created by kangjonghyuk on 2016. 7. 13..
 */
public class FragPagerAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Content", "Favorite" };
    private Context mContext;

    public FragPagerAdapter(FragmentManager fm, Context context){
        super(fm);
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch(position){
            case 0 :
                fragment = new ContentFragment();
                return fragment;
            case 1 :
                fragment = new FavoriteFragment();
                return fragment;
        }
        return null;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        SpannableString sb = new SpannableString("     " + tabTitles[position]);
        return sb;
    }
}
