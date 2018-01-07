package com.spartacus.PermissionUtil;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public String TAG= "ASDASDASD";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionUtil.checkPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionUtil.PermissionAskListener() {
            @Override
            void onDenied() {
                super.onDenied();
                Log.e("ASDASDASD","onDenied");

            }

            @Override
            void onGranted() {
                super.onGranted();
                Log.e("ASDASDASD","onGranted");

            }

            @Override
            void AllReadyGranted() {
                super.AllReadyGranted();
                Log.e("ASDASDASD","AllReadyGranted");

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onResuleListener.onRequestPermissionsResult(this, requestCode,permissions,grantResults);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        PermissionUtil.onResuleListener.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PermissionUtil.onResuleListener.onActivityResult(this,requestCode,resultCode,data);
    }
}
