package com.baichang.android.circle.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iCong on 2017/3/21.
 */

public class InteractionCommentList {

    @Expose public String userId;
    @Expose @SerializedName("commentIcon") public String avatar;
    @Expose @SerializedName("commentName") public String name;
    @Expose
    //@SerializedName("commentTime")
    @SerializedName("created") public String time;
    @Expose @SerializedName("commentContent") public String content;
    @Expose @SerializedName("commentList") public List<InteractionCommentReplyList> comments =
            new ArrayList<>();

    public boolean isNullComment() {
        return comments == null || comments.isEmpty();
    }

    public boolean isShowMore() {
        return comments != null && comments.size() > 2;
    }

    @Expose public int trendsId;

    @Expose public String id;
}
