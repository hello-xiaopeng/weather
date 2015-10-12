package com.weather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weather.app.model.City;
import com.weather.app.model.County;
import com.weather.app.model.Province;

public class WeatherDB {
	/**
	 * ���ݿ���
	 */
	public static final String DB_ANME="weather";
	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	
	private static WeatherDB weatherDB;
	private SQLiteDatabase db;
	/**
	 * �����췽��˽�л�
	 */
	private WeatherDB(Context context){
		WeatherOpenHeleper heleper = new WeatherOpenHeleper(context, DB_ANME, null, VERSION);
		db = heleper.getWritableDatabase();
	}
	/**
	 * ��ȡWeatherDB��ʵ�� (synchronized ���Է�ֹ����߳�ͬʱ����)
	 */
	public synchronized static WeatherDB getInstance(Context context){
		if(weatherDB  == null){
			weatherDB = new WeatherDB(context);
		}
		return weatherDB;
	}
	/**
	 * ��Provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("province_name", province.getProvinceName());
			contentValues.put("province_code", province.getProvinceCode());
			db.insert("Province", null, contentValues);
		}
	}
	/**
	 * �����ݿ��ȡȫ������ʡ�ݵ���Ϣ
	 */
	public List<Province> loadProvinces(){
		List<Province> provinces = new ArrayList<Province>();
		Cursor cursor = db.query( "Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				provinces.add(province);
				
			}while(cursor.moveToNext());
		}
		
		
		return provinces;
		
	}

	/**
	 * ��Cityʵ���浽���ݿ�
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("city_name", city.getCityName());
			contentValues.put("city_code", city.getCityCode());
			contentValues.put("province_id", city.getProvinceId());
			db.insert("City", null, contentValues);
			
		}
	}
	/**
	 * �����ݿ��ȡĳʡ�����еĳ�����Ϣ
	 */
	public List<City> loadCities(int provinceId){
		List<City> citys = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				citys.add(city);
			} while (cursor.moveToNext());
		}
		return citys;
		
	}
	/**
	 * ��Countyʵ���浽���ݿ�
	 */
	public void saveCounty(County county){
		if(county !=null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("county_name", county.getCountyName());
			contentValues.put("county_code", county.getCountyCode());
			contentValues.put("city_id", county.getCityId());
			db.insert("County", null, contentValues);
		}
		
	}
	/**
	 * �����ݿ��ȡĳ�������е�����Ϣ
	 */
	public List<County> loadCounties(int cityId){
		List<County> counties = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null,null,null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				counties.add(county);
			} while (cursor.moveToNext());
		}
		return counties;
	}
}
