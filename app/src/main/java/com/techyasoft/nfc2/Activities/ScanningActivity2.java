package com.techyasoft.nfc2.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import android.widget.Chronometer;
import android.widget.Toast;

import com.techyasoft.nfc2.Adapters.detailAdapterUser;
import com.techyasoft.nfc2.Adapters.detailAdapterUser2;
import com.techyasoft.nfc2.Dialogs.InputText;
import com.techyasoft.nfc2.Interfaces.CheckCardNumerQueryListener;
import com.techyasoft.nfc2.Interfaces.GetTourQueryListener;
import com.techyasoft.nfc2.Interfaces.GuardDetailQueryListener;
import com.techyasoft.nfc2.Interfaces.GuardTourQueryListener;
import com.techyasoft.nfc2.Interfaces.InputTextListener;
import com.techyasoft.nfc2.Interfaces.QueryListener;
import com.techyasoft.nfc2.Queries.AddHistoryQuery;
import com.techyasoft.nfc2.Queries.AddTourQuery;
import com.techyasoft.nfc2.Queries.CheckCardNumberQuery;
import com.techyasoft.nfc2.Queries.GetTourQuery;
import com.techyasoft.nfc2.Queries.GuardDetailGetQuery;
import com.techyasoft.nfc2.Queries.GuardDetailQuery;
import com.techyasoft.nfc2.Queries.GuardTourQuery;
import com.techyasoft.nfc2.R;
import com.techyasoft.nfc2.Utils.Helper;
import com.techyasoft.nfc2.databinding.ActivityScanning2Binding;
import com.techyasoft.nfc2.model.CardData;
import com.techyasoft.nfc2.model.GuardDetail;
import com.techyasoft.nfc2.model.GuardTour;
import com.techyasoft.nfc2.model.History;
import com.techyasoft.nfc2.model.Profile;
import com.techyasoft.nfc2.model.Tour;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ScanningActivity2 extends AppCompatActivity {

    final String TAG = ScanningActivity2.class.getSimpleName();
    ActivityScanning2Binding binding;

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
    detailAdapterUser2 adapterUser;
    List<String> cardList = new ArrayList<>();
    HashMap<Integer,List<String>> listHashMap = new HashMap<>();
    boolean isRunning;
    long timer;
    public static int TOTAL_CONTAINERS = 0;
    List<Tour> list = new ArrayList<>();
    String comment = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanning2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        hideUi();
        context=this;
        mProfile= Helper.getProfileInfo(this);

        init();


        sdf = new SimpleDateFormat("yyyy-MM-dd");

        binding.recycler.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recycler.setLayoutManager(linearLayoutManager);
        adapterUser = new detailAdapterUser2(this,list);
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
            showDialog(hexdump);

            Parcelable[] rawMsgs=intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs=null;
            if (rawMsgs !=null){
                msgs=new NdefMessage[rawMsgs.length];
                for (int i=0;i<rawMsgs.length;i++){
                    msgs[i]=(NdefMessage) rawMsgs[i];

                }

            }
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
        Log.i(TAG,"start");
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
        Log.i(TAG,"Resume");
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
        GetTourQuery query = new GetTourQuery(new GetTourQueryListener() {
            @Override
            public void OnQuerySuccess(List<Tour> tours) {
                Log.i(TAG,tours.size()+ " list size");
                tour_counter = tours.size();
                list.addAll(tours);
            }

            @Override
            public void onNotFound() {
                Log.i(TAG,"Not found");
            }

            @Override
            public void OnQueryFail(String msg) {
                Log.i(TAG,msg);
            }
        },this,profile.getId(),sdf.format(new Date()));


        new Thread(new Runnable() {
            @Override
            public void run() {
                query.executeQuery();
            }
        }).start();
    }

    void updateTagCounter(String cardNumber){
        History history = new History();
        history.setGuard_id(profile.getId());
        history.setCard_number(cardNumber);
        history.setDate(sdf.format(new Date()));
        history.setComment(comment);
        history.setTour_counter(tour_counter+"");
        tag_counter++;
        Log.i(TAG,tag_counter+" tag values");
        AddHistoryQuery query = new AddHistoryQuery(new QueryListener() {
            @Override
            public void OnQuerySuccess() {
                Log.i(TAG,"Add History Success");
            }

            @Override
            public void OnQueryFail(String msg) {
                Log.i(TAG,"Add History query :"+msg);
            }
        },this,history);


        new Thread(new Runnable() {
            @Override
            public void run() {
                query.executeQuery();
            }
        }).start();
    }

    void resetTagCounter(){
        tag_counter = 0;
    }

    void updateGuardTourStatusS(){
        tour_counter++;
        Tour tour = new Tour();
        tour.setGuard_id(profile.getId());
        tour.setTour_number(tour_counter+"");
        tour.setTotal_swipes(tag_counter+"");
        tour.setDate(sdf.format(new Date()));
        tour.setTour_counter(99+"");
        reset(tour);
        AddTourQuery query = new AddTourQuery(this, tour, new QueryListener() {
            @Override
            public void OnQuerySuccess() {
                Log.i(TAG,"Success Add tour");
            }

            @Override
            public void OnQueryFail(String msg) {
                Log.i(TAG,"Fail Add tour");
                Log.i(TAG,msg);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                query.executeQuery();
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
                if (hh.equals(tour_hour) && mm.equals(tour_minutes) && ss.equals(tour_seconds)){
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
                    Log.i(TAG,"equal");
                    updateGuardTourStatusS();
                }
            }
        });
        binding.timer.setBase(SystemClock.elapsedRealtime() - timer);
        binding.timer.start();
    }

    void reset(Tour tour){
        resetTagCounter();
        list.add(tour);
        addToAdapter();
        binding.timer.stop();
        binding.timer.setBase(SystemClock.elapsedRealtime());
        binding.timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        WriteModeOff();
        Log.i(TAG,"Pause");
        if (isRunning){
            binding.timer.stop();
            timer = SystemClock.elapsedRealtime() - binding.timer.getBase();
        }

    }

    void showDialog(String cardNumber){
        if (isRunning){
            binding.timer.stop();
            timer = SystemClock.elapsedRealtime() - binding.timer.getBase();
        }
        InputText dialog = new InputText();
        dialog.setListener(new InputTextListener() {
            @Override
            public void onInputText(String text) {
                comment = text;
                updateTagCounter(cardNumber);
                resumeCountDown();
            }
        });
        dialog.show(getSupportFragmentManager(),"TAG");
    }
}