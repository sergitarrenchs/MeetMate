package com.lasalle.meet.apiadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.R;
import com.lasalle.meet.enities.Event;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventAdapterView extends RecyclerView.Adapter<EventAdapterView.ViewHolderEvents> {

    private List<Event> eventList;
    private OnNoteListener mOnNoteListener;

    public EventAdapterView(List<Event> eventList, OnNoteListener onNoteListener) {
        this.eventList = eventList;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderEvents onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recycler_list, null, false);
        return new ViewHolderEvents(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderEvents holder, int position) {
        holder.showInfo(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolderEvents extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView eventName;
        TextView eventDate;
        ImageView eventImage;
        OnNoteListener onNoteListener;
        ImageView eventImageButton;

        public ViewHolderEvents(@NonNull @NotNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            eventName = itemView.findViewById(R.id.eventNameText);
            eventDate = itemView.findViewById(R.id.adressText);
            eventImage = itemView.findViewById(R.id.eventImageView);
            eventImageButton = itemView.findViewById(R.id.eventImageButton);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        public void showInfo(Event event) {
            eventName.setText(event.getName());
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
