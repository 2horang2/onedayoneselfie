package horangya.jhm.begin.onedayoneselfie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import horangya.jhm.begin.onedayoneselfie.R;
import horangya.jhm.begin.onedayoneselfie.activity.ImageViewerActivity;
import horangya.jhm.begin.onedayoneselfie.activity.MainActivity;
import horangya.jhm.begin.onedayoneselfie.dummy.MyMemory.MyMemoryItem;

import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;


/**
 * Created by florentchampigny on 24/04/15.
 */
public class MyMemoryRecyclerViewAdapter extends RecyclerView.Adapter<MyMemoryRecyclerViewAdapter.ViewHolder> {

    List<MyMemoryItem> contents;
    Context mContext;
    private ImageHelper imageHelper;

    static final int EXIST_MEMORY = 1000;
    static final int NOT_EXIST_MEMORY = 2000;


    RequestOptions requestOptions = new RequestOptions();
    public MyMemoryRecyclerViewAdapter( List<MyMemoryItem> contents,ImageHelper imageHelper) {
        this.contents = contents;
        this.imageHelper = imageHelper;
    }

    public interface ImageHelper{
        void onImageSelect(ImageView imgView, TextView textView,int position);
        void onMoveImage(ImageView imgView, String uri,int position);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case EXIST_MEMORY:
                holder.viewType=EXIST_MEMORY;
            case NOT_EXIST_MEMORY:
                holder.viewType=NOT_EXIST_MEMORY;
        }

        String date = contents.get(position).getId();
        date = date.substring(0,4) + "."+date.substring(4,6) + "." + date.substring(6,8);

        holder.txt_my_memory_date.setText(date);
        if(contents.get(position).getMyMemoryUrl() != null
                && contents.get(position).getMyMemoryUrl()!= "") {


            Glide.with(mContext)
                    .asBitmap()
                    .load(contents.get(position).getMyMemoryUrl())
                    .apply(requestOptions)
                    .into(holder.img_my_memory);

            Log.i("imgpppppp",position+"");

            holder.img_my_memory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //https://stackoverflow.com/questions/51360750/how-to-edit-my-recyclerview-image-in-my-adapater-class
                    if(imageHelper!=null) {
                        imageHelper.onMoveImage(holder.img_my_memory, contents.get(position).getMyMemoryUrl(),position);
                    }
                }
            });
            holder.img_my_memory.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //https://stackoverflow.com/questions/51360750/how-to-edit-my-recyclerview-image-in-my-adapater-class
                    if(imageHelper!=null) {
                        imageHelper.onImageSelect(holder.img_my_memory, holder.txt_my_memory_date,position);
                    }
                    return true;
                }
            });

        }else{
            holder.txt_my_memory_date.setText(date+ " 의 기억은 어디있을까요?");
            holder.img_my_memory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //https://stackoverflow.com/questions/51360750/how-to-edit-my-recyclerview-image-in-my-adapater-class
                    if(imageHelper!=null)
                        imageHelper.onImageSelect(holder.img_my_memory,holder.txt_my_memory_date,position);
                }
            });
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView txt_my_memory_date;
        public ImageView img_my_memory;;
        public int viewType = 0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_my_memory_date=itemView.findViewById(R.id.txt_my_memory_date);
            img_my_memory=itemView.findViewById(R.id.img_my_memory);
            viewType = getItemViewType2();
        }

        public int getItemViewType2() {
            return EXIST_MEMORY;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (contents.get(position).getMyMemoryUrl().length()) {
            case 0:
                return NOT_EXIST_MEMORY;
            default:
                return EXIST_MEMORY;
        }
    }


    @Override
    public int getItemCount() {
        return contents.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        mContext=parent.getContext();


        requestOptions.placeholder(R.drawable.ic_btn_calendar_heart_down);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.error(R.drawable.ic_btn_calendar_heart_down);
        requestOptions.skipMemoryCache(true);



        switch (viewType) {
            case EXIST_MEMORY: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_memory, parent, false);
                return new ViewHolder(view) {
                };
            }
            case NOT_EXIST_MEMORY: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_card_no_memory, parent, false);
                return new ViewHolder(view) {
                };
            }
        }
        return null ;
    };
}