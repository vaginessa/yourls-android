package de.mateware.ayourls;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
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
            final Context context = imageView.getContext();
            int size = context.getResources()
                              .getDimensionPixelSize(R.dimen.qr_size_library);
            if (size > 1000) size = 1000;

            int color = ContextCompat.getColor(context, R.color.primary_dark);
            int bgcolor = ContextCompat.getColor(context, R.color.background);

            Uri.Builder qrGenerationUriBuilder = new Uri.Builder();
            qrGenerationUriBuilder.scheme("https")
                                  .authority("api.qrserver.com")
                                  .appendEncodedPath("v1")
                                  .appendEncodedPath("create-qr-code")
                                  .appendQueryParameter("data", url)
                                  .appendQueryParameter("charset-source", "UTF-8")
                                  .appendQueryParameter("color", String.valueOf(Color.red(color)) + "-" + String.valueOf(Color.green(color)) + "-" + String.valueOf(Color.blue(color)))
                                  .appendQueryParameter("bgcolor", String.valueOf(Color.red(bgcolor)) + "-" + String.valueOf(Color.green(bgcolor)) + "-" + String.valueOf(Color.blue(bgcolor)))
                                  .appendQueryParameter("size", size + "x" + size);

            Picasso.with(context)
                   .load(qrGenerationUriBuilder.build())
                   .into(imageView, new Callback() {
                       @Override
                       public void onSuccess() {
                           log.debug("call onQrImageLoaded");
                           if (context instanceof QrImageLoaderCallback) {
                               ((QrImageLoaderCallback) context).onQrImageLoaded();
                           }
                       }

                       @Override
                       public void onError() {
                           log.error("call onQrImageLoaded");
                           if (context instanceof QrImageLoaderCallback) {
                               ((QrImageLoaderCallback) context).onQrImageLoaded();
                           }
                       }
                   });
        }
    }

    public interface QrImageLoaderCallback {
        void onQrImageLoaded();
    }


}
