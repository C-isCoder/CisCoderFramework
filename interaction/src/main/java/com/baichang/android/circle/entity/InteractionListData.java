package com.baichang.android.circle.entity;

import com.baichang.android.circle.common.InteractionConfig;
import com.baichang.android.config.ConfigurationImpl;
import com.baichang.android.utils.BCDensityUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by iCong on 2017/3/20.
 */

public class InteractionListData implements Serializable {

    @Expose @SerializedName("trendsTitle") public String title;

    @Expose @SerializedName("trendsId") public int id;

    @Expose @SerializedName("hostName") public String name;

    @Expose @SerializedName("hostIcon") public String avatar;

    @Expose @SerializedName("releaseTime") public String time;

    @Expose @SerializedName("trendsContent") public String content;

    @Expose @SerializedName("trendsImages") public List<String> images;

    @Expose @SerializedName("praiseNum") public int praiseCount;

    @Expose @SerializedName("commentNum") public int commentCount;

    @Expose @SerializedName("trendsVideo") public String video;

    @Expose public String videoPic;
    @Expose public int isPraise;
    @Expose public int isCollect;

    @Expose public String hostUserId;

    @Expose public String type;

    public boolean isShowAll = true;

    public boolean isMax6() {
        return content.length() > getDisplayNumber();
    }

    public int getDisplayNumber() {
        int width = InteractionConfig.getInstance().getDisplayDisplay();
        if (width == 0) {
            return 30 * 6;
        } else {
            // ((屏幕宽度[px] - padding[dp] * 2)/(一个字占用的px[sp])) * 6 行
            return ((width - BCDensityUtil.dip2px(ConfigurationImpl.get().getAppContext(), 10) * 2)
                    / BCDensityUtil.sp2px(ConfigurationImpl.get().getAppContext(), 13)) * 6;
        }
    }
}
