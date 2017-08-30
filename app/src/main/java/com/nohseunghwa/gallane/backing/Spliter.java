package com.nohseunghwa.gallane.backing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.regex.Pattern;

import static com.nohseunghwa.gallane.backing.Constants.ERROR_EMPTY_INPUT;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_INVALID;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_IN_DONT_DIVIDE;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_IN_MEMBER;
import static com.nohseunghwa.gallane.backing.Constants.ERROR_WRONG_EXPRESSION;
import static com.nohseunghwa.gallane.backing.Constants.ITEMnITEM;
import static com.nohseunghwa.gallane.backing.Constants.LEFTnRIGHT;
import static com.nohseunghwa.gallane.backing.Constants.MEMBER2MEMBER;
import static com.nohseunghwa.gallane.backing.Constants.MEMBERnMEMBER;
import static com.nohseunghwa.gallane.backing.Constants.MEMBERnRATIO;
import static com.nohseunghwa.gallane.backing.Constants.TITLEnMONEY;
import static com.nohseunghwa.gallane.backing.Constants.VALID_CHARACTERS_MEMBER;
import static java.lang.Double.parseDouble;


/**
 * Created by fontjuna on 2017-08-29.
 */

public class Spliter {
//    private static Spliter spliter;

    private boolean mError; // 입력 데이타 검사 결과

    private int mUnit;
    private String mData;   // 받은 텍스트
    private String mResult; // 보낼 텍스트 (결과)

    private ArrayList<Title> mDatas;    // 받은 데이타 구조화
    private ArrayList<Title> mResults;  // 보낼 데이타 구조화

    private Title mFinal;    // 최종 집계 금액 데이타

    //    private ArrayList<String> mToken; // 작업용, 입력 데이타를 각 원소로 분리
    private ArrayList<ArrayList<String>> mTokens; // 작업용, 입력 데이타를 각 원소로 분리
    private DecimalFormat df = new DecimalFormat("#,##0");

    public Spliter(String input, int unit) {
        excute(input, unit);
    }

    // Procedure( Methods )
    //=========================================================================================//
    private void excute(String data, int unit) {
        mData = data;
        mUnit = unit;
        intializeData();
        confirmData();
        if (!isError()) {
            splitData();
        }
    }

    private void splitData() {
        String[] element = mData.split(ITEMnITEM);
        for (String s : element) {
            mTokens.add(splitTokens(s, TITLEnMONEY + LEFTnRIGHT));// + MEMBERnMEMBER));
        }
        if (mTokens.isEmpty()) {
            mError = true;
            mResult = ERROR_EMPTY_INPUT;
        }
        if (!isError()) {
            mDatasCreate();         // make a mDatas
            if (!isError()) {
                mResultsCreate();   // make a mResults
                if (!isError()) {
                    createText();   // make a mResult
                }
            }
        }
    }

    private void createText() {
        int gather = 0;
        int intVal = 0;
        String member = "";
        for (String key : mFinal.keySet()) {
            intVal = ((int) (mFinal.get(key) + mUnit * 0.9) / mUnit) * mUnit;
            gather += intVal;
            member += key + " : " + intVal + "\n";
        }
        mResult = mFinal.getTitle() + " :\n"
                + "총 금 액 : " + df.format((int) (mFinal.getAmount())) + "\n"
                + "계산단위 : " + df.format(mUnit) + "\n"
                + "걷는금액 : " + df.format(gather) + "\n"
                + "남는금액 : " + df.format(gather - (int) mFinal.getAmount()) + "\n\n"
                + member;
    }

    private void mResultsCreate() {
        for (Title data : mDatas) {
            double amount = 0.0;
            double ratio = 0.0;
            double unitPrice = 0.0;
            Title result = new Title();
            result.setTitle(data.getTitle());
            result.setAmount(data.getAmount());
            amount += data.getAmount();
            for (String key : data.keySet()) {
                ratio += data.get(key);
            }
            unitPrice = amount / ratio;
            for (String key : data.keySet()) {
                result.put(key, data.get(key) * unitPrice);
            }
            mResults.add(result);
        }
        for (Title t : mResults) {
            if (mFinal.getTitle().isEmpty()) {
                mFinal.setTitle(t.getTitle());
            }
            mFinal.setAmount(mFinal.getAmount() + t.getAmount());
            for (String key : t.keySet()) {
                mFinal.put(key, (mFinal.getData().containsKey(key) ? mFinal.get(key) : 0.0) + t.get(key));
            }
        }
    }

