package app.patuhmobile.module;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.patuhmobile.App;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.module.Home.HomeActivity;
import app.patuhmobile.module.Login.LoginActivity;
import app.patuhmobile.utils.PermissionUtil;

public class LoginRegisterActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtil.PermissionResultCallback{


    private static final int PERMISSIONS = 98;
    private App mApp;
    private AuthInfo session;

    private boolean isPermissionGranted = false;
    private boolean isNeverAskedAgainClicked = false;

    PermissionUtil permissionUtils;
    ArrayList<String> permissions=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        Button btnMasuk = (Button) findViewById(R.id.btn_masuk);
        Button btnDaftar = (Button) findViewById(R.id.btn_daftar);
        permissionUtils=new PermissionUtil(this);
        setPermissions();
        mApp = App.get(this);
        if (isPermissionGranted){
            session = mApp.getAuthInfo();
            validateSession();
        }

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted){
                    startActivity(new Intent(LoginRegisterActivity.this, LoginActivity.class));
                    finish();
                } else if (isNeverAskedAgainClicked){
                    Toast.makeText(LoginRegisterActivity.this, "Mohon aktifkan permission untuk app ini di menu Setting", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginRegisterActivity.this , "APlikasi akan crash jika perijinan tidak disetujui semua", Toast.LENGTH_LONG).show();
                    setPermissions();
                }
            }
        });
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPermissionGranted){
                startActivity(new Intent(LoginRegisterActivity.this, DaftarActivity.class));
                HelperBridge.isClickedDaftarFromProfile = false;
                HelperBridge.isClickedforUpdate = false;
                finish();
            } else if (isNeverAskedAgainClicked){
                    Toast.makeText(LoginRegisterActivity.this, "Mohon aktifkan permission untuk app ini di menu Setting", Toast.LENGTH_LONG).show();
            } else {
                    Toast.makeText(LoginRegisterActivity.this , "APlikasi akan crash jika perijinan tidak disetujui semua", Toast.LENGTH_LONG).show();
                    setPermissions();
                }
            }
        });

    }


    private void setPermissions() {
        if (permissions.size() == 0){
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(Manifest.permission.INTERNET);
            permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
            permissions.add(Manifest.permission.CAMERA);
        }
        permissionUtils.check_permission(permissions, "Pengecekan Permission", PERMISSIONS);
    }


    @Override
    protected void onStart() {
        super.onStart();
        setPermissions();
    }

    private void validateSession() {
        if (session.getUsername() != null){
            startActivity(new Intent(LoginRegisterActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    @Override
    public void PermissionGranted(int request_code) {
        isPermissionGranted = true;
        Log.e("PERM","permision granted");
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.e("PERM","Partial granted");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.e("PERM","denied");
    }



    @Override
    public void NeverAskAgain(int request_code) {
        Log.e("PERM","never asked again");
        isNeverAskedAgainClicked = true;
    }
}
