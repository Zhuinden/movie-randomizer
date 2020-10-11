package com.zhuinden.movierandomizerclient.utils.navigation;

import android.support.v4.app.Fragment;

/**
 * Created by Zhuinden on 2017.12.28..
 */

public class BaseFragment extends Fragment {
    public final <T extends BaseKey> T getKey() {
        return getArguments() != null ? getArguments().<T>getParcelable("KEY") : null;
    }
}
