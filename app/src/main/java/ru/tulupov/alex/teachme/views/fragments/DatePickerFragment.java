package ru.tulupov.alex.teachme.views.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.tulupov.alex.teachme.Constants;


public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener
{
    private TextView etDate = null;
    private String prevDate;


    private void setText(String text) {
        if ( etDate != null && text != null && !text.equals("")) {
            etDate.setText(text);
        }
    }

    public void setEditTextDate(TextView etDate) {
        this.etDate = etDate;
        prevDate = etDate.getText().toString();
    }

    public DatePickerFragment() {}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        setText(getDate(calendar.getTimeInMillis()));
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        setText(prevDate);
    }

    public String getDate (long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }
}
