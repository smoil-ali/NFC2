package com.techyasoft.nfc2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.techyasoft.nfc2.Interfaces.LoginQueryListener;
import com.techyasoft.nfc2.MainActivity;
import com.techyasoft.nfc2.Queries.LoginQuery;
import com.techyasoft.nfc2.Utils.Helper;
import com.techyasoft.nfc2.databinding.ActivityLoginBinding;
import com.techyasoft.nfc2.model.Profile;


public class LoginActivity extends AppCompatActivity implements LoginQueryListener {

    final String TAG = LoginActivity.class.getSimpleName();
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideUi();
        binding.btnLogin.setOnClickListener(v -> click());

    }

    public void click(){
        if (isValid()){
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnLogin.setText("");
            LoginQuery loginQuery =new LoginQuery(this,binding.
                    userName.getText().toString(),
                    binding.password.getText().toString(),this);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loginQuery.executeQuery();
                }
            }).start();
        }
    }

    public boolean isValid(){
        if (binding.userName.getText().toString().trim().matches("")){
            return false;
        }else return !binding.password.getText().toString().trim().matches("");
    }


    @Override
    public void OnQuerySuccess(int size, Profile mProfile) {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText("LOGIN");
        Log.i(TAG,"Size "+mProfile.getUserName());
        if (size>0){
            Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT).show();
            Helper.setProfileInfo(mProfile,this);
            Helper.setLogInTrue(this,true);
            openScreen();
        }else {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void OnQueryFail(String msg) {
        binding.progressBar.setVisibility(View.GONE);
        binding.btnLogin.setText("LOGIN");
        Log.i(TAG,msg);
    }

    public void openScreen(){
        Intent intent=new Intent(this, ScanningActivity.class);
        startActivity(intent);
        finish();
    }

    public void hideUi(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}