package com.weather.app.activity;

import java.security.PublicKey;

import com.weather.app.R;
import com.weather.app.util.HttpCallbackListener;
import com.weather.app.util.HttpUtil;
import com.weather.app.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {
	private LinearLayout weatherInfoLayout;
	//������ʾ������
	private TextView cityNameText;
	//������ʾ����ʱ��
	private TextView pushlishText;
	//������ʾ����������Ϣ
	private TextView weatherDespText;
	//������ʾ����1
	private TextView temp1Text;
	//������ʾ����2
	private TextView temp2Text;
	//������ʾ��ǰʱ��
	private TextView currentDateText;
	//�л����а�ť
	private Button switchCity;
	//����������ť 
	private Button efreshWeather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		pushlishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weater_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){
			System.out.println("�ؼ����Ų�ѯ����"+countyCode);
			//���ؼ�����ʱ��ȥ��ѯ����
			pushlishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			System.out.println("��ʾ��������");
			//û���ؼ�����ʱ��ֱ����ʾ��������
			shouWeather();
		}
	}
	/**
	 * ��ѯ�ؼ���������Ӧ����������
	 * @param countyCode
	 */
	private void queryWeatherCode(String countyCode) {
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		System.out.println(address);
		queryFromServer(address,"countyCode");
	}
	/**
	 * ���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ
	 * @param address
	 * @param countyCode
	 */
	private void queryFromServer(String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//�ӷ��������ص������н�������������
						String[] array = response.split("\\|");
						if(array != null && array.length == 2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					//������������ص�������Ϣ
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
					pushlishText.setText("ͬ��ʧ��");
					}
				});
			
			}
		});
		
	}
	/**
	 *��ѯ������������Ӧ��������Ϣ
	 * @param weatherCode
	 */
	protected void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address, "weatherCode");
	}
	/**
	 * ��sp�ļ��ж�ȡ�洢��������Ϣ������ʾ������
	 */
	private void shouWeather() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(sp.getString("cityName", ""));
		temp1Text.setText(sp.getString("temp1", ""));
		temp2Text.setText(sp.getString("temp2", ""));
		weatherDespText.setText(sp.getString("weatherDesp", ""));
		pushlishText.setText("����" + sp.getString("weatherTime", "") + "����");
		currentDateText.setText(sp.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}
}
