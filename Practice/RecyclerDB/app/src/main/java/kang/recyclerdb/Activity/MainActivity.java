package kang.recyclerdb.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import kang.recyclerdb.Adapter.ContractCursorAdapter;
import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.Fragment.ListContractFragment;
import kang.recyclerdb.HolderView;
import kang.recyclerdb.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDbHelper = new DbHelper(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //Lab_1
        if (id == R.id.lab_1) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Lab 1'" + ";", null);

            result.moveToFirst();

            while (!result.isAfterLast()) {
                Log.d("TAG", "lab_1");
                String name = result.getString(1);
                String naesun = result.getString(2);
                String number = result.getString(3);
                String email = result.getString(4);
                String depart = result.getString(5);

                ContractCursorAdapter adapter = new ContractCursorAdapter();

                //필요한 작업 처리
                result.moveToNext();
                Log.d("TAG", ""+name +" / " + naesun + " / " + number + " / " + email + " / " + depart);
            }
            result.close();

            //Lab_2
        } else if (id == R.id.lab_2) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Lab 2'" + ";", null);

            result.moveToFirst();

            while (!result.isAfterLast()) {
                Log.d("TAG", "lab_2");
                String name = result.getString(1);
                String naesun = result.getString(2);
                String number = result.getString(3);
                String email = result.getString(4);
                String depart = result.getString(5);

                result.moveToNext();
                Log.d("TAG", ""+name +" / " + naesun + " / " + number + " / " + email + " / " + depart);
            }
            result.close();

            //Lab_3
        } else if (id == R.id.lab_3) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Lab 3'" + ";", null);

            result.moveToFirst();

            while (!result.isAfterLast()) {
                Log.d("TAG", "lab_3");
                String name = result.getString(1);
                String naesun = result.getString(2);
                String number = result.getString(3);
                String email = result.getString(4);
                String depart = result.getString(5);

                result.moveToNext();
                Log.d("TAG", ""+name +" / " + naesun + " / " + number + " / " + email + " / " + depart);
            }
            result.close();

            //Design
        } else if (id == R.id.lab_design) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Design'" + ";", null);

            result.moveToFirst();

            while (!result.isAfterLast()) {
                Log.d("TAG", "Design");
                String name = result.getString(1);
                String naesun = result.getString(2);
                String number = result.getString(3);
                String email = result.getString(4);
                String depart = result.getString(5);

                result.moveToNext();
                Log.d("TAG", ""+name +" / " + naesun + " / " + number + " / " + email + " / " + depart);
            }
            result.close();

            //Manage
        } else if (id == R.id.lab_manage) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract where depart = " + "'Manage'" + ";", null);

            result.moveToFirst();

            while (!result.isAfterLast()) {
                Log.d("TAG", "Manage");
                String name = result.getString(1);
                String naesun = result.getString(2);
                String number = result.getString(3);
                String email = result.getString(4);
                String depart = result.getString(5);

                result.moveToNext();
                Log.d("TAG", ""+name +" / " + naesun + " / " + number + " / " + email + " / " + depart);
            }
            result.close();

            //Lab_All
        } else if (id == R.id.labAll) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor result = db.rawQuery("select * from Contract;", null);

            result.moveToFirst();

            while (!result.isAfterLast()) {
                Log.d("TAG", "lab_ALL");
                String name = result.getString(1);
                String naesun = result.getString(2);
                String number = result.getString(3);
                String email = result.getString(4);
                String depart = result.getString(5);

                result.moveToNext();
                Log.d("TAG", ""+name +" / " + naesun + " / " + number + " / " + email + " / " + depart);
            }
            result.close();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}



