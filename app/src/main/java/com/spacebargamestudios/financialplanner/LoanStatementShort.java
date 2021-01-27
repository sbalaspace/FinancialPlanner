package com.spacebargamestudios.financialplanner;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.util.Objects;

public class LoanStatementShort extends AppCompatActivity {

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.balancestatement);

        mProgressBar = new ProgressDialog(this);

        // setup the table
        mTableLayout = (TableLayout) findViewById(R.id.balancestatement);

        mTableLayout.setStretchAllColumns(true);

        startLoadData();
    }

    public void startLoadData() {

        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Computing");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
        new LoadDataTask().execute(0);
    }

    public void loadData(int freq){

        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;
        int smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);

        double emi_p = 0;
        double emi_i = 0;
        //int freq = 12;

        FinCalc calc = new FinCalc();
        FinTable[] data = calc.LoanStatement();

        DecimalFormat decimalFormat = new DecimalFormat("   #,###,###,###");

        int rows = data.length;
        Objects.requireNonNull(getSupportActionBar()).setTitle("Balance Sheet (" + String.valueOf(rows/freq) + " rows)" );
        TextView textSpacer = null;

        mTableLayout.removeAllViews();

        for(int ii = -1; ii < rows; ii++){
            FinTable row = null;

            if( ii > -1){
                row = data[ii];
            }else{
                textSpacer = new TextView(this);
                textSpacer.setText("");
            }

            /**************************************************************************************/
            final TextView tv = new TextView((this));
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv.setGravity(Gravity.START);
            tv.setPadding(5, 15, 0, 15);
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv.setTextColor(Color.parseColor("#000000"));
            if (ii == -1){
                tv.setGravity(Gravity.CENTER);
                tv.setText("Time");
                tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else if ((ii+1)%freq == 0) {
                tv.setText(String.valueOf(row.year +"y " + (row.month+1) +"m "));
                if ((ii+1)%12 == 0){
                    tv.setText(String.valueOf(row.year +"y "));
                }
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }

            /**************************************************************************************/

            final TextView tv1 = new TextView((this));
            tv1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv1.setGravity(Gravity.RIGHT);
            tv1.setPadding(5, 15, 0, 15);
            tv1.setTypeface(null, Typeface.BOLD);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv1.setTextColor(Color.parseColor("#000000"));
            if (ii == -1){
                tv1.setGravity(Gravity.CENTER);
                tv1.setText("EMI");
                tv1.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else if ((ii+1)%freq == 0) {
                tv1.setText(decimalFormat.format(row.sip*12));
                tv1.setTextColor(Color.parseColor("#0000ff"));
                tv1.setBackgroundColor(Color.parseColor("#f7f7f7"));
            }

            /**************************************************************************************/

            final TextView tv2 = new TextView((this));
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv2.setGravity(Gravity.RIGHT);
            tv2.setPadding(5, 15, 0, 15);
            tv2.setTypeface(null, Typeface.BOLD);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv2.setTextColor(Color.parseColor("#000000"));
            if (ii == -1){
                tv2.setGravity(Gravity.CENTER);
                tv2.setText("  EMI Interest  ");
                tv2.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else if ((ii+1)%freq == 0) {
                emi_i += row.interest;
                tv2.setText(decimalFormat.format(emi_i));
                tv2.setTextColor(Color.parseColor("#0000ff"));
                tv2.setBackgroundColor(Color.parseColor("#f8f8f8"));
                emi_i = 0;
            } else {
                emi_i += row.interest;
            }

            /**************************************************************************************/

            final TextView tv3 = new TextView((this));
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv3.setGravity(Gravity.RIGHT);
            tv3.setPadding(5, 15, 0, 15);
            tv3.setTypeface(null, Typeface.BOLD);
            tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv3.setTextColor(Color.parseColor("#000000"));
            if (ii == -1){
                tv3.setGravity(Gravity.CENTER);
                tv3.setText("  EMI Principal  ");
                tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else if ((ii+1)%freq == 0) {
                emi_p += row.principal;
                tv3.setText(decimalFormat.format(emi_p));
                tv3.setTextColor(Color.parseColor("#1c7f30"));
                tv3.setBackgroundColor(Color.parseColor("#f7f7f7"));
                emi_p = 0;
            }else {
                emi_p += row.principal;
            }

            /**************************************************************************************/

            final TextView tv4 = new TextView((this));
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            tv4.setGravity(Gravity.RIGHT);
            tv4.setPadding(5, 15, 0, 15);
            tv4.setTypeface(null, Typeface.BOLD);
            tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
            tv4.setTextColor(Color.parseColor("#000000"));
            if (ii == -1){
                tv4.setGravity(Gravity.CENTER);
                tv4.setText("  Loan Balance  ");
                tv4.setBackgroundColor(Color.parseColor("#f0f0f0"));
            } else if ((ii+1)%freq == 0) {
                tv4.setText(decimalFormat.format(row.finalCorpus));
                tv4.setTextColor(Color.parseColor("#8b008b"));
                tv4.setBackgroundColor(Color.parseColor("#f8f8f8"));
            }

            if (ii == -1 || (ii+1)%freq == 0) {
                final TableRow tr = new TableRow(this);
                tr.setId(ii + 1);
                TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                tr.setPadding(0, 0, 0, 0);
                tr.setLayoutParams(trParams);


                tr.addView(tv);
                tr.addView(tv1);
                tr.addView(tv2);
                tr.addView(tv3);
                tr.addView(tv4);

                mTableLayout.addView(tr, trParams);
            }
        }
    }

    class LoadDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {

            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            mProgressBar.hide();
            loadData(12);
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
