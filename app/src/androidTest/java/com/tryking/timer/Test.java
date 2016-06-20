package com.tryking.timer;

import android.test.AndroidTestCase;

import com.orhanobut.logger.Logger;
import com.tryking.timer.utils.CommonUtils;

/**
 * Created by Tryking on 2016/5/20.
 */
public class Test extends AndroidTestCase {
    public void testString() {
        float f1 = 2.4f;
        float f2 = 2.6f;
        float f3 = 2.4534f;
        String approximation1 = CommonUtils.getApproximation(f1);
        String approximation2 = CommonUtils.getApproximation(f2);
        String approximation3 = CommonUtils.getApproximation(f3);
        Logger.e(approximation1 + "::" + approximation2 + "::" + approximation3);
    }
}
