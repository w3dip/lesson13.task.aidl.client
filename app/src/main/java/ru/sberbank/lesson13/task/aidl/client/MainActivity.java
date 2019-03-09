package ru.sberbank.lesson13.task.aidl.client;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ru.sberbank.lesson13.task.aidl.contract.Data;
import ru.sberbank.lesson13.task.aidl.contract.IRemoteDataService;

public class MainActivity extends FragmentActivity implements FragmentB.OnFragmentInteractionListener {
    private IRemoteDataService remoteDataService;
    private ScheduledExecutorService scheduledThreadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction() {
        EditText editText = findViewById(R.id.myEditText);
        try {
            remoteDataService.write(new Data(editText.getText().toString()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            remoteDataService = IRemoteDataService.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, R.string.remote_service_connected, Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            Toast.makeText(MainActivity.this, R.string.remote_service_disconnected, Toast.LENGTH_SHORT).show();
            remoteDataService = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("ru.sberbank.lesson13.task.aidl.REMOTE_SERVICE_CALL");
        intent.setPackage("ru.sberbank.lesson13.task.aidl.service");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        scheduledThreadPool = Executors.newScheduledThreadPool(5);
        scheduledThreadPool.scheduleWithFixedDelay(() -> {
            try {
                Data data = remoteDataService.read();
                TextView logOutput = findViewById(R.id.logOutput);;
                logOutput.setText(data.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
        scheduledThreadPool.shutdown();
    }
}
