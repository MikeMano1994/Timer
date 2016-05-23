package com.tryking.timer;

import android.test.AndroidTestCase;

import com.orhanobut.logger.Logger;
import com.tryking.timer.utils.CommonUtils;

/**
 * Created by Tryking on 2016/5/20.
 */
public class Test extends AndroidTestCase {
    public void testString() {
        String s1= "12";
        String s = CommonUtils.deleteStr(s1, "12");
        Logger.e(s);
    }
}
