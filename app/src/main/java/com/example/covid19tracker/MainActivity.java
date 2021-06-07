package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private TextView tv_confirmed, tv_confirmed_new, tv_active, tv_active_new, tv_death, tv_death_new, tv_sample, tv_sample_new,
            tv_recovered, tv_recovered_new, tv_date, tv_time, currtime, tv_vaccinated, tv_vaccinated_new, crealtime;
    private LinearLayout lin_state, lin_world;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PieChart pieChart;
    private String str_confirmed, str_confirmed_new, str_active, str_active_new, str_tested, str_tested_new, str_death, str_death_new,
                str_last_update_time, str_recovered, str_recovered_new, formattedDate, str_vaccinated, str_vaccinated_kalke;
    private long int_active_new, vaccinated_new;
    private ProgressDialog progressDialog;
    private boolean doubleback = false;
    private Toast backToast;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Covid-19 Tracker (India)");

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate = df.format(c.getTime());
        currtime = findViewById(R.id.activity_main_currtime_new_textview);
        currtime.setText(formattedDate);
        crealtime = findViewById(R.id.activity_main_currtime_textview);
        SimpleDateFormat t = new SimpleDateFormat("hh:mm a");
        String xtime = t.format(c.getTime());
        crealtime.setText(xtime);


        init();
        fetchData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                fetchData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Data Refreshed!", Toast.LENGTH_SHORT).show();
            }
        });

        lin_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, StatewiseDataActivity.class ));
            }
        });

        lin_world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this, WorldDataActivity.class);
                startActivity(i);
            }
        });

    }

    private void fetchData()
    {

        ShowDialog(this);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://api.covid19india.org/data.json";
        pieChart.clearChart();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray all_state_array = null;
                JSONArray testdata_array = null;

                try {
                    all_state_array = response.getJSONArray("statewise");
                    testdata_array = response.getJSONArray("tested");
                    JSONObject test_data_india = testdata_array.getJSONObject(testdata_array.length()-1);
                    JSONObject data_india = all_state_array.getJSONObject(0);
                    str_confirmed = data_india.getString("confirmed");
                    str_confirmed_new = data_india.getString("deltaconfirmed");
                    str_active = data_india.getString("active");
                    str_recovered = data_india.getString("recovered");
                    str_recovered_new = data_india.getString("deltarecovered");
                    str_death = data_india.getString("deaths");
                    str_death_new = data_india.getString("deltadeaths");
                    str_last_update_time = data_india.getString("lastupdatedtime");
                    str_tested = test_data_india.getString("totalsamplestested");
                    str_tested_new = test_data_india.getString("samplereportedtoday");
                    str_vaccinated = test_data_india.getString("totalindividualsvaccinated");
                    str_vaccinated_kalke = testdata_array.getJSONObject(testdata_array.length()-2).getString("totalindividualsvaccinated");

                    Handler delay = new Handler();

                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tv_confirmed.setText(NumberFormat.getInstance().format(Long.parseLong(str_confirmed)));
                            tv_confirmed_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_confirmed_new)));

                            tv_active.setText(NumberFormat.getInstance().format(Long.parseLong(str_active)));
                            int_active_new = Long.parseLong(str_confirmed_new) - (Long.parseLong(str_recovered_new)+Long.parseLong(str_death_new));
                            tv_active_new.setText("+"+NumberFormat.getInstance().format(int_active_new));

                            tv_recovered.setText(NumberFormat.getInstance().format(Long.parseLong(str_recovered)));
                            tv_recovered_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_recovered_new)));

                            tv_death.setText(NumberFormat.getInstance().format(Long.parseLong(str_death)));
                            tv_death_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_death_new)));

                            tv_sample.setText(NumberFormat.getInstance().format(Long.parseLong(str_tested)));
                            tv_sample_new.setText("+"+NumberFormat.getInstance().format(Long.parseLong(str_tested_new)));

                            tv_vaccinated.setText(NumberFormat.getInstance().format(Long.parseLong(str_vaccinated)));
                            vaccinated_new = Long.parseLong(str_vaccinated) - Long.parseLong(str_vaccinated_kalke);
                            tv_vaccinated_new.setText("+"+NumberFormat.getInstance().format(vaccinated_new));

                            tv_date.setText(FormatDate(str_last_update_time,1));
                            tv_time.setText(FormatDate(str_last_update_time,2));

                            pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(str_active), Color.parseColor("#007afe")));
                            pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(str_recovered), Color.parseColor("#08a045")));
                            pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(str_death), Color.parseColor("#F6404F")));
                            pieChart.startAnimation();

                            DismissDialog();

                        }
                    },1000);



                } catch (JSONException e) {
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

    public void ShowDialog(Context context)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void DismissDialog()
    {
        progressDialog.dismiss();
    }

    public String FormatDate(String date, int test_case)
    {
        Date mDate = null;
        String dateformat;
        try
        {
            mDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).parse(date);
            if(test_case==1)
            {
                dateformat = new SimpleDateFormat("dd-MM-yyyy").format(mDate);
                return dateformat;
            }
            else if(test_case==2)
            {
                dateformat = new SimpleDateFormat("hh:mm a").format(mDate);
                return dateformat;
            }
            else if (test_case == 0) {
                dateformat = new SimpleDateFormat("dd MMM yyyy, hh:mm a").format(mDate);
                return dateformat;
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
            return date;
        }
        return null;
    }

    private void init()
    {
        tv_confirmed = findViewById(R.id.activity_main_confirmed_textview);
        tv_confirmed_new = findViewById(R.id.activity_main_confirmed_new_textview);
        tv_active = findViewById(R.id.activity_main_active_textview);
        tv_active_new = findViewById(R.id.activity_main_active_new_textview);
        tv_death = findViewById(R.id.activity_main_death_textview);
        tv_death_new = findViewById(R.id.activity_main_death_new_textview);
        tv_sample = findViewById(R.id.activity_main_sample_textview);
        tv_sample_new = findViewById(R.id.activity_main_sample_new_textview);
        tv_recovered = findViewById(R.id.activity_main_recovered_textview);
        tv_recovered_new = findViewById(R.id.activity_main_recovered_new_textview);
        tv_vaccinated = findViewById(R.id.activity_main_vaccinated_textview);
        tv_vaccinated_new = findViewById(R.id.activity_main_vaccinated_new_textview);
        tv_date = findViewById(R.id.activity_main_date_textview);
        tv_time = findViewById(R.id.activity_main_time_textview);
        pieChart = findViewById(R.id.activity_main_piechart);
        swipeRefreshLayout = findViewById(R.id.activity_main_swipe_refresh_layout);
        lin_state = findViewById(R.id.activity_main_statewise_lin);
        lin_world = findViewById(R.id.activity_main_world_lin);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_about)
        {
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if(doubleback)
        {
            backToast.cancel();
            super.onBackPressed();
            return;
        }
        doubleback = true;
        backToast = Toast.makeText(MainActivity.this, "Press again to exit app", Toast.LENGTH_SHORT);
        backToast.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                doubleback = false;
            }
        }, 2000);
    }
}