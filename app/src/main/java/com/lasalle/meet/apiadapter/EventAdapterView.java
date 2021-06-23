package com.lasalle.meet.apiadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lasalle.meet.R;
import com.lasalle.meet.enities.Event;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EventAdapterView extends RecyclerView.Adapter<EventAdapterView.ViewHolderEvents> {

    List<Event> eventList;

    public EventAdapterView(List<Event> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolderEvents onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_recycler_list, null, false);
        return new ViewHolderEvents(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderEvents holder, int position) {
        holder.showInfo(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolderEvents extends RecyclerView.ViewHolder {

        TextView eventName;

        public ViewHolderEvents(@NonNull @NotNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_recycler_data);
        }

        public void showInfo(Event event) {
            eventName.setText(event.getName());
        }
    }
}
