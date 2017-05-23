package com.example.b.expensewatcher;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.b.expensewatcher.models.Category;
import com.example.b.expensewatcher.models.Expense;
import com.example.b.expensewatcher.Utilities.CategoryAdapter;
import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.DateFormatting;

import java.util.Arrays;
import java.util.Calendar;

public class TransactionActivity extends AppCompatActivity {

    private int year, month, day;
    EditText transaction_amount, transaction_date;
    Expense newExpense = new Expense();
    String chosen_category;
    Category[] categories;
    ListView categorylist;
    ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        setTitle("Enter Expenses");

        //Find all the existing views
        transaction_amount = (EditText) findViewById(R.id.transaction_value);
        transaction_date = (EditText) findViewById(R.id.transaction_date);
        categorylist = (ListView) findViewById(R.id.transaction_categoryList);

        //Setup the calendar
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //Set amount and date to the transaction that is to be updated.
        Intent mIntent = getIntent();
        if(mIntent.getExtras() != null) {
            transaction_amount.setText(String.format("%.2f",mIntent.getFloatExtra("amount",0)));

            //Split the date field into day, month, year and set the EditText to the date.
            DateFormatting dt = new DateFormatting();
            String[] datearray = new String[0];
            if(mIntent.getExtras() != null) {
                try {
                    datearray = dt.formatLongtoString(mIntent.getLongExtra("date", 0)).split("/");
                    transaction_date.setText(new StringBuilder().append(datearray[0]).
                            append("/").append(datearray[1]).append("/").append(datearray[2]).append(" "));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.d("mIntent id", String.valueOf(mIntent.getIntExtra("id",0)));
            Log.d("mIntent Amount", String.valueOf(mIntent.getFloatExtra("amount",0)));
            Log.d("mIntent Date", String.valueOf(new StringBuilder().append(datearray[0]).append(datearray[1]).append(datearray[2])));
            Log.d("mIntent Category", String.valueOf(mIntent.getStringExtra("category")));
        }
        else
        {
            transaction_date.setText(new StringBuilder().append(day).
                    append("/").append(month+1).append("/").append(year).append(" "));
        }

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,monthOfYear);
                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                transaction_date.setText(new StringBuilder().append(dayOfMonth).
                        append("/").append(monthOfYear+1).append("/").append(year).append(" "));
            }
        };

       /* transaction_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                new DatePickerDialog(TransactionActivity.this, date, c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

               return false;
            }
        });
        */
        //Allows the user to click on the date field and set the date.
        transaction_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TransactionActivity.this, date, c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        //Get a list of all the categories stored in the database
        DatabaseHelper databasehelper = new DatabaseHelper(TransactionActivity.this);
        categories = databasehelper.allCategories(databasehelper);

        //Populate listview with all categories
        CategoryAdapter allcategories = new CategoryAdapter(TransactionActivity.this, categories);
        categorylist.setAdapter(allcategories);
        categorylist.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        chosen_category = categories[position].title;
                    }
                }
        );

        //DONE: Set selection in listview
        if(mIntent.getExtras() != null) {

            int position = 0;
            for(Category a : categories) {

                if(a.title.equals(mIntent.getStringExtra("category")))
                break;

                position++;

            }
            categorylist.setItemChecked(position, true);
            chosen_category = mIntent.getStringExtra("category");
        }

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_addexpense);

    }

    public void displayResults(Boolean response) {
        if(response)
        {
            Intent mIntent = getIntent();
            if(mIntent.getExtras() != null)
                Toast.makeText(TransactionActivity.this, "Expense Updated", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(TransactionActivity.this, "You have added a new expense", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(TransactionActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
        }
    }

    /*
        ADD NEW EXPENSES. VARIABLES: AMOUNT, DATE, CATEGORY. TODO: DETAILS
     */
    public class addExpense extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            Log.d("Expense date",params[0].trim());
            Log.d("Expense amount", String.valueOf(Float.valueOf(params[1])));
            Log.d("Expense category", params[2]);

            //Assign the new details to object of class Expense.
            newExpense.amount = Float.valueOf(params[1]);
            newExpense.category = params[2];

            DateFormatting df = new DateFormatting();
            try{

                newExpense.timestamp = df.formatStringtoLong(params[0].trim());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            Log.d("AMOUNT", String.valueOf(newExpense.amount));
            Log.d("Category",newExpense.category);
            Log.d("Date",String.valueOf(newExpense.timestamp));

            //Call the createExpense function in the DatabaseHelper class
            DatabaseHelper databaseHelper = new DatabaseHelper(TransactionActivity.this);
            boolean response = databaseHelper.createExpense (databaseHelper, newExpense);

            return response;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            displayResults(aBoolean);

        }
    }

    /*
        UPDATES THE EXISTING EXPENSE. VARIABLES USED ARE ID, AMOUNT, DATE AND CATEGORY. TODO: ADD DETAILS
     */
    public class updateExpense extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }


            Log.d("UpdateExpID",params[0]);
            Log.d("UpdateExpDate",params[1]);
            Log.d("UpdateExpAmount", String.valueOf(Float.valueOf(params[2])));
            Log.d("UpdateExpCat", params[3]);

            DateFormatting df = new DateFormatting();
            try{

                newExpense.timestamp = df.formatStringtoLong(params[1].trim());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            //Assign the new details to object of class Expense.
            newExpense.$id = Integer.valueOf(params[0]);
            newExpense.amount = Float.valueOf(params[2]);
            newExpense.category = params[3];

            Log.d("UpdateExpAmount2", String.valueOf(newExpense.amount));
            Log.d("UpdateExpCat2",newExpense.category);
            Log.d("UpdateExpDate2",String.valueOf(newExpense.timestamp));

            //Call the createExpense function in the DatabaseHelper file
            DatabaseHelper databaseHelper = new DatabaseHelper(TransactionActivity.this);
            boolean response = databaseHelper.updateExpense (databaseHelper, newExpense);

            return response;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            displayResults(aBoolean);
        }
    }

    //Inflate all the required menus
    //TODO: Move the 'Cancel' option to the left hand side.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exit,menu);
        getMenuInflater().inflate(R.menu.addexpense ,menu);
        //getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_addexpense:
                if(!transaction_amount.getText().toString().equals(null) || !chosen_category.equals(null) || !transaction_date.getText().toString().equals(null)) {
                    Intent mIntent = getIntent();
                    if(mIntent.getExtras() == null) {
                        new addExpense().execute(transaction_date.getText().toString(), transaction_amount.getText().toString(), chosen_category);
                        transaction_amount.setText(null);
                        transaction_amount.setHint(R.string.entervalue);
                        // Clear the choices in the ListView categorylist
                        categorylist.setItemChecked(categorylist.getSelectedItemPosition(),false);
                    }
                    else
                        new updateExpense().execute(String.valueOf(mIntent.getIntExtra("id",0)), transaction_date.getText().toString(), transaction_amount.getText().toString(), chosen_category);
                }
                else {
                    Toast.makeText(this, R.string.details_missing, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_exit:
                //TODO: Close TransactionAct and refresh DashboardActivity
                NavUtils.navigateUpFromSameTask(TransactionActivity.this);
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(TransactionActivity.this);
                break;
           /* case R.id.menu_dashboard:
                Toast.makeText(TransactionActivity.this,"More options coming...",Toast.LENGTH_LONG).show();
                break;
            case R.id.menu_settings:
                Toast.makeText(TransactionActivity.this,"More options coming...",Toast.LENGTH_LONG).show();
                break;*/
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }
}
