package com.spartacus.PermissionUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.Random;

public class PermissionUtil {

    public static OnResuleListener onResuleListener;
    private static int permission_requestCode;
    private static int openSetting_requestCode;
    private static Context mcontext;
    private static String permissionname;
    private static String usermessage;

    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static void checkPermission(Context context, String permission, PermissionAskListener listener) {
        checkPermission(context,permission,listener,getApplicationName(context)+" "+"Need to needs your permission");
    }

    public static void checkPermission(final Context context, String permission, PermissionAskListener listener, String message ) {

        PermissionUtil.usermessage =message;
        PermissionUtil.permissionname = permission;
        mcontext = context;
        onResuleListener = listener;
        permission_requestCode = RandomInt();
        openSetting_requestCode = RandomInt();
        while (permission_requestCode==openSetting_requestCode)
        {
            openSetting_requestCode=RandomInt();
        }


        if (shouldAskPermission(context, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, permission)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Permission");
                builder.setMessage(message);
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions((Activity) mcontext,
                                new String[]{permissionname},
                                permission_requestCode);                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (SharedPreferences.isFirstTimeAskingPermission(context,permission,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        boolean sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                        intent.setData(uri);
                        ((Activity)context).startActivityForResult(intent,openSetting_requestCode);
                        Toast.makeText(((Activity)context).getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permission_requestCode);
            }

            SharedPreferences.firstTimeAskingPermission(context,permission,true);

        } else {
            listener.AllReadyGranted();
        }
    }

    abstract static class PermissionAskListener implements OnResuleListener {

        @Override
        public void onRequestPermissionsResult(int requestCode, final String[] permissions, final int[] grantResults) {
            if (requestCode == permission_requestCode) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //The External Storage Write Permission is granted to you... Continue your left job...
                    onGranted(permissions,grantResults);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)mcontext, permissionname)) {
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder((Activity)mcontext);
                        builder.setTitle("Need Permission");
                        builder.setMessage(usermessage);
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();


                                ActivityCompat.requestPermissions((Activity)mcontext, new String[]{permissionname}, permission_requestCode);


                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                onDenied(permissions,grantResults);
                            }
                        });
                        builder.show();
                    } else {
                        onDenied(permissions,grantResults);
                        Toast.makeText(((Activity)mcontext).getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                    }
                }
            }



       /*     if (PermissionUtil.permission_requestCode != requestCode) return;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != 0) {
                    onDenied(permissions[i], requestCode);
                    return;
                }

            }
            onGranted(permissions, requestCode);*/
        }

        void onDenied(String[] permission, int[] grantResults) {


        }

        void onGranted(String[] permissions, int[] grantResults) {

        }

        void AllReadyGranted() {

        }
    }

    interface OnResuleListener {
        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
      //   void onActivityResult(int requestCode, int resultCode, Intent data);
      //   void onResume();
    }

    private static int RandomInt() {
        int min = 0;
        int max = 500;

        Random r = new Random();
        return r.nextInt(max - min + 1) + min;

    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int stringId = applicationInfo.labelRes;
        return stringId == 0 ? applicationInfo.nonLocalizedLabel.toString() : context.getString(stringId);
    }

    class Constants {
        public final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
        public final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        public final String CAMERA = Manifest.permission.CAMERA;
        public final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
        public final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
        public final String ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
        public final String BODY_SENSORS = Manifest.permission.BODY_SENSORS;
        public final String CALL_PHONE = Manifest.permission.CALL_PHONE;
        public final String GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
        public final String PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
        public final String READ_CALENDAR = Manifest.permission.READ_CALENDAR;
        public final String READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
        public final String READ_CONTACTS = Manifest.permission.READ_CONTACTS;
        public final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
        public final String READ_SMS = Manifest.permission.READ_SMS;
        public final String RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
        public final String RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
        public final String RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
        public final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
        public final String SEND_SMS = Manifest.permission.SEND_SMS;
        public final String USE_SIP = Manifest.permission.USE_SIP;
        public final String WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
        public final String WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
        public final String WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    }
}


class SharedPreferences {
    private static final String PREFERENCE = "PermissionUtil";

    //shared prefrnce control
    public static boolean isFirstTimeAskingPermission(Context context, String prefKey, boolean defaultValue) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).getBoolean(prefKey, defaultValue);
    }

    public static boolean firstTimeAskingPermission(Context context, String prefKey, boolean Value) {
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE).edit().putBoolean(prefKey, Value).commit();
    }

}

