package com.lasalle.meet;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompatExtras;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lasalle.meet.enities.DatePickerFragment;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.User;
import com.lasalle.meet.exceptions.eventexceptions.EventDateStartAfterEndException;
import com.lasalle.meet.exceptions.eventexceptions.EventDateStartBeforeNowException;
import com.lasalle.meet.exceptions.eventexceptions.EventEndDateInvalidException;
import com.lasalle.meet.exceptions.eventexceptions.EventException;
import com.lasalle.meet.exceptions.eventexceptions.EventInvalidNumberException;
import com.lasalle.meet.exceptions.eventexceptions.EventNameNullException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullDescriptionException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullFinishException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullNumberException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullStartDateException;
import com.lasalle.meet.exceptions.eventexceptions.EventNullTypeException;
import com.lasalle.meet.exceptions.eventexceptions.EventStartDateInvalidException;


public class NewEvent extends AppCompatActivity {
    private ImageButton NextButton;

    private User user;
    private static String userId = "USER_ID";

    private static String event_name = "EVENT_NAME";
    private static String event_description = "EVENT_DESCRIPTION";
    private static String event_startDate = "EVENT_START_DATE";
    private static String event_endDate = "EVENT_END_DATE";
    private static String event_Type = "EVENT_TYPE";
    private static String event_Num = "EVENT_NUM";

    private TextInputEditText eventName;
    private TextInputEditText eventDescription;
    private TextInputEditText eventStartDate;
    private TextInputEditText eventEndDate;
    private TextInputEditText eventType;
    private TextInputEditText eventMaxParticipants;

    private TextInputLayout eventName_layout;
    private TextInputLayout eventDescription_layout;
    private TextInputLayout eventStartDate_layout;
    private TextInputLayout eventEndDate_layout;
    private TextInputLayout eventType_layout;
    private TextInputLayout eventMaxParticipants_layout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_event_1_activity);

        user = (User) getIntent().getSerializableExtra(userId);

        NextButton = (ImageButton) findViewById(R.id.next_button_imageView);

        NextButton.setOnClickListener(v -> {
//            Intent intent = new Intent(NewEvent.this, FinishEvent.class);
//            intent.putExtra(userId, user);
//            intent.putExtra(event_name, eventName.getText().toString());
//            intent.putExtra(event_description, eventDescription.getText().toString());
//            intent.putExtra(event_startDate, eventStartDate.getText().toString());
//            intent.putExtra(event_endDate, eventEndDate.getText().toString());
//            intent.putExtra(event_Type, eventType.getText().toString());
//            intent.putExtra(event_Num, Integer.parseInt(eventMaxParticipants.getText().toString()));
//
//            startActivity(intent);
            try {
                Event.newEventCheck(eventName.getText().toString(), eventDescription.getText().toString(), eventStartDate.getText().toString(), eventEndDate.getText().toString(), eventMaxParticipants.getText().toString(), eventType.getText().toString());
            } catch (EventNameNullException e) {
                eventName_layout.setError("The name is empty");
            } catch (EventNullDescriptionException e) {
                eventDescription_layout.setError("The description is empty");
            } catch (EventNullStartDateException e) {
                eventStartDate_layout.setError("The start date is empty");
            } catch (EventNullFinishException e) {
                eventEndDate_layout.setError("The end date is empty");
            } catch (EventNullNumberException e) {
                eventMaxParticipants_layout.setError("The number of participants are empty");
            } catch (EventNullTypeException e) {
                eventType_layout.setError("The event type is empty");
            } catch (EventStartDateInvalidException e) {
                eventStartDate_layout.setError("The starting date needs to be a valid date");
            } catch (EventEndDateInvalidException e) {
                eventEndDate_layout.setError("The ending date needs to be a valid date");
            } catch (EventDateStartBeforeNowException e) {
                eventStartDate_layout.setError("The starting date have to be after current date");
            } catch (EventDateStartAfterEndException e) {
                eventStartDate_layout.setError("The starting date have to be before ending date");
            } catch (EventInvalidNumberException e) {
                eventMaxParticipants_layout.setError("Invalid number of participants");
            } catch (EventException ignored){}
        });

        eventName = (TextInputEditText) findViewById(R.id.event_name_signup2);
        eventDescription = (TextInputEditText) findViewById(R.id.event_name_signup);
        eventStartDate = (TextInputEditText) findViewById(R.id.event_name_signup3);
        eventEndDate = (TextInputEditText) findViewById(R.id.event_name_signup4);
        eventType = (TextInputEditText) findViewById(R.id.event_name_signup6);
        eventMaxParticipants = (TextInputEditText) findViewById(R.id.event_name_signup5);

        eventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        String selectedDate = twoDigits(day) + " / " + twoDigits(month+1) + " / " + year;
                        eventStartDate.setText(selectedDate);
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        String selectedDate = twoDigits(day) + " / " + twoDigits(month+1) + " / " + year;
                        eventEndDate.setText(selectedDate);
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    @Override
    public void onBackPressed() {
        user.logOutUser();

        Intent intent = new Intent(NewEvent.this, HomeScreen.class);
        intent.putExtra(userId, user);
        startActivity(intent);
    }

}
