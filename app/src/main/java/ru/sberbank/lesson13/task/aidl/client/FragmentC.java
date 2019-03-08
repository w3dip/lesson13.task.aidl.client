package ru.sberbank.lesson13.task.aidl.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentC extends Fragment {
    private static final String MY_TEXT = "myText";

    private String myText;

    public static FragmentC newInstance(String text) {
        FragmentC fragment = new FragmentC();
        Bundle args = new Bundle();
        args.putString(MY_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myText = getArguments().getString(MY_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c, container, false);
        TextView textView = view.findViewById(R.id.myTextView);
        textView.setText(myText);
        return view;
    }

}
