package ru.kostopraff.dreamless.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.vk.api.sdk.VK;
import com.vk.api.sdk.auth.VKAccessToken;
import com.vk.api.sdk.auth.VKAuthCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import ru.kostopraff.dreamless.R;

public class MasterSetupActivity extends FragmentActivity {

    private static final String TAG = MasterSetupActivity.class.getSimpleName();

    private static SharedPreferences mSharedPreferences;

    private static final int ACTION_CONTINUE = 0;
    private static final int ACTION_BACK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (null == savedInstanceState) {
            GuidedStepSupportFragment.addAsRoot(this, new StartFragment(), android.R.id.content);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        VKAuthCallback callback = new VKAuthCallback() {
            @Override
            public void onLogin(@NotNull VKAccessToken vkAccessToken) {
                finish();
            }

            @Override
            public void onLoginFailed(int i) {
                startActivity(new Intent(getApplicationContext(), ErrorActivity.class));
            }
        };
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private static void addAction(List actions, long id, String title, String desc) {
        actions.add(new GuidedAction.Builder()
                .id(id)
                .title(title)
                .description(desc)
                .build());
    }

    public static class StartFragment extends GuidedStepSupportFragment {
        @NonNull
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            String title = getString(R.string.master_title);
            String breadcrumb = getString(R.string.master_breadcrumb);
            String description = getString(R.string.master_desc);
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List actions, Bundle savedInstanceState) {
            addAction(actions, ACTION_CONTINUE, getString(R.string.start), "");
            addAction(actions, ACTION_BACK, getString(R.string.exit), getString(R.string.accept_defaults));
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case ACTION_CONTINUE:
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new HourFormatFragment());
                    break;
                case ACTION_BACK:
                    Objects.requireNonNull(getActivity()).finish();
                    break;
                default:
                    Log.w(TAG, "Action is not defined");
                    startActivity(new Intent(getActivity(), ErrorActivity.class));
                    break;
            }
        }
    }

    public static class HourFormatFragment extends GuidedStepSupportFragment{
        private static final int FORMAT12HHMM = 121;
        private static final int FORMAT12HHMMSS = 122;
        private static final int FORMAT24HHMM = 241;
        private static final int FORMAT24HHMMSS = 242;
        private static final int FORMATOFF = -1;
        @NonNull
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            String title = getString(R.string.master_time_title);
            String breadcrumb = getString(R.string.master_time_breadcrumb);
            String description = getString(R.string.master_time_desc);
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }
        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
            addAction(actions, FORMAT24HHMM, getString(R.string.format24hhmm_text), getString(R.string.format24));
            addAction(actions, FORMAT24HHMMSS, getString(R.string.format24hhmmss_text), getString(R.string.format24));
            addAction(actions, FORMAT12HHMM, getString(R.string.format12hhmm_text), getString(R.string.format12));
            addAction(actions, FORMAT12HHMMSS, getString(R.string.format12hhmmss_text), getString(R.string.format12));
            addAction(actions, FORMATOFF, getString(R.string.format_time_off), getString(R.string.rofl1));
        }
        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case FORMAT24HHMM:
                    mSharedPreferences.edit().putString("FORMAT_TIME", getString(R.string.format24hmm))
                            .putBoolean(getString(R.string.time), true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMAT24HHMMSS:
                    mSharedPreferences.edit().putString("FORMAT_TIME", getString(R.string.format24hmmss))
                            .putBoolean(getString(R.string.time), true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMAT12HHMM:
                    mSharedPreferences.edit().putString("FORMAT_TIME", getString(R.string.format12hmma))
                            .putBoolean(getString(R.string.time), true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMAT12HHMMSS:
                    mSharedPreferences.edit().putString("FORMAT_TIME", getString(R.string.format12hmmssa))
                            .putBoolean(getString(R.string.time), true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMATOFF:
                    mSharedPreferences.edit().putBoolean(getString(R.string.time), false).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
            }
        }
    }

    public static class DateFormatFragment extends GuidedStepSupportFragment{ //1января1970четверг
        private static final int FORMATDATESIMPLE = 300;                // D.MM.YY
        private static final int FORMATDATESIMPLESHORTDAY = 301;        // D.MM.YY E
        private static final int FORMATDATESIMPLEALT = 302;             // YYYY.MM.DD
        private static final int FORMATDATESIMPLEALTSHORTDAY = 303;     // YYYY.MM.DD E
        private static final int FORMATDATEFULLDAY = 304;               // EEEE, d MMMM yyyy
        private static final int FORMATDATEFULLDAYTHIN = 305;           // EEEE, d MMMM
        private static final int FORMATDATESHORTFULLDAY = 306;          // EEEE, d MMM. yyyy
        private static final int FORMATDATESHORTFULLDAYTHIN = 307;      // EEEE, d MMM.
        private static final int FORMATDATEDAY = 308;                   // E. d MMMM yyyy
        private static final int FORMATDATEDAYTHIN = 309;               // E. d MMMM
        private static final int FORMATDATESHORTDAY = 310;              // E. d MMM. yyyy
        private static final int FORMATDATESHORTDAYTHIN = 311;          // E. d MMM.
        private static final int FORMATDATE = 312;                      // d MMMM yyyy
        private static final int FORMATDATETHIN = 313;                  // d MMMM
        private static final int FORMATDATEDEFAULT = 314;               // d MMMM, EEEE
        private static final int FORMATDATESHORT = 315;                 // d MMM. yyyy
        private static final int FORMATDATETHINNEST = 316;              // d MMM.
        private static final int FORMATDATESTUPID = 317;                // d MMM. E.
        private static final int FORMATOFF = -1;
        @NonNull
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            String title = getString(R.string.master_date_title);
            String breadcrumb = getString(R.string.master_date_breadcrumb);
            String description = getString(R.string.rofl2);
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }
        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
            addAction(actions, FORMATDATESIMPLE, getString(R.string.formatdatesimple_text), "");
            addAction(actions, FORMATDATESIMPLESHORTDAY, getString(R.string.formatdatesimpleshortday_text), "");
            addAction(actions, FORMATDATESIMPLEALT, getString(R.string.formatdatesimplealt_text), "");
            addAction(actions, FORMATDATESIMPLEALTSHORTDAY, getString(R.string.formatdatesimplealtshortday_text), "");
            addAction(actions, FORMATDATEFULLDAY, getString(R.string.formatdatefullday_text), "");
            addAction(actions, FORMATDATEFULLDAYTHIN, getString(R.string.formatdatefulldaythin_text), "");
            addAction(actions, FORMATDATESHORTFULLDAY, getString(R.string.formatdateshortfullday_text), "");
            addAction(actions, FORMATDATESHORTFULLDAYTHIN, getString(R.string.formatdateshortfulldaythin_text), "");
            addAction(actions, FORMATDATEDAY, getString(R.string.formatdateday_text), "");
            addAction(actions, FORMATDATEDAYTHIN, getString(R.string.formatdatedaythin_text), "");
            addAction(actions, FORMATDATESHORTDAY, getString(R.string.formatdateshortday_text), "");
            addAction(actions, FORMATDATESHORTDAYTHIN, getString(R.string.formatdateshortdaythin_text), "");
            addAction(actions, FORMATDATE, getString(R.string.formatdate_text), "");
            addAction(actions, FORMATDATETHIN, getString(R.string.formatdatethin_text), "");
            addAction(actions, FORMATDATEDEFAULT, getString(R.string.formatdatedefault_text), "");
            addAction(actions, FORMATDATESHORT, getString(R.string.formatdateshort_text), "");
            addAction(actions, FORMATDATETHINNEST, getString(R.string.formatdatethinnest_text), "");
            addAction(actions, FORMATDATESTUPID, getString(R.string.formatdatestupid_text), "");
            addAction(actions, FORMATOFF, getString(R.string.formate_date_off), getString(R.string.rofl3));
        }
        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case FORMATDATESIMPLE:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatesimple)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());

                    break;
                case FORMATDATESIMPLESHORTDAY:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatesimpleshortday)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESIMPLEALT:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatesimplealt)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESIMPLEALTSHORTDAY:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatesimplealtshortday)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEFULLDAY:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatefullday)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEFULLDAYTHIN:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatefulldaythin)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORTFULLDAY:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdateshortfullday)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORTFULLDAYTHIN:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdateshortfulldaythin)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEDAY:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdateday)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEDAYTHIN:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatedaythin)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORTDAY:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdateshortday)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORTDAYTHIN:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdateshortdaythin)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATE:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdate)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATETHIN:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatethin)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEDEFAULT:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatedefault)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORT:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdateshort)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATETHINNEST:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatethinnest)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESTUPID:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), true)
                            .putString("FORMAT_DATE", getString(R.string.formatdatestupid)).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATOFF:
                    mSharedPreferences.edit().putBoolean(getString(R.string.date), false).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
            }
        }
    }

    public static class VkStepFragment extends GuidedStepSupportFragment{
        private static final int ACTION_TEST = -1;
        private static final int ACTION_VK = 10;
        private static final int ACTION_SKIP = 2;
        @NonNull
        @Override
        public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {
            String title = getString(R.string.master_vk_title);
            String breadcrumb = getString(R.string.master_vk_breadcrumb);
            String description = getString(R.string.master_vk_desc);
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
            if(!VK.isLoggedIn())
                addAction(actions, ACTION_VK, getString(R.string.auth_vk), "");
            else addAction(actions, ACTION_VK, getString(R.string.auth_vk_ok), getString(R.string.auth_vk_ok_quit));
            addAction(actions, ACTION_SKIP, getString(R.string.master_quit), "");
            addAction(actions, ACTION_TEST, getString(R.string.test_launch),"");
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case ACTION_VK:
                    if(!VK.isLoggedIn()){
                        mSharedPreferences.edit().putBoolean("MASTER", true).apply();
                        VK.login(Objects.requireNonNull(this.getActivity()));
                    }
                    else {
                        mSharedPreferences.edit().putBoolean("MASTER", true).apply();
                        VK.logout();
                        finishGuidedStepSupportFragments();
                    }
                    break;
                case ACTION_SKIP:
                    mSharedPreferences.edit().putBoolean("MASTER", true).apply();
                    finishGuidedStepSupportFragments();
                    break;
                case ACTION_TEST:
                    mSharedPreferences.edit().putBoolean("MASTER", true).apply();
                    Intent intentDream = new Intent(Intent.ACTION_MAIN);
                    intentDream.setClassName("com.android.systemui", "com.android.systemui.Somnambulator");
                    startActivity(intentDream);
                    break;
                default:
                    Log.w(TAG, "Action is not defined");
                    startActivity(new Intent(getActivity(), ErrorActivity.class));
                    break;
            }
        }
    }
}