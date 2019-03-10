package horangya.jhm.begin.onedayoneselfie.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import horangya.jhm.begin.onedayoneselfie.R;

import android.location.Address;
import android.widget.Toast;

import horangya.jhm.begin.onedayoneselfie.dummy.MyMemory.MyMemoryItem;
import horangya.jhm.begin.onedayoneselfie.Utils;

public class IntroActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public AVLoadingIndicatorView indicatorView;
    private static Typeface typeface;
    Handler handlerIntro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        indicatorView= (AVLoadingIndicatorView) findViewById(R.id.indicator);

        try{
            if(typeface == null) {
                typeface = Typeface.createFromAsset(getAssets(),
                        "Mohave.ttf");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setTypeface(typeface);
        //grantExternalStoragePermission();


        //인터넷에 연결돼 있나 확인
        ConnectivityManager connect = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if (connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() ==NetworkInfo.State.CONNECTED
                ||connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() ==NetworkInfo.State.CONNECTED )
        { } else {
            Toast.makeText(this,"인터넷 연결을 해주세요!",Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= 23) {

                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {


                    File root = android.os.Environment.getExternalStorageDirectory();//원하는 폴더를 만들기위해 작업
                    File dir = new File (root.getAbsolutePath() + "/MyMemory/Pictures");//이렇게하면 mnt/sdcard/Test 라는 경로가 됩니다.
                    if(!dir.exists()) { //폴더가 존재하지 않으면
                        dir.mkdirs();      //생성!
                    }
                    if(!dir.exists()) { //폴더가 존재하지 않으면
                        dir.mkdirs();      //생성!
                    }

                    resultAction();

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1);
                }
        }
        return;
    }

//


    public boolean makePicDir(){//존재하지 않는 Dir 만들기

        try {
            String rootPath = Environment.getExternalStorageDirectory().toString();
            File root = new File(rootPath + "/MyMemory/Pictures");
            root.mkdirs();
        }catch (Exception e){
            return false;
        }
        return true;
    }



    public void updateMyMemoryList(){

        Utils.myMemoryItemArrayList = new ArrayList<>();


        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyMemory/Pictures";
        File directory = new File(path);
        File[] files = directory.listFiles();

        Utils.myMemoryItemArrayList = new ArrayList<>();

        Log.i("intro_path",path);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {

                String id = files[i].getName().substring(0, 8);
                String imgUrl = files[i].getAbsolutePath();

                String address = "";
                String dateTime = "";
                int d1 = 0, d2 = 0;

                if (Build.VERSION.SDK_INT >= 24) {

                    List<Address> addressList = null;
                    Uri uri = Uri.fromFile(files[i]);
                    try {
                        InputStream in = getContentResolver().openInputStream(uri);
                        ExifInterface exifInterface = new ExifInterface(in);
                        in.close();
                        geoDegree geo = new geoDegree(exifInterface);

                        d1 = geo.getLatitudeE6();
                        d2 = geo.getLongitudeE6();

                        if (geo.getImageTime() != null)
                            dateTime = geo.getImageTime();
                        else
                            dateTime = id.substring(0, 4) + ";" + id.subSequence(4, 6) + ";" + id.substring(6, 8);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    try {

                        final Geocoder geocoder = new Geocoder(this);

                        addressList = geocoder.getFromLocation(
                                d1, // 위도
                                d2, // 경도
                                10); // 얻어올 값의 개수
                        if (addressList != null) {
                            if (addressList.size() == 0) {
                                address = "장소 정보 없음";
                            } else {
                                address = addressList.get(0).toString();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Log.i("intro_size", Utils.myMemoryItemArrayList.size() + "");
                MyMemoryItem item = new MyMemoryItem(id, imgUrl, address, dateTime);
                Utils.myMemoryItemArrayList.add(item);
            }
        }

    }


    void startAnim(){
        indicatorView.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        indicatorView.hide();
        // or avi.smoothToHide();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED  && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED ){
                //Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                //resume tasks needing this permission
            }else{
                finish();
            }
        }
        resultAction();
    }

    private void resultAction(){
        startAnim();

        updateMyMemoryList();

        SharedPreferences pref = getSharedPreferences("startingDate" , MODE_PRIVATE);
        String str = pref.getString("startingDate" ,  null);

        if(str == null){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("startingDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString() );
            //editor.putString("startingDate","2018-11-01" );
            editor.commit();
        }

        Utils.startingDate = pref.getString("startingDate" ,  null);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                stopAnim();
                startActivity(new Intent(IntroActivity.this , MainActivity.class));
                finish();
                stopAnim();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            }
        };

        handlerIntro = new Handler();
        handlerIntro.postDelayed(runnable,2000);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[],int[] grantResuts){
////        switch (requestCode){
////            case CAMERA
////        }
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                return;
//            }
//        }
//
//    }


}

