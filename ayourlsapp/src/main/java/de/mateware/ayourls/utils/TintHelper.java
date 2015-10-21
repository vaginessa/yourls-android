package de.mateware.ayourls.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Created by mate on 16.10.2015.
 */
public class TintHelper {
    public static void tintMenu(@NonNull Context context, @NonNull Menu menu, @ColorRes int colorRes) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            Drawable drawable = item.getIcon();
            if (drawable != null) item.setIcon(tintDrawable(drawable, context, colorRes));
            if (item.hasSubMenu()) tintMenu(context, item.getSubMenu(), colorRes);
        }
    }

    public static Drawable tintDrawable(@NonNull Drawable drawable, @NonNull Context context, @ColorRes int colorRes) {
        drawable = DrawableCompat.wrap(drawable);
        drawable = drawable.mutate();
        DrawableCompat.setTint(drawable, ContextCompat.getColor(context, colorRes));
        return drawable;
    }

    public static void tintImageView(@NonNull ImageView imageView, @ColorRes int colorRes) {
        imageView.setImageDrawable(tintDrawable(imageView.getDrawable(), imageView.getContext(), colorRes));
    }

}
