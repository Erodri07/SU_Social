package com.example.susocial.Club;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.susocial.ClubDetail;
import com.example.susocial.R;

import java.util.List;

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {

    private List<ClubModel> clublist;

    public ClubAdapter(List<ClubModel>clublist){this.clublist=clublist;}

    @NonNull
    @Override
    public ClubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clubinfo_design,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ClubAdapter.ViewHolder holder, int position) {
        int resource = clublist.get(position).getImageview1();
        String name = clublist.get(position).getTextview1();
        String introduction = clublist.get(position).getTextview2();
        String rate = clublist.get(position).getTextview3();

        holder.setData(resource,name,introduction,rate);
    }

    @Override
    public int getItemCount() {
        return clublist.size();
    }

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

        public void setData(int resource, String name, String introduction, String rate) {
            cardView1.setBackgroundResource(resource);
            textView1.setText(name);
            textView2.setText(introduction);
            textView3.setText(rate);
        }

        @Override
        public void onClick(View v) {
            Context mcontext = v.getContext();
            Intent intent =new Intent(mcontext, ClubDetail.class);
            mcontext.startActivity(intent);
        }
    }
}
