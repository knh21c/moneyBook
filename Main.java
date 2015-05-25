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
		//��Ƽ��Ƽ�� �������ٰ� �ö���� �ܾ��� �ٲ� �� �ֱ� ������ onResume()�� onInit�޼��� ȣ��
		onInit();
	}
	
	public void onInit(){
		//�ʱ� ����Ǹ� �ܾ׺ҷ��ͼ� ȭ�鿡 �ѷ��ִ� �Լ�
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
		//������ ��¥�� ����ϱ����� �Լ�.
		Calendar cal = new GregorianCalendar();
		StringBuilder time = new StringBuilder();
		time.append(String.format("%d�� %d�� %d��", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
		
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
			//DB���� �ܾ� �ҷ��ͼ� input�� ���ϰ� �ܾ�DB ������Ʈ, onSaveRecord�޼��� ȣ��
			if(earn.getText().toString().isEmpty()){
				Toast.makeText(this, "�ݾ��� �Է��� �ֽʽÿ�.", Toast.LENGTH_SHORT).show();
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
			//DB���� �ܾ����� �ҷ��ͼ� �Է°��� ���� �ٽ� ����, onSaveRecord�޼��� ȣ��
			if(usage.getText().toString().isEmpty()){
				Toast.makeText(this, "�ݾ��� �Է��� �ֽʽÿ�.", Toast.LENGTH_SHORT).show();
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
	//����, ���� ������ DB�� �����ϱ� ���� ������ �Լ�
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
			row.put("content", "����");
			db.insert("record", null, row);
		}
		else if(content == EARN){
			row.put("date", date.toString());
			row.put("time", time.toString());
			row.put("left", money);
			row.put("content", "����");
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
