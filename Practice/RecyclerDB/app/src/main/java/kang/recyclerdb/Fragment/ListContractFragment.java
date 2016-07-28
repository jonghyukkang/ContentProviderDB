package kang.recyclerdb.Fragment;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.AutoScrollHelper;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kang.recyclerdb.Activity.InformationActivity;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.Adapter.ContractCursorAdapter;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.ExpandableListAdapter;
import kang.recyclerdb.ExpandedMenuModel;
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

    private DrawerLayout mDrawerLayout;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader = new ArrayList<ExpandedMenuModel>();
    HashMap<ExpandedMenuModel, List<String>> listDataChild = new HashMap<ExpandedMenuModel, List<String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        mDbHelper = new DbHelper(getContext());

        // Floating Button을 눌렀을 때  'DialogFragment'띄움
        view.findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new Dialog_Fragment();
                dialogFragment.setTargetFragment(ListContractFragment.this, 1);
                dialogFragment.show(getFragmentManager(), "dialog");
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
                int idx_company = cursor.getColumnIndex(ContractColumns.COMPANYNAME);
                int idx_name = cursor.getColumnIndex(ContractColumns.NAME);
                int idx_naesun = cursor.getColumnIndex(ContractColumns.NAESUN);
                int idx_number = cursor.getColumnIndex(ContractColumns.NUMBER);
                int idx_email = cursor.getColumnIndex(ContractColumns.EMAIL);
                int idx_depart = cursor.getColumnIndex(ContractColumns.DEPART);

                String id = cursor.getString(idx_id);
                String company = cursor.getString(idx_company);
                String name = cursor.getString(idx_name);
                String naesun = cursor.getString(idx_naesun);
                String number = cursor.getString(idx_number);
                String email = cursor.getString(idx_email);
                String depart = cursor.getString(idx_depart);

                Intent intent = new Intent(getContext(), InformationActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("COMPANY", company);
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

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        expandableList = (ExpandableListView) getActivity().findViewById(R.id.navigationmenu);

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        ImageButton img_btn_groupSetting = (ImageButton) header.findViewById(R.id.img_btn_groupSetting);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        img_btn_groupSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Group Settings", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_groupAdd = (Button) getActivity().findViewById(R.id.btn_groupAdd);
        btn_groupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DialogFrag_GroupAdd();
                dialogFragment.setTargetFragment(ListContractFragment.this, 1);
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        Button btn_groupDel = (Button) getActivity().findViewById(R.id.btn_groupDel);
        btn_groupDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DialogFrag_GroupDel();
                dialogFragment.setTargetFragment(ListContractFragment.this, 1);
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        prepareListData();

        mMenuAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild, expandableList);

        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                if (groupPosition == 1) {
//                    switch (childPosition) {
//                        case 0:
//                            getLoaderManager().restartLoader(LAB_1, null, ListContractFragment.this);
//                            mDrawerLayout.closeDrawers();
//                            break;
//                        case 1:
//                            getLoaderManager().restartLoader(LAB_2, null, ListContractFragment.this);
//                            mDrawerLayout.closeDrawers();
//                            break;
//                        case 2:
//                            getLoaderManager().restartLoader(LAB_3, null, ListContractFragment.this);
//                            mDrawerLayout.closeDrawers();
//                            break;
//                        case 3:
//                            getLoaderManager().restartLoader(DESIGN, null, ListContractFragment.this);
//                            mDrawerLayout.closeDrawers();
//                            break;
//                        case 4:
//                            getLoaderManager().restartLoader(MANAGE, null, ListContractFragment.this);
//                            mDrawerLayout.closeDrawers();
//                            break;
//                    }
//                }
                return false;
            }
        });

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int gPosition = groupPosition;

                String str = listDataHeader.get(gPosition).getIconName();
                Bundle bundle = new Bundle();
                bundle.putString("STR", str);

                getLoaderManager().restartLoader(gPosition, bundle, ListContractFragment.this);
                mDrawerLayout.closeDrawers();

                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                mMenuAdapter.notifyDataSetChanged();
                listDataHeader.clear();
                prepareListData();
            }
        }
    }

    public void prepareListData() {
        ExpandedMenuModel itemAll = new ExpandedMenuModel();
        itemAll.setIconName("전체 연락처");
        listDataHeader.add(itemAll);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        for (c.moveToPosition(1); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (int i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
                System.out.println("TABLE - " + temp[i]);
                ExpandedMenuModel item = new ExpandedMenuModel();
                item.setIconName(temp[i]);
                listDataHeader.add(item);
            }
        }
        db.close();

//        List<String> heading1 = new ArrayList<>();
//        heading1.add("1연구소");
//        heading1.add("2연구소");
//        heading1.add("3연구소");
//        heading1.add("디자인");
//        heading1.add("경영지원");
//
//        listDataChild.put(listDataHeader.get(1), heading1);
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder

        if (args == null || id == 0) {
            return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, null, null, ContractColumns.NAME);
        }
        String str = args.getString("STR");
        if (args != null) {
            return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, "companyname = " + "'"+str+"'", null, ContractColumns.NAME);
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


//        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        String sql = "select * from Contract;";
//        Cursor results = db.rawQuery(sql, null);
//
//        results.moveToFirst();
//        while(!results.isAfterLast()){
//            int id = results.getInt(0);
//            String voca = results.getString(1);
//            Log.d("TAG", ""+ id + " / " + voca);
//            results.moveToNext();
//        }
//        results.close();
//
//        Cursor c = db.rawQuery(
//                "SELECT name FROM sqlite_master WHERE type='table'", null);
//        ArrayList<String[]> result = new ArrayList<String[]>();
//        int i = 0;
//        result.add(c.getColumnNames());
//        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//            String[] temp = new String[c.getColumnCount()];
//            for (i = 0; i < temp.length; i++) {
//                temp[i] = c.getString(i);
//                System.out.println("TABLE - "+temp[i]);
//
//
//                Cursor c1 = db.rawQuery(
//                        "SELECT * FROM "+temp[i], null);
//                c1.moveToFirst();
//                String[] COLUMNS = c1.getColumnNames();
//                for(int j=0;j<COLUMNS.length;j++){
//                    c1.move(j);
//                    System.out.println("    COLUMN - "+COLUMNS[j]);
//                }
//            }
//            result.add(temp);
//            Log.d("TAG", ""+ result);
//        }