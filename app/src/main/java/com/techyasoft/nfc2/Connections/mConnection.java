package com.techyasoft.nfc2.Connections;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;


import com.techyasoft.nfc2.Interfaces.mConnectionListener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class mConnection {
    final String TAG = mConnection.class.getSimpleName();
    Context context;
    mConnectionListener listener;
    private static String ip1 =  "10.26.69.90";        //"192.168.0.52"; //     //"192.168.100.129"
    private static String ip2 =  "192.168.100.129";        //"192.168.0.52"; //     //"192.168.100.129"
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "GHQ";
    private static String username1 =  "sa";   //"admin";  //"admin";
    private static String username2 =  "admin";   //"admin";  //"admin";
    private static String password1 =  "t35t5q1";; //"admin"; //"12345678"
    private static String password2 =  "admin";; //"admin"; //"12345678"
    private static String url;
    public static Connection connection = null;

    public mConnection(Context context, mConnectionListener listener) {
        this.context = context;
        this.listener = listener;
        start();
    }

    public void start(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new Thread(() -> {
            try {
                Class.forName(Classes);
                url = "jdbc:jtds:sqlserver://" + ip2 +":"+port+";"+"databaseName=" + database + ";user=" + username2 + ";password="+ password2 + ";";
                connection = DriverManager.getConnection(url);
                Log.i(TAG,"Success");
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnSuccess();
                    }
                });
            } catch (ClassNotFoundException e) {
                Log.i(TAG,e.getMessage());
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnFail(e.getMessage());
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
                Log.i(TAG,e.toString());
                ((Activity)context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnFail(e.getMessage());
                    }
                });
            }
        }).start();
    }
}
