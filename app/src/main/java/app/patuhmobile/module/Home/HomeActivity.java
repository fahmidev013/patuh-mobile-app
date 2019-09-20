package app.patuhmobile.module.Home;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import javax.inject.Inject;

import app.patuhmobile.AppConfig;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.events.EventMessage;
import app.patuhmobile.core.BaseActivity;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.module.DaftarActivity;
import app.patuhmobile.module.Home.Cerita.CeritaFragment;
import app.patuhmobile.module.Home.Cerita.DetailCeritaFragment;
import app.patuhmobile.module.Home.Panutan.DetailPanutanFragment;
import app.patuhmobile.module.Home.Panutan.PanutanFragment;
import app.patuhmobile.module.Home.events.CallParentActivity;
import app.patuhmobile.module.KuponSaya.KuponSayaFragment;
import app.patuhmobile.module.Login.LoginActivity;
import app.patuhmobile.module.Notif.NotifFragment;
import app.patuhmobile.module.Profile.ProfileFragment;
import app.patuhmobile.module.TukarPoin.ChildTukarFragment;
import app.patuhmobile.module.TukarPoin.TukarPoinFragment;
import app.patuhmobile.module.Ujian.UjianFragment;
import app.patuhmobile.module.Upload.UploadFragment;
import app.patuhmobile.utils.DialogUtils;
import app.patuhmobile.utils.ImagePicker;
import app.patuhmobile.utils.PermissionUtil;
import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements HomeView,
        PanutanFragment.OnFragmentInteractionListener,
        HomeFragment.OnFragmentInteractionListener,
        TukarPoinFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        CeritaFragment.OnFragmentInteractionListener,
        //NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener,
        DetailPanutanFragment.OnFragmentInteractionListener,
        UjianFragment.OnFragmentInteractionListener,
        NotifFragment.OnFragmentInteractionListener,
        UploadFragment.OnFragmentInteractionListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        DetailCeritaFragment.OnFragmentInteractionListener,
        KuponSayaFragment.OnFragmentInteractionListener,
        PermissionUtil.PermissionResultCallback,
        ChildTukarFragment.OnFragmentInteractionListener {


    @Inject HomePresenter presenter;


    @BindView(R.id.circleView)
    CircleProgressView cpv;
    @BindView(R.id.background_progress)
    RelativeLayout bp;

    @BindView(R.id.navleft)
    BottomNavigationView btnNavigationLayoutLeft;

    @BindView(R.id.navright)
    BottomNavigationView btnNavigationLayoutRight;

    @BindView(R.id.btn_nav_center)
    ImageView btnAdd;

    @BindView(R.id.drawer_layout)
    DrawerLayout vDrawerLayout;
    /*@BindView(R.id.nav_view)
    NavigationView vNavigationView;*/

    ActionBar toolbar;


    @Inject
    AuthInfo session;


    EventBus eventBus = EventBus.getDefault();

    PermissionUtil permissionUtils;
    ArrayList<String> permissions=new ArrayList<>();

    public static boolean isAppRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.activity_home);
        eventBus.register(this);
        ButterKnife.bind(this);
        initializeView(savedInstanceState, R.id.fragment_container);
        //toolbar = getSupportActionBar();
        presenter.attachView(this);
        setActionListener();
        if (HelperBridge.isFirstTimeDialog) {
            DialogUtils.showDialog(this, "Selamat Datang \n" + session.getUsername(), R.layout.dialog_home, R.id.txt_title, R.id.img_pp, session.getPicprofile(), R.id.btn_confirm);
            HelperBridge.isFirstTimeDialog = false;
        }
        setPermissions();
        listenNotif();
    }

    private void listenNotif() {



        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "1";
        String channel2 = "2";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Channel 1",NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("This is BNT");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(channel2,
                    "Channel 2",NotificationManager.IMPORTANCE_MIN);

            notificationChannel.setDescription("This is bTV");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(notificationChannel2);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void setPermissions() {
        permissionUtils=new PermissionUtil(this);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.CAMERA);
    }

    private void setActionListener() {

        btnNavigationLayoutRight.setOnNavigationItemSelectedListener(this);
        btnNavigationLayoutLeft.setOnNavigationItemSelectedListener(this);
        disableShiftMode(btnNavigationLayoutLeft);
        disableShiftMode(btnNavigationLayoutRight);
      //  vNavigationView.setNavigationItemSelectedListener(this);
        btnAdd.setOnClickListener(view -> {
            loadFragment(new UploadFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_SPG_PROFILE);
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager() != null){
//            toolbar.setTitle(getActiveFragment());
        } else {
  //          toolbar.setTitle("PATUH");
        }
    }

    @Override
    protected void onDestroy() {
        eventBus.unregister(this);
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        cpvStart(cpv, bp);
    }

    @Override
    public void hideProgress() {
        cpvStop(cpv, bp);
    }


    @Override
    public void onPanutanFragmentInteraction() {
        //vDrawerLayout.openDrawer(vNavigationView);
    }

    @Override
    public void onPanutanFragmentClickContent(Artikel artikel) {
        //toolbar.setTitle(AppConfig.FRAG_NAV_DETAIL_CONTENT);
        loadFragment(DetailPanutanFragment.newInstance(artikel), R.id.fragment_container, AppConfig.FRAG_NAV_DETAIL_CONTENT);
    }

    @Override
    public void onHomeFragmentInteraction() {
        loadFragment(new ProfileFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_SPG_PROFILE);
    }

    @Override
    public void onProfileFragmentInteraction(Uri uri) {

    }

    @Override
    public void keluar() {
        session.resetAndRemoveFromCache();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void daftar() {
        HelperBridge.isClickedDaftarFromProfile = true;
        HelperBridge.isClickedforUpdate = true;
        startActivity(new Intent(this, DaftarActivity.class));
        finish();
    }

    @Override
    public void goHome() {
        this.loadFragment(new HomeFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_BTM_HOME);
    }

    @Override
    public void onTukarpoinFragmentInteraction() {
        loadFragment(KuponSayaFragment.newInstance(null, null), R.id.fragment_container, AppConfig.FRAG_NAV_BTM_KUPON_SAYA);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.navigation_home:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_BTM_HOME);
                    loadFragment(new HomeFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_BTM_HOME);
                    return true;
                case R.id.navigation_ujian:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_BTM_UJIAN);
                    loadFragment(new UjianFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_BTM_UJIAN);
                    return true;
                case R.id.navigation_kupon:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_BTM_KUPON);
                    loadFragment(new TukarPoinFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_BTM_KUPON);
                    return true;
                case R.id.navigation_notif:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_BTM_NOTIF);
                    loadFragment(new NotifFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_BTM_NOTIF);
                    return true;
                case R.id.nav_camera:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_SPG_PROFILE);
                    loadFragment(new PanutanFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_SPG_PROFILE);
                    item.setChecked(true);
                    // close drawer when item is tapped
                    vDrawerLayout.closeDrawers();
                    return true;
                case R.id.nav_gallery:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_SPG_SETTING);
                    loadFragment(new HomeFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_SPG_SETTING);
                    item.setChecked(true);
                    // close drawer when item is tapped
                    vDrawerLayout.closeDrawers();
                    return true;
                case R.id.nav_manage:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_SPG_PROFILE);
                    loadFragment(new PanutanFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_SPG_PROFILE);
                    item.setChecked(true);
                    // close drawer when item is tapped
                    vDrawerLayout.closeDrawers();
                    return true;
                case R.id.nav_slideshow:
                    //toolbar.setTitle(AppConfig.FRAG_NAV_SPG_SETTING);
                    loadFragment(new HomeFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_SPG_SETTING);
                    item.setChecked(true);
                    // close drawer when item is tapped
                    vDrawerLayout.closeDrawers();
                    return true;
            }
            return false;
    }


    public static void openNav(HomeActivity activity){
        //activity.vDrawerLayout.openDrawer(activity.vNavigationView);
        activity.getSupportActionBar().setTitle(AppConfig.FRAG_NAV_SPG_PROFILE);
        activity.loadFragment(new ProfileFragment(), R.id.fragment_container, AppConfig.FRAG_NAV_SPG_PROFILE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCeritaFragmentClickContent(Artikel artikel) {
        loadFragment(DetailCeritaFragment.newInstance(artikel), R.id.fragment_container, AppConfig.FRAG_NAV_DETAIL_CONTENT);
    }


    @Override
    public void PermissionGranted(int request_code) {
        if (HelperBridge.userChoosenTask.equalsIgnoreCase("Take Photo")){
            cameraIntent();
        } else {
            galleryIntent();
        }

    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            eventBus.post(new SuccessEvent(requestCode, resultCode, data));
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public final int REQUEST_CAMERA = 100;
    public final int SELECT_FILE = 101;

    private void cameraIntent()
    {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, REQUEST_CAMERA);
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }

    private void galleryIntent()
    {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, SELECT_FILE);
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);*/
    }

    @Override
    public void PartialPermissionGranted(int request_code, ArrayList<String> granted_permissions) {

        if (HelperBridge.userChoosenTask.equals("Pilih dari Kamera")){
            cameraIntent();
        } else {
            galleryIntent();
        }
    }

    @Override
    public void PermissionDenied(int request_code) {

    }

    @Override
    public void NeverAskAgain(int request_code) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void checkPermission() {
        permissionUtils.check_permission(permissions,"Explain here why the app needs permissions",1);
    }



    @Subscribe
    public void onSuccesEvent(CallParentActivity event){
        /*if (event.getRequestCode() == SELECT_FILE) {
            //  onSelectFromGalleryResult(event.getMdata());
        } else if (event.getRequestCode() == REQUEST_CAMERA) {
            // onCaptureImageResult(event.getMdata());
        }*/
    }

    @Subscribe
    public void onSuccesCommentEvent(EventMessage event){
        /*if (event.getRequestCode() == SELECT_FILE) {
            //  onSelectFromGalleryResult(event.getMdata());
        } else if (event.getRequestCode() == REQUEST_CAMERA) {
            // onCaptureImageResult(event.getMdata());
        }*/
    }

    @Override
    public void onKuponSayaFragmentInteraction(Uri uri) {

    }

    @Override
    public void onChildTukarFragmentInteraction(Uri uri) {

    }
}
