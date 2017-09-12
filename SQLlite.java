package activitytest.example.com.book;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by haha on 2017-08-28.
 */

public class SQLlite extends SQLiteOpenHelper {


    public SQLlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLlite(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        //账号基本信息数据表  user
        /**
         * U_Num    学号，不允许重复
         * U_Nmae   姓名
         * U_Phone     手机号
         * U_Pwd    密码
         * U_Value  账户权限值  2，超级管理员，1，管理员， 0，普通用户
         */
        String sql = "create table user(U_Num varchar(11) primary key,U_Name varchar(20),U_Phone varchar(11),U_Pwd varchar(20),U_Value varchar(1))";
        db.execSQL(sql);
        String sqlite = "insert into user values('admin', 'admin',12345678910, 'admin', 2)";
        db.execSQL(sqlite);

        //图书信息数据表 book
        String sql1 = "create table book(ISBN varchar(20) primary key, B_Name varchar(50), B_Author varchar(50), B_Publishment varchar(80), B_Num varchar(5))";
        db.execSQL(sql1);

        String sqlitebook1 = "insert into book values(5555555555, '1', '郭霖', '人民邮电出版社', 5)";
        db.execSQL(sqlitebook1);


        //图书不同书号的基本信息  bookinfo-rmation
        /**
         * nmu  图书馆的书号
         * isbn     一类书的标记
         * borrowed     是否借阅    0，未借阅，1，借阅
         * orderd       是否预约    0， 未预约  1，以预约
         * introduction     图书简介
         */
        String sql2 = "create table bookinformation(B_Nmu varchar(20) primary key, ISBN varchar(20), Borrowed varchar(1), Ordered varchar(1), Introduction varchar(1000))";
        db.execSQL(sql2);
        String sqlite2 = "insert into bookinformation values(000000001, 5555555555, 0, 0, '第一行代码——Android》是Android初学者的最佳入门书。全书由浅入深、系统全面地讲解了Android软件开发的方方面面。你编写一个完整的项目，教会你如何打包、上架、嵌入广告并获得盈利。《第一行代码——Android》内容通俗易懂，既适合初学者循序渐进地阅读，也可作为一本参考手册，随时查阅。')";
        db.execSQL(sqlite2);

        //图书借阅信息表 record
        String sql3 = "create table record(B_Num varchar(50) primary key, S_Num varchar(20), BorrowTiem varchar(50), Retrun varchar(50))";
        db.execSQL(sql3);
        String sqlite3 = "insert into record(B_Num, S_Num) values(000000001, 'admin')";
        db.execSQL(sqlite3);

        //超期图书信息表overtime
        String sql4 = "create table OverTime(S_Num varchar(20), B_Num varchar(20), B_Name varchar(50), OverTime varchar(20), primary key(B_Num, S_Num))";
        db.execSQL(sql4);

        //预约图书信息表 Orderbook
        String sql5 = "create table Orderbook(B_Num varchar(20) primary key, S_Name varchar(50), S_Class varchar(50), B_Name varchar(50), S_Num varcahr(20), B_Atuthor varchar(50))";
        db.execSQL(sql5);

        //挂失图书信息表 losebook
        String sql6 = "create table losebook(GSBH Internet primary key, B_Num varchar(20), B_Name varchar(50), S_Num varchar(20))";
        db.execSQL(sql6);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void adduser(String num, String psw, String name, String phone) {
        ContentValues cv = new ContentValues();
        cv.put("U_Num", num);
        cv.put("U_Name", name);
        cv.put("U_Phone", phone);
        cv.put("U_Pwd", psw);
        cv.put("U_Value", 0);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("user", null, cv);
        db.close();
    }

    public void addadmin(String num, String psw, String name, String phone) {
        ContentValues cv = new ContentValues();
        cv.put("U_Num", num);
        cv.put("U_Name", name);
        cv.put("U_Phone", phone);
        cv.put("U_Pwd", psw);
        cv.put("U_Value", 1);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("user", null, cv);
        db.close();
    }

    public void addbook(String isbn, String name, String author, String publish, String number) {
        ContentValues vs = new ContentValues();
        vs.put("ISBN", isbn);
        vs.put("B_Name", name);
        vs.put("B_Author", author);
        vs.put("B_Publishment", publish);
        vs.put("B_Num", number);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("book", null, vs);
        db.close();
    }


    public void addbookmessage(String isbn, String name, String number) {
        ContentValues vs = new ContentValues();
        vs.put("B_Nmu", isbn);
        vs.put("ISBN", name);
        vs.put("Introduction", number);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("bookinformation", null, vs);
        db.close();
    }

    //修改书籍数量
    public void revisebook(String isbn, String number) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE book SET B_Num = " + number + " WHERE ISBN = " + isbn;
        db.execSQL(sql);
        db.close();
    }


}
