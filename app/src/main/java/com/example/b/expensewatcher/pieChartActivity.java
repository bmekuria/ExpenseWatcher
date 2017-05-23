package com.example.b.expensewatcher;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.b.expensewatcher.Utilities.DatabaseHelper;
import com.example.b.expensewatcher.Utilities.SwipeDetector;
import com.example.b.expensewatcher.Utilities.TotalExpensePerCategoryAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by B on 24-Apr-17.
 */

public class pieChartActivity extends AppCompatActivity{

    PieChart pieChart ;
    ArrayList<PieEntry> pieEntries;
    ArrayList<String> PieEntryLabels;
    PieDataSet pieDataSet;
    PieData pieData;

    LineChart lineChart;
    ArrayList<Entry> lineEntries;
    ArrayList<String> lineEntryLabels;

    ListView total_expense_category_list;
    TotalExpensePerCategoryAdapter total_expense_category_adapter;
    String categorytoexpand;
    SwipeDetector swipeDetector = new SwipeDetector();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);

        total_expense_category_list = (ListView) findViewById(R.id.total_expenses_per_category_list);


        pieChart = (PieChart) findViewById(R.id.chart1);

        pieEntries = new ArrayList<>();

        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();

        pieDataSet = new PieDataSet(pieEntries, "");

        pieData = new PieData(pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        //pieChart.setBackgroundColor(Color.LTGRAY);
        //pieChart.getDescription().setText("Expenses by Category");

        //TODO: Press and Hold pieChart then provide a dialog appears to save image
       // pieChart.saveToGallery(String.valueOf(new StringBuilder().append("pieChart").append(System.currentTimeMillis()).append(".jpg")),85);

        //pieChart.animateY(0);

        //TODO: When click on category, display the percentage of total category in the center

        //Line Chart
        lineChart = (LineChart) findViewById (R.id.chart2);

        //Creating a list of entries

/*      lineEntries = new ArrayList<>();
        AddValuesToLINEENTRY();

        LineDataSet dataset = new LineDataSet(lineEntries,"");

        //Creating labels
        //TODO: Add past six months
        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        //Set the data
        LineData data = new LineData(dataset);
        lineChart.setData(data);
        lineChart.getDescription().setEnabled(false);
        //lineChart.getDescription().setText("Expenses by Month");

        //TODO: Press and Hold lineChart then provide a dialog to save image
        //lineChart.saveToGallery(String.valueOf(new StringBuilder().append("lineChart").append(System.currentTimeMillis()).append(".jpg")),85);
*/

    }



    public void AddValuesToPIEENTRY(){

        DatabaseHelper db = new DatabaseHelper(pieChartActivity.this);
        final String[][] catexpenses = db.pieChartCalc(db,db.allExpenses(db));

        //TODO: Only when interested in posting the top expenses
        //int numTopExp = 5;
        //numTopExp = catexpenses.length > numTopExp ? numTopExp : catexpenses.length;

        total_expense_category_adapter = new TotalExpensePerCategoryAdapter(pieChartActivity.this, catexpenses);
        total_expense_category_list.setAdapter(total_expense_category_adapter);

        for(int i = 0; i < catexpenses.length; i++) {
            pieEntries.add(new PieEntry(Float.valueOf(catexpenses[i][1]),catexpenses[i][0],i));

        }

        total_expense_category_list.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Log.d("Chosen Category",catexpenses[position][0]);
                        //Create intent and pass category
                        //Started Activity should display all transactions under given category
                    }
                }
        );

    }


    public void AddValuesToLINEENTRY() {

        DatabaseHelper db = new DatabaseHelper(pieChartActivity.this);
        String[][] totalexpensesbymonth = db.lineChartCalc(db,db.allExpenses(db));

        //TODO: Only interested in posting the aggregate for the past year
        //int pastyear = 12;
        //pastyear = totalexpensesbymonth.length > pastyear ? pastyear : totalexpensesbymonth.length;

        for(int i = 0; i < totalexpensesbymonth.length; i++) {
            lineEntries.add(new Entry(Float.valueOf(totalexpensesbymonth[i][1]),i));
            lineEntryLabels.add(totalexpensesbymonth[i][0]);
        }

        //TODO: Definitely a problem in matching entries to labels
        Collections.sort(lineEntries, new EntryXComparator());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.home) {
            NavUtils.navigateUpFromSameTask(pieChartActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }
}
