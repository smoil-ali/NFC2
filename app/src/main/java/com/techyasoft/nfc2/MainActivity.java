package com.techyasoft.nfc2;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.techyasoft.nfc2.Activities.LoginActivity;
import com.techyasoft.nfc2.Activities.ScanningActivity;
import com.techyasoft.nfc2.Activities.ScanningActivity2;
import com.techyasoft.nfc2.Connections.mConnection;
import com.techyasoft.nfc2.Interfaces.mConnectionListener;
import com.techyasoft.nfc2.Utils.Helper;
import com.techyasoft.nfc2.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements mConnectionListener {


    final String TAG=MainActivity.class.getSimpleName();
    NfcAdapter nfcAdapter;


    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        hideUi();
        nfcAdapter= NfcAdapter.getDefaultAdapter(this);
        if (false){
            Toast.makeText(this, "This device does't support NFC", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            new mConnection(this,this);
        }

    }

    public void hideUi(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    public void OnSuccess() {
        binding.text.setText("Success");
        openScreen();
    }

    @Override
    public void OnFail(String msg) {
        binding.text.setText(msg);
        binding.text.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.GONE);
    }

    public void openScreen(){
        if (Helper.getLogInStatus(this)){
            Intent intent=new Intent(this, ScanningActivity2.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent=new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"resume is running");
    }
}