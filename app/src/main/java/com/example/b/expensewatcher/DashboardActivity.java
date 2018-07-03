package com.example.b.expensewatcher;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b.expensewatcher.Utilities.DateFormatting;
import com.example.b.expensewatcher.Utilities.PrefManager;
import com.example.b.expensewatcher.models.Expense;
import com.example.b.expensewatcher.Utilities.DatabaseHelper;

import com.example.b.expensewatcher.data.ExpenseWatcherPreferences;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

import static com.google.android.gms.ads.AdSize.LARGE_BANNER;


/*
TODO: I/Choreographer: The application may be doing too much work on its main thread.
A SQLiteConnection object for database '/data/user/0/com.example.b.expensewatcher/databases/myexpenses.db' was leaked!  Please fix your application to end transactions in progress properly and to close the database when it is no longer needed.
 */


public class DashboardActivity extends AppCompatActivity{

    RecyclerView expenselist;
    List<Expense> sectionedExpenses;
    TextView textViewRemainingBalance, textViewTotalIncome, textViewTotalExpense;
    SectionedRecyclerViewAdapter expenseAdapter;
    private AdView mAdView;

    ExpenseWatcherPreferences data;
    public static String WHICH_ACCOUNT;

    /*
    TODO: For advanced version of Android have options from home screen to (Android 8)
    Add Transaction - TransactionActivity
    Analyze Transactions - PieChartActivity
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        data = new ExpenseWatcherPreferences();
        data.LoadLanguage(getBaseContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

         //http://stacktips.com/tutorials/android/actionbar-with-custom-view-example-in-android
        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.shapeGreen)));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        //To change background of the header set the background color to mCustomView
        View mCustomView = mInflater.inflate(R.layout.dashboard_menu, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.dashboard_title_header);
        mTitleTextView.setText(R.string.app_name);

        MobileAds.initialize(this,"ca-app-pub-1496600661129039~8008179970");
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView = new AdView(this);
        mAdView.setAdSize(LARGE_BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        try {
            mAdView.loadAd(adRequest);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        ImageButton settingsButton = (ImageButton) mCustomView
                .findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
            }
        });

        ImageButton analyzeButton = (ImageButton) mCustomView
                .findViewById(R.id.analyzeButton);
        analyzeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, pieChartActivity.class));
            }
        });


        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        textViewRemainingBalance = (TextView) findViewById(R.id.remainingbalance_textView);
        textViewTotalIncome = (TextView) findViewById(R.id.totalincome_textView);
        textViewTotalExpense = (TextView) findViewById(R.id.totalexpense_textView);

        //Displays all the expenses
        expenselist = (RecyclerView) findViewById(R.id.dashboard_expenses_list);
        expenselist.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));

        new DashBoardExpenses().execute();

        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, TransactionActivity.class));
            }
        });

        checkWelcomeSlider();
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        new DashBoardExpenses().execute();
        checkWelcomeSlider();
    }

    public void checkWelcomeSlider(){
        boolean check = new PrefManager(getBaseContext()).isFirstTimeLaunch();
        if(check){
            startActivity(new Intent(DashboardActivity.this,WelcomeActivity.class));
        }
    }


    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        MobileAds.initialize(this,"ca-app-pub-1496600661129039~8008179970");
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView = new AdView(this);
        mAdView.setAdSize(LARGE_BANNER);
        mAdView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        try {
            mAdView.loadAd(adRequest);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    /**********************************************************************************************
     * DISPLAY ALL THE EXPENSES ON THE DASHBOARD
     */
    public class DashBoardExpenses extends AsyncTask<Void, Void, Expense[]> {

        @Override
        protected Expense[] doInBackground(Void... params) {
            Expense[] expenseTrans = new Expense[0];
            WHICH_ACCOUNT = data.getPreferredAccount(DashboardActivity.this);

            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(DashboardActivity.this);
                expenseTrans = databaseHelper.allExpenses(databaseHelper, WHICH_ACCOUNT);
                databaseHelper.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }


            return expenseTrans;

        }

