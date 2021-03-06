package com.weather.app.activity;

import java.security.PublicKey;

import com.weather.app.R;
import com.weather.app.service.AutoUpdateService;
import com.weather.app.util.HttpCallbackListener;
import com.weather.app.util.HttpUtil;
import com.weather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener {
	private LinearLayout weatherInfoLayout;
	//用于显示城市名
	private TextView cityNameText;
	//用于显示发布时间
	private TextView pushlishText;
	//用于显示天气描述信息
	private TextView weatherDespText;
	//用于显示气温1
	private TextView temp1Text;
	//用于显示气温2
	private TextView temp2Text;
	//用于显示当前时间
	private TextView currentDateText;
	//切换城市按钮
	private Button switchCity;
	//更新天气按钮 
	private Button efreshWeather;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		pushlishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weater_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			System.out.println("县级代号查询天气"+countyCode);
			//有县级代号时就去查询天气
			pushlishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			System.out.println("显示本地天气");
			//没有县级代号时就直接显示本地天气
			shouWeather();
		}
		switchCity = (Button) findViewById(R.id.switch_city);
		efreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		efreshWeather.setOnClickListener(this);
	}
	/**
	 * 查询县级代号所对应的天气代号
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		System.out.println("查询县级代号所对应的天气代号"+address);
		queryFromServer(address,"countyCode");
	}
	/**
	 * 根据传入的地址和类型去想服务器查询天气代号或者天气信息
	 * @param address
	 * @param countyCode
	 */
	private void queryFromServer(String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//从服务器返回的数据中解析出天气代号
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					//处理服务器返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							shouWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
					pushlishText.setText("同步失败");
					}
				});
			
			}
		});
		
	}
	/**
	 *查询天气代号所对应的天气信息
	 * @param weatherCode
	 */
	protected void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		System.out.println("查询天气代号所对应的天气信息"+address);
		queryFromServer(address, "weatherCode");
	}
	/**
	 * 从sp文件中读取存储的天气信息，并显示界面上
	 */
	private void shouWeather() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(sp.getString("cityName", ""));
		temp1Text.setText(sp.getString("temp1", ""));
		temp2Text.setText(sp.getString("temp2", ""));
		weatherDespText.setText(sp.getString("weatherDesp", ""));
		pushlishText.setText("今天" + sp.getString("weatherTime", "") + "发布");
		currentDateText.setText(sp.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
		//激活服务
		Intent intent = new Intent(this,AutoUpdateService.class);
		startService(intent);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this,ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			pushlishText.setText("同步中...");
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = sp.getString("weaterCode", "");
			System.out.println("weaterCode"+weatherCode);
			if(!TextUtils.isEmpty(weatherCode)){
				queryWeatherInfo(weatherCode);
			}
			break;
		}
	}
}
