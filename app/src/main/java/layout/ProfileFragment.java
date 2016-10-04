package layout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.Iterator;

import work.rahulyadav.rahul.nearby.HomeActivity;
import work.rahulyadav.rahul.nearby.LogInActivity;
import work.rahulyadav.rahul.nearby.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

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
  /*  public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/  TextView puserName;
    TextView puserDetails;
    TextView puserMobile;
    String currMob;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragView= inflater.inflate(R.layout.fragment_profile, container, false);
        Button logOut= (Button) fragView.findViewById(R.id.buttonLogOut);
        puserName= (TextView) fragView.findViewById(R.id.textUserName);
        puserDetails= (TextView) fragView.findViewById(R.id.textUDetails);
        puserMobile= (TextView) fragView.findViewById(R.id.textUMobile);
        SharedPreferences userdata=getActivity().getSharedPreferences("loggedINUserData",Context.MODE_PRIVATE);
        puserName.setText(userdata.getString("userName","Error"));
        puserMobile.setText(userdata.getString("userMobile",""));
        puserDetails.setText(userdata.getString("userDetails",""));
        //currMob=userdata.getString("userMobile","noNumber");

        /*Button buttonTest= (Button) fragView.findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.homeTabLayout);
                TabLayout.Tab tab = tabLayout.getTabAt(2);
                tab.select();
            }
        });*/

        /*Backendless.Data.of(BackendlessUser.class).find(new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                Iterator<BackendlessUser> userIterator=response.getCurrentPage().iterator();
                while(userIterator.hasNext()){
                    BackendlessUser userNext=userIterator.next();
                    String tempMobile=""+ userNext.getProperty("mobile");
                    if(tempMobile.equals(currMob)){
                        puserName.setText(""+userNext.getProperty("name"));
                        puserMobile.setText(tempMobile);
                        puserDetails.setText(""+userNext.getProperty("details"));
                        break;
                    }
                }
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Toast.makeText(getActivity(),"Something went wrong !!",Toast.LENGTH_SHORT).show();
                puserName.setText(" Error !!!");
            }
        });*/
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Backendless.UserService.logout(new AsyncCallback<Void>() {
                    @Override
                    public void handleResponse(Void response) {
                        Toast.makeText(getActivity(),"Logged out !!",Toast.LENGTH_SHORT).show();
                        Intent goToHome=new Intent(getActivity(),HomeActivity.class);
                        startActivity(goToHome);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Toast.makeText(getActivity(),"Failed Logging out !!",Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        return fragView;
    }

    // TODO: Rename method, update argument and hook method into UI event
 /*   public void onButtonPressed(Uri uri) {
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    */
}
