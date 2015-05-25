package com.example.moneybook;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends FragmentActivity{
	GoogleMap googleMap;
	LocationManager mLocMan;
	String mProvider;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		//�������ڸ��� init�޼���ȣ��
		init();
		//�����̼ǸŴ��� �ý��ۼ��񽺷� ȹ��
		mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mProvider = mLocMan.getBestProvider(new Criteria(), true);
	}
	
	public void onResume(){
		super.onResume();
		//��Ƽ��Ƽ�� ����� ������ϸ� init�޼��� ȣ���ϰ� �����̼ǸŴ��� ��ġ������Ʈ
		init();
		mLocMan.requestLocationUpdates(mProvider, 3000, 10, mListener);
	}
	
	public void onPause(){
		super.onPause();
		mLocMan.removeUpdates(mListener);
	}
	//�����̼Ǹ�����
	LocationListener mListener = new LocationListener() {
		public void onLocationChanged(Location location){
			//�ٲ� ��ġ�� ����, �浵�����ؼ� animate�� ��ġ�̵�
			googleMap.animateCamera(CameraUpdateFactory.
					newLatLng(new LatLng(location.getLatitude(),
							location.getLongitude())));
		}
		
		public void onProviderDisabled(String provider){
			Toast.makeText(Map.this, "���� ��� �Ұ�", Toast.LENGTH_SHORT).show();
		}
		
		public void onProviderEnabled(String provider){
			
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras){
			
		}
	};
	
	void init(){
		//googleMap null�̸� getMap()�ϰ�, �� 15�� ����
		if(googleMap == null){
			googleMap = ((SupportMapFragment)getSupportFragmentManager().
					findFragmentById(R.id.gmap)).getMap();
			if(googleMap != null){
				addMarker();
			}
		}
		googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	}
	
	void addMarker(){
		//�׽�Ʈ��� �б� ����, �浵 �� �ְ� ��Ŀ �߰�
		googleMap.addMarker(new MarkerOptions().position(new LatLng(37.450992, 127.127113)).title("TEST"));
	}
}
