package app.patuhmobile.module.Home.Cerita;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import app.patuhmobile.model.Artikel;
import app.patuhmobile.model.Komentar;
import app.patuhmobile.model.ResponeApiModel;
import app.patuhmobile.module.adapter.KomenAdapter;
import app.patuhmobile.utils.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailCeritaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetailCeritaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailCeritaFragment extends Fragment {
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

    private ImageView picProfileHeader, picProfileComment, picContent;
    private TextView tvJudul, tvLokasi, tvKonten, btnKomentar;

    private EditText komentar;

    private RecyclerView mRecycleView;

    private RecyclerView.LayoutManager mLayoutManager;
    KomenAdapter adapter;

    private Artikel artikel;

    private ArrayList<Komentar> komentars = new ArrayList<>();

    private OnFragmentInteractionListener mListener;


    private EventBus eventBus;

    public DetailCeritaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailCeritaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailCeritaFragment newInstance(Artikel artikel) {
        DetailCeritaFragment fragment = new DetailCeritaFragment();
        Bundle args = new Bundle();
        args.putSerializable("Artikel", artikel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = new EventBus();
        eventBus.register(this);
        if (getArguments() != null) {
            this.artikel = (Artikel) getArguments().getSerializable("Artikel");
            judul = artikel.getTitle();
            kategori = artikel.getCategory();
            lokasi = artikel.getGPSLocation();
            konten = artikel.getStory();
            imageId = artikel.getImageIds().get(0);
            no = String.valueOf(artikel.getId());
        }
        dialog = new ProgressDialog(getActivity());
        mApp = App.get(getActivity());
        session = mApp.getAuthInfo();
        authService = new AuthService(mApp, session, new EventBus());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog.setMessage("Memuat data, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        mView = inflater.inflate(R.layout.fragment_detail_cerita, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        komentar = (EditText) mView.findViewById(R.id.komentar);
        mRecycleView = (RecyclerView) mView.findViewById(R.id.layoutview);
        btnKomentar = (TextView) mView.findViewById(R.id.btn_kirim_komentar);
        tvJudul = (TextView) mView.findViewById(R.id.tv_judul);
        toogleLike = (ImageView) mView.findViewById(R.id.toogle_like);
        tvLokasi = (TextView) mView.findViewById(R.id.tv_lokasi);
        tvKonten = (TextView) mView.findViewById(R.id.tv_konten);
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
                    eventBus.post(new EventMessage(""));
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

        final int[] button01pos = {0};
        toogleLike.setOnClickListener(new View.OnClickListener() {
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
        });
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
                        .load(AppConfig.API_BASE_URL + "UserProfile/GetProfilePic?UserId=" + session.getUserId())
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
//            mListener.onFragmentInteraction(uri);
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
        eventBus.unregister(this);
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
        void onFragmentInteraction(Uri uri);
    }


    @Subscribe
    public void updateDataEvent(EventMessage eventMessage){
        dialog.setMessage("Memperbaharui data, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        addData();
    }


}
