package com.lasalle.meet;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.lasalle.meet.enities.DatePickerFragment;
import com.lasalle.meet.enities.Event;
import com.lasalle.meet.enities.TimePickerFragment;
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

        eventName = (TextInputEditText) findViewById(R.id.event_name_input);
        eventDescription = (TextInputEditText) findViewById(R.id.event_description_input);
        eventStartDate = (TextInputEditText) findViewById(R.id.event_start_date_input);
        eventEndDate = (TextInputEditText) findViewById(R.id.event_end_date_input);
        eventType = (TextInputEditText) findViewById(R.id.event_type_input);
        eventMaxParticipants = (TextInputEditText) findViewById(R.id.event_max_participants_input);

        eventName_layout = (TextInputLayout) findViewById(R.id.event_name_layout);
        eventDescription_layout = (TextInputLayout) findViewById(R.id.event_description_layout);
        eventStartDate_layout = (TextInputLayout) findViewById(R.id.event_start_date_layout);
        eventEndDate_layout = (TextInputLayout) findViewById(R.id.event_end_date_layout);
        eventType_layout = (TextInputLayout) findViewById(R.id.event_type_layout);
        eventMaxParticipants_layout = (TextInputLayout) findViewById(R.id.event_max_participants_layout);

        eventStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventStartDate_layout.setErrorEnabled(false);
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        String selectedDate = twoDigits(day) + " / " + twoDigits(month+1) + " / " + year;

                        TimePickerFragment new2Fragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = hourOfDay + " : " + minute;

                                String date = selectedDate + " " + selectedTime;

                                eventStartDate.setText(date);
                            }

                        });

                        new2Fragment.show(getSupportFragmentManager(), "timePicker");
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        eventEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventEndDate_layout.setErrorEnabled(false);
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        String selectedDate = twoDigits(day) + " / " + twoDigits(month+1) + " / " + year;

                        TimePickerFragment new2Fragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = twoDigits(hourOfDay) + " : " + twoDigits(minute);

                                String date = selectedDate + " " + selectedTime;

                                eventEndDate.setText(date);
                            }

                        });

                        new2Fragment.show(getSupportFragmentManager(), "timePicker");
                    }
                });

                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        eventName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                eventName_layout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        eventDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                eventDescription_layout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eventMaxParticipants.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                eventMaxParticipants_layout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        eventType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                eventType_layout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        NextButton = (ImageButton) findViewById(R.id.next_button_imageView);

        NextButton.setOnClickListener(v -> {
            try {
                Event.newEventCheck(eventName.getText().toString(), eventDescription.getText().toString(), eventStartDate.getText().toString(), eventEndDate.getText().toString(), eventMaxParticipants.getText().toString(), eventType.getText().toString());

                Intent intent = new Intent(NewEvent.this, FinishEvent.class);
                intent.putExtra(userId, user);
                intent.putExtra(event_name, eventName.getText().toString());
                intent.putExtra(event_description, eventDescription.getText().toString());
                intent.putExtra(event_startDate, eventStartDate.getText().toString());
                intent.putExtra(event_endDate, eventEndDate.getText().toString());
                intent.putExtra(event_Type, eventType.getText().toString());
                intent.putExtra(event_Num, Integer.parseInt(eventMaxParticipants.getText().toString()));

                startActivity(intent);

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
