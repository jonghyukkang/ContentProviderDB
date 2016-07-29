package kang.recyclerdb.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.Fragment.ListContractFragment;
import kang.recyclerdb.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDbHelper = new DbHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            Toast.makeText(MainActivity.this, "Reset 기능 입니다", Toast.LENGTH_SHORT).show();
//            SQLiteDatabase db = mDbHelper.getWritableDatabase();
//            Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//            for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
//                String[] temp = new String[c.getColumnCount()];
//                for (int i = 0; i< temp.length; i++){
//                    temp[i] = c.getString(i);
//                    db.execSQL("DROP TABLE IF EXISTS "+ temp[i]);
//                }
//            }
//            c.close();
//            mDbHelper.onCreate(db);
//            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}




