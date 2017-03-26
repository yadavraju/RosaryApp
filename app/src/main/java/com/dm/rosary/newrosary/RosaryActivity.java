package com.dm.rosary.newrosary;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.rosary.R;
import com.dm.rosary.utils.AppText;
import com.dm.rosary.utils.BitmapUtil;
import com.dm.rosary.utils.FileUtil;
import com.dm.rosary.utils.OnSwipeTouchListener;
import com.dm.rosary.utils.PreferenceHelper;

import java.io.FileNotFoundException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RosaryActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_rosary1)
    RelativeLayout activityRosary;
    @BindView(R.id.iv_rosary)
    ImageView ivRosary;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    @BindView(R.id.count)
    TextView count;

    private PreferenceHelper helper ;
    private int matraCount = 0;

    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;
    private int a = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rosary);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        helper = new PreferenceHelper(RosaryActivity.this);
        count.setText("Count: "+helper.getString(AppText.SAVE_COUNT, "0"));
        matraCount = Integer.parseInt(helper.getString(AppText.SAVE_COUNT, "0"));
        a = Integer.parseInt(helper.getString(AppText.SPEAK_COUNT,"1"));
        toolBarSetUp();
        rosarySwifeMethod();
        CheckTSS();
    }

    private void CheckTSS() {
        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    //speak the user text
    private void speakWords(String speech) {
        //speak straight away
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private void rosarySwifeMethod() {
        ivRosary.setOnTouchListener(new OnSwipeTouchListener(RosaryActivity.this) {
            public void onSwipeTop() {
                startanimation();
                matraCount++;
                helper.edit().putString(AppText.SAVE_COUNT,String.valueOf(matraCount)).commit();
                stopanimation();
            }

            public void onSwipeRight() {
                startanimation();
                matraCount++;
                helper.edit().putString(AppText.SAVE_COUNT,String.valueOf(matraCount)).commit();
                stopanimation();

            }

            public void onSwipeLeft() {
                startanimation();
                matraCount++;
                helper.edit().putString(AppText.SAVE_COUNT,String.valueOf(matraCount)).commit();
                stopanimation();
            }

            public void onSwipeBottom() {
                startanimation();
                matraCount++;
                helper.edit().putString(AppText.SAVE_COUNT,String.valueOf(matraCount)).commit();
                stopanimation();
            }

        });
    }

    private void toolBarSetUp() {
        /**
         * Menu item click listner
         * */
        toolbar.setTitle("");
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cg_background:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 1);
                                break;
                            case R.id.action_cg_clay:
                                ivRosary.setImageDrawable(ContextCompat.getDrawable(RosaryActivity.this, R.drawable.ro2));

                                break;
                            case R.id.action_cg_diamond:
                                ivRosary.setImageDrawable(ContextCompat.getDrawable(RosaryActivity.this, R.drawable.gray_ros));
                                break;
                            case R.id.action_cg_quartz:
                                ivRosary.setImageDrawable(ContextCompat.getDrawable(RosaryActivity.this, R.drawable.rosary1));
                                break;
                        }
                        return true;
                    }
                });
    }


    private void startanimation() {
        RotateAnimation anim = new RotateAnimation(0.0f, -360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //Setup anim with desired properties
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE); //Repeat animation indefinitely
        anim.setDuration(500); //Put desired duration per anim cycle here, in milliseconds

        //Start animation
        ivRosary.startAnimation(anim);
    }

    private void stopanimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                ivRosary.clearAnimation();
                count.setText("Count: "+matraCount);
                if (matraCount==108*a){
                    speakWords("Your mantra count is "+matraCount);
                    a++;
                    helper.edit().putString(AppText.SPEAK_COUNT,String.valueOf(a)).commit();
                }
            }
        }, SPLASH_TIME_OUT);

    }

    private String base;

    //获取本地图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String img_url = uri.getPath();//这是本机的图片路径
            Log.e("znq", "img_url" + FileUtil.getRealFilePath(this, uri));
            ContentResolver cr = this.getContentResolver();
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeStream(cr
                        .openInputStream(uri), null, options);
                /* 将Bitmap设定到ImageView */
                Log.e("znq", "bitmap" + BitmapUtil.convertIconToString(bitmap));
                base = BitmapUtil.convertIconToString(bitmap);
                Drawable d = new BitmapDrawable(getResources(), bitmap);
                //ivBackground.setImageBitmap(bitmap);
                activityRosary.setBackground(d);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //setup TTS
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cg_background) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
