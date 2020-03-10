package ru.kostopraff.dreamless;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.service.dreams.DreamService;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import ru.kostopraff.dreamless.R;
import ru.kostopraff.dreamless.Unsplash;

public class PhotoDream extends DreamService {

    private ImageView imageView;
    private Drawable drawable;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        setFullscreen(true);
        setInteractive(false);
        setContentView(R.layout.dream_photo);

        imageView = findViewById(R.id.dream_photo);
        imageView.setImageResource(R.drawable.test_squirrel);
        Timer timer = new Timer();
        MyTask loadImage = new MyTask();
        timer.schedule(loadImage, 0,30000);
    }

    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

    }

    private class MyTask extends TimerTask{
        @Override
        public void run() {
            Dream dream = new Dream();
            dream.execute();
        }
    }

    private class Dream extends AsyncTask<Void, Void, String>{
        HttpURLConnection http;
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://api.unsplash.com/photos/random/?client_id=7AxaQaI1SBW6_mEmazBw3_EQvC6ljJLKRrBYphexCzc");
                http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("GET");
                http.connect();

                InputStream inputStream = http.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                StringBuilder builder = new StringBuilder();
                while (scanner.hasNextLine()) {
                    builder.append(scanner.nextLine());
                }
                return builder.toString();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Gson g = new Gson();
            Unsplash unsplash = g.fromJson(s, Unsplash.class);

            drawable = imageView.getDrawable();
            Picasso.get()
                    .load(unsplash.getUrls().getRegular())
                    .placeholder(drawable)
                    .priority(Picasso.Priority.HIGH)
                    .into(imageView);
        }
    }
}
