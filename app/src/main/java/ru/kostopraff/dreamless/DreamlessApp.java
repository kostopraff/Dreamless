package ru.kostopraff.dreamless;

import android.app.Application;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.VKTokenExpiredHandler;

public class DreamlessApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VK.addTokenExpiredHandler(tokenTracker);
    }
    private VKTokenExpiredHandler tokenTracker = new VKTokenExpiredHandler() {
        @Override
        public void onTokenExpired() {

        }
    };
}
