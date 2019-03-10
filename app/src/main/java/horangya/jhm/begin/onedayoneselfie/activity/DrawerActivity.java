package horangya.jhm.begin.onedayoneselfie.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import butterknife.BuildConfig;
import horangya.jhm.begin.onedayoneselfie.R;
import horangya.jhm.begin.onedayoneselfie.Utils;
import horangya.jhm.begin.onedayoneselfie.dummy.MyMemory;
import horangya.jhm.begin.onedayoneselfie.fragment.SelfieFragment;

import static horangya.jhm.begin.onedayoneselfie.Utils.setGlobalFont;
import static horangya.jhm.begin.onedayoneselfie.Utils.typeface;
import static horangya.jhm.begin.onedayoneselfie.activity.MainActivity.mViewPager;


/**
 * Created by florentchampigny on 27/05/2016.
 */
public class DrawerActivity  extends AppCompatActivity
        implements AdapterView.OnItemClickListener {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;

    ArrayList<MyMemory.MyMemoryItem> items=new ArrayList<>();

    List<MyMemory.MyMemoryItem> mlist;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!BuildConfig.DEBUG) {
            //Fabric.with(this, new Crashlytics());
        }

    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,items.get(position).getId(),Toast.LENGTH_SHORT).show();
    }
    String startDate;
    int ITEM_COUNT;
    @Override
    protected void onStart() {
        super.onStart();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }

        ListView listTreasureRat = (ListView)findViewById(R.id.list_treasure_rat);

        Calendar cal = new GregorianCalendar(Locale.KOREA);
        cal.setTime(new Date());
        SimpleDateFormat fm = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat fmEndDate = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = fmEndDate.format(cal.getTime());

        SharedPreferences pref = getSharedPreferences("startingDate" , MODE_PRIVATE);
        String startDate = pref.getString("startingDate" ,  null);

        ITEM_COUNT = Utils.doDiffOfDate(endDate,startDate);

        items = new ArrayList<>();
        mlist = new ArrayList<>();


        Log.i("intoday", ITEM_COUNT+startDate+endDate);
        for (int i = 0; i < ITEM_COUNT+1; i++) {

            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_YEAR, -i); // 하루를 더한다.

            String strDate = fm.format(cal.getTime());

            MyMemory.MyMemoryItem item = new MyMemory.MyMemoryItem(strDate,"","","");
            items.add(item);
        }

        for(int i=0; i<items.size();
            mlist.add(items.get(i++)));

        for (int i = 0; i < Utils.myMemoryItemArrayList.size(); i++) {
            for (int j = 0; j <mlist.size(); j++) {
                if (Utils.myMemoryItemArrayList.get(i).getId().equals(mlist.get(j).getId())){
                    mlist.remove(j);
                }
            }
        }
        ItemAdapter adapter = new ItemAdapter(this,mlist);
        listTreasureRat.setAdapter(adapter);
        listTreasureRat.setClickable(true);

        TextView txtStartDate,txtTreasureCnt,txtTreasureRat;
        txtStartDate = findViewById(R.id.txt_v_start_date);
        txtTreasureCnt = findViewById(R.id.txt_v_treasure_cnt);
        txtTreasureRat = findViewById(R.id.txt_v_treasure_rat);

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH);

        int day= calendar.get(Calendar.DAY_OF_MONTH);


        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatePickerDialog dialog = new DatePickerDialog(v.getContext(), dateListener, year, month, day);
               // dialog.show();


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        v.getContext(),
                        R.style.MyDatePickerStyle,
                        dateListener,
                        year, month, day);
                datePickerDialog.show();
            }
        });


        TextView txtAlarm,txtTheme;
        txtAlarm = findViewById(R.id.txt_alarm);
        txtTheme = findViewById(R.id.txt_theme);

        txtAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"준비중입니다 ^0^",Toast.LENGTH_LONG).show();
            }
        });
        txtTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"준비중입니다 ^0^",Toast.LENGTH_LONG).show();
            }
        });

        txtStartDate.setText(startDate);
        txtTreasureCnt.setText((ITEM_COUNT+1)+"개");
        try {
            txtTreasureRat.setText(100 - ((int) (mlist.size() * 100) / (ITEM_COUNT+1) ) + "%");
        }catch (Exception e){

            txtTreasureRat.setText("0");
        }

    }
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


            SharedPreferences pref = getSharedPreferences("startingDate" , MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            String YYYY = year+"";
            String MM;
            monthOfYear = monthOfYear+1;
            if(monthOfYear < 10){
                MM = "0"+monthOfYear;
            }else{
                MM = ""+monthOfYear;
            }
            String DD;
            if(dayOfMonth < 10){
                DD = "0"+dayOfMonth;
            }else{
                DD = ""+dayOfMonth;
            }
            if((YYYY+"-"+MM+"-"+DD).length() == 10) {
                editor.putString("startingDate", YYYY + "-" + MM + "-" + DD);
                editor.commit();

                startActivity(new Intent(DrawerActivity.this, IntroActivity.class));
                finish();
            }
        }

    };


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
            super.onOptionsItemSelected(item);
    }
    class ItemAdapter extends BaseAdapter {

        Activity context;
        public ItemAdapter(Activity context, List<MyMemory.MyMemoryItem> items){
            this.context = context;
            mlist = items;
        }
        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(getApplicationContext()).
                    inflate(R.layout.list_item_card_no_memory_drawer,parent,false);
            TextView tv = (TextView)view.findViewById(R.id.txt_my_memory_date);
            tv.setText(mlist.get(position).getId() + "의 기억은 어디있을까요?");

            try{
                if(typeface == null) {
                    typeface = Typeface.createFromAsset(view.getContext().getAssets(),
                            "Mohave.ttf");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            setGlobalFont(view);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        for (int j = 0; j <items.size(); j++) {
                            if (mlist.get(position).getId().equals(items.get(j).getId())){
                                SelfieFragment.mRecyclerView.scrollToPosition(j);
                                mViewPager.onPageSelected(j);
                            }
                    }
                }
            });

            return view;
        }
    }



}
