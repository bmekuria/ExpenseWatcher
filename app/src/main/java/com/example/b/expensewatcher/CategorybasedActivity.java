package com.example.b.expensewatcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.ExpensesPerCategoryAdapter;
import com.example.b.expensewatcher.Utilities.SwipeDetector;
import com.example.b.expensewatcher.models.Expense;

import com.example.b.expensewatcher.data.ExpenseWatcherPreferences;

public class CategorybasedActivity extends AppCompatActivity {

    ListView expenses_category_list;
    ExpensesPerCategoryAdapter expensesperCategoryAdapter;
    AlertDialog expenseDeleterDialog;
    SwipeDetector swipeDetector = new SwipeDetector();
    StringBuilder expenseMsgDel = new StringBuilder();
    int expenseToDelete = 0;
    String chosenCategory;
    ExpenseWatcherPreferences data;
    public static String WHICH_ACCOUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorybased);

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.shapeGreen)));

        expenses_category_list = (ListView) findViewById(R.id.expenses_per_category_list);
        data = new ExpenseWatcherPreferences();

        Intent mIntent = getIntent();
        if(mIntent != null) {

           chosenCategory = mIntent.getStringExtra("category");
            LayoutInflater mInflater = LayoutInflater.from(this);

            //To change background of the header set the background color to mCustomView
            View mCustomView = mInflater.inflate(R.layout.headers, null);
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.header_title);
            mTitleTextView.setText(chosenCategory);
            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);

            new CategoryExpenses().execute(chosenCategory);
        }

        expenseDeleterDialog =  new AlertDialog.Builder(this).setTitle(R.string.confirmdel)
                .setMessage(expenseMsgDel)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (expenseToDelete != 0) {
                            new deleteExpense().execute(expenseToDelete);
                            new CategoryExpenses().execute(chosenCategory);
                            expenseToDelete = 0;
                        }
                    }
                })
                .setNeutralButton(R.string.cancel, null) // don't need to do anything but dismiss here
                .create();

    }

    /**********************************************************************************************
     * DISPLAY ALL THE EXPENSES PER CATEGORY
     */
    public class CategoryExpenses extends AsyncTask<String, Void, Expense[]> {
        @Override
        protected Expense[] doInBackground(String... params) {
            Expense[] expenseTrans = new Expense[0];
            WHICH_ACCOUNT = data.getPreferredAccount(CategorybasedActivity.this);
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(CategorybasedActivity.this);
                expenseTrans = databaseHelper.allExpensesperCategory(databaseHelper,params[0],WHICH_ACCOUNT);
                databaseHelper.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            Log.d("doInB,ExpLength",String.valueOf(expenseTrans.length));

            return expenseTrans;
        }

        @Override
        protected void onPostExecute(final Expense[] expensesperCategory) {

            expensesperCategoryAdapter = new ExpensesPerCategoryAdapter(CategorybasedActivity.this, expensesperCategory);
            expenses_category_list.setAdapter(expensesperCategoryAdapter);
            expenses_category_list.setOnTouchListener(swipeDetector);
            expenses_category_list.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(swipeDetector.swipeDetected()) {
                                if(swipeDetector.getAction() == SwipeDetector.Action.RL) {
                                    expenseToDelete = expensesperCategory[position].$id;
                                    expenseMsgDel.append(getString(R.string.amount)+": "+expensesperCategory[position].amount)
                                            .append("\n"+getString(R.string.category)+": "+expensesperCategory[position].category);
                                    expenseDeleterDialog.show();
                                }
                            }
                            else {
                                Log.d("Chosen item", String.valueOf(expensesperCategory[position]));
                                Intent intent = new Intent(CategorybasedActivity.this, TransactionActivity.class);
                                intent.putExtra("id", expensesperCategory[position].$id);
                                intent.putExtra("amount", expensesperCategory[position].amount);
                                intent.putExtra("date", expensesperCategory[position].timestamp);
                                intent.putExtra("category", expensesperCategory[position].category);
                                startActivity(intent);
                            }
                        }
                    }
            );
        }
    }

    /*********************************************************************************************
     DELETE EXPENSE
     */
    private class deleteExpense extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int value = 0;
            Log.d("Exp2BDel",String.valueOf(params[0]));
            WHICH_ACCOUNT = data.getPreferredAccount(CategorybasedActivity.this);

            try{
                DatabaseHelper databaseHelper = new DatabaseHelper(CategorybasedActivity.this);
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
                Toast.makeText(CategorybasedActivity.this,R.string.expensedel,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(CategorybasedActivity.this,R.string.error,Toast.LENGTH_LONG).show();

            }
        }
    }

}