        @Override
        protected void onPostExecute(final Expense[] expenses) {
            super.onPostExecute(expenses);

            DatabaseHelper db = new DatabaseHelper(DashboardActivity.this);

            if(expenses != null) {
                float[] calcExpenses = db.calcExpenses(db, expenses);
                db.close();

                textViewRemainingBalance.setText(String.format("%.2f", calcExpenses[1] - calcExpenses[0]));

                textViewTotalIncome.setText(String.format("%.2f", calcExpenses[1]));
                textViewTotalIncome.setTextColor(Color.GREEN);

                textViewTotalExpense.setText("-" + String.format("%.2f", calcExpenses[0]));
                textViewTotalExpense.setTextColor(Color.RED);

                expenseAdapter = new SectionedRecyclerViewAdapter();

               Date date = expenses[0].timestamp;

                //Grouping the transactions by date
                for (int i = 0; i < expenses.length; i++) {
                    if (date.equals(expenses[i].timestamp) && i > 0) {
                        continue;
                    }

                    date = expenses[i].timestamp;
                    sectionedExpenses = getExpensesWithDate(date, expenses);

                    if (sectionedExpenses.size() > 0) {
                        expenseAdapter.addSection(new ExpensesSection(
                                new DateFormatting().formatDatetoString("notModel",date), sectionedExpenses));
                    }
                }

                expenselist.setAdapter(expenseAdapter);

            }
            else
            {
                textViewRemainingBalance.setText("0");

                textViewTotalIncome.setText("0");
                textViewTotalIncome.setTextColor(Color.GREEN);

                textViewTotalExpense.setText("0");
                textViewTotalExpense.setTextColor(Color.RED);
            }
        }
    }


    private class ExpensesSection extends StatelessSection {

        String title;
        List<Expense> list;

        public ExpensesSection(String title, List<Expense> list) {
            super(new SectionParameters.Builder(R.layout.item_expense)
                    .headerResourceId(R.layout.section_header)
                    .build());

            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            Float amount  = list.get(position).amount;
            String category = list.get(position).category;


            itemHolder.amount.setText(String.valueOf(amount));
            itemHolder.category.setText(category);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
               /* Toast.makeText(DashboardActivity.this, String.format("Clicked on position #%s of Section %s",
                        expenseAdapter.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
*/
                    Log.d("Intent Dash-Trans", String.valueOf(new StringBuilder().append(list.get(position).$id)
                            .append(list.get(position).amount).append(list.get(position).timestamp).append(list.get(position).category)));

                    Intent intent = new Intent(DashboardActivity.this, TransactionActivity.class);
                    intent.putExtra("id", list.get(position).$id);
                    intent.putExtra("amount", list.get(position).amount);
                    intent.putExtra("date", list.get(position).timestamp);
                    intent.putExtra("category", list.get(position).category);
                    intent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);

                    startActivity(intent);
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.tvTitle.setText(title);
        }

        private class HeaderViewHolder extends RecyclerView.ViewHolder {

            private final TextView tvTitle;

            HeaderViewHolder(View view) {
                super(view);

                tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            }
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {

            private final View rootView;
            private final TextView amount;
            private final TextView category;

            ItemViewHolder(View view) {
                super(view);

                rootView = view;
                amount = (TextView) view.findViewById(R.id.expenseAmount);
                category = (TextView) view.findViewById(R.id.expenseCategory);
            }
        }

    }


    private List<Expense> getExpensesWithDate(Date date, Expense[] expenses) {
        List<Expense> sectionedExpenses = new ArrayList<>();

       for(int i = 0; i < expenses.length; i++){
            if (date.equals(expenses[i].timestamp)) {
                sectionedExpenses.add(expenses[i]);
            }
        }

        return sectionedExpenses;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //getMenuInflater().inflate(R.menu.piechart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        //TODO: Delete this menu
        switch(item.getItemId()){
            case R.id.menu_settings: startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));break;
            case R.id.menu_analyze: startActivity(new Intent(DashboardActivity.this, pieChartActivity.class)); break;
            default: break;
        }

        return super.onOptionsItemSelected(item);

    }

    private Boolean exit = false;

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        expenselist.setAdapter(null);

    }

    @Override
    public void onBackPressed() {
        if(exit) {
            finish();
        }
        else
        {
            Toast.makeText(this, R.string.backagain,Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

}
