package com.example.moneybook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Exchange extends Activity {
	//각나라 화폐는 상수로 미리 정의 알아보기 쉽게!
	final int KRW = 0, USD = 1, JPY = 2, EUR = 3, CNY = 4, AUD = 5, CAD = 6, NZD = 7;
	Spinner nationChoice1, nationChoice2;
	ArrayAdapter<CharSequence> adspin;
	double USDrate, JPYrate, EURrate, CNYrate;
	double AUDrate, CADrate, NZDrate;
	int nation1, nation2;
	EditText exchangeInput;
	TextView exchangeOutput;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange);
		
		nationChoice1 = (Spinner)findViewById(R.id.nationchoice1);
		nationChoice2 = (Spinner)findViewById(R.id.nationchoice2);
		exchangeInput = (EditText)findViewById(R.id.exchangeinput);
		exchangeOutput = (TextView)findViewById(R.id.exchangeoutput);
		
		nationChoice1.setPrompt("국가를 선택 하십시오.");
		nationChoice2.setPrompt("국가를 선택 하십시오.");
		
		adspin = ArrayAdapter.createFromResource(this, R.array.nation, android.R.layout.simple_spinner_item);
		adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nationChoice1.setAdapter(adspin);
		nationChoice2.setAdapter(adspin);
		
		nationChoice1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				nation1 = position;
			}
			public void onNothingSelected(AdapterView<?> parent){
				
			}
		});
		
		nationChoice2.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				nation2 = position;
			}
			public void onNothingSelected(AdapterView<?> parent){
				
			}
		});
		//http://xurrency.com 접속해서 html통채로 다운받는 스레드. 다운받는데 시간이 좀 걸림...
		//api는 유료결제를 해야되기 때문에...
		DownThread mThread = new DownThread();
		mThread.start();
		
		overridePendingTransition(R.anim.exchange_enter, R.anim.exchange_exit);
	}
	
	public void mOnClick(View v){
		double money, exchangeResult;
		money = Double.parseDouble(exchangeInput.getText().toString());
		exchangeResult = toOther(toKRW(money));
		exchangeOutput.setText(String.format("%.2f", exchangeResult));
	}
	//입력값을 먼저 한국 화폐로 바꾸고 다시 바꾸고 싶은 나라의 화폐로 바꾸는 방식
	//toKRW -> toOther.
	public double toKRW(double money){
		if(nation1 == KRW)
			return money;
		else if(nation1 == USD)
			return (money*USDrate);
		else if(nation1 == JPY)
			return (money*JPYrate/100);
		else if(nation1 == EUR)
			return money*EURrate;
		else if(nation1 == CNY)
			return money*CNYrate;
		else if(nation1 == AUD)
			return money*AUDrate;
		else if(nation1 == CAD)
			return money*CADrate;
		else if(nation1 == NZD)
			return money*NZDrate;
		else
			return 0;
	}
	
	public double toOther(double money){
		if(nation2 == KRW)
			return money;
		else if(nation2 == USD)
			return money/USDrate;
		else if(nation2 == JPY)
			return money/JPYrate*100;
		else if(nation2 == EUR)
			return money/EURrate;
		else if(nation2 == CNY)
			return money/CNYrate;
		else if(nation2 == AUD)
			return money/AUDrate;
		else if(nation2 == CAD)
			return money/CADrate;
		else if(nation2 == NZD)
			return money/NZDrate;
		else
			return 0;
	}
	
	class DownThread extends Thread {
		String mAddr;
		String mResult;
		String USDstr;
		
		DownThread (){
			mResult = "";
		}
		
		public void run(){
			try {
				//각 나라 환율정보 나와있는 URL
				URL USDurl = new URL("http://xurrency.com/usd/krw");
				URL JPYurl = new URL("http://xurrency.com/jpy/krw");
				URL EURurl = new URL("http://xurrency.com/eur/krw");
				URL CNYurl = new URL("http://xurrency.com/cny/krw");
				URL AUDurl = new URL("http://xurrency.com/aud/krw");
				URL CADurl = new URL("http://xurrency.com/cad/krw");
				URL NZDurl = new URL("http://xurrency.com/nzd/krw");
				HttpURLConnection conn = (HttpURLConnection)USDurl.openConnection();
				if(conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						//버퍼리더의 리드라인 메서드로 47번 읽고, 그 줄에서 30~37번째 추출하면 환율정보...
						for(int i=0; i<47; i++){
							mResult = br.readLine();
						}
						br.close();
					}
					conn.disconnect();
					USDrate = Double.parseDouble(mResult.substring(30, 37));
				}
				conn = (HttpURLConnection)JPYurl.openConnection();
				if(conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
						for(int i=0; i<47; i++){
							mResult = br.readLine();
						}
						br.close();
					}
					conn.disconnect();
					JPYrate = (Double.parseDouble(mResult.substring(30, 37))*100);
				}
				conn = (HttpURLConnection)EURurl.openConnection();
				if(conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
						for(int i=0; i<47; i++){
							mResult = br.readLine();
						}
						br.close();
					}
					conn.disconnect();
					EURrate = Double.parseDouble(mResult.substring(30, 37));
				}
				conn = (HttpURLConnection)CNYurl.openConnection();
				if(conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
						for(int i=0; i<47; i++){
							mResult = br.readLine();
						}
						br.close();
					}
					conn.disconnect();
					CNYrate = Double.parseDouble(mResult.substring(30, 37));
				}
				conn = (HttpURLConnection)AUDurl.openConnection();
				if(conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
						for(int i=0; i<47; i++){
							mResult = br.readLine();
						}
						br.close();
					}
					conn.disconnect();
					AUDrate = Double.parseDouble(mResult.substring(30, 37));
				}
				conn = (HttpURLConnection)CADurl.openConnection();
				if(conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
						for(int i=0; i<47; i++){
							mResult = br.readLine();
						}
						br.close();
					}
					conn.disconnect();
					CADrate = Double.parseDouble(mResult.substring(30, 37));
				}
				conn = (HttpURLConnection)NZDurl.openConnection();
				if(conn != null) {
					conn.setConnectTimeout(10000);
					conn.setUseCaches(false);
					if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
						BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
						
						for(int i=0; i<47; i++){
							mResult = br.readLine();
						}
						br.close();
					}
					conn.disconnect();
					NZDrate = Double.parseDouble(mResult.substring(30, 37));
				}
			}
			catch(Exception e){;}
		}
	}
}