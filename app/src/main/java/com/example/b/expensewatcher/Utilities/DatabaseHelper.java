package com.example.b.expensewatcher.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.b.expensewatcher.models.CategoryPerExpense;
import com.example.b.expensewatcher.models.Expense;
import com.example.b.expensewatcher.models.RecyclerViewCategory;

import java.util.Arrays;
import java.util.Comparator;


/**
 * Created by B on 16-Mar-17.
 */

//TODO: W/SQLiteConnectionPool: A SQLiteConnection object for database '/com.example.b.expensewatcher.data/user/0/com.example.b.expensewatcher/databases/myexpenses.db' was leaked!  Please fix your application to end transactions in progress properly and to close the database when it is no longer needed.

public class DatabaseHelper extends SQLiteOpenHelper{
    //public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    //    super(context, "expenseTracker.db", null, 1);
    //}
    //TODO: Verify adding onDestroy method.

    Float sumofExpenses = new Float(0);
    Float remainingBalance = new Float(0);
    Float totalIncome = new Float(0);

    public DatabaseHelper(Context context)
    { super(context, "myexpenses.db", null, 2);}

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_MAIN_ACCOUNT = "CREATE TABLE IF NOT EXISTS MAIN_ACCOUNT("+
                "_id INTEGER PRIMARY KEY, "+
                "amount INTEGER NOT NULL, "+
                "timestamp INTEGER NOT NULL, "+
                "category TEXT NOT NULL, " +
                "details TEXT "+
                ")";

        String CREATE_TABLE_SECONDARY_ACCOUNT = "CREATE TABLE IF NOT EXISTS SECONDARY_ACCOUNT("+
                "_id INTEGER PRIMARY KEY, "+
                "amount INTEGER NOT NULL, "+
                "timestamp INTEGER NOT NULL, "+
                "category TEXT NOT NULL, " +
                "details TEXT "+
                ")";

        //TODO: Add subcategory
        String CREATE_TABLE_Category = "CREATE TABLE IF NOT EXISTS Category("+
                "_id INTEGER PRIMARY KEY, "+
                "title TEXT NOT NULL, "+
                "image TEXT NOT NULL, "+
                "cat_type TEXT NOT NULL "+
                ")";

/*
        String CREATE_TABLE_PASSCODE = "CREATE TABLE IF NOT EXISTS PASSCODE("+
                "_id INTEGER PRIMARY KEY, "+
                "passcode TEXT NOT NULL "+
                ")";
*/

        try{

            db.execSQL(CREATE_TABLE_Category);
           // db.execSQL(CREATE_TABLE_PASSCODE);
            db.execSQL(CREATE_TABLE_MAIN_ACCOUNT);
            db.execSQL(CREATE_TABLE_SECONDARY_ACCOUNT);

            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Income','income','Income')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Transportation','transportation','Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Mobile','mobile','Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Utilities','utilities', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Food','food','Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Personal','personal', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Childcare','childcare', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Clothing','clothing', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Education','education', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Events','firework', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Gifts','gifts', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Healthcare','healthcare', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Household','household', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Insurance','insurance', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Occupational','occupational', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Leisure','leisure', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Hobbies','hobbies', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Loans','loans', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Vacation','vacation', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Pet','pet', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Savings','savings', 'Expenditure')");
            db.execSQL("INSERT INTO Category(title, image, cat_type) VALUES ('Taxes','taxes', 'Expenditure')");

