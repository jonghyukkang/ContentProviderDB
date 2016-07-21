package kang.recyclerdb.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableString;
import kang.recyclerdb.Fragment.ListContractFragment;
import kang.recyclerdb.Fragment.TabFragment2;

/**
 * Created by kangjonghyuk on 2016. 7. 20..
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private int tabCount;
    private String tabTitles[] = new String[] { "연락처", "대화" };

    public TabPagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 :
                ListContractFragment listContractFragment = new ListContractFragment();
                return listContractFragment;
            case 1 :
                TabFragment2 tabFragment2 = new TabFragment2();
                return tabFragment2;
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableString sb = new SpannableString(" " + tabTitles[position]);
        return sb;
    }
}
