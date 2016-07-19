package kang.recyclerdb;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by kangjonghyuk on 2016. 7. 18..
 */
public class HolderView extends RecyclerView.ViewHolder {
    public TextView mName;
    public TextView mNaesun;
    public TextView mNumber;
    public TextView mEmail;
    public TextView mDepart;

    public HolderView(View v) {
        super(v);
        mName = (TextView) v.findViewById(R.id.tvName);
        mNaesun = (TextView) v.findViewById(R.id.tvNaesun);
        mNumber = (TextView) v.findViewById(R.id.tvNumber);
        mEmail = (TextView) v.findViewById(R.id.tvEmail);
        mDepart = (TextView) v.findViewById(R.id.tvDepart);
    }
}

