package kang.recyclerdb.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 26..
 */
public class DialogFrag_GroupDel extends DialogFragment implements DialogInterface.OnClickListener{

    private EditText mEditCompanyGroup;
    DbHelper mDbHelper;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_frag_group_del, null);

        mEditCompanyGroup = (EditText)view.findViewById(R.id.editCompanyGroup);
        mDbHelper = new DbHelper(getContext());
        return new AlertDialog.Builder(getActivity())
                .setTitle("회사 삭제")
                .setView(view)
                .setPositiveButton(R.string.delete, this)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String myCompany = mEditCompanyGroup.getText().toString();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+myCompany);
        String sql = "delete from "+ContractColumns.TABLE_NAME+" where companyname = "+"'"+myCompany+"'";
        db.execSQL(sql);
        db.close();

        Intent intent = new Intent();
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }
}