    private void mDatasCreate() {
        String head;
        double amount;
        int position;
        int no = 1;

        for (ArrayList<String> tokenList : mTokens) {
            head = "DutchPay" + no++;
            amount = 0.0;
            TreeMap<String, Double> ratio = new TreeMap<>();
            position = tokenList.indexOf(TITLEnMONEY);
            if (position > 0) {
                head = tokenList.get(position - 1);
            }
            position = tokenList.indexOf(LEFTnRIGHT);
            if (position < 1) {
                mError = true;
                mResult = ERROR_IN_DONT_DIVIDE;
            } else {
                try {
                    amount = parseDouble(Calculation.Calculate(tokenList.get(position - 1)));
                } catch (RuntimeException e) {
                    mError = true;
                    mResult = ERROR_INVALID;
                }
                if (Pattern.matches(VALID_CHARACTERS_MEMBER, tokenList.get(position + 1))) {
                    ratio = makeMemberAndRatio(tokenList.get(position + 1));
                } else {
                    mError = true;
                    mResult = ERROR_IN_MEMBER;
                }
            }
            if (!isError()) {
                mDatas.add(new Title(head, amount, ratio));
            }
        }
    }

    // Member ratio
    private TreeMap<String, Double> makeMemberAndRatio(String memberAndRatio) {
        int nFrom;
        int nTo;
        String[] fromTo;
        double rate;
        String sRate;
        String[] element;
        TreeMap<String, Double> ratio = new TreeMap<>();
        ArrayList<String> member = splitTokens(memberAndRatio, MEMBERnMEMBER);
        for (int i = 0; i < member.size(); i++) {
            if (!MEMBERnMEMBER.equals(member.get(i))) { // ","가 아니면
                if (member.get(i).contains(MEMBER2MEMBER)) {
                    element = splitElement(member.get(i)); // 배율만 구하기 위해 임시전용
                    sRate = MEMBERnRATIO + element[1];
                    fromTo = element[0].split(MEMBER2MEMBER);
                    nFrom = Integer.parseInt(fromTo[0]);
                    nTo = Integer.parseInt(fromTo[1]);
                    for (int j = nFrom; j <= nTo; j++) {
                        element = splitElement(j + sRate);
                        rate = 0.0;
                        if (ratio.containsKey(element[0])) {
                            rate = ratio.get(element[0]);
                        }
                        ratio.put(element[0], rate + parseDouble(element[1]));
                    }
                } else {
                    element = splitElement(member.get(i));
                    rate = 0.0;
                    if (ratio.containsKey(element[0])) {
                        rate = ratio.get(element[0]);
                    }
                    ratio.put(element[0], rate + parseDouble(element[1]));
                }
            }
        }
        return ratio;
    }


    // split to member and ratio
    private String[] splitElement(String element) {
        String[] item = element.split(MEMBERnRATIO);
        String[] items = {"", ""};
        if (item.length < 1) {
            mError = true;
            mResult = ERROR_IN_MEMBER;
        } else if (item.length < 2) {
            items[0] = item[0];
            items[1] = "1.0";
        } else {
            items = item;
        }
        return items;
    }

    // split to token from input data
    private ArrayList<String> splitTokens(String data, String delimeter) {
        ArrayList<String> tokenList = new ArrayList<>();
        String token = "";
        for (int i = 0; i < data.length(); i++) {
            if (delimeter.contains(data.substring(i, i + 1))) {
                if (!token.isEmpty()) {
                    tokenList.add(token);
                    token = "";
                }
                tokenList.add(data.substring(i, i + 1));
            } else {
                token += data.substring(i, i + 1);
            }
        }
        if (!token.isEmpty()) {
            tokenList.add(token);
        }
        return tokenList;
    }

    // intialize and check validaiton
    //=========================================================================================//
    private void intializeData() {
        mError = false;
        mDatas = new ArrayList<>();
        mResult = "";
        mResults = new ArrayList<>();
        mTokens = new ArrayList<ArrayList<String>>();
        mFinal = new Title();
    }

    private void confirmData() {
        mData = mData.replace(" ", "");
        mError = false;//!Pattern.matches(VALID_CHARACTERS_ALL, mData);
        if (isError()) {
            mResult = ERROR_WRONG_EXPRESSION;
        }
    }

    // Getters
    //=========================================================================================//
    public boolean isError() {
        return mError;
    }

    public String getData() {
        return mData;
    }

    public String getResult() {
        return mResult;
    }

    public ArrayList<Title> getDatas() {
        return mDatas;
    }

    public ArrayList<Title> getResults() {
        return mResults;
    }

    // Construstor
    //=========================================================================================//
//    public void Spliter(String data, int unit) {
//        excute(data, unit);
//    }

    //    private static Spliter getInstance() {
//        if (spliter == null) {
//            spliter = new Spliter();
//        }
//        return spliter;
//    }
//
//    public void Compute(String data, int unit) {
//        Spliter.getInstance().excute(data, unit);
//    }
    //=========================================================================================//
    private int getDigits(int amount) {
        return df.format(amount).length();
    }

    private String padNum(double num, int n) {
        return padLeft(df.format(num), n);
    }

    private static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s).replace(" ", "_");
    }
}
