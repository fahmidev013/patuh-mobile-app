package app.patuhmobile.module.Home.Cerita;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.patuhmobile.App;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.core.BaseFragment;
import app.patuhmobile.model.Artikel;
import app.patuhmobile.module.Home.Panutan.PanutanFragment;
import app.patuhmobile.module.adapter.HorizontalRecycleViewAdapter;
import app.patuhmobile.utils.DialogUtils;
import app.patuhmobile.utils.NetworkUtils;
import app.patuhmobile.utils.ViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CeritaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CeritaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CeritaFragment extends BaseFragment implements CeritaView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.layoutview)
    RecyclerView mRecycleView;

    AuthService authService;
    App mApp;
    HorizontalRecycleViewAdapter adapter;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    ProgressDialog dialog;


    private OnFragmentInteractionListener mListener;

    public CeritaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CeritaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CeritaFragment newInstance(String param1, String param2) {
        CeritaFragment fragment = new CeritaFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cerita, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        setHasOptionsMenu(false);

        mRecycleView.setHasFixedSize(true);
        //mRecycleView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL));
        adapter = new HorizontalRecycleViewAdapter(getActivity(), artikels, new HorizontalRecycleViewAdapter.onClickLIstener() {
            @Override
            public void onItemClicked(int position) {
                mListener.onCeritaFragmentClickContent(artikels.get(position)
                );
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


    private ArrayList<PanutanFragment.Mahasiswa> mahasiswaArrayList;
    private ArrayList<Artikel> artikels = new ArrayList<>();


    void addData(){
        dialog.setMessage("Mengambil data, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        if (NetworkUtils.isOnline(getActivity())){
            authService.getAllNews(new AuthService.ArtikelCallback() {
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
            DialogUtils.showToast("Gagal, Jaringan internet tidak ada", getActivity());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
        void onCeritaFragmentClickContent(Artikel artikel);
    }
}