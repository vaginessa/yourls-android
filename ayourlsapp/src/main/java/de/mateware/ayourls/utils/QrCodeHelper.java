package de.mateware.ayourls.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.EnumMap;
import java.util.Map;

import de.mateware.ayourls.R;

/**
 * Created by mate on 27.10.2015.
 */
public class QrCodeHelper {

    //private static final Logger log = LoggerFactory.getLogger(QrCodeHelper.class);

    private static QrCodeHelper instance;
    private final int color;
    private final int bgcolor;

    private LruCache<String, Bitmap> memoryCache;

    public static QrCodeHelper getInstance(Context context) {
        if (instance == null) instance = new QrCodeHelper(context.getApplicationContext());
        return instance;
    }

    private File qrDir;

    private QrCodeHelper(Context context) {
        final int maxMemory = (int) (Runtime.getRuntime()
                                            .maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return getBitmapByteCount(bitmap) / 1024;
            }

            public int getBitmapByteCount(Bitmap bitmap) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) return bitmap.getRowBytes() * bitmap.getHeight();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return bitmap.getByteCount();
                return bitmap.getAllocationByteCount();
            }
        };
        qrDir = new File(context.getFilesDir(), "qr");
        //noinspection ResultOfMethodCallIgnored
        qrDir.mkdirs();
        color = ContextCompat.getColor(context, R.color.primary_dark);
        bgcolor = ContextCompat.getColor(context, R.color.background);
    }

    private void addBitmapToMemoryCache(String shortUrl, int size, Bitmap bitmap) {
        if (getBitmapFromMemCache(shortUrl, size) == null) {
            memoryCache.put(getMemoryCacheKey(shortUrl, size), bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String shortUrl, int size) {
        return memoryCache.get(getMemoryCacheKey(shortUrl, size));
    }

    private String getMemoryCacheKey(String shortUrl, int size) {
        return String.valueOf(size) + shortUrl;
    }

    public Bitmap generateQrBitmap(String shortUrl, int size, int padding) throws WriterException {
        return generateQrBitmap(shortUrl, size, padding, color, bgcolor);
    }

    public Bitmap generateQrBitmap(String shortUrl, int size, int padding, int foregroundColor, int backgroundColor) throws WriterException {

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        QRCode qrCode = Encoder.encode(shortUrl, ErrorCorrectionLevel.L, hints);
        int width = qrCode.getMatrix()
                          .getWidth();
        int height = qrCode.getMatrix()
                           .getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = qrCode.getMatrix()
                                           .get(x, y) > 0 ? foregroundColor : backgroundColor;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        if ((padding * 2) > size) padding = size / 2 - 1;

        return addMarginToBitmap(getResizedBitmap(bitmap, size - padding * 2, size - padding * 2), padding, backgroundColor);

    }

    private static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static Bitmap addMarginToBitmap(Bitmap bitmap, int marginSize, int marginColor) {
        if (marginSize == 0) return bitmap;
        int newWidth = bitmap.getWidth() + marginSize * 2;
        int newHeight = bitmap.getHeight() + marginSize * 2;

        Bitmap bitmapWithMargin = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithMargin);
        canvas.drawColor(marginColor);
        canvas.drawBitmap(bitmap, marginSize, marginSize, new Paint(Paint.FILTER_BITMAP_FLAG));
        bitmap.recycle();
        return bitmapWithMargin;
    }


    @BindingAdapter(value = {"shorturl", "qrsize"}, requireAll = false)
    public static void setQrToImageView(ImageView imageView, String shorturl, int qrsize) {
        if (!TextUtils.isEmpty(shorturl)) {

            QrCodeHelper helper = QrCodeHelper.getInstance(imageView.getContext());

            Bitmap bitmap = helper.getBitmapFromMemCache(shorturl, qrsize);

            if (bitmap != null) imageView.setImageBitmap(bitmap);
            else {
                helper.new BitmapWorkerTask(imageView).execute(new Pair<String, Integer>(shorturl, qrsize));
            }
        }
    }

    class BitmapWorkerTask extends AsyncTask<Pair<String, Integer>, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Pair<String, Integer>... params) {
            String shorturl = params[0].first;
            int size = params[0].second;
            try {
                Bitmap bitmap = instance.generateQrBitmap(shorturl, size, 0);
                addBitmapToMemoryCache(shorturl, size, bitmap);
                return bitmap;
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }


}
