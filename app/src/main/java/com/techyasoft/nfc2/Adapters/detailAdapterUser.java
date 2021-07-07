package com.techyasoft.nfc2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techyasoft.nfc2.Activities.ScanningActivity;
import com.techyasoft.nfc2.databinding.ItemViewBinding;

import java.util.HashMap;
import java.util.Set;

public class detailAdapterUser extends RecyclerView.Adapter<detailAdapterUser.ViewHodler> {
    final String TAG = detailAdapterUser.class.getSimpleName();
    Context context;
    HashMap<Integer, Integer> list;

    public detailAdapterUser(Context context,HashMap<Integer, Integer> list) {
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
    public void onBindViewHolder(@NonNull detailAdapterUser.ViewHodler holder, int position) {
        int key = position+1;
        int val = list.get(key);
        holder.binding.tourNumber.setText(key+"");
        holder.binding.boxNumber.setText(val+"/"+ ScanningActivity.TOTAL_CONTAINERS);
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
