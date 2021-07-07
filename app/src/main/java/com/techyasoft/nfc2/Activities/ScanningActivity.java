package com.techyasoft.nfc2.Activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.techyasoft.nfc2.Adapters.detailAdapterUser;
import com.techyasoft.nfc2.Interfaces.CheckCardNumerQueryListener;
import com.techyasoft.nfc2.Interfaces.GuardDetailQueryListener;
import com.techyasoft.nfc2.Interfaces.GuardTourQueryListener;
import com.techyasoft.nfc2.Interfaces.QueryListener;
import com.techyasoft.nfc2.Queries.CheckCardNumberQuery;
import com.techyasoft.nfc2.Queries.GuardDetailGetQuery;
import com.techyasoft.nfc2.Queries.GuardDetailQuery;
import com.techyasoft.nfc2.Queries.GuardTourQuery;
import com.techyasoft.nfc2.Queries.UpdateGuardTourStatusQuery;
import com.techyasoft.nfc2.Utils.Helper;
import com.techyasoft.nfc2.databinding.ActivityScanningBinding;
import com.techyasoft.nfc2.model.CardData;
import com.techyasoft.nfc2.model.GuardDetail;
import com.techyasoft.nfc2.model.GuardTour;
import com.techyasoft.nfc2.model.Profile;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ScanningActivity extends AppCompatActivity {

    final String TAG = ScanningActivity.class.getSimpleName();
    ActivityScanningBinding binding;
    Profile mProfile;
    int tag_counter = 0;
    int tour_counter = 0;

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] writingTagFilters;
    boolean writeMode;
    Tag Mytag;
    Context context;
    GuardTour guard;
    public GuardDetail guardDetail;
    Profile profile;
    boolean addQuery;
    SimpleDateFormat sdf;
    String tour_hour,tour_minutes,tour_seconds;
    detailAdapterUser adapterUser;
    HashMap<Integer,Integer> list = new HashMap<>();
    List<String> cardList = new ArrayList<>();
    HashMap<Integer,List<String>> listHashMap = new HashMap<>();
    boolean isRunning;
    long timer;
    public static int TOTAL_CONTAINERS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityScanningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        hideUi();
        context=this;
        mProfile= Helper.getProfileInfo(this);

        init();


        sdf = new SimpleDateFormat("yyyy-MM-dd");

        binding.recycler.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.recycler.setLayoutManager(linearLayoutManager);
        adapterUser = new detailAdapterUser(this,list);
        binding.recycler.setAdapter(adapterUser);
    }
    public void hideUi(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void init(){
        context=this;


        nfcAdapter=NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null){
            Toast.makeText(context, "This device does't support NFC", Toast.LENGTH_SHORT).show();
            finish();
        }

        readFromIntent(getIntent());
        pendingIntent=PendingIntent.getActivity(this,0,new Intent(this,getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
        IntentFilter tagDetected=new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters=new IntentFilter[]{tagDetected};
    }

    private void readFromIntent(Intent intent) {
        String action=intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
        ){

            byte[] tagId = getIntent().getByteArrayExtra(NfcAdapter.EXTRA_ID);
            String hexdump = new String();
            for (int i = 0; i < tagId.length; i++) {
                String x = Integer.toHexString(((int) tagId[i] & 0xff));
                if (x.length() == 1) {
                    x = '0' + x;
                }
                hexdump += x + ' ';
            }
            Log.i("EHEHEHEHEHE",hexdump);
            updateTagCounter(hexdump);

            Parcelable[] rawMsgs=intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs=null;
            if (rawMsgs !=null){
                msgs=new NdefMessage[rawMsgs.length];
                for (int i=0;i<rawMsgs.length;i++){
                    msgs[i]=(NdefMessage) rawMsgs[i];

                }

            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs==null || msgs.length==0)return;
        String text="";
        byte[] payload=msgs[0].getRecords()[0].getPayload();
        String textEncoding=((payload[0] & 128)==0)?"UTF-8" : "UTF-16";//Get the text encoding
        int languageCodeLength= payload[0] & 0063;

        try{
            text=new String(payload,languageCodeLength+1,payload.length - languageCodeLength-1,textEncoding);
        } catch (UnsupportedEncodingException e){
            Log.e("UnSupported",e.toString());
        }


    }


    private void WriteModeOff() {
        writeMode=false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void WriteModeOn() {
        writeMode=true;
        nfcAdapter.enableForegroundDispatch(this,pendingIntent,writingTagFilters,null);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            Mytag=intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isRunning){
            getAssignData();
            getGuardDetail();
        }else {
            resumeCountDown();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        WriteModeOn();
    }

    void getAssignData(){
        profile = Helper.getProfileInfo(this);
        Log.i(TAG,profile.toString());
        GuardTourQuery guardTourQuery = new GuardTourQuery(this,profile.getId());
        guardTourQuery.setListener(new GuardTourQueryListener() {
            @Override
            public void OnQuerySuccess(GuardTour guardTour) {
                Log.i(TAG,guardTour.toString());
                guard = guardTour;
                tour_hour = Integer.parseInt(guard.getTour_hour()) < 10 ? "0"+guard.getTour_hour(): guardTour.getTour_hour()+"";
                tour_minutes = Integer.parseInt(guard.getTour_minutes()) < 10 ? "0"+guard.getTour_minutes(): guardTour.getTour_minutes()+"";
                tour_seconds = Integer.parseInt(guard.getTour_seconds()) < 10 ? "0"+guard.getTour_seconds(): guardTour.getTour_seconds()+"";
                TOTAL_CONTAINERS = Integer.parseInt(guard.getTour_container());
                countDown();
            }

            @Override
            public void onNotFound() {
                Log.i(TAG,"Data Not Found");
            }

            @Override
            public void OnQueryFail(String msg) {
                Log.i(TAG,msg);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                guardTourQuery.executeQuery();
            }
        }).start();

    }

    void getGuardDetail(){
        GuardDetailGetQuery guardDetailGetQuery = new GuardDetailGetQuery(this,
                profile.getId(),sdf.format(new Date()), new GuardDetailQueryListener() {
            @Override
            public void OnQuerySuccess(GuardDetail guardDetail) {
                Log.i(TAG,guardDetail.toString());
                ScanningActivity.this.guardDetail = guardDetail;
                if (guardDetail.getGuard_id() == null)
                    addQuery = true;
                else{
                    addQuery = false;
                    list.putAll(Helper.ConvertStringToHashMap(guardDetail.getDetails()));
                    if (guardDetail.getTour_counter() != null)
                        tour_counter = Integer.parseInt(guardDetail.getTour_counter());
                    addToAdapter();
                }

            }

            @Override
            public void onNotFound() {
                Log.i(TAG,"Not Found");
            }

            @Override
            public void OnQueryFail(String msg) {
                Log.i(TAG,msg);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                guardDetailGetQuery.executeQuery();
            }
        }).start();
    }

    void updateTagCounter(String cardNumber){

        CheckCardNumberQuery query = new CheckCardNumberQuery(this, new CheckCardNumerQueryListener() {
            @Override
            public void onFound(CardData data) {
                Log.i(TAG,data.toString());
                cardList.add(data.getBox_name());
                tag_counter++;
            }

            @Override
            public void onNotFound() {
                Log.i(TAG,"Not Found");
                Toast.makeText(context, "Card Not Found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String msg) {
                Log.i(TAG,msg);
            }
        },cardNumber);
        new Thread(new Runnable() {
            @Override
            public void run() {
                query.executeQuery();
            }
        }).start();
    }

    void resetTagCounter(){
        tag_counter = 0;
        cardList.clear();
    }

    void updateGuardTourStatusS(){
        HashMap<Integer, Integer> hashMap;
        if (guardDetail.getDetails() == null){
            hashMap = new HashMap<>();
        }else
            hashMap = Helper.ConvertStringToHashMap(guardDetail.getDetails());
        hashMap.put(tour_counter,tag_counter);
        if (guardDetail.getBox() == null || guardDetail.getBox().equals("null")){
            listHashMap = new HashMap<>();
        }
        else{
            listHashMap = Helper.ConvertStringToList(guardDetail.getBox());
        }
        listHashMap.put(tour_counter,cardList);
        guardDetail.setDetails(Helper.ConvertHashMapToString(hashMap));
        guardDetail.setGuard_id(profile.getId());
        guardDetail.setDate(sdf.format(new Date()));
        guardDetail.setTour_counter(tour_counter+"");
        guardDetail.setBox(Helper.ConvertListToString(listHashMap));
        reset(hashMap);
        GuardDetailQuery guardDetailQuery = new GuardDetailQuery(this);
        guardDetailQuery.setListener(new QueryListener() {
            @Override
            public void OnQuerySuccess() {
                Log.i(TAG,"Success");
            }

            @Override
            public void OnQueryFail(String msg) {
                Log.i(TAG,msg);
            }
        });
        if (addQuery){
            guardDetailQuery.addDetail(guardDetail);
            addQuery = false;
        }else{
            guardDetailQuery.updateDetail(guardDetail);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                guardDetailQuery.executeQuery();
            }
        }).start();
    }

    void addToAdapter(){
        Log.i(TAG,list.size() + "");
        adapterUser.notifyItemInserted(list.size());
    }

    void countDown(){
        binding.timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
                updateTagCounter("1234");
                if (hh.equals(tour_hour) && mm.equals(tour_minutes) && ss.equals(tour_seconds)){
                    tour_counter++;
                    Log.i(TAG,"equal");
                    updateGuardTourStatusS();
                }
            }
        });
        binding.timer.setBase(SystemClock.elapsedRealtime());
        binding.timer.start();
        isRunning = true;
    }

    void resumeCountDown(){
        binding.timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h   = (int)(time /3600000);
                int m = (int)(time - h*3600000)/60000;
                int s= (int)(time - h*3600000- m*60000)/1000 ;
                String hh = h < 10 ? "0"+h: h+"";
                String mm = m < 10 ? "0"+m: m+"";
                String ss = s < 10 ? "0"+s: s+"";
                cArg.setText(hh+":"+mm+":"+ss);
                if (hh.equals(tour_hour) && mm.equals(tour_minutes) && ss.equals(tour_seconds)){
                    tour_counter++;
                    Log.i(TAG,"equal");
                    updateGuardTourStatusS();
                }
            }
        });
        binding.timer.setBase(SystemClock.elapsedRealtime() - timer);
        binding.timer.start();
    }

    void reset(HashMap<Integer, Integer> hashMap){
        resetTagCounter();
        list.putAll(hashMap);
        addToAdapter();
        binding.timer.stop();
        binding.timer.setBase(SystemClock.elapsedRealtime());
        binding.timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WriteModeOff();
        if (isRunning){
            binding.timer.stop();
            timer = SystemClock.elapsedRealtime() - binding.timer.getBase();
        }

    }

}