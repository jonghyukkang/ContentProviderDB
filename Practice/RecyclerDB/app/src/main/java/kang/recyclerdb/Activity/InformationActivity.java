package kang.recyclerdb.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.ETC.Cheeses;
import kang.recyclerdb.Fragment.Dialog_Fragment;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 21..
 */
public class InformationActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvNaesun;
    private TextView tvNumber;
    private TextView tvEmail;
    private TextView tvDepart;
    private TextView tvCompany;
    private String sName, sNaesun, sNumber, sEmail, sDepart, sId, sCompany;
    public static Context mContext;
    public android.support.design.widget.FloatingActionButton btnFab;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);
        mContext = this;
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvName = (TextView) findViewById(R.id.tvName);
        tvNaesun = (TextView) findViewById(R.id.tvNaesun);
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDepart = (TextView) findViewById(R.id.tvDepart);
        btnFab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.btnfab);

        Intent intent = getIntent();
        sId = intent.getStringExtra("ID");
        sCompany = intent.getStringExtra("COMPANY");
        sName = intent.getStringExtra("NAME");
        sNaesun = intent.getStringExtra("NAESUN");
        sNumber = intent.getStringExtra("NUMBER");
        sEmail = intent.getStringExtra("EMAIL");
        sDepart = intent.getStringExtra("DEPART");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(sName);

        tvCompany.setText(sCompany);
        tvName.setText(sName);
        tvNaesun.setText(sNaesun);
        tvNumber.setText(sNumber.substring(0, 3) + " - " + sNumber.substring(3, 7) + " - " + sNumber.substring(7, 11));
        tvEmail.setText(sEmail);
        tvDepart.setText(sDepart);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(InformationActivity.this);

        ImageView img_delete = new ImageView(InformationActivity.this);
        img_delete.setImageResource(R.drawable.icon_delete);

        ImageView img_edit = new ImageView(InformationActivity.this);
        img_edit.setImageResource(R.drawable.icon_edit);

        SubActionButton subButton_delete = itemBuilder.setContentView(img_delete).build();
        SubActionButton subButton_edit = itemBuilder.setContentView(img_edit).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(InformationActivity.this)
                .addSubActionView(subButton_delete)
                .addSubActionView(subButton_edit)
                .attachTo(btnFab)
                .build();

        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(
                        Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(sId)),
                        null, null);
                Toast.makeText(getApplicationContext(), "삭제 되었습니다", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Fragment f = Dialog_Fragment.newInstance(Integer.parseInt(sId));
                f.show(getSupportFragmentManager(), "dialog");
            }
        });


        loadBackdrop();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Glide.with(this).load(Cheeses.getRandomCheeseDrawable()).centerCrop().into(imageView);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCall:
                String call_number = "tel:" + sNumber;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(call_number));
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
                break;

            case R.id.btnEmail:
                String email_address = "mailto: " + sEmail;
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(email_address));
                startActivity(Intent.createChooser(emailIntent, "Send Email"));
                break;

        }
    }

    public void onUserSelectValue(String company, String name, String naesun, String number, String email, String depart) {
        tvCompany.setText(company);
        tvName.setText(name);
        tvNaesun.setText(naesun);
        tvNumber.setText(number.substring(0, 3) + " - " + number.substring(3, 7) + " - " + number.substring(7, 11));
        tvEmail.setText(email);
        tvDepart.setText(depart);
        collapsingToolbar.setTitle(name);
    }
}
