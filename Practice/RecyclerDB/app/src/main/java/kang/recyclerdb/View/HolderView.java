package kang.recyclerdb.View;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 18..
 */
public class HolderView extends RecyclerView.ViewHolder {
    public TextView mCompany;
    public TextView mName;
    public TextView mDepart;

    public HolderView(View v) {
        super(v);
        mCompany = (TextView) v.findViewById(R.id.tvCompany);
        mName = (TextView) v.findViewById(R.id.tvName);
        mDepart = (TextView) v.findViewById(R.id.tvDepart);
    }
}

