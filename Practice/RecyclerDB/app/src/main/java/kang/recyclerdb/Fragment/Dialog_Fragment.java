package kang.recyclerdb.Fragment;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.R;

public class Dialog_Fragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String EXTRA_ID = "id";
    private EditText mEditName;
    private EditText mEditNaesun;
    private EditText mEditNumber;
    private EditText mEditEmail;
    private Spinner mDepartList;
    public long id;

    public static Dialog_Fragment newInstance(long id){
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ID, id);

        Dialog_Fragment dialogFragment = new Dialog_Fragment();
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment, null);

        mEditName = (EditText)view.findViewById(R.id.editName);
        mEditNaesun = (EditText)view.findViewById(R.id.editNaesun);
        mEditNumber = (EditText)view.findViewById(R.id.editNumber);
        mEditEmail = (EditText)view.findViewById(R.id.editEmail);
        mDepartList = (Spinner) view.findViewById(R.id.departList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.depart_array, android.R.layout.simple_spinner_dropdown_item);
        mDepartList.setAdapter(adapter);

        boolean mode = true;
        if (getArguments() != null && getArguments().getLong(EXTRA_ID) != 0){
            id = getArguments().getLong(EXTRA_ID);
            Uri uri = Uri.withAppendedPath(
                    ContractColumns.URI_MENSAGENS, String.valueOf(id));

            Cursor cursor = getActivity().getContentResolver().query( uri, null, null, null, null);
            if (cursor.moveToNext()) {
                mode = false;
                String myName = cursor.getString(cursor.getColumnIndex(ContractColumns.NAME));
                String myNaesun = cursor.getString(cursor.getColumnIndex(ContractColumns.NAESUN));
                String myNumber = cursor.getString(cursor.getColumnIndex(ContractColumns.NUMBER));
                String myEmail = cursor.getString(cursor.getColumnIndex(ContractColumns.EMAIL));
                String myDepart = cursor.getString(cursor.getColumnIndex(ContractColumns.DEPART));

                mEditName.setText(myName);
                mEditNaesun.setText(myNaesun);
                mEditNumber.setText(myNumber);
                mEditEmail.setText(myEmail);
                mDepartList.setSelection(getIndex(mDepartList, myDepart));
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

    private int getIndex(Spinner spinner, String myString){
        int index = 0;

        for(int i=0; i<spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(myString)){
                index= i;
            }
        }
        return index;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        String myName = mEditName.getText().toString();
        String myNaesun = mEditNaesun.getText().toString();
        String myNumber = mEditNumber.getText().toString();
        String myEmail = mEditEmail.getText().toString();
        String myDepart = mDepartList.getSelectedItem().toString();

        if(myName.trim().equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Please Enter people name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(myNaesun.trim().equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Please Enter people naesun", Toast.LENGTH_SHORT).show();
            return;
        }
        if(myNumber.trim().equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Please Enter people number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(myEmail.trim().equalsIgnoreCase("")){
            Toast.makeText(getContext(), "Please Enter people email", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(ContractColumns.NAME, myName);
        values.put(ContractColumns.NAESUN, myNaesun);
        values.put(ContractColumns.NUMBER, myNumber);
        values.put(ContractColumns.EMAIL, myEmail);
        values.put(ContractColumns.DEPART, myDepart);

        if (id != 0){
            Uri uri = Uri.withAppendedPath(
                    ContractColumns.URI_MENSAGENS, String.valueOf(id));
            getContext().getContentResolver().update(uri, values, null, null);
        } else {
            getContext().getContentResolver().insert(
                    ContractColumns.URI_MENSAGENS, values);
        }
    }

}
