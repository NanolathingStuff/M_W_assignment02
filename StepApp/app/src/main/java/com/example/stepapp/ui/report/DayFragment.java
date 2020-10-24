package com.example.stepapp.ui.report;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.stepapp.R;
import com.example.stepapp.StepAppOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;


public class DayFragment extends Fragment {

    TextView numStepsTextView;
    AnyChartView anyChartView;

    Date cDate = new Date();
    String current_time = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

    public Map<String, Integer> stepsByDay = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) {
            container.removeAllViews();
        }

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_day, container, false);

        // Create column chart
        anyChartView = root.findViewById(R.id.dailyBarChart);
        anyChartView.setProgressBar(root.findViewById(R.id.loadingBar));

        Cartesian cartesian = createColumnChart();
        anyChartView.setBackgroundColor("#00000000");
        anyChartView.setChart(cartesian);


        // Add the number of steps in text view TODO
        //numStepsTextView = root.findViewById(R.id.numStepsTextView);
        //todaySteps = StepAppOpenHelper.loadSingleRecord(getContext(), current_time);
        //numStepsTextView.setText(String.valueOf(todaySteps));

        return root;
    }
    /**
     * Utility function to create the column chart
     *
     * @return Cartesian: cartesian with column chart and data
     */
    //TODO Assignment
    public Cartesian createColumnChart(){
        //***** Read data from SQLiteDatabase *********/
        // DONE 1: Get the map  number of steps for today
        //  from the database and initialize it to variable stepsByDay
        stepsByDay = StepAppOpenHelper.loadStepsByDay(getContext());

        // TODO 2: Creating a new map that contains the last 5 days?
        //  number of steps during each day set to 0
        Map<String, Integer> graph_map = new TreeMap<>();
        for(int i = 0; i < 5; i++){
            graph_map.put(i, 0);
        }

        // DONE 3: Replace the number of steps for each hour in graph_map
        //  with the number of steps read from the database
        graph_map.putAll(stepsByDay); //see below

        //***** Create column chart using AnyChart library *********/
        // 1. Create and get the cartesian coordinate system for column chart
        Cartesian cartesian = AnyChart.column();

        // 2. Create data entries for x and y axis of the graph
        List<DataEntry> data = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : graph_map.entrySet())
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));

        // 3. Add the data to column chart and get the columns
        Column column = cartesian.column(data);

        //***** Modify the UI of the chart *********/
        // DONE 4. Change the color of column chart and its border
        column.fill("#1EB980");
        column.stroke("#1EB980");

        //Modifying properties of tooltip
        column.tooltip()
                .titleFormat("At hour: {%X}")
                .format("{%Value}{groupsSeparator: } Steps")
                .anchor(Anchor.RIGHT_TOP);

        // DONE 5: Modify column chart tooltip properties
        column.tooltip().position(Position.RIGHT_TOP).offsetX(0d).offsetY(5);

        // Modifying properties of cartesian
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);
        cartesian.yScale().minimum(0);


        // DONE 6: Modify the UI of the cartesian
        cartesian.background().fill("#00000000");
        cartesian.animation(true);
        cartesian.yAxis(0).title("Number of Steps");
        cartesian.xAxis(0).title("Hour");

        return cartesian;
    }
}