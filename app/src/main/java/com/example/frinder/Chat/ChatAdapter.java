package com.example.frinder.Chat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.frinder.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {

    private List<ChatObject> chatList;
    private Context context;

    public ChatAdapter(List<ChatObject> matchesList, Context context) {
        this.chatList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolders chatViewHolders = new ChatViewHolders(layoutView);
        return chatViewHolders;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {


        holder.mMessage.setText(chatList.get(position).getMessage());

        if (chatList.get(position).getCurrentUser()){

            holder.mMessage.setGravity(Gravity.RIGHT);
            holder.mMessage.setTextColor(Color.parseColor("#f4f4f4"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#1a9247"));

        }else{
            holder.mMessage.setGravity(Gravity.LEFT);
            holder.mMessage.setTextColor(Color.parseColor("#f4f4f4"));
            holder.mContainer.setBackgroundColor(Color.parseColor("#404040"));

        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
