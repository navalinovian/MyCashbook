package com.navalinovian.mycashbook;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CashflowAdapter extends RecyclerView.Adapter<CashflowAdapter.CashflowViewHolder> {

    private ArrayList<Cashflow> dataList;
    private Integer report_code;

    public CashflowAdapter(ArrayList<Cashflow> dataList) {
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public CashflowAdapter.CashflowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_cashflow, parent, false);
        return new CashflowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CashflowAdapter.CashflowViewHolder holder, int position) {
        holder.amount.setText(dataList.get(position).getmAmount());
        holder.date.setText(dataList.get(position).getmDate());
        holder.description.setText(dataList.get(position).getmDescription());
        report_code = dataList.get(position).getmReport_code();
        if (report_code==1){
            holder.arrow.setImageResource(R.drawable.arrow_up);
        }else{
            holder.arrow.setImageResource(R.drawable.arrow_down);
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class CashflowViewHolder extends RecyclerView.ViewHolder {
        private TextView amount, date, description;
        private ImageView arrow;
        public CashflowViewHolder(View itemView){
            super(itemView);
            amount = (TextView) itemView.findViewById(R.id.nominal);
            date = (TextView) itemView.findViewById(R.id.date_textView);
            description = (TextView) itemView.findViewById(R.id.description_textView);
            arrow = (ImageView) itemView.findViewById(R.id.arrow_image);
        }

    }
}
