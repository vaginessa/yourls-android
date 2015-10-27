package de.mateware.ayourls.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

import de.mateware.ayourls.R;

/**
 * Created by mate on 27.10.2015.
 */
public class QrCodeHelper {

    private static QrCodeHelper instance;
    private final int color;
    private final int bgcolor;

    public static QrCodeHelper getInstance(Context context) {
        if (instance == null) instance = new QrCodeHelper(context.getApplicationContext());
        return instance;
    }

    private File qrDir;

    private QrCodeHelper(Context context) {
        qrDir = new File(context.getFilesDir(), "qr");
        //noinspection ResultOfMethodCallIgnored
        qrDir.mkdirs();
        color = ContextCompat.getColor(context, R.color.primary_dark);
        bgcolor = ContextCompat.getColor(context, R.color.background);
    }

    public Bitmap generateQrBitmap(String shortUrl, int size, int margin, int foregroundColor, int backgroundColor) throws WriterException {
        BarcodeFormat format = BarcodeFormat.QR_CODE;
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, margin); /* default = 4 */
        BitMatrix bitMatrix = new MultiFormatWriter().encode(shortUrl, format, size, size, hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? foregroundColor : backgroundColor;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public void generateQr(String shortUrl) throws IOException, WriterException {
        File qrImageFile = getFile(shortUrl);
        if (qrImageFile.exists()) //noinspection ResultOfMethodCallIgnored
            qrImageFile.delete();
        if (qrImageFile.createNewFile()) {
            FileOutputStream fOut = new FileOutputStream(qrImageFile);
            generateQrBitmap(shortUrl,400,0, color,bgcolor).compress(Bitmap.CompressFormat.PNG, 80, fOut);
            fOut.flush();
            fOut.close();
        }
    }

    public File getQrFile(String shortUrl) throws FileNotFoundException {
        File qrImageFile = getFile(shortUrl);
        if (qrImageFile.exists())
            return qrImageFile;
        throw new FileNotFoundException("QrImage for " + shortUrl + " not found");
    }

    public Bitmap getQrBitmapFromFile(String shortUrl, int size) throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(getQrFile(shortUrl));
            return getResizedBitmap(BitmapFactory.decodeStream(fis, null, options), size, size);
        } finally {
            if (fis != null)
                fis.close();
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
    }

    public boolean deleteQrFile(String shortUrl) {
        File qrImageFile = getFile(shortUrl);
        return qrImageFile.delete();
    }

    private File getFile(String shortUrl) {
        return new File(qrDir, getHash(shortUrl) + "." + getFileEndingForBitmapCompressFormat());
    }

    private String getHash(String shortUrl) {
        String hash;
        try {
            hash = HashHelper.getHash(shortUrl, HashHelper.UTF_8, HashHelper.SHA_1);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            hash = shortUrl.replaceAll("[^a-zA-Z0-9]+", "");
        }
        return hash;
    }

    @SuppressLint("NewApi")
    private String getFileEndingForBitmapCompressFormat() {
        return getBitmapCompressFormat().name()
                                        .toLowerCase(Locale.getDefault());
    }

    private Bitmap.CompressFormat getBitmapCompressFormat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return Bitmap.CompressFormat.WEBP;
        } else
            return Bitmap.CompressFormat.PNG;
    }
}
