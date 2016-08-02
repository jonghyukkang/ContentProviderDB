package kang.recyclerdb.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 8. 1..
 */
public class Dialog_Fragment_Group extends DialogFragment implements DialogInterface.OnClickListener {

    private EditText mEditDepart;
    private EditText mEditCompanygroup;
    private DbHelper mDbHelper;

    private CompleteListener listener;

    public interface CompleteListener
    {
        void completeListener(String text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (CompleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "attaching d fragment failed!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_frag_edit_group_add, null);

        mEditDepart = (EditText) view.findViewById(R.id.editDepart);
        mEditCompanygroup = (EditText) view.findViewById(R.id.editCompanyGroup);
        mDbHelper = new DbHelper(getContext());

        return new AlertDialog.Builder(getActivity())
                .setTitle("그룹/부서 추가하기")
                .setView(view)
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String myDepart = mEditDepart.getText().toString();
        String myCompany = mEditCompanygroup.getText().toString();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS " + myCompany + " (" +
                "_ID INTEGER PRIMARY KEY, " +
                "companyname TEXT , " +
                "depart TEXT )");
        db.execSQL("INSERT INTO " + myCompany + " VALUES (null, " + "'" + myCompany + "', " + "'" + myDepart + "');");
        db.close();

        listener.completeListener("completeAddCompany");
    }
}
//        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+ "(companyname, depart) VALUES (" + "'"+myCompany+"', "+ "'"+myDepart+"');");
