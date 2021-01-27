package com.spacebargamestudios.financialplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class LoanOrEMI extends AppCompatActivity {

    private EditText emi, period, roi, totalpaid, loan, totalint;
    private Button calculate, balancestatement, balancestatementplus;
    private RadioButton rb_loan, rb_emi;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_or_emi);

        loan = findViewById(R.id.edittext_loanamount);
        period = findViewById(R.id.edittext_period);
        roi = findViewById(R.id.edittext_roi);
        emi = findViewById(R.id.edittext_emi);
        totalpaid = findViewById(R.id.edittext_totalpayment);
        totalint = findViewById(R.id.edittext_totalinterest);

        calculate = findViewById(R.id.button_calculate);
        balancestatement  = findViewById(R.id.button_balancestatement);
        balancestatementplus = findViewById(R.id.button_balancestatementplus);

        rb_loan = findViewById(R.id.radiobutton_loan);
        rb_emi = findViewById(R.id.radiobutton_emi);

        Random rand = new Random();

        final boolean checked = rand.nextBoolean();
        rb_loan.setChecked(checked);
        FinData.rb_corpus = checked;
        loan.setEnabled(checked);
        rb_emi.setChecked(!checked);
        FinData.rb_sip = !checked;
        emi.setEnabled(!checked);

        if (checked) {
            FinData.corpus = (rand.nextInt(10) + 1) * 1000000;
            loan.setText(Integer.toString((int) FinData.corpus));
        } else {
            FinData.startingsip = (rand.nextInt(10) + 1) * 1000;
            emi.setText(Integer.toString((int) FinData.startingsip));
        }

        FinData.period = rand.nextInt(30);
        period.setText(Integer.toString(FinData.period));

        FinData.roi = rand.nextInt(15);
        roi.setText(Double.toString(FinData.roi));

        final FinCalc calc = new FinCalc();
        if (checked){
            calc.LoanToEMICalc();
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(16);
            emi.setFilters(FilterArray);
            FilterArray[0] = new InputFilter.LengthFilter(16);
            loan.setFilters(FilterArray);
        } else {
            calc.EMItoLoanCalc();
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(16);
            emi.setFilters(FilterArray);
            FilterArray[0] = new InputFilter.LengthFilter(16);
            loan.setFilters(FilterArray);
        }
        displayResults();

        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                if (FinData.rb_corpus) {
                    calc.LoanToEMICalc();
                } else {
                    calc.EMItoLoanCalc();
                }
                displayResults();
            }
        });

        balancestatement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                if (FinData.rb_corpus) {
                    calc.LoanToEMICalc();
                } else {
                    calc.EMItoLoanCalc();
                }
                displayResults();
                gotoBalanceStatement(v);
            }
        });

        balancestatementplus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                if (FinData.rb_corpus) {
                    calc.LoanToEMICalc();
                } else {
                    calc.EMItoLoanCalc();
                }
                displayResults();
                gotoBalanceStatementPlus(v);
            }
        });

        rb_loan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean tmp = true;
                rb_loan.setChecked(tmp);
                FinData.rb_corpus = tmp;
                rb_emi.setChecked(false);
                FinData.rb_sip = false;
                loan.setEnabled(tmp);
                emi.setEnabled(false);
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(16);
                emi.setFilters(FilterArray);
                FilterArray[0] = new InputFilter.LengthFilter(16);
                loan.setFilters(FilterArray);
            }
        });

        rb_emi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean tmp = false;
                rb_loan.setChecked(tmp);
                FinData.rb_corpus = tmp;
                rb_emi.setChecked(true);
                FinData.rb_sip = true;
                loan.setEnabled(tmp);
                emi.setEnabled(true);
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(9);
                emi.setFilters(FilterArray);
                FilterArray[0] = new InputFilter.LengthFilter(17);
                loan.setFilters(FilterArray);
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

        if (FinData.rb_corpus){
            vv2 = checkInputDouble(loan);
            if (vv2 < 0){
                resetInput(loan,"1000000");
                vv2 = 1000000;
            }
            FinData.corpus = vv2;
        } else {
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(9);
            emi.setFilters(FilterArray);
            vv2 = checkInputInt(emi);
            if (vv2 < 0){
                resetInput(emi,"1");
                vv2 = 1;
            }
            FinData.startingsip = vv2;
        }

    }

    public void displayResults(){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        formatter.applyPattern("#,###,###,###");
        String formattedString;

        long tmp;

        tmp = (long)FinData.totalInvestment;
        formattedString = formatter.format(tmp);
        totalpaid.setText(formattedString);

        tmp = (long)FinData.interestEarned;
        formattedString = formatter.format(tmp);
        totalint.setText(formattedString);

        if (FinData.rb_corpus) {
            tmp = (long)FinData.startingsip;
            emi.setText(Long.toString(tmp));
        } else {
            tmp = (long) FinData.corpus;
            loan.setText(Long.toString(tmp));
        }
    }

    public void gotoBalanceStatement(View view){
        Intent intent = new Intent (this, LoanStatementShort.class);
        startActivity(intent);
    }

    public void gotoBalanceStatementPlus(View view){
        Intent intent = new Intent (this, LoanStatementLong.class);
        startActivity(intent);
    }
}
