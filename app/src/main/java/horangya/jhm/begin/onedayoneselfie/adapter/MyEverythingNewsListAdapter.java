package horangya.jhm.begin.onedayoneselfie.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kelin.calendarlistview.library.BaseCalendarListAdapter;
import com.kelin.calendarlistview.library.CalendarHelper;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import horangya.jhm.begin.onedayoneselfie.activity.MainActivity;
import horangya.jhm.begin.onedayoneselfie.R;
import horangya.jhm.begin.onedayoneselfie.calendarlistview.NewsService;

import static com.kelin.calendarlistview.library.CalendarHelper.YEAR_MONTH_FORMAT;
import static horangya.jhm.begin.onedayoneselfie.Utils.setGlobalFont;
import static horangya.jhm.begin.onedayoneselfie.Utils.typeface;

public class MyEverythingNewsListAdapter extends BaseCalendarListAdapter<NewsService.News.StoriesBean> {


    public MyEverythingNewsListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getSectionHeaderView(String date, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;
        List<NewsService.News.StoriesBean> modelList = dateDataMap.get(date);
        if (convertView != null) {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.listitem_calendar_header, null);
            headerViewHolder = new HeaderViewHolder();
            headerViewHolder.dayText = (TextView) convertView.findViewById(R.id.header_day);
            headerViewHolder.yearMonthText = (TextView) convertView.findViewById(R.id.header_year_month);
            headerViewHolder.isFavImage = (ImageView) convertView.findViewById(R.id.header_btn_fav);
            convertView.setTag(headerViewHolder);
        }

        Calendar calendar = CalendarHelper.getCalendarByYearMonthDay(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        if (day < 10) {
            dayStr = "0" + dayStr;
        }

        headerViewHolder.dayText.setText(dayStr);
        headerViewHolder.yearMonthText.setText(YEAR_MONTH_FORMAT.format(calendar.getTime()));
        try{
            if(typeface == null) {
                typeface = Typeface.createFromAsset(convertView.getContext().getAssets(),
                        "Mohave.ttf");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setGlobalFont(convertView);

        return convertView;
    }

    @Override
    public View getItemView(NewsService.News.StoriesBean model, String date, int pos, View convertView, ViewGroup parent) {
        ContentViewHolder contentViewHolder;
        if (convertView != null) {
            contentViewHolder = (ContentViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.listitem_calendar_content, null);
            contentViewHolder = new ContentViewHolder();
            contentViewHolder.titleTextView = (TextView) convertView.findViewById(R.id.title);
            contentViewHolder.timeTextView = (TextView) convertView.findViewById(R.id.time);
            contentViewHolder.newsImageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(contentViewHolder);
        }

        contentViewHolder.titleTextView.setText(model.getLocation());
        contentViewHolder.timeTextView.setText(model.getDateTime());


        Glide.with(convertView.getContext()).asBitmap().load(model.getImages().get(0)).into(contentViewHolder.newsImageView);

        contentViewHolder.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 위도,경도 입력 후 지도 버튼 클릭 => 지도화면으로 인텐트 날리기
                double d1 = 0;
                double d2 = 0;
                //= Double.parseDouble(et2.getText().toString());

                Intent intent = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:" + d1 + "," + d2));
                parent.getContext().startActivity(intent);
            }
        });


        try{
            if(typeface == null) {
                typeface = Typeface.createFromAsset(convertView.getContext().getAssets(),
                        "Mohave.ttf");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setGlobalFont(convertView);

        return convertView;
    }

    private static class HeaderViewHolder {
        TextView dayText;
        TextView yearMonthText;
        ImageView isFavImage;
    }

    private static class ContentViewHolder {
        TextView titleTextView;
        TextView timeTextView;
        ImageView newsImageView;
    }

}
