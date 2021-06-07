package com.example.covid19tracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.covid19tracker.EachStateDataActivity;
import com.example.covid19tracker.Models.StatewiseModel;
import com.example.covid19tracker.R;

import java.util.ArrayList;

import static com.example.covid19tracker.Constant.*;

public class StatewiseAdapter extends RecyclerView.Adapter<StatewiseAdapter.ViewHolder>
{
    Context mcontext;
    private ArrayList<StatewiseModel> arrayList;

    public StatewiseAdapter(Context mcontext, ArrayList<StatewiseModel> arrayList) {
        this.mcontext = mcontext;
        this.arrayList = arrayList;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.layout_state_wise, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull StatewiseAdapter.ViewHolder holder,final int position)
    {
        StatewiseModel currentItem = arrayList.get(position);
        String statename = currentItem.getState();
        String statecases = currentItem.getConfirmed();
        holder.stateCases.setText(statecases);
        holder.stateName.setText(statename);

        holder.lin_statewise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                StatewiseModel clicked = arrayList.get(position);
                Intent perState = new Intent(mcontext, EachStateDataActivity.class);

                perState.putExtra(STATE_NAME, clicked.getState());
                perState.putExtra(STATE_CONFIRMED,clicked.getConfirmed());
                perState.putExtra(STATE_CONFIRMED_NEW, clicked.getConfirmed_new());
                perState.putExtra(STATE_ACTIVE, clicked.getActive());
                perState.putExtra(STATE_DEATH, clicked.getDeath());
                perState.putExtra(STATE_DEATH_NEW, clicked.getDeath_new());
                perState.putExtra(STATE_RECOVERED, clicked.getRecovered());
                perState.putExtra(STATE_RECOVERED_NEW, clicked.getRecovered_new());
                perState.putExtra(STATE_LAST_UPDATE, clicked.getLastupdated());

                mcontext.startActivity(perState);

            }
        });

    }

    public void filterList(ArrayList <StatewiseModel> filterList)
    {
        arrayList = filterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList==null ? 0 : arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView stateName, stateCases;
        LinearLayout lin_statewise;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            stateName = itemView.findViewById(R.id.statewise_layout_name_textview);
            stateCases = itemView.findViewById(R.id.statewise_layout_confirmed_textview);
            lin_statewise = itemView.findViewById(R.id.layout_state_wise_lin);

        }
    }
}