            //INSERT DEFAULT PASSWORD
           // db.execSQL("INSERT INTO PASSCODE(passcode) VALUES ('1234')");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS User");
            db.execSQL("DROP TABLE IF EXISTS Expense");
            db.execSQL("DROP TABLE IF EXISTS PASSCODE");
            db.execSQL("DROP TABLE IF EXISTS MAIN_ACCOUNT");
            db.execSQL("DROP TABLE IF EXISTS SECONDARY_ACCOUNT");
            db.execSQL("DROP TABLE IF EXISTS Category");
            onCreate(db);
            Log.d("OnUpgrade", "OnUpgrade called");
           // db.close();
        }
        else
            onCreate(db);
    }

    /*
        Read Passcode
     */
    public String readpasscode(DatabaseHelper db){

        SQLiteDatabase sq = db.getReadableDatabase();
        Cursor mCursor = sq.rawQuery("SELECT passcode FROM PASSCODE", new String[] {});

        if(mCursor.moveToNext())
            return mCursor.getString(0);
        else
            return "wrongpassword";
    }

    /*
        Change Passcode
     */
    public boolean changepasscode(DatabaseHelper db, String oldpassword, String newpassword) {

        Cursor mCursor;
        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("passcode",newpassword);

       mCursor = sq.rawQuery("SELECT _id FROM PASSCODE WHERE passcode=?", new String[] {oldpassword});

        if(mCursor.moveToNext()){
            cv.put("passcode",newpassword);
            sq.update("PASSCODE",cv,"_id=?", new String[]{mCursor.getString(0)});
            return true;
        }

        return false;
    }

    public boolean createExpense(DatabaseHelper db, Expense newExpense, String ACCOUNT) {

        SQLiteDatabase sq = db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(newExpense.amount != null && newExpense.category != null && newExpense.timestamp != null) {
            cv.put("amount", newExpense.amount);
            cv.put("timestamp", newExpense.timestamp);
            cv.put("category", newExpense.category);
            //cv.put("details", newExpense.details);

            sq.insert(ACCOUNT, null, cv);

            Log.d("SAVETRANSACTION", "Transaction added" + newExpense.amount);
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
            sq.insert("Category", null, cv);
            Log.d("SAVECATEGORY", ""+item);
            //db.close();
            return true;
        }
        else {
            //db.close();
            return false;
        }



        }

    public RecyclerViewCategory[] allCategories(DatabaseHelper db) {

        SQLiteDatabase sq = db.getReadableDatabase();
        RecyclerViewCategory[] category = new RecyclerViewCategory[0];
        Cursor mCursor = null;

        try {
            //may cause a problem
            mCursor = sq.rawQuery(
                    "SELECT * FROM Category",
                    null);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
        if(mCursor.moveToFirst()) {
            category = new RecyclerViewCategory[mCursor.getCount()];
            try{
                    for(int i = 0; i < mCursor.getCount(); i++, mCursor.moveToNext()) {
                        category[i] = new RecyclerViewCategory();
                        category[i].title = mCursor.getString(mCursor.getColumnIndex("title"));
                        category[i].image = mCursor.getString(mCursor.getColumnIndex("image"));
                        category[i].category_type = mCursor.getString(mCursor.getColumnIndex("cat_type"));
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

    /*
    Provide a list of all categories of expenses with their respective total sums.
     */
    public Expense[] allExpensesperCategory(DatabaseHelper db, String categoryname, String ACCOUNT) {
        SQLiteDatabase sq = db.getReadableDatabase();
        Expense[] expenses = null;
        Cursor mCursor = null;
        String query = "SELECT * FROM "+ACCOUNT+" WHERE category=?";

        try {
            //may cause a problem
            mCursor = sq.rawQuery(
                    query,
                    new String[]{categoryname});
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

        Arrays.sort(expenses, new Comparator<Expense>() {
                    @Override
                    public int compare(Expense lhs, Expense rhs) {

                        if(lhs.timestamp < rhs.timestamp)
                        return 1;
                        else if(lhs.timestamp > rhs.timestamp)
                            return -1;
                        else
                            return 0;

                    }
                });

        mCursor.close();
        return expenses;

    }

    public Expense[] allExpenses(DatabaseHelper db, String ACCOUNT) {
        SQLiteDatabase sq = db.getReadableDatabase();
        Expense[] expenses = null;
        Cursor mCursor = null;
        String query = "SELECT * FROM "+ACCOUNT+" ORDER BY timestamp DESC";

        try {
            //may cause a problem
            mCursor = sq.rawQuery(
                    query,
                    null);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(mCursor != null && mCursor.moveToFirst()) {

            Log.d("Count", String.valueOf(mCursor.getCount()));
            try{
                expenses = new Expense[mCursor.getCount()];

                    for(int i = 0; i < mCursor.getCount(); i++) {
                        /*
                        Log.d("ALLEXP, ID", String.valueOf(mCursor.getInt(0)));
                        Log.d("AMOUNT", String.valueOf(mCursor.getFloat(1)));
                        Log.d("TIMESTAMP", mCursor.getString(2));
                        Log.d("CATEGORY", mCursor.getString(3));
                        */

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
                mCursor.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            mCursor.close();
            return null;
        }

        return expenses;
    }

    public int deleteExpense(DatabaseHelper db, int expenseId, String ACCOUNT) {
        SQLiteDatabase sq = db.getWritableDatabase();
        int value = 0;

        try{
            value = sq.delete(ACCOUNT, "_id=?", new String[]{Integer.toString(expenseId)});
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

    public boolean updateExpense(DatabaseHelper databaseHelper, Expense newExpense, String ACCOUNT) {

        SQLiteDatabase sq = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        if(newExpense.amount != null && newExpense.category != null && newExpense.timestamp != null) {

            cv.put("amount", newExpense.amount);
            cv.put("timestamp", newExpense.timestamp);
            cv.put("category", newExpense.category);

            sq.update(ACCOUNT, cv, "_id=?", new String[]{Integer.toString(newExpense.$id)} );
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
            if (mCursor.getString(0).equals("Expenditure")) {
                mCursor.close();
                return true;
            }
            else {
                mCursor.close();
                return false;
            }
        }

        mCursor.close();
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

    
    public CategoryPerExpense[] pieChartCalc(DatabaseHelper db, Expense[] expenses, String ACCOUNT) {

        CategoryPerExpense[] categoricalexpenses = new CategoryPerExpense[0];
        SQLiteDatabase sq = db.getReadableDatabase();

        //This query provides a list of all categories in the ACCOUNT
        // plus the number of times each category has been selected
        String query = "SELECT category, count(*) FROM Category, "+ACCOUNT+" WHERE "+
                "category = Category.title AND Category.cat_type = \"Expenditure\""+
                "GROUP BY category";
        Cursor mCursor = sq.rawQuery(query, new String[]{});
        
        if(mCursor.moveToNext()) {

            //Initializing the double array
            categoricalexpenses = new CategoryPerExpense[mCursor.getCount()];
            int count_category;

            /*
            categoricalexpenses[i][0] : Category Name
            categoricalexpenses[i][1] : Total Expenses per Category
            categoricalexpenses[i][2] : Share of total expense per category of total expenses of all categories
             */
            //Find all the categories that have been used
            for(count_category = 0; count_category < mCursor.getCount(); count_category++, mCursor.moveToNext()) {

                    categoricalexpenses[count_category] = new CategoryPerExpense();
                    categoricalexpenses[count_category].category_title = mCursor.getString(0);
                    Log.d("CATEXPLIST", categoricalexpenses[count_category].category_title);

            }

            //Sum up the expense for each category
            float catsumexp;
            float totalexp = 0;
           for(int a = 0; a < count_category; a++) {

               catsumexp = 0;
               for (int b = 0; b < expenses.length; b++) {

                   if(categoricalexpenses[a].category_title.equals(expenses[b].category)) {
                       catsumexp += expenses[b].amount;
                       totalexp += catsumexp;
                   }

                }
               categoricalexpenses[a].total_expenses_per_category = catsumexp;
            }

            //Calculate how much each category is of the sum of all expenses
            for(int a = 0; a < count_category; a++)
                categoricalexpenses[a].share_expenses_per_category = Float.valueOf(categoricalexpenses[a].total_expenses_per_category) / totalexp;

        }


        //Sort the array of expenses category wise in descending order
        Arrays.sort(categoricalexpenses, new Comparator<CategoryPerExpense>() {
            @Override
            public int compare(CategoryPerExpense lhs, CategoryPerExpense rhs) {
                if(lhs.total_expenses_per_category > rhs.total_expenses_per_category)
                    return -1;
                else if(lhs.total_expenses_per_category < rhs.total_expenses_per_category)
                    return 1;
                else
                    return 0;
            }
        });


        mCursor.close();
        return categoricalexpenses;
    }

    public String[][] lineChartCalc(DatabaseHelper db, Expense[] expenses) {

        String[][] totalexpensesbymonth = new String[0][2];
        return totalexpensesbymonth;
    }

}

