package ru.kostopraff.dreamless;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        List<ConnectionFromList> connections = new ArrayList<ConnectionFromList>();

        RecyclerView recyclerView = view.findViewById(R.id.list_connections);
        ConnectionsListAdapter adapter = new ConnectionsListAdapter(connections, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        ConnectionFromList smbConnection = new ConnectionFromList();
        smbConnection.setIcon(1);
        smbConnection.setName("Samba");
        connections.add(smbConnection);
        ConnectionFromList galleryConnection = new ConnectionFromList();
        galleryConnection.setIcon(2);
        galleryConnection.setName("Gallery");
        connections.add(galleryConnection);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(itemAnimator);

        return view;
    }

}
