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
import android.widget.Filter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.Adapters.StatewiseAdapter;
import com.example.covid19tracker.Models.CountrywiseModel;
import com.example.covid19tracker.Models.StatewiseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StatewiseDataActivity extends AppCompatActivity
{

    private RecyclerView recyclerView_statewise;
    private StatewiseAdapter statewiseAdapter;
    private ArrayList <StatewiseModel> statewiseModelArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText editText_search;
    private String state, confirmed, confirmed_new, active, active_new, death, death_new, recovered, recovered_new, lastupdated;
    private MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statewise_data);

        getSupportActionBar().setTitle("Select State");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        init();

        fetchstatewise();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchstatewise();
                Toast.makeText(StatewiseDataActivity.this, "Data Refreshed!", Toast.LENGTH_SHORT).show();
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

    public void Filter(String text)
    {
        ArrayList <StatewiseModel> filterList = new ArrayList<>();
        for(StatewiseModel item: statewiseModelArrayList)
        {
            if(item.getState().toLowerCase().contains(text.toLowerCase()))
            {
                filterList.add(item);
            }
        }
        statewiseAdapter.filterList(filterList);
    }

    private void fetchstatewise()
    {
        mainActivity.ShowDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String apiUrl ="https://api.covid19india.org/data.json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try {
                    JSONArray jsonArray = response.getJSONArray("statewise");
                    statewiseModelArrayList.clear();

                    for(int i=1; i<jsonArray.length();i++)
                    {
                        JSONObject states = jsonArray.getJSONObject(i);

                        if(!states.getString("state").equals("State Unassigned"))
                        {

                            state = states.getString("state");

                            confirmed = states.getString("confirmed");
                            confirmed_new = states.getString("deltaconfirmed");

                            active = states.getString("active");

                            death = states.getString("deaths");
                            death_new = states.getString("deltadeaths");

                            recovered = states.getString("recovered");
                            recovered_new = states.getString("deltarecovered");

                            lastupdated = states.getString("lastupdatedtime");

                            StatewiseModel statewiseModel = new StatewiseModel(state, confirmed, confirmed_new, active, death,
                                    death_new, recovered, recovered_new, lastupdated);

                            statewiseModelArrayList.add(statewiseModel);
                        }

                        Collections.sort(statewiseModelArrayList, new Comparator<StatewiseModel>() {
                            @Override
                            public int compare(StatewiseModel o1, StatewiseModel o2)
                            {
                                if(Long.parseLong(o1.getConfirmed())>Long.parseLong(o2.getConfirmed()))
                                {
                                    return -1;
                                }
                                else
                                    return 1;
                            }
                        });
                    }

                    Handler delay = new Handler();
                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run()
                        {
                            statewiseAdapter.notifyDataSetChanged();
                            mainActivity.DismissDialog();
                        }
                    }, 1000);


                }

                catch (JSONException e) {
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
        swipeRefreshLayout = findViewById(R.id.activity_state_wise_swipe_refresh_layout);
        editText_search = findViewById(R.id.activity_state_wise_search_editText);

        recyclerView_statewise = findViewById(R.id.activity_state_wise_recyclerview);
        recyclerView_statewise.setHasFixedSize(true);
        recyclerView_statewise.setLayoutManager(new LinearLayoutManager(this));

        statewiseModelArrayList = new ArrayList<>();
        statewiseAdapter = new StatewiseAdapter(StatewiseDataActivity.this,statewiseModelArrayList);
        recyclerView_statewise.setAdapter(statewiseAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}