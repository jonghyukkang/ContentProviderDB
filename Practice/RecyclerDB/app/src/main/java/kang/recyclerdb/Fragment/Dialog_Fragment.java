package kang.recyclerdb.Fragment;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kang.recyclerdb.Activity.InformationActivity;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.R;

public class Dialog_Fragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String EXTRA_ID = "id";
    private Spinner mCompanyList;
    private EditText mEditName;
    private EditText mEditNaesun;
    private EditText mEditNumber;
    private EditText mEditEmail;
    private EditText mEditDepart;
    private DbHelper mDbHelper;
    ArrayList<String> companyList = new ArrayList<String>();
    public long id;

    public static Dialog_Fragment newInstance(long id) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ID, id);
        Dialog_Fragment dialogFragment = new Dialog_Fragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_frag_member_add, null);

        mCompanyList = (Spinner) view.findViewById(R.id.spinner_company);
        mEditName = (EditText) view.findViewById(R.id.editName);
        mEditNaesun = (EditText) view.findViewById(R.id.editNaesun);
        mEditNumber = (EditText) view.findViewById(R.id.editNumber);
        mEditEmail = (EditText) view.findViewById(R.id.editEmail);
        mEditDepart = (EditText) view.findViewById(R.id.editDepart);
        mDbHelper = new DbHelper(getContext());

        Spinner_Redraw();

        // 연락처 편집 부분
        boolean mode = true;
        if (getArguments() != null && getArguments().getLong(EXTRA_ID) != 0) {
            id = getArguments().getLong(EXTRA_ID);
            Uri uri = Uri.withAppendedPath(
                    ContractColumns.URI_MENSAGENS, String.valueOf(id));

            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToNext()) {
                mode = false;
                String myCompany = cursor.getString(cursor.getColumnIndex(ContractColumns.COMPANYNAME));
                String myName = cursor.getString(cursor.getColumnIndex(ContractColumns.NAME));
                String myNaesun = cursor.getString(cursor.getColumnIndex(ContractColumns.NAESUN));
                String myNumber = cursor.getString(cursor.getColumnIndex(ContractColumns.NUMBER));
                String myEmail = cursor.getString(cursor.getColumnIndex(ContractColumns.EMAIL));
                String myDepart = cursor.getString(cursor.getColumnIndex(ContractColumns.DEPART));

                mCompanyList.setSelection(getIndex(mCompanyList, myCompany));
                mEditName.setText(myName);
                mEditNaesun.setText(myNaesun);
                mEditNumber.setText(myNumber);
                mEditEmail.setText(myEmail);
                mEditDepart.setText(myDepart);

            }
            cursor.close();
        }

        return new AlertDialog.Builder(getActivity())
                .setTitle(mode ? R.string.new_add : R.string.old_edit)
                .setView(view)
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void Spinner_Redraw(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        for (c.moveToPosition(1); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
                companyList.add(temp[i]);
            }
        }
        companyList.add("직접 입력");
        db.close();
        c.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, companyList);
        mCompanyList.setAdapter(adapter);
        mCompanyList.setOnItemSelectedListener(mItemSelected);
    }

    AdapterView.OnItemSelectedListener mItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
            final int inputPosition = mCompanyList.getCount();
            if (mCompanyList.getSelectedItemPosition() == (inputPosition-1)) {
                DialogFragment dialogFragment = new DialogFrag_GroupAdd();
                dialogFragment.setTargetFragment(Dialog_Fragment.this, 2);
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                companyList.clear();
                mCompanyList.invalidate();
                Spinner_Redraw();
            }
        }
    }

    // DialogFragment 등록버튼을 눌렀을 때
    @Override
    public void onClick(DialogInterface dialog, int which) {
        String myCompany = mCompanyList.getSelectedItem().toString();
        String myName = mEditName.getText().toString();
        String myNaesun = mEditNaesun.getText().toString();
        String myNumber = mEditNumber.getText().toString();
        String myEmail = mEditEmail.getText().toString();
        String myDepart = mEditDepart.getText().toString();

        if (myName.trim().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please Enter people name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (myNaesun.trim().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please Enter people naesun", Toast.LENGTH_SHORT).show();
            return;
        }
        if (myNumber.trim().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please Enter people number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (myEmail.trim().equalsIgnoreCase("")) {
            Toast.makeText(getContext(), "Please Enter people email", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ContractColumns.COMPANYNAME, myCompany);
        values.put(ContractColumns.NAME, myName);
        values.put(ContractColumns.NAESUN, myNaesun);
        values.put(ContractColumns.NUMBER, myNumber);
        values.put(ContractColumns.EMAIL, myEmail);
        values.put(ContractColumns.DEPART, myDepart);

        // 편집 모드 일땐 update()
        if (id != 0) {
            Uri uri = Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(id));
            getContext().getContentResolver().update(uri, values, null, null);

            InformationActivity informationActivity = (InformationActivity) getActivity();
            informationActivity.onUserSelectValue(myCompany, myName, myNaesun, myNumber, myEmail, myDepart);
        } else {
            // 첫 등록일땐 insert()
            getContext().getContentResolver().insert(ContractColumns.URI_MENSAGENS, values);
//            SQLiteDatabase db = mDbHelper.getWritableDatabase();
//         db.insert(myCompany, null, values);
        }
    }
}
//    Uri BASE_URI = Uri.parse("content://"+ AUTHORITY);
//    Uri URI_MENSAGENS = Uri.withAppendedPath(BASE_URI, "msgs");

//    Uri URI_TABLE = Uri.withAppendedPath(ContractColumns.BASE_URI, myCompany);
//    getContext().getContentResolver().insert(URI_TABLE, values);
