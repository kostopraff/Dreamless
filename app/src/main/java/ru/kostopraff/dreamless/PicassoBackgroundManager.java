package ru.kostopraff.dreamless;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.leanback.app.BackgroundManager;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Timer;
import java.util.TimerTask;

public class PicassoBackgroundManager {
    private static final String TAG = PicassoBackgroundManager.class.getSimpleName();

    private final int DEFAULT_BACKGROUND_RES_ID = R.drawable.dreamless_banner_4k;
    private static Drawable mDefaultBackground;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private BackgroundManager mBackgroundManager = null;
    private DisplayMetrics mMetrics;
    private PicassoBackgroundManagerTarget mBackgroundTarget;
    private Timer mBackgroundTimer;

    public PicassoBackgroundManager (Activity activity) {
        mDefaultBackground = activity.getDrawable(DEFAULT_BACKGROUND_RES_ID);
        mBackgroundManager = BackgroundManager.getInstance(activity);
        mBackgroundManager.attach(activity.getWindow());
        mBackgroundTarget = new PicassoBackgroundManagerTarget(mBackgroundManager);
        mMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
    }

    public void startUpdateBackgroundTimer(int delay) {
        if (mBackgroundTimer != null) {
            mBackgroundTimer.cancel();
        }
        mBackgroundTimer = new Timer();
        mBackgroundTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.v("Picasso", "Updating background");
                        Picasso.get()
                                .load("https://picsum.photos/"
                                        + mMetrics.widthPixels
                                        + "/"
                                        + mMetrics.heightPixels
                                        + "/?blur=10")
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .error(mDefaultBackground)
                                .into(mBackgroundTarget);
                    }
                });
            }
        }, delay);
    }

    // * Picasso target for updating default_background images
    public static class PicassoBackgroundManagerTarget implements Target {
        BackgroundManager mBackgroundManager;

        public PicassoBackgroundManagerTarget(BackgroundManager backgroundManager) {
            this.mBackgroundManager = backgroundManager;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            this.mBackgroundManager.setBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            this.mBackgroundManager.setDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {
            // Do nothing, default_background manager has its own transitions
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            PicassoBackgroundManagerTarget that = (PicassoBackgroundManagerTarget) o;

            if (!mBackgroundManager.equals(that.mBackgroundManager))
                return false;

            return true;
        }
    }
}