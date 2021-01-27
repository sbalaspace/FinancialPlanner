package com.spacebargamestudios.financialplanner;

import java.net.FileNameMap;

public class FinCalc {

    public void SavingsToCorpusCalc(){
        double roi = FinData.roi / 1200.0;
        FinData.finalsavings = FinData.currentsavings;
        for (int ii =0; ii < FinData.period; ii++){
            for (int jj = 0; jj < 12; jj++) {
                FinData.finalsavings = FinData.finalsavings * (1 + roi);
            }
        }
    }

    public void LoanToEMICalc(){
        double roi = FinData.roi / 1200.0;
        double time = FinData.period*12;
        FinData.startingsip = FinData.corpus * roi * Math.pow(1+roi, time) / (Math.pow(1+roi, time) - 1);
        FinData.totalInvestment = FinData.startingsip * time;
        FinData.interestEarned = FinData.totalInvestment - FinData.corpus;
    }

    public void EMItoLoanCalc(){
        double roi = FinData.roi / 1200.0;
        double time = FinData.period*12;
        FinData.corpus = FinData.startingsip * (Math.pow(1+roi, time) - 1) / (roi * Math.pow(1+roi, time));
        FinData.totalInvestment = FinData.startingsip * time;
        FinData.interestEarned = FinData.totalInvestment - FinData.corpus;
    }

    public void SIPtoCorpusCalc(){
        double roi = FinData.roi / 1200.0;
        double sipfactor = FinData.sipfactor / 100.0;

        FinData.totalInvestment = 0;
        FinData.interestEarned = 0;
        FinData.corpus = 0;

        double sip = FinData.startingsip;
        double tmp;

        outerloop:
        for(int ii = 0; ii < FinData.period; ii++){
            for(int jj = 0; jj < 12; jj++){
                FinData.totalInvestment += sip;
                FinData.interestEarned += (FinData.totalInvestment + FinData.interestEarned) *roi;
                FinData.corpus = FinData.totalInvestment + FinData.interestEarned;
                if (FinData.corpus > 1e16){
                    break outerloop;
                }
            }
            sip *= (1+sipfactor);
        }
    }

    public void CorpusToSIPCalc(){
        double roi = FinData.roi / 1200.0;
        double sipfactor = FinData.sipfactor / 100.0;

        FinData.totalInvestment = 0;
        FinData.interestEarned = 0;
        double tmpCorpus = 0;

        double sip = 1;

        for(int ii = 0; ii < FinData.period; ii++){
            for(int jj = 0; jj < 12; jj++){
                FinData.totalInvestment += sip;
                FinData.interestEarned += (FinData.totalInvestment + FinData.interestEarned) *roi;
                tmpCorpus = FinData.totalInvestment + FinData.interestEarned;
            }
            sip *= (1+sipfactor);
        }

        sip = Math.ceil(FinData.corpus / tmpCorpus);
        FinData.startingsip = sip;
        FinData.totalInvestment = 0;
        FinData.interestEarned = 0;
        double tmp;
        for(int ii = 0; ii < FinData.period; ii++){
            for(int jj = 0; jj < 12; jj++){
                tmp = FinData.totalInvestment + FinData.interestEarned;
                if(tmp < FinData.corpus && tmp < 1e16){
                    FinData.totalInvestment += sip;
                    FinData.interestEarned += (FinData.totalInvestment + FinData.interestEarned) *roi;
                } else if (tmp < 1e16){
                    FinData.totalInvestment += 0;
                    FinData.interestEarned += (FinData.totalInvestment + FinData.interestEarned) *roi;
                } else {
                    FinData.totalInvestment += 0;
                    FinData.interestEarned += 0;
                }

            }
            sip *= (1+sipfactor);
        }
    }

    public FinTable[] LoanStatement(){
        int size = FinData.period*12;
        FinTable[] data = new FinTable[size];
        double roi = FinData.roi / 1200;

        int yr = 0;
        int mm = 1;

        double pos = FinData.corpus;
        for (int ii = 0; ii < size; ii++){
            FinTable row = new FinTable();
            row.year = yr;
            row.month = mm++;
            row.sip = FinData.startingsip;
            row.interest = pos * roi;
            row.principal = FinData.startingsip - row.interest;
            pos -= row.principal;
            row.finalCorpus = pos;
            data[ii] = row;
            if (mm > 11){
                mm = 1;
                yr++;
            }
        }

        return data;
    }

    public FinTable[] SIPtoCorpus_BalanceStatement(){
        int size = FinData.period*12;
        FinTable[] data = new FinTable[size];

        //SIPtoCorpusCalc();
        double roi = FinData.roi / 1200.0;
        double sipfactor = FinData.sipfactor / 100.0;

        double sip = FinData.startingsip;
        double totInv = 0;
        double totInt = 0;
        double tmp;
        for(int ii = 0; ii < FinData.period; ii++){
            for(int jj = 0; jj < 12; jj++){
                FinTable row = new FinTable();
                row.year = ii;
                row.month = jj;
                tmp = totInt + totInv;
                if (tmp < FinData.corpus && tmp < 1e16) {
                    row.sip = sip;
                    totInv += sip;
                    row.totalInvested = totInv;
                    totInt += (totInv + totInt) * roi;
                    row.totalInterest = totInt;
                    row.finalCorpus = totInv + totInt;
                } else if (tmp < 1e16){
                    row.sip = 0;
                    //totInv += sip;
                    row.totalInvested = totInv;
                    totInt += (totInv + totInt) * roi;
                    row.totalInterest = totInt;
                    row.finalCorpus = totInv + totInt;
                } else {
                    row.sip = 0;
                    row.totalInvested = totInv;
                    row.totalInterest = totInt;
                    row.finalCorpus = totInv + totInt;
                }
                    data[ii*12+jj] = row;
            }
            sip *= (1+sipfactor);
        }
        return data;
    }

