package ru.kostopraff.dreamless;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKApiCallback;
import com.vk.api.sdk.VKApiManager;
import com.vk.api.sdk.VKApiResponseParser;
import com.vk.api.sdk.VkResult;
import com.vk.api.sdk.exceptions.VKApiExecutionException;
import com.vk.api.sdk.requests.VKRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DreamPicsum extends DreamService {

    private AppCompatImageView imageView;
    private AppCompatImageView vkNotificationIcon;
    private Drawable drawable;
    private int width, height;
    private int oldNotificationCount = -1, viewTime;

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
        Timer timer = new Timer();
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
                    VK.execute(new VKRequest("notifications.get"), new VKApiCallback() {
                        @Override
                        public void success(Object o) {
                            Log.v("VK", "Success sending request: " + o.toString());
                            JSONObject response = (JSONObject) o;
                            try {
                                JSONArray notificationCount = response.getJSONObject("response").getJSONArray("items");
                                if(oldNotificationCount == -1) {
                                    oldNotificationCount = notificationCount.length();
                                    viewTime = response.getJSONObject("response").getInt("last_viewed");
                                }
                                if (notificationCount.length() != oldNotificationCount){
                                    oldNotificationCount = notificationCount.length();
                                    Log.v("VK", "NEW NOTIFICATION");
                                    vkNotificationIcon.setVisibility(View.VISIBLE);
                                } else if (viewTime != response.getJSONObject("response").getInt("last_viewed")){
                                    vkNotificationIcon.setVisibility(View.INVISIBLE);
                                }
                            } catch (JSONException e) {
                                Log.e("VK", "ERROR json parse: " + e.toString());
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
}
