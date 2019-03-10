package horangya.jhm.begin.onedayoneselfie.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import agency.tango.materialintroscreen.SlideFragmentBuilder;
import horangya.jhm.begin.onedayoneselfie.R;


import agency.tango.materialintroscreen.MaterialIntroActivity;

import agency.tango.materialintroscreen.MessageButtonBehaviour;

import agency.tango.materialintroscreen.SlideFragmentBuilder;

import agency.tango.materialintroscreen.animations.IViewTranslation;
import horangya.jhm.begin.onedayoneselfie.Utils;

import static horangya.jhm.begin.onedayoneselfie.Utils.typeface;

public class InitActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        SharedPreferences pref = getSharedPreferences("startingDate" , MODE_PRIVATE);
        String str = pref.getString("startingDate" ,  null);

        if(str != null){
            startActivity(new Intent(InitActivity.this,IntroActivity.class));
            finish();
        }



        enableLastSlideAlphaExitTransition(true);



        getBackButtonTranslationWrapper()

                .setEnterTranslation(new IViewTranslation() {

                    @Override

                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {

                        view.setAlpha(percentage);

                    }

                });



        addSlide(new SlideFragmentBuilder()

                        .backgroundColor(R.color.first_slide_background)

                        .buttonsColor(R.color.gray_eeeeee)

                        .description("\n\n안녕하세요.\n당신의 기억을 하루에 한장씨 보관할 수 있는 기억보관함입니다.\n기억해주세요.\n하루에 한장 입니다.")


                        .build()
                );



        addSlide(new SlideFragmentBuilder()

                .backgroundColor(R.color.second_slide_background)

                .buttonsColor(R.color.gray_eeeeee)
                .image(R.drawable.init_side_bar)

                .title("SIDE BAR")

                .description("\n\n당신의 시작일과 아직 비어있는 기억들을 볼 수 있습니다.")

                .build());



        //addSlide(new CustomSlide());

        addSlide(new SlideFragmentBuilder()

                        .backgroundColor(R.color.third_slide_background)

                        .buttonsColor(R.color.gray_eeeeee)

                        .neededPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET})

                        .image(R.drawable.init_my_memory)

                        .title("MY MEMORY")

                        .description("\n\n최근 순으로 하루에 하나씩 카드가 생성됩니다.\n빈 카드를 한 번 클릭해서 사진을 넣어주세요.\n\n 이미 채운 기억은 길게 누르면 수정 가능합니다.")

                        .build());



        addSlide(new SlideFragmentBuilder()

                .backgroundColor(R.color.fourth_slide_background)

                .buttonsColor(R.color.gray_eeeeee)
                        .image(R.drawable.init_my_every)

                .title("MY EVERYTHING")

                .description("\n\n달력 기준으로 기억 보관함의 상태를 한 눈에 볼 수 있습니다.\n하트가 차 있는 날이 기억이 된 상태입니다.")

                .build()

                );

        Typeface typeface = null;
        try{
            if(typeface == null) {
                typeface = Typeface.createFromAsset(getAssets(),
                        "Mohave.ttf");
            }
            Utils.setGlobalFont(getWindow().getDecorView());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override

    public void onFinish() {

        super.onFinish();

        Toast.makeText(this, "정말 반가워요! :)", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(InitActivity.this,IntroActivity.class));
        finish();

    }

}