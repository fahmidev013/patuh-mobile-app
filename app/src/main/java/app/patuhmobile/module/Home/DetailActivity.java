package app.patuhmobile.module.Home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import javax.inject.Inject;

import app.patuhmobile.R;
import app.patuhmobile.core.BaseActivity;
import at.grabner.circleprogress.CircleProgressView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements DetailView {

    @Inject
    DetailPresenter presenter;


    @BindView(R.id.circleView)
    CircleProgressView cpv;
    @BindView(R.id.background_progress)
    RelativeLayout bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initializeView(savedInstanceState, R.id.fragment_container);
        presenter.attachView(this);
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
}
