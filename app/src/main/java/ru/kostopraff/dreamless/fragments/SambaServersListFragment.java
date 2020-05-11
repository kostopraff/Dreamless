package ru.kostopraff.dreamless.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ImageCardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.Objects;
import java.util.Scanner;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import ru.kostopraff.dreamless.R;

public class SambaServersListFragment extends Fragment {
    private Context mContext;
    private View rootV;

    public SambaServersListFragment(Context context) {
        // Required empty public constructor
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootV = inflater.inflate(R.layout.fragment_samba_servers_list_fragment, container, false);
        new NetworkUsers(mContext, rootV).execute("smb://");
        return rootV;
    }

    private class NetworkUsers extends AsyncTask<String, String, String> {

        private Context asyncContext;
        private View rootView;
        StringBuilder result = null;

        private NetworkUsers(Context context, View rootView) {
            this.asyncContext = context;
            this.rootView = rootView;
        }

        @Override
        protected void onPostExecute(String result) {
            // your code, thats runs after "doInBackground" action
            if (result != null) {
                // Initialize a new CardView instance
                CardView cardView = new CardView(asyncContext);
                LinearLayoutCompat linearLayoutCompat = rootView.findViewById(R.id.servers_root_linear);
                // Initialize a new LayoutParams instance, CardView width and height
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                // Set margins for card view
                layoutParams.setMargins(20, 20, 20, 20);

                // Set the card view layout params
                cardView.setLayoutParams(layoutParams);

                // Set the card view content padding
                cardView.setPadding(25, 25, 25, 25);

                // Set the card view background color
                cardView.setBackgroundColor(Color.LTGRAY);

                // Set card view elevation
                cardView.setElevation(8f);

                // Finally, add the CardView in root layout
                linearLayoutCompat.addView(cardView);
            }
        }


        @Override
        protected String doInBackground(String... params) {
            SmbFile[] domains;
            StringBuilder result = new StringBuilder();
            try {
                domains = (new SmbFile("smb://")).listFiles();
                for (SmbFile domain : domains) {
                    result.append(domain);
                    SmbFile[] servers = domain.listFiles();
                    for (SmbFile server : servers) {
                        result.append("\t" + server);
                    }
                }
            } catch (SmbException | MalformedURLException e) {
                e.printStackTrace();
            }
            return Objects.requireNonNull(result).toString();
        }
    }
}
