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
import android.support.v7.view.menu.ExpandedMenuView;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;

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

        prepareListData();

        mMenuAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild, expandableList);

        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 0) {
                    switch (childPosition) {
                        case 0:
                            getLoaderManager().restartLoader(ALL_VIEW, null, ListContractFragment.this);
                            mDrawerLayout.closeDrawers();
                            break;

                        case 1:
                            getLoaderManager().restartLoader(LAB_1, null, ListContractFragment.this);
                            mDrawerLayout.closeDrawers();
                            break;

                        case 2:
                            getLoaderManager().restartLoader(LAB_2, null, ListContractFragment.this);
                            mDrawerLayout.closeDrawers();
                            break;

                        case 3:
                            getLoaderManager().restartLoader(LAB_3, null, ListContractFragment.this);
                            mDrawerLayout.closeDrawers();
                            break;

                        case 4:
                            getLoaderManager().restartLoader(DESIGN, null, ListContractFragment.this);
                            mDrawerLayout.closeDrawers();
                            break;

                        case 5:
                            getLoaderManager().restartLoader(MANAGE, null, ListContractFragment.this);
                            mDrawerLayout.closeDrawers();
                            break;
                    }
                }
                Log.d("TAG", "" + groupPosition + " / " + childPosition);
                return false;
            }
        });

        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Log.d("DEBUG", "heading clicked");
                return false;
            }
        });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();
        final int count = 1;

        Button btn_groupAdd = (Button) getActivity().findViewById(R.id.btn_groupAdd);
        btn_groupAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandedMenuModel item3 = new ExpandedMenuModel();
                item3.setIconName("heading3");
                listDataHeader.add(item3);
                mMenuAdapter.notifyDataSetChanged();

            }
        });

        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("Maneullab");
        listDataHeader.add(item1);

        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("heading2");
        listDataHeader.add(item2);

        List<String> heading1 = new ArrayList<>();
        heading1.add("전체 연락처");
        heading1.add("1연구소");
        heading1.add("2연구소");
        heading1.add("3연구소");
        heading1.add("디자인");
        heading1.add("경영지원");

        List<String> heading2 = new ArrayList<String>();
        heading2.add("Submenu of item 2");
        heading2.add("Submenu of item 2");
        heading2.add("Submenu of item 2");
        heading2.add("Add submenu 2");

        listDataChild.put(listDataHeader.get(0), heading1);
        listDataChild.put(listDataHeader.get(1), heading2);
    }

    private void setupDrawerContent(NavigationView navigationView) {
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
        data.moveToFirst();
        mAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.setCursor(null);
    }
}
