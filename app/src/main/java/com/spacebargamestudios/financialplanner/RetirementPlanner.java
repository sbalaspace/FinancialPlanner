package com.spacebargamestudios.financialplanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class RetirementPlanner extends AppCompatActivity {

    private EditText currentage, retirementage, life, expenses, savings, inflation, roi, sipfactor, result;
    private Button calculate, balancestatement, balancestatementplus;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.retirement_planner);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.plan_my_retirement );

        currentage = findViewById(R.id.edittext_currentage);
        retirementage = findViewById(R.id.edittext_retirementage);
        life = findViewById(R.id.edittext_lifeexpectancy);
        expenses = findViewById(R.id.edittext_currentexpenses);
        savings = findViewById(R.id.edittext_currentsavings);
        inflation = findViewById(R.id.edittext_inflation);
        roi = findViewById(R.id.edittext_roi);
        result = findViewById(R.id.edittext_totalexpense);
        sipfactor = findViewById(R.id.edittext_sipfactor);

        calculate = findViewById(R.id.button_calculate);
        balancestatement = findViewById(R.id.button_balancestatement);
        balancestatementplus = findViewById(R.id.button_balancestatementplus);

        Random rand = new Random();

        FinData.currentage = rand.nextInt(98)+1;
        currentage.setText(Integer.toString(FinData.currentage));

        FinData.retirementage = rand.nextInt(100 - FinData.currentage)+FinData.currentage;
        retirementage.setText(Integer.toString(FinData.retirementage));

        FinData.lifeexpectancy = rand.nextInt(150 - FinData.retirementage) + FinData.retirementage;
        life.setText(Integer.toString(FinData.lifeexpectancy));

        FinData.currentexpenses = (rand.nextInt(1000) + 1)*1000;
        expenses.setText(Integer.toString(FinData.currentexpenses));

        FinData.currentsavings = FinData.currentexpenses * rand.nextInt(100);
        savings.setText(Integer.toString(FinData.currentsavings));

        FinData.inflation = rand.nextInt(50);
        inflation.setText(Double.toString(FinData.inflation));

        FinData.roi = rand.nextInt(15);
        roi.setText(Double.toString(FinData.roi));

        FinData.sipfactor = rand.nextInt(50);
        sipfactor.setText(Double.toString(FinData.sipfactor));

        final FinCalc calc = new FinCalc();
        calc.getCorpusReqd();
        displayResults();

        calculate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                calc.getCorpusReqd();
                displayResults();
            }
        });

        balancestatement.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                calc.getCorpusReqd();
                displayResults();
                FinData.freq = 12;
                gotoBalanceStatement(v);
            }
        });

        balancestatementplus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkUserInputs();
                calc.getCorpusReqd();
                displayResults();
                FinData.freq = 1;
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

        int vv1 = checkInputInt(currentage);
        if (vv1 < 0){
            resetInput(currentage,"1");
            vv1 = 1;
        }
        FinData.currentage = vv1;

        vv1 = checkInputInt(retirementage);
        if (vv1 < 0 || vv1 > 100){
            vv1 = FinData.currentage+1;
            resetInput(retirementage,Integer.toString(vv1));
        }
        FinData.retirementage = vv1;

        vv1 = checkInputInt(life);
        if (vv1 < 0 || vv1 > 150){
            vv1 = FinData.retirementage + 1;
            resetInput(life,Integer.toString(vv1));
        }
        FinData.lifeexpectancy = vv1;

        vv1 = checkInputInt(expenses);
        if (vv1 < 0){
            resetInput(expenses,"0");
            vv1 = 1;
        }
        FinData.currentexpenses = vv1;

        vv1 = checkInputInt(savings);
        if (vv1 < 0){
            resetInput(savings,"0");
            vv1 = 1;
        }
        FinData.currentsavings = vv1;

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

        vv2 = checkInputDouble(inflation);
        if (vv2 < 0){
            resetInput(inflation,"1");
            vv2 = 1;
        }
        if (vv2 > 100){
            resetInput(inflation,"100");
            vv2 = 100;
        }
        FinData.inflation = vv2;
    }

    public void displayResults(){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        formatter.applyPattern("#,###,###,###");
        String formattedString;

        long tmp = (long)FinData.corpus;
        formattedString = formatter.format(tmp);
        result.setText(formattedString);
    }

    public void gotoBalanceStatement(View view){
        Intent intent = new Intent (this, RetirementStatement.class);
        startActivity(intent);
    }

    public void gotoBalanceStatementPlus(View view){
        Intent intent = new Intent (this, RetirementStatement.class);
        startActivity(intent);
    }
}
