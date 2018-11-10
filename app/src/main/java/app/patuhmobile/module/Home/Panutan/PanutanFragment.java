package app.patuhmobile.module.Home.Panutan;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.patuhmobile.App;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.core.BaseFragment;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.module.adapter.HorizontalRecycleViewAdapter;
import app.patuhmobile.utils.DialogUtils;
import app.patuhmobile.utils.NetworkUtils;
import app.patuhmobile.utils.ViewUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PanutanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PanutanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PanutanFragment extends BaseFragment implements PanutanView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    /*@BindView(R.id.rv)
    RecyclerView mRecycleView;*/

    //@BindView(R.id.layoutview)
    RecyclerView mRecycleView;

    private RecyclerView.LayoutManager mLayoutManager;

    AuthService authService;
    App mApp;

    ProgressDialog dialog;

    HorizontalRecycleViewAdapter adapter;
    private RecyclerView.Adapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PanutanFragment() {
        // Required empty public constructor
    }


    public static PanutanFragment newInstance(String param1, String param2) {
        PanutanFragment fragment = new PanutanFragment();
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
        authService = new AuthService(mApp,mApp.getAuthInfo(), mApp.getEventBus());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dialog = new ProgressDialog(getActivity());
    }
    View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_panutan, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ButterKnife.bind(this, view);
        setHasOptionsMenu(false);

        mRecycleView = (RecyclerView)  mView.findViewById(R.id.layoutview);
        mRecycleView.setHasFixedSize(true);
        //mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
       adapter = new HorizontalRecycleViewAdapter(getActivity(), artikels, new HorizontalRecycleViewAdapter.onClickLIstener() {
            @Override
            public void onItemClicked(int position) {
                try {
                    mListener.onPanutanFragmentClickContent(
                            artikels.get(position)
                    );
                } catch (Exception e) {}

            }
        });
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecycleView.setLayoutManager(mLayoutManager);
        mRecycleView.setNestedScrollingEnabled(false);
        mRecycleView.setAdapter(adapter);
        SnapHelper helper = new ViewUtils.SnapHelperOneByOne();
        helper.attachToRecyclerView(mRecycleView);

        addData();
    }



    private ArrayList<Mahasiswa> mahasiswaArrayList;
    private ArrayList<Artikel> artikels = new ArrayList<>();


    void addData(){
        dialog.setMessage("Mengambil data, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        if (NetworkUtils.isOnline(getActivity())){
            authService.getAllArticle(new AuthService.ArtikelCallback() {
                @Override
                public void onSuccess(ArrayList<Artikel> artikelList) {
                    artikels.clear();
                    artikels.addAll(artikelList);
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPanutanFragmentInteraction();
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
        void onPanutanFragmentInteraction();
        void onPanutanFragmentClickContent(Artikel artikel);
    }




    public class Mahasiswa {

        private String judul;
        private String cerita;
        private String lokasi;

        public Mahasiswa(String judul, String cerita, String lokasi) {
            this.judul = judul;
            this.cerita = cerita;
            this.lokasi = lokasi;

        }

        public String getJudul() {
            return judul;
        }

        public void setJudul(String judul) {
            this.judul = judul;
        }

        public String getCerita() {
            return cerita;
        }

        public void setCerita(String cerita) {
            this.cerita = cerita;
        }

        public String getLokasi() {
            return lokasi;
        }

        public void setLokasi(String lokasi) {
            this.lokasi = lokasi;
        }
    }

}
