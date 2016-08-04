package kang.recyclerdb.Activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kang.recyclerdb.Adapter.ExpandListAdapter;
import kang.recyclerdb.ETC.TotalListener;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.ETC.ExpandedMenuModel;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 29..
 */
public class GroupDeleteActivity extends AppCompatActivity implements TotalListener {

    ExpandableListView mExpandList;
    ExpandListAdapter mExpandAdapter;
    List<ExpandedMenuModel> listDataHeader = new ArrayList<>();
    HashMap<ExpandedMenuModel, List<String>> listDataChild = new HashMap<>();
    public DbHelper mDbHelper;
    Button btn_groupDel;
    ArrayList<String> delete_list = new ArrayList<>();
    ArrayList<String> delete_group = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_edit_layout);

        btn_groupDel = (Button) findViewById(R.id.btn_groupDel2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("그룹 삭제");

        mDbHelper = new DbHelper(this);
        mExpandList = (ExpandableListView) findViewById(R.id.expandable_list);

        prepareListData();
        mExpandAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild, mExpandList);
        mExpandAdapter.setListener(this);
        mExpandList.setAdapter(mExpandAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_groupDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(delete_list.size() == 0){
//                    Toast.makeText(GroupDeleteActivity.this, "삭제 할 목록을 선택해 주세요.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                DialogSimple();
            }
        });
    }

    private void DialogSimple() {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("해당 그룹에 속한 연락처가 모두 삭제 됩니다. \n삭제하시겠습니까?").setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        try {
                            if (delete_group.size() != 0) {
                                for (int i = 0; i < delete_group.size(); i++) {
                                    db.execSQL("DROP TABLE IF EXISTS " + delete_group.get(i));
                                    String sql = "delete from " + ContractColumns.TABLE_NAME + " where companyname = " + "'" + delete_group.get(i) + "'";
                                    String sql1 = "DROP TABLE IF EXISTS "+delete_group.get(i);

                                    db.execSQL(sql);
                                    db.execSQL(sql1);
                                }
                            }
                            for (int i = 0; i < delete_list.size(); i++) {
                                String sql = "delete from " + ContractColumns.TABLE_NAME + " where companyname = " + "'" + mExpandAdapter.list_group.get(i) + "'" +
                                        " AND depart = " + "'" + delete_list.get(i) + "'";
                                db.execSQL(sql);
                            }
                        }
                            catch(IndexOutOfBoundsException e){
                                Log.d("DialogSimple", "IndexOutOfBoundsException");
                            }
                        db.close();

                        delete_list.clear();
                        delete_group.clear();
                        listDataHeader.clear();
                        listDataChild.clear();
                        prepareListData();
                        mExpandAdapter.notifyDataSetChanged();

                        mExpandAdapter.list_group.clear();
                        mExpandAdapter.sGroupList.clear();
                        mExpandAdapter.initCheckStates(false);
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("삭제");
        alert.show();
    }

    public void prepareListData() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql = "SELECT DISTINCT companyname FROM "+ ContractColumns.TABLE_NAME;
//        String sql = "SELECT name FROM sqlite_master WHERE type='table'";
        Cursor results = db.rawQuery(sql, null);
        int i = 0;

        results.moveToFirst();
        while (!results.isAfterLast()) {
            String cName = results.getString(0);

            ExpandedMenuModel item = new ExpandedMenuModel();
            item.setIconName(cName);
            listDataHeader.add(item);

            ArrayList<String> result = new ArrayList<>();
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
            listDataChild.put(listDataHeader.get(i), result);
            i++;
            results.moveToNext();
        }
        results.close();
    }

    @Override
    public void onTotalChanged(ArrayList<String> list, ArrayList<String> group) {
        this.delete_list = list;
        this.delete_group = group;
        Log.d("GroupDeleteActivity", "" + delete_list);
        Log.d("groupEditActivity", "" + delete_group);
    }

    @Override
    public void expandGroupEvent(int groupPosition, boolean isExpanded) {
        if (isExpanded)
            mExpandList.collapseGroup(groupPosition);
        else
            mExpandList.expandGroup(groupPosition);
    }
}
