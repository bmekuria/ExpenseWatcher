package com.example.b.expensewatcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.ExpensesPerMonthlyCategoryAdapter;
import com.example.b.expensewatcher.models.Expense;

import com.example.b.expensewatcher.data.ExpenseWatcherPreferences;

public class CategoryMonthlyActivity extends AppCompatActivity {
    RecyclerView expenses_category_list;
    AlertDialog expenseDeleterDialog;
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

        expenses_category_list = (RecyclerView) findViewById(R.id.expenses_per_category_list);
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

            new CategoryMonthlyExpenses().execute(chosenCategory);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CategoryMonthlyExpenses().execute(chosenCategory);
    }

    /**********************************************************************************************
     * DISPLAY ALL THE EXPENSES PER CATEGORY
     */
    public class CategoryMonthlyExpenses extends AsyncTask<String, Void, Expense[]> implements ExpensesPerMonthlyCategoryAdapter.Exp_Monthly_Category_ItemListener {
        @Override
        protected Expense[] doInBackground(String... params) {
            Expense[] expenseTrans = new Expense[0];
            WHICH_ACCOUNT = data.getPreferredAccount(CategoryMonthlyActivity.this);
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(CategoryMonthlyActivity.this);
                expenseTrans = databaseHelper.allMonthlyExpensesperCategory(databaseHelper,params[0],WHICH_ACCOUNT);
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

            ExpensesPerMonthlyCategoryAdapter expensesPerMonthlyCategoryAdapter =
                    new ExpensesPerMonthlyCategoryAdapter(CategoryMonthlyActivity.this,
                            expensesperCategory, this);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            expenses_category_list.addItemDecoration(new DividerItemDecoration(CategoryMonthlyActivity.this, LinearLayoutManager.VERTICAL));
            expenses_category_list.setLayoutManager(mLayoutManager);
            expenses_category_list.setAdapter(expensesPerMonthlyCategoryAdapter);
        }

        @Override
        public void onItemClick(Expense item) {

            Intent intent = new Intent(CategoryMonthlyActivity.this, MonthlyExpensesActivity.class);
            intent.putExtra("category", item.category);
            intent.putExtra("monthyear", item.monthyear);
            startActivity(intent);
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
            WHICH_ACCOUNT = data.getPreferredAccount(CategoryMonthlyActivity.this);

            try{
                DatabaseHelper databaseHelper = new DatabaseHelper(CategoryMonthlyActivity.this);
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
                Toast.makeText(CategoryMonthlyActivity.this,R.string.expensedel,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(CategoryMonthlyActivity.this,R.string.error,Toast.LENGTH_LONG).show();

            }
        }
    }

}
