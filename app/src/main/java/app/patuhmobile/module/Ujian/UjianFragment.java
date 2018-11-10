package app.patuhmobile.module.Ujian;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import app.patuhmobile.R;
import app.patuhmobile.core.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UjianFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UjianFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UjianFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UjianFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UjianFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UjianFragment newInstance(String param1, String param2) {
        UjianFragment fragment = new UjianFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ujian, container, false);
    }

    boolean isClicked = false;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout btnSimA = (RelativeLayout) getView().findViewById(R.id.btnsima);
        RelativeLayout btnSimC = (RelativeLayout) getView().findViewById(R.id.btnsimc);
        RelativeLayout mContainer = (RelativeLayout) getView().findViewById(R.id.fragcontainer);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        btnSimA.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           isClicked = (isClicked) ? false : true;
                                           if (isClicked) mContainer.setVisibility(View.VISIBLE);
                                           ft.replace(R.id.fragcontainer, new JawabanFragment());
                                           ft.addToBackStack(null);
                                           ft.commit();
                                       }
                                   }

        );
        btnSimC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked = (isClicked) ? false : true;
                if (isClicked) mContainer.setVisibility(View.VISIBLE);
                ft.replace(R.id.fragcontainer, new JawabanFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
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
    }
}