    public FinTable[] RetirementStatement(){
        int size = (FinData.lifeexpectancy - FinData.currentage)*12;
        FinTable[] data = new FinTable[size];

        getCorpusReqd();
        double roi = FinData.roi / 1200.0;
        double sipfactor = FinData.sipfactor / 100.0;
        double inflation = FinData.inflation / 100.0;

        double sip = FinData.startingsip;
        double totInv = FinData.currentsavings;
        double totInt = 0;
        double expense = FinData.currentexpenses;
        FinData.period = FinData.retirementage - FinData.currentage;

        int cc = 0;
        for(int ii = 0; ii < FinData.period; ii++){
            for(int jj = 0; jj < 12; jj++){
                FinTable row = new FinTable();
                row.year = ii+FinData.currentage;
                row.month = jj;
                row.sip = sip;
                totInv += sip;
                row.totalInvested = totInv;
                row.finalCorpus = totInv + totInt;
                totInt += row.finalCorpus*roi;
                row.totalInterest = totInt;
                row.finalCorpus = totInv + totInt;
                row.expense = expense;
                data[cc] = row;
                cc++;
            }
            sip *= (1+sipfactor);
            expense *= (1+inflation);
        }

        FinData.period = FinData.lifeexpectancy - FinData.retirementage;
        double corpus = totInv+totInt;

        for(int ii =0; ii < FinData.period; ii++){
            for (int jj = 0; jj < 12; jj++){
                FinTable row = new FinTable();
                row.year = ii+FinData.retirementage;
                row.month = jj;
                row.totalInvested = 0;
                row.totalInterest = 0;

                corpus -= expense;
                corpus *= (1+roi);
                row.finalCorpus = corpus;
                row.expense = expense;
                data[cc] = row;
                cc++;
            }
            expense *= (1+inflation);
        }

        return data;
    }

    public void getFinalExpense(){
        FinData.expensefinal = FinData.currentexpenses;
        double inflation = FinData.inflation / 100.0;
        for (int ii = FinData.currentage; ii < FinData.lifeexpectancy-1; ii++){
            FinData.expensefinal *= (1+inflation);
        }
    }

    public void getCorpusReqd(){
        getFinalExpense();
        double expense = FinData.expensefinal;
        double roi = FinData.roi / 1200.0;
        double inflation = FinData.inflation / 100.0;

        double corpus = expense;
        for (int ii = 0; ii < 11; ii++){
            corpus = corpus/(1+roi);
            corpus += expense;
        }
        expense = expense/(1+inflation);

        for (int ii = FinData.lifeexpectancy - 2; ii >= FinData.retirementage; ii--){
            for (int jj = 0; jj < 12; jj++){
                corpus = corpus/(1+roi);
                corpus += expense;
            }
            expense = expense/(1+inflation);
        }

        FinData.period = FinData.retirementage - FinData.currentage;
        SavingsToCorpusCalc();

        FinData.corpus = corpus - FinData.finalsavings;
        if (FinData.corpus < 0){
            FinData.corpus = 0;
            FinData.startingsip = 0;
        } else {
            CorpusToSIPCalc();
        }
        FinData.corpus += FinData.finalsavings;
    }

    public void buyingOrRenting(){
        int size = FinData.period*12;
        double roi_inv = FinData.roi_inv / 1200.0;
        double inflation = FinData.inflation / 100.0;
        double rent = FinData.currentexpenses;
        double diff;

        FinData.buyercorpus = 0;
        FinData.rentercorpus = FinData.currentsavings;
        for(int ii = 0; ii < size; ii++){
            diff = FinData.startingsip - rent;
            if (diff > 0){
                FinData.rentercorpus += diff;
                FinData.rentercorpus *= (1+roi_inv);
                FinData.buyercorpus *= (1+roi_inv);
            } else {
                FinData.buyercorpus += (-diff);
                FinData.buyercorpus *= (1+roi_inv);
                FinData.rentercorpus *= (1+roi_inv);
            }
            if ((ii+1)%12 == 0){
                rent *= (1+inflation);
            }
        }
    }

    public FinTable[] BuyRentStatement(){
        int size = FinData.period*12;
        FinTable[] data = new FinTable[size];

        double roi_inv = FinData.roi_inv / 1200.0;
        double inflation = FinData.inflation / 100.0;
        double rent = FinData.currentexpenses;
        double diff;

        FinData.buyercorpus = 0;
        FinData.rentercorpus = FinData.currentsavings;
        int yr = 0;
        int month = 1;
        for(int ii = 0; ii < size; ii++){
            FinTable row = new FinTable();
            row.year = yr;
            row.month = month++;
            row.sip = FinData.startingsip;
            row.expense = rent;
            diff = FinData.startingsip - rent;
            if (diff > 0){
                row.principal = diff;
                FinData.rentercorpus += diff;
                FinData.rentercorpus *= (1+roi_inv);
                FinData.buyercorpus *= (1+roi_inv);
                row.totalInvested = FinData.rentercorpus;
                row.totalInterest = FinData.buyercorpus;
            } else {
                row.principal = -diff;
                FinData.buyercorpus += (-diff);
                FinData.buyercorpus *= (1+roi_inv);
                FinData.rentercorpus *= (1+roi_inv);
                row.totalInvested = FinData.rentercorpus;
                row.totalInterest = FinData.buyercorpus;
            }
            data[ii] = row;
            if ((ii+1)%12 == 0){
                rent *= (1+inflation);
                yr++;
                month = 1;
            }
        }
        return data;
    }
}
