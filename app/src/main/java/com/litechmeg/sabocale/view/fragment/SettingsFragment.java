package com.litechmeg.sabocale.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.litechmeg.sabocale.R;

/**
 * Created by megukanipan on 2015/04/23.
 */
public class SettingsFragment extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 第３引数のbooleanは"container"にreturnするViewを追加するかどうか
        //trueにすると最終的なlayoutに再度、同じView groupが表示されてしまうのでfalseでOKらしい
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}
