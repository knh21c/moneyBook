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
		//////////////���ǳ� ����///////////////////////
		spi_year = (Spinner)findViewById(R.id.spi_year);
		spi_month = (Spinner)findViewById(R.id.spi_month);
		spi_day = (Spinner)findViewById(R.id.spi_day);
		spi_year.setPrompt("�⵵");
		spi_month.setPrompt("��");
		spi_day.setPrompt("��");
		
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
			.setTitle("DB ����")
			.setMessage("������ �����ʹ� ������ �� �����ϴ�. ���� �����Ͻðڽ��ϱ�?")
			.setPositiveButton("��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//total(�� �ܾ�)���̺� ����
					
					SQLiteDatabase db = mMoneyHelper.getWritableDatabase();
					db.execSQL("UPDATE total SET left = '0';");
					mMoneyHelper.close();
					//record(����/���� ���)���̺� ����
					
					db = mRecordHelper.getWritableDatabase();
					db.execSQL("DELETE FROM record;");
					mRecordHelper.close();
					
					Toast.makeText(Setting.this, "������ ���� �Ϸ�!", Toast.LENGTH_SHORT)
					.show();
				}
			})
			.setNegativeButton("�ƴϿ�", null)
			.show();
			break;
		case R.id.registbtn:
			//������ư
			RadioButton radio_use, radio_earn;
			radio_use = (RadioButton)findViewById(R.id.radio_use);
			radio_earn = (RadioButton)findViewById(R.id.radio_earn);
			//��ϱݾ� �Է�
			EditText input = (EditText)findViewById(R.id.input);
			//�˶��Ŵ���
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
				Toast.makeText(this, "���� �Ǵ� ������ ������ �ּ���.", Toast.LENGTH_SHORT).show();
				break;
			}
			
			if(inputStr.equals(Empty)){
				Toast.makeText(this, "�ݾ��� �Է��� �ּ���.", Toast.LENGTH_SHORT).show();
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
			
			Toast.makeText(this, "��� �Ϸ�!", Toast.LENGTH_SHORT).show();
			input.setText("");
			break;
		case R.id.map:
			mapIntent = new Intent(this, Map.class);
			startActivity(mapIntent);
			break;
		}
	}
}