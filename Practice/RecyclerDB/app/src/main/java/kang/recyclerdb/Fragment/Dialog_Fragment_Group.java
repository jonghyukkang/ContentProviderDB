package kang.recyclerdb.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import kang.recyclerdb.Activity.InformationActivity;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 8. 1..
 */
public class Dialog_Fragment_Group extends DialogFragment implements DialogInterface.OnClickListener {

    private DbHelper mDbHelper;

    private EditText mEditDepart;
    private EditText mEditCompanygroup;
    private Spinner mCompanyList;
    ArrayList<String> companyList = new ArrayList<>();

    private CompleteListener listener;

    public interface CompleteListener {
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

        mCompanyList = (Spinner) view.findViewById(R.id.spinner_company);
        mEditDepart = (EditText) view.findViewById(R.id.editDepart);
        mEditCompanygroup = (EditText) view.findViewById(R.id.editCompanyGroup);
        mDbHelper = new DbHelper(getContext());

        Spinner_Redraw();

        return new AlertDialog.Builder(getActivity())
                .setTitle("그룹/부서 추가하기")
                .setView(view)
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    public void Spinner_Redraw(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        String sql = "SELECT DISTINCT companyname FROM "+ ContractColumns.TABLE_NAME;
        Cursor c = db.rawQuery(sql, null);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
                companyList.add(temp[i]);
            }
        }
        companyList.add("직접 입력");
        db.close();
        c.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, companyList);
        mCompanyList.setAdapter(adapter);
        mCompanyList.setOnItemSelectedListener(cSpinnerSelected);
    }

    AdapterView.OnItemSelectedListener cSpinnerSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
            final int inputPosition = mCompanyList.getCount();
            if (mCompanyList.getSelectedItemPosition() == (inputPosition-1)) {
                mEditCompanygroup.setEnabled(true);
            } else{
                mEditCompanygroup.setEnabled(false);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String myDepart = mEditDepart.getText().toString();
        String myCompany;

        if (mEditCompanygroup.isEnabled() == true) {
            myCompany = mEditCompanygroup.getText().toString();
        } else {
            myCompany = mCompanyList.getSelectedItem().toString();
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO " + ContractColumns.TABLE_NAME + "(companyname, depart) VALUES (" + "'" + myCompany + "', " + "'" + myDepart + "');");
        db.close();
        listener.completeListener("completeAddCompany");
    }
}

//        db.execSQL("CREATE TABLE "+ myCompany +" (" +
//                "_ID INTEGER PRIMARY KEY, "+
//                "companyname TEXT NOT NULL, "+
//                "name TEXT NOT NULL, "+
//                "naesun TEXT NOT NULL, "+
//                "number TEXT NOT NULL, "+
//                "email TEXT NOT NULL, "+
//                "depart TEXT NOT NULL)");
//        db.close();
//        db.execSQL("CREATE TABLE IF NOT EXISTS " + myCompany + " (" +
//                "_ID INTEGER PRIMARY KEY, " +
//                "companyname TEXT , " +
//                "depart TEXT )");
//        db.execSQL("INSERT INTO " + myCompany + " VALUES (null, " + "'" + myCompany + "', " + "'" + myDepart + "');");
