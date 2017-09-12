package activitytest.example.com.book;

/**
 * Created by haha on 2017-08-29.
 */

public class CItem {
    private String ID = "";
    private String Value = "";
    public CItem(){
        ID = "";
        Value = "";
}

    public CItem (String _ID, String _Value){
        ID = _ID;
        Value = _Value;
    }
    public String toString(){
        return Value;
    }

    public String GetID(){
        return ID;
    }

    public String GetValue(){
        return Value;
    }

}
