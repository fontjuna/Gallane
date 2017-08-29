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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nohseunghwa.gallane.R;
import com.nohseunghwa.gallane.backing.Galler;

import static com.nohseunghwa.gallane.backing.Constants.HINT_EXPRESSION;
import static com.nohseunghwa.gallane.backing.Constants.HINT_INFORMATION;
import static com.nohseunghwa.gallane.backing.Constants.INPUT_EXPRESSION;

public class KidsFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = KidsFragment.class.getSimpleName();
    private TextView mResultTextView;
    private EditText mInputEditText;
    private String mInput = "";
    private String mResult = "";
    private int mUnit = 0;
    private int mChoice = -1;
    private InputMethodManager imm;

    public KidsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kids, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mInputEditText = (EditText) getView().findViewById(R.id.input_edit_text);
        mResultTextView = (TextView) view.findViewById(R.id.result_text_view);
        mResultTextView.setHint(HINT_INFORMATION);

        view.findViewById(R.id.calc_button).setOnClickListener(this);
        view.findViewById(R.id.init_button).setOnClickListener(this);
        view.findViewById(R.id.add_button_at).setOnClickListener(this);
        view.findViewById(R.id.add_button_bat).setOnClickListener(this);
        view.findViewById(R.id.add_button_comma).setOnClickListener(this);
        view.findViewById(R.id.add_button_slash).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        boolean skip = true;
        switch (v.getId()) {
            case R.id.calc_button:
                calcEditText();
                skip = false;
                break;
            case R.id.init_button:
                initEditText();
                skip = false;
                break;
            case R.id.add_button_at:
            case R.id.add_button_bat:
            case R.id.add_button_comma:
            case R.id.add_button_slash:
                addKeyboard(v);
                break;
        }
        if (!skip) {
            saveCurrentData();
            imm.hideSoftInputFromWindow(mResultTextView.getWindowToken(), 0);
        }
    }

    private void addKeyboard(View v) {
        mInput = mInputEditText.getText().toString() + ((TextView) v).getText().toString();
        mInputEditText.setText(mInput);
        mInputEditText.setSelection(mInputEditText.length());
    }

    private void calcEditText() {
        mInputEditText = (EditText) getView().findViewById(R.id.input_edit_text);
        Galler cal = new Galler(mInputEditText.getText().toString(), getUnit());
        mResult = cal.getTextResult();
        mResultTextView.setText(mResult);
    }

    private void initEditText() {
        mResult = "";
        mInputEditText.setText("");
        mResultTextView.setText("");
        mInputEditText.setHint(HINT_EXPRESSION);
        mResultTextView.setHint(HINT_INFORMATION);
    }

    public void restoreResult() {
        imm.hideSoftInputFromWindow(mResultTextView.getWindowToken(), 0);
        restoreCurrentData();
    }

    private void saveCurrentData() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = message.edit();
        editor.putString(INPUT_EXPRESSION, mResult);
        editor.apply();
    }

    private void restoreCurrentData() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mResult = message.getString(INPUT_EXPRESSION, "");
        mResultTextView.setText(mResult);
    }

    private int getUnit() {
        RadioGroup unitRadioGroup = (RadioGroup) getView().findViewById(R.id.unit_radio_group);
        int radioButtonId = unitRadioGroup.getCheckedRadioButtonId();
        int unit;
        if (radioButtonId == -1) {
            unit = 10;
        } else {
            RadioButton unitRadioButton = (RadioButton) getView().findViewById(radioButtonId);
            unit = Integer.parseInt(unitRadioButton.getText().toString());
        }
        return unit;
    }

}
