package com.alyndroid.syarty.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alyndroid.syarty.R;
import com.alyndroid.syarty.data.local.SharedPreferenceHelper;
import com.alyndroid.syarty.pojo.CoreUserData;
import com.alyndroid.syarty.util.constants.ConstantValues;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CommonUtils {
    private static final String DATE_TAG = "Date_Dialog";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    private CommonUtils() {
    }

    public static ProgressDialog showLoadingDialog(Context context, String title, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;

    }

    public static String getAccountAuthorization(Context context) {
        if (SharedPreferenceHelper.getInstance(context).getAccountAuthorization() != null) {
            return ConstantValues.AUTHORIZATION_KEY + SharedPreferenceHelper.getInstance(context).getAccountAuthorization();
        }

        return null;
    }

    public static void showGPSDisabledAlertToUser(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        (dialog, id) -> {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(callGPSSettingIntent);
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                (dialog, id) -> dialog.cancel());
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static void showTimePicker(TimePickerDialog.OnTimeSetListener listener, Activity activity) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                listener,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.dismissOnPause(true);
        tpd.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        tpd.show(activity.getFragmentManager(), DATE_TAG);
    }

    public static void showDatePicker(DatePickerDialog.OnDateSetListener listener, Activity activity, boolean showYearFirst) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                listener,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        Calendar maxCal = Calendar.getInstance();
        maxCal.set(Calendar.YEAR, maxCal.get(Calendar.YEAR) - 21);
        dpd.setMaxDate(maxCal);
        dpd.dismissOnPause(true);
        if (showYearFirst)
            dpd.showYearPickerFirst(true);
        dpd.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        dpd.show(activity.getFragmentManager(), DATE_TAG);
    }


    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    public static CoreUserData formatLoginResponseToCoreUserData() {
        return null;
    }

    public static void showSlowInternetConnectionToast(Activity activity) {
        showToast(activity, activity.getResources().getString(R.string.check_internet_connection));
    }

    public static void showToast(final Activity activity, final String msg) {
        if (null == activity) {
            return;
        }

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (info != null) {
                switch (info.getType()) {
                    case ConnectivityManager.TYPE_WIFI:
                    case ConnectivityManager.TYPE_MOBILE:
                        return true;

                    default:
//                        showSlowInternetConnectionToast(activity);
                        return false;
                }
            } else {
//                showSlowInternetConnectionToast(activity);
            }
        }
        return false;
    }

    public static boolean isGPSAvailable(Context activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isValidRegex(String input, String regex) {
        return input.matches(regex);
    }

    public static String formatMoney(String money) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return String.valueOf(decimalFormat.format(Double.parseDouble(money)));
    }

    public static String formatMoney(String source, String money) {
        return formatStrings(source, formatMoney(money));
    }

    public static String formatStrings(String source, String value) {
        return String.format(source, value);
    }

    public static String getDate(String sourceDateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(sourceDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Locale locale = new Locale("ar");

        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd", locale);
        return targetFormat.format(sourceDate);
    }

    public static String getTime(String sourceDateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date sourceDate = null;
        try {
            sourceDate = dateFormat.parse(sourceDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Locale locale = new Locale("ar");

        SimpleDateFormat targetFormat = new SimpleDateFormat("hh:mm a", locale);
        return targetFormat.format(sourceDate);
    }

    public static int convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static File scaleDown(File file, float maxImageSize,
                                 boolean filter, Bitmap bitmap) {

        if (null == bitmap) {
            return null;
        }

        float ratio = Math.min(
                (float) maxImageSize / bitmap.getWidth(),
                (float) maxImageSize / bitmap.getHeight());
        int width = Math.round((float) ratio * bitmap.getWidth());
        int height = Math.round((float) ratio * bitmap.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width,
                height, filter);

        try {
            FileOutputStream fOut = new FileOutputStream(file);
            newBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

            fOut.flush();
            fOut.close();

            return file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String dateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());

        return df.format(date);
    }

    public static File takePicture(Activity activity, int REQUEST_IMAGE) {
        // Use a folder to store all results
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        10);
                return null;
            }
        }
        File folder = new File(Environment.getExternalStorageDirectory() + "/OpenALPR/");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Generate the path for the next photo
        String name = CommonUtils.dateToString(new Date(), "yyyy-MM-dd-hh-mm-ss");
        File capturedImageFile = new File(folder, name + ".jpg");
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(capturedImageFile));
        activity.startActivityForResult(intent, REQUEST_IMAGE);
        return  capturedImageFile;
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    public static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String addTimeToCurrent(int timeToAdd){
        String date2;
        try{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        String currentDateAndTime = sdf.format(new Date());
        Date date = sdf.parse(currentDateAndTime);
        Calendar calendar = Calendar.getInstance(Locale.US);
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, timeToAdd);
        date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(calendar.getTime());
        } catch (Exception e){
            date2 = "error";
        }
        return date2;
    }
}
