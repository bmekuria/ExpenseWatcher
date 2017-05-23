package com.example.b.expensewatcher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText first_passcode;
    Toast mToast;
    int inactive_counter = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense Tracking Suite");
        //when six digits have been entered
        first_passcode = (EditText) findViewById(R.id.first_et_password);
        first_passcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(mToast != null) {
                    mToast.cancel();
                }

                if(first_passcode.getText().toString().equals("123456"))
                {
                    //correct password
                    mToast = Toast.makeText(getApplicationContext(), "Correct Passcode",Toast.LENGTH_SHORT);
                    mToast.show();
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));

                }
                else
                {
                    //false password
                    mToast = Toast.makeText(getApplicationContext(), "Wrong Passcode",Toast.LENGTH_SHORT);
                    mToast.show();

                }
            }
        });

    }

}
