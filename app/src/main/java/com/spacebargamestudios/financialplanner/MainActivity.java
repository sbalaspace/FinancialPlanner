package com.spacebargamestudios.financialplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button retirementplanner = findViewById(R.id.button_retirementplanner);
        Button buyingvsrenting = findViewById(R.id.button_buyingvsrenting);
        /*Button LoanToEMI = findViewById(R.id.button_loan2emi);
        Button EMItoLoan = findViewById(R.id.button_emi2loan);
        Button siptocorpus = findViewById(R.id.button_sip2corpus);
        Button corpustosip = findViewById(R.id.button_corpus2sip);*/
        Button corpusorsip = findViewById(R.id.button_corpus_sip);
        Button loanoremi = findViewById(R.id.button_loan_emi);

        retirementplanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRetirementPlanner(view);
            }
        });

        buyingvsrenting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoBuyingVsRenting(view);
            }
        });

        /*LoanToEMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLoan2EMI(view);
            }
        });

        EMItoLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoEMI2Loan(view);
            }
        });

        siptocorpus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSIPtoCropus(view);
            }
        });

        corpustosip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCorpustoSIP(view);
            }
        });*/

        corpusorsip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoCorpusOrSIP(view);
            }
        });

        loanoremi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLoanOrEMI(view);
            }
        });
    }

    public void gotoRetirementPlanner(View view){
        Intent intent = new Intent(this, RetirementPlanner.class);
        startActivity(intent);
    }

    public void gotoBuyingVsRenting(View view){
        Intent intent = new Intent(this, BuyingVsRenting.class);
        startActivity(intent);
    }

    public void gotoLoan2EMI(View view){
        Intent intent = new Intent(this, LoanToEMI.class);
        startActivity(intent);
    }

    public void gotoEMI2Loan(View view){
        Intent intent = new Intent(this, EMItoLoan.class);
        startActivity(intent);
    }

    public void gotoSIPtoCropus(View view){
        Intent intent = new Intent(this, SIPtoCorpus.class);
        startActivity(intent);
    }

    public void gotoCorpustoSIP(View view){
        Intent intent = new Intent(this, CorpustoSIP.class);
        startActivity(intent);
    }

    public void gotoCorpusOrSIP(View view){
        Intent intent = new Intent(this, CorpusOrSIP.class);
        startActivity(intent);
    }

    public void gotoLoanOrEMI(View view){
        Intent intent = new Intent(this, LoanOrEMI.class);
        startActivity(intent);
    }
}

