package com.orane.icliniq.utils.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orane.icliniq.R;
import com.orane.icliniq.utils.walletModel.SpecialistModel;

import java.util.ArrayList;
import java.util.List;

public class SpecialistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SpecialistModel> mItemList;

    public SpecialistAdapter(ArrayList<SpecialistModel> walletList) {
        mItemList=walletList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View view;
        view = LayoutInflater.from(context)
                .inflate(R.layout.wallet_recyclerview_item, viewGroup, false);
        return new RecyclerItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        holder.txt_amount.setVisibility(View.GONE);
        holder.txt_date.setVisibility(View.GONE);
        holder.txt_details.setText(mItemList.get(position).getSpName());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position ;
    }

    private static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_details,txt_date,txt_amount;

        public RecyclerItemViewHolder(View itemView) {
            super(itemView);
            txt_details = itemView.findViewById(R.id.txt_details);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_amount = itemView.findViewById(R.id.txt_amount);
        }
    }

}

