package ru.kostopraff.dreamless.activities;

import android.content.Intent;
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

import ru.kostopraff.dreamless.R;

public class GuidedStepActivity extends FragmentActivity {

    private static final String TAG = GuidedStepActivity.class.getSimpleName();

    /* Action ID definition */
    private static final int ACTION_CONTINUE = 0;
    private static final int ACTION_BACK = 1;
    private static final int ACTION_TEST = -1;
    private static final int ACTION_VK = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
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
            addAction(actions, ACTION_TEST, "Тестовый запуск","");
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case ACTION_CONTINUE:
                    GuidedStepSupportFragment.add(Objects.requireNonNull(getFragmentManager()), new FirstStepFragment());
                    break;
                case ACTION_BACK:
                    Objects.requireNonNull(getActivity()).finish();
                    break;
                default:
                    Log.w(TAG, "Action is not defined");
                    break;
            }
        }
    }

    public static class FirstStepFragment extends GuidedStepSupportFragment{
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
            addAction(actions, ACTION_BACK, "Выход", "");
            addAction(actions, ACTION_TEST, "Тестовый запуск","");
        }

        @Override
        public void onGuidedActionClicked(GuidedAction action) {
            switch ((int) action.getId()){
                case ACTION_VK:
                    if(!VK.isLoggedIn()){
                        VK.login(Objects.requireNonNull(this.getActivity()),
                                Collections.singleton(VKScope.NOTIFICATIONS));
                    }
                    else {
                        VK.logout();
                        Objects.requireNonNull(getActivity()).finish();
                    }
                    break;
                case ACTION_BACK:
                    Objects.requireNonNull(getActivity()).finish();
                    break;
                case ACTION_TEST:
                    Intent intentDream = new Intent(Intent.ACTION_MAIN);
                    intentDream.setClassName("com.android.systemui", "com.android.systemui.Somnambulator");
                    startActivity(intentDream);
                default:
                    Log.w(TAG, "Action is not defined");
                    break;
            }
        }
    }
}