package de.mateware.ayourls.utils;

import android.content.Context;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mate on 27.10.2015.
 */
public class QrCodeHelper {

    private static QrCodeHelper instance;

    public static QrCodeHelper getInstance(Context context) {
        if (instance == null) instance = new QrCodeHelper(context.getApplicationContext());
        return instance;
    }

    private File qrDir;

    private QrCodeHelper(Context context) {
        qrDir = new File(context.getFilesDir(), "qr");
        qrDir.mkdirs();
    }

    public void generateQr(String shortUrl) throws IOException {
        String hash = getHash(shortUrl);
        File qrImageFile = new File(qrDir, hash + ".png");
        if (qrImageFile.exists()) qrImageFile.delete();
        if (qrImageFile.createNewFile()) {
            FileOutputStream fOut = new FileOutputStream(qrImageFile);
            QRCode.from(shortUrl)
                  .withCharset(HashHelper.UTF_8)
                  .withSize(400, 400)
                  .writeTo(fOut);
            fOut.flush();
            fOut.close();
        }
    }

    public File getQrFile(String shortUrl) throws FileNotFoundException {
        String hash = getHash(shortUrl);
        File qrImageFile = new File(qrDir, hash + ".png");
        if (qrImageFile.exists())
            return qrImageFile;
        throw new FileNotFoundException("QrImage for "+shortUrl+ " not found");
    }

    public String getHash(String shortUrl){
        String hash;
        try {
            hash = HashHelper.getHash(shortUrl, HashHelper.UTF_8, HashHelper.SHA_1);
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            hash = shortUrl.replaceAll("[^a-zA-Z0-9]+", "");
        }
        return hash;
    }
}
