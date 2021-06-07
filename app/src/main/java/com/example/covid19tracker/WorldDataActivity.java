package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

public class WorldDataActivity extends AppCompatActivity
{
    TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_death, tv_death_new, tv_recovered, tv_recovered_new,
        tv_totaltests;

    SwipeRefreshLayout swipeRefreshLayout;

    String str_confirmed, str_confirmed_new, str_active, str_active_new, str_death, str_death_new, str_recovered, str_recovered_new,
        str_totaltests;

    LinearLayout countrylin;

    ProgressDialog progressDialog;

    PieChart pieChart;

    private long active_new;

    private MainActivity mainActivity = new MainActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle("Covid-19 Tracker (World)");



        init();

        fetchworld();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                fetchworld();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(WorldDataActivity.this, "Data Refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

        countrylin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(WorldDataActivity.this, CountrywiseDataActivity.class));
            }
        });

    }

    private void fetchworld()
    {

        mainActivity.ShowDialog(this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://corona.lmao.ninja/v2/all";
        pieChart.clearChart();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    str_confirmed = response.getString("cases");
                    str_confirmed_new = response.getString("todayCases");
                    str_active = response.getString("active");
                    str_recovered = response.getString("recovered");
                    str_recovered_new = response.getString("todayRecovered");
                    str_death = response.getString("deaths");
                    str_death_new = response.getString("todayDeaths");
                    str_totaltests = response.getString("tests");

                    Handler delay = new Handler();

                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            tv_confirmed.setText(NumberFormat.getInstance().format(Long.parseLong(str_confirmed)));
                            tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_confirmed_new)));
                            tv_active.setText(NumberFormat.getInstance().format(Long.parseLong(str_active)));
                            active_new = Long.parseLong(str_confirmed_new) - (Long.parseLong(str_recovered_new)+Long.parseLong(str_death_new));
                            tv_active_new.setText("+"+NumberFormat.getInstance().format(active_new));
                            tv_recovered.setText(NumberFormat.getInstance().format(Long.parseLong(str_recovered)));
                            tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_recovered_new)));
                            tv_death.setText(NumberFormat.getInstance().format(Long.parseLong(str_death)));
                            tv_death_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_death_new)));
                            tv_totaltests.setText(NumberFormat.getInstance().format(Long.parseLong(str_totaltests)));

                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                            pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                            pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(str_death), Color.parseColor("#F6404F")));
                            pieChart.startAnimation();

                            mainActivity.DismissDialog();

                        }
                    },1000);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    private void init()
    {
        tv_confirmed = findViewById(R.id.activity_world_data_confirmed_textView);
        tv_confirmed_new = findViewById(R.id.activity_world_data_confirmed_new_textView);
        tv_active = findViewById(R.id.activity_world_data_active_textView);
        tv_active_new = findViewById(R.id.activity_world_data_active_new_textView);
        tv_death = findViewById(R.id.activity_world_data_death_textView);
        tv_death_new = findViewById(R.id.activity_world_data_death_new_textView);
        tv_recovered = findViewById(R.id.activity_world_data_recovered_textView);
        tv_recovered_new = findViewById(R.id.activity_world_data_recovered_new_textView);
        tv_totaltests = findViewById(R.id.activity_world_data_tests_textView);
        countrylin = findViewById(R.id.activity_world_data_countrywise_lin);
        pieChart = findViewById(R.id.activity_world_data_piechart);
        swipeRefreshLayout = findViewById(R.id.activity_world_data_swipe_refresh_layout);


    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}