package com.spartacus.PermissionUtil;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.checkPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionUtil.PermissionAskListener() {
            @Override
            public void OnActivityResuleListener(int requestCode, String[] permissions, int[] grantResults) {
                super.OnActivityResuleListener(requestCode, permissions, grantResults);
                Log.e("HHHHHH","FFFFFFFFF");
            }

            @Override
            void onDenied(String permission, int requestCode) {
                super.onDenied(permission, requestCode);
                Log.e("onDenied","onDenied");

            }

            @Override
            void onGranted(String[] permissions, int requestCode) {
                super.onGranted(permissions, requestCode);
                Log.e("onGranted","onGranted");

            }

            @Override
            void AllReadyGranted() {
                super.AllReadyGranted();
                Log.e("AllReadyGranted","AllReadyGranted");

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onActivityResuleListener.OnActivityResuleListener(requestCode,permissions,grantResults);
    }
}
