package com.lasalle.meet.apiadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.lasalle.meet.R;
import com.lasalle.meet.enities.Message;
import com.lasalle.meet.enities.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MessageAdapterView extends RecyclerView.Adapter<MessageAdapterView.ViewHolderEvents>{
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_message_recycler, null, false);
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

    public void setDataSet(List<Message> newList){
        messageList.clear();
        messageList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolderEvents extends RecyclerView.ViewHolder {

        MaterialTextView messageView;
        MaterialTextView messageViewOther;

        public ViewHolderEvents(@NonNull @NotNull View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.textView6);
            messageViewOther = itemView.findViewById(R.id.textView7);

        }

        public void showInfo(Message message) {
            if (message.getUserID() == user.getId()) {
                messageView.setText(message.getContent());
                messageView.setVisibility(View.VISIBLE);
            } else {
                messageViewOther.setText(message.getContent());
                messageViewOther.setVisibility(View.VISIBLE);
            }


        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
