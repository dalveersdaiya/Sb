package in.ajm.sb.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.LruCache;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class LayoutToImageConverter {
    View view;
    Context context;
    Bitmap bMap;
    RecyclerView recyclerView;
    boolean isForRecyclerView = false;
    OnLayoutCaptured onLayoutCaptured;

    public LayoutToImageConverter(Context context, View view, boolean isForRecyclerView) {
        this.context = context;
        this.view = view;
        this.isForRecyclerView = isForRecyclerView;
    }

    public LayoutToImageConverter(Context context, RecyclerView recyclerView, boolean isForRecyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.isForRecyclerView = isForRecyclerView;
    }

    public Bitmap getImageFromLayout() {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        bMap = Bitmap.createBitmap(view.getDrawingCache());

        return bMap;
    }

    public Uri getImageUri(Context context, Bitmap bitmap, String titleToImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, titleToImage, null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        return bitmapUri;
    }

    public Bitmap getImageFromRecyclerView() {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        Bitmap bigBitmap = null;
        if (adapter != null) {
            int size = adapter.getItemCount();
            int height = 0;
            Paint paint = new Paint();
            int iHeight = 0;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            LruCache<String, Bitmap> bitmaCache = new LruCache<>(cacheSize);
            for (int i = 0; i < size; i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(recyclerView, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);
                holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                holder.itemView.setDrawingCacheEnabled(true);
                holder.itemView.buildDrawingCache();
                Bitmap drawingCache = holder.itemView.getDrawingCache();
                if (drawingCache != null) {

                    bitmaCache.put(String.valueOf(i), drawingCache);
                }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                height += holder.itemView.getMeasuredHeight();
            }

            bigBitmap = Bitmap.createBitmap(recyclerView.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
            Canvas bigCanvas = new Canvas(bigBitmap);
            bigCanvas.drawColor(Color.WHITE);

            for (int i = 0; i < size; i++) {
                Bitmap bitmap = bitmaCache.get(String.valueOf(i));
                bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                iHeight += bitmap.getHeight();
                bitmap.recycle();
            }
        }

        return bigBitmap;
    }

    public void shareLayoutImage(String message, String imageTitle) {
        Bitmap bitmap;
        if (isForRecyclerView) {
            bitmap = getImageFromRecyclerView();
        } else {
            bitmap = getImageFromLayout();
        }

        Uri getBitmapUri = getImageUri(context, bitmap, imageTitle);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        i.putExtra(Intent.EXTRA_STREAM, getBitmapUri);
        try {
            context.startActivity(Intent.createChooser(i, message));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onLayoutCaptured.screenshotCaptured();
                }
            },600);

        } catch (android.content.ActivityNotFoundException ex) {

            ex.printStackTrace();
        }
    }

    public void setOnLayoutCapturedListener(OnLayoutCaptured onLayoutCaptured) {
        this.onLayoutCaptured = onLayoutCaptured;
    }

    public interface OnLayoutCaptured {
        void screenshotCaptured();
    }

}
