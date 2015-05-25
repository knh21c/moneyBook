package com.example.moneybook;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlarmReceiver extends BroadcastReceiver {
	final int USE = 0, EARN = 1;
	int dbMoney, inputMoney, resultMoney;
	int Type;
	public void onReceive(Context context, Intent intent){
		RecordDBHelper mRecordHelper = new RecordDBHelper(context);
		MoneyDBHelper mMoneyHelper = new MoneyDBHelper(context);
		SQLiteDatabase db = mMoneyHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT left FROM total;", null);
		//����Ʈ�κ��� Extra�� ����
		inputMoney = intent.getIntExtra("input", 0);
		Type = intent.getIntExtra("type", 3);
		//DB���� �ܾ� �о� �鿩 ����
		while(cursor.moveToNext()){
			dbMoney = cursor.getInt(0);
		}
		if(Type == USE){
			resultMoney = dbMoney - inputMoney;
		} else if (Type == EARN){
			resultMoney = dbMoney + inputMoney;
		}
		db.execSQL("UPDATE total SET left = '" + resultMoney + "';");
		//cursor, DBHelper�ݱ�
		cursor.close(); mMoneyHelper.close();
		//���� �ð��� ���ϰ� DB�� �����ϱ� ���� ��ü��
		Calendar cal = new GregorianCalendar();
		StringBuilder date = new StringBuilder();
		StringBuilder time = new StringBuilder();
		ContentValues row = new ContentValues();
		
		date.append(String.format("%d-%d-%d", cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
		time.append(String.format("%d:%d:%d", cal.get(Calendar.HOUR_OF_DAY), 
				cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
		
		db = mRecordHelper.getWritableDatabase();
		
		if(Type == USE){
			row.put("date", date.toString());
			row.put("time", time.toString());
			row.put("left", inputMoney);
			row.put("content", "����");
			db.insert("record", null, row);
		} else if(Type == EARN){
			row.put("date", date.toString());
			row.put("time", time.toString());
			row.put("left", inputMoney);
			row.put("content", "����");
			db.insert("record", null, row);
		}
		
		mRecordHelper.close();
		
		NotificationManager mNotiManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification noti = new Notification(R.drawable.ic_launcher, "����/���� ��� �Ϸ�!",
				System.currentTimeMillis());
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
		PendingIntent content = PendingIntent.getService(context, 0, null, 0);
		noti.setLatestEventInfo(context, "��� �Ϸ�!", "�����Ͻ� ����/������ ����߽��ϴ�.", content);
		mNotiManager.notify(0, noti);
		
		intent.removeExtra("type"); intent.removeExtra("input");
	}
}
