package de.mateware.ayourls;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Mate on 09.10.2015.
 */
public final class DataBinder {

    private static final Logger log = LoggerFactory.getLogger(DataBinder.class);

    private DataBinder() {
    }

    @BindingAdapter({"bind:shorturl"})
    public static void setQr(ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            Context context = imageView.getContext();
            int size = 500;
            Uri.Builder qrGenerationUriBuilder = new Uri.Builder();
            qrGenerationUriBuilder.scheme("https")
                                  .authority("chart.googleapis.com")
                                  .appendEncodedPath("chart")
                                  .appendQueryParameter("cht", "qr")
                                  .appendQueryParameter("chs", size + "x" + size)
                                  .appendQueryParameter("chl", url);
            Picasso.with(context)
                   .load(qrGenerationUriBuilder.build())
                   .into(imageView);
        }
    }

}
