package vn.hailt.hdwallpaper.ultis;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.PAINT_FLAGS;

public class PositionedCropTransformation extends BitmapTransformation {

    /**
     * Commonly needed values:
     * top-left:
     * 0, 0
     * top-right:
     * 0, 1
     * bottom-left:
     * 1, 0
     * bottom-right:
     * 1, 1
     * <p>
     * Use 0.5f for center
     */

    private float xPercentage;
    private float yPercentage;

    public PositionedCropTransformation(@FloatRange(from = 0.0, to = 1.0) float xPercentage, @FloatRange(from = 0.0, to = 1.0) float yPercentage) {
        super();
        this.xPercentage = xPercentage;
        this.yPercentage = yPercentage;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        final Bitmap toReuse = pool.get(outWidth, outHeight, toTransform.getConfig() != null
                ? toTransform.getConfig() : Bitmap.Config.ARGB_8888);
        Bitmap transformed = crop(toReuse, toTransform, outWidth, outHeight, xPercentage, yPercentage);
        if (toReuse != transformed) {
            toReuse.recycle();
        }
        return transformed;
    }

    private static Bitmap crop(Bitmap recycled, Bitmap toCrop, int width, int height, float xPercentage, float yPercentage) {
        if (toCrop == null) {
            return null;
        } else if (toCrop.getWidth() == width && toCrop.getHeight() == height) {
            return toCrop;
        }

        final float scale;
        float dx = 0, dy = 0;
        Matrix m = new Matrix();
        if (toCrop.getWidth() * height > width * toCrop.getHeight()) {
            scale = (float) height / (float) toCrop.getHeight();
            dx = (width - toCrop.getWidth() * scale);
            dx *= xPercentage;
        } else {
            scale = (float) width / (float) toCrop.getWidth();
            dy = (height - toCrop.getHeight() * scale);
            dy *= yPercentage;
        }

        m.setScale(scale, scale);
        m.postTranslate((int) (dx + 0.5f), (int) (dy + 0.5f));
        final Bitmap result;
        if (recycled != null) {
            result = recycled;
        } else {
            result = Bitmap.createBitmap(width, height, getSafeConfig(toCrop));
        }

        TransformationUtils.setAlpha(toCrop, result);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(PAINT_FLAGS);
        canvas.drawBitmap(toCrop, m, paint);
        return result;
    }

    private static Bitmap.Config getSafeConfig(Bitmap bitmap) {
        return bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}