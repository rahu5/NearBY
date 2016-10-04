package work.rahulyadav.rahul.nearby;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class LogInActivity extends AppCompatActivity {
    private EditText getMob;
    private EditText getPass;
    SharedPreferences loginUserData;
    private String[] getLocationLogIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_log_in);
        getMob= (EditText) findViewById(R.id.editTextLoginMobile);
        getPass= (EditText) findViewById(R.id.editTextLogInPassword);
        ImageButton signUp= (ImageButton) findViewById(R.id.imageButtonSignUp);
        if(getIntent()!=null){
            getLocationLogIn=getIntent().getStringArrayExtra("work.rahulyadav.rahul.gpsLocation");
        }
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LogInActivity.this,"moving to sign up",Toast.LENGTH_SHORT).show();
                Intent gotoSignUp=new Intent(LogInActivity.this,SignUpActivity.class);
                gotoSignUp.putExtra("work.rahulyadav.rahul.loginToSignUp",getLocationLogIn);
                startActivity(gotoSignUp);
            }
        });
        ImageButton logIn= (ImageButton) findViewById(R.id.imageButtonLogIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mob=getMob.getText().toString();
                String pass=getPass.getText().toString();
                Backendless.UserService.login(mob, pass, new AsyncCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        Intent goToHome=new Intent(LogInActivity.this,HomeActivity.class);
                        loginUserData=getSharedPreferences("loggedINUserData",MODE_PRIVATE);
                        SharedPreferences.Editor dataEditor=loginUserData.edit();
                        dataEditor.putString("userMobile",mob);
                        dataEditor.putString("userName",backendlessUser.getProperty("name").toString());
                        dataEditor.putString("userDetails",backendlessUser.getProperty("details").toString());
                        dataEditor.apply();
                        startActivity(goToHome);
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault) {
                        Toast.makeText(LogInActivity.this,"Wrong Fields",Toast.LENGTH_SHORT).show();
                    }
                },true);
            }
        });
    }
}
