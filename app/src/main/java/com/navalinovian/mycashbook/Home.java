package com.navalinovian.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Home extends AppCompatActivity {

    GraphView graphView;
    ImageButton incomeBtn, detailBtn, expenseBtn, settingsBtn;
    String person_id;
    TextView incomeTotal, expenseTotal;
    DBHandler dbHandler;
    String incomeFormat, expenseFormat, stringDate;
    private ArrayList<Cashflow> cashflowArrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        graphView   = findViewById(R.id.graph);
        incomeBtn   = findViewById(R.id.income_button);
        detailBtn   = findViewById(R.id.detail_button);
        expenseBtn  = findViewById(R.id.expense_button);
        settingsBtn = findViewById(R.id.settings_button);
        incomeTotal = findViewById(R.id.totalPemasukan_value);
        expenseTotal = findViewById(R.id.totalPengeluaran_value);
        person_id   = getIntent().getStringExtra("person_id");

        dbHandler = new DBHandler(Home.this);
        cashflowArrayList = dbHandler.getAllCashflow(Integer.parseInt(person_id));

        graphView.getGridLabelRenderer().setLabelFormatter(
                new DateAsXAxisLabelFormatter(Home.this, new SimpleDateFormat("dd MM yy", Locale.ENGLISH)));

        try {
            if (cashflowArrayList.size() > 0) {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                for (int i = 0; i < cashflowArrayList.size(); i++) {
                    stringDate = cashflowArrayList.get(i).getmDate();
                    if (cashflowArrayList.get(i).getmReport_code()==1){
                        try {
                            Date date = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH).parse(stringDate);
                            DataPoint point = new DataPoint(date, Float.parseFloat(cashflowArrayList.get(i).getmAmount()));
                            series.appendData(point, true, cashflowArrayList.size());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                graphView.addSeries(series);

                LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();
                for (int i = 0; i < cashflowArrayList.size(); i++) {
                    stringDate = cashflowArrayList.get(i).getmDate();
                    if (cashflowArrayList.get(i).getmReport_code()==0){
                        try {
                            Date date = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH).parse(stringDate);
                            DataPoint point = new DataPoint(date, Float.parseFloat(cashflowArrayList.get(i).getmAmount()));
                            series2.appendData(point, true, cashflowArrayList.size());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                series.setColor(R.color.green);
                series2.setColor(R.color.red);
                graphView.addSeries(series2);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);

        try {
            String incomeResult = dbHandler.getSumAmount(Integer.parseInt(person_id) , 1);
            incomeFormat = decimalFormat.format(Integer.parseInt(incomeResult));
            incomeTotal.setText("Rp. "+ incomeFormat);
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            String expenseResult = dbHandler.getSumAmount(Integer.parseInt(person_id), 0);
            expenseFormat = decimalFormat.format(Integer.parseInt(expenseResult));
            expenseTotal.setText("Rp. "+expenseFormat);
        }catch (Exception e){
            e.printStackTrace();
        }

        incomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Income.class)
                        .putExtra("report_code",1+"").putExtra("person_id", person_id));
            }
        });
        detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, DetailCashflow.class)
                        .putExtra("person_id", person_id));
            }
        });

        expenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Income.class)
                        .putExtra("report_code", 0+"").putExtra("person_id", person_id));
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Settings.class)
                        .putExtra("person_id", person_id));
            }
        });

    }
}