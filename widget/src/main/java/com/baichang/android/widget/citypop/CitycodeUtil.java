package com.baichang.android.widget.cityPop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 城市代码
 *
 * @author zd
 */
class CityCodeUtil {

    private ArrayList<String> province_list = new ArrayList<String>();
    private ArrayList<String> city_list = new ArrayList<String>();
    private ArrayList<String> country_list = new ArrayList<String>();
    private ArrayList<String> province_list_code = new ArrayList<String>();
    private ArrayList<String> city_list_code = new ArrayList<String>();
    private ArrayList<String> country_list_code = new ArrayList<String>();
    /**
     * 单例
     */
    private static CityCodeUtil INSTANCE;

    private CityCodeUtil() {
    }

    public ArrayList<String> getProvince_list_code() {
        return province_list_code;
    }

    public ArrayList<String> getCity_list_code() {
        return city_list_code;
    }

    public void setCity_list_code(ArrayList<String> city_list_code) {
        this.city_list_code = city_list_code;
    }

    public ArrayList<String> getCountry_list_code() {
        return country_list_code;
    }

    public void setCountry_list_code(ArrayList<String> country_list_code) {
        this.country_list_code = country_list_code;
    }

    public void setProvince_list_code(ArrayList<String> province_list_code) {

        this.province_list_code = province_list_code;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static CityCodeUtil getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new CityCodeUtil();
        }
        return INSTANCE;
    }

    public ArrayList<String> getProvince(List<CityEntity> province) {
        if (province_list_code.size() > 0) {
            province_list_code.clear();
        }
        if (province_list.size() > 0) {
            province_list.clear();
        }
        for (int i = 0; i < province.size(); i++) {
            province_list.add(province.get(i).getCity_name());
            province_list_code.add(province.get(i).getId());
        }
        return province_list;

    }

    public ArrayList<String> getCity(
            HashMap<String, List<CityEntity>> cityHashMap, String provinceCode) {
        if (city_list_code.size() > 0) {
            city_list_code.clear();
        }
        if (city_list.size() > 0) {
            city_list.clear();
        }
        List<CityEntity> city = new ArrayList<CityEntity>();
        city = cityHashMap.get(provinceCode);
        for (int i = 0; i < city.size(); i++) {
            city_list.add(city.get(i).getCity_name());
            city_list_code.add(city.get(i).getId());
        }
        return city_list;

    }

    public ArrayList<String> getCountry(
            HashMap<String, List<CityEntity>> cityHashMap, String cityCode) {
        List<CityEntity> country = new ArrayList<CityEntity>();
        if (country_list_code.size() > 0) {
            country_list_code.clear();
        }
        if (country_list.size() > 0) {
            country_list.clear();
        }
        country = cityHashMap.get(cityCode);
        for (int i = 0; i < country.size(); i++) {
            country_list.add(country.get(i).getCity_name());
            country_list_code.add(country.get(i).getId());
        }
        return country_list;
    }
}
