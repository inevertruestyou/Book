package activitytest.example.com.book;


public class entity {
    //第一列表头
    private String sheetRow1;
    //第二列表头
    private String sheetRow2;
    //第三列表头
    private String sheetRow3;

    private String sheetRow4;

    public entity(String sheetRow1, String sheetRow2, String sheetRow3, String sheetRow4) {
        this.sheetRow1 = sheetRow1;
        this.sheetRow2 = sheetRow2;
        this.sheetRow3 = sheetRow3;
        this.sheetRow4 = sheetRow4;
    }

    public String getSheetRow1() {
        return sheetRow1;
    }


    public String getSheetRow2() {
        return sheetRow2;
    }


    public String getSheetRow3() {
        return sheetRow3;
    }


    public String getSheetRow4() {
        return sheetRow4;
    }


}