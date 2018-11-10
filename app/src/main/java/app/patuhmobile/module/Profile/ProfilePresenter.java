package app.patuhmobile.module.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.inject.Inject;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.auth.events.AuthenticationErrorEvent;
import app.patuhmobile.core.Presenter;
import app.patuhmobile.module.Home.HomeActivity;
import app.patuhmobile.module.Home.HomeView;
import app.patuhmobile.utils.NetworkUtils;

/**
 * Created by Fahmi Hakim on 24/05/2018.
 * for SERA
 */

public class ProfilePresenter implements Presenter<ProfileView> {

    private EventBus eventBus;
    private ProfileView view;
    private AuthService authService;
    private App app;
    private AuthInfo session;

    @Inject()
    public ProfilePresenter(EventBus eventBus, AuthService authService, App application, AuthInfo authInfo) {
        this.eventBus = eventBus;
        app = application;
        this.authService = authService;
        session = authInfo;
    }

    @Override
    public void attachView(ProfileView view) {

        this.view = view;
        eventBus.register(this);
    }

    public void postLogin(String username, String password) {
        view.showProgress();
        view.showToast("Proses...Mohon tunggu sebentar...");
        if (NetworkUtils.isOnline(app.getBaseContext())){
            authService.login(username, password, new AuthService.LoginCallback() {
                @Override
                public void onSuccess() {
                    view.hideProgress();
                    saveImage(session.getUserId());
                }

                @Override
                public void onFail(Throwable e) {
                    view.hideProgress();
                    view.showToast("Gagal, mohon cek username password anda");
                }
            });

        } else {
            view.hideProgress();
            view.showToast("Tidak ada Internet");
        }

    }


    public void saveImage(String userId) {
        view.showProgress();
        view.showToast("Proses...Save Image Profile...");
        if (NetworkUtils.isOnline(app.getBaseContext())){
            Glide.with(app.getBaseContext())
                    .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + userId)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(new SimpleTarget<Bitmap>(100,100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
                            saveImage(resource);
                            session.setPicprofile(saveImage(resource));
                            view.showToast("Login Sukses");
                            view.changeActivity(HomeActivity.class);
                        }
                    });

        } else {
            view.hideProgress();
            view.showToast("Tidak ada Internet");
        }

    }



    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = session.getUserId() + String.valueOf(new Random().nextInt(100)) + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/PATUH");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Add the image to the system gallery
            galleryAddPic(savedImagePath);

        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        //sendBroadcast(mediaScanIntent);
    }


    @Override
    public void detachView() {

        this.view = null;
    }

    @Subscribe
    public void onEvent(AuthenticationErrorEvent event){
    }
}
