package horangya.jhm.begin.onedayoneselfie.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kelin.calendarlistview.library.CalendarHelper;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import horangya.jhm.begin.onedayoneselfie.R;
import horangya.jhm.begin.onedayoneselfie.Utils;
import horangya.jhm.begin.onedayoneselfie.activity.ImageViewerActivity;
import horangya.jhm.begin.onedayoneselfie.activity.IntroActivity;
import horangya.jhm.begin.onedayoneselfie.activity.MainActivity;
import horangya.jhm.begin.onedayoneselfie.adapter.MyMemoryRecyclerViewAdapter;
import horangya.jhm.begin.onedayoneselfie.dummy.MyMemory;
import horangya.jhm.begin.onedayoneselfie.dummy.MyMemory.MyMemoryItem;
import horangya.jhm.begin.onedayoneselfie.header.MaterialViewPagerHeaderDecorator;
import okhttp3.internal.Util;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static horangya.jhm.begin.onedayoneselfie.Utils.REQUEST_IMAGE_CAPTURE;
import static horangya.jhm.begin.onedayoneselfie.Utils.RESULT_LOAD_IMAGE;
import static horangya.jhm.begin.onedayoneselfie.Utils.setGlobalFont;
import static horangya.jhm.begin.onedayoneselfie.Utils.typeface;
import static horangya.jhm.begin.onedayoneselfie.activity.MainActivity.mViewPager;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class SelfieFragment extends Fragment {

    private static final boolean GRID_LAYOUT = false;
    private static int ITEM_COUNT = 0;

    ImageView selectedImg;
    RequestOptions requestOptions = new RequestOptions();

    String mCurrentPhotoPath;

    static public  MyMemoryRecyclerViewAdapter myMemoryRecyclerViewAdapter;
    static public RecyclerView mRecyclerView;
    ArrayList<MyMemoryItem> items = new ArrayList<>();
    String startDate;
    int selectedPosition = 1;

    public static SelfieFragment newInstance() {
        return new SelfieFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        try{
            if(typeface == null) {
                typeface = Typeface.createFromAsset(getActivity().getAssets(),
                        "Mohave.ttf");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setGlobalFont(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getActivity().setResult(RESULT_OK);


        requestOptions.placeholder(R.drawable.ic_btn_calendar_heart_down);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.error(R.drawable.ic_btn_calendar_heart_down);
        requestOptions.skipMemoryCache(true);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        updateList();
        setUpList();

    }


    @Override
    public void onStart() {
// 어댑터 갱신

        if(myMemoryRecyclerViewAdapter != null)
            myMemoryRecyclerViewAdapter.notifyDataSetChanged();

        // 그리드뷰 갱신

        //if(mRecyclerView != null)
            //mRecyclerView.(null);

        super.onStart();
    }


    @Override
    public void onStop() {

        try {
            Utils.unbindDrawables(mRecyclerView);
        } catch (Exception e) {
            // TODO: handle exception
        }
        super.onStop();
    }
    private void clearApplicationCache(java.io.File dir){
        if(dir==null)
            dir = getActivity().getCacheDir();
        else;
        if(dir==null)
            return;
        else;
        java.io.File[] children = dir.listFiles();
        try{
            for(int i=0;i<children.length;i++)
                if(children[i].isDirectory())
                    clearApplicationCache(children[i]);
                else children[i].delete();
        }
        catch(Exception e){}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearApplicationCache(null);
    }

    private void setUpList(){

        if (GRID_LAYOUT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        mRecyclerView.setHasFixedSize(true);
        //Use this now

        if(mRecyclerView.getItemDecorationCount() == 0 ) {
            mRecyclerView.addItemDecoration(new MaterialViewPagerHeaderDecorator());
        }

        myMemoryRecyclerViewAdapter =  new MyMemoryRecyclerViewAdapter(items,new MyMemoryRecyclerViewAdapter.ImageHelper() {
            @Override
            public void onImageSelect(ImageView imgView,TextView textView,int position) {
                selectedImg = imgView;
                selectImage(imgView,textView);
                selectedPosition = position;
            }

            @Override
            public void onMoveImage(ImageView imgView,String uri,int position)  {
                moveImage(imgView,uri);
                selectedPosition = position;
            }
        });

        mRecyclerView.setAdapter(myMemoryRecyclerViewAdapter);
    }


    private void moveImage(ImageView imgView,String uri) {
        ImageViewerActivity.start(getActivity(), uri, imgView);
    }
    private void selectImage(ImageView imgView,TextView textView) {
        final CharSequence[] items = {"사진 찍기", "갤러리에서 가져오기",
                "닫기"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext() );
        builder.setTitle("기억을 넣어주세요");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("사진 찍기")) {
                    activeTakePhoto(imgView,textView);
                } else if (items[item].equals("갤러리에서 가져오기")) {
                    activeGallery(imgView,textView);
                } else if (items[item].equals("닫기")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private File saveImg(String imgName){
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/MyMemory/Pictures");
        if(!dir.exists()) {
            dir.mkdir();
        }

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            try {
                String imageFileName =  imgName+".jpg";
                photoFile = new File(dir,imgName +".jpg");
                mCurrentPhotoPath = photoFile.getAbsolutePath();

            } catch (Exception e) {
            }
        }
        return photoFile;
    }

    private String convertDate(String date){
        String rDate="";
        if(date.length() > 8){
            rDate = date.substring(0,4)+date.substring(5,7)+date.subSequence(8,10);
        }else{
            rDate = date;
        }
        return rDate;
    }
    private void activeTakePhoto(ImageView imgView,TextView textView) {  //open camera

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = saveImg( convertDate(textView.getText().toString()));
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(
                    getContext(),
                    getActivity().getApplicationContext().getPackageName() + ".fileprovider",
                    photoFile);
            takePicture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void activeGallery(ImageView imgView,TextView textView) { // open gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");

        File photoFile = saveImg( convertDate(textView.getText().toString()));

        // Continue only if the File was successfully created
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(
                    getContext(),
                    getActivity().getApplicationContext().getPackageName() + ".fileprovider",
                    photoFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, RESULT_LOAD_IMAGE);//zero can be replaced with any action
        }
    }

    String mSelectedImagePath;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LOAD_IMAGE:
                if(resultCode == RESULT_OK){

                    mSelectedImagePath = getPath(data.getData());
                    System.out.println("mSelectedImagePath : " + mSelectedImagePath);
                    try {
                        File sd = Environment.getExternalStorageDirectory();
                        if (sd.canWrite()) {
                            File source = new File(mSelectedImagePath );
                            File destination= new File(mCurrentPhotoPath);
                            if (source.exists()) {
                                FileChannel src = new FileInputStream(source).getChannel();
                                FileChannel dst = new FileOutputStream(destination).getChannel();
                                dst.transferFrom(src, 0, src.size());       // copy the first file to second.....
                                src.close();
                                dst.close();
                            }
                            if(destination.exists()) {

                                selectedImg.refreshDrawableState();
                                selectedImg.destroyDrawingCache();
                                selectedImg.setImageBitmap(null);


                                Glide.with(getActivity()).load(data.getData())
                                        .apply(requestOptions)
                                        .into(selectedImg);

                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "기억담기 실패ㅠ_ㅠ", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "기억담기 실패ㅠ_ㅠ", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e) {
                        System.out.println("Error :" + e.getMessage());
                    }
                }
                break;

            case REQUEST_IMAGE_CAPTURE:
                if(resultCode == RESULT_OK){
                    File file = new File(mCurrentPhotoPath);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media
                                .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                        if (bitmap != null) {
                            selectedImg.setImageBitmap(bitmap);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }

        IntroActivity introActivity = new IntroActivity();
        introActivity.updateMyMemoryList();

        updateList();
        setUpList();
        SelfieFragment.mRecyclerView.scrollToPosition(selectedPosition);
        mViewPager.onPageSelected(selectedPosition);
        Log.i("slslslss",selectedPosition+"");

    }


    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        getActivity().startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void updateList(){

        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat fmEndDate = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = fmEndDate.format(cal.getTime());

        SharedPreferences pref = getActivity().getSharedPreferences("startingDate" , MODE_PRIVATE);
        startDate = pref.getString("startingDate" ,  null);

        ITEM_COUNT = Utils.doDiffOfDate(endDate,startDate);
        items = new ArrayList<>();
        for (int i = 0; i < ITEM_COUNT+1; i++) {

            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -i); // 하루를 더한다.

            String strDate = fm.format(cal.getTime());
            MyMemoryItem item = new MyMemoryItem(strDate,"","","");
            items.add(item);
        }

        for (int i = 0; i < Utils.myMemoryItemArrayList.size(); i++) {
            for (int j = 0; j <items.size(); j++) {
                if (Utils.myMemoryItemArrayList.get(i).getId().equals(items.get(j).getId())){
                    items.get(j).setMyMemoryUrl(Utils.myMemoryItemArrayList.get(i).getMyMemoryUrl());
                }
            }
        }

    }


}
