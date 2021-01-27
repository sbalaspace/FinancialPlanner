package com.spacebargamestudios.financialplanner;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class LoanToEMI extends AppCompatActivity {

    private EditText emi, period, roi, totalpaid, loan, totalint;
    private Button calculate, balancestatement, balancestatementplus;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_to_emi);

        loan = findViewById(R.id.edittext_loanamount);
        period = findViewById(R.id.edittext_period);
        roi = findViewById(R.id.edittext_roi);
        emi = findViewById(R.id.edittext_emi);
        totalpaid = findViewById(R.id.edittext_totalpayment);
        totalint = findViewById(R.id.edittext_totalinterest);

        calculate = findViewById(R.id.button_calculate);
        balancestatement  = findViewById(R.id.button_balancestatement);
        balancestatementplus = findViewById(R.id.button_balancestatementplus);

        Random rand = new Random();

        FinData.corpus = (rand.nextInt(10)+1)*1000000;
        loan.setText(Integer.toString((int) FinData.corpus));

        FinData.period = rand.nextInt(30);
        period.setText(Integer.toString(FinData.period));

        FinData.roi = rand.nextInt(15);
        roi.setText(Double.toString(FinData.roi));

        final FinCalc calc = new FinCalc();
        calc.LoanToEMICalc();
        displayResults();

        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                calc.LoanToEMICalc();
                displayResults();
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
        et.setTextColor(Color.parseColor("#FF0000"));
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

        vv2 = checkInputDouble(loan);
        if (vv2 < 0){
            resetInput(loan,"1000000");
            vv2 = 1;
        }
        FinData.corpus = vv2;
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

        tmp = (long)FinData.startingsip;
        formattedString = formatter.format(tmp);
        emi.setText(formattedString);
    }
}
