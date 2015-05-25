package com.example.moneybook;

import java.util.Calendar;
import java.util.GregorianCalendar;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity{
	int inputMoney, dbMoney, totalMoney, usageMoney;
	Intent intent;
	MoneyDBHelper mHelper;
	RecordDBHelper mRecordDBHelper;
	EditText earn, usage;
	TextView left;
	final int USAGE = 0, EARN = 1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		earn = (EditText)findViewById(R.id.earn);
		usage = (EditText)findViewById(R.id.usage);
		left = (TextView)findViewById(R.id.left);
		
		mHelper = new MoneyDBHelper(this);
		mRecordDBHelper = new RecordDBHelper(this);
		onInit();
		Refresh();
	}
	
	public void onResume(){
		super.onResume();
		//액티비티가 가려졌다가 올라오면 잔액이 바뀔 수 있기 때문에 onResume()에 onInit메서드 호출
		onInit();
	}
	
	public void onInit(){
		//초기 실행되면 잔액불러와서 화면에 뿌려주는 함수
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor;
		cursor = db.rawQuery("SELECT left FROM total", null);
		
		while(cursor.moveToNext()){
			dbMoney = cursor.getInt(0);
		}
		
		left.setText("" + dbMoney);
		cursor.close();
		mHelper.close();
	}
	
	void Refresh(){
		//오늘의 날짜를 출력하기위한 함수.
		Calendar cal = new GregorianCalendar();
		StringBuilder time = new StringBuilder();
		time.append(String.format("%d년 %d월 %d일", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
		
		TextView today = (TextView)findViewById(R.id.today);
		today.setText(time.toString());
	}
	
	public void mOnClick(View v){
		SQLiteDatabase db;
		Cursor cursor;
		switch (v.getId()){
		case R.id.calculatorbtn:
			intent = new Intent(this, Calculator.class);
			startActivity(intent);
			break;
		
		case R.id.recordbtn:
			intent = new Intent(this, UsageRecord.class);
			startActivity(intent);
			break;
		case R.id.btnearn:
			//DB에서 잔액 불러와서 input값 더하고 잔액DB 업데이트, onSaveRecord메서드 호출
			if(earn.getText().toString().isEmpty()){
				Toast.makeText(this, "금액을 입력해 주십시오.", Toast.LENGTH_SHORT).show();
				break;
			}
				
			db = mHelper.getWritableDatabase();
			cursor = db.rawQuery("SELECT left FROM total", null);
			
			while(cursor.moveToNext()){
				dbMoney = cursor.getInt(0);
			}
			inputMoney = Integer.parseInt(earn.getText().toString());
			totalMoney = inputMoney + dbMoney;
			db.execSQL("UPDATE total SET left = '" + totalMoney + "';");
			
			cursor.close();
			mHelper.close();
			
			left.setText("" + totalMoney);
			earn.setText("");
			onSaveRecord(inputMoney, EARN);
			break;
		case R.id.btnusage:
			//DB에서 잔액정보 불러와서 입력값을 뺀후 다시 저장, onSaveRecord메서드 호출
			if(usage.getText().toString().isEmpty()){
				Toast.makeText(this, "금액을 입력해 주십시오.", Toast.LENGTH_SHORT).show();
				break;
			}
			
			db = mHelper.getWritableDatabase();
			cursor = db.rawQuery("SELECT left FROM total", null);
			while(cursor.moveToNext()){
				dbMoney = cursor.getInt(0);
			}
			usageMoney = Integer.parseInt(usage.getText().toString());
			totalMoney = dbMoney - usageMoney;
			db.execSQL("UPDATE total SET left = '" + totalMoney + "';");
			
			cursor.close();
			mHelper.close();
			
			left.setText("" + totalMoney);
			usage.setText("");
			onSaveRecord(usageMoney, USAGE);
			break;
		case R.id.exchangebtn:
			intent = new Intent(this, Exchange.class);
			startActivity(intent);
			break;
		case R.id.settingbtn:
			intent = new Intent(this, Setting.class);
			startActivity(intent);
			break;
		}
	}
	//수입, 지출 내역을 DB에 저장하기 위해 정의한 함수
	public void onSaveRecord (int money, int content){
		Calendar cal = new GregorianCalendar();
		StringBuilder date = new StringBuilder();
		StringBuilder time = new StringBuilder();
		ContentValues row = new ContentValues();
		
		date.append(String.format("%d-%d-%d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
		time.append(String.format("%d:%d:%d", cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
		SQLiteDatabase db = mRecordDBHelper.getWritableDatabase();
		if( content == USAGE){
			row.put("date", date.toString());
			row.put("time", time.toString());
			row.put("left", money);
			row.put("content", "지출");
			db.insert("record", null, row);
		}
		else if(content == EARN){
			row.put("date", date.toString());
			row.put("time", time.toString());
			row.put("left", money);
			row.put("content", "수입");
			db.insert("record", null, row);
		}
		mRecordDBHelper.close();
	}
}

class MoneyDBHelper extends SQLiteOpenHelper {
	public MoneyDBHelper(Context context){
		super(context, "Test.db", null, 1);
	}
	
	public void onCreate(SQLiteDatabase db){
		db.execSQL("CREATE TABLE total ( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
				 + "left INTEGER);");
		db.execSQL("INSERT INTO total VALUES (null, '0');");
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS total");
		onCreate(db);
	}
}

class RecordDBHelper extends SQLiteOpenHelper {
	public RecordDBHelper(Context context){
		super(context, "Record.db", null, 1);
	}
	
	public void onCreate(SQLiteDatabase db){
		db.execSQL("CREATE TABLE record ( _id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT, time TEXT, left INTEGER, content TEXT);");
		
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS record");
		onCreate(db);
	}
}
