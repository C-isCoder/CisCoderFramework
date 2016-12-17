package com.baichang.library.test.common;

import android.os.Bundle;
import android.widget.TextView;

import com.baichang.android.widget.marqueeView.MarqueeView;
import com.baichang.library.test.R;
import com.baichang.library.test.base.CommonActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarqueeViewActivity extends CommonActivity {
    @BindView(R.id.marquee_view1)
    MarqueeView mMarquee1;
    @BindView(R.id.marquee_view2)
    MarqueeView mMarquee2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee_view);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //设置列表数据
        List<String> info = new ArrayList<>();
        info.add("1. 这是第一条公告");
        info.add("2. 这是第二条公告");
        info.add("3. 这是第三条公告");
        info.add("4. 这是第四条公告");
        info.add("5. 这是第五条公告");
        info.add("6. 这是第六条公告");
        mMarquee1.startWithList(info);
        //设置字符串数据
        String notice = "心中有阳光，脚底有力量！心中有阳光，脚底有力量！心中有阳光，脚底有力量！";
        mMarquee2.startWithText(notice);
        //设置事件监听
        mMarquee1.setOnItemClickListener((i, textView) -> {
            showMessage(textView.getText());
            showMessage(String.valueOf(i));
        });
    }
}
