package com.example.b.expensewatcher;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.TotalExpensePerCategoryAdapter;
import com.example.b.expensewatcher.models.CategoryPerExpense;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

import com.example.b.expensewatcher.data.ExpenseWatcherPreferences;

/**
 * Created by B on 24-Apr-17.
 */

public class pieChartActivity extends AppCompatActivity
        implements TotalExpensePerCategoryAdapter.Exp_Category_ItemListener, OnChartValueSelectedListener {

    PieChart pieChart ;
    ArrayList<PieEntry> pieEntries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;

    TotalExpensePerCategoryAdapter total_expense_category_adapter;
    RecyclerView.LayoutManager mLayoutManager;

    LineChart lineChart;
    ArrayList<Entry> lineEntries;
    ArrayList<String> lineEntryLabels;

    RecyclerView total_expense_category_list;
    CategoryPerExpense[] catexpenses;

    ExpenseWatcherPreferences data;

    public static String WHICH_ACCOUNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        setTitle(R.string.title_activity_pie_chart);

        ActionBar mActionBar = getSupportActionBar();
        assert mActionBar != null;
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.shapeGreen)));

        total_expense_category_list = (RecyclerView) findViewById(R.id.total_expenses_per_category_list);
        total_expense_category_list.setFocusable(false);// Disable parent focus
        total_expense_category_list.setNestedScrollingEnabled(false); //

        pieChart = (PieChart) findViewById(R.id.chart1);
        pieChart.setOnChartValueSelectedListener(this);

        pieEntries = new ArrayList<>();

        PieEntryLabels = new ArrayList<>();

        createPieChartList(AddValuesToPIEENTRY());

        //pieChart.setBackgroundColor(Color.LTGRAY);
        //pieChart.getDescription().setText("Expenses by Category");

        //TODO: Press and Hold pieChart then provide a dialog appears to save image
         //pieChart.saveToGallery(String.valueOf(new StringBuilder().append("pieChart").append(System.currentTimeMillis()).append(".jpg")),85);
        //pieChart.animateY(0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        createPieChartList(AddValuesToPIEENTRY());
    }

    public boolean AddValuesToPIEENTRY(){

        data = new ExpenseWatcherPreferences();
        WHICH_ACCOUNT = data.getPreferredAccount(pieChartActivity.this);
        final DatabaseHelper db = new DatabaseHelper(pieChartActivity.this);
        catexpenses = db.pieChartCalc(db, db.allExpenses(db, WHICH_ACCOUNT), WHICH_ACCOUNT);

        if(catexpenses.length == 0){
            return false;
        }

        //Donut Chart: Add each total expense per category and their respective titles.
        for(int i = 0; i < catexpenses.length; i++) {
            pieEntries.add(new PieEntry(catexpenses[i].total_expenses_per_category, catexpenses[i].category_title, i));
        }

        return true;
    }

    private void createPieChartList(boolean result) {
        if(result) {
            pieDataSet = new PieDataSet(pieEntries, "");

            pieData = new PieData(pieDataSet);

            pieDataSet.setColors(colortemplate());

            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.setDrawEntryLabels(false);

            Legend legend = pieChart.getLegend();
            int colorcodes[] = getColors(legend);

            //List: Add array of total expense per category, title and color code to Adapter. Set adapter to list.
            total_expense_category_adapter =
                    new TotalExpensePerCategoryAdapter(pieChartActivity.this, catexpenses, colorcodes, this);

            mLayoutManager = new LinearLayoutManager(getApplicationContext());

            //Add the divider line between rows and set the layout manager
            total_expense_category_list.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
            total_expense_category_list.setLayoutManager(mLayoutManager);
            total_expense_category_list.setAdapter(total_expense_category_adapter);

        }
        else
        {
            //TODO: Set picture showing NO DATA
            pieEntries.add(new PieEntry(1f, String.valueOf(R.string.expense_name),1));
            pieChart.getDescription().setText(String.valueOf(R.string.enter_expense_header));
            pieChart.setOnClickListener(new View.OnClickListener(

            ) {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(pieChartActivity.this, TransactionActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if(e == null) {
            pieChart.setCenterText(null);
            return;
        }

        PieEntry pieEntry = (PieEntry) e;

        //Display the percentage of the respective expense category when clicked
        float f =  catexpenses[(int) pieEntry.getData()].share_expenses_per_category;
        f = (float) (Math.ceil(f*100)/100);
        String sharePercent = String.format("%.2f",f*100)+"%";

        pieChart.setCenterText(pieEntry.getLabel()+"\n"+pieEntry.getValue()+"\n"+ sharePercent);
        pieChart.setCenterTextSize(18);

    }

    @Override
    public void onNothingSelected() {

        pieChart.setCenterText(null);
    }

    /*
    * Color template for the pieChart
    * */
    private ArrayList<Integer> colortemplate(){
        final int[] pieColors = {
                Color.rgb(131,139,131),
                Color.rgb(100,149,237),
                Color.rgb(0,206,209),
                Color.rgb(205,92,92),
                Color.rgb(64,224,208),
                Color.rgb(95,158,160),
                Color.rgb(139,137,137),
                Color.rgb(107,142,35),
                Color.rgb(255,192,203),
                Color.rgb(135,206,250),
                Color.rgb(189,183,107),
                Color.rgb(240,230,140),
                Color.rgb(238,233,233),
                Color.rgb(193,205,193),
                Color.rgb(205,201,201),
                Color.rgb(255,140,0),
                Color.rgb(219,112,147),
                Color.rgb(240,255,240),
                Color.rgb(244,238,224),
                Color.rgb(218,112,214),
                Color.rgb(238,221,130),
                Color.rgb(218,165,32),
                Color.rgb(184,134,11),
                Color.rgb(188,143,143),
                Color.rgb(154,205,50),
                Color.rgb(139,69,19),
                Color.rgb(205,133,63),
                Color.rgb(255,165,0),
                Color.rgb(255,127,80),
                Color.rgb(255,20,147),
                Color.rgb(160,32,240)
        };


        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : pieColors) {
            colors.add(c);
        }

        return colors;
    }

    private int [] getColors(Legend legend) {
        LegendEntry [] legendEntries = legend.getEntries();
        int [] colors = new int[legendEntries.length];
        for (int i = 0; i < legendEntries.length; i++) {
            colors[i] = legendEntries[i].formColor;
        }
        return colors;
    }

    private String [] getLabels(Legend legend) {
        LegendEntry[] legendEntries = legend.getEntries();
        String [] labels = new String[legendEntries.length];
        for (int i = 0; i < legendEntries.length; i++) {
            labels[i] = legendEntries[i].label;
        }
        return labels;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home) {
            NavUtils.navigateUpFromSameTask(pieChartActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(CategoryPerExpense item) {

        Intent intent = new Intent(pieChartActivity.this, CategoryMonthlyActivity.class);
        intent.putExtra("category", item.category_title);
        startActivity(intent);
    }

}
