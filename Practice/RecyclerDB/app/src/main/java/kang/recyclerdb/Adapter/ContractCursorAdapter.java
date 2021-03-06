package kang.recyclerdb.Adapter;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import kang.recyclerdb.Activity.InformationActivity;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.View.HolderView;
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

        final ImageButton btn_call = (ImageButton) v.findViewById(R.id.btnCall);
        final ImageButton btn_email = (ImageButton) v.findViewById(R.id.btnEmail);

        final HolderView HolderView = new HolderView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = HolderView.getAdapterPosition();
                mCursor.moveToPosition(position);
                if (mListener != null) mListener.itemClickListener(mCursor);
            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = HolderView.getAdapterPosition();
                mCursor.moveToPosition(position);
                int idx_number = mCursor.getColumnIndex(ContractColumns.NUMBER);
                String call_number = "tel:" + mCursor.getString(idx_number);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(call_number));
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                v.getContext().startActivity(intent);
            }
        });

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = HolderView.getAdapterPosition();
                mCursor.moveToPosition(position);
                int idx_email = mCursor.getColumnIndex(ContractColumns.EMAIL);
                String email_address = "mailto: " + mCursor.getString(idx_email);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(email_address));
                v.getContext().startActivity(Intent.createChooser(emailIntent, "Send Email"));
            }
        });
        return HolderView;
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        mCursor.moveToPosition(position);
        int idx_company = mCursor.getColumnIndex(ContractColumns.COMPANYNAME);
        int idx_name = mCursor.getColumnIndex(ContractColumns.NAME);
        int idx_depart = mCursor.getColumnIndex(ContractColumns.DEPART);

        String company = mCursor.getString(idx_company);
        String name = mCursor.getString(idx_name);
        String depart = mCursor.getString(idx_depart);

        holder.mCompany.setText(company);
        holder.mName.setText(name);
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

    public Cursor getCursor() {
        return mCursor;
    }

    public void setCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface OnClickItem {
        void itemClickListener(Cursor cursor);
    }
}

