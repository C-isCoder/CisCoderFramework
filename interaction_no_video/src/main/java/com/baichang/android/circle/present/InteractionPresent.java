package com.baichang.android.circle.present;

import android.content.Context;
import android.view.View;
import com.baichang.android.common.IBasePresent;

/**
 * Created by iCong on 2017/3/20.
 */

public interface InteractionPresent extends IBasePresent {

    void attachView(View contentView);

    void jumpMe(Context context);
}
