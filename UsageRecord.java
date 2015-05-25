package com.example.moneybook;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class UsageRecord extends Activity{
	RecordDBHelper mRecordDBHelper;
	ListView totalRecord, useRecord, earnRecord, searchRecord;
	ViewFlipper mFlip;
	Spinner fromYear, toYear, fromMonth, toMonth, fromDay, toDay;
	ArrayAdapter<CharSequence> adYear, adMonth, adDay;
	String fromYearStr, fromMonthStr, fromDayStr, toYearStr, toMonthStr, toDayStr;
	ArrayList<String> data;
	ArrayAdapter<String> Adapter;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usagerecord);
		
		mRecordDBHelper = new RecordDBHelper(this);
		//ViewFlipper초기화 및 애니메이션 설정
		mFlip = (ViewFlipper)findViewById(R.id.flip);
		mFlip.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.flip_in));
		mFlip.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.flip_out));
		//ListView 객체 생성
		totalRecord = (ListView)findViewById(R.id.totalrecord);
		earnRecord = (ListView)findViewById(R.id.earnrecord);
		useRecord = (ListView)findViewById(R.id.userecord);
		searchRecord = (ListView)findViewById(R.id.searchrecord);
		//Spinner객체 생성
		fromYear = (Spinner)findViewById(R.id.from_year);
		fromMonth = (Spinner)findViewById(R.id.from_month);
		fromDay = (Spinner)findViewById(R.id.from_day);
		toYear = (Spinner)findViewById(R.id.to_year);
		toMonth = (Spinner)findViewById(R.id.to_month);
		toDay = (Spinner)findViewById(R.id.to_day);
		//Spinner에 Prompt속성 설정
		fromYear.setPrompt("년도"); fromMonth.setPrompt("월"); fromDay.setPrompt("일");
		toYear.setPrompt("년도"); toMonth.setPrompt("월"); toDay.setPrompt("일");
		//SpinnerAdapter생성
		adYear = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
		adYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adMonth = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
		adMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adDay = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
		adDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//SpinnerAdapter등록
		fromYear.setAdapter(adYear); toYear.setAdapter(adYear);
		fromMonth.setAdapter(adMonth); toMonth.setAdapter(adMonth);
		fromDay.setAdapter(adDay); toDay.setAdapter(adDay);
		
		//액티비티 애니메이션 설정
		overridePendingTransition(R.anim.usagerecord_enter, R.anim.usagerecord_exit);
		//액티비티 출력 DB갱신 함수
		onInit();
		//리스너를 분리해서 만드는게 훨씬 나을뻔 했다... 코드가 너무 지저분함
		totalRecord.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, final int position, long id){
				new AlertDialog.Builder(UsageRecord.this)
				.setTitle("DB 삭제")
				.setMessage("선택하신 수입/지출 내역을 삭제 합니다.")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String subStr="";
						subStr = (String)totalRecord.getItemAtPosition(position);
						String subData="";
						char[] dataChar = subStr.toCharArray();
						for(int i=0; i<subStr.length(); i++){
							if(dataChar[i] == 32){
								for(int j=i+1; j<subStr.length(); j++){
									if(dataChar[j] == 44)
										break;
									subData += dataChar[j];
								}
								break;
							}
						}
						
						SQLiteDatabase db = mRecordDBHelper.getReadableDatabase();
						db.execSQL("DELETE FROM record WHERE time = '" + subData + "';");
						
						Cursor cursor = db.rawQuery("SELECT date, time,  left, content FROM record", null);
						data = new ArrayList<String>();
						data.clear();
						while(cursor.moveToNext()){
							data.add(String.format("%s %s,  %s원, %s", cursor.getString(0),
									cursor.getString(1), cursor.getString(2), cursor.getString(3)));
						}
						
						Adapter = new ArrayAdapter<String>
							(UsageRecord.this, android.R.layout.simple_list_item_1, data);
						
						totalRecord.setAdapter(Adapter);
						
						cursor.close();
						mRecordDBHelper.close();
						
						Toast.makeText(UsageRecord.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
						mFlip.setDisplayedChild(0);
					}
				})
				.setNegativeButton("아니오", null)
				.show();
			}
		});
		
		earnRecord.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, final int position, long id){
				new AlertDialog.Builder(UsageRecord.this)
				.setTitle("DB 삭제")
				.setMessage("선택하신 수입/지출 내역을 삭제 합니다.")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String subStr="";
						subStr = (String)earnRecord.getItemAtPosition(position);
						String subData="";
						char[] dataChar = subStr.toCharArray();
						for(int i=0; i<subStr.length(); i++){
							if(dataChar[i] == 32){
								for(int j=i+1; j<subStr.length(); j++){
									if(dataChar[j] == 44)
										break;
									subData += dataChar[j];
								}
								break;
							}
						}
						
						SQLiteDatabase db = mRecordDBHelper.getReadableDatabase();
						db.execSQL("DELETE FROM record WHERE time = '" + subData + "';");
						
						Cursor cursor = db.rawQuery("SELECT date, time,  left, content FROM record WHERE content = '수입'", null);
						data = new ArrayList<String>();
						data.clear();
						while(cursor.moveToNext()){
							data.add(String.format("%s %s,  %s원, %s", cursor.getString(0),
									cursor.getString(1), cursor.getString(2), cursor.getString(3)));
						}
						
						Adapter = new ArrayAdapter<String>
							(UsageRecord.this, android.R.layout.simple_list_item_1, data);
						
						earnRecord.setAdapter(Adapter);
						
						cursor.close();
						mRecordDBHelper.close();
						
						Toast.makeText(UsageRecord.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
						mFlip.setDisplayedChild(1);
					}
				})
				.setNegativeButton("아니오", null)
				.show();
			}
		});
		
		useRecord.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, final int position, long id){
				new AlertDialog.Builder(UsageRecord.this)
				.setTitle("DB 삭제")
				.setMessage("선택하신 수입/지출 내역을 삭제 합니다.")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String subStr="";
						subStr = (String)useRecord.getItemAtPosition(position);
						String subData="";
						char[] dataChar = subStr.toCharArray();
						for(int i=0; i<subStr.length(); i++){
							if(dataChar[i] == 32){
								for(int j=i+1; j<subStr.length(); j++){
									if(dataChar[j] == 44)
										break;
									subData += dataChar[j];
								}
								break;
							}
						}
						
						SQLiteDatabase db = mRecordDBHelper.getReadableDatabase();
						db.execSQL("DELETE FROM record WHERE time = '" + subData + "';");
						
						Cursor cursor = db.rawQuery("SELECT date, time,  left, content FROM record WHERE content = '지출'", null);
						data = new ArrayList<String>();
						data.clear();
						while(cursor.moveToNext()){
							data.add(String.format("%s %s,  %s원, %s", cursor.getString(0),
									cursor.getString(1), cursor.getString(2), cursor.getString(3)));
						}
						
						Adapter = new ArrayAdapter<String>
							(UsageRecord.this, android.R.layout.simple_list_item_1, data);
						
						useRecord.setAdapter(Adapter);
						
						cursor.close();
						mRecordDBHelper.close();
						
						Toast.makeText(UsageRecord.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
						mFlip.setDisplayedChild(2);
					}
				})
				.setNegativeButton("아니오", null)
				.show();
			}
		});
		
		/*searchRecord.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View view, final int position, long id){
				new AlertDialog.Builder(UsageRecord.this)
				.setTitle("DB 삭제")
				.setMessage("선택하신 수입/지출 내역을 삭제 합니다.")
				.setPositiveButton("예", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String data="";
						data = (String)searchRecord.getItemAtPosition(position);
						String subData="";
						char[] dataChar = data.toCharArray();
						for(int i=0; i<data.length(); i++){
							if(dataChar[i] == 32){
								for(int j=i+1; j<data.length(); j++){
									if(dataChar[j] == 44)
										break;
									subData += dataChar[j];
								}
								break;
							}
						}
						
						SQLiteDatabase db = mRecordDBHelper.getWritableDatabase();
						//db.execSQL("DELETE FROM record WHERE time = '" + subData + "';");
						//mRecordDBHelper.close();
						Cursor cursor = db.rawQuery("SELECT * FROM record WHERE time = '" + subData + "';", null);
						ArrayList<String> dataArr = new ArrayList<String>();
						ArrayList<Integer> IdArr = new ArrayList<Integer>();
						while(cursor.moveToNext()){
							dataArr.add(String.format(String.format("%s %s,  %s원, %s", cursor.getString(1),
									cursor.getString(2), cursor.getString(3), cursor.getString(4))));
							IdArr.add(cursor.getInt(0));
						}
						for(int i=0; i<dataArr.size(); i++){
							if(data.equals(dataArr.get(i))){
								db.execSQL("DELETE FROM record WHERE _id = '" + IdArr.get(i) + "';");
								break;
							}
						}
						
						cursor = db.rawQuery("SELECT date, time,  left, content FROM record WHERE content = '지출'", null);
						
						while(cursor.moveToNext()){
							dataArr.add(String.format("%s %s,  %s원, %s", cursor.getString(0),
									cursor.getString(1), cursor.getString(2), cursor.getString(3)));
						}
						
						ArrayAdapter<String> Adapter = new ArrayAdapter<String>
							(UsageRecord.this, android.R.layout.simple_list_item_1, dataArr);
						
						useRecord.setAdapter(Adapter);
						
						cursor.close();
						mRecordDBHelper.close();
						
						Toast.makeText(UsageRecord.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
						mFlip.setDisplayedChild(3);
					}
				})
				.setNegativeButton("아니오", null)
				.show();
			}
		});*/
	}
	void onInit(){
		//record 테이블에서 데이터 읽어와 totalRecord에 리스트로 출력
		SQLiteDatabase db = mRecordDBHelper.getReadableDatabase();
		Cursor cursor;
		ArrayList<String> data = new ArrayList<String>();
		
		cursor = db.rawQuery("SELECT date, time, left, content FROM record", null);
		
		while(cursor.moveToNext()){
			data.add(String.format("%s %s, %s원, %s", cursor.getString(0),
					cursor.getString(1), cursor.getString(2), cursor.getString(3)));
		}
		ArrayAdapter<String> Adapter;
		Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
		
		totalRecord.setAdapter(Adapter);
		
		cursor.close();
		mRecordDBHelper.close();
	}
	
	public void mOnClick(View v){
		SQLiteDatabase db = mRecordDBHelper.getReadableDatabase();
		Cursor cursor;
		
		
		switch(v.getId()){
		//총기록, 수입기록, 지출기록 쿼리만 수정
		case R.id.totalrecordbtn:
			data = new ArrayList<String>();
			cursor = db.rawQuery("SELECT date, time,  left, content FROM record", null);
			
			while(cursor.moveToNext()){
				data.add(String.format("%s %s,  %s원, %s", cursor.getString(0),
						cursor.getString(1), cursor.getString(2), cursor.getString(3)));
			}
			
			Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
			
			totalRecord.setAdapter(Adapter);
			
			cursor.close();
			mRecordDBHelper.close();
			mFlip.setDisplayedChild(0);
			break;
		case R.id.earnrecordbtn:
			data = new ArrayList<String>();
			cursor = db.rawQuery("SELECT date, time, left, content FROM record WHERE content = '수입';", null);
			
			while(cursor.moveToNext()){
				data.add(String.format("%s %s,  %s원, %s", cursor.getString(0),
						cursor.getString(1), cursor.getString(2), cursor.getString(3)));
			}
			
			Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
			
			earnRecord.setAdapter(Adapter);
			
			cursor.close();
			mRecordDBHelper.close();
			mFlip.setDisplayedChild(1);
			break;
		case R.id.userecordbtn:
			data = new ArrayList<String>();
			cursor = db.rawQuery("SELECT date, time, left, content FROM record WHERE content = '지출';", null);
			
			while(cursor.moveToNext()){
				data.add(String.format("%s %s, %s원, %s", cursor.getString(0),
						cursor.getString(1), cursor.getString(2), cursor.getString(3)));
			}
			
			Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
			
			useRecord.setAdapter(Adapter);
			
			cursor.close();
			mRecordDBHelper.close();
			mFlip.setDisplayedChild(2);
			break;
		//날짜 입력받고 거기에 맞춰서 BETWEEN으로 검색(문제있음!)
		case R.id.search:
			//스피너의 선택된 항목을 문자열로 구한다.
			fromYearStr = (String)adYear.getItem(fromYear.getSelectedItemPosition());
			fromMonthStr = (String)adMonth.getItem(fromMonth.getSelectedItemPosition());
			fromDayStr = (String)adDay.getItem(fromDay.getSelectedItemPosition());
			toYearStr = (String)adYear.getItem(toYear.getSelectedItemPosition());
			toMonthStr = (String)adMonth.getItem(toMonth.getSelectedItemPosition());
			toDayStr = (String)adDay.getItem(toDay.getSelectedItemPosition());
			//DB에서 BETWEEN문을 사용하기 위해 ArrayList start, end 생성
			ArrayList<String> start = new ArrayList<String>();
			ArrayList<String> end = new ArrayList<String>();
			int fromDateInt, toDateInt;
			fromDateInt = Integer.parseInt(fromYearStr+fromMonthStr+fromDayStr);
			toDateInt = Integer.parseInt(toYearStr+toMonthStr+toDayStr);
			//DB에서 로드하기 위해 문자열 조합
			String fromDate, toDate;
			fromDate = fromYearStr + "-" + fromMonthStr + "-" + fromDayStr;
			toDate = toYearStr + "-" + toMonthStr + "-" + toDayStr;
			//사용자가 입력한 시작일과 일치하는 레코드의 모든 _id필드값을 로드
			cursor = db.rawQuery("SELECT _id FROM record WHERE date = '" + fromDate + "';", null);
			//그 값을 start에 저장
			while(cursor.moveToNext()){
				start.add(cursor.getString(0));
			}
			//사용자가 입력한 종료일과 일치하는 레코드의 모든 _id필드값을 로드
			cursor = db.rawQuery("SELECT _id FROM record WHERE date = '" + toDate + "';", null);
			//그 값을 end에 저장
			while(cursor.moveToNext()){
				end.add(cursor.getString(0));
			}
			
			if(fromDateInt > toDateInt){
				Toast.makeText(this, "날짜 입력 순서가 잘못됐습니다.", Toast.LENGTH_SHORT).show();
				break;
			} else if(start.size() == 0 || end.size() == 0){
				Toast.makeText(this, "수입/지출 내역이 없습니다.", Toast.LENGTH_SHORT).show();
				break;
			}
			data = new ArrayList<String>();
			//start의 첫번 째 _id값과 end의 마지막 _id값을 BETWEEN구문으로 만들면 사용자가 원하는 기간의 모든 데이터를 로드할 수 있음
			cursor = db.rawQuery("SELECT date, time, left, content FROM record WHERE _id BETWEEN " + 
								start.get(0) + " AND " + end.get(end.size()-1) + ";", null);
			while(cursor.moveToNext()){
				data.add(String.format("%s %s, %s원, %s", cursor.getString(0),
						cursor.getString(1), cursor.getString(2), cursor.getString(3)));
			}
			
			Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
			
			searchRecord.setAdapter(Adapter);
			
			cursor.close();
			mRecordDBHelper.close();
			mFlip.setDisplayedChild(3);
			break;
		}
	}
}
