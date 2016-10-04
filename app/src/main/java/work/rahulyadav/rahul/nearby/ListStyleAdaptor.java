package work.rahulyadav.rahul.nearby;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by RAHUL on 5/27/2016.
 */
public class ListStyleAdaptor extends BaseAdapter {
    String getUserlati;
    String getuserLongi;
    private ArrayList<PeopleData> items;
    LayoutInflater myinflater;
    public ListStyleAdaptor(Context con, ArrayList<PeopleData> item,String getLati,String getLongi){
        //categ=1 for people and 2 for Sp
        this.items = item;
        this.getUserlati=getLati;
        this.getuserLongi=getLongi;
        myinflater= (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        /*int Indsize=0;
        Iterator<PeopleData> itr=items.iterator();
        while(itr.hasNext()){
            PeopleData ob=itr.next();
            if(ob.userCategory.equals("Individual"))
                Indsize++;
        }
        if(fetchCategory==1)
            return Indsize;
        return items.size()-Indsize;*/
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
                convertView=myinflater.inflate(R.layout.list_withbutton,parent,false);
            TextView textOnTop= (TextView) convertView.findViewById(R.id.textName);
            TextView textOnDown= (TextView) convertView.findViewById(R.id.textDistance);
            ImageView dataImage= (ImageView) convertView.findViewById(R.id.imageButtonList);
            dataImage.setFocusable(false);
            Location myLoc=new Location("");
            myLoc.setLatitude(Double.parseDouble(getUserlati));
            myLoc.setLongitude(Double.parseDouble(getuserLongi));
            Location thisLoc = new Location("");
            thisLoc.setLatitude(Double.parseDouble(items.get(position).userLat));
            thisLoc.setLongitude(Double.parseDouble(items.get(position).userLong));
            Float distance = myLoc.distanceTo(thisLoc);
            int roundDist = Math.round(distance);
            String userDistanceFrom = roundDist > +1000 ? (((float) roundDist / 1000) + " Kilometers Away") : (roundDist + " Meters Away");
            textOnTop.setText(items.get(position).userName);
            textOnDown.setText(userDistanceFrom);
        return convertView;
    }
}
