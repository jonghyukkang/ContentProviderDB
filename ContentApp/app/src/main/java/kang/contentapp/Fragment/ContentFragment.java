package kang.contentapp.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import kang.contentapp.ContentDB;
import kang.contentapp.MyContentProvider;
import kang.contentapp.Activity.PeopleEdit;
import kang.contentapp.R;

/**
 * Created by kangjonghyuk on 2016. 7. 13..
 */
public class ContentFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>
 {

    private SimpleCursorAdapter dataAdapter;
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_fragment, container, false);

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

        dataAdapter = new SimpleCursorAdapter(getContext(), R.layout.people_info, null, columns, to, 0);
        listView = (ListView) view.findViewById(R.id.peopleList);
        listView.setAdapter(dataAdapter);
        getLoaderManager().initLoader(0, null, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String peopleNumber =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_NUMBER));

                Toast.makeText(getContext(), peopleNumber, Toast.LENGTH_SHORT).show();

                String rowId =
                        cursor.getString(cursor.getColumnIndexOrThrow(ContentDB.KEY_PEOPLE));

                Intent peopleEdit = new Intent(getContext(), PeopleEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "update");
                bundle.putString("rowId", rowId);
                peopleEdit.putExtras(bundle);
                startActivity(peopleEdit);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
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
        CursorLoader cursorLoader = new CursorLoader(getContext(),
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
