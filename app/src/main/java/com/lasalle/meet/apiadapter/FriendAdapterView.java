package com.lasalle.meet.apiadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.R;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.Message;
import com.lasalle.meet.enities.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapterView extends RecyclerView.Adapter<FriendAdapterView.ViewHolderEvents>{
    private List<User> friendList;
    private FriendAdapterView.OnNoteListener mOnNoteListener;
    private List<User> currentFriendList;

    public FriendAdapterView(List<User> friendList, FriendAdapterView.OnNoteListener onNoteListener, List<User> currentFriendList) {
        this.friendList = friendList;
        this.mOnNoteListener = onNoteListener;
        this.currentFriendList = currentFriendList;
    }

    @NonNull
    @NotNull
    @Override
    public FriendAdapterView.ViewHolderEvents onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_selector_recycler_view, null, false);
        return new FriendAdapterView.ViewHolderEvents(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull FriendAdapterView.ViewHolderEvents holder, int position) {
        holder.showInfo(this.friendList.get(position));
    }

    public void setDataSet(List<User> newList){
        currentFriendList.clear();
        currentFriendList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolderEvents extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userName;
        CircleImageView userImage;
        FriendAdapterView.OnNoteListener onNoteListener;
        ImageView userAddButton;

        public ViewHolderEvents(@NonNull @NotNull View itemView, FriendAdapterView.OnNoteListener onNoteListener) {
            super(itemView);
            userName = itemView.findViewById(R.id.textView8);
            userImage = itemView.findViewById(R.id.profileImage);
            userAddButton = itemView.findViewById(R.id.imageView3);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

            userAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userAddButton.getTag().toString().equals("YES FRIEND")) {
                        if (onNoteListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                onNoteListener.onFriendAdd(position);
                            }
                        }
                    } else if (userAddButton.getTag().toString().equals("NO FRIEND")) {
                        if (onNoteListener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                onNoteListener.onFriendDelete(position);
                            }
                        }
                    }
                }
            });
        }

        public void showInfo(User user) {
            userName.setText(user.getFullName());
            if (currentFriendList.contains(user)) {
                userAddButton.setImageResource(R.drawable.delete_friend);
                userAddButton.setTag("NO FRIEND");
            }

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnNoteListener {
        void onNoteClick(int position);
        void onFriendAdd(int position);
        void onFriendDelete(int position);
    }
}
