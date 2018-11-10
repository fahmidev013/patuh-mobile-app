package app.patuhmobile.module.Profile;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.greenrobot.eventbus.EventBus;
import org.w3c.dom.Text;

import javax.inject.Inject;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.core.BaseFragment;
import app.patuhmobile.datahelper.HelperBridge;
import app.patuhmobile.model.Point;
import app.patuhmobile.module.Home.HomeActivity;
import app.patuhmobile.module.Home.HomeFragmentPresenter;
import app.patuhmobile.module.Login.LoginActivity;
import app.patuhmobile.utils.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends BaseFragment implements ProfileView{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private App mApp;
    private AuthInfo session;

    @Inject
    ProfilePresenter presenter;

    private ProgressDialog dialog;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    private ImageView btnArrow, btnArrowkk, btnArrowtp, imgPP, btnArrowBahasa, btnBack;
    private TextView llText, nama, mail, hp, llTextkk, llTexttp, tvPoin;
    private LinearLayout llTextBahasa;
    private RelativeLayout btnSunting;
    private Button btnKeluar;


    int rotationAngle = 0;
    private AuthService authService;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mApp = App.get(getActivity());
        session = mApp.getAuthInfo();
        authService = new AuthService(mApp, session, null);
        btnArrow = (ImageView) getView().findViewById(R.id.btn_arrowsk);
        btnArrowtp = (ImageView) getView().findViewById(R.id.btn_arrowtp);
        btnBack = (ImageView) getView().findViewById(R.id.btn_back);
        imgPP = (ImageView) getView().findViewById(R.id.img_pp);
        btnArrowBahasa = (ImageView) getView().findViewById(R.id.btn_arrowbahasa);
        btnArrowkk = (ImageView) getView().findViewById(R.id.btn_arrowkk);
        llText = (TextView) getView().findViewById(R.id.layout_sk);
        nama = (TextView) getView().findViewById(R.id.tv_nama);
        mail = (TextView) getView().findViewById(R.id.tv_mail);
        hp = (TextView) getView().findViewById(R.id.tv_hp);
        tvPoin = (TextView) getView().findViewById(R.id.tvPoin);
        llTextkk = (TextView) getView().findViewById(R.id.llkk);
        llTexttp = (TextView) getView().findViewById(R.id.lltp);
        llTextBahasa = (LinearLayout) getView().findViewById(R.id.layout_bahasa);
        btnSunting = (RelativeLayout) getView().findViewById(R.id.ll_btn_sunting);
        btnKeluar = (Button) getView().findViewById(R.id.btnKeluar);
        btnKeluar.setOnClickListener(view1 -> mListener.keluar());
        btnArrowBahasa.setOnClickListener(view1 -> toogleArrow(view1, llTextBahasa));
        btnArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogleArrow(view, llText);
            }
        });
        btnArrowkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogleArrow(view, llTextkk);
            }
        });
        btnArrowtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toogleArrow(view, llTexttp);
            }
        });

        btnBack.setOnClickListener(view1 -> mListener.goHome());

        btnSunting.setOnClickListener(view1 -> mListener.daftar());

        if (session.getUserId() != null){
            dialog.setMessage("Menampilkan data, mohon menunggu..");
            dialog.setCancelable(false);
            dialog.show();
            if (NetworkUtils.isOnline(mApp.getBaseContext())){
                Glide.with(this)
                        .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + session.getUserId())
                        .animate(R.anim.abc_fade_in)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imgPP);
                nama.setText(session.getUsername());
                mail.setText(session.getMail());
                hp.setText(session.getHp());
                //tvPoin.setText(String.valueOf(session.getPointReward()));
                if (dialog.isShowing()) dialog.dismiss();
            }

        }


        if (NetworkUtils.isOnline(mApp.getBaseContext())) {
            authService.getPoints(session.getUserId(), new AuthService.PointCallback() {
                @Override
                public void onSuccess(Point point) {
                    tvPoin.setText(String.valueOf(point.getPoint()));
                }

                @Override
                public void onFail(Throwable e) {

                }
            });
        }


        /*if (session.getPicprofile() == null){
            Glide.with(this)
                    .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + session.getUserId())
                    .animate(R.anim.abc_fade_in)
                    .centerCrop()
                    .into(imgPP);
            nama.setText(session.getUsername());
            mail.setText(session.getMail());
            hp.setText(session.getHp());

        } else {
            Glide.with(this)
                    .load(session.getPicprofile())
                    .animate(R.anim.abc_fade_in)
                    .centerCrop()
                    .into(imgPP);
            nama.setText(session.getUsername());
            mail.setText(session.getMail());
            hp.setText(session.getHp());
        }*/



    }



    private boolean isCLicked = false;
    private void toogleArrow(View imageView, View layout){
        isCLicked = (isCLicked) ? false : true;
        rotationAngle = rotationAngle == 0 ? 90 : 0;  //toggle
        imageView.animate().rotation(rotationAngle).setDuration(500).start();
        layout.setVisibility(isCLicked ? View.VISIBLE : View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onProfileFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showToast(String mesg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void changeActivity(Class activity) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onProfileFragmentInteraction(Uri uri);
        void keluar();
        void daftar();
        void goHome();
    }
}
