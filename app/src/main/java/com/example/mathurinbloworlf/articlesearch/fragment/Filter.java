package com.example.mathurinbloworlf.articlesearch.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mathurinbloworlf.articlesearch.R;
import com.example.mathurinbloworlf.articlesearch.activity.ArticleFeed;
import com.example.mathurinbloworlf.articlesearch.model.Article;
import com.loopj.android.http.RequestParams;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Filter extends DialogFragment{

    Calendar myCalendar;
    EditText date_pickup;
    Spinner order_filter;
    Button save_filter;
    CheckBox arts, fashion, sport;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //StringBuilder fq;

    public static Filter newInstance(){
        Filter filter = new Filter();

        return filter;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        //super.onCancel(dialog);
        //Toast.makeText(getContext(), "Dialog has been canceled", Toast.LENGTH_SHORT).show();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        sharedPreferences = getContext().getSharedPreferences("FilterPref", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_frag, null);

        //Setting up date pickup
        myCalendar = Calendar.getInstance();
        date_pickup = view.findViewById(R.id.date_filter);
        date_pickup.setText(sharedPreferences.getString("date", myCalendar.getTime().toString()));
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                formatLabel(date_pickup);
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

        //Setting up the checkboxes
        arts = view.findViewById(R.id.check_arts);
        fashion = view.findViewById(R.id.check_fashion);
        sport = view.findViewById(R.id.check_sports);

        arts.setChecked(sharedPreferences.getBoolean("arts", false));
        fashion.setChecked(sharedPreferences.getBoolean("fashion", false));
        sport.setChecked(sharedPreferences.getBoolean("sports", false));

        //Setting up the spinner
        order_filter = view.findViewById(R.id.order_filter);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.filter_sort_order,
                        android.R.layout.simple_spinner_item);
        order_filter.setAdapter(arrayAdapter);
        order_filter.setSelection(sharedPreferences.getInt("sort", 0));

        //Setting up the save button
        save_filter = view.findViewById(R.id.save_filter);
        save_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date;
                String order;
                date = date_pickup.getText().toString().replace("/", "");
                if(order_filter.getSelectedItem().toString().equals("Newest")){
                    order = "newest";
                }
                else {
                    order = "oldest";
                }
                ((ArticleFeed)getActivity()).getRequestParams().put("begin_date",date);
                ((ArticleFeed)getActivity()).getRequestParams().put("sort",order);

                String fq = "";
                if (arts.isChecked()){
                    fq = fq + " \"arts\"";
                }
                else{
                    if (fq.contains(" \"arts\"")){
                        fq = fq.replace(" \"arts\"", "");
                    }
                }
                if (fashion.isChecked()){
                    fq = fq + " \"fashion & style\"";
                }
                else{
                    if (fq.contains(" \"fashion & style\"")){
                        fq = fq.replace(" \"fashion & style\"", "");
                    }
                }
                if (sport.isChecked()){
                    fq = fq + " \"sports\"";
                }
                else{
                    if (fq.contains(" \"sports\"")){
                        fq = fq.replace(" \"sports\"", "");
                    }
                }
                if(!TextUtils.isEmpty(fq)){
                    ((ArticleFeed)getActivity()).getRequestParams().put("fq", "news_desk:("+fq.trim()+")");
                }

                editor.putBoolean("sports", sport.isChecked());
                editor.putBoolean("fashion", fashion.isChecked());
                editor.putBoolean("arts", arts.isChecked());
                editor.putString("date", date_pickup.getText().toString());
                editor.putInt("sort", order_filter.getSelectedItemPosition());
                editor.apply();


                dismiss();
            }
        });

        setCancelable(true);

        builder.setView(view);
        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private void formatLabel(EditText editText) {
        //String myFormat = "MM/dd/yyyy"; //In which you need put here
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }
}
