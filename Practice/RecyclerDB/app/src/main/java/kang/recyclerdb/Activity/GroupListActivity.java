package kang.recyclerdb.Activity;



import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kang.recyclerdb.Adapter.ExpandableListAdapter;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.ETC.ExpandedMenuModel;
import kang.recyclerdb.Fragment.Dialog_Fragment;
import kang.recyclerdb.Fragment.Dialog_Fragment_Group;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 8. 1..
 */
public class GroupListActivity extends AppCompatActivity implements Dialog_Fragment_Group.CompleteListener {

    ExpandableListView mExpandList;
    ExpandableListAdapter mExpandAdapter;
    List<ExpandedMenuModel> listDataHeader = new ArrayList<>();
    HashMap<ExpandedMenuModel, List<String>> listDataChild = new HashMap<>();
    public DbHelper mDbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("회사 편집");

        mDbHelper = new DbHelper(this);
        mExpandList = (ExpandableListView) findViewById(R.id.expandable_list);

        prepareListData();
        mExpandAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, mExpandList);
        mExpandList.setAdapter(mExpandAdapter);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_group_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
                Intent intent = new Intent(GroupListActivity.this, GroupDeleteActivity.class);
                startActivity(intent);
                break;

            case R.id.action_move:
                Dialog_Fragment_Group newFragment = new Dialog_Fragment_Group();
                newFragment.show(getSupportFragmentManager(),"hi");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void completeListener(String text) {
        if(text.equals("completeAddCompany")){
            listDataHeader.clear();
            listDataChild.clear();
            prepareListData();
            mExpandAdapter.notifyDataSetChanged();
        }
    }
}
