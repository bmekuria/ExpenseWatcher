package com.example.b.expensewatcher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.b.expensewatcher.models.Expense;
import com.example.b.expensewatcher.Utilities.DashboardAdapter;
import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.SwipeDetector;

public class DashboardActivity extends AppCompatActivity{

    ListView expenselist;
    TextView textViewRemainingBalance, textViewTotalIncome, textViewTotalExpense;
    DashboardAdapter expenseAdapter;
    AlertDialog expenseDeleterDialog;
    int expenseToDelete = 0;
    SwipeDetector swipeDetector = new SwipeDetector();
    StringBuilder expenseMsgDel = new StringBuilder();
    //Button buttonAnalyze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //mVisualizerView = (VisualizerView) findViewById(R.id.activity_dashboard);
        setTitle("ExpenseWatcher");

        expenseDeleterDialog =  new AlertDialog.Builder(this).setTitle(R.string.confirmdel)
                .setMessage(expenseMsgDel)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (expenseToDelete != 0) {
                            new deleteExpense().execute(expenseToDelete);
                            new DashBoardExpenses().execute();
                            expenseToDelete = 0;
                        }
                    }
                })
                .setNeutralButton(R.string.cancel, null) // don't need to do anything but dismiss here
                .create();


        //Pie Chart view of expenditures
        expenselist = (ListView) findViewById(R.id.dashboard_expenses_list);
        textViewRemainingBalance = (TextView) findViewById(R.id.remainingbalance_textView);
        textViewTotalIncome = (TextView) findViewById(R.id.totalincome_textView);
        textViewTotalExpense = (TextView) findViewById(R.id.totalexpense_textView);
       // buttonAnalyze = (Button) findViewById(R.id.button_analyze);

        //Displays all the expenses
        new DashBoardExpenses().execute();


        FloatingActionButton fb = (FloatingActionButton) findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this, TransactionActivity.class));
            }
        });

        /*buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, pieChartActivity.class));
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        new DashBoardExpenses().execute();

    }

    /*********************************************************************************************
     DELETE EXPENSE
     */
    private class deleteExpense extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            int value = 0;
            Log.d("Exp2BDel",String.valueOf(params[0]));

            try{
                DatabaseHelper databaseHelper = new DatabaseHelper(DashboardActivity.this);
                value = databaseHelper.deleteExpense(databaseHelper,params[0]);
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
                Toast.makeText(DashboardActivity.this,R.string.expensedel,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(DashboardActivity.this,R.string.error,Toast.LENGTH_LONG).show();

            }
        }
    }

    /**********************************************************************************************
     * DISPLAY ALL THE EXPENSES ON THE DASHBOARD
     */
    public class DashBoardExpenses extends AsyncTask<Void, Void, Expense[]> {

        @Override
        protected Expense[] doInBackground(Void... params) {
            Expense[] expenseTrans = new Expense[0];
            try {
                DatabaseHelper databaseHelper = new DatabaseHelper(DashboardActivity.this);
                expenseTrans = databaseHelper.allExpenses(databaseHelper);
                databaseHelper.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            Log.d("doInB,ExpLength",String.valueOf(expenseTrans.length));

            return expenseTrans;

        }

        @Override
        protected void onPostExecute(final Expense[] expenses) {
            super.onPostExecute(expenses);

            DatabaseHelper db = new DatabaseHelper(DashboardActivity.this);
            float[] calcExpenses = db.calcExpenses(db,expenses);

            textViewRemainingBalance.setText(String.format("%.2f",calcExpenses[1]-calcExpenses[0]));

            textViewTotalIncome.setText(R.string.income+" "+String.format("%.2f",calcExpenses[1]));

            textViewTotalExpense.setText(R.string.expenditures+" "+String.format("%.2f",calcExpenses[0]));

            expenseAdapter = new DashboardAdapter(DashboardActivity.this, expenses);
            expenselist.setAdapter(expenseAdapter);
            expenselist.setOnTouchListener(swipeDetector);
            expenselist.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(swipeDetector.swipeDetected()) {
                                if(swipeDetector.getAction() == SwipeDetector.Action.RL) {
                                    expenseToDelete = expenses[position].$id;
                                    expenseMsgDel.append(R.string.amount+": "+expenses[position].amount)
                                            .append("\n"+R.string.category+": "+expenses[position].category);
                                    expenseDeleterDialog.show();
                                }
                            }
                            else {
                                Log.d("Chosen item", String.valueOf(expenses[position]));
                                Intent intent = new Intent(DashboardActivity.this, TransactionActivity.class);
                                intent.putExtra("id", expenses[position].$id);
                                intent.putExtra("amount", expenses[position].amount);
                                intent.putExtra("date", expenses[position].timestamp);
                                intent.putExtra("category", expenses[position].category);
                                startActivity(intent);
                            }
                        }
                    }
            );

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getMenuInflater().inflate(R.menu.piechart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement

        switch(item.getItemId()){
            case R.id.menu_dashboard:  new DashBoardExpenses().execute(); break;
            case R.id.menu_settings: startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));break;
            case R.id.menu_analyze: startActivity(new Intent(DashboardActivity.this, pieChartActivity.class)); break;
           // case R.id.menu_register: startActivity(new Intent(DashboardActivity.this, RegisterUser.class));break;
            default: break;
        }

        return super.onOptionsItemSelected(item);

    }
}
