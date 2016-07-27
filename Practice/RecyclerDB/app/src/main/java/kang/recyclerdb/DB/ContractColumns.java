package kang.recyclerdb.DB;

import android.net.Uri;
import android.provider.BaseColumns;

public interface ContractColumns extends BaseColumns {

    String AUTHORITY = "kang.recyclerdb";
    Uri BASE_URI = Uri.parse("content://"+ AUTHORITY);
    Uri URI_MENSAGENS = Uri.withAppendedPath(BASE_URI, "msgs");

    // TableName = "Contract"
    String TABLE_NAME = "Maneullab";

    // "Contract" Table's fields
    String COMPANYNAME = "companyname";
    String NAME = "name";
    String NAESUN = "naesun";
    String NUMBER = "number";
    String EMAIL = "email";
    String DEPART = "depart";

}