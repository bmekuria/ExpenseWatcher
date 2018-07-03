package com.example.b.expensewatcher;

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

import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.ExpensesPerMonthlyCategoryAdapter;
import com.example.b.expensewatcher.Utilities.MonthlyExpensesAdapter;
import com.example.b.expensewatcher.data.ExpenseWatcherPreferences;
import com.example.b.expensewatcher.models.Expense;

public class MonthlyExpensesActivity extends AppCompatActivity {

    RecyclerView expenses_category_month_list;
    String chosenCategory, monthyear, WHICH_ACCOUNT;
    ExpenseWatcherPreferences data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_expenses);

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.shapeGreen)));

        expenses_category_month_list = (RecyclerView) findViewById(R.id.monthly_expenses_per_category_list);
        data = new ExpenseWatcherPreferences();

        Intent mIntent = getIntent();
        if (mIntent != null) {

            chosenCategory = mIntent.getStringExtra("category");
            monthyear = mIntent.getStringExtra("monthyear");
            LayoutInflater mInflater = LayoutInflater.from(this);

            //To change background of the header set the background color to mCustomView
            View mCustomView = mInflater.inflate(R.layout.headers, null);
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.header_title);
            mTitleTextView.setText(chosenCategory + " " + monthyear);
            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
            mActionBar.setDisplayShowTitleEnabled(false);

            new MonthlyExpensesperCat().execute(chosenCategory,monthyear);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new MonthlyExpensesperCat().execute(chosenCategory,monthyear);
    }

    /**********************************************************************
         * DISPLAY ALL THE EXPENSES PER SPECIFIC CATEGORY AND MONTH - YEAR
         */
        public class MonthlyExpensesperCat extends AsyncTask<String, String, Expense[]>
        implements MonthlyExpensesAdapter.Monthly_Exp_ItemListener {

            @Override
            protected Expense[] doInBackground(String... params) {
                Expense[] expenseTrans = new Expense[0];
                WHICH_ACCOUNT = data.getPreferredAccount(MonthlyExpensesActivity.this);
                try {
                    DatabaseHelper databaseHelper = new DatabaseHelper(MonthlyExpensesActivity.this);
                    expenseTrans = databaseHelper.MonthExpenseperCategory(databaseHelper,params[0],params[1],WHICH_ACCOUNT);
                    databaseHelper.close();
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                Log.d("doInB,ExpLength",String.valueOf(expenseTrans.length));

                return expenseTrans;
            }

            @Override
            protected void onPostExecute(Expense[] expenses) {
                MonthlyExpensesAdapter MonthlyExpensesperCatAdapter =
                        new MonthlyExpensesAdapter(MonthlyExpensesActivity.this,
                                expenses, this);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                expenses_category_month_list.addItemDecoration(
                        new DividerItemDecoration(MonthlyExpensesActivity.this, LinearLayoutManager.VERTICAL));
                expenses_category_month_list.setLayoutManager(mLayoutManager);
                expenses_category_month_list.setAdapter(MonthlyExpensesperCatAdapter);
            }

            @Override
            public void onItemClick(Expense item) {
                Intent intent = new Intent(MonthlyExpensesActivity.this, TransactionActivity.class);
                intent.putExtra("id",item.$id);
                intent.putExtra("category", item.category);
                intent.putExtra("amount", item.amount);
                intent.putExtra("date",item.timestamp);
                startActivity(intent);
            }
        }


}
