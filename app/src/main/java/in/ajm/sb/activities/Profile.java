package in.ajm.sb.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import in.ajm.sb.R;
import in.ajm.sb.helper.GeneralHelper;
import in.ajm.sb.helper.PreferencesManager;
import in.ajm.sb_library.charts.FitChart;
import in.ajm.sb_library.charts.FitChartValue;

import static com.yalantis.ucrop.util.FileUtils.getDataColumn;

public class Profile extends BaseActivity {

    FitChart fitChartMonthly;
    FitChart fitChartYearly;
    FitChart fitChartOverAll;
    Collection<FitChartValue> values;
    Point point;

    boolean checkCamera = false;
    private static final int READ_GALLERY_PERMISSIONS_REQUEST = 1;
    private static final int READ_CAMERA_PERMISSIONS_REQUEST = 2;
    private File imageFilePath;
    private Uri imageFileUri;
    File imageFileCrop;
    String selectedImagePath = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForTheme(this);
        setContentView(R.layout.activity_profile);
        viewByIds();
        applyClickListeners();
        setupToolBar(getResources().getString(R.string.profile), true);
        setFitChartValues();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setChart(values, fitChartYearly);
                setChart(values, fitChartMonthly);
                setChart(values, fitChartOverAll);
            }
        }, 2000);

    }

    public void viewByIds() {
        fitChartMonthly = findViewById(R.id.fitChart_monthly);
        fitChartYearly = findViewById(R.id.fitChart_yearly);
        fitChartOverAll = findViewById(R.id.fitChart_over_all);
        point = GeneralHelper.getInstance(this).getScreenSize();
        setLayoutParamsForChart(fitChartMonthly);
        setLayoutParamsForChart(fitChartYearly);
        setLayoutParamsForChart(fitChartOverAll);

    }

    public void applyClickListeners() {

    }

    private void setChart(Collection<FitChartValue> values, FitChart fitChart) {
        fitChart.setMinValue(0f);
        fitChart.setMaxValue(232f);
        fitChart.setValues(values);
        fitChart.setFocusable(false);
    }

    private void setFitChartValues() {
        values = new ArrayList<>();
        values.add(new FitChartValue(78f, ContextCompat.getColor(this, R.color.md_blue_600)));
        values.add(new FitChartValue(93f, ContextCompat.getColor(this, R.color.red_600)));
        values.add(new FitChartValue(61f, ContextCompat.getColor(this, R.color.green_600)));
    }

    public void setLayoutParamsForChart(FitChart fitChart) {
        ViewGroup.LayoutParams layoutParams = fitChart.getLayoutParams();
        layoutParams.width = point.x / 3;
        layoutParams.height = point.x / 3;
        fitChart.setLayoutParams(layoutParams);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageFileCrop = new File(
                (Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        + "/." + APPNAME
                        + "/"
                        + System.currentTimeMillis() + ".jpeg"));
        if (requestCode == 10 && resultCode == RESULT_OK) {
            String pathName = "";
            Uri path = data.getData();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pathName = getPath(context, path);
            } else {
                pathName = getRealPathFromURI(context, path);
            }
            try {
            } catch (Exception e) {
                Toast.makeText(context, getResources().getString(R.string.unable_to_upload), Toast.LENGTH_LONG).show();
            }
            if (requestCode == 10) {
                if (pathName == null) {
                    Toast.makeText(context, getResources().getString(R.string.unable_to_upload), Toast.LENGTH_LONG).show();
                } else {
                    final Uri resultUri = Uri.parse(data.getData().toString());
                    pathName = getRealPathFromURI(this, Uri.parse(data.getData().toString()));
                }
            }
        } else if (requestCode == 15 && resultCode == RESULT_OK) {
            imageFilePath = new File(PreferencesManager.getPreferenceByKey(
                    context, "IMAGEWWC"));
            Uri fileUri = null;
            if (requestCode == 15) {
                String pathName = "";
                Uri path = Uri.fromFile(imageFilePath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath.getPath(),
                        options);
                try {
                    ExifInterface exif = new ExifInterface(imageFilePath.getPath());
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    Bitmap bmRotated = rotateBitmap(bitmap, orientation);
//				/* uri from bitmap */
                    Uri fileUris = getImageUri(context, bmRotated);
                    File finalFile = new File(getRealPathFromURI(this,fileUris));
                    fileUri=Uri.fromFile(finalFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            selectedImagePath = String.valueOf(resultUri);
            beginRealmTransaction();

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

    }

    public void imageOpen(final int REQUEST_CAMERA,
                          final int SELECT_FILE) {

        final CharSequence[] items = {getResources().getString(R.string.take_photo), getResources().getString(R.string.choose_from_gallery),
                getResources().getString(R.string.cancel)};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.take_photo))) {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        checkCamera = true;
                        getPermissionToReadUserCamera();
                    } else {
                        cameraSelectPhoto(REQUEST_CAMERA);
                    }


                } else if (items[item].equals(getResources().getString(R.string.choose_from_gallery))) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                        checkCamera = false;
                        getPermissionToReadUserGallery();
                    } else {
                        gallerySelectPhoto(SELECT_FILE);
                    }


                } else if (items[item].equals(getResources().getString(R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void gallerySelectPhoto(int SELECT_FILE) {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                SELECT_FILE);
    }

    private void cameraSelectPhoto(int REQUEST_CAMERA) {
        File imageFile = new File(
                (Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        + "/." + APPNAME
                        + "/"
                        + System.currentTimeMillis() + ".jpeg"));

//        PreferencesManager.setPreferenceByKey(context,
//                "IMAGEWWC", imageFile.getAbsolutePath());
        imageFilePath = imageFile;
        imageFileUri = Uri.fromFile(imageFile);
        Intent i = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT,
                imageFileUri);
        startActivityForResult(i, REQUEST_CAMERA);
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }






    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadUserCamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA)) {
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    READ_CAMERA_PERMISSIONS_REQUEST);
        } else {
            File imageFile1 = new File(
                    (Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            + "/." + APPNAME + ""));
            imageFile1.mkdirs();
//            imageOpen(15, 10);
            cameraSelectPhoto(15);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToReadUserGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_GALLERY_PERMISSIONS_REQUEST);
        } else {
            if (checkCamera) {
                cameraSelectPhoto(15);
            } else {
                File imageFile1 = new File(
                        (Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                                + "/." + APPNAME + ""));
                imageFile1.mkdirs();
                gallerySelectPhoto(10);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        File imageFile1 = new File(
                (Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                        + "/." + APPNAME + ""));
        imageFile1.mkdirs();

        if (requestCode == READ_CAMERA_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPermissionToReadUserGallery();
            } else {
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == READ_GALLERY_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!checkCamera)
                    gallerySelectPhoto(10);
                else
                    cameraSelectPhoto(15);
            } else {
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:

                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public void promptUserForImageSelection(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            imageOpen(15, 10);
        } else {
            File imageFile1 = new File(
                    (Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            + "/." + APPNAME + ""));
            imageFile1.mkdirs();
            imageOpen(15, 10);
        }
    }
}
