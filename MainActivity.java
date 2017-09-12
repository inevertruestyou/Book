package activitytest.example.com.book;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class MainActivity extends Activity {

    private RecyclerView rv_sheet;
    private List<entity> list;
    private sheetAdapter sheetAdapter;

    int listnum;
    Boolean haveisbn = true;
    Boolean havebook = true;
    Boolean havelogin = true;
    int lenght = 0;       //用来计数查询数据个数
    String str0 = "0";
    String str1 = "1";
    String str2 = "2";
    int identity = 0;      //用户身份  0未普通用户， 1为管理员  2为超级管理员
    int checkway = 1;       //1为简单查询模式， 2为复杂查询模式
    List<Book> v1 = new ArrayList<>();
    private String sname;//保存登录时的用户名
    private String booknum;     //书籍在架数量
    private SQLlite dblite;

    String stringname;    //登陆时的用户名
    public static int screenH, screenW;


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    gotoIpView();  //登录界面
                    break;
                case 1:
                    gotoQueryMainView();    //查询界面
                    break;
                case 2:
                    gotoloseView(); //切换到挂失界面
                    break;
                case 3:
                    gotoyuyueView();    //切换到预约界面
                    break;
                case 4:
                    goToHelpView();   //帮助界面
                    break;
                case 5:
                    goToAboutView();    //关于界面
                    break;
                case 6:
                    goTobookset();
                    break;
                case 7:
                    gotouserset();
                    break;
                case 8:
                    gotoadminset();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels;     // 屏幕宽度（像素）
        screenH = metric.heightPixels;   // 屏幕高度（像素）
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        dblite = DbManger.getIntance(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        goToWelcomeView();
    }

    private void goToWelcomeView() {
        MySurfaceView mView = new MySurfaceView(this);
        setContentView(mView);
    }

    private void goToHelpView() {
        setContentView(R.layout.helpview);
        Button btn_back = this.findViewById(R.id.ImageButtonquery_qrid01);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainMenu();
            }
        });
    }

    private void goToMainMenu() {
        setContentView(new MainMenu(this));
    }

    private void gotoQueryMainView() {
        //============================================查询界面
        setContentView(R.layout.my_select);
        Button btn_backselect = this.findViewById(R.id.back_select);
        btn_backselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainMenu();
            }
        });
        //=============================所有图书查询
        Button btn_bookselect = this.findViewById(R.id.bookselet);
        btn_bookselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoQueryView();
            }
        });

        //==============================个人借阅图书查询

        Button btn_mymessagecelect = this.findViewById(R.id.messagelect);
        btn_mymessagecelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                gotoSelfView();
            }
        });
    }

    private void gotoloseView() {        //挂失界面
        setContentView(R.layout.lose);
        Button btn_back = this.findViewById(R.id.ImageButtonlose);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainMenu();
            }
        });
    }

    private void gotoyuyueView() {       //预约界面

    }

    private void goToAboutView() {       //关于界面
        setContentView(R.layout.about);
        Button btn_back = this.findViewById(R.id.back_about);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainMenu();
            }
        });
    }

    private void goTopassword() {        //忘记密码

    }

    //-----------------------------------------------------------------书籍查询页面----------------------------------------------------------------------
    public void gotoQueryView() {
        setContentView(R.layout.query);
        final Spinner sp =  findViewById(R.id.Spinner01);
        List<CItem> lst = new ArrayList<>();
        CItem ct = new CItem("1", "书名");
        CItem ct1 = new CItem("2", "作者");
        CItem ct2 = new CItem("3", "出版社");
        lst.add(ct);
        lst.add(ct1);
        lst.add(ct2);
        ArrayAdapter<CItem> Adapter = new ArrayAdapter<CItem>(MainActivity.this,
                android.R.layout.simple_spinner_item, lst);
        sp.setAdapter(Adapter);
        Button sbmit = (Button) findViewById(R.id.querybok);
        final RadioButton simpleq = (RadioButton) findViewById(R.id.simpleQuery);
        final RadioButton highq = (RadioButton) findViewById(R.id.highQuery);
        final EditText simpleEdit = (EditText) findViewById(R.id.simpleQueryEdit);
        final EditText highEditSM = (EditText) findViewById(R.id.highEditSM);
        final EditText highEditZZ = (EditText) findViewById(R.id.highEditZZ);
        final EditText highEditCBS = (EditText) findViewById(R.id.highEditCBS);

        final LinearLayout simple = (LinearLayout) findViewById(R.id.linearsimple);
        final LinearLayout high =  findViewById(R.id.linearhigh);

        //imagebutton后退按钮的监听
        Button ibquery = this.findViewById(R.id.ImageButtonquery);
        ibquery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeKeyboard();
                        gotoQueryMainView();
                    }
                }
        );

        //=============================================两个简单和高级单选按钮的单击的监听的方法===========================================================

        simpleq.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        simple.setVisibility(View.VISIBLE);
                        high.setVisibility(View.GONE);
                    }
                }
        );
        //高级单选按钮的单击监听方法
        highq.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        simple.setVisibility(View.GONE);
                        high.setVisibility(View.VISIBLE);
                    }
                }
        );
        sbmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Vector<String> vvvv =new Vector<String>();
                        //简单查询的事件监听
                        if (simpleq.isChecked()) {
                            String result = simpleEdit.getText().toString().trim();
                            String id = ((CItem) sp.getSelectedItem()).GetID().toString().trim();
                            if (result.equals("")) {
                                Toast.makeText
                                        (
                                                MainActivity.this,
                                                "输入不能为空，请输入要查询的内容!",
                                                Toast.LENGTH_SHORT
                                        ).show();
                            } else {
                                if (id.equals("1"))          //通过输入书名进行查询
                                {


                                    //--------------------------------通过输入书名进行查询--------------------------------------------------------------------------
                                    SQLiteDatabase db = dblite.getWritableDatabase();
                                    Cursor cursor = db.query("book", new String[]{"ISBN", "B_Name", "B_Author", "B_Publishment", "B_Num"}, null, null, null, null, null);
                                    while (cursor.moveToNext()) {      //判断用户名是否存在
                                        String name = cursor.getString(cursor.getColumnIndex("B_Name")).trim();
                                        if (result.equals(name)) {
                                            havebook = true;
                                            break;
                                        } else {
                                            havebook = false;
                                        }
                                        db.close();
                                    }
                                    if (havebook) {
                                        SQLiteDatabase db1 = dblite.getWritableDatabase();
                                        Cursor cursor1 = db1.query("book", new String[]{"ISBN", "B_Name", "B_Author", "B_Publishment", "B_Num"}, null, null, null, null, null);
                                        while (cursor1.moveToNext()) {
                                            Book book = new Book();
                                            lenght++;
                                            book.setIsbn(cursor.getString(cursor.getColumnIndex("ISBN")).trim());
                                            book.setName(cursor.getString(cursor.getColumnIndex("B_Name")).trim());
                                            book.setAuthor(cursor.getString(cursor.getColumnIndex("B_Author")).trim());
                                            book.setPublish(cursor.getString(cursor.getColumnIndex("B_Publishment")).trim());
                                            book.setBooknum(cursor.getString(cursor.getColumnIndex("B_Num")).trim());
                                            v1.add(book);
                                        }
                                        closeKeyboard();
                                        gotoSelect();
                                        db1.close();
                                    } else {
                                        Toast.makeText(MainActivity.this, "没有这本书", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                //=======================================通过作者进行查询==============================================================

                                else if (id.equals("2")) {
                                    SQLiteDatabase db = dblite.getWritableDatabase();
                                    Cursor cursor = db.query("book", new String[]{"ISBN", "B_Name", "B_Author", "B_Publishment", "B_Num"}, null, null, null, null, null);
                                    while (cursor.moveToNext()) {      //判断用户名是否存在
                                        String name = cursor.getString(cursor.getColumnIndex("B_Name")).trim();
                                        if (result.equals(name)) {
                                            havebook = true;
                                            break;
                                        } else {
                                            havebook = false;
                                        }
                                        db.close();
                                    }
                                    SQLiteDatabase db1 = dblite.getWritableDatabase();
                                    if (havebook) {
                                        Cursor cursor1 = db1.query("book", new String[]{"ISBN", "B_Name", "B_Author", "B_Publishment", "B_Num"}, null, null, null, null, null);
                                        while (cursor1.moveToNext()) {
                                            Book book = new Book();
                                            lenght++;
                                            book.setIsbn(cursor.getString(cursor.getColumnIndex("ISBN")).trim());
                                            book.setName(cursor.getString(cursor.getColumnIndex("B_Name")).trim());
                                            book.setAuthor(cursor.getString(cursor.getColumnIndex("B_Author")).trim());
                                            book.setPublish(cursor.getString(cursor.getColumnIndex("B_Publishment")).trim());
                                            book.setBooknum(cursor.getString(cursor.getColumnIndex("B_Num")).trim());
                                            v1.add(book);

                                        }
                                        Log.i(TAG, "onClick: ===============" + v1.get(0));
                                        Log.i(TAG, "onClick: =============" + v1.get(1));
                                        closeKeyboard();
                                        gotoSelect();
                                        db1.close();
                                    } else {
                                        Toast.makeText(MainActivity.this, "没有这本书", Toast.LENGTH_SHORT).show();
                                    }
                                }

//============================================通过出版社进行查询===========================================================
                                else if (id.equals("3")) {
                                    SQLiteDatabase db = dblite.getWritableDatabase();
                                    Cursor cursor = db.query("book", new String[]{"ISBN", "B_Name", "B_Author", "B_Publishment", "B_Num"}, null, null, null, null, null);
                                    while (cursor.moveToNext()) {      //判断用户名是否存在
                                        Log.i(TAG, "onClick:判断出版社是否存在 ");
                                        String name = cursor.getString(cursor.getColumnIndex("B_Publishment")).trim();
                                        if (result.equals(name)) {
                                            Book book = new Book();
                                            lenght++;
                                            book.setIsbn(cursor.getString(cursor.getColumnIndex("ISBN")).trim());
                                            book.setName(cursor.getString(cursor.getColumnIndex("B_Name")).trim());
                                            book.setAuthor(cursor.getString(cursor.getColumnIndex("B_Author")).trim());
                                            book.setPublish(cursor.getString(cursor.getColumnIndex("B_Publishment")).trim());
                                            book.setBooknum(cursor.getString(cursor.getColumnIndex("B_Num")).trim());
                                            v1.add(book);
                                            db.close();
                                            closeKeyboard();
                                            gotoSelect();
                                        } else {
                                            db.close();
                                            Toast.makeText(MainActivity.this, "没有这个出版社的书籍", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        } else if (highq.isChecked()) {
                            String highSM = highEditSM.getText().toString().trim();
                            String highZZ = highEditZZ.getText().toString().trim();
                            String highCBS = highEditCBS.getText().toString().trim();
                            if (highSM.equals("") && highZZ.equals("") && highCBS.equals("")) {
                                Toast.makeText
                                        (
                                                MainActivity.this,
                                                "输入不能全为空，请输入要查询的内容!",
                                                Toast.LENGTH_SHORT
                                        ).show();
                            }

//==========================================书名和作者的组合查询=====================================================
                            else if ((!highSM.equals("")) && (!highZZ.equals("")) && (highCBS.equals(""))) {
                                //                     queryTOgird=DBUtil.getBnAuAllfrombook(highSM, highZZ);
                                //                  toast(queryTOgird,resultnumdetails);
                            }
//==========================================书名和出版社的组合查询==========================================
                            else if ((!highSM.equals("")) && (highZZ.equals("")) && (!highCBS.equals(""))) {
                                //                    queryTOgird=DBUtil.getBnCbAllfrombook(highSM, highCBS);
                                //                   toast(queryTOgird,resultnumdetails);
                            }
//==========================================作者和出版社的组合查询==========================================
                            else if ((highSM.equals("")) && (!highZZ.equals("")) && (!highCBS.equals(""))) {
                                //                    queryTOgird=DBUtil.getAuCbAllfrombook(highZZ, highCBS);
                                //                    toast(queryTOgird,resultnumdetails);
                            }
//==========================================书名作者出版社三者的组合查询==========================================
                            else if ((!highSM.equals("")) && (!highZZ.equals("")) && (!highCBS.equals(""))) {
                                //                     queryTOgird=DBUtil.getBnAuCbAllfrombook(highSM, highZZ, highCBS);
                                //                     toast(queryTOgird,resultnumdetails);
                            } else {
                                Toast.makeText
                                        (
                                                MainActivity.this,
                                                "输入最多一个为空，请输入要查询的内容!",
                                                Toast.LENGTH_SHORT
                                        ).show();
                            }
                        }
                    }

                }
        );
    }

    public void exit() {
        System.exit(0);
    }

    //==========================================添加管理员==============================================================
    public void gotoadminset() {
        setContentView(R.layout.adduser);
        Button btn_backadd = this.findViewById(R.id.ImageButtonquery_qrid01);
        btn_backadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                goToMainMenu();

            }
        });
        final EditText addusernum1 = this.findViewById(R.id.addusernum);
        final EditText addusername1 = this.findViewById(R.id.addusername);
        final EditText adduserphone1 = this.findViewById(R.id.adduserphone);
        final EditText adduserpwd1 = this.findViewById(R.id.adduserpwd);
        Button btn_ok = this.findViewById(R.id.ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernum = addusernum1.getText().toString().trim();
                String username = addusername1.getText().toString().trim();
                String userphone = adduserphone1.getText().toString().trim();
                String userpwd = adduserpwd1.getText().toString().trim();
                if (!("").equals(usernum)) {
                    if (!("").equals(userpwd)) {
                        if (!("").equals(username)) {
                            if (isMobileNum(userphone)) {
                                dblite.addadmin(usernum, userpwd, username, userphone);
                                Toast.makeText(MainActivity.this, "添加管理员成功", Toast.LENGTH_SHORT).show();
                                addusernum1.getText().clear();
                                addusername1.getText().clear();
                                adduserphone1.getText().clear();
                                adduserpwd1.getText().clear();
                            } else {
                                Toast.makeText(MainActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "姓名不正确", Toast.LENGTH_SHORT).show();
                            adduserphone1.getText().clear();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addusernum1.getText().clear();
        addusername1.getText().clear();
        adduserphone1.getText().clear();
        adduserpwd1.getText().clear();
    }

    //=================================用来验证手机号是否正确==========================================================

    private boolean isMobileNum(String s) {
        String regex = "^1[3|4|5|7|8][0-9]{9}$";
        return s.matches(regex);
    }

    //========================================添加书籍===============================================================

    private void goTobookset() {
        setContentView(R.layout.bookset);
        Button btn_backbookadd = this.findViewById(R.id.back_addbook);
        btn_backbookadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                goToMainMenu();
            }
        });
        final EditText addbookisbn = this.findViewById(R.id.addbookisbn);
        final EditText addbookname = this.findViewById(R.id.addbookname);
        final EditText addbookauthor = this.findViewById(R.id.addbookauthor);
        final EditText addbookpublish = this.findViewById(R.id.addbookpublish);
        final EditText addbooknum = this.findViewById(R.id.addbooknum);
        final EditText addbookmessage = this.findViewById(R.id.addbookmessag);
        final EditText addbookn = this.findViewById(R.id.addbookn);

        Button btn_ok = this.findViewById(R.id.booksetok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bookisbn = addbookisbn.getText().toString().trim();
                String bookname = addbookname.getText().toString().trim();
                String bookauthor = addbookauthor.getText().toString().trim();
                String bookpublish = addbookpublish.getText().toString().trim();
                String booknum = addbooknum.getText().toString().trim();
                String bookmessage = addbookmessage.getText().toString().trim();
                String bookn = addbookn.getText().toString().trim();
                if (!("").equals(bookisbn)) {
                    if (!("").equals(bookname)) {
                        if (!("").equals(bookauthor)) {
                            if (!("").equals(bookpublish)) {
                                String num = null, isbn;
                                SQLiteDatabase db = dblite.getWritableDatabase();
                                Cursor cursor = db.query("book", new String[]{"ISBN", "B_Name", "B_Author", "B_Publishment", "B_Num"}, null, null, null, null, null);
                                while (cursor.moveToNext()) {      //判断isbn书号是否存在
                                    isbn = cursor.getString(cursor.getColumnIndex("ISBN")).trim();
                                    num = cursor.getString(cursor.getColumnIndex("B_Num")).trim();
                                    if (bookisbn.equals(isbn)) {
                                        haveisbn = false;
                                        break;
                                    } else {
                                        haveisbn = true;
                                    }
                                }
                                if (haveisbn) {
                                    dblite.addbook(bookisbn, bookname, bookauthor, bookpublish, booknum);
                                    dblite.addbookmessage(bookn, bookisbn, bookmessage);
                                    Toast.makeText(MainActivity.this, "添加书籍成功", Toast.LENGTH_SHORT).show();
                                    addbookisbn.getText().clear();
                                    addbookname.getText().clear();
                                    addbookauthor.getText().clear();
                                    addbookpublish.getText().clear();
                                    addbooknum.getText().clear();
                                    addbookmessage.getText().clear();
                                    addbookn.getText().clear();
                                } else {
                                    Toast.makeText(MainActivity.this, "书籍已存在，数量增加" + booknum, Toast.LENGTH_SHORT).show();
                                    //数量转化为int类型进行相加
                                    int booka = Integer.parseInt(num);
                                    int bookb = Integer.parseInt(booknum);
                                    int booknum_new = booka + bookb;
                                    //int类型转化为stir你提供类型
                                    String booknumber = String.valueOf(booknum_new);
                                    dblite.revisebook(bookisbn, booknumber);
                                }
                                db.close();
                            } else {
                                Toast.makeText(MainActivity.this, "书籍出版社不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "书籍作者不能为空", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "书籍名不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "书籍ISBN不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


        addbookisbn.getText().clear();
        addbookname.getText().clear();
        addbookauthor.getText().clear();
        addbookpublish.getText().clear();
        addbooknum.getText().clear();
    }


    //----------------------------------------查询所有书籍显示页面------------------------------------------------
    public void gotoSelect() {
        setContentView(R.layout.sheet_main);

        Button btn_back = this.findViewById(R.id.back_selectend);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v1.clear();
                lenght = 0;
                closeKeyboard();
                gotoQueryView();

            }
        });
        //数据
        list = new ArrayList<entity>();
        for (int i = 0; i < lenght; i++) {
            Book book = v1.get(i);
            list.add(new entity(book.getName(), book.getAuthor(), book.getPublish(), book.getBooknum()));
        }



        rv_sheet = (RecyclerView) findViewById(R.id.rv_sheet);
        //设置线性布局 Creates a vertical LinearLayoutManager
        rv_sheet.setLayoutManager(new LinearLayoutManager(this));
        //设置recyclerView每个item间的分割线
        rv_sheet.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));
        //创建recyclerView的实例，并将数据传输到适配器
        sheetAdapter = new sheetAdapter(list);
        sheetAdapter.setOnItemClickListener(new sheetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                listnum = position;
                closeKeyboard();
                gotomessage();
            }
        });
        rv_sheet.setAdapter(sheetAdapter);
    }

    public void gotomessage() {
        setContentView(R.layout.activity_main);
        Button btn_back = this.findViewById(R.id.detai_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                gotoSelect();
            }
        });

        TextView textView_isbn = this.findViewById(R.id.datei01);
        TextView textView_name = this.findViewById(R.id.deta02);
        TextView textView_author = this.findViewById(R.id.deta04);
        TextView textView_publish = this.findViewById(R.id.detai03);
        TextView textView_message = this.findViewById(R.id.detai05);
        TextView textView_num = this.findViewById(R.id.detai04);
        String getisbn = v1.get(listnum).getIsbn();
        SQLiteDatabase db = dblite.getWritableDatabase();
        String message = null;
        Cursor cursor = db.query("bookinformation", new String[]{"Introduction"}, "ISBN =" + getisbn , null, null, null, null);
        while (cursor.moveToNext()) {
            message = cursor.getString(cursor.getColumnIndex("Introduction")).trim();
        }
        textView_isbn.setText(v1.get(listnum).getIsbn());
        textView_name.setText(v1.get(listnum).getName());
        textView_author.setText(v1.get(listnum).getAuthor());
        textView_publish.setText(v1.get(listnum).getPublish());
        textView_message.setText(message);
        textView_num.setText(v1.get(listnum).getBooknum());
    }


