package com.spacebargamestudios.financialplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class CorpusOrSIP extends AppCompatActivity {

    private EditText sip1, period, roi, sipfactor, finalcorpus, totalinv, totalint;
    private Button calculate, balancestatement, balancestatementplus;
    private RadioButton rb_corpus, rb_sip;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.corpus_or_sip);

        sip1 = findViewById(R.id.edittext_startingsip);
        period = findViewById(R.id.edittext_period);
        roi = findViewById(R.id.edittext_roi);
        sipfactor = findViewById(R.id.edittext_sipannualincrease);
        finalcorpus = findViewById(R.id.edittext_finalcorpus);
        totalinv = findViewById(R.id.edittext_invested);
        totalint = findViewById(R.id.edittext_interest);

        calculate = findViewById(R.id.button_calculate);
        balancestatement  = findViewById(R.id.button_balancestatement);
        balancestatementplus = findViewById(R.id.button_balancestatementplus);

        rb_corpus = findViewById(R.id.radiobutton_corpus);
        rb_sip = findViewById(R.id.radiobutton_sip);

        Random rand = new Random();

        final boolean checked = rand.nextBoolean();
        rb_corpus.setChecked(checked);
        FinData.rb_corpus = checked;
        finalcorpus.setEnabled(checked);
        rb_sip.setChecked(!checked);
        FinData.rb_sip = !checked;
        sip1.setEnabled(!checked);

        if (checked){
            FinData.corpus = rand.nextInt(10)*1000000;
            finalcorpus.setText(Integer.toString((int) FinData.corpus));
        } else {
            sip1.setText(Integer.toString((rand.nextInt(10)+1)*1000));
            FinData.startingsip = Double.parseDouble(sip1.getText().toString());
        }

        FinData.period = rand.nextInt(99);
        period.setText(Integer.toString(FinData.period));

        FinData.roi = rand.nextInt(15);
        roi.setText(Double.toString(FinData.roi));

        FinData.sipfactor = rand.nextInt(50);
        sipfactor.setText(Double.toString(FinData.sipfactor));

        final FinCalc calc = new FinCalc();
        if (checked){
            calc.CorpusToSIPCalc();
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(14);
            sip1.setFilters(FilterArray);
            FilterArray[0] = new InputFilter.LengthFilter(16);
            finalcorpus.setFilters(FilterArray);
        } else {
            calc.SIPtoCorpusCalc();
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(9);
            sip1.setFilters(FilterArray);
            FilterArray[0] = new InputFilter.LengthFilter(17);
            finalcorpus.setFilters(FilterArray);
        }
        displayResults();

        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                if (FinData.rb_corpus) {
                    calc.CorpusToSIPCalc();
                } else {
                    calc.SIPtoCorpusCalc();
                }
                displayResults();
            }
        });

        balancestatement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                if (FinData.rb_corpus) {
                    calc.CorpusToSIPCalc();
                } else {
                    calc.SIPtoCorpusCalc();
                }
                gotoBalanceStatement(v);
            }
        });

        balancestatementplus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                if (FinData.rb_corpus) {
                    calc.CorpusToSIPCalc();
                } else {
                    calc.SIPtoCorpusCalc();
                }
                gotoBalanceStatementPlus(v);
            }
        });

        rb_corpus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean tmp = true;
                rb_corpus.setChecked(tmp);
                FinData.rb_corpus = tmp;
                rb_sip.setChecked(false);
                FinData.rb_sip = false;
                finalcorpus.setEnabled(tmp);
                sip1.setEnabled(false);
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(14);
                sip1.setFilters(FilterArray);
                FilterArray[0] = new InputFilter.LengthFilter(16);
                finalcorpus.setFilters(FilterArray);
            }
        });

        rb_sip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean tmp = false;
                rb_corpus.setChecked(tmp);
                FinData.rb_corpus = tmp;
                rb_sip.setChecked(true);
                FinData.rb_sip = true;
                finalcorpus.setEnabled(tmp);
                sip1.setEnabled(true);
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(9);
                sip1.setFilters(FilterArray);
                FilterArray[0] = new InputFilter.LengthFilter(17);
                finalcorpus.setFilters(FilterArray);
            }
        });
    }


    public int checkInputInt(EditText et){
        try {
            return Integer.parseInt(et.getText().toString());
        } catch (Exception e){
            return -999;
        }
    }

    public double checkInputDouble(EditText et){
        try {
            return Double.parseDouble(et.getText().toString());
        } catch (Exception e){
            return -999;
        }
    }
    public void resetInput(EditText et, String value){
        et.setText(value);
        //et.setTextColor(Color.parseColor("#FF0000"));
    }

    public void checkUserInputs(){
        int vv1 = checkInputInt(period);
        if (vv1 < 1){
            resetInput(period,"1");
            vv1 = 1;
        }
        FinData.period = vv1;

        double vv2 = checkInputDouble(roi);
        if (vv2 < 0){
            resetInput(roi,"1");
            vv2 = 1;
        }
        if (vv2 > 100){
            resetInput(roi,"100");
            vv2 = 100;
        }
        FinData.roi = vv2;

        vv2 = checkInputDouble(sipfactor);
        if (vv2 < 0){
            resetInput(sipfactor,"1");
            vv2 = 1;
        }
        if (vv2 > 100){
            resetInput(sipfactor,"100");
            vv2 = 100;
        }
        FinData.sipfactor = vv2;

        if (FinData.rb_corpus){
            vv2 = checkInputDouble(finalcorpus);
            if (vv2 < 0){
                resetInput(finalcorpus,"1000000");
                vv2 = 1000000;
            }
            FinData.corpus = vv2;
        } else {
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(9);
            sip1.setFilters(FilterArray);
            vv2 = checkInputInt(sip1);
            if (vv2 < 0){
                resetInput(sip1,"1");
                vv2 = 1;
            }
            FinData.startingsip = vv2;
        }

    }

    public void displayResults(){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        formatter.applyPattern("#,###,###,###");
        String formattedString;

        long tmp = (long)FinData.totalInvestment;
        formattedString = formatter.format(tmp);
        totalinv.setText(formattedString);

        tmp = (long)FinData.interestEarned;
        formattedString = formatter.format(tmp);
        totalint.setText(formattedString);

        if (FinData.rb_corpus) {
            tmp = (long)FinData.startingsip;
            sip1.setText(Long.toString(tmp));
        } else {
            tmp = (long)FinData.corpus;
            finalcorpus.setText(Long.toString(tmp));
        }
    }

    public void gotoBalanceStatement(View view){
        Intent intent = new Intent (this, BalanceStatementShort.class);
        startActivity(intent);
    }

    public void gotoBalanceStatementPlus(View view){
        Intent intent = new Intent (this, BalanceStatementLong.class);
        startActivity(intent);
    }
}
