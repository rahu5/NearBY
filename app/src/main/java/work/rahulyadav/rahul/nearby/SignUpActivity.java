package work.rahulyadav.rahul.nearby;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class SignUpActivity extends AppCompatActivity{
    private String[] loc;
    RadioButton radioInd;
    RadioButton radioService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_sign_up);
        final EditText userName = (EditText) findViewById(R.id.editTextLoginMobile);
        final EditText userMobile = (EditText) findViewById(R.id.editTextRegisterMobile);
        final EditText userDetails = (EditText) findViewById(R.id.editTextDetails);
        final EditText userPassword = (EditText) findViewById(R.id.editTextLogInPassword);
        final RadioGroup category = (RadioGroup) findViewById(R.id.radioGroup);
        radioInd= (RadioButton) findViewById(R.id.radioButtonIndividual);
        radioService= (RadioButton) findViewById(R.id.radioButtonService);
        ImageButton registerMe = (ImageButton) findViewById(R.id.imageButtonRegister);

        if(getIntent()!=null){
            loc=getIntent().getStringArrayExtra("work.rahulyadav.rahul.loginToSignUp");
        }
        registerMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String pass = userPassword.getText().toString();
                String mobile = userMobile.getText().toString();
                String detail = userDetails.getText().toString();
                int radioId = category.getCheckedRadioButtonId();
                RadioButton cat = (RadioButton) findViewById(radioId);
                String catgry = (String) cat.getText();
                //register User
                BackendlessUser resiterUser=new BackendlessUser();
                resiterUser.setPassword(pass);
                resiterUser.setProperty("name", user);
                resiterUser.setProperty("mobile", mobile);
                resiterUser.setProperty("details", detail);
                resiterUser.setProperty("category", catgry);
                if(loc!=null) {
                    resiterUser.setProperty("locLatitude", loc[0]);
                    resiterUser.setProperty("locLongitude", loc[1]);
                }else {
                    resiterUser.setProperty("locLatitude", "26.9");
                    resiterUser.setProperty("locLongitude", "80.9");
                }
                Backendless.UserService.register(resiterUser, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        Toast.makeText(SignUpActivity.this,"Hurray!! You've got registered",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(SignUpActivity.this,"Dang!! Mobile already registered.",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }
    public void onRegisterRadioClicked(View v){
        // Is the button now checked?
        boolean checked = ((RadioButton) v).isChecked();

        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.radioButtonIndividual:
                if (checked)
                    radioInd.setChecked(true);
                radioService.setChecked(false);
                    break;
            case R.id.radioButtonService:
                if (checked)
                    radioInd.setChecked(false);
                    radioService.setChecked(true);
                    break;
        }
    }
}
