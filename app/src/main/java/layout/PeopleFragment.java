package layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.ArrayList;
import java.util.Iterator;

import work.rahulyadav.rahul.nearby.ListStyleAdaptor;
import work.rahulyadav.rahul.nearby.PeopleData;
import work.rahulyadav.rahul.nearby.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PeopleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<PeopleData> peopledata;
    ArrayList<PeopleData> forSearchStore;
    private ListStyleAdaptor peopleAdaptor;
    private ListView peopleList;
    private EditText searchtext;
    private Button searchButton;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public PeopleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static PeopleFragment newInstance(String param1, String param2) {
        PeopleFragment fragment = new PeopleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View peopleView= inflater.inflate(R.layout.fragment_people, container, false);
        peopleList= (ListView) peopleView.findViewById(R.id.listPeople);
        searchtext= (EditText) peopleView.findViewById(R.id.editSearchText);
        searchButton= (Button) peopleView.findViewById(R.id.buttonSearch);
        peopledata=new ArrayList<>();
        forSearchStore=new ArrayList<>();
        final Context thisContext=getActivity();
        Backendless.Data.of(BackendlessUser.class).find(new AsyncCallback<BackendlessCollection<BackendlessUser>>() {
            @Override
            public void handleResponse(BackendlessCollection<BackendlessUser> response) {
                Iterator<BackendlessUser> userIterator=response.getCurrentPage().iterator();
                int counting=0;
                while(userIterator.hasNext() && counting<50){
                    BackendlessUser userNext=userIterator.next();
                    peopledata.add(new PeopleData(""+userNext.getProperty("name"),
                            ""+userNext.getProperty("mobile"),
                            ""+userNext.getProperty("category"),
                            ""+userNext.getProperty("details"),
                            ""+userNext.getProperty("locLatitude"),
                            ""+userNext.getProperty("locLongitude")));
                }
                SharedPreferences getUserData= thisContext.getSharedPreferences("loggedINUserData",Context.MODE_PRIVATE);
                String sendlati=getUserData.getString("userLati","26.9");
                String sendlongi=getUserData.getString("userLongi","80.9");
                peopleAdaptor=new ListStyleAdaptor(getContext(),peopledata,sendlati,sendlongi);
                peopleList.setAdapter(peopleAdaptor);
            }

            @Override
            public void handleFault(BackendlessFault fault) {

            }
        });
        peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),peopledata.get(position).userName+"touched",Toast.LENGTH_SHORT).show();
                System.out.println("TouchedAAAAAAAAA");
                SharedPreferences currListItemData = getActivity().getSharedPreferences("currentListItemData",Context.MODE_PRIVATE);
                SharedPreferences.Editor dataEditor=currListItemData.edit();
                dataEditor.putString("userMobile",peopledata.get(position).userMobile);
                dataEditor.putString("userName",peopledata.get(position).userName);
                dataEditor.putString("userDetails",peopledata.get(position).userDetails);
                dataEditor.putString("userLat",peopledata.get(position).userLat);
                dataEditor.putString("userLong",peopledata.get(position).userLong);
                dataEditor.apply();
                TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.homeTabLayout);
                TabLayout.Tab tab = tabLayout.getTabAt(1);
                tab.select();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getSearchText=searchtext.getText().toString().toLowerCase();
                if(forSearchStore.size()==0) {
                    Iterator<PeopleData> delItr = peopledata.iterator();
                    while (delItr.hasNext()) {
                        forSearchStore.add(delItr.next());
                    }
                }
                peopledata.clear();
                Iterator<PeopleData> itr=forSearchStore.iterator();
                while (itr.hasNext()){
                    PeopleData ob=itr.next();
                    System.out.println(ob.userDetails);
                    if(ob.userDetails.toLowerCase().contains(getSearchText)){
                        peopledata.add(ob);
                    }
                }
                peopleAdaptor.notifyDataSetChanged();
                searchtext.setText("");
            }
        });
        return peopleView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
