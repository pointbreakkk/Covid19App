package com.example.covid19tracker.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.covid19tracker.EachCountryDataActivity;
import com.example.covid19tracker.Models.CountrywiseModel;
import com.example.covid19tracker.Models.StatewiseModel;
import com.example.covid19tracker.R;

import java.text.NumberFormat;
import java.util.ArrayList;

import static com.example.covid19tracker.Constant.*;

public class CountrywiseAdapter extends RecyclerView.Adapter<CountrywiseAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList <CountrywiseModel> arrayList;
    private String searchTexts="";
    private SpannableStringBuilder spannableStringBuilder;

    public CountrywiseAdapter(Context context, ArrayList<CountrywiseModel> arrayList)
    {
        this.context = context;
        this.arrayList = arrayList;
    }



    @NonNull

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_countrywise, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountrywiseAdapter.MyViewHolder holder, final int position)
    {
        CountrywiseModel currentItem = arrayList.get(position);
        String name = currentItem.getCountry();
        String countrycases = currentItem.getConfirmed();
        String flag = currentItem.getFlag();
        String rank = String.valueOf(position+1);
        Long countrycasesl = Long.parseLong(countrycases);

        holder.tv_country.setText(name);
        holder.tv_rank.setText(rank);
        holder.tv_cases.setText(NumberFormat.getInstance().format(countrycasesl));

        Glide.with(context).load(flag).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.iv_flag);

        holder.lin_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CountrywiseModel clicked = arrayList.get(position);
                Intent perCountry = new Intent (context, EachCountryDataActivity.class);

                perCountry.putExtra(COUNTRY_NAME, clicked.getCountry());
                perCountry.putExtra(COUNTRY_CONFIRMED, clicked.getConfirmed());
                perCountry.putExtra(COUNTRY_NEW_CONFIRMED, clicked.getConfirmed_new());
                perCountry.putExtra(COUNTRY_ACTIVE, clicked.getActive());
                perCountry.putExtra(COUNTRY_RECOVERED, clicked.getRecovered());
                perCountry.putExtra(COUNTRY_RECOVERED_NEW, clicked.getRecovered_new());
                perCountry.putExtra(COUNTRY_DECEASED, clicked.getDeath());
                perCountry.putExtra(COUNTRY_NEW_DECEASED, clicked.getDeath_new());
                perCountry.putExtra(COUNTRY_TESTS, clicked.getTests());
                perCountry.putExtra(COUNTRY_FLAGURL, clicked.getFlag());


                context.startActivity(perCountry);


            }
        });
    }

    public void filterList(ArrayList<CountrywiseModel> filterList)
    {
        arrayList = filterList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount()
    {
        return arrayList==null || arrayList.isEmpty() ? 0 : arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_country, tv_rank, tv_cases;
        private ImageView iv_flag;
        LinearLayout lin_country;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_country = itemView.findViewById(R.id.layout_country_wise_country_name_textview);
            tv_rank = itemView.findViewById(R.id.layout_country_wise_country_rank);
            tv_cases = itemView.findViewById(R.id.layout_country_wise_confirmed_textview);
            iv_flag = itemView.findViewById(R.id.layout_country_wise_flag_imageview);
            lin_country = itemView.findViewById(R.id.layout_country_wise_lin);
        }
    }
}
