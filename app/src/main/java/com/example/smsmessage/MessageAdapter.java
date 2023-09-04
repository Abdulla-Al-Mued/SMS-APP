package com.example.smsmessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsmessage.databinding.ItemSmsMessageBinding;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<SmsMessage> smsList;
    private Context context;
    private ItemSmsMessageBinding smsItemBinding;

    public MessageAdapter(List<SmsMessage> smsList, Context context) {
        this.smsList = smsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        smsItemBinding = ItemSmsMessageBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(smsItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SmsMessage sms = smsList.get(position);
        holder.binding.smsTitle.setText(sms.getSender());
        holder.binding.details.setText(sms.getMessage());
        //holder.binding.itemDate.setText(sms.getDate());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemSmsMessageBinding binding;
        public ViewHolder(ItemSmsMessageBinding smsBinding) {
            super(smsBinding.getRoot());
            binding = smsBinding;
        }
    }
}