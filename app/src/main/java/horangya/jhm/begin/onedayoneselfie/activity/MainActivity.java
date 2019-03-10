package horangya.jhm.begin.onedayoneselfie.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import horangya.jhm.begin.onedayoneselfie.R;
import horangya.jhm.begin.onedayoneselfie.Utils;
import horangya.jhm.begin.onedayoneselfie.fragment.SelfieFragment;
import horangya.jhm.begin.onedayoneselfie.fragment.ScrollFragment;
import horangya.jhm.begin.onedayoneselfie.header.HeaderDesign;
import horangya.jhm.begin.onedayoneselfie.libs.material.MaterialViewPager;
import horangya.jhm.begin.onedayoneselfie.libs.material.MaterialViewPagerAnimator;
import horangya.jhm.begin.onedayoneselfie.libs.material.MaterialViewPagerHelper;
import horangya.jhm.begin.onedayoneselfie.libs.material.MaterialViewPagerSettings;


public class MainActivity extends DrawerActivity {

    //@BindView(R.id.materialViewPager)
    int cnt = 0;
    public static MaterialViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setResult(RESULT_OK);
        setTitle("");
        ButterKnife.bind(this);

        mViewPager = (MaterialViewPager)findViewById(R.id.materialViewPager);
        final Toolbar toolbar = mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        grantExternalStoragePermission();

        MobileAds.initialize(this, "ca-app-pub-3558679387182372/6269940865");
        FloatingActionButton  fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mViewPager.onRestoreInstanceState(mViewPager.onSaveInstanceState() );

            }
        });

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position % 2) {
                    case 0:
                        return SelfieFragment.newInstance();
                    case 1:
                        return ScrollFragment.newInstance();
                    default:
                        return ScrollFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return "MY MEMORY";
                    case 1:
                        return "MY EVERYTHING";
                }
                return "";
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                            R.color.colorPrimaryDark,
                                "https://travelblog.expedia.co.kr/wp-content/uploads/2017/02/0.jpg");
                            //"http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                    case 1:
                        ScrollFragment scrollFragment = new ScrollFragment();
                        if(Utils.myMemoryItemArrayList.size()>0
                                && cnt == 0) {
                            scrollFragment.updateList();
                            cnt++;
                        }
                        return HeaderDesign.fromColorResAndUrl(
                            R.color.colorPrimaryDark,
                            "https://travelblog.expedia.co.kr/wp-content/uploads/2016/03/1.jpg");

                }

                return null;
            }
        });

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getPagerTitleStrip().setTypeface(Typeface.createFromAsset(getAssets(),
                "Mohave.ttf"),0);
        mViewPager.getPagerTitleStrip().setTextColorResource(R.color.colorBase);
        final View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                    Toast.makeText(getApplicationContext(), "Yes, the title is clickable", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED  && grantResults[1] == PackageManager.PERMISSION_GRANTED ){
                //Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            }
        }
    }

    private boolean isBackKeyPressed = false;
    private final int MSG_TIMER_EXPIRED = 1;
    private long currentMillis = 0;

    @Override
    public void onBackPressed(){
        if(!isBackKeyPressed){
            isBackKeyPressed = true;
           // getAD();
           //// adad();
            adad();
            currentMillis = Calendar.getInstance().getTimeInMillis();
            startTimer();

        }else{
            isBackKeyPressed = false;
            if( Calendar.getInstance().getTimeInMillis() <=
                    currentMillis + 2000 ){
             //  startActivity(new Intent(MainActivity.this,AdActivity.class));
            }
        }
    }

    private void startTimer(){
        backTimeHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED,2000);
    }
    private Handler backTimeHandler = new Handler(){
        public  void handleMessage(Message msg){
            switch (msg.what){
                case MSG_TIMER_EXPIRED:{
                    isBackKeyPressed = false;
                }
                break;
            }
        }
    };



    private InterstitialAd mInterstitialAd;
    void adad() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.adId));
        AdRequest adRequest1 = new AdRequest.Builder().build();
        //AdRequest adRequest1 = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest1);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                finish();
                Log.i("dddddbbb", i + "");
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                displayFullAd();
            }
        });
    }

    private void displayFullAd(){
        if(mInterstitialAd !=null && mInterstitialAd.isLoaded())
            mInterstitialAd.show();
        else finish();
    }

}
