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
		//시작하자마자 init메서드호출
		init();
		//로케이션매니져 시스템서비스로 획득
		mLocMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mProvider = mLocMan.getBestProvider(new Criteria(), true);
	}
	
	public void onResume(){
		super.onResume();
		//액티비티가 멈췄다 재시작하면 init메서드 호출하고 로케이션매니져 위치업데이트
		init();
		mLocMan.requestLocationUpdates(mProvider, 3000, 10, mListener);
	}
	
	public void onPause(){
		super.onPause();
		mLocMan.removeUpdates(mListener);
	}
	//로케이션리스너
	LocationListener mListener = new LocationListener() {
		public void onLocationChanged(Location location){
			//바뀐 위치의 위도, 경도설정해서 animate로 위치이동
			googleMap.animateCamera(CameraUpdateFactory.
					newLatLng(new LatLng(location.getLatitude(),
							location.getLongitude())));
		}
		
		public void onProviderDisabled(String provider){
			Toast.makeText(Map.this, "서비스 사용 불가", Toast.LENGTH_SHORT).show();
		}
		
		public void onProviderEnabled(String provider){
			
		}
		
		public void onStatusChanged(String provider, int status, Bundle extras){
			
		}
	};
	
	void init(){
		//googleMap null이면 getMap()하고, 줌 15로 설정
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
		//테스트삼아 학교 위도, 경도 값 주고 마커 추가
		googleMap.addMarker(new MarkerOptions().position(new LatLng(37.450992, 127.127113)).title("TEST"));
	}
}
