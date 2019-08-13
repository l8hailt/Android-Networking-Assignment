package vn.hailt.hdwallpaper.asynctask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DownloadTask extends AsyncTask<Bitmap, Void, Void> {

    private OnDownloadListener listener;

    public DownloadTask(OnDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(Bitmap... bitmaps) {

        Bitmap bitmap = bitmaps[0];

        String timeStamp = new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(new Date());
        String fileName = "wpp_" + timeStamp + ".jpg";
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HDWallpaper";

        File folder = new File(dirPath);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        File file = new File(dirPath, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (listener != null)
            listener.onFinish();
    }

    public interface OnDownloadListener {
        void onFinish();
    }
}
