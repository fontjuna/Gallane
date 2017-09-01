package com.nohseunghwa.gallane;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.nohseunghwa.gallane.fragments.CalcFragment;
import com.nohseunghwa.gallane.fragments.KidsFragment;
import com.nohseunghwa.gallane.fragments.SendFragment;

import static com.nohseunghwa.gallane.backing.Constants.CALC_INPUT;
import static com.nohseunghwa.gallane.backing.Constants.CALC_PREVIUOS;
import static com.nohseunghwa.gallane.backing.Constants.CALC_RESULT;
import static com.nohseunghwa.gallane.backing.Constants.HINT_INFORMATION;
import static com.nohseunghwa.gallane.backing.Constants.INPUT_EXPRESSION;
import static com.nohseunghwa.gallane.backing.Constants.TAB_TITLE_0;
import static com.nohseunghwa.gallane.backing.Constants.TAB_TITLE_2;
import static com.nohseunghwa.gallane.backing.Constants.TAB_TITLE_3;

public class GallaActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galla);


        MobileAds.initialize(this, "ca-app-pub-3056892491225323~4989059619");


        mAdView = (AdView) findViewById(R.id.adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        clearSharedPreference();
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
//        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    KidsFragment fragment = (KidsFragment) adapter.getItem(position);
                    fragment.restoreResult();
                } else if (position == 1) {
//                    PapaFragment fragment = (PapaFragment) adapter.getItem(position);
//                    fragment.restoreResult();
//                } else if (position == 2) {
                    SendFragment fragment = (SendFragment) adapter.getItem(position);
                    fragment.restoreResult();
                } else if (position == 2) {
                    CalcFragment fragment = (CalcFragment) adapter.getItem(position);
                    fragment.restoreResult();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_gallane, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.help_gallane, null, false);
        ((TextView) view.findViewById(R.id.help_text)).setText(HINT_INFORMATION);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        view.findViewById(R.id.button_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return true;
    }

    private void clearSharedPreference() {
        SharedPreferences message = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = message.edit();
        editor.putString(INPUT_EXPRESSION, "");
        editor.putString(CALC_INPUT, "0");
        editor.putString(CALC_PREVIUOS, "");
        editor.putString(CALC_RESULT, "");
        editor.apply();
    }

    private static class MyPagerAdapter extends FragmentStatePagerAdapter {

        public static final int PAGE_NUM = 3;
        private KidsFragment mKidsFragment;
        //        private PapaFragment mPapaFragment;
        private SendFragment mSendFragment;
        private CalcFragment mCalcFragment;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            mKidsFragment = new KidsFragment();
//            mPapaFragment = new PapaFragment();
            mSendFragment = new SendFragment();
            mCalcFragment = new CalcFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return mKidsFragment;
                case 1:
//                    return mPapaFragment;
//                case 2:
                    return mSendFragment;
                case 2:
                    return mCalcFragment;
            }
            return mKidsFragment;
        }

        @Override
        public int getCount() {
            return PAGE_NUM;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: {
                    return TAB_TITLE_0;
                }
                case 1: {
//                    return TAB_TITLE_1;
//                }
//                case 2: {
                    return TAB_TITLE_2;
                }
                case 2: {
                    return TAB_TITLE_3;
                }
            }
            return super.getPageTitle(position);
        }
    }

}