//================================================添加用户============================================================

    public void gotouserset() {
        setContentView(R.layout.adduser);
        Button btn_backadd = this.findViewById(R.id.ImageButtonquery_qrid01);
        btn_backadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                goToMainMenu();
            }
        });
        final EditText addusernum1 = this.findViewById(R.id.addusernum);
        final EditText addusername1 = this.findViewById(R.id.addusername);
        final EditText adduserphone1 = this.findViewById(R.id.adduserphone);
        final EditText adduserpwd1 = this.findViewById(R.id.adduserpwd);
        Button btn_ok = this.findViewById(R.id.ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernum = addusernum1.getText().toString().trim();
                String username = addusername1.getText().toString().trim();
                String userphone = adduserphone1.getText().toString().trim();
                String userpwd = adduserpwd1.getText().toString().trim();
                if (!("").equals(usernum)) {
                    if (!("").equals(userpwd)) {
                        if (!("").equals(username)) {
                            if (isMobileNum(userphone)) {
                                dblite.adduser(usernum, userpwd, username, userphone);
                                Toast.makeText(MainActivity.this, "添加用户成功", Toast.LENGTH_SHORT).show();
                                addusernum1.getText().clear();
                                addusername1.getText().clear();
                                adduserphone1.getText().clear();
                                adduserpwd1.getText().clear();
                            } else {
                                Toast.makeText(MainActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                            adduserphone1.getText().clear();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addusernum1.getText().clear();
        addusername1.getText().clear();
        adduserphone1.getText().clear();
        adduserpwd1.getText().clear();
    }


    //-------------------------------------使用MySQL数据库------------------------------------------------------------------------------------
//    private void gotoIpView() {
//        setContentView(R.layout.client_main);
//        Button login = this.findViewById(R.id.login);   //登陆按钮
//        TextView lostpsw = this.findViewById(R.id.lostpassword);  //重置按钮
//        final EditText names = findViewById(R.id.name);
//        final EditText password = findViewById(R.id.password);
//        TextView lostpassword = findViewById(R.id.lostpassword);
//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                stringname = names.getText().toString().trim();
//                String stringpsw = password.getText().toString().trim();
//                String sqlpsw = DBUtil.selectpwd(stringname);   //调用数据库查询函数
//                if (stringpsw.equals(sqlpsw)){
//                    goToMainMenu();
//                }else {
//                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//------------------------------------------使用本地sqlite数据库----------------------------------------------
    private void gotoIpView() {
        setContentView(R.layout.client_main);
        Button login = this.findViewById(R.id.login);   //登陆按钮
        TextView lostpsw = this.findViewById(R.id.lostpassword);  //重置按钮

        final EditText names = findViewById(R.id.name);
        final EditText passwd = findViewById(R.id.password);

        lostpsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.lostpassword);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringname = names.getText().toString().trim();
                String stringpsw = passwd.getText().toString().trim();
                if (TextUtils.isEmpty(stringname)) {
                    Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(stringpsw)) {
                    Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else {
                    String password = null;
                    String useradmin = null;
                    SQLiteDatabase db = dblite.getWritableDatabase();
                    Cursor cursor = db.query("user", new String[]{"U_Num", "U_Pwd", "U_Value"}, null, null, null, null, null);
                    sname = stringname;     //保存用户名
                    while (cursor.moveToNext()) {      //判断用户名是否存在
                        String name = cursor.getString(cursor.getColumnIndex("U_Num")).trim();
                        password = cursor.getString(cursor.getColumnIndex("U_Pwd")).trim();
                        useradmin = cursor.getString(cursor.getColumnIndex("U_Value")).trim();
                        if (stringname.equals(name)) {
                            havelogin = true;
                            break;
                        } else {
                            havelogin = false;
                        }
                    }
                    if (havelogin) {
                        db.close();
                        cursor.close();
                        if (str0.equals(useradmin)) {
                            identity = 0;       //普通用户
                            closeKeyboard();
                            goToMainMenu();
                        } else if (str1.equals(useradmin)) {
                            identity = 1;       //普通管理员
                            closeKeyboard();
                            goToMainMenu();
                        } else if (str2.equals(useradmin)) {
                            identity = 2;       //超级管理员
                            closeKeyboard();
                            goToMainMenu();
                        } else {
                            Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //屏蔽返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return false;
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}