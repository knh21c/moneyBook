package com.example.moneybook;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends Activity{
	Spinner spi_year, spi_month, spi_day;
	ArrayAdapter<CharSequence> adYear, adMonth, adDay;
	MoneyDBHelper mMoneyHelper;
	RecordDBHelper mRecordHelper;
	int dbMoney;
	TextView left;
	Intent mapIntent;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		//////////////스피너 설정///////////////////////
		spi_year = (Spinner)findViewById(R.id.spi_year);
		spi_month = (Spinner)findViewById(R.id.spi_month);
		spi_day = (Spinner)findViewById(R.id.spi_day);
		spi_year.setPrompt("년도");
		spi_month.setPrompt("월");
		spi_day.setPrompt("일");
		
		adYear = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
		adYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adMonth = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
		adMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adDay = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
		adDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spi_year.setAdapter(adYear);
		spi_month.setAdapter(adMonth);
		spi_day.setAdapter(adDay);
		//////////////////////////////////////////////////
		
		mMoneyHelper = new MoneyDBHelper(this);
		mRecordHelper = new RecordDBHelper(this);
		
		left = (TextView)findViewById(R.id.left);
	}
	
	public void mOnClick(View v){
		switch(v.getId()){
		case R.id.dbclear:
			new AlertDialog.Builder(Setting.this)
			.setTitle("DB 삭제")
			.setMessage("삭제된 데이터는 복구할 수 없습니다. 정말 삭제하시겠습니까?")
			.setPositiveButton("예", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//total(총 잔액)테이블 삭제
					
					SQLiteDatabase db = mMoneyHelper.getWritableDatabase();
					db.execSQL("UPDATE total SET left = '0';");
					mMoneyHelper.close();
					//record(수입/지출 기록)테이블 삭제
					
					db = mRecordHelper.getWritableDatabase();
					db.execSQL("DELETE FROM record;");
					mRecordHelper.close();
					
					Toast.makeText(Setting.this, "데이터 삭제 완료!", Toast.LENGTH_SHORT)
					.show();
				}
			})
			.setNegativeButton("아니오", null)
			.show();
			break;
		case R.id.registbtn:
			//라디오버튼
			RadioButton radio_use, radio_earn;
			radio_use = (RadioButton)findViewById(R.id.radio_use);
			radio_earn = (RadioButton)findViewById(R.id.radio_earn);
			//등록금액 입력
			EditText input = (EditText)findViewById(R.id.input);
			//알람매니져
			AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(Setting.this, AlarmReceiver.class);
			
			Calendar cal1 = Calendar.getInstance();
			String year, month, day, inputStr;
			int inputInt;
			String Empty = "";
			
			inputStr = input.getText().toString();
			if(radio_use.isChecked()){
				intent.putExtra("type", 0);
			} else if(radio_earn.isChecked()){
				intent.putExtra("type", 1);
			} else if(!radio_use.isChecked() && !radio_earn.isChecked()){
				Toast.makeText(this, "수입 또는 지출을 선택해 주세요.", Toast.LENGTH_SHORT).show();
				break;
			}
			
			if(inputStr.equals(Empty)){
				Toast.makeText(this, "금액을 입력해 주세요.", Toast.LENGTH_SHORT).show();
				break;
			}else{
				inputInt = Integer.parseInt(inputStr);
				intent.putExtra("input", inputInt);
			}
			
			PendingIntent sender = PendingIntent.getBroadcast(Setting.this, 0, intent, 0);
			
			year = (String)spi_year.getItemAtPosition(spi_year.getSelectedItemPosition());
			month = (String)spi_month.getItemAtPosition(spi_month.getSelectedItemPosition());
			day = (String)spi_day.getItemAtPosition(spi_day.getSelectedItemPosition());
			
			
			cal1.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), 0, 55);
			long time = cal1.getTimeInMillis();
			
			am.set(AlarmManager.RTC_WAKEUP, time, sender);
			//sender.cancel();
			
			Toast.makeText(this, "등록 완료!", Toast.LENGTH_SHORT).show();
			input.setText("");
			break;
		case R.id.map:
			mapIntent = new Intent(this, Map.class);
			startActivity(mapIntent);
			break;
		}
	}
}