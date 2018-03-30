package in.ajm.sb.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.ByteArrayOutputStream;

/**
 * Class used to perform image related actions.
 */
public class ImageHelper {
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public static Bitmap rotateImage(Context context, byte[] data, int rotation) {
        Bitmap bitmap = ImageHelper.convertBytesIntoBitmap(data);

        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false);

            oldBitmap.recycle();
        }

        return bitmap;
    }

    private static Bitmap convertBytesIntoBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
    }

    public static byte[] convertBitmapIntoBytes(Bitmap bmp, Bitmap.CompressFormat compressFormat) {
        if (compressFormat == null)
            compressFormat = Bitmap.CompressFormat.JPEG;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(compressFormat, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap decodeSampledBitmapFromByte(Context context, byte[] bitmapBytes) {
        int screenSizes[] = DisplayHelper.getDisplaySize(context);
        int reqWidth = screenSizes[0];
        int reqHeight = screenSizes[1];


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Load & resize the image to be 1/inSampleSize dimensions
        // Use when you do not want to scale the image with a inSampleSize that is a power of 2
        options.inScaled = true;
        options.inDensity = options.outWidth;
        options.inTargetDensity = reqWidth * options.inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false; // If set to true, the decoder will return null (no bitmap), but the out... fields will still be set, allowing the caller to query the bitmap without having to allocate the memory for its pixels.
        options.inPurgeable = true;         // Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inInputShareable = true;    // Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future

        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and height
     * <p/>
     * The function rounds up the sample size to a power of 2 or multiple
     * of 8 because BitmapFactory only honors sample size this way.
     * For example, BitmapFactory downsamples an image by 2 even though the
     * request is 3. So we round up the sample size to avoid OOM.
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int initialInSampleSize = computeInitialSampleSize(options, reqWidth, reqHeight);

        int roundedInSampleSize;
        if (initialInSampleSize <= 8) {
            roundedInSampleSize = 1;
            while (roundedInSampleSize < initialInSampleSize) {
                // Shift one bit to left
                roundedInSampleSize <<= 1;
            }
        } else {
            roundedInSampleSize = (initialInSampleSize + 7) / 8 * 8;
        }

        return roundedInSampleSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final double height = options.outHeight;
        final double width = options.outWidth;

        final long maxNumOfPixels = reqWidth * reqHeight;
        final int minSideLength = Math.min(reqHeight, reqWidth);

        int lowerBound = (maxNumOfPixels < 0) ? 1 :
                (int) Math.ceil(Math.sqrt(width * height / maxNumOfPixels));
        int upperBound = (minSideLength < 0) ? 128 :
                (int) Math.min(Math.floor(width / minSideLength),
                        Math.floor(height / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if (maxNumOfPixels < 0 && minSideLength < 0) {
            return 1;
        } else if (minSideLength < 0) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }


    public static Bitmap cropImage(Bitmap source, int x, int y, int width, int height) {
        return Bitmap.createBitmap(source, x, y, width, height);
    }

    private static Bitmap resizeImage(Bitmap source, int width, int height) {
        return Bitmap.createScaledBitmap(source, width, height, true);
    }

    public static BitmapMatchStatus matchBitmaps(Bitmap img1, Bitmap img2) {
        int TOLERANCE = 15;
        int MAX_WIDTH_HEIGHT = 100;

        int width, height;
        long totalPixels;
        int numOfDifferentPixels = 0;
        BitmapMatchStatus backgroundStatus = BitmapMatchStatus.Plain;

        width = img1.getWidth();
        height = img1.getHeight();

        if (width > MAX_WIDTH_HEIGHT || height > MAX_WIDTH_HEIGHT) {
            img1 = resizeImage(img1, MAX_WIDTH_HEIGHT, MAX_WIDTH_HEIGHT);
            img2 = resizeImage(img2, MAX_WIDTH_HEIGHT, MAX_WIDTH_HEIGHT);

            width = height = MAX_WIDTH_HEIGHT;
        }

        totalPixels = (long) width * (long) height;

        for (int x = 0; x < MAX_WIDTH_HEIGHT; x++) {
            for (int y = 0; y < MAX_WIDTH_HEIGHT; y++) {
                int pixel1 = img1.getPixel(x, y);
                int red1 = Color.red(pixel1);
                int blue1 = Color.blue(pixel1);
                int green1 = Color.green(pixel1);

                int pixel2 = img2.getPixel(x, y);
                int red2 = Color.red(pixel2);
                int blue2 = Color.blue(pixel2);
                int green2 = Color.green(pixel2);

                int redDiff = red1 - red2;
                int blueDiff = blue1 - blue2;
                int greenDiff = green1 - green2;


                if (!(redDiff == 0 && blueDiff == 0 && greenDiff == 0)) {
                    if (redDiff > TOLERANCE || blueDiff > TOLERANCE || greenDiff > TOLERANCE) {
                        numOfDifferentPixels++;
                    } else {
                        backgroundStatus = BitmapMatchStatus.Tolerable;
                    }
                }
            }
        }

        double percentage_difference = ((double) numOfDifferentPixels / (double) totalPixels) * 100.0;
        if (percentage_difference >= 1) {
            backgroundStatus = BitmapMatchStatus.Intolerable;
        } else {
            if (backgroundStatus != BitmapMatchStatus.Tolerable) {
                backgroundStatus = BitmapMatchStatus.Plain;
            }
        }

        return backgroundStatus;
    }

    public static Bitmap createFourImageCollageFromImage(Bitmap bitmap, int width, int height, int margin, int borderPadding) {
        Bitmap result = Bitmap.createBitmap(width + (borderPadding * 2) + margin, height + (borderPadding * 2) + margin, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();

        Bitmap imgBitmap = ImageHelper.resizeImage(bitmap, width / 2, height / 2);

        canvas.drawBitmap(imgBitmap, borderPadding, borderPadding, paint);
        canvas.drawBitmap(imgBitmap, borderPadding + imgBitmap.getWidth() + margin, borderPadding, paint);
        canvas.drawBitmap(imgBitmap, borderPadding, borderPadding + imgBitmap.getHeight() + margin, paint);
        canvas.drawBitmap(imgBitmap, borderPadding + imgBitmap.getWidth() + margin, borderPadding + imgBitmap.getHeight() + margin, paint);

        return result;
    }

    public static Bitmap resizeImageInRatio(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null)
            return bitmap;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        return bitmap;

//		int newHeight = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()));
//		Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, newHeight, true);
    }

    public enum BitmapMatchStatus {
        Intolerable,
        Tolerable,
        Plain
    }
}
