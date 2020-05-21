package ru.kostopraff.dreamless.activities;

import android.content.ComponentName;
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
import com.vk.api.sdk.auth.VKScope;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ru.kostopraff.dreamless.PicassoBackgroundManager;
import ru.kostopraff.dreamless.R;

public class MasterSetupActivity extends FragmentActivity {

    private static final String TAG = MasterSetupActivity.class.getSimpleName();

    private static SharedPreferences mSharedPreferences;

    /* Action ID definition */
    private static final int ACTION_CONTINUE = 0;
    private static final int ACTION_BACK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mSharedPreferences = getSharedPreferences("Dreamless", Context.MODE_PRIVATE);
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
            String title = "Мастер настройки Dreamless";
            String breadcrumb = "Вас приветствует";
            String description = "Давайте настроим нашу заставку";
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List actions, Bundle savedInstanceState) {
            addAction(actions, ACTION_CONTINUE, "Начать", "");
            addAction(actions, ACTION_BACK, "Выход", "");
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
            String title = "Формат времени";
            String breadcrumb = "Мастер настройки Dreamless";
            String description = "Упустишь минуту - потеряешь час...";
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }
        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
            addAction(actions, FORMAT24HHMM, "HH:MM", "24 часовой");
            addAction(actions, FORMAT24HHMMSS, "HH:MM:SS", "24 часовой");
            addAction(actions, FORMAT12HHMM, "HH:MM AM/PM", "12 часовой");
            addAction(actions, FORMAT12HHMMSS, "HH:MM:SS AM/PM", "12 часовой");
            addAction(actions, FORMATOFF, "Не показывать время", "Счастливые часов не наблюдают...");
        }
        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case FORMAT24HHMM:
                    mSharedPreferences.edit().putString("FORMAT_TIME", "H:mm")
                            .putBoolean("TIME", true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMAT24HHMMSS:
                    mSharedPreferences.edit().putString("FORMAT_TIME", "H:mm:ss")
                            .putBoolean("TIME", true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMAT12HHMM:

                    mSharedPreferences.edit().putString("FORMAT_TIME", "h:mm aa")
                            .putBoolean("TIME", true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMAT12HHMMSS:
                    mSharedPreferences.edit().putString("FORMAT_TIME", "h:mm:ss aa")
                            .putBoolean("TIME", true).apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new DateFormatFragment());
                    break;
                case FORMATOFF:
                    mSharedPreferences.edit().putBoolean("TIME", false).apply();
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
            String title = "Формат даты";
            String breadcrumb = "Мастер настройки Dreamless";
            String description = "Одно «сейчас» лучше трех «потом»...";
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }
        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
            addAction(actions, FORMATDATESIMPLE, "1.01.70", "");
            addAction(actions, FORMATDATESIMPLESHORTDAY, "1.01.70 чт.", "");
            addAction(actions, FORMATDATESIMPLEALT, "1970.01.01", "");
            addAction(actions, FORMATDATESIMPLEALTSHORTDAY, "1970.01.01 чт.", "");
            addAction(actions, FORMATDATEFULLDAY, "Четверг, 1 января 1970", "");
            addAction(actions, FORMATDATEFULLDAYTHIN, "Четверг, 1 января", "");
            addAction(actions, FORMATDATESHORTFULLDAY, "Четверг, 1 янв. 1970", "");
            addAction(actions, FORMATDATESHORTFULLDAYTHIN, "Четверг, 1 янв.", "");
            addAction(actions, FORMATDATEDAY, "чт. 1 января 1970 ", "");
            addAction(actions, FORMATDATEDAYTHIN, "чт. 1 января", "");
            addAction(actions, FORMATDATESHORTDAY, "чт. 1 янв. 1970", "");
            addAction(actions, FORMATDATESHORTDAYTHIN, "чт. 1 янв.", "");
            addAction(actions, FORMATDATE, "1 января 1970", "");
            addAction(actions, FORMATDATETHIN, "1 января", "");
            addAction(actions, FORMATDATEDEFAULT, "1 января, четверг", "");
            addAction(actions, FORMATDATESHORT, "1 янв. 1970", "");
            addAction(actions, FORMATDATETHINNEST, "1 янв.", "");
            addAction(actions, FORMATDATESTUPID, "1 янв. чт.", "");
            addAction(actions, FORMATOFF, "Не показывать дату", "Года - что вода...");
        }
        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case FORMATDATESIMPLE:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d.MM.yy").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());

                    break;
                case FORMATDATESIMPLESHORTDAY:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d.MM.yy E").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESIMPLEALT:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "yyyy.MM.dd").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESIMPLEALTSHORTDAY:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "yyyy.MM.dd E").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEFULLDAY:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "EEEE, d MMMM yyyy").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEFULLDAYTHIN:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "EEEE, d MMMM").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORTFULLDAY:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "EEEE, d MMM. yyyy").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORTFULLDAYTHIN:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "EEEE, d MMM.").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEDAY:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "E. d MMMM yyyy").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEDAYTHIN:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "E. d MMMM").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORTDAY:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "E. d MMM. yyyy").apply();

                    break;
                case FORMATDATESHORTDAYTHIN:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "E. d MMM.").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATE:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d MMMM yyyy").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATETHIN:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d MMMM").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATEDEFAULT:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d MMMM, EEEE").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESHORT:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d MMM. yyyy").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATETHINNEST:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d MMM.").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATDATESTUPID:
                    mSharedPreferences.edit().putBoolean("DATE", true)
                            .putString("FORMAT_DATE", "d MMM. E.").apply();
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new VkStepFragment());
                    break;
                case FORMATOFF:
                    mSharedPreferences.edit().putBoolean("DATE", false).apply();
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
            String title = "Авторизация в соц. сетях";
            String breadcrumb = "Мастер настройки Dreamless";
            String description = "Это необходимо для получения уведомлений";
            Drawable icon = Objects.requireNonNull(getActivity()).getDrawable(R.mipmap.dreamless_icon);
            return new GuidanceStylist.Guidance(title, description, breadcrumb, icon);
        }

        @Override
        public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
            if(!VK.isLoggedIn())
                addAction(actions, ACTION_VK, "Авторизация VK", "");
            else addAction(actions, ACTION_VK, "Вы уже авторизованы в VK", "Выйти?");
            addAction(actions, ACTION_SKIP, "Выход из мастера настройки", "");
            addAction(actions, ACTION_TEST, "Тестовый запуск","");
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