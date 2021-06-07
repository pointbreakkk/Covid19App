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

import static com.example.covid19tracker.Constant.*;

public class EachStateDataActivity extends AppCompatActivity
{
    TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_death, tv_death_new, tv_recovered, tv_recovered_new,
            tv_lastupdate;
    String str_confirmed, str_confirmed_new, str_active, str_active_new, str_death, str_death_new, str_recovered, str_recovered_new,
            str_lastUpdated, str_name;
    PieChart pieChart;
    private MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_state_data);

        GetIntent();

        getSupportActionBar().setTitle(str_name);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Init();

        LoadData();

    }

    private void LoadData()
    {
        mainActivity.ShowDialog(this);

        Handler delay = new Handler();

        delay.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                tv_confirmed.setText(NumberFormat.getInstance().format(Long.parseLong(str_confirmed)));
                tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_confirmed_new)));

                tv_active.setText(NumberFormat.getInstance().format(Long.parseLong(str_active)));
                long active_new = Long.parseLong(str_confirmed_new) - (Long.parseLong(str_recovered_new)+ Long.parseLong(str_death_new));
                tv_active_new.setText("+"+NumberFormat.getInstance().format(active_new));

                tv_death.setText(NumberFormat.getInstance().format(Long.parseLong(str_death)));
                tv_death_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_death_new)));

                tv_recovered.setText(NumberFormat.getInstance().format(Long.parseLong(str_recovered)));
                tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_recovered_new)));

                String date = mainActivity.FormatDate(str_lastUpdated,0);
                tv_lastupdate.setText(date);

                pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(str_death), Color.parseColor("#F6404F")));
                pieChart.startAnimation();

                mainActivity.DismissDialog();

            }
        }, 1000);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void Init()
    {
        tv_confirmed = findViewById(R.id.activity_each_state_confirmed_textView);
        tv_confirmed_new = findViewById(R.id.activity_each_state_confirmed_new_textView);
        tv_active = findViewById(R.id.activity_each_state_active_textView);
        tv_active_new = findViewById(R.id.activity_each_state_active_new_textView);
        tv_death = findViewById(R.id.activity_each_state_death_textView);
        tv_death_new = findViewById(R.id.activity_each_state_death_new_textView);
        tv_recovered = findViewById(R.id.activity_each_state_recovered_textView);
        tv_recovered_new = findViewById(R.id.activity_each_state_recovered_new_textView);
        tv_lastupdate = findViewById(R.id.activity_each_state_lastupdate_textView);
        pieChart = findViewById(R.id.activity_each_state_piechart);

    }

    private void GetIntent()
    {
        Intent i = getIntent();
        str_name = i.getStringExtra(STATE_NAME);
        str_confirmed = i.getStringExtra(STATE_CONFIRMED);
        str_confirmed_new = i.getStringExtra(STATE_CONFIRMED_NEW);
        str_active = i.getStringExtra(STATE_ACTIVE);
        str_death = i.getStringExtra(STATE_DEATH);
        str_death_new = i.getStringExtra(STATE_DEATH_NEW);
        str_recovered = i.getStringExtra(STATE_RECOVERED);
        str_recovered_new = i.getStringExtra(STATE_RECOVERED_NEW);
        str_lastUpdated = i.getStringExtra(STATE_LAST_UPDATE);

    }
}