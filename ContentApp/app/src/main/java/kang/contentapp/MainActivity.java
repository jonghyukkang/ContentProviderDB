package kang.contentapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayListView();

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent peopleEdit = new Intent(getBaseContext(), PeopleEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "add");
                peopleEdit.putExtras(bundle);
                startActivity(peopleEdit);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, MainActivity.this);
    }

    private void displayListView() {
        String[] columns = new String[]{
                ContentDB.KEY_NAME,
                ContentDB.KEY_NAESUN,
                ContentDB.KEY_NUMBER,
                ContentDB.KEY_EMAIL,
                ContentDB.KEY_DEPART
        };

        int[] to = new int[]{
                R.id.name,
                R.id.naesun,
                R.id.number,
                R.id.email,
                R.id.depart
        };

        dataAdapter = new SimpleCursorAdapter(this, R.layout.people_info, null, columns, to, 0);

        final ListView listView = (ListView) findViewById(R.id.peopleList);
        listView.setAdapter(dataAdapter);
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String peopleNumber =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_NUMBER));

                Toast.makeText(getApplicationContext(), peopleNumber, Toast.LENGTH_SHORT).show();

                String rowId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_PEOPLE));

                Intent peopleEdit = new Intent(getBaseContext(), PeopleEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "update");
                bundle.putString("rowId", rowId);
                peopleEdit.putExtras(bundle);
                startActivity(peopleEdit);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ContentDB.KEY_PEOPLE,
                ContentDB.KEY_NAME,
                ContentDB.KEY_NAESUN,
                ContentDB.KEY_NUMBER,
                ContentDB.KEY_EMAIL,
                ContentDB.KEY_DEPART};
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapter.swapCursor(null);
    }

}
