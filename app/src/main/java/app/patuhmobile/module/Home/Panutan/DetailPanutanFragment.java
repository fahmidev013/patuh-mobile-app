package app.patuhmobile.module.Home.Panutan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.auth.events.EventMessage;
import app.patuhmobile.core.BaseFragment;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.model.Komentar;
import app.patuhmobile.model.Like;
import app.patuhmobile.model.ResponeApiModel;
import app.patuhmobile.module.adapter.KomenAdapter;
import app.patuhmobile.utils.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailPanutanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailPanutanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailPanutanFragment extends BaseFragment implements DetailPanutanView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    /*private static final String title = "param1";
    private static final String noartikel = "param0";
    private static final String cat = "param2";
    private static final String loc = "param3";
    private static final String story = "param4";
    private static final String imgId = "param5";*/

    // TODO: Rename and change types of parameters
    private String no;
    private String judul;
    private String kategori;
    private String lokasi;
    private String konten;
    private String imageId;
    private View mView;

    AuthService authService;
    App mApp;


    private ProgressDialog dialog;
    private AuthInfo session;
    private ImageView btnExit, toogleLike;

    private ImageView picProfileHeader, picProfileComment, picContent, btnLapor, btnSosmed;
    private TextView tvJudul, tvLokasi, tvKonten, btnKomentar;

    private EditText komentar;

    private RecyclerView mRecycleView;

    private RecyclerView.LayoutManager mLayoutManager;
    KomenAdapter adapter;

    private Artikel mArtikel;

    private EventBus mBus;

    private ArrayList<Komentar> komentars = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public DetailPanutanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailPanutanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailPanutanFragment newInstance(Artikel artikel) {
        DetailPanutanFragment fragment = new DetailPanutanFragment();
        Bundle args = new Bundle();
        args.putSerializable("Artikel", artikel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBus = new EventBus();
        mBus.register(this);
        mApp = App.get(getActivity());
        session = mApp.getAuthInfo();
        authService = new AuthService(mApp, session, new EventBus());
        authService.getEventBus().register(this);
        if (getArguments() != null) {
            mArtikel = (Artikel) getArguments().getSerializable("Artikel");
            judul = mArtikel.getTitle();
            kategori = mArtikel.getCategory();
            lokasi = mArtikel.getGPSLocation();
            konten = mArtikel.getStory();
            imageId = mArtikel.getImageIds().get(0);
            no = String.valueOf(mArtikel.getId());
        }

        dialog = new ProgressDialog(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog.setMessage("Memuat data, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        mView = inflater.inflate(R.layout.fragment_detail_panutan, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        komentar = (EditText) mView.findViewById(R.id.komentar);
        mRecycleView = (RecyclerView) mView.findViewById(R.id.layoutview);
        btnLapor = (ImageView) mView.findViewById(R.id.btn_lapor);
        btnKomentar = (TextView) mView.findViewById(R.id.btn_kirim_komentar);
        tvJudul = (TextView) mView.findViewById(R.id.tv_judul);
        toogleLike = (ImageView) mView.findViewById(R.id.toogle_like);
        tvLokasi = (TextView) mView.findViewById(R.id.tv_lokasi);
        tvKonten = (TextView) mView.findViewById(R.id.tv_konten);
        btnSosmed = (ImageView) mView.findViewById(R.id.btn_sharesocmed);
        picProfileHeader = (ImageView) mView.findViewById(R.id.picprofile);
        picContent = (ImageView) mView.findViewById(R.id.piccontent);
        picProfileComment = (ImageView) mView.findViewById(R.id.piccomment);
        btnExit = (ImageView) mView.findViewById(R.id.btnexit);
        tvJudul.setText(judul);
        tvLokasi.setText(lokasi);
        tvKonten.setText(konten);
        btnExit.setOnClickListener(view1 -> getActivity().onBackPressed());
        btnKomentar.setOnClickListener(view1 -> {
            dialog.setMessage("Mengirim komentar, mohon menunggu..");
            dialog.setCancelable(false);
            dialog.show();
            authService.unggahKomentar(session.getUserId(), no, komentar.getText().toString(), new AuthService.DataCallback() {
                @Override
                public void onSuccess(ResponeApiModel responeRegisterModel) {
                    authService.getEventBus().post(new EventMessage("Sukses"));
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Sukses", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(Throwable e) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
                }
            });

        });
        btnLapor.setOnClickListener(view1 -> showAlert());
        btnSosmed.setOnClickListener(view1 -> {
            /*Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, judul);
            intent.putExtra(Intent.EXTRA_TEXT, konten);
            startActivity(Intent.createChooser(intent, "Share Artikel PATUH"));*/
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, judul);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Judul : " + judul + "\n" + "Artikel: " + konten + "\n Download Aplikasi PATUH : http://play.google.com/store/apps/details?id=" + getActivity().getApplicationContext().getPackageName());
            emailIntent.setType("text/plain");
            startActivity(Intent.createChooser(emailIntent, "Share Artikel PATUH"));
        });
        checkLike(String.valueOf(mArtikel.getId()), session.getUserId());
        final int[] button01pos = {0};
        /*toogleLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (button01pos[0] == 0) {
                    //toogleLike.setImageResource(R.drawable.ic_love);
                    toogleLike.setColorFilter(Color.BLACK);
                    button01pos[0] = 1;
                } else if (button01pos[0] == 1) {
                    //toogleLike.setImageResource(R.drawable.ic_like);
                    toogleLike.setColorFilter(Color.RED);
                    button01pos[0] = 0;
                }
            }
        });*/
        toogleLike.setOnClickListener(view1 -> postLike(String.valueOf(mArtikel.getId()), session.getUserId()));
        mRecycleView.setHasFixedSize(true);

        adapter = new KomenAdapter(getActivity(), komentars, new KomenAdapter.onClickLIstener() {
            @Override
            public void onItemClicked(int position) {

            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setAdapter(adapter);

        if (session.getUserId() != null){
            if (NetworkUtils.isOnline(mApp.getBaseContext())){
                Glide.with(getActivity())
                        .load(AppConfig.API_BASE_URL + "Article/getarticleimage?imageid=" + imageId)
                        .animate(R.anim.abc_fade_in)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(picContent);
                Glide.with(getActivity())
                        .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + mArtikel.getcCreated())
                        .animate(R.anim.abc_fade_in)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(picProfileHeader);
                Glide.with(getActivity())
                        .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + session.getUserId())
                        .animate(R.anim.abc_fade_in)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(picProfileComment);

            }

        }

        addData();
    }

    private enum VALUE {
        REASON1,
        REASON2,
        REASON3
    }

    private TextView reason1, reason2, reason3;
    private VALUE value = null;
    private void showAlert() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View promptView = layoutInflater.inflate(R.layout.layout_alert_spamreport, null);
        reason1 = (TextView) promptView.findViewById(R.id.tv_reason1);
        reason2 = (TextView) promptView.findViewById(R.id.tv_reason2);
        reason3 = (TextView) promptView.findViewById(R.id.tv_reason3);
        Button cancelLapor = (Button) promptView.findViewById(R.id.btn_cancel);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setCancelable(true);
        AlertDialog alertD = alertDialogBuilder.create();
        alertD.show();
        reason1.setOnClickListener(view -> {
            value = VALUE.REASON1;
            alertD.hide();
            postLaporan(String.valueOf(mArtikel.getId()), reason1.getText().toString());
                }
        );
        reason2.setOnClickListener(view -> {
            value = VALUE.REASON2;
            alertD.hide();
            postLaporan(String.valueOf(mArtikel.getId()), reason2.getText().toString());
                }
        );
        reason3.setOnClickListener(view -> {
            value = VALUE.REASON3;
            alertD.hide();
            postLaporan(String.valueOf(mArtikel.getId()), reason3.getText().toString());
                }
        );
        cancelLapor.setOnClickListener(view -> alertD.hide());
    }

    private void postLaporan(String artikelId, String deskripsi){
        dialog.setCancelable(false);
        dialog.show();
        if (NetworkUtils.isOnline(getActivity())){
            authService.laporKonten(artikelId, deskripsi, new AuthService.DataCallback() {
                @Override
                public void onSuccess(ResponeApiModel responeRegisterModel) {
                    //toogleLike.setColorFilter(like.get(0).getcStatus().equalsIgnoreCase("Y") ? colorFilter : new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY));
                    /*komentars.clear();
                    komentars.addAll(artikelList);
                    adapter.notifyDataSetChanged();*/
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Lapor berhasil", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail(Throwable e) {
                    toogleLike.setImageDrawable(toogleLike.getDrawable());
                    //toogleLike.setColorFilter(toogleLike.getColorFilter());
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Gagal, ulangi lagi", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(getActivity(), "Gagal, tidak ada internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLike(String articleId, String userId){
        dialog.setCancelable(false);
        dialog.show();
        if (NetworkUtils.isOnline(getActivity())){
            authService.getLikeStatus(articleId, userId, new AuthService.LikeCallback() {
                @Override
                public void onSuccess(ArrayList<Like> like) {
                    //toogleLike.setColorFilter(like.get(0).getcStatus().equalsIgnoreCase("Y") ? colorFilter : new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY));
                    if (like.size() != 0) toogleLike.setImageDrawable(like.get(0).getcStatus().equalsIgnoreCase("Y") ? getResources().getDrawable(R.drawable.ic_action_heart) : getResources().getDrawable(R.drawable.ic_action_heart_balck));
                    /*komentars.clear();
                    komentars.addAll(artikelList);
                    adapter.notifyDataSetChanged();*/
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    toogleLike.setImageDrawable(toogleLike.getDrawable());
                    //toogleLike.setColorFilter(toogleLike.getColorFilter());
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Gagal, ulangi lagi", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(getActivity(), "Gagal, tidak ada internet", Toast.LENGTH_SHORT).show();
        }
    }

    private PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DST_OUT);
    private void postLike(String articleId, String userId){
        dialog.setCancelable(false);
        dialog.show();
        if (NetworkUtils.isOnline(getActivity())){
            authService.unggahLike(articleId, userId, toogleLike.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_action_heart).getConstantState())  ? "N" : "Y", new AuthService.DataCallback() {
                @Override
                public void onSuccess(ResponeApiModel model) {
                    checkLike(articleId, userId);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    toogleLike.setImageDrawable(toogleLike.getDrawable());
                    //toogleLike.setColorFilter(toogleLike.getColorFilter());
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    Toast.makeText(getActivity(), "Gagal, ulangi lagi", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(getActivity(), "Gagal, tidak ada internet", Toast.LENGTH_SHORT).show();
        }
    }

    void addData(){
        if (NetworkUtils.isOnline(getActivity())){
            authService.getAllComments(no, new AuthService.KomentarCallback() {
                @Override
                public void onSuccess(ArrayList<Komentar> artikelList) {
                    komentars.clear();
                    komentars.addAll(artikelList);
                    adapter.notifyDataSetChanged();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFail(Throwable e) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(getActivity(), "Gagal, ulangi lagi", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(getActivity(), "Gagal, tidak ada internet", Toast.LENGTH_SHORT).show();
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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
        mBus.unregister(this);
        super.onDetach();
        mListener = null;
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
        //void onFragmentInteraction(Uri uri);
    }


    @Subscribe
    public void updateDataEvent(EventMessage eventMessage){
        dialog.setMessage("Memperbaharui data, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        addData();
    }



}
