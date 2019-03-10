package horangya.jhm.begin.onedayoneselfie;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import horangya.jhm.begin.onedayoneselfie.dummy.MyMemory.MyMemoryItem;

public class Utils {


    public static ArrayList<MyMemoryItem> myMemoryItemArrayList;

    public static Typeface typeface;
    public static void setGlobalFont(View view) {
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
    final public static int REQUEST_IMAGE_CAPTURE = 1000;
    final public static int RESULT_LOAD_IMAGE = 2000;
    public static String startingDate = null;

    static  public int doDiffOfDate(String start, String end){

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(start);
            Date endDate = formatter.parse(end);

            // 시간차이를 시간,분,초를 곱한 값으로 나누면 하루 단위가 나옴
            Log.i("intoday",endDate.getTime()+"  "+beginDate.getTime());
            long diff = endDate.getTime() - beginDate.getTime();
            Log.i("intoday",diff+"");
            long diffDays = diff / (24 * 60 * 60 * 1000);
            if(diffDays < 0)
                diffDays *= -1;

            return (int) diffDays;

        } catch (ParseException e) {

            e.printStackTrace();

        }
        return 0;
    }



    // 메모리에서 제거하기
    public static void unbindDrawables(View view) {

        try {
            if (view == null)
                return;

            if (view instanceof ImageView) {
                ((ImageView) view).setImageDrawable(null);
            }
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
