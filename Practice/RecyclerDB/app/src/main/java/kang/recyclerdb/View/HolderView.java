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
    public TextView mName;
    public ImageView mImageView;

    public HolderView(View v) {
        super(v);
        mName = (TextView) v.findViewById(R.id.tvName);
        mImageView = (ImageView) v.findViewById(R.id.img_depart);

    }
}

