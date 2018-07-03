package com.example.b.expensewatcher;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b.expensewatcher.Utilities.AutoFitGridLayoutManager;
import com.example.b.expensewatcher.Utilities.DecimalDigitsInputFilter;
import com.example.b.expensewatcher.Utilities.RecyclerViewCategoryAdapter;
import com.example.b.expensewatcher.models.Category;
import com.example.b.expensewatcher.models.Expense;
import com.example.b.expensewatcher.Utilities.CategoryAdapter;
import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.DateFormatting;

import java.util.Calendar;
import java.util.Date;

import com.example.b.expensewatcher.data.ExpenseWatcherPreferences;
import com.example.b.expensewatcher.models.RecyclerViewCategory;

public class TransactionActivity extends AppCompatActivity implements RecyclerViewCategoryAdapter.ItemListener {

    private int year, month, day, itemToDelete;
    EditText transaction_amount, transaction_date;
    TextView transaction_delete;
    Expense newExpense = new Expense();
    String chosen_category;
    RecyclerViewCategory[] categories;
    RecyclerView categorylist;
    ExpenseWatcherPreferences data;
    AlertDialog.Builder expenseDeleterDialog;
    Intent mIntent;

    public static String WHICH_ACCOUNT;

    @Override
    public void onItemClick(RecyclerViewCategory item) {
        chosen_category = item.title;
        Toast.makeText(this,item.title,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        //TODO: Set title to center
        //setTitle(getString(R.string.enter_expense_header));

        data = new ExpenseWatcherPreferences();
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.shapeGreen)));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        //Set Header
        View mCustomView = mInflater.inflate(R.layout.transaction_menu, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.enter_expense_text);
        mTitleTextView.setText(R.string.enter_expense_header);

        //Find all the existing views
        transaction_delete = (TextView) findViewById(R.id.transaction_delete);
        transaction_delete.setVisibility(View.INVISIBLE);

        transaction_amount = (EditText) findViewById(R.id.transaction_value);
        transaction_date = (EditText) findViewById(R.id.transaction_date);
        categorylist = (RecyclerView) findViewById(R.id.transaction_categoryList);

        //Setting integer and decimal limits. Currently set at 10 integers and 2 decimals
        transaction_amount.setFilters(new InputFilter[]
                {new DecimalDigitsInputFilter(10,2)});

        //Setup the calendar
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        //In case of an update transaction, set amount and date to the transaction that is to be updated.
        mIntent = getIntent();
        if(mIntent.getExtras() != null) {
            //Log.d("Chosen Item: TransAct",String.valueOf(mIntent.getIntExtra("id",0)));
            transaction_amount.setText(String.format("%.2f",mIntent.getFloatExtra("amount",0)));
            transaction_amount.setEnabled(true);
            transaction_delete.setVisibility(View.VISIBLE);

            itemToDelete = mIntent.getIntExtra("id", 0);
            transaction_delete.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View v) {
                                                          if (itemToDelete != 0) {
                                                              expenseDeleterDialog.show();
                                                          }
                                                      }
                                                  }
            );

            expenseDeleterDialog = new AlertDialog.Builder(this).setTitle(R.string.confirmdel)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new deleteExpense().execute(itemToDelete);
                            itemToDelete = 0;
                            refreshIntent();
                            finish();
                        }
                    })
                    .setNeutralButton(R.string.cancel, null);

            //Split the date field into day, month, year and set the EditText to the date.
            DateFormatting dt = new DateFormatting();
            String[] datearray = new String[0];

                chosen_category = mIntent.getStringExtra("category");

                try {
                    datearray = dt.formatDatetoString("notmodel",(Date)mIntent.getSerializableExtra("date")).split("/");
                    transaction_date.setText(new StringBuilder().append(datearray[0]).
                            append("/").append(datearray[1]).append("/").append(datearray[2]).append(" "));
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
        else
        {
            //If not an update, set the date to the current date
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
        databasehelper.close();

        //Populate listview with all categories
        final RecyclerViewCategoryAdapter allcategories = new RecyclerViewCategoryAdapter(TransactionActivity.this, categories, this);
        categorylist.setAdapter(allcategories);


        AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, 200);
        categorylist.setLayoutManager(layoutManager);
        categorylist.setBackgroundColor(getResources().getColor(R.color.cardview_light_background));
        categorylist.setHasFixedSize(true);


        //To highlight the particular category
        if(mIntent.getExtras() != null) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        refreshIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshIntent();
    }

    private void refreshIntent() {
        int flags = mIntent.getFlags();
        if((flags & mIntent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) != 0) {
            mIntent.removeExtra("id");
            mIntent.removeExtra("amount");
            mIntent.removeExtra("date");
            mIntent.removeExtra("category");
        }
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
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            //Refer to the MAIN or SECONDARY ACCOUNT
            WHICH_ACCOUNT = data.getPreferredAccount(TransactionActivity.this);

            //Assign the new details to object of class Expense.
            newExpense.amount = Float.valueOf(params[1]);
            newExpense.category = params[2];

            //Change the date format
            DateFormatting df = new DateFormatting();
            try{

                newExpense.timestamp = df.formatStringtoDate("Transaction", params[0].trim());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            //Call the createExpense function in the DatabaseHelper class
            DatabaseHelper databaseHelper = new DatabaseHelper(TransactionActivity.this);
            boolean response = databaseHelper.createExpense (databaseHelper, newExpense, WHICH_ACCOUNT);
            databaseHelper.close();

            return response;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            displayResults(aBoolean);

        }
    }

    /*
        UPDATE THE EXISTING EXPENSE. VARIABLES USED ARE ID, AMOUNT, DATE AND CATEGORY. TODO: ADD DETAILS
     */
    public class updateExpense extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            WHICH_ACCOUNT = data.getPreferredAccount(TransactionActivity.this);

            DateFormatting df = new DateFormatting();
            try{

                newExpense.timestamp = df.formatStringtoDate("Transaction",params[1].trim());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            //Assign the new details to object of class Expense.
            newExpense.$id = Integer.valueOf(params[0]);
            newExpense.amount = Float.valueOf(params[2]);
            newExpense.category = params[3];

            //Call the createExpense function in the DatabaseHelper file
            DatabaseHelper databaseHelper = new DatabaseHelper(TransactionActivity.this);
            boolean response = databaseHelper.updateExpense (databaseHelper, newExpense, WHICH_ACCOUNT);
            databaseHelper.close();

            return response;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            displayResults(aBoolean);
        }
    }

    /*********************************************************************************************
     DELETE EXPENSE
     */
    private class deleteExpense extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int value = 0;

            WHICH_ACCOUNT = data.getPreferredAccount(TransactionActivity.this);

            try{
                DatabaseHelper databaseHelper = new DatabaseHelper(TransactionActivity.this);
                value = databaseHelper.deleteExpense(databaseHelper,params[0],WHICH_ACCOUNT);
                databaseHelper.close();

            }
            catch(Exception e){
                e.printStackTrace();
            }

            return  value;
        }

        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);

            if(i != 0) {
                Toast.makeText(TransactionActivity.this,R.string.expensedel,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(TransactionActivity.this,R.string.error,Toast.LENGTH_LONG).show();

            }
        }
    }

    //Inflate all the required menus
    //TODO: Move the 'Cancel' option to the left hand side.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addexpense ,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_addexpense: addExpense();
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(TransactionActivity.this);
                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addExpense(){
        boolean emptyAmount = TextUtils.isEmpty(transaction_amount.getText().toString());
        boolean emptyCategory = TextUtils.isEmpty(chosen_category);
        boolean emptyDate = TextUtils.isEmpty(transaction_date.getText().toString());

        //Check if variables are empty, if not add expense.
        if(!emptyAmount && !emptyCategory && !emptyDate) {
            Intent mIntent = getIntent();

            if(mIntent.getExtras() == null) {
                new addExpense().execute(transaction_date.getText().toString(), transaction_amount.getText().toString(), chosen_category);
                transaction_amount.setText(null);
                transaction_amount.setHint(R.string.entervalue);

                // Clear the choices in the ListView categorylist
                //TODO (RecyclerView): categorylist.setItemChecked(categorylist.getSelectedItemPosition(),false);
            }
            else {
                new updateExpense().execute(String.valueOf(mIntent.getIntExtra("id", 0)),
                        transaction_date.getText().toString(), transaction_amount.getText().toString(), chosen_category);
                refreshIntent();
            }
        }
        else {
            Toast.makeText(this, R.string.details_missing, Toast.LENGTH_SHORT).show();
        }
    }
}
