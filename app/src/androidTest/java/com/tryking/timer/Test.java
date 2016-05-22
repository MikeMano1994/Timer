package com.tryking.timer;

import android.test.AndroidTestCase;

import com.orhanobut.logger.Logger;
import com.tryking.timer.utils.CommonUtils;

/**
 * Created by Tryking on 2016/5/20.
 */
public class Test extends AndroidTestCase {
    public void testString() {
        int i = 5;
        String s = CommonUtils.intToStr(i);
        Logger.e(s);
    }
}
