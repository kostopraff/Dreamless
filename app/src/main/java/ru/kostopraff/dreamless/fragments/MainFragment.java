package ru.kostopraff.dreamless.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.app.BrowseSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.DividerRow;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.OnItemViewClickedListener;
import androidx.leanback.widget.OnItemViewSelectedListener;
import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.Row;
import androidx.leanback.widget.RowPresenter;
import androidx.leanback.widget.SectionRow;

import java.util.Objects;

import ru.kostopraff.dreamless.PicassoBackgroundManager;
import ru.kostopraff.dreamless.R;
import ru.kostopraff.dreamless.activities.ErrorActivity;
import ru.kostopraff.dreamless.activities.MasterSetupActivity;

public class MainFragment extends BrowseSupportFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private ArrayObjectAdapter mRowsAdapter;
    private PicassoBackgroundManager picassoBackgroundManager;

    private static final int GRID_ITEM_WIDTH = 300;
    private static final int GRID_ITEM_HEIGHT = 200;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        picassoBackgroundManager = new PicassoBackgroundManager(Objects.requireNonNull(getActivity()));
        picassoBackgroundManager.startUpdateBackgroundTimer(0);
        setupUIElements();
        loadRows();
        setupEventListeners();
    }

    private void setupUIElements() {
        setTitle(getString(R.string.app_name));
        //setBadgeDrawable(getResources().getDrawable(R.drawable.dreamless_badge));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(false);
        setBrandColor(getResources().getColor(R.color.headers));
    }

    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private void loadRows() {
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        /* GridItemPresenter */
        HeaderItem gridItemPresenterHeader = new HeaderItem(0, "Testing app section");
        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add("Мастер первоначальной настройки");
        gridRowAdapter.add("Start Dream");
        gridRowAdapter.add("Test error fragment");
        gridRowAdapter.add("Dream settings");
        mRowsAdapter.add(new SectionRow("Anything"));
        mRowsAdapter.add(new DividerRow());
        mRowsAdapter.add(new ListRow(gridItemPresenterHeader, gridRowAdapter));

        /* set */
        setAdapter(mRowsAdapter);
    }

    private class GridItemPresenter extends Presenter {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent) {
            TextView view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setBackgroundColor(getResources().getColor(R.color.default_background));
            view.setTextColor(Color.WHITE);
            view.setGravity(Gravity.CENTER);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, Object item) {
            ((TextView) viewHolder.view).setText((String) item);
        }

        @Override
        public void onUnbindViewHolder(ViewHolder viewHolder) {

        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            // each time the item is clicked, code inside here will be executed.
            if (item instanceof String) {
                if (item == "Мастер первоначальной настройки") {
                    startActivity(new Intent(getActivity(), MasterSetupActivity.class));
                } else if (item == "Start Dream"){
                    Intent intentDream = new Intent(Intent.ACTION_MAIN);
                    intentDream.setClassName("com.android.systemui", "com.android.systemui.Somnambulator");
                    startActivity(intentDream);
                } else if (item == "Test error fragment"){
                    startActivity(new Intent(getContext(), ErrorActivity.class));
                } else if (item == "Dream settings"){
                    ComponentName name = new ComponentName("com.android.tv.settings",
                            "com.android.tv.settings.device.display.daydream.DaydreamActivity");
                    Intent i=new Intent(Intent.ACTION_MAIN)
                            .addCategory(Intent.CATEGORY_LAUNCHER)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                            .setComponent(name);;
                    startActivity(i);
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (item instanceof String) { //gridpreseneter
                picassoBackgroundManager.startUpdateBackgroundTimer(500);
            }
        }
    }
}