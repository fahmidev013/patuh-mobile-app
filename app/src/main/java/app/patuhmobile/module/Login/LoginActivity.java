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


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

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

    @BindView(R.id.btn_fb)
    Button btnSosmed;

    @BindView(R.id.sign_in_button)
    SignInButton btnLoginGoogle;

    @BindView(R.id.tvBantuan)
    TextView btnBantuan;

    @BindView(R.id.tvDaftra)
    TextView btnDaftar;


    ArrayList<String> permissions=new ArrayList<>();

    PermissionUtil permissionUtils;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 99;


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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnLoginGoogle.setSize(SignInButton.SIZE_STANDARD);
        btnLoginGoogle.setOnClickListener(view -> {});
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            showMessage(this, account.getDisplayName());
            Log.e("GOOGLE", account.getDisplayName());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE", "signInResult:failed code=" + e.getStatusCode());
        }
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
