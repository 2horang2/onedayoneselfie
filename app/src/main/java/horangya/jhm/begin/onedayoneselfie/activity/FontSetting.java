package horangya.jhm.begin.onedayoneselfie.activity;

import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.tsengvn.typekit.Typekit;

import butterknife.BuildConfig;
import horangya.jhm.begin.onedayoneselfie.R;


/**
 * Created by florentchampigny on 27/05/2016.
 */
public class FontSetting extends Application {


    public void onCreate( ) {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "Mohave.ttf"))
                .addBold(Typekit.createFromAsset(this, "Mohave-Bold.otf"))
                .addItalic(Typekit.createFromAsset(this, "Mohave Italics.otf"))
                .addBoldItalic(Typekit.createFromAsset(this, "Mohave-Bold Italics.otf"))
                .addCustom1(Typekit.createFromAsset(this, "Mohave-SemiBold Italics.otf"))
                .addCustom2(Typekit.createFromAsset(this, "Mohave-SemiBold.otf"));




    }

}
