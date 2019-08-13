package vn.hailt.hdwallpaper.ultis;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Build;
import android.view.View;

public class ShowPhotoUtils {

    public static void showOnlyPhoto(Activity activity, View[] views) {

        for (View view : views) {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOut.setDuration(500);
            fadeOut.start();
        }

        setFullscreen(activity);
    }

    public static void exitShowOnlyPhoto(Activity activity, View[] views) {

        for (View view : views) {
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeIn.setDuration(500);
            fadeIn.start();
        }

        exitFullscreen(activity);
    }

    private static void setFullscreen(Activity activity) {
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (Build.VERSION.SDK_INT >= 19) {
            flags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(flags);
    }

    private static void exitFullscreen(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }
}
