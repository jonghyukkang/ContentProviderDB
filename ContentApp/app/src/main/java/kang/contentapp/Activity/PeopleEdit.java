package kang.contentapp.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import kang.contentapp.ContentDB;
import kang.contentapp.MyContentProvider;
import kang.contentapp.R;

/**
 * Created by kangjonghyuk on 2016. 7. 13..
 */
public class PeopleEdit extends Activity implements View.OnClickListener{
    private Spinner departList;
    private Button save, delete;
    private String mode;
    private EditText name, naesun, number, email;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        if(this.getIntent().getExtras() != null){
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode"); // "add" or "update"
        }

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        name = (EditText) findViewById(R.id.name);
        naesun = (EditText) findViewById(R.id.naesun);
        number = (EditText) findViewById(R.id.number);
        email = (EditText) findViewById(R.id.email);
        departList = (Spinner) findViewById(R.id.departList);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.depart_array, android.R.layout.simple_spinner_dropdown_item);
        departList.setAdapter(adapter);

        // trim() = 빈 공간 없애주기
        // equalsIgnoreCase() = 대소문자 똑같이 받아들이기
        if(mode.trim().equalsIgnoreCase("add")){ //mode가 "add"이면
            delete.setEnabled(false);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadPeopleInfo();
        }
    }

    public void onClick(View v){
        String myName = name.getText().toString();
        String myNaesun = naesun.getText().toString();
        String myNumber = number.getText().toString();
        String myEmail = email.getText().toString();
        String myDepart = departList.getSelectedItem().toString();

        if(myName.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please Enter people name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(myNaesun.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please Enter people naesun", Toast.LENGTH_SHORT).show();
            return;
        }
        if(myNumber.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please Enter people number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(myEmail.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please Enter people email", Toast.LENGTH_SHORT).show();
            return;
        }

        switch(v.getId()){
            case R.id.save :
                ContentValues values = new ContentValues();

                values.put(ContentDB.KEY_NAME, myName);
                values.put(ContentDB.KEY_NAESUN, myNaesun);
                values.put(ContentDB.KEY_NUMBER, myNumber);
                values.put(ContentDB.KEY_EMAIL, myEmail);
                values.put(ContentDB.KEY_DEPART, myDepart);

                if(mode.trim().equalsIgnoreCase("add")){
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
                } else {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;

            case R.id.delete :
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                getContentResolver().delete(uri, null ,null);
                finish();
                break;
        }
    }

    private void loadPeopleInfo(){
        String[] projection = {
                ContentDB.KEY_PEOPLE,
                ContentDB.KEY_NAME,
                ContentDB.KEY_NAESUN,
                ContentDB.KEY_NUMBER,
                ContentDB.KEY_EMAIL,
                ContentDB.KEY_DEPART };

        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null){
            cursor.moveToFirst();
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_NAME));
            String myNaesun = cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_NAESUN));
            String myNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_NUMBER));
            String myEmail = cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_EMAIL));
            String myDepart = cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_DEPART));

            name.setText(myName);
            naesun.setText(myNaesun);
            number.setText(myNumber);
            email.setText(myEmail);
            departList.setSelection(getIndex(departList, myDepart));
        }
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
}
