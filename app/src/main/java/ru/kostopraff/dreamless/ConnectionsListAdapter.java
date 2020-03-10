package ru.kostopraff.dreamless;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.kostopraff.dreamless.fragments.SambaFragment;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ConnectionsListAdapter extends RecyclerView.Adapter<ConnectionsListAdapter.ViewHolder> {

    private List<ConnectionFromList> connectionsList;
    private Context context;

    public ConnectionsListAdapter(List<ConnectionFromList> connections, Context context) {
        this.connectionsList = connections;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connection, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConnectionFromList connection = connectionsList.get(position);
        holder.name.setText(connection.getName());
        holder.icon.setImageResource(R.drawable.test_squirrel);
        View.OnClickListener clickListener;
        switch (connection.getName()){
            case "Samba": {
                clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((AppCompatActivity) context).getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .replace(R.id.bottom_replace, new SambaFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                };
                break;
            }
            default: {
                clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "Fool!", Toast.LENGTH_SHORT).show();

                    }
                };
            }
        }
        holder.root.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return connectionsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView icon;
        private LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemConnectionName);
            icon = itemView.findViewById(R.id.itemConnectionIcon);
            root = itemView.findViewById(R.id.itemConnectionRoot);
        }

    }
}
