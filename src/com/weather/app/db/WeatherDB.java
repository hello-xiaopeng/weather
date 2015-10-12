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
	 * 数据库名
	 */
	public static final String DB_ANME="weather";
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	
	private static WeatherDB weatherDB;
	private SQLiteDatabase db;
	/**
	 * 将构造方法私有化
	 */
	private WeatherDB(Context context){
		WeatherOpenHeleper heleper = new WeatherOpenHeleper(context, DB_ANME, null, VERSION);
		db = heleper.getWritableDatabase();
	}
	/**
	 * 获取WeatherDB的实例 (synchronized 可以防止多个线程同时访问)
	 */
	public synchronized static WeatherDB getInstance(Context context){
		if(weatherDB  == null){
			weatherDB = new WeatherDB(context);
		}
		return weatherDB;
	}
	/**
	 * 讲Province实例存储到数据库
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
	 * 从数据库读取全国所有省份的信息
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
	 * 讲City实例存到数据库
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
	 * 从数据库读取某省下所有的城市信息
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
	 * 将County实例存到数据库
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
	 * 从数据库读取某城市所有的县信息
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
