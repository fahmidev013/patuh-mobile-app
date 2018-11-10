package app.patuhmobile.module.Login;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import app.patuhmobile.R;
import app.patuhmobile.core.BaseActivity;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.module.DaftarActivity;
import app.patuhmobile.module.LupaSandiActivity;
import app.patuhmobile.utils.PermissionUtil;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements LoginView,
        ActivityCompat.OnRequestPermissionsResultCallback,
        PermissionUtil.PermissionResultCallback {


    @Inject LoginPresenter presenter;

    /*@BindView(R.id.circleView)
    CircleProgressView cpv;
    @BindView(R.id.background_progress)
    RelativeLayout bp;*/
    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.edt_username)
    EditText etUsername;

    @BindView(R.id.edt_password)
    EditText etPassword;


    @BindView(R.id.tvBantuan)
    TextView btnBantuan;

    @BindView(R.id.tvDaftra)
    TextView btnDaftar;


    ArrayList<String> permissions=new ArrayList<>();

    PermissionUtil permissionUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.layout_pro_masuk);
        ButterKnife.bind(this);
        setPermissions();
        initializeView(savedInstanceState, R.id.fragment_container);
        presenter.attachView(this);
        setActionListener();
        btnBantuan.setOnClickListener(view -> startActivity(new Intent(this, LupaSandiActivity.class)));
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, DaftarActivity.class));
                HelperBridge.isClickedDaftarFromProfile = false;
                HelperBridge.isClickedforUpdate = false;
            }
        });
    }

    private void setPermissions() {
        permissionUtils=new PermissionUtil(this);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void setActionListener() {
        btnLogin.setOnClickListener(view -> {
            permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);
        });
        //btnLogin.setOnClickListener(view -> {startActivity(new Intent(this, HomeActivity.class));});
    }

    @Override
    public void showToast(String mesg) {
        showMessage(this, mesg);
    }

    @Override
    public void showProgress() {
        //cpvStart(cpv, bp);
    }

    @Override
    public void hideProgress() {
        //cpvStop(cpv, bp);
    }

    @Override
    public void changeActivity(Class activity) {
        startActivity(new Intent(this, activity));
        HelperBridge.isFirstTimeDialog = true;
        HelperBridge.showProfilefromHome = false;
        finish();
    }



    @Override
    public void PermissionGranted(int request_code) {
        Log.i("PERMISSION","GRANTED");
        //startActivity(new Intent(this, HomeActivity.class));
        presenter.postLogin(etUsername.getText().toString(), etPassword.getText().toString());
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {
        Log.i("PERMISSION PARTIALLY","GRANTED");
    }

    @Override
    public void PermissionDenied(int request_code) {
        Log.i("PERMISSION","DENIED");
    }

    @Override
    public void NeverAskAgain(int request_code) {
        Log.i("PERMISSION","NEVER ASK AGAIN");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
