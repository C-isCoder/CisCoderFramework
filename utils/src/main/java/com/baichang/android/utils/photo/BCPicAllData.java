package com.baichang.android.utils.photo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcello on 2015/5/16.
 */
public class BCPicAllData implements Serializable{

    public int position;
    public List<BCPicData> pics;

    public BCPicAllData() {
        pics = new ArrayList<>();
    }
}
