package horangya.jhm.begin.onedayoneselfie.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.kelin.calendarlistview.library.CalendarHelper;
import com.kelin.calendarlistview.library.CalendarListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

import butterknife.ButterKnife;
import horangya.jhm.begin.onedayoneselfie.Utils;
import horangya.jhm.begin.onedayoneselfie.calendarlistview.CalendarItemAdapter;
import horangya.jhm.begin.onedayoneselfie.calendarlistview.CustomCalendarItemModel;
import horangya.jhm.begin.onedayoneselfie.adapter.MyEverythingNewsListAdapter;
import horangya.jhm.begin.onedayoneselfie.calendarlistview.NewsService;
import horangya.jhm.begin.onedayoneselfie.calendarlistview.retrofit.RetrofitProvider;
import horangya.jhm.begin.onedayoneselfie.dummy.MyMemory;
import horangya.jhm.begin.onedayoneselfie.R;
import okhttp3.internal.Util;
import rx.Notification;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by florentchampigny on 24/04/15.
 */
public class ScrollFragment extends Fragment {

    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyyMMdd");
    public static final SimpleDateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat("yyyyMMdd");

    public static CalendarListView calendarListView;
    public static MyEverythingNewsListAdapter myEverythingNewsListAdapter;
    public static CalendarItemAdapter calendarItemAdapter;
    //key:date "yyyy-mm-dd" format.
    public static  TreeMap<String, List<NewsService.News.StoriesBean>> listTreeMap = new TreeMap<>();
    public static Handler handler = new Handler();


    private ArrayList<MyMemory.MyMemoryItem> tempList;

    public static ScrollFragment newInstance() {

        return new ScrollFragment();
    }

