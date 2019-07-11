package com.ahmet.orderfoods.LocaDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmet.orderfoods.Model.Order;

import java.util.ArrayList;


/**
 * Created by ahmet on 7/23/2017.
 * Updated by ahmet on 6/16/2019.
 */

public class OrdersDatabase extends SQLiteOpenHelper {

    private static final String DB_Name = "OrderFood.db";
    private static final int DB_Version = 1;

    private static final String Table_NAME = "order_";

    private static final String KEY_ID = "ID";
    private static final String KEY_ProductID = "ProductID";
    private static final String KEY_ProductName = "ProductName";
    private static final String KEY_Quantity = "Quantity";
    private static final String KEY_Price = "Price";
    private static final String KEY_Discount = "Discount";


    public OrdersDatabase(Context context) {
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String create_Table = "create table "+Table_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +KEY_ProductID+" varchar(150),"
                +KEY_ProductName+" varchar(200) ,"
                +KEY_Quantity+" varchar(100) ,"
                +KEY_Price+" varchar(10) ,"
                +KEY_Discount+" varchar(25))";

        sqLiteDatabase.execSQL(create_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        String delete_query = "DROP table "+Table_NAME+" IF EXITS";
        sqLiteDatabase.execSQL(delete_query);

        onCreate(sqLiteDatabase);
    }

    //Methoed Add new Data
    public void addCarts(Order order){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues orderValues = new ContentValues();

        orderValues.put(KEY_ProductID, order.getProductId());
        orderValues.put(KEY_ProductName, order.getProductName());
        orderValues.put(KEY_Quantity, order.getQuantity());
        orderValues.put(KEY_Price, order.getPrice());
        orderValues.put(KEY_Discount, order.getDiscount());

        db.insert(Table_NAME, null, orderValues);
    }

    //Methoed get All Data
    public ArrayList<Order> getOrders(){

        ArrayList<Order> mListOrder = new ArrayList<>();

        String select_query = "select * from "+Table_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select_query, null);

        if (cursor.moveToFirst()){

            do{
                int ID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                String productId = cursor.getString(cursor.getColumnIndex(KEY_ProductID));
                String productName = cursor.getString(cursor.getColumnIndex(KEY_ProductName));
                String quantity = cursor.getString(cursor.getColumnIndex(KEY_Quantity));
                String price = cursor.getString(cursor.getColumnIndex(KEY_Price));
                String discount = cursor.getString(cursor.getColumnIndex(KEY_Discount));

                Order order = new Order(ID, productId, productName, quantity, price, discount);
                mListOrder.add(order);

            }while(cursor.moveToNext());
        }
        return mListOrder;
    }

    // Mothed one get Contact by Id in Database


    //Methoed one Delete Data
    public int clearFromCart(Order order){
        SQLiteDatabase db =this.getWritableDatabase();
        return db.delete(Table_NAME, "id=?", new String[]{String.valueOf(order.getId())});
    }

    //Methoed Delete All Data
    public void clearFromCart(int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+Table_NAME+" where id = "+id);
        sqLiteDatabase.close();
    }

    //Methoed2 Delete All Data
    public void clearCarts(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from "+Table_NAME+"");
        sqLiteDatabase.close();
    }

    public int checkCarts(String productID) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("select * from "+Table_NAME+" Where "+KEY_ProductID+" Like'" + productID + "'", null);
        int count = rs.getCount();
        return count;
    }
}
