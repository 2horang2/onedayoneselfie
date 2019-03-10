package horangya.jhm.begin.onedayoneselfie.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MyMemory {

    /**
     * An array of sample (MyMemory) items.
     */
    public static final List<MyMemoryItem> ITEMS = new ArrayList<MyMemoryItem>();

    /**
     * A map of sample (MyMemory) items, by ID.
     */
    public static final Map<String, MyMemoryItem> ITEM_MAP = new HashMap<String, MyMemoryItem>();


    private static void addItem(MyMemoryItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static MyMemoryItem createMyMemoryItem(int position) {
        return new MyMemoryItem(String.valueOf(position), "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg", "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A MyMemory item representing a piece of content.
     */
    public static class MyMemoryItem {
        public void setId(String id) {
            this.id = id;
        }

        public void setMyMemoryUrl(String myMemoryUrl) {
            this.myMemoryUrl = myMemoryUrl;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String id;
        public String myMemoryUrl;
        public String content;
        public String details;

        public String getId() {
            return id;
        }

        public String getMyMemoryUrl() {
            return myMemoryUrl;
        }

        public String getContent() {
            return content;
        }

        public String getDetails() {
            return details;
        }


        public MyMemoryItem(String id, String myMemoryUrl, String content, String details) {
            this.id = id;
            this.myMemoryUrl = myMemoryUrl;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
