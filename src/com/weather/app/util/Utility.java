package com.weather.app.util;

import android.text.TextUtils;

import com.weather.app.db.WeatherDB;
import com.weather.app.model.City;
import com.weather.app.model.County;
import com.weather.app.model.Province;

public class Utility {

	/**
	 * �����ʹ�����������ص�ʡ������(������|���У�����|���С�)   �����浽���ݿ�
	 */
	public synchronized static boolean handleProvincesResponse(WeatherDB db,
			String response) {
		if (!TextUtils.isEmpty(response)) {
			// �ָ�ȫ��ʡ������
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					// �ָ�һ��ʡ������
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// ���������������ݴ洢��Province��
					db.saveProvince(province);
				}
				return true;
			}
		}

		return false;

	}

	/**
	 * �����ʹ�����������ص��м�����
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
					//���������������ݴ洢��city��
					db.saveCity(city);
				}
				return true;
			}
		}
		return false;

	}
	/**
	 * �����ʹ�����������ص��ؼ�����
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
					//���������������ݴ洢��County��
					db.saveCounty(county);
				}
			}
			return true;
		}
		return false;
		
	}
}
