package ru.sberbank.lesson13.task.aidl.client;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class ExampleIntentService extends IntentService {
    public static final String ACTION_CHANGE_VALUE_FILTER = "ru.sberbank.lesson13.task.aidl.client.action.CHANGE_VALUE_FILTER";
    public static final String ACTION_CHANGE_VALUE = "ru.sberbank.lesson13.task.aidl.client.action.CHANGE_VALUE";
    public static final String EXTRA_PARAM_VALUE = "ru.sberbank.lesson13.task.aidl.client.extra.PARAM_VALUE";

    private LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);

    public ExampleIntentService() {
        super("ExampleIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CHANGE_VALUE.equals(action)) {
                for (int i = 1; i <= 15; i++) {
                    intent.setAction(ACTION_CHANGE_VALUE_FILTER);
                    localBroadcastManager.sendBroadcast(intent.putExtra(EXTRA_PARAM_VALUE, "Current value " + i));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
