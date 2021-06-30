package com.lasalle.meet.enities;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;

public class EditSessionActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener {
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //do some stuff for example write on log and update TextField on activity
        Log.w("DatePicker", "Date = " + year);
//        ((EditText) findViewById(R.id.tf_date)).setText("Date = " + year);
    }
}