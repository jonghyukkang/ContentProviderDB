package kang.recyclerdb.Fragment;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import kang.recyclerdb.Activity.GroupListActivity;
import kang.recyclerdb.Activity.InformationActivity;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.Adapter.ContractCursorAdapter;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.Adapter.ExpandableListAdapter;
import kang.recyclerdb.ETC.ExpandedMenuModel;
import kang.recyclerdb.R;


public class ListContractFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ALL_VIEW = 0;
    private static final int NAVI_GROUP_ADD_CODE = 1;
    private static final int FLOATING_BUTTON_CODE = 3;
    private static final String ALL = "전체";

    private RecyclerView mRecyclerView;
    private ContractCursorAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private DrawerLayout mDrawerLayout;

    DbHelper mDbHelper;
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader = new ArrayList<>();
    HashMap<ExpandedMenuModel, List<String>> listDataChild = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        mDbHelper = new DbHelper(getContext());

        setHasOptionsMenu(true);

        // Floating Button을 눌렀을 때  'DialogFragment'띄움
        view.findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new Dialog_Fragment();
                dialogFragment.setTargetFragment(ListContractFragment.this, FLOATING_BUTTON_CODE);
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
                Intent intent = new Intent(getContext(), GroupListActivity.class);
                startActivity(intent);
            }
        });

        Button btn_groupAdd = (Button) getActivity().findViewById(R.id.btn_groupAdd);
        btn_groupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DialogFrag_GroupAdd();
                dialogFragment.setTargetFragment(ListContractFragment.this, NAVI_GROUP_ADD_CODE);
                dialogFragment.show(getFragmentManager(), "dialog");
            }
        });

        prepareListData();
        mMenuAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild, expandableList);
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int gPosition = groupPosition;
                int cPosition = childPosition;

                String company = listDataHeader.get(gPosition).getIconName();
                List depart = listDataChild.get(listDataHeader.get(gPosition));

                Bundle bundle = new Bundle();
                bundle.putString("COMPANY", company);
                bundle.putString("DEPART", (String) depart.get(cPosition));

                getLoaderManager().restartLoader(cPosition, bundle, ListContractFragment.this);
                mDrawerLayout.closeDrawers();
                return false;
            }
        });

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                int gPosition = groupPosition;

                if (gPosition == 0) {
                    getLoaderManager().restartLoader(1000, null, ListContractFragment.this);
                    mDrawerLayout.closeDrawers();
                }
                return false;
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 그룹 추가시 Navigationview 리셋
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                listDataHeader.clear();
                prepareListData();
                mMenuAdapter.notifyDataSetChanged();
                getLoaderManager().restartLoader(ALL_VIEW, null, this);
            }
        }

        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                listDataHeader.clear();
                prepareListData();
                mMenuAdapter.notifyDataSetChanged();
            }
        }
    }

    public void prepareListData() {
        ExpandedMenuModel itemAll = new ExpandedMenuModel();
        itemAll.setIconName("전체 연락처");
        listDataHeader.add(itemAll);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        String sql = "SELECT DISTINCT companyname FROM " + ContractColumns.TABLE_NAME;
        Cursor results = db.rawQuery(sql, null);
        int i = 0;

        results.moveToFirst();
        while (!results.isAfterLast()) {
            String cName = results.getString(0);

            ExpandedMenuModel item = new ExpandedMenuModel();
            item.setIconName(cName);
            listDataHeader.add(item);

            ArrayList<String> result = new ArrayList<>();
            result.add(ALL);
            Cursor c1 = db.rawQuery("SELECT DISTINCT depart FROM " + ContractColumns.TABLE_NAME + " where companyname = " + "'" + cName + "'", null);
            c1.moveToFirst();
            int num = c1.getCount();
            for (int j = 0; j < num; j++) {
                int idx_depart = c1.getColumnIndex(ContractColumns.DEPART);
                String str = c1.getString(idx_depart);
                result.add(str);
                c1.moveToNext();
                if (c1.isAfterLast()) {
                    break;
                }
            }
            c1.close();
            listDataChild.put(listDataHeader.get(i + 1), result);
            i++;
            results.moveToNext();
        }
        results.close();
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
        if (args == null || id == 1000) {
            getActivity().setTitle("전체 연락처");
            return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, "length(name)>0", null, ContractColumns.NAME);
        }
        String company = args.getString("COMPANY");
        String depart = args.getString("DEPART");
        String name = args.getString("QUERY_NAME");
        if (args != null) {
            getActivity().setTitle(company + "   |   " + depart);
            if (id == 0)
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, "companyname = " + "'" + company + "'" +" AND length(name)>0", null, ContractColumns.NAME);
            if (id == 3471)
                return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, "name = " + "'" + name.trim() + "'", null, ContractColumns.NAME);

            return new CursorLoader(getActivity(), ContractColumns.URI_MENSAGENS, null, "companyname = " + "'" + company + "'" + " AND depart = " + "'" + depart + "'"+" AND length(name)>0", null, ContractColumns.NAME);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint(" 이름을 입력하세요 ");
        searchView.setOnQueryTextListener(queryTextListener);

        super.onCreateOptionsMenu(menu, inflater);

    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // 글자를 전부 입력한 후에 변경
            String query_name = query;

            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            Cursor mCount = db.rawQuery("SELECT * FROM "+ ContractColumns.TABLE_NAME +" WHERE name = " +"'"+query.trim()+"'", null);
            mCount.moveToFirst();
            int count = mCount.getCount();
            if(count <= 0){
                Toast.makeText(getContext(), "검색된 인원이 없습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putString("QUERY_NAME", query_name);
            getLoaderManager().restartLoader(3471, bundle, ListContractFragment.this);

            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // 글자를 입력할 때마다 변경
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}


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
////        SQLiteDatabase db = mDbHelper.getWritableDatabase();

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
//                        "SELECT depart FROM "+temp[i], null);
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