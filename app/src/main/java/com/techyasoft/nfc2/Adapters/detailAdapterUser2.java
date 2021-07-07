package com.techyasoft.nfc2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techyasoft.nfc2.Activities.ScanningActivity;
import com.techyasoft.nfc2.Activities.ScanningActivity2;
import com.techyasoft.nfc2.databinding.ItemViewBinding;
import com.techyasoft.nfc2.model.Tour;

import java.util.HashMap;
import java.util.List;

public class detailAdapterUser2 extends RecyclerView.Adapter<detailAdapterUser2.ViewHodler> {
    final String TAG = detailAdapterUser2.class.getSimpleName();
    Context context;
    List<Tour> list;

    public detailAdapterUser2(Context context, List<Tour> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewBinding binding = ItemViewBinding.inflate(LayoutInflater.from(context),parent,false);
        return new ViewHodler(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull detailAdapterUser2.ViewHodler holder, int position) {
        Tour tour = list.get(position);
        holder.binding.tourNumber.setText(tour.getTour_number()+"");
        holder.binding.boxNumber.setText(tour.getTotal_swipes()+"/"+ ScanningActivity2.TOTAL_CONTAINERS);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        ItemViewBinding binding;
        public ViewHodler(@NonNull ItemViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
