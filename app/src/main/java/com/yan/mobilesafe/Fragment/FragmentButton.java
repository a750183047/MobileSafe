package com.yan.mobilesafe.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * FragmentButton
 * Created by neokree on 31/12/14.
 */
public class FragmentButton extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button button = new Button(this.getActivity());
        button.setText("哎呀！出了一些问题，刷新一下试试。。");
        button.setGravity(Gravity.CENTER);
        return button;

    }
}
