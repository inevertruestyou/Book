package activitytest.example.com.book;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by haha on 2017-08-28.
 */

public class DbManger {
    private static SQLlite dblite;
    public static SQLlite getIntance(Context context){
        if (dblite == null){
            dblite = new SQLlite(context);
        }
        return dblite;
    }

    //查询数据

    /**
     *
     * @param db        数据库对象
     * @param sql       查询的sql语句
     * @param selectionArgs     查询条件的占位符
     * @return      //查询结果
     */
    public static Cursor selectDataBysql(SQLiteDatabase db, String sql, String[] selectionArgs){
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(sql, selectionArgs);
        }
        return cursor;
    }
}

//
