package kang.contentprovidertest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by kangjonghyuk on 2016. 7. 12..
 */
public class CountryEdit extends Activity implements View.OnClickListener {

    private Spinner continentList;
    private Button save, delete;
    private String mode;
    private EditText code, name;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        code = (EditText) findViewById(R.id.code);
        name = (EditText) findViewById(R.id.name);

        continentList = (Spinner) findViewById(R.id.continentList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.continent_array, android.R.layout.simple_spinner_dropdown_item);
        continentList.setAdapter(adapter);

        if (mode.trim().equalsIgnoreCase("add")) {
            delete.setEnabled(false);
        } else {
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadCountryInfo();
        }
    }

    public void onClick(View v) {
        String myContinent = continentList.getSelectedItem().toString();
        String myCode = code.getText().toString();
        String myName = name.getText().toString();

        if (myCode.trim().equalsIgnoreCase("")) {
            Toast.makeText(getBaseContext(), "Please Enter country code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (myName.trim().equalsIgnoreCase("")) {
            Toast.makeText(getBaseContext(), "Please Enter country name", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()) {
            case R.id.save:
                ContentValues values = new ContentValues();
                values.put(CountriesDB.KEY_CODE, myCode);
                values.put(CountriesDB.KEY_NAME, myName);
                values.put(CountriesDB.KEY_CONTINENT, myContinent);

                if (mode.trim().equalsIgnoreCase("add")) {
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
                } else {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;
            case R.id.delete:
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                getContentResolver().delete(uri, null, null);
                finish();
                break;
        }
    }

    private void loadCountryInfo() {

        String[] projection = {
                CountriesDB.KEY_WORID,
                CountriesDB.KEY_CODE,
                CountriesDB.KEY_NAME,
                CountriesDB.KEY_CONTINENT};
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myCode = cursor.getString(cursor.getColumnIndexOrThrow(CountriesDB.KEY_CODE));
            String myName = cursor.getString(cursor.getColumnIndexOrThrow(CountriesDB.KEY_NAME));
            String myContinent = cursor.getString(cursor.getColumnIndexOrThrow(CountriesDB.KEY_CONTINENT));
            code.setText(myCode);
            name.setText(myName);
            continentList.setSelection(getIndex(continentList, myContinent));
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
