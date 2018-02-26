
## PermissionUtil

Its an easy utility to handle all permission conditions . 
1. if permission graded. 
2. permission denied . 
3. if user check "dont show again ". 
4. if permission already graded before. 
5. if an unexpected error happen .


## How to integrate into your app?

Step 1. :

```java
public class MainActivity extends AppCompatActivity {

    PermissionHelper permissionHelper;

```
Step 2. 
```java
permissionHelper=new PermissionHelper(this);
        permissionHelper.checkPermission( Constants.READ_EXTERNAL_STORAGE, new PermissionHelper.PermissionAskListener() {
            @Override
            public void onDenied() {
            }

            @Override
            public void onGranted() {
            }

            @Override
            public void AllReadyGranted() {
            }
        });
```

Step 3. 
```java
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
```
