package app.patuhmobile.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.R;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.di.ActivityComponent;
import app.patuhmobile.di.ActivityModule;
import app.patuhmobile.di.DaggerActivityComponent;
import app.patuhmobile.module.Home.HomeActivity;
import app.patuhmobile.module.Home.HomeFragment;
import app.patuhmobile.module.Profile.ProfileFragment;
import at.grabner.circleprogress.CircleProgressView;


/**
 * Created by Reynaldi on 12/1/2016.
 */

public abstract class BaseActivity extends AppCompatActivity  {


    private ActivityComponent component;

    public ActivityComponent component() {
        if (this.component == null) {
            this.component = DaggerActivityComponent.builder()
                    .appComponent(getApp().component())
                    .activityModule(new ActivityModule(this))
                    .build();
        }
        return this.component;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    protected App getApp() {
        return (App) getApplication();
    }


    protected void initializeView(Bundle savedInstanceState, int view) {
        if (findViewById(view) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }


            if(HelperBridge.showProfilefromHome){
                loadFragment(new ProfileFragment(), view, AppConfig.FRAG_NAV_SPG_PROFILE);
            } else {
                loadFragment(new HomeFragment(), view, AppConfig.FRAG_NAV_BTM_HOME);
            }

            /*HomeFragment homeFragment = HomeFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, homeFragment);
            transaction.commit();*/
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager() != null){
            FragmentManager fm = getSupportFragmentManager();
            int count = fm.getBackStackEntryCount();
            if (count == 1) {
                this.finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    protected void cpvStart(CircleProgressView cpv, RelativeLayout bp){
        //bp.setBackgroundColor(getResources().getColor(R.color.dark_transparent));
        bp.setVisibility(View.VISIBLE);
    }

    protected void cpvStop(CircleProgressView cpv, RelativeLayout bp){
        bp.setVisibility(View.GONE);

    }


    protected void showDialog(Activity activity, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    protected void showMessage(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }


    protected void loadFragment(Fragment fragment, int container, String fragTag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
        transaction.replace(container, fragment);
        transaction.addToBackStack(fragTag);
        transaction.commit();
    }

    public String getActiveFragment() {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                return "Nol";
            }
            String tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            return tag;

    }




}
