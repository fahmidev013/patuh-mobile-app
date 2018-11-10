package app.patuhmobile.module.Home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import app.patuhmobile.R;
import app.patuhmobile.auth.AuthInfo;
import app.patuhmobile.core.BaseActivity;
import app.patuhmobile.core.BaseFragment;


import app.patuhmobile.databinding.FragmentHomeBinding;
import app.patuhmobile.module.adapter.HomePagerAdapter;
import app.patuhmobile.module.adapter.NonSwipeableViewpager;
import app.patuhmobile.utils.DialogUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment implements HomeView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Inject
    HomeFragmentPresenter presenter;

    @Inject
    AuthInfo authInfo;

    private String mParam1;
    private String mParam2;

    @BindView(R.id.viewpager)
    NonSwipeableViewpager mViewPager;

    @BindView(R.id.btn_profile)
    ImageView btnProfileSetting;

    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;


    /*@BindView(R.id.txt_hello)
    TextView txt_hello;*/


    private FragmentHomeBinding binding;
    private HomePagerAdapter mAdapterViewpager;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        binding.setUser(authInfo);
        View mView = binding.getRoot();
        return mView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mAdapterViewpager = new HomePagerAdapter(getActivity(), getChildFragmentManager());
        mViewPager.setAdapter(mAdapterViewpager);
        mViewPager.setSwipeable(false);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.palleteaccentpurpledark));
        mViewPager.setCurrentItem(1);
        btnProfileSetting.setOnClickListener(view1 -> mListener.onHomeFragmentInteraction());

        //txt_hello.setText(authInfo.getUsername() + authInfo.getLoginType());
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onHomeFragmentInteraction();
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
        ((BaseActivity)getActivity()).component().inject(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

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
        void onHomeFragmentInteraction();
    }
}
