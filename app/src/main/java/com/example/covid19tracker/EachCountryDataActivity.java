package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.TextView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.NumberFormat;

import static com.example.covid19tracker.Constant.COUNTRY_ACTIVE;
import static com.example.covid19tracker.Constant.COUNTRY_CONFIRMED;
import static com.example.covid19tracker.Constant.COUNTRY_DECEASED;
import static com.example.covid19tracker.Constant.COUNTRY_NAME;
import static com.example.covid19tracker.Constant.COUNTRY_NEW_CONFIRMED;
import static com.example.covid19tracker.Constant.COUNTRY_NEW_DECEASED;
import static com.example.covid19tracker.Constant.COUNTRY_RECOVERED;
import static com.example.covid19tracker.Constant.COUNTRY_RECOVERED_NEW;
import static com.example.covid19tracker.Constant.COUNTRY_TESTS;
import static com.example.covid19tracker.Constant.STATE_ACTIVE;
import static com.example.covid19tracker.Constant.STATE_CONFIRMED;
import static com.example.covid19tracker.Constant.STATE_CONFIRMED_NEW;
import static com.example.covid19tracker.Constant.STATE_DEATH;
import static com.example.covid19tracker.Constant.STATE_DEATH_NEW;
import static com.example.covid19tracker.Constant.STATE_LAST_UPDATE;
import static com.example.covid19tracker.Constant.STATE_NAME;
import static com.example.covid19tracker.Constant.STATE_RECOVERED;
import static com.example.covid19tracker.Constant.STATE_RECOVERED_NEW;

public class EachCountryDataActivity extends AppCompatActivity
{

    TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_death, tv_death_new, tv_recovered, tv_recovered_new,
            tv_tests;

    String str_confirmed, str_confirmed_new, str_active, str_active_new, str_death, str_death_new, str_recovered, str_recovered_new,
            str_tests, str_name;

    PieChart pieChart;
    private MainActivity mainActivity = new MainActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_country_data);

        GetIntent();

        getSupportActionBar().setTitle(str_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        loadData();


    }

    private void loadData()
    {
        mainActivity.ShowDialog(this);
        Handler delay = new Handler();

        delay.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                tv_confirmed.setText(NumberFormat.getInstance().format(Long.parseLong(str_confirmed)));
                tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_confirmed_new)));

                tv_recovered.setText(NumberFormat.getInstance().format(Long.parseLong(str_recovered)));
                tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_recovered_new)));

                tv_death.setText(NumberFormat.getInstance().format(Long.parseLong(str_death)));
                tv_death_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_death_new)));

                tv_active.setText(NumberFormat.getInstance().format(Long.parseLong(str_active)));
                long active_new = Long.parseLong(str_confirmed_new) - (Long.parseLong(str_recovered_new)+ Long.parseLong(str_death_new));
                tv_active_new.setText("+"+NumberFormat.getInstance().format(active_new));

                tv_tests.setText(NumberFormat.getInstance().format(Long.parseLong(str_tests)));

                pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(str_death), Color.parseColor("#F6404F")));
                pieChart.startAnimation();

                mainActivity.DismissDialog();

            }
        }, 1000);

    }

    private void init()
    {
        tv_confirmed = findViewById(R.id.activity_each_country_data_confirmed_textView);
        tv_confirmed_new = findViewById(R.id.activity_each_country_data_confirmed_new_textView);
        tv_active = findViewById(R.id.activity_each_country_data_active_textView);
        tv_active_new = findViewById(R.id.activity_each_country_data_active_new_textView);
        tv_death = findViewById(R.id.activity_each_country_data_death_textView);
        tv_death_new = findViewById(R.id.activity_each_country_data_death_new_textView);
        tv_recovered = findViewById(R.id.activity_each_country_data_recovered_textView);
        tv_recovered_new = findViewById(R.id.activity_each_country_data_recovered_new_textView);
        tv_tests = findViewById(R.id.activity_each_country_data_tests_textView);
        pieChart = findViewById(R.id.activity_each_country_data_piechart);

    }

    private void GetIntent()
    {
        Intent i = getIntent();
        str_name = i.getStringExtra(COUNTRY_NAME);
        str_confirmed = i.getStringExtra(COUNTRY_CONFIRMED);
        str_confirmed_new = i.getStringExtra(COUNTRY_NEW_CONFIRMED);
        str_active = i.getStringExtra(COUNTRY_ACTIVE);
        str_death = i.getStringExtra(COUNTRY_DECEASED);
        str_death_new = i.getStringExtra(COUNTRY_NEW_DECEASED);
        str_recovered = i.getStringExtra(COUNTRY_RECOVERED);
        str_recovered_new = i.getStringExtra(COUNTRY_RECOVERED_NEW);
        str_tests = i.getStringExtra(COUNTRY_TESTS);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}