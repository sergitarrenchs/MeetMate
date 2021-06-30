package com.lasalle.meet.apiadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.R;
import com.lasalle.meet.enities.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewFriendAdapterView extends RecyclerView.Adapter<AddNewFriendAdapterView.ViewHolderEvents> {

    private List<User> friendList;
    private AddNewFriendAdapterView.OnNoteListener mOnNoteListener;

    public AddNewFriendAdapterView(List<User> friendList, AddNewFriendAdapterView.OnNoteListener mOnNoteListener) {
        this.friendList = friendList;
        this.mOnNoteListener = mOnNoteListener;
    }

    @NonNull
    @NotNull
    @Override
    public AddNewFriendAdapterView.ViewHolderEvents onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_recycler, null, false);
        return new AddNewFriendAdapterView.ViewHolderEvents(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddNewFriendAdapterView.ViewHolderEvents holder, int position) {
        holder.showInfo(this.friendList.get(position));
    }

    public void setDataSet(List<User> newList){
        friendList.clear();
        friendList.addAll(newList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public class ViewHolderEvents extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView userName;
        CircleImageView userImage;
        AddNewFriendAdapterView.OnNoteListener onNoteListener;
        ImageView userAddButton;
        ImageView userDiscardButton;
        ImageView userBackground;

        public ViewHolderEvents(@NonNull @NotNull View itemView, AddNewFriendAdapterView.OnNoteListener onNoteListener) {
            super(itemView);
            userName = itemView.findViewById(R.id.textView8);
            userImage = itemView.findViewById(R.id.profileImage);
            userAddButton = itemView.findViewById(R.id.imageView4);
            userDiscardButton = itemView.findViewById(R.id.imageView3);
            userBackground = itemView.findViewById(R.id.imageView5);


            this.onNoteListener = onNoteListener;

            userAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNoteListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onNoteListener.onAddClick(position);
                        }
                    }
                }
            });

            userDiscardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onNoteListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onNoteListener.onDiscardClick(position);
                        }
                    }
                }
            });
        }

        public void showInfo(User user) {
            userName.setText(user.getFullName());

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
        void onAddClick(int position);
        void onDiscardClick(int position);
    }
}
