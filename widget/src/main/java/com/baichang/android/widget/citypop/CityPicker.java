package com.baichang.android.widget.cityPop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;


import com.baichang.android.config.ConfigurationImpl;
import com.baichang.android.utils.BCFileUtil;
import com.baichang.android.widget.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;


/**
 * 城市Picker
 *
 * @author zd
 */
public class CityPicker extends LinearLayout {
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker countryPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCountryIndex = -1;
    private Context mContext;
    private List<CityEntity> province_list = new ArrayList<CityEntity>();
    private HashMap<String, List<CityEntity>> city_map = new HashMap<String, List<CityEntity>>();
    private HashMap<String, List<CityEntity>> country_map = new HashMap<String, List<CityEntity>>();

    private CityCodeUtil citycodeUtil;
    private String city_code_string;
    private String city_string;
    /**
     * 设置线的颜色
     */
    private int mColor;

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public CityPicker(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        if (isInEditMode()) return;
        getAddressInfo();
    }

    // 获取城市信息
    private void getAddressInfo() {
        // 读取城市信息string
        JSONParser parser = new JSONParser();
        String area_str = BCFileUtil.readAssets(mContext, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");
        city_map = parser.getJSONParserResultArray(area_str, "area1");
        country_map = parser.getJSONParserResultArray(area_str, "area2");
    }

    public static class JSONParser {
        private ArrayList<String> province_list_code = new ArrayList<String>();
        private ArrayList<String> city_list_code = new ArrayList<String>();

        private List<CityEntity> getJSONParserResult(String JSONString, String key) {
            List<CityEntity> list = new ArrayList<CityEntity>();
            JsonObject result = new JsonParser().parse(JSONString).getAsJsonObject().getAsJsonObject(key);

            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator.next();
                CityEntity cityInfo = new CityEntity();
                cityInfo.setCity_name(entry.getValue().getAsString());
                cityInfo.setId(entry.getKey());
                province_list_code.add(entry.getKey());
                list.add(cityInfo);
            }
            System.out.println(province_list_code.size());
            return list;
        }

        private HashMap<String, List<CityEntity>> getJSONParserResultArray(
                String JSONString, String key) {
            HashMap<String, List<CityEntity>> hashMap = new HashMap<String, List<CityEntity>>();
            JsonObject result = new JsonParser().parse(JSONString).getAsJsonObject().getAsJsonObject(key);
            Iterator iterator = result.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator.next();
                List<CityEntity> list = new ArrayList<CityEntity>();
                JsonArray array = entry.getValue().getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {
                    CityEntity cityInfo = new CityEntity();
                    cityInfo.setCity_name(array.get(i).getAsJsonArray().get(0).getAsString());
                    cityInfo.setId(array.get(i).getAsJsonArray().get(1).getAsString());
                    city_list_code.add(array.get(i).getAsJsonArray().get(1).getAsString());
                    list.add(cityInfo);
                }
                hashMap.put(entry.getKey(), list);
            }
            return hashMap;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.city_select_city_picker, this);
        citycodeUtil = CityCodeUtil.getInstance();
        initView();
    }

    private void initView() {
        // 获取控件引用
        provincePicker = (ScrollerNumberPicker) findViewById(R.id.city_picker_province);
        cityPicker = (ScrollerNumberPicker) findViewById(R.id.city_picker_city);
        countryPicker = (ScrollerNumberPicker) findViewById(R.id.city_picker_country);

        provincePicker.setLineColor(mColor == 0 ? ConfigurationImpl.get().getAppBarColor() : mColor);
        provincePicker.setData(citycodeUtil.getProvince(province_list));
        provincePicker.setDefault(0);

        cityPicker.setLineColor(mColor == 0 ? ConfigurationImpl.get().getAppBarColor() : mColor);
        cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(0)));
        cityPicker.setDefault(1);

        countryPicker.setLineColor(mColor == 0 ? ConfigurationImpl.get().getAppBarColor() : mColor);
        countryPicker.setData(citycodeUtil.getCountry(country_map, citycodeUtil.getCity_list_code().get(1)));
        countryPicker.setDefault(1);

        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (TextUtils.isEmpty(text)) return;
                if (tempProvinceIndex != id) {
                    String selectPicker = cityPicker.getSelectedText();
                    if (TextUtils.isEmpty(selectPicker)) return;
                    String selectCountry = countryPicker.getSelectedText();
                    if (TextUtils.isEmpty(selectCountry)) return;
                    // 城市数组
                    cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil.getProvince_list_code().get(id)));
                    cityPicker.setDefault(1);
                    countryPicker.setData(citycodeUtil.getCountry(country_map, citycodeUtil.getCity_list_code().get(1)));
                    countryPicker.setDefault(1);
                    int last = provincePicker.getListSize();
                    if (id > last) {
                        provincePicker.setDefault(last - 1);
                    }
                }
                tempProvinceIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (TextUtils.isEmpty(text)) return;
                if (temCityIndex != id) {
                    String selectProvince = provincePicker.getSelectedText();
                    if (TextUtils.isEmpty(selectProvince)) return;
                    String selectCountry = countryPicker.getSelectedText();
                    if (TextUtils.isEmpty(selectCountry)) return;
                    countryPicker.setData(citycodeUtil.getCountry(country_map, citycodeUtil.getCity_list_code().get(id)));
                    countryPicker.setDefault(1);
                    int last = cityPicker.getListSize();
                    if (id > last) {
                        cityPicker.setDefault(last - 1);
                    }
                }
                temCityIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        countryPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (TextUtils.isEmpty(text)) return;
                if (tempCountryIndex != id) {
                    String selectProvince = provincePicker.getSelectedText();
                    if (TextUtils.isEmpty(selectProvince)) return;
                    String selectCity = cityPicker.getSelectedText();
                    if (TextUtils.isEmpty(selectCity)) return;
                    // 城市数组
                    city_code_string = citycodeUtil.getCountry_list_code().get(id);
                    int last = countryPicker.getListSize();
                    if (id > last) {
                        countryPicker.setDefault(last - 1);
                    }
                }
                tempCountryIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        return city_code_string;
    }

    public String getCity_string() {
        if (cityPicker.getSelectedText().equalsIgnoreCase("市辖区") || cityPicker.getSelectedText().equalsIgnoreCase("县")) {
            city_string = provincePicker.getSelectedText() + " "
                    + countryPicker.getSelectedText();
        } else {
            city_string = provincePicker.getSelectedText() + " "
                    + cityPicker.getSelectedText() + " " + countryPicker.getSelectedText();
        }
        return city_string;
    }

    public interface OnSelectingListener {
        void selected(boolean selected);
    }

    public void setLineColor(int colorRes) {
        mColor = colorRes;
        initView();
    }
}
