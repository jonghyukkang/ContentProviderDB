package kang.recyclerdb.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 26..
 */
public class DialogFrag_GroupAdd extends DialogFragment implements DialogInterface.OnClickListener{

    private EditText mEditCompanyGroup;
    DbHelper mDbHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_frag_group_add, null);

        mEditCompanyGroup = (EditText)view.findViewById(R.id.editCompanyGroup);
        mDbHelper = new DbHelper(getContext());
        return new AlertDialog.Builder(getActivity())
                .setTitle("회사명 추가")
                .setView(view)
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String myCompany = mEditCompanyGroup.getText().toString();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL("CREATE TABLE "+ myCompany +" (" +
                "_ID INTEGER PRIMARY KEY, "+
                "companyname TEXT NOT NULL, "+
                "name TEXT NOT NULL, "+
                "naesun TEXT NOT NULL, "+
                "number TEXT NOT NULL, "+
                "email TEXT NOT NULL, "+
                "depart TEXT NOT NULL)");
        db.close();

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
