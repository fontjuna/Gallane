package com.nohseunghwa.gallane.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nohseunghwa.gallane.R;

import static com.nohseunghwa.gallane.backing.Constants.INPUT_EXPRESSION;
import static com.nohseunghwa.gallane.backing.Constants.NO_BANKING;

public class SendFragment extends Fragment {

    public static final String TAG = SendFragment.class.getSimpleName();
    TextView mMsgText;
    String mMessage;
    private InputMethodManager imm;

    public SendFragment() {
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
        Log.d(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    private void saveCurrentData(){
    }

    private void restoreCurrentData(){
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mMessage = message.getString(INPUT_EXPRESSION, "");
        mMsgText.setText(mMessage + makeBankingText(true));
    }

    public void restoreResult() {
        imm.hideSoftInputFromWindow(mMsgText.getWindowToken(), 0);
        restoreCurrentData();
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: ");
        mMsgText = (TextView) view.findViewById(R.id.content_text);

        Button sendButton = (Button) view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMessage.isEmpty()) {
                    Toast.makeText(getActivity(), "보낼 내용이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    sendingSms();
                }
            }
        });

        Button previewButton = (Button) view.findViewById(R.id.preview_button);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewSms();
                imm.hideSoftInputFromWindow(mMsgText.getWindowToken(), 0);
            }
        });
    }

    private void previewSms() {
        mMsgText.setText(mMessage + makeBankingText(true));
    }

    private void sendingSms() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mMessage + makeBankingText(false));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private String makeBankingText(boolean append) {
        EditText nameText = (EditText) getView().findViewById(R.id.name_text);
        EditText bankText = (EditText) getView().findViewById(R.id.bank_text);
        EditText accountText = (EditText) getView().findViewById(R.id.account_text);

        String name = nameText.getText().toString();
        String bank = bankText.getText().toString();
        String account = accountText.getText().toString();
        boolean yn = (name.isEmpty() || bank.isEmpty() || account.isEmpty());

        String message = (yn ? append ? NO_BANKING : "" :
                "\n\n▣ 입금 계좌"
                        + "\n예 금 주 : " + name + "\n은 행 명 : "
                        + bank + "\n계좌번호 : " + account);

        return message;

    }

}
