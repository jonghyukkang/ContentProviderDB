package kang.recyclerdb.Fragment;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.Adapter.ContractCursorAdapter;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.R;


public class ListContractFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        NavigationView.OnNavigationItemSelectedListener {
    private static final int ALL_VIEW = 0;
    private static final int LAB_1 = 1;
    private static final int LAB_2 = 2;
    private static final int LAB_3 = 3;
    private static final int DESIGN = 4;
    private static final int MANAGE = 5;

    private RecyclerView mRecyclerView;
    private ContractCursorAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DbHelper mDbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);

        view.findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_Fragment().show(getFragmentManager(), "dialog");
            }
        });
        mDbHelper = new DbHelper(getContext());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        configureSwipe();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ContractCursorAdapter(new ContractCursorAdapter.OnClickItem() {
            @Override
            public void itemClickListener(Cursor cursor) {
                long id = cursor.getLong(cursor.getColumnIndex(ContractColumns._ID));
                Dialog_Fragment f = Dialog_Fragment.newInstance(id);
                f.show(getFragmentManager(), "dialog");
            }
        });

        mAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(ALL_VIEW, null, this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Lab_1
        if (id == R.id.lab_1) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Lab 1'" + ";", null);
            getLoaderManager().restartLoader(LAB_1, null, this);
            result.close();

            //Lab_2
        } else if (id == R.id.lab_2) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Lab 2'" + ";", null);
            getLoaderManager().restartLoader(LAB_2, null, this);
            result.close();

            //Lab_3
        } else if (id == R.id.lab_3) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Lab 3'" + ";", null);
            getLoaderManager().restartLoader(LAB_3, null, this);
            result.close();

            //Design
        } else if (id == R.id.lab_design) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Design'" + ";", null);
            getLoaderManager().restartLoader(DESIGN, null, this);
            result.close();

            //Manage
        } else if (id == R.id.lab_manage) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Manage'" + ";", null);
            getLoaderManager().restartLoader(MANAGE, null, this);
            result.close();

            //Lab_All
        } else if (id == R.id.labAll) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract;", null);
            getLoaderManager().restartLoader(ALL_VIEW, null, this);
            result.close();
        }

        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void configureSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int x = viewHolder.getLayoutPosition();
                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(x);
                final long id = cursor.getLong(cursor.getColumnIndex(ContractColumns._ID));
                getActivity().getContentResolver().delete(
                        Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(id)),
                        null, null);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder
        String Lab_1 = "depart = " + "'Lab 1'";
        String Lab_2 = "depart = " + "'Lab 2'";
        String Lab_3 = "depart = " + "'Lab 3'";
        String Design = "depart = " + "'Design'";
        String Manage = "depart = " + "'Manage'";

        switch (id) {
            case 0:
                // All_View
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, null, null, ContractColumns.NAME);
            case 1:
                // Lab_1
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, Lab_1, null, ContractColumns.NAME);
            case 2:
                // Lab_2
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, Lab_2, null, ContractColumns.NAME);
            case 3:
                // Lab_3
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, Lab_3, null, ContractColumns.NAME);
            case 4:
                // Design
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, Design, null, ContractColumns.NAME);
            case 5:
                // Manage
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, Manage, null, ContractColumns.NAME);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setCursor(null);
    }
}


//} else if (id == R.id.lab_manage) {
//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        Cursor result = db.rawQuery("select * from Contract where depart = " + "'Manage'" + ";", null);
//
//        result.moveToFirst();
//
//        while (!result.isAfterLast()) {
//        Log.d("TAG", "Manage");
//        String name = result.getString(1);
//        String naesun = result.getString(2);
//        String number = result.getString(3);
//        String email = result.getString(4);
//        String depart = result.getString(5);
//
//        result.moveToNext();
//        getLoaderManager().restartLoader(MANAGE, null, this);
//        Log.d("TAG", "" + name + " / " + naesun + " / " + number + " / " + email + " / " + depart);
//        }
//        result.close();