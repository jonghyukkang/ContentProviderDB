package kang.recyclerdb.Fragment;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */

import android.content.Intent;
import android.database.Cursor;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import kang.recyclerdb.Activity.InformationActivity;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.Adapter.ContractCursorAdapter;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.R;


public class ListContractFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

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
        mDbHelper = new DbHelper(getContext());

        // Floating Button을 눌렀을 때  'DialogFragment'띄움
        view.findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog_Fragment().show(getFragmentManager(), "dialog");
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // 연락처 클릭 시 개인정보 띄움
        mAdapter = new ContractCursorAdapter(new ContractCursorAdapter.OnClickItem() {
            @Override
            public void itemClickListener(Cursor cursor) {
                int idx_id = cursor.getColumnIndex(ContractColumns._ID);
                int idx_name = cursor.getColumnIndex(ContractColumns.NAME);
                int idx_naesun = cursor.getColumnIndex(ContractColumns.NAESUN);
                int idx_number = cursor.getColumnIndex(ContractColumns.NUMBER);
                int idx_email = cursor.getColumnIndex(ContractColumns.EMAIL);
                int idx_depart = cursor.getColumnIndex(ContractColumns.DEPART);

                String id = cursor.getString(idx_id);
                String name = cursor.getString(idx_name);
                String naesun = cursor.getString(idx_naesun);
                String number = cursor.getString(idx_number);
                String email = cursor.getString(idx_email);
                String depart = cursor.getString(idx_depart);

                Intent intent = new Intent(getContext(), InformationActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("NAME", name);
                intent.putExtra("NAESUN", naesun);
                intent.putExtra("NUMBER", number);
                intent.putExtra("EMAIL", email);
                intent.putExtra("DEPART", depart);

                startActivity(intent);
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
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.lab_1:
                        getLoaderManager().restartLoader(LAB_1, null, ListContractFragment.this);
                        break;

                    case R.id.lab_2:
                        getLoaderManager().restartLoader(LAB_2, null, ListContractFragment.this);
                        break;

                    case R.id.lab_3:
                        getLoaderManager().restartLoader(LAB_3, null, ListContractFragment.this);
                        break;

                    case R.id.lab_design:
                        getLoaderManager().restartLoader(DESIGN, null, ListContractFragment.this);
                        break;

                    case R.id.lab_manage:
                        getLoaderManager().restartLoader(MANAGE, null, ListContractFragment.this);
                        break;

                    case R.id.labAll:
                        getLoaderManager().restartLoader(ALL_VIEW, null, ListContractFragment.this);
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
            case 0 :
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
        data.moveToFirst();
        mAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setCursor(null);
    }
}

//
//    long id = cursor.getLong(cursor.getColumnIndex(ContractColumns.NAME));
//    Dialog_Fragment f = Dialog_Fragment.newInstance(id);
//f.show(getFragmentManager(), "dialog");