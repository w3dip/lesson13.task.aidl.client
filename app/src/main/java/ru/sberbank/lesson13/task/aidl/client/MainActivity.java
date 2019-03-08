package ru.sberbank.lesson13.task.aidl.client;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.EditText;

import static ru.sberbank.lesson13.task.aidl.client.ExampleIntentService.ACTION_CHANGE_VALUE;
import static ru.sberbank.lesson13.task.aidl.client.ExampleIntentService.ACTION_CHANGE_VALUE_FILTER;

public class MainActivity extends FragmentActivity implements FragmentB.OnFragmentInteractionListener {
    private static final String SERVICE_IS_STARTED = "isStarted";

    private EditText editTextView;

    private LocalBroadcastManager localBroadcastManager;
    private ExampleBroadcastReceiver exampleBroadcastReceiver;
    private IntentFilter intentFilter;

    private boolean isStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextView = findViewById(R.id.myEditText);
        intentFilter = new IntentFilter(ACTION_CHANGE_VALUE_FILTER);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        exampleBroadcastReceiver = new ExampleBroadcastReceiver(new EditViewCallbackImpl());

        if (savedInstanceState == null) {
            startService(new Intent(ACTION_CHANGE_VALUE, null, MainActivity.this,
                    ExampleIntentService.class));
            isStarted = true;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SERVICE_IS_STARTED, isStarted);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager.registerReceiver(exampleBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(exampleBroadcastReceiver);
    }

    @Override
    public void onFragmentInteraction() {
        EditText editText = findViewById(R.id.myEditText);
        Fragment fragment = FragmentC.newInstance(editText.getText().toString());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.textViewContainer, fragment);
        fragmentTransaction.commit();
    }

    public class EditViewCallbackImpl implements EditViewCallback {
        @Override
        public void onValueChanged(String value) {
            editTextView.setText(value);
        }
    }
}
