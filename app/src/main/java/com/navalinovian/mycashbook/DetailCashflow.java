package com.navalinovian.mycashbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class DetailCashflow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CashflowAdapter cashflowAdapter;
    private ArrayList<Cashflow> cashflowArrayList ;
    private DBHandler dbHandler;
    ImageButton backbtn;
    String person_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cashflow);

        dbHandler = new DBHandler(DetailCashflow.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        person_id = getIntent().getStringExtra("person_id");
        cashflowArrayList = dbHandler.getAllCashflow(Integer.parseInt(person_id));
        cashflowAdapter = new CashflowAdapter(cashflowArrayList);
        backbtn = (ImageButton) findViewById(R.id.back_button);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailCashflow.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cashflowAdapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



}