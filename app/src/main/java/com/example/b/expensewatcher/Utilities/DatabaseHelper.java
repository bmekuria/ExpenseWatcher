package com.example.b.expensewatcher.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.b.expensewatcher.models.Category;
import com.example.b.expensewatcher.models.Expense;
import com.example.b.expensewatcher.models.User;

import java.util.Arrays;
import java.util.Comparator;


/**
 * Created by B on 16-Mar-17.
 */

//TODO: W/SQLiteConnectionPool: A SQLiteConnection object for database '/data/user/0/com.example.b.expensewatcher/databases/myexpenses.db' was leaked!  Please fix your application to end transactions in progress properly and to close the database when it is no longer needed.

public class DatabaseHelper extends SQLiteOpenHelper{
    //public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    //    super(context, "expenseTracker.db", null, 1);
    //}
    //TODO: Verify adding onDestroy method.

    Float sumofExpenses = new Float(0);
    Float remainingBalance = new Float(0);
    Float totalIncome = new Float(0);

    public DatabaseHelper(Context context)
    { super(context, "myexpenses.db", null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_Expense = "CREATE TABLE IF NOT EXISTS Expense("+
                "_id INTEGER PRIMARY KEY, "+
                "amount INTEGER NOT NULL, "+
                "timestamp INTEGER NOT NULL, "+
                "category TEXT NOT NULL, " +
                "details TEXT "+
                ")";

        String CREATE_TABLE_Category = "CREATE TABLE IF NOT EXISTS Category("+
                "_id INTEGER PRIMARY KEY, "+
                "title TEXT NOT NULL, "+
                "cat_type TEXT NOT NULL "+
                ")";

        String CREATE_TABLE_User = "CREATE TABLE IF NOT EXISTS User("+
                "_id INTEGER PRIMARY KEY, "+
                "userName TEXT NOT NULL, "+
                "firstName TEXT NOT NULL, "+
                "lastName TEXT NOT NULL, "+
                "passcode TEXT NOT NULL, "+
                "mobileNumber TEXT "+
                ")";

        try{

            db.execSQL(CREATE_TABLE_Category);
            db.execSQL(CREATE_TABLE_User);
            db.execSQL(CREATE_TABLE_Expense);


            Log.d("CREATING Transaction",CREATE_TABLE_Expense);
            Log.d("CREATING TABLE Category", CREATE_TABLE_Category);
            Log.d("CREATING TABLE USER",CREATE_TABLE_User);


          //DONE: Insert into table Category
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Income', 'Income')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Clothes', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Medical', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Groceries', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Cafe', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Bills', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Snacks', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Transportation', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Mobile', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Furniture', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Gym', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Medical', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, cat_type) VALUES ('Holidays', 'Expenditure')");
            //db.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS User");
            db.execSQL("DROP TABLE IF EXISTS Expense");
            db.execSQL("DROP TABLE IF EXISTS Category");
            onCreate(db);
            Log.d("OnUpgrade", "OnUpgrade called");
           // db.close();
        }
        else
            onCreate(db);
    }

    public void createUser(DatabaseHelper db, User user) {

        Cursor mCursor;
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        mCursor = sq.rawQuery("SELECT * FROM User WHERE userName=?", new String[]{user.userName});
        Log.d("SAVEUSER ",""+ String.valueOf(mCursor));
        if(!mCursor.moveToNext()) {
            cv.put("userName", user.userName);
            cv.put("firstName", user.firstName);
            cv.put("lastName", user.lastName);
            cv.put("passcode", user.passcode);
            cv.put("mobileNumber", user.mobileNumber);

            sq.insert("User", null, cv);

            Log.d("SAVEPROFILE", "User data added" +user.userName);
        }

        //db.close();
    }

    public boolean createExpense(DatabaseHelper db, Expense newExpense) {

        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(newExpense.amount != null && newExpense.category != null && newExpense.timestamp != null) {
            cv.put("amount", newExpense.amount);
            cv.put("timestamp", newExpense.timestamp);
            cv.put("category", newExpense.category);
            //cv.put("details", newExpense.details);

            sq.insert("Expense", null, cv);

            Log.d("SAVETRANSACTION", "Transaction data added" + newExpense.amount);
           // db.close();
            return true;
        }
        else
        {
            //db.close();
            return false;
        }
    }

    //TODO: Create Category
    public Boolean createCategory(DatabaseHelper db, String item) {

        Cursor mCursor;

        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        mCursor = sq.rawQuery("SELECT * FROM Category WHERE title=?", new String[]{item});
        Log.d("Category Saving.... ",""+ String.valueOf(mCursor));
        if(!mCursor.moveToNext()) {
            cv.put("title", item);
            //TODO: Check that the table name is used instead of the column name
            sq.insert("Category", null, cv);
            Log.d("SAVECATEGORY", "Category data added" +item);
            //db.close();
            return true;
        }
        else {
            //db.close();
            return false;
        }



        }

