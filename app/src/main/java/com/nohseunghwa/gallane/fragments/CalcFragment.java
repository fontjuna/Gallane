package com.nohseunghwa.gallane.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nohseunghwa.gallane.R;
import com.nohseunghwa.gallane.backing.Calculation;

import java.text.DecimalFormat;

import static com.nohseunghwa.gallane.backing.Constants.CALC_INPUT;
import static com.nohseunghwa.gallane.backing.Constants.CALC_PREVIUOS;
import static com.nohseunghwa.gallane.backing.Constants.CALC_RESULT;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalcFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = CalcFragment.class.getSimpleName();

    private TextView mResultTextView;
    private TextView mPreviuosTextView;
    private TextView mInputTextView;
    private ScrollView mScrollView;
    private String mInput;
    private String mPreviuos;
    private String mResult;
    private DecimalFormat df = new DecimalFormat("#,##0.######");
    private InputMethodManager imm;


    public CalcFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calc, container, false);
    }

    public void restoreResult() {
        imm.hideSoftInputFromWindow(mResultTextView.getWindowToken(), 0);
        restoreCurrentData();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInputTextView = (TextView) view.findViewById(R.id.input_text_view);
        mPreviuosTextView = (TextView) view.findViewById(R.id.previuos_text_view);
        mResultTextView = (TextView) view.findViewById(R.id.result_text_view);
        mScrollView = (ScrollView) view.findViewById(R.id.scroll_win);
        mInputTextView.setText(mInput);
        mPreviuosTextView.setText(mPreviuos);
        mResultTextView.setText(mResult);

        view.findViewById(R.id.button_0).setOnClickListener(this);
        view.findViewById(R.id.button_1).setOnClickListener(this);
        view.findViewById(R.id.button_2).setOnClickListener(this);
        view.findViewById(R.id.button_3).setOnClickListener(this);
        view.findViewById(R.id.button_4).setOnClickListener(this);
        view.findViewById(R.id.button_5).setOnClickListener(this);
        view.findViewById(R.id.button_6).setOnClickListener(this);
        view.findViewById(R.id.button_7).setOnClickListener(this);
        view.findViewById(R.id.button_8).setOnClickListener(this);
        view.findViewById(R.id.button_9).setOnClickListener(this);
        view.findViewById(R.id.button_dot).setOnClickListener(this);

        view.findViewById(R.id.button_sqrt).setOnClickListener(this);
        view.findViewById(R.id.button_pow).setOnClickListener(this);
        view.findViewById(R.id.button_twozero).setOnClickListener(this);
        view.findViewById(R.id.button_clear).setOnClickListener(this);
        view.findViewById(R.id.button_plus).setOnClickListener(this);
        view.findViewById(R.id.button_minus).setOnClickListener(this);
        view.findViewById(R.id.button_divide).setOnClickListener(this);
        view.findViewById(R.id.button_by).setOnClickListener(this);
        view.findViewById(R.id.button_left).setOnClickListener(this);
        view.findViewById(R.id.button_right).setOnClickListener(this);

        view.findViewById(R.id.button_del).setOnClickListener(this);
        view.findViewById(R.id.button_ac).setOnClickListener(this);
        view.findViewById(R.id.button_go).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String input;
        switch (view.getId()) {
            case R.id.button_go: {
                try {
                    input=mInput.replace("×","*");
                    input=input.replace("÷","/");
                    double val = Double.parseDouble(Calculation.Calculate(input));
                    mResult = (mResult + "\n" + mPreviuos).trim();
                    mResultTextView.setText(mResult);
                    mPreviuos = mInput + " = " + df.format(val);
                    mPreviuosTextView.setText(mPreviuos);
                    mInput = "0";
                    mInputTextView.setText(mInput);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "수식에 이상이 있습니다.", Toast.LENGTH_SHORT).show();
                    mResult = (mResult + "\n" + mPreviuos).trim();
                    mResultTextView.setText(mResult);
                    mPreviuos = mInput + " = error";
                    mPreviuosTextView.setText(mPreviuos);
                    mInput = "0";
                    mInputTextView.setText(mInput);
                }
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                break;
            }
            case R.id.button_del: {
                mInput = mInput.length() > 0 ? mInput.substring(0, mInput.length() - 1) : "";
                mInput = mInput.isEmpty() ? "0" : mInput;
                mInputTextView.setText(mInput);
                break;
            }
            case R.id.button_ac: {
                mResult = "";
                mResultTextView.setText(mResult);
                mPreviuos = "";
                mPreviuosTextView.setText(mPreviuos);
                mInput = "0";
                mInputTextView.setText(mInput);
                break;
            }
            case R.id.button_clear: {
                mInput = "0";
                mInputTextView.setText(mInput);
                break;
            }
            case R.id.button_pow: {
                mInput += "^";
                mInputTextView.setText(mInput);
                break;
            }
//            case R.id.button_twozero: {
//                mInput += "00";
//                mInputTextView.setText(mInput);
//                break;
//            }
            default: {
                if (mInput.equals("0")) {
                    mInput = "";
                }
                mInput += ((TextView) view).getText().toString();
                mInputTextView.setText(mInput);
                break;
            }
        }
        saveCurrentData();
    }

//    public void scrollToEnd(){
//        mScrollView.post(new Runnable() {
//            @Override
//            public void run() {
//                mScrollView.fullScroll(View.FOCUS_DOWN);
//            }
//        });
//
//    }

    private void saveCurrentData() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = message.edit();
        editor.putString(CALC_INPUT, mInput);
        editor.putString(CALC_PREVIUOS, mPreviuos);
        editor.putString(CALC_RESULT, mResult);
        editor.apply();
    }

    private void restoreCurrentData() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mInput = message.getString(CALC_INPUT, "0");
        mPreviuos = message.getString(CALC_PREVIUOS, "");
        mResult = message.getString(CALC_RESULT, "");
        mResultTextView.setText(mResult);
        mPreviuosTextView.setText(mPreviuos);
        mInputTextView.setText(mInput);
    }

}
