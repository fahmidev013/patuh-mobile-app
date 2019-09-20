package app.patuhmobile.module.TukarPoin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.patuhmobile.App;
import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.auth.AuthService;
import app.patuhmobile.model.Kupon;
import app.patuhmobile.model.ResponeApiModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChildTukarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChildTukarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildTukarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private Kupon mKupon;
    private AuthService authService;
    ProgressDialog dialog;
    private App mApp;
    private AuthInfo session;

    private OnFragmentInteractionListener mListener;

    public ChildTukarFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChildTukarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChildTukarFragment newInstance(Kupon param1) {
        ChildTukarFragment fragment = new ChildTukarFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKupon = (Kupon) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_child_tukar, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mApp = App.get(getActivity());
        session = mApp.getAuthInfo();
        authService = new AuthService(mApp,mApp.getAuthInfo(), mApp.getEventBus());
        dialog = new ProgressDialog(getActivity());
        final ViewPager viewPager = (ViewPager) mView.findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager, mKupon);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.htab_tabs);
        Button btnTukarPoin = (Button) view.findViewById(R.id.btn_tukar_poin);
        btnTukarPoin.setOnClickListener(view1 -> {
                    tukarPointClicked(mKupon);
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        // TODO: 31/03/17
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void setupViewPager(ViewPager viewPager, Kupon kupon) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFrag(new DummyFragment(
                ContextCompat.getColor(getActivity(), R.color.palleteaccentpurpledark), kupon, KUPONFLAG.BENEFIT), "KEUNTUNGAN");
        adapter.addFrag(new DummyFragment(
                ContextCompat.getColor(getActivity(), R.color.palletepurplebg), kupon, KUPONFLAG.USAGE), "CARA PAKAI");
        adapter.addFrag(new DummyFragment(
                ContextCompat.getColor(getActivity(), R.color.palleteaccentcyan), kupon, KUPONFLAG.TNC), "S & K");
        viewPager.setAdapter(adapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onChildTukarFragmentInteraction(uri);
        }
    }

    private void tukarPointClicked(Kupon kupon){
        dialog.setMessage("Menukar kupon, mohon menunggu..");
        dialog.setCancelable(false);
        dialog.show();
        authService.tukarKupon(kupon.getId(), session.getUserId(), new AuthService.DataCallback() {
            @Override
            public void onSuccess(ResponeApiModel responeRegisterModel) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(), "Berhasil, kode kupon" + responeRegisterModel.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail(Throwable e) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(getActivity(), "GAGAL!!" + e, Toast.LENGTH_SHORT).show();
            }
        });
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


    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public enum KUPONFLAG {
        TNC, BENEFIT, USAGE
    }

    public static class DummyFragment extends Fragment {
        int color;
        Kupon mKupon;
        KUPONFLAG flagValue;

        public DummyFragment() {
        }

        @SuppressLint("ValidFragment")
        public DummyFragment(int color, Kupon kupon, KUPONFLAG kuponflag) {
            this.color = color;
            this.mKupon = kupon;
            this.flagValue = kuponflag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);

            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);

            return view;
        }


        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TextView tvKonten = (TextView) view.findViewById(R.id.tv_dummy_konten);
            switch (flagValue){
                case TNC:
                    tvKonten.setText(mKupon.getTnc());
                    break;
                case USAGE:
                    tvKonten.setText(mKupon.getUsage());
                    break;
                case BENEFIT:
                    tvKonten.setText(mKupon.getBenefit());
                    break;
            }
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
        void onChildTukarFragmentInteraction(Uri uri);
    }
}
