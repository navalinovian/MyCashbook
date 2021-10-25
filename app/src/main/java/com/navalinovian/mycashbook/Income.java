package com.navalinovian.mycashbook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Income extends AppCompatActivity {

    EditText date, amount, description;
    final Calendar myCalendar = Calendar.getInstance();
    Button submit, cancel;
    DBHandler dbHandler;
    TextView title;
    String person_id,report_code;
    boolean success=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        date = (EditText) findViewById(R.id.date_picker_income);
        amount = (EditText) findViewById(R.id.amount_editText);
        description = (EditText) findViewById(R.id.description_editText);
        submit = (Button) findViewById(R.id.simpan_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        report_code = getIntent().getStringExtra("report_code");
        title = (TextView) findViewById(R.id.income_and_expense_title);
        person_id = getIntent().getStringExtra("person_id");


        dbHandler = new DBHandler(Income.this);

        if (Integer.parseInt(report_code) == 0){
            title.setText("Tambah Data Pengeluaran");
            title.setTextColor(getResources().getColor(R.color.red));
        }

        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Income.this, date1,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    dbHandler.addReport(Integer.parseInt(report_code),
                            amount.getText().toString().trim(),
                            date.getText().toString().trim(),
                            description.getText().toString().trim(),
                            Integer.parseInt(person_id));
                    success=true;
                }catch (Exception e){
                    Toast.makeText(Income.this, "FAIL", Toast.LENGTH_LONG).show();
                }
                if (success) {
                    Toast.makeText(Income.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Income.this, Home.class).putExtra("person_id", person_id);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    Income.this.finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        date.setText(sdf.format(myCalendar.getTime()));
    }
}