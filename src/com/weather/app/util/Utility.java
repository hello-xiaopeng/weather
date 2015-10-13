package com.weather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
	
	/**
	 * �������������ص�json���ݣ����������������ݴ洢������
	 */
	public static void handleWeatherResponse(Context context,String response){
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weaterInfo = jsonObject.getJSONObject("weatherinfo");
			
			String cityName = weaterInfo.getString("city");
			String weaterCode = weaterInfo.getString("cityid");
			String temp1 = weaterInfo.getString("temp1");
			String temp2 = weaterInfo.getString("temp2");
			String weatherDesp = weaterInfo.getString("weather");
			String weatherTime = weaterInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weaterCode,temp1,temp2,weatherDesp,weatherTime);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * �����������ص�����������Ϣ�洢��SharePreferences�ļ���
	 * @param context
	 * @param cityName
	 * @param weaterCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param weatherTime
	 */
	private static void saveWeatherInfo(Context context, String cityName,
			String weaterCode, String temp1, String temp2, String weatherDesp,
			String weatherTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("cityName", cityName);
		editor.putString("weaterCode", weaterCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weatherDesp", weatherDesp);
		editor.putString("weatherTime", weatherTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
