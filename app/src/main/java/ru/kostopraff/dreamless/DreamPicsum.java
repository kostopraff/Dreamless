package ru.kostopraff.dreamless;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.service.dreams.DreamService;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;

import androidx.annotation.RequiresApi;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class DreamPicsum extends DreamService {

    private ImageView imageView;
    private Drawable drawable;
    private int width, height;
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
        imageView.setImageResource(R.drawable.dreamless_tv_banner);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
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
    }
}
