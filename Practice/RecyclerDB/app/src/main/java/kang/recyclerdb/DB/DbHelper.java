package kang.recyclerdb.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper implements ContractColumns{
    private static final String DB_NAME = "ContractAddress";
    private static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ ContractColumns.TABLE_NAME+" ("+
                ContractColumns._ID +" INTEGER PRIMARY KEY, " +
                ContractColumns.COMPANYNAME +" TEXT, " +
                ContractColumns.NAME +" TEXT, " +
                ContractColumns.NAESUN +" TEXT, " +
                ContractColumns.NUMBER +" TEXT, " +
                ContractColumns.EMAIL +" TEXT, " +
                ContractColumns.DEPART +" TEXT)");

        //Manage
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '장준영', '200', '01063283471', 'iron28@maneullab.com','Manage');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '지강미', '301', '01096004729', 'kmji@maneullab.com','Manage');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '위성학', '300', '01032287525', 'shwee@maneullab.com','Manage');");
        //Design
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '이원식', '400', '01041385883', 'neolee@maneullab.com','Design');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '조선정', '401', '01052930025', 'jsjleo@maneullab.com','Design');");
        //Lab_1 ( Software )
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '이진수', '500', '01037340029', 'js@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '길근호', '503', '01089652307', 'ghkil15@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '이준수', '504', '01087661244', 'june@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '신영준', '505', '01044078105', 'yjshin@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '신광호', '506', '01091631429', 'hshin@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '김승연', '507', '01057977411', 'sosory@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '강종혁', '511', '01055033471', 'kangjh@maneullab.com','Lab 1');");
        //Lab_1 ( Hardware )
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '구권철', '502', '01080069744', 'gupiro@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '이미영', '508', '01036877747', 'speckle@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '양주열', '509', '01055299058', 'yangjy@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '한인수', '510', '01088734717', 'ishan81@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '전민철', '512', '01088325566', 'allure@maneullab.com','Lab 1');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '서현덕', '501', '01027787996', 'shd7996@maneullab.com','Lab 1');");
        //Lab_2
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '김 용', '600', '01034675412', 'kimyong@maneullab.com','Lab 2');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '김광오', '602', '01042051238', 'stone@maneullab.com','Lab 2');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '김태수', '603', '01047280306', 'wing@maneullab.com','Lab 2');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '신완섭', '604', '01040325956', 'dokebibat@maneullab.com','Lab 2');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '한현석', '605', '01047336841', 'hhs1120@maneullab.com','Lab 2');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '최시온', '601', '01037579310', 'sion@maneullab.com','Lab 2');");
        //Lab_3
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '정국면', '700', '01096561530',  'hitman@maneullab.com','Lab 3');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '정정원', '702', '01035005514', 'j21@maneullab.com','Lab 3');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '김현민', '701', '01091480784', 'skhm50@maneullab.com','Lab 3');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '김재민', '777', '01023330305', 'kjm@maneullab.com','Lab 3');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '이용현', '707', '0112830931', 'elrobo@maneullab.com','Lab 3');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, 'Maneullab', '김성학', '703', '01090536750', 'shkim@maneullab.com','Lab 3');");



        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '삼성', '정국면', '700', '01096561530',  'hitman@maneullab.com','해외법인');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '삼성', '정정원', '702', '01035005514', 'j21@maneullab.com','해외법인');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '삼성', '김현민', '701', '01091480784', 'skhm50@maneullab.com','소프트웨어');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '삼성', '김재민', '777', '01023330305', 'kjm@maneullab.com','소프트웨어');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '삼성', '이용현', '707', '0112830931', 'elrobo@maneullab.com','하드웨어');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '삼성', '김성학', '703', '01090536750', 'shkim@maneullab.com','하드웨어');");

        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '넥슨', '정국면', '700', '01096561530',  'hitman@maneullab.com','게임서버');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '넥슨', '정정원', '702', '01035005514', 'j21@maneullab.com','게임서버');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '넥슨', '김현민', '701', '01091480784', 'skhm50@maneullab.com','엔진관리');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '넥슨', '김재민', '777', '01023330305', 'kjm@maneullab.com','엔진관리');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '넥슨', '이용현', '707', '0112830931', 'elrobo@maneullab.com','경영');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '넥슨', '김성학', '703', '01090536750', 'shkim@maneullab.com','경영');");

        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '티맥스', '정국면', '700', '01096561530',  'hitman@maneullab.com','운영체제팀');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '티맥스', '정정원', '702', '01035005514', 'j21@maneullab.com','운영체제팀');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '티맥스', '김현민', '701', '01091480784', 'skhm50@maneullab.com','윈도우팀');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '티맥스', '김재민', '777', '01023330305', 'kjm@maneullab.com','윈도우팀');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '티맥스', '이용현', '707', '0112830931', 'elrobo@maneullab.com','하드웨어');");
        db.execSQL("INSERT INTO "+ ContractColumns.TABLE_NAME+" VALUES (null, '티맥스', '김성학', '703', '01090536750', 'shkim@maneullab.com','하드웨어');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ContractColumns.TABLE_NAME);
        onCreate(db);
    }
}