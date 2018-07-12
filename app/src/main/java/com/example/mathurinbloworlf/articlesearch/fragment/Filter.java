package com.example.mathurinbloworlf.articlesearch.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mathurinbloworlf.articlesearch.R;
import com.example.mathurinbloworlf.articlesearch.activity.ArticleFeed;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Filter extends DialogFragment{

    Calendar myCalendar;
    EditText date_pickup;
    Spinner order_filter;
    Button save_filter;

    public static Filter newInstance(){
        Filter filter = new Filter();
        return filter;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //super.onCancel(dialog);
        Toast.makeText(getContext(), "Dialog has been canceled", Toast.LENGTH_SHORT).show();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //String mDataRecieved = getArguments().getString(TITLE,"defaultTitle");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_frag, null);

        //Setting up date pickup
        myCalendar = Calendar.getInstance();
        date_pickup = view.findViewById(R.id.date_filter);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //Setting up the spinner
        order_filter = view.findViewById(R.id.order_filter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.filter_sort_order,
                        android.R.layout.simple_spinner_item);
        order_filter.setAdapter(arrayAdapter);
        //Setting up the save button
        save_filter = view.findViewById(R.id.save_filter);
        /*
        save_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });
        */

        setCancelable(true);

        builder.setView(view);
        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date_pickup.setText(sdf.format(myCalendar.getTime()));
    }
}
