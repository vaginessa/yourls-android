package de.mateware.ayourls;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Mate on 09.10.2015.
 */
public final class DataBinder {

    private static final Logger log = LoggerFactory.getLogger(DataBinder.class);

    private DataBinder() {
    }

    @BindingAdapter({"bind:keyword"})
    public static void setQr(ImageView imageView, String keyword) {

        Context context = imageView.getContext();

        int size = imageView.getWidth();

        String serverUrl = PreferenceManager.getDefaultSharedPreferences(context)
                                            .getString(context.getString(R.string.pref_key_server_url), null);
        if (!TextUtils.isEmpty(serverUrl)) {
            String linkUrl = Uri.parse(serverUrl)
                                .buildUpon()
                                .appendPath(keyword)
                                .build()
                                .toString();

            com.google.zxing.Writer writer = new QRCodeWriter();
            String finaldata = Uri.encode(linkUrl, "utf-8");

            BitMatrix bm = null;



            try {
                bm = writer.encode(finaldata, BarcodeFormat.QR_CODE,size, size);

                Bitmap ImageBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

                for (int i = 0; i < size; i++) {//width
                    for (int j = 0; j < size; j++) {//height
                        ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
                    }
                }

                if (ImageBitmap != null) {
                    imageView.setImageBitmap(ImageBitmap);
                }

            } catch (WriterException e) {
                e.printStackTrace();
            }

        }
    }
}
