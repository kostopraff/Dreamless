package ru.kostopraff.dreamless;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.requests.VKRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class DreamPicsum extends DreamService {

    private AppCompatImageView imageView;
    private AppCompatImageView vkNotificationIcon;
    private Drawable drawable;
    private Timer timer;
    private int width, height;
    private int notificationCount = 0;
    private static final String [] FIELDS = {"friends", "messages", "photos", "videos", "notes", "gifts",
            "events", "groups", "notifications", "sdk", "app_requests"};

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFullscreen(true);
        setInteractive(false);
        setContentView(R.layout.dream_photo);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        imageView = findViewById(R.id.dream_photo);
        switch (height){
            case 720:
                imageView.setImageResource(R.drawable.dreamless_banner_720);
            case 1080:
                imageView.setImageResource(R.drawable.dreamless_banner_1080);
            case 2160:
                imageView.setImageResource(R.drawable.dreamless_banner_4k);
        }

    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        vkNotificationIcon = findViewById(R.id.vk_notification);

        final Handler uiHandler = new Handler();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                drawable = imageView.getDrawable();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get()
                                .load("https://picsum.photos/"+width+"/"+height)
                                .placeholder(drawable)
                                .priority(Picasso.Priority.HIGH)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .into(imageView);
                    }
                });
            }
        },0,10000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(VK.isLoggedIn()){
                    VK.execute(new VKRequest("account.getCounters"), new VKApiCallback() {
                        @Override
                        public void success(Object o) {
                            Log.v("VK", "Success sending request: " + o.toString());
                            JSONObject response = (JSONObject) o;
                            notificationCount = 0;
                            try {
                                for (String i : FIELDS) {
                                    notificationCount += response
                                        .getJSONObject("response")
                                        .optInt(i);
                                }
                                if(vkNotificationIcon.getVisibility() == View.VISIBLE){
                                    if (notificationCount == 0) {
                                        vkNotificationIcon.setVisibility(View.INVISIBLE);
                                    }
                                } else if (notificationCount > 0){
                                    Log.v("VK", "NEW NOTIFICATION");
                                    vkNotificationIcon.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                Log.e("VK", "Error parsing json: " + e.toString());
                            }
                        }
                        @Override
                        public void fail(@NotNull Exception e) {
                            Log.e("VK", e.toString());
                        }
                    });
                }
            }
        }, 5000, 30000);
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
        timer.cancel();
    }
}
