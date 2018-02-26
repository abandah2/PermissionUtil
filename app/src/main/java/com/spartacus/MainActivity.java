package com.spartacus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.spartacus.PermissionUtil.Constants;
import com.spartacus.PermissionUtil.PermissionHelper;
import com.spartacus.PermissionUtil.R;

/**
 * Created by Abandah on 1/7/2018.
 *
 */
public class MainActivity extends AppCompatActivity {

    public String TAG= "ASDASDASD";
    PermissionHelper permissionHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionHelper=new PermissionHelper(this);
        permissionHelper.checkPermission( Constants.READ_EXTERNAL_STORAGE, new PermissionHelper.PermissionAskListener() {
            @Override
            public void onDenied() {
                Log.e("ASDASDASD","onDenied");

            }

            @Override
            public void onGranted() {
                Log.e("ASDASDASD","onGranted");

            }

            @Override
            public void AllReadyGranted() {
                Log.e("ASDASDASD","AllReadyGranted");

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            permissionHelper.onRequestPermissionsResult(this, requestCode,permissions,grantResults);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
            permissionHelper.onResume(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            permissionHelper.onActivityResult(this,requestCode,resultCode,data);
    }
}
