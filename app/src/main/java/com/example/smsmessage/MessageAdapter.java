package com.example.smsmessage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsmessage.databinding.ItemSmsMessageBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<SmsMessage> smsList;
    private Context context;
    private ItemSmsMessageBinding smsItemBinding;

    public MessageAdapter(List<SmsMessage> smsList, Context context) {
        this.smsList = smsList;
        this.context = context;
    }

    public void addMessage(SmsMessage message) {
        smsList.add(message);
        notifyItemInserted(smsList.size() - 1);
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
        holder.binding.itemDate.setText(getTimeByMillisecond(sms.getDate()));
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

    public String getTimeByMillisecond(String time) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        long milliSeconds= Long.parseLong(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }
}