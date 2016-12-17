package com.baichang.library.test.common;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baichang.android.utils.pinyin.Pinyin;
import com.baichang.android.widget.BCSideBar;
import com.baichang.library.test.R;
import com.baichang.library.test.adapter.CityAdapter;
import com.baichang.library.test.base.CommonActivity;
import com.baichang.library.test.model.CityData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SideViewActivity extends CommonActivity {
    private static final String[] CITY = new String[]{
            "上海", "北京", "香港", "武汉", "重庆", "天津", "广州", "深圳", "沈阳", "新北", "南京", "哈尔滨", "台北",
            "西安", "成都", "长春", "杭州", "济南", "大连", "太原", "郑州", "青岛", "石家庄", "昆明", "兰州", "高雄",
            "淄博", "长沙", "南昌", "乌鲁木齐", "贵阳", "鞍山", "唐山", "无锡", "吉林", "抚顺", "福州", "苏州", "包头",
            "齐齐哈尔", "徐州", "合肥", "台中", "邯郸", "洛阳", "南宁", "大同", "汕头", "烟台", "大庆", "淮南", "本溪",
            "常州", "呼和浩特", "柳州", "宁波", "伊春", "商丘", "台南", "鸡西", "淮安", "枣庄", "阜新", "锦州", "潍坊",
            "张家口", "牡丹江", "平顶山", "西宁", "湛江", "保定", "宜昌", "襄樊", "新竹", "桃园", "基隆", "嘉义"
    };

    @BindView(R.id.recycler_view)
    RecyclerView rvCity;
    @BindView(R.id.side_bar)
    BCSideBar mSideBar;

    private CityAdapter mAdapter;
    private List<CityData> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_view);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        for (String city : CITY) {
            cityList.add(new CityData("", city, Pinyin.toPinyin(city.charAt(0)).substring(0, 1)));
            Collections.sort(cityList);
        }
    }

    private void initView() {
        mAdapter = new CityAdapter(this, cityList);
        mAdapter.setOnItemClickListener(position -> {
            showMessage(cityList.get(position).name);
        });
        rvCity.setLayoutManager(new LinearLayoutManager(this));
        rvCity.setAdapter(mAdapter);
        mSideBar.setOnTouchingLetterChangedListener(s -> {
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).index.equalsIgnoreCase(s)) {
                    ((LinearLayoutManager) rvCity.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        });
    }
}
