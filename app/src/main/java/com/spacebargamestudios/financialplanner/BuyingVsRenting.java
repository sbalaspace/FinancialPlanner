package com.spacebargamestudios.financialplanner;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class BuyingVsRenting extends AppCompatActivity {

    private EditText homecost, downpayment, period, roi_loan, rent, rentinc, roi_inv, buyer_corpus, renter_corpus;
    private Button calculate, balancestatement,  balancestatementplus;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.buying_vs_renting);

        homecost = findViewById(R.id.edittext_costofhome);
        downpayment = findViewById(R.id.edittext_downpayment);
        period = findViewById(R.id.edittext_period);
        roi_loan = findViewById(R.id.edittext_roiloan);
        rent = findViewById(R.id.edittext_currentRent);
        rentinc = findViewById(R.id.edittext_annualincrease);
        roi_inv = findViewById(R.id.edittext_roi_inv);
        buyer_corpus = findViewById(R.id.edittext_buyer);
        renter_corpus = findViewById(R.id.edittext_renter);

        calculate = findViewById(R.id.button_calculate);
        balancestatement = findViewById(R.id.button_balancestatement);
        balancestatementplus = findViewById(R.id.button_balancestatementplus);

        Random rand = new Random();

        int randexp = rand.nextInt(3) + 4;
        FinData.corpus = (rand.nextInt(10) + 1) * Math.pow(10, randexp);
        homecost.setText(Long.toString((int) FinData.corpus));

        FinData.currentsavings = (int) (FinData.corpus * rand.nextInt(80) / 100.0);
        downpayment.setText(Integer.toString(FinData.currentsavings));

        FinData.period = rand.nextInt(98) + 1;
        period.setText(Integer.toString(FinData.period));

        FinData.roi = rand.nextInt(50);
        roi_loan.setText(Double.toString(FinData.roi));

        FinData.currentexpenses = (int) (FinData.corpus / ((rand.nextInt(21)+10)*12));
        rent.setText(Integer.toString(FinData.currentexpenses));

        FinData.inflation = rand.nextInt(50);
        rentinc.setText(Double.toString(FinData.inflation));

        FinData.roi_inv = rand.nextInt(50);
        roi_inv.setText(Double.toString(FinData.roi_inv));

        final FinCalc calc = new FinCalc();
        calc.LoanToEMICalc();
        calc.buyingOrRenting();

        checkUserInputs();
        calc.LoanToEMICalc();
        calc.buyingOrRenting();
        displayResults();

        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                calc.LoanToEMICalc();
                calc.buyingOrRenting();
                displayResults();
            }
        });

        balancestatement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                calc.LoanToEMICalc();
                calc.buyingOrRenting();

                displayResults();
                gotoBalanceStatement(v);
            }
        });

        balancestatementplus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                calc.LoanToEMICalc();
                calc.buyingOrRenting();

                displayResults();
                gotoBalanceStatementPlus(v);
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

        vv1 = checkInputInt(rent);
        if (vv1 < 0){
            resetInput(rent,"0");
            vv1 = 1;
        }
        FinData.currentexpenses = vv1;

        double vv2 = checkInputDouble(roi_loan);
        if (vv2 < 0){
            resetInput(roi_loan,"0");
            vv2 = 1;
        }
        if (vv2 > 100){
            resetInput(roi_loan,"100");
            vv2 = 100;
        }
        FinData.roi = vv2;

        vv2 = checkInputDouble(roi_inv);
        if (vv2 < 0){
            resetInput(roi_inv,"0");
            vv2 = 1;
        }
        if (vv2 > 100){
            resetInput(roi_inv,"100");
            vv2 = 100;
        }
        FinData.roi_inv = vv2;

        vv2 = checkInputDouble(rentinc);
        if (vv2 < 0){
            resetInput(rentinc,"0");
            vv2 = 1;
        }
        if (vv2 > 100){
            resetInput(rentinc,"100");
            vv2 = 100;
        }
        FinData.inflation = vv2;

        vv2 = checkInputDouble(homecost);
        if (vv2 < 0){
            resetInput(homecost,"0");
            vv2 = 1;
        }
        FinData.corpusreqd = vv2;

        vv1 = checkInputInt(downpayment);
        if (vv1 < 0){
            resetInput(downpayment,"0");
            vv1 = 1;
        }
        if (vv1 > vv2){
            vv1 = (int)(vv2*0.2);
            resetInput(downpayment,Integer.toString(vv1));
        }
        FinData.currentsavings = vv1;

        FinData.corpus = FinData.corpusreqd - FinData.currentsavings;

    }

    public void displayResults(){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        formatter.applyPattern("#,###,###,###");
        String formattedString;

        long tmp;

        tmp = (long)FinData.buyercorpus;
        formattedString = formatter.format(tmp);
        buyer_corpus.setText(formattedString);

        tmp = (long)FinData.rentercorpus;
        formattedString = formatter.format(tmp);
        renter_corpus.setText(formattedString);
    }

    public void gotoBalanceStatement(View view){
        Intent intent = new Intent (this, BuyRentStatementShort.class);
        startActivity(intent);
    }

    public void gotoBalanceStatementPlus(View view){
        Intent intent = new Intent (this, BuyRentStatementLong.class);
        startActivity(intent);
    }
}