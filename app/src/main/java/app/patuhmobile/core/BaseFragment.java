package app.patuhmobile.core;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import app.patuhmobile.App;
import app.patuhmobile.R;
import app.patuhmobile.di.ActivityComponent;
import app.patuhmobile.module.Home.HomeActivity;
import app.patuhmobile.module.Home.Panutan.PanutanFragment;
import app.patuhmobile.module.Home.events.CallParentActivity;

public abstract class BaseFragment extends Fragment {


    public ActivityComponent component() {
        return ((BaseActivity)getActivity()).component();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_quote:
                HomeActivity.openNav((HomeActivity) getActivity());
                //Toast.makeText(getActivity(), "ADD!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    protected App getApp() {
        return (App) getActivity().getApplication();
    }

    protected Context getAppContext() {
        return getActivity().getApplicationContext();
    }



}
