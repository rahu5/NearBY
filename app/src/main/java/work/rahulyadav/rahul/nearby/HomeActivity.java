package work.rahulyadav.rahul.nearby;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import layout.PeopleFragment;
import layout.ProfileFragment;
import layout.RecentFragment;
import layout.ServicesFragment;


public class HomeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static final String APP_ID = "746EFCB0-484C-38BF-FFC7-8BA976CBF900";
    public static final String SECRET_KEY = "3DB6A532-7724-1BF8-FFAB-BC3FF60FDE00";
    public static final String VERSION = "v1";
    private static final int MY_PERMISSIONS_LOCATIONS =0 ;
    private ActionBar actionBar;
    private ViewPager viewPagerMain;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private String mLatitudeText;
    private String mLongitudeText;
    SharedPreferences loginUserData;
    public TabLayout tabLayoutMain;
    String currentMobile;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    private void proceedAfterGPS(){
        try {
            Backendless.initApp(this, APP_ID, SECRET_KEY, VERSION);
            if (Backendless.UserService.loggedInUser() == "") {
                Toast.makeText(this, "location:"+mLatitudeText+":"+mLongitudeText, Toast.LENGTH_SHORT).show();
                Intent newInt = new Intent(this, LogInActivity.class);
                String[] locSend={mLatitudeText,mLongitudeText};
                newInt.putExtra("work.rahulyadav.rahul.gpsLocation",locSend);
                startActivity(newInt);

            } else {
                loginUserData=getSharedPreferences("loggedINUserData",MODE_PRIVATE);
                SharedPreferences.Editor dataEditor=loginUserData.edit();
                dataEditor.putString("userLati",mLatitudeText);
                dataEditor.putString("userLongi",mLongitudeText);
                dataEditor.apply();
                Toast.makeText(this, "logged In"+mLatitudeText+" mobile: "+loginUserData.getString("userMobile",null), Toast.LENGTH_SHORT).show();
                FragmentPagerAdapter pagerAdapter = new ConnectPagerAdapter(getSupportFragmentManager(), HomeActivity.this);
                viewPagerMain = (ViewPager) findViewById(R.id.homeViewPager);
                viewPagerMain.setAdapter(pagerAdapter);
                tabLayoutMain = (TabLayout) findViewById(R.id.homeTabLayout);
                //tabLayoutMain.setTabsFromPagerAdapter(pagerAdapter);
                tabLayoutMain.setupWithViewPager(viewPagerMain);
                viewPagerMain.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutMain));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something Went Wrong:Home", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);

        }else{
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
            if (mLastLocation != null) {
                 mLatitudeText=String.valueOf(mLastLocation.getLatitude());
                mLongitudeText=String.valueOf(mLastLocation.getLongitude());
                proceedAfterGPS();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    if (mLastLocation != null) {
                        mLatitudeText=String.valueOf(mLastLocation.getLatitude());
                        mLongitudeText=String.valueOf(mLastLocation.getLongitude());
                        proceedAfterGPS();
                    } else {
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                }else
                    startActivity(new Intent(this,ErrorActivity.class));

                // other 'case' lines to check for other
                // permissions this app might request
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Connection disconnected",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Can't connect",Toast.LENGTH_LONG).show();
    }
}
class ConnectPagerAdapter extends FragmentPagerAdapter {
    final int TOTAL_PAGES=3;
    private final Context context;
    private int[] tabImageIconsId={R.drawable.discover_image,
            R.drawable.chats_image,
            R.drawable.connections_image};
    private String[] tabNames={"PEOPLE","RECENT","PROFILE"};
    public ConnectPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context=context;

    }

    @Override
    public CharSequence getPageTitle(int position) {
       // Drawable image = ContextCompat.getDrawable(context, tabImageIconsId[position]);
        //image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(tabNames[position]);
        //ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        //sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new PeopleFragment();
            case 1:return new RecentFragment();
            case 2:return new ProfileFragment();
        }
        return new RecentFragment();
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }
}
