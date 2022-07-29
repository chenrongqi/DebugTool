package com.crqi.deeplink.qrcodescan.zxing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Hashtable;

public class DecoderLocalFile {
    private static int reqHeight = 170;
    private static int reqWidth = 170;

    public static String getDecodeUrl(String picturePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                if ((halfHeight / inSampleSize) < reqHeight
                        && (halfWidth / inSampleSize) < reqWidth) {
                    String result = decodeBitmap(picturePath, options, inSampleSize);
                    if (!"-1".equals(result)) {
                        return result;
                    }
                }
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                    String result = decodeBitmap(picturePath, options, inSampleSize);
                    if (!"-1".equals(result)) {
                        return result;
                    }
                }
            } else {
                String result = decodeBitmap(picturePath, options, inSampleSize);
                if (!"-1".equals(result)) {
                    return result;
                }
            }
            return "-1";
        } catch (Exception e) {
            return "-1";
        }
    }

    private static String decodeBitmap(String picturePath, BitmapFactory.Options options, int inSampleSize) {
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(picturePath, options);
        String result = handleQRCodeFormPhoto(bm);
        return result;
    }

    public static String handleQRCodeFormPhoto(Bitmap bitmap) {
        Hashtable<DecodeHintType, String> tab = new Hashtable<DecodeHintType, String>();
        tab.put(DecodeHintType.CHARACTER_SET, "utf-8");

        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        // 转成二进制图片
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        // 实例化二维码解码对象
        MultiFormatReader multiFormatReader = new MultiFormatReader();
        Result result;
        try {
            // 根据解码类型解码，返回解码结果
            result = multiFormatReader.decode(bitmap1, tab);
            return result.getText();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return "-1";
        }
    }
}
