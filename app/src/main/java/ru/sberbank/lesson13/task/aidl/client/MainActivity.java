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

import ru.sberbank.lesson13.task.aidl.contract.Data;
import ru.sberbank.lesson13.task.aidl.contract.IRemoteDataService;

public class MainActivity extends FragmentActivity implements FragmentB.OnFragmentInteractionListener {
    private IRemoteDataService remoteDataService;
    //private static final String SERVICE_IS_STARTED = "isStarted";

    private EditText editTextView;

    //private boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextView = findViewById(R.id.myEditText);
        /*if (savedInstanceState == null) {
            //startService(new Intent(ACTION_CHANGE_VALUE, null, MainActivity.this,
            //        ExampleIntentService.class));
            //isStarted = true;
        }*/
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SERVICE_IS_STARTED, isStarted);
        super.onSaveInstanceState(outState);
    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager.registerReceiver(exampleBroadcastReceiver, intentFilter);
    }*/

    /*@Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(exampleBroadcastReceiver);
    }*/

    @Override
    public void onFragmentInteraction() {
        EditText editText = findViewById(R.id.myEditText);
        try {
            remoteDataService.write(new Data(editText.getText().toString()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        /*Fragment fragment = FragmentC.newInstance(editText.getText().toString());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.textViewContainer, fragment);
        fragmentTransaction.commit();*/
    }

    /*public class EditViewCallbackImpl implements EditViewCallback {
        @Override
        public void onValueChanged(String value) {
            editTextView.setText(value);
        }
    }*/

    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            remoteDataService = IRemoteDataService.Stub.asInterface(service);
            Toast.makeText(MainActivity.this, R.string.remote_service_connected, Toast.LENGTH_SHORT).show();
            try {
                Data data = remoteDataService.read();
                TextView logOutput = findViewById(R.id.logOutput);;
                logOutput.append(data.getValue());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            /*mService = new Messenger(service);
            mCallbackText.append("Attached\n");
            try {
                Message msg = Message.obtain(null,
                        RemoteService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);

                msg = Message.obtain(null,
                        RemoteService.MSG_SET_VALUE, 0, 0);
                Bundle data = new Bundle();
                data.putString(MSG_SET_VALUE_FIELD, getResources().getString(R.string.hello_from_activity));
                msg.setData(data);
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Toast.makeText(AdditionalActivity.this, R.string.example_service_connected,
                    Toast.LENGTH_SHORT).show();*/
        }

        public void onServiceDisconnected(ComponentName className) {
            remoteDataService = null;
            //mCallbackText.append("Disconnected\n");
            Toast.makeText(MainActivity.this, R.string.remote_service_disconnected, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("ru.sberbank.lesson13.task.aidl.REMOTE_SERVICE_CALL");
        intent.setPackage("ru.sberbank.lesson13.task.aidl.service");
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
    }
}
