package com.example.b.expensewatcher.Utilities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.b.expensewatcher.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;

import java.text.SimpleDateFormat;

/**
 * Created by B on 24-Apr-17.
 */

public class PieChartFrag extends com.example.b.expensewatcher.Utilities.SimpleFragment {

    public static android.app.Fragment newInstance() {
        return new PieChartFrag();
    }

    private PieChart mChart;
    Typeface tf;

    public static void main(String args[]) {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_simple_pie, container, false);

        mChart = (PieChart) v.findViewById(R.id.pieChart1);

        //mChart.getDescription().setEnabled(false);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");


        mChart.setCenterTextTypeface(tf);
        mChart.setCenterText(generateCenterText());
        //mChart.setCenterTextSize(10f);
        mChart.setCenterTextTypeface(tf);

        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);

        mChart.setData(generatePieData());

        return v;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Revenues\nQuarters 2015");
        s.setSpan(new RelativeSizeSpan(2f), 0, 8, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 8, s.length(), 0);
        return s;
    }
}
