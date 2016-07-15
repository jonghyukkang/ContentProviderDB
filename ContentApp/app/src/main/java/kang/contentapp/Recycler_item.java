package kang.contentapp;

/**
 * Created by kangjonghyuk on 2016. 7. 14..
 */
public class Recycler_item  {
    public String mName;
    public String mNaesun;
    public String mNumber;
    public String mEmail;
    public String mDepart;

    Recycler_item(String name, String naesun, String number, String email, String depart){
        this.mName = name;
        this.mNaesun = naesun;
        this.mNumber = number;
        this.mEmail = email;
        this.mDepart = depart;
    }

    String getName(){ return this.mName; }
    String getNaesun(){ return this.mNaesun; }
    String getNumber(){ return this.mNumber; }
    String getEmail(){ return this.mEmail; }
    String getDepart(){ return this.mDepart; }
}
