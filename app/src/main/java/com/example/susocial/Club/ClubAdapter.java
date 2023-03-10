package com.example.susocial.Club;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.susocial.ClubDetail;
import com.example.susocial.R;

import org.checkerframework.checker.units.qual.C;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> implements Filterable {

    private List<ClubModel> clublist;
    private List<ClubModel> clublistfull;

    public ClubAdapter(List<ClubModel>clublist){
        this.clublist=clublist;
        clublistfull = new ArrayList<>(clublist);
    }

    @NonNull
    @Override
    public ClubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clubinfo_design,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapter.ViewHolder holder, int position) {
        ClubModel currentclub = clublist.get(position);
        holder.cardView1.setBackgroundResource(currentclub.getImageview1());
        holder.textView1.setText(currentclub.getTextview1());
        holder.textView2.setText(currentclub.getTextview2());
        holder.textView3.setText(currentclub.getTextview3());

    }

    @Override
    public int getItemCount() {
        return clublist.size();
    }

    @Override
    public Filter getFilter() {
        return clubfilter;
    }
    private Filter clubfilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ClubModel> filterlist = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filterlist.addAll(clublistfull);
            }
            else {
                String filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim();

                for (ClubModel club: clublistfull){
                    if(club.getTextview1().toLowerCase().contains(filterPattern)){
                       filterlist.add(club);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterlist;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clublist.clear();
            clublist.addAll((List)results.values);
            notifyDataSetChanged();;
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CardView cardView1;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        Context context;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView1 = itemView.findViewById(R.id.club_pic);
            textView1 = itemView.findViewById(R.id.club_name);
            textView2 = itemView.findViewById(R.id.club_introduction);
            textView3 = itemView.findViewById(R.id.club_rate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context mcontext = v.getContext();
            Intent intent =new Intent(mcontext, ClubDetail.class);
            mcontext.startActivity(intent);
        }
    }
}