    public static void updateList(){
        if(Utils.myMemoryItemArrayList != null
                && Utils.myMemoryItemArrayList.size()>0) {
            if (listTreeMap.size() == 0) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, +0);
                loadNewsList(DAY_FORMAT.format(calendar.getTime()));
            }
            Log.i("ddddddddddddd", listTreeMap.size() + "   ");
            if (listTreeMap.size() != 0) {
                for (int i = 0; i < (Utils.myMemoryItemArrayList.size() / listTreeMap.size()) + 3; i++) {
                    Log.i("ddddddddddddd", listTreeMap.size() + "   " + i);
                    if (Utils.myMemoryItemArrayList.size() == listTreeMap.size())
                        break;

                    String date = listTreeMap.firstKey();
                    Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
                    calendar.add(Calendar.MONTH, -1);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                    loadNewsList(DAY_FORMAT.format(calendar.getTime()));

                }
            }
        }
        if(Utils.myMemoryItemArrayList.size()>0 &&
                listTreeMap.size() > 0) {
            Log.i("ddddddddddddd2",listTreeMap.size()+listTreeMap.firstKey()+"   "+myEverythingNewsListAdapter.getCount());
            loadCalendarData(listTreeMap.firstKey());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scroll, container, false);
        Log.i("size11",listTreeMap.size()+"" );
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


        calendarListView = (CalendarListView) getActivity().findViewById(R.id.calendar_listview);
        myEverythingNewsListAdapter = new MyEverythingNewsListAdapter(getActivity().getApplicationContext());
        calendarItemAdapter = new CalendarItemAdapter(getActivity().getApplicationContext());
        calendarListView.setCalendarListViewAdapter(calendarItemAdapter, myEverythingNewsListAdapter);



        tempList = new ArrayList<>();
        tempList = Utils.myMemoryItemArrayList;

        if( Utils.myMemoryItemArrayList != null
                && Utils.myMemoryItemArrayList.size()>0) {
            // set start time,just for test.
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, +0);
            loadNewsList(DAY_FORMAT.format(calendar.getTime()));
        }


        // deal with refresh and load more event.
        calendarListView.setOnListPullListener(new CalendarListView.onListPullListener() {
            @Override
            public void onRefresh() {
                try {
//                    if( Utils.myMemoryItemArrayList != null&& Utils.myMemoryItemArrayList.size()>0) {
//                        String date = listTreeMap.firstKey();
//                        Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
//                        calendar.add(Calendar.MONTH, -1);
//                        calendar.set(Calendar.DAY_OF_MONTH, 1);
//                        loadNewsList(DAY_FORMAT.format(calendar.getTime()));
//                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoadMore() {
                try {

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //
        calendarListView.setOnMonthChangedListener(new CalendarListView.OnMonthChangedListener() {
            @Override
            public void onMonthChanged(String yearMonth) {
                Calendar calendar = CalendarHelper.getCalendarByYearMonth(yearMonth);
                loadCalendarData(yearMonth);
            }
        });

        calendarListView.setOnCalendarViewItemClickListener(new CalendarListView.OnCalendarViewItemClickListener() {
            @Override
            public void onDateSelected(View View, String selectedDate, int listSection, SelectedDateRegion selectedDateRegion) {

            }
        });

    }


    //this code is just for generate test date for ListView!
    public static  void loadNewsList(String date) {
        Calendar calendar = getCalendarByYearMonthDay(date);
        String key = CalendarHelper.YEAR_MONTH_FORMAT.format(calendar.getTime());

        // just not care about how data to create.
        Random random = new Random();
        final List<String> set = new ArrayList<>();

        for(int z=0; z<Utils.myMemoryItemArrayList.size();z++){
            String pDdate = Utils.myMemoryItemArrayList.get(z).getId();
            String sDate = "";
            Log.i("scrollLog",pDdate);
            String yyyy = pDdate.substring(0,4);
            String mm = pDdate.substring(4,6);
            String dd = pDdate.substring(6,8);
            sDate = yyyy+"-"+mm+"-"+dd;
            set.add(sDate);
            Log.i("scrollLog",set.get(z));
        }

        Observable<Notification<NewsService.News>> newsListOb =
                RetrofitProvider.getInstance().create(NewsService.class)
                        .getNewsList(date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        //.compose(bindToLifecycle())
                        .materialize().share();

        newsListOb.filter(Notification::isOnNext)
                .map(n -> n.getValue())
                .filter(m -> !m.getStories().isEmpty())
                .flatMap(m -> Observable.from(m.getStories()))
                .doOnNext(i -> {
                    int index = random.nextInt(Utils.myMemoryItemArrayList.size());
                    String day = set.get(index);
                    if (listTreeMap.get(day) == null) {
                        List<NewsService.News.StoriesBean> list = new ArrayList<NewsService.News.StoriesBean>();
                        i.setLocation(Utils.myMemoryItemArrayList.get(index).getContent());
                        i.setDateTime(Utils.myMemoryItemArrayList.get(index).getDetails());
                        List<String> imgList = new ArrayList<>();
                        imgList.add(Utils.myMemoryItemArrayList.get(index).getMyMemoryUrl());
                        i.setImages(imgList);
                        i.setId(index);
                        list.add(i);
                        listTreeMap.put(day, list);
                    }
                })
                .toList()
                .subscribe((l) -> {
                    myEverythingNewsListAdapter.setDateDataMap(listTreeMap);
                    myEverythingNewsListAdapter.notifyDataSetChanged();
                    calendarItemAdapter.notifyDataSetChanged();
                })
        ;

    }

    // date (yyyy-MM),load data for Calendar View by date,load one month data one times.
    // generate test data for CalendarView,imitate to be a Network Requests. update "calendarItemAdapter.getDayModelList()"
    //and notifyDataSetChanged will update CalendarView.
    public static void loadCalendarData(final String date) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Random random = new Random();
                            if (calendarListView.getCurrentSelectedDate() != null
                                    && calendarListView.getCurrentSelectedDate().length() >7
                                    && date.equals(calendarListView.getCurrentSelectedDate().substring(0, 7))) {
                                for (String d : listTreeMap.keySet()) {
                                    if (date.equals(d.substring(0, 7))) {
                                        CustomCalendarItemModel customCalendarItemModel = calendarItemAdapter.getDayModelList().get(d);
                                        if (customCalendarItemModel != null) {
                                            customCalendarItemModel.setNewsCount(listTreeMap.get(d).size());
                                            customCalendarItemModel.setFav(random.nextBoolean());
                                        }

                                    }
                                }
                                calendarItemAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }.start();

    }

    public static Calendar getCalendarByYearMonthDay(String yearMonthDay) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTimeInMillis(DAY_FORMAT.parse(yearMonthDay).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    OnListFragmentInteractionListener  mListener;
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MyMemory.MyMemoryItem item);
    }


    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            onBackPressed();
        }
    }

    private static Typeface typeface;

    private void setGlobalFont(View view) {
        if(view != null) {
            if(view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                int vgCnt = viewGroup.getChildCount();
                for(int i = 0; i<vgCnt; i++) {
                    View v = viewGroup.getChildAt(i);
                    if(v instanceof TextView) {
                        ((TextView) v).setTypeface(typeface);
                    }
                    setGlobalFont(v);
                }
            }
        }
    }

}
