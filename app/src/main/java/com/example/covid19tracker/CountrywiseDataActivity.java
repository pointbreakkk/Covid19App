package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.Adapters.CountrywiseAdapter;
import com.example.covid19tracker.Models.CountrywiseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CountrywiseDataActivity extends AppCompatActivity
{
    private RecyclerView recyclerView_countrywise;
    private CountrywiseAdapter countrywiseAdapter;
    private ArrayList<CountrywiseModel> countrywiseModelArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText editText_search;
    private String country, confirmed, confirmed_new, active, death, death_new, recovered, tests, recovered_new;
    private MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countrywise_data);

        getSupportActionBar().setTitle("Select Country");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        fetchcountrydata();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchcountrydata();
                Toast.makeText(CountrywiseDataActivity.this, "Data Refreshed!", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Filter(s.toString());
            }
        });
    }

    private void Filter(String text)
    {
        ArrayList <CountrywiseModel> filterList = new ArrayList<>();
        for(CountrywiseModel item: countrywiseModelArrayList)
        {
            if(item.getCountry().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(item);
            }
        }

        countrywiseAdapter.filterList(filterList);
    }


    private void fetchcountrydata()
    {
        mainActivity.ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl = "https://corona.lmao.ninja/v2/countries";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    countrywiseModelArrayList.clear();
                    for(int i=0; i<response.length(); i++)
                    {
                        JSONObject countryJSONobject = response.getJSONObject(i);

                        country = countryJSONobject.getString("country");
                        confirmed = countryJSONobject.getString("cases");
                        confirmed_new = countryJSONobject.getString("todayCases");
                        active = countryJSONobject.getString("active");
                        recovered = countryJSONobject.getString("recovered");
                        recovered_new = countryJSONobject.getString("todayRecovered");
                        death = countryJSONobject.getString("deaths");
                        death_new = countryJSONobject.getString("todayDeaths");
                        tests = countryJSONobject.getString("tests");

                        JSONObject flag = countryJSONobject.getJSONObject("countryInfo");
                        String flagUrl = flag.getString("flag");

                        CountrywiseModel countrywiseModel = new CountrywiseModel(country, confirmed, confirmed_new, active, death, death_new,
                                recovered, tests, flagUrl, recovered_new);

                        countrywiseModelArrayList.add(countrywiseModel);

                    }

                    Collections.sort(countrywiseModelArrayList, new Comparator<CountrywiseModel>() {
                        @Override
                        public int compare(CountrywiseModel o1, CountrywiseModel o2)
                        {
                            if(Long.parseLong(o1.getConfirmed())>Long.parseLong(o2.getConfirmed()))
                            {
                                return -1;
                            }
                            else
                                return 1;
                        }
                    });

                    Handler delay = new Handler();
                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            countrywiseAdapter.notifyDataSetChanged();
                            mainActivity.DismissDialog();
                        }
                    },1000);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void init()
    {
        swipeRefreshLayout = findViewById(R.id.activity_country_wise_swipe_refresh_layout);
        editText_search = findViewById(R.id.activity_country_wise_search_editText);

        recyclerView_countrywise = findViewById(R.id.activity_country_wise_recyclerview);
        recyclerView_countrywise.setHasFixedSize(true);
        recyclerView_countrywise.setLayoutManager(new LinearLayoutManager(this));

        countrywiseModelArrayList = new ArrayList<>();
        countrywiseAdapter = new CountrywiseAdapter(CountrywiseDataActivity.this,countrywiseModelArrayList);
        recyclerView_countrywise.setAdapter(countrywiseAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}