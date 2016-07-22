package kang.recyclerdb.ETC;

/**
 * Created by kangjonghyuk on 2016. 7. 19..
 */
public interface Const {
    String QUERY_LAB_1 = "select * from Contract where depart = " + "'Lab 1'" + ";";
    String QUERY_LAB_2 = "select * from Contract where depart = " + "'Lab 2'" + ";";
    String QUERY_LAB_3 = "select * from Contract where depart = " + "'Lab 3'" + ";";
    String QUERY_DESIGN = "select * from Contract where depart = " + "'Design'" + ";";
    String QUERY_MANAGE = "select * from Contract where depart = " + "'Manage'" + ";";
    String QUERY_ALL = "select * from Contract;";

}
