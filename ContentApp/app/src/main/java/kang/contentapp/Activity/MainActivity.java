package kang.contentapp.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import kang.contentapp.Adapter.FragPagerAdapter;
import kang.contentapp.R;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new FragPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_add){
            Intent peopleEdit = new Intent(MainActivity.this, PeopleEdit.class);
            Bundle bundle = new Bundle();
            bundle.putString("mode", "add");
            peopleEdit.putExtras(bundle);
            startActivity(peopleEdit);
        }
        return super.onOptionsItemSelected(item);
    }
}