    public Category[] allCategories(DatabaseHelper db) {

        SQLiteDatabase sq = db.getReadableDatabase();
        Category[] category = null;
        Cursor mCursor = null;

        try {
            //may cause a problem
            mCursor = sq.rawQuery(
                    "SELECT title FROM Category",
                    null);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        if(mCursor != null) {
            category = new Category[mCursor.getCount()];
            try{
                if(mCursor.moveToFirst()) {
                    for(int i = 0; i < mCursor.getCount(); i++, mCursor.moveToNext()) {
                        category[i] = new Category(mCursor.getString(mCursor.getColumnIndex("title")));
                    }
                }
                mCursor.close();
            }
            catch(NullPointerException e) {
                e.printStackTrace();
            }
        }

        //db.close();
        return  category;
    }

    public Expense[] allExpenses(DatabaseHelper db) {
        SQLiteDatabase sq = db.getReadableDatabase();
        Expense[] expenses = null;
        Cursor mCursor = null;

        try {
            //may cause a problem
            mCursor = sq.rawQuery(
                    "SELECT * FROM Expense",
                    null);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(mCursor != null) {

            Log.d("Count", String.valueOf(mCursor.getCount()));
            try{
                expenses = new Expense[mCursor.getCount()];
                if(mCursor.moveToFirst()) {

                    for(int i = 0; i < mCursor.getCount(); i++) {
                        Log.d("ALLEXP, ID", String.valueOf(mCursor.getInt(0)));
                        Log.d("AMOUNT", String.valueOf(mCursor.getFloat(1)));
                        Log.d("TIMESTAMP", mCursor.getString(2));
                        Log.d("CATEGORY", mCursor.getString(3));

                        expenses[i] = new Expense();
                        expenses[i].$id = mCursor.getInt(0);
                        expenses[i].amount = mCursor.getFloat(1);
                        expenses[i].category = mCursor.getString(3);

                        try {
                            expenses[i].timestamp = Long.parseLong(mCursor.getString(2));
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }

                        mCursor.moveToNext();

                    }

                }
                mCursor.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        //db.close();

        return expenses;
    }

    public int deleteExpense(DatabaseHelper db, int expenseId) {
        SQLiteDatabase sq = db.getWritableDatabase();
        int value = 0;

        try{
            value = sq.delete("Expense", "_id=?", new String[]{Integer.toString(expenseId)});
            Log.d("DEL RETURN VALUE",String.valueOf(value));

        }
        catch (Exception e)
        {
            e.printStackTrace();
            value = 0;
        }

        //db.close();
        return value;
    }

    public boolean updateExpense(DatabaseHelper databaseHelper, Expense newExpense) {

        SQLiteDatabase sq = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(newExpense.amount != null && newExpense.category != null && newExpense.timestamp != null) {

            cv.put("amount", newExpense.amount);
            cv.put("timestamp", newExpense.timestamp);
            cv.put("category", newExpense.category);

            sq.update("Expense", cv, "_id=?", new String[]{Integer.toString(newExpense.$id)} );
            //databaseHelper.close();
            return true;
        }
        //else
        //    databaseHelper.close();
        return false;
    }

    private Boolean isExpenditure(DatabaseHelper db, String categoryname) {

        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor mCursor = sq.rawQuery("SELECT cat_type FROM Category WHERE title=?", new String[]{categoryname});

        if(mCursor.moveToNext()) {
            if (mCursor.getString(0).equals("Expenditure"))
                return true;
            else
                return false;
        }

        return false;
    }

    public float[] calcExpenses(DatabaseHelper db, Expense[] expenses) {

        float[] totalcal = new float[2];
        //If the item is an expenditure add
        for(int i = 0; i < expenses.length; i++) {
            if (isExpenditure(db, expenses[i].category)) {

                sumofExpenses += expenses[i].amount;

            } else {
                totalIncome += expenses[i].amount;
            }
        }
        totalcal[0] = sumofExpenses;
        totalcal[1] = totalIncome;
        return totalcal;
    }
    
    public String[][] pieChartCalc(DatabaseHelper db, Expense[] expenses) {

        String[][] categoricalexpenses = new String[0][0];
        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor mCursor = sq.rawQuery("SELECT category, count(*) FROM Expense GROUP BY category", new String[]{});
        
        if(mCursor.moveToNext()) {
            
            categoricalexpenses = new String[mCursor.getCount()][3];
            int i;

            /*
            categoricalexpenses[i][0] : Category Name
            categoricalexpenses[i][1] : Total Expenses per Category
            categoricalexpenses[i][2] : Share of total expense per category of total expenses of all categories
             */
            //Find all the categories that have been used
            for(i = 0; i < mCursor.getCount(); i++, mCursor.moveToNext()) {
                categoricalexpenses[i][0] = mCursor.getString(0);
                Log.d("CATEXPLIST",categoricalexpenses[i][0]);
            }

            //Sum up the expense for each category
            float catsumexp;
            float totalexp = 0;
           for(int a = 0; a < i; a++) {
               catsumexp = 0;
               for (int b = 0; b < expenses.length; b++) {
                   if(categoricalexpenses[a][0].equals(expenses[b].category)) {
                       catsumexp += expenses[b].amount;
                       totalexp += catsumexp;
                   }
                }
               categoricalexpenses[a][1] = String.valueOf(catsumexp);
            }

            //Calculate how much each category is of the sum of all expenses
            for(int a = 0; a < i; a++)
                categoricalexpenses[a][2] = String.valueOf(Float.valueOf(categoricalexpenses[a][1])/totalexp);

        }


        //Sort the array of expenses category wise in descending order
        Arrays.sort(categoricalexpenses, new Comparator<String[]>() {
            @Override
            public int compare(String[] lhs, String[] rhs) {

                //Log.d("ORDERING", String.valueOf(new StringBuilder()
                //.append(lhs[1]).append(" ").append(rhs[1])));

                if(Float.valueOf(lhs[1]) > Float.valueOf(rhs[1]))
                    return 1;
                else if(Float.valueOf(lhs[1]) < Float.valueOf(rhs[1]))
                    return -1;
                else
                return 0;

            }
        });

        return categoricalexpenses;
    }

    public String[][] lineChartCalc(DatabaseHelper db, Expense[] expenses) {

        String[][] totalexpensesbymonth = new String[0][2];
        return totalexpensesbymonth;
    }

}

