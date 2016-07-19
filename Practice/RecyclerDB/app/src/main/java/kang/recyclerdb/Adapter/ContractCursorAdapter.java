package kang.recyclerdb.Adapter;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.HolderView;
import kang.recyclerdb.R;

public class ContractCursorAdapter extends RecyclerView.Adapter<HolderView> {

    private Cursor mCursor;
    private OnClickItem mListener;

    public ContractCursorAdapter(OnClickItem listener) {
        mListener = listener;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.people_info, parent, false);

        final HolderView HolderView = new HolderView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = HolderView.getAdapterPosition();
                mCursor.moveToPosition(position);
                if (mListener != null) mListener.itemClickListener(mCursor);
            }
        });

        return HolderView;
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        mCursor.moveToPosition(position);
        int idx_name = mCursor.getColumnIndex(ContractColumns.NAME);
        int idx_naesun = mCursor.getColumnIndex(ContractColumns.NAESUN);
        int idx_number = mCursor.getColumnIndex(ContractColumns.NUMBER);
        int idx_email = mCursor.getColumnIndex(ContractColumns.EMAIL);
        int idx_depart = mCursor.getColumnIndex(ContractColumns.DEPART);

        String name = mCursor.getString(idx_name);
        String naesun = mCursor.getString(idx_naesun);
        String number = mCursor.getString(idx_number);
        String email = mCursor.getString(idx_email);
        String depart = mCursor.getString(idx_depart);

        holder.mName.setText(name);
        holder.mNaesun.setText(naesun);
        holder.mNumber.setText(number);
        holder.mEmail.setText(email);
        holder.mDepart.setText(depart);
    }


    @Override
    public int getItemCount() {
        return (mCursor != null) ? mCursor.getCount() : 0;
    }

    @Override
    public long getItemId(int position) {
        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                int idx_id = mCursor.getColumnIndex(ContractColumns._ID);
                return mCursor.getLong(idx_id);
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public Cursor getCursor(){
        return mCursor;
    }

    public void setCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface OnClickItem {
        void itemClickListener(Cursor cursor);
    }
}
