package com.lasalle.meet.apiadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.R;
import com.lasalle.meet.enities.Message;
import com.lasalle.meet.enities.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapterView extends RecyclerView.Adapter<MessageAdapterView.ViewHolderEvents>{
    private static final int MESSAGE_SENT = 1;
    private static final int MESSAGE_RECEIVED = -1;

    private List<Message> messageList;
    private User user;

    public MessageAdapterView(List<Message> messageList, User user) {
        this.messageList = messageList;
        this.user = user;
    }

    @NonNull
    @NotNull
    @Override
    public MessageAdapterView.ViewHolderEvents onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == MESSAGE_SENT) ? R.layout.chat_message_recycler_right : R.layout.chat_message_recycler;

        CardView view = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);


        return new MessageAdapterView.ViewHolderEvents(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageAdapterView.ViewHolderEvents holder, int position) {
        holder.showInfo(this.messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int userId = user.getId();
        int senderId = messageList.get(position).getUserID();
        return (senderId == userId) ? MESSAGE_SENT : MESSAGE_RECEIVED;
    }


    public void setDataSet(List<Message> newList){
        messageList.clear();
        messageList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolderEvents extends RecyclerView.ViewHolder {

        MaterialTextView messageView;

        public ViewHolderEvents(@NonNull @NotNull View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.message_id);

        }

        public void showInfo(Message message) {
                messageView.setText(message.getContent());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
