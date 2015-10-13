package com.weather.app.util;

import android.text.TextUtils;

import com.weather.app.db.WeatherDB;
import com.weather.app.model.City;
import com.weather.app.model.County;
import com.weather.app.model.Province;

public class Utility {

	/**
	 * 解析和处理服务器返回的省级数据(“代号|城市，代号|城市“)   并保存到数据库
	 */
	public synchronized static boolean handleProvincesResponse(WeatherDB db,
			String response) {
		if (!TextUtils.isEmpty(response)) {
			// 分割全部省的数据
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					// 分割一个省的数据
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// 将解析出来的数据存储到Province表
					db.saveProvince(province);
				}
				return true;
			}
		}

		return false;

	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public synchronized static boolean handleCityResponse(WeatherDB db,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCitys = response.split(",");
			if (allCitys != null && allCitys.length > 0) {
				for (String c : allCitys) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					//将解析出来的数据存储到city表
					db.saveCity(city);
				}
				return true;
			}
		}
		return false;

	}
	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public synchronized static boolean handleCountiesResponse(WeatherDB db,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length>0){
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					//将解析出来的数据存储到County表
					db.saveCounty(county);
				}
			}
			return true;
		}
		return false;
		
	}
}
