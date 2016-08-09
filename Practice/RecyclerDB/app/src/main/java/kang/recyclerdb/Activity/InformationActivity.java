package kang.recyclerdb.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kang.recyclerdb.DB.ContractColumns;
import kang.recyclerdb.DB.DbHelper;
import kang.recyclerdb.Fragment.Dialog_Fragment;
import kang.recyclerdb.R;

/**
 * Created by kangjonghyuk on 2016. 7. 21..
 */
public class InformationActivity extends AppCompatActivity {
    private static final String Maneullab = "Maneullab";
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;
    private TextView tvName;
    private TextView tvNaesun;
    private TextView tvNumber;
    private TextView tvEmail;
    private TextView tvDepart;
    private TextView tvCompany;
    private ImageView imgView;
    private String sName, sNaesun, sNumber, sEmail, sDepart, sId, sCompany, sHotSearch, sProImage;
    public static Context mContext;
    public android.support.design.widget.FloatingActionButton btnFab;
    private CollapsingToolbarLayout collapsingToolbar;
    public DbHelper mDbHelper;

    private Uri mImageCaptureUri;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.information_layout);

        mContext = this;
        mDbHelper = new DbHelper(this);
        tvCompany = (TextView) findViewById(R.id.tvCompany);
        tvName = (TextView) findViewById(R.id.tvName);
        tvNaesun = (TextView) findViewById(R.id.tvNaesun);
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDepart = (TextView) findViewById(R.id.tvDepart);
        imgView = (ImageView) findViewById(R.id.backdrop);
        ImageView img_maneullabLogo = (ImageView) findViewById(R.id.img_maneullabLogo);
        btnFab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.btnfab);

        Intent intent = getIntent();
        sId = intent.getStringExtra("ID");
        sCompany = intent.getStringExtra("COMPANY");
        sName = intent.getStringExtra("NAME");
        sNaesun = intent.getStringExtra("NAESUN");
        sNumber = intent.getStringExtra("NUMBER");
        sEmail = intent.getStringExtra("EMAIL");
        sDepart = intent.getStringExtra("DEPART");
        sHotSearch = intent.getStringExtra("HOTSEARCH");

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(sName);

        if (sCompany.equals(Maneullab)) {
            img_maneullabLogo.setImageResource(R.drawable.maneullab_logo);
        } else {
            img_maneullabLogo.setImageResource(R.drawable.icon_guest);
            img_maneullabLogo.setPadding(0, 0, 40, 0);
        }

        tvCompany.setText(sCompany);
        tvName.setText(sName);
        tvNaesun.setText(sNaesun);
        if (sNumber != null) {
            if (sNumber.length() == 11)
                tvNumber.setText(sNumber.substring(0, 3) + " - " + sNumber.substring(3, 7) + " - " + sNumber.substring(7, 11));
            else if (sNumber.length() == 10) {
                tvNumber.setText(sNumber.substring(0, 3) + " - " + sNumber.substring(3, 6) + " - " + sNumber.substring(6, 10));
            }
        }
        tvEmail.setText(sEmail);
        tvDepart.setText(sDepart);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(InformationActivity.this);

        ImageView img_delete = new ImageView(InformationActivity.this);
        img_delete.setImageResource(R.drawable.icon_delete);

        final ImageView img_hotsearch = new ImageView(InformationActivity.this);
        if (sHotSearch.equals("yes")) {
            img_hotsearch.setImageResource(R.drawable.icon_star_full);
        } else if (sHotSearch.equals("no")) {
            img_hotsearch.setImageResource(R.drawable.icon_start_empty);
        }


        ImageView img_edit = new ImageView(InformationActivity.this);
        img_edit.setImageResource(R.drawable.icon_edit);

        ImageView img_editPicture = new ImageView(InformationActivity.this);
        img_editPicture.setImageResource(R.drawable.icon_galley);

        SubActionButton subButton_delete = itemBuilder.setContentView(img_delete).build();
        SubActionButton subButton_hotSearch = itemBuilder.setContentView(img_hotsearch).build();
        SubActionButton subButton_edit = itemBuilder.setContentView(img_edit).build();
        SubActionButton subButton_editPic = itemBuilder.setContentView(img_editPicture).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(InformationActivity.this)
                .addSubActionView(subButton_delete)
                .addSubActionView(subButton_hotSearch)
                .addSubActionView(subButton_edit)
                .addSubActionView(subButton_editPic)
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

        img_hotsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                String sql = "SELECT hotsearch FROM " + ContractColumns.TABLE_NAME + " where _ID = " + sId;
                Cursor c = db.rawQuery(sql, null);
                c.moveToFirst();
                String str = c.getString(0); // null or on

                if (str.equals("no")) {
                    img_hotsearch.setImageResource(R.drawable.icon_star_full);
                    Toast.makeText(InformationActivity.this, "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
                    String hotsearch_on = "yes";
                    ContentValues values = new ContentValues();
                    values.put(ContractColumns.HOTSEARCH, hotsearch_on);
                    Uri uri = Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(sId));
                    getContentResolver().update(uri, values, null, null);
                } else {
                    img_hotsearch.setImageResource(R.drawable.icon_start_empty);
                    Toast.makeText(InformationActivity.this, "즐겨찾기에서 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    ContentValues values = new ContentValues();
                    String hotsearch_off = "no";
                    values.put(ContractColumns.HOTSEARCH, hotsearch_off);
                    Uri uri = Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(sId));
                    getContentResolver().update(uri, values, null, null);
                }
                c.close();
                db.close();
            }
        });

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_Fragment f = Dialog_Fragment.newInstance(Integer.parseInt(sId));
                f.show(getSupportFragmentManager(), "dialog");
            }
        });

        img_editPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, PICK_FROM_ALBUM);
                mDialog = createDialog();
                mDialog.show();
            }
        });

        try {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            String[] columns = {ContractColumns.PRO_IMAGE};
            String[] params = {sId};
            Cursor cursor = db.query(ContractColumns.TABLE_NAME,
                    columns,
                    ContractColumns._ID + " = ?",
                    params, null, null, null);

            cursor.moveToFirst();
            String name = cursor.getString(0);
            Uri uri = Uri.parse(name);
            imgView.setImageURI(uri);
            cursor.close();
            db.close();
        } catch (NullPointerException e) {
            imgView.setImageAlpha(255);
        }
    }

    private AlertDialog createDialog() {
        final View innerView = getLayoutInflater().inflate(R.layout.dialog_image_crop, null);

        Button btn_camera = (Button) innerView.findViewById(R.id.btn_camera_crop);
        Button btn_gellary = (Button) innerView.findViewById(R.id.btn_gellary_crop);

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCameraAction();
                setDismiss(mDialog);
            }
        });

        btn_gellary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAlbumAction();
                setDismiss(mDialog);
            }
        });

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("연락처 사진");
        ab.setView(innerView);

        return ab.create();
    }

    private void setDismiss(AlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void doCameraAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mImageCaptureUri = createSaveCropFile();
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void doAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCall:
                String call_number = "tel:" + sNumber;
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(call_number));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                try {
                    Uri profile_uri = data.getData();
                    imgView.setImageURI(profile_uri);

                    SQLiteDatabase db = mDbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(ContractColumns.PRO_IMAGE, String.valueOf(profile_uri));
                    Uri uri = Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(sId));
                    getContentResolver().update(uri, values, null, null);
                    db.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case PICK_FROM_CAMERA:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("output", mImageCaptureUri);
                startActivityForResult(intent, CROP_FROM_CAMERA);
                break;

            case CROP_FROM_CAMERA :
                String full_path = mImageCaptureUri.getPath();
                Bitmap photo = BitmapFactory.decodeFile(full_path);
                imgView.setImageBitmap(photo);

                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(ContractColumns.PRO_IMAGE, String.valueOf(full_path));
                Uri uri = Uri.withAppendedPath(ContractColumns.URI_MENSAGENS, String.valueOf(sId));
                getContentResolver().update(uri, values, null, null);
                db.close();
                break;
        }
    }

    private Uri createSaveCropFile(){
        Uri uri;
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
        return uri;
    }
}
