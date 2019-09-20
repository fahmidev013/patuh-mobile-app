package app.patuhmobile.module.TukarPoin;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import app.patuhmobile.App;
import app.patuhmobile.AppConfig;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.core.BaseFragment;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.model.Kupon;
import app.patuhmobile.model.Point;
import app.patuhmobile.model.ResponeApiModel;
import app.patuhmobile.module.adapter.AdapterRecview;
import app.patuhmobile.module.adapter.HorizontalRecycleViewAdapter;
import app.patuhmobile.utils.DialogUtils;
import app.patuhmobile.utils.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TukarPoinFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TukarPoinFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TukarPoinFragment extends BaseFragment implements TukarPoinView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    List<Kupon> kuponList = new ArrayList<>();
    private AuthService authService;
    ProgressDialog dialog;
    private App mApp;
    private AuthInfo session;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View mView;
    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecycleViewKupon;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView tvUser, tvPoin;
    private Button btnKuponSaya;
    private ImageView imgPic;
    private RelativeLayout fragContainer;
    AdapterRecview adapter;

    public TukarPoinFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TukarPoinFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TukarPoinFragment newInstance(String param1, String param2) {
        TukarPoinFragment fragment = new TukarPoinFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = App.get(getActivity());
        session = mApp.getAuthInfo();
        authService = new AuthService(mApp,mApp.getAuthInfo(), mApp.getEventBus());

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
        mView = inflater.inflate(R.layout.fragment_tukar_poin, container, false);
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUser = (TextView) mView.findViewById(R.id.tukarpoin_usertv);
        tvPoin = (TextView) mView.findViewById(R.id.tukarpoin_pointv);
        imgPic = (ImageView) mView.findViewById(R.id.tukarpoin_picimgv);
        btnKuponSaya = (Button) mView.findViewById(R.id.btn_kupon_saya);
        fragContainer = (RelativeLayout) mView.findViewById(R.id.child_fragcontainer);
        btnKuponSaya.setOnClickListener(view1 -> mListener.onTukarpoinFragmentInteraction());
        mRecycleViewKupon = (RecyclerView) mView.findViewById(R.id.rv_kupon);
        mRecycleViewKupon.setHasFixedSize(true);
        adapter = new AdapterRecview(kuponList, new AdapterRecview.KuponAdapterCallback() {
            @Override
            public void onClicked(Kupon kupon, int position) {
                ChildTukarFragment fragmentTukar = ChildTukarFragment.newInstance(kupon);
                FragmentTransaction ft =  getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(android.R.id.content, fragmentTukar);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecycleViewKupon.setLayoutManager(mLayoutManager);
        mRecycleViewKupon.setNestedScrollingEnabled(false);
        mRecycleViewKupon.setAdapter(adapter);

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
                        .into(imgPic);
                tvUser.setText(session.getUsername());
                authService.getPoints(session.getUserId(), new AuthService.PointCallback() {
                    @Override
                    public void onSuccess(Point point) {
                        tvPoin.setText(String.valueOf(point.getPoint()));
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });
                if (dialog.isShowing()) dialog.dismiss();
            }


        }

        addData();

    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onTukarpoinFragmentInteraction();
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


    void addData(){
        dialog.setMessage("Mengambil data, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        if (NetworkUtils.isOnline(getActivity())){
            authService.getAllKupon(new AuthService.KuponCallback() {
                @Override
                public void onKuponSuccess(ArrayList<Kupon> daftarKupon) {
                    kuponList.clear();
                    kuponList.addAll(daftarKupon);
                    adapter.notifyDataSetChanged();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onKuponFail(Throwable e) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    DialogUtils.showToast("Gagal, mohon ulangi lagi", getActivity());
                }
            });

        } else {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            DialogUtils.showToast("Tidak ada Internet", getActivity());
            //  view.hideProgress();
            //view.showToast("Tidak ada Internet");
        }

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
        void onTukarpoinFragmentInteraction();
    }
}
