package com.example.moneybook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Calculator extends Activity{
	String calResultStr = "";
	Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero;
	Button btnPlus, btnSub, btnDiv, btnMul, btnClear, btnResult;
	TextView calResult;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);
		
		btnOne = (Button)findViewById(R.id.one);
		btnTwo = (Button)findViewById(R.id.two);
		btnThree = (Button)findViewById(R.id.three);
		btnFour = (Button)findViewById(R.id.four);
		btnFive = (Button)findViewById(R.id.five);
		btnSix = (Button)findViewById(R.id.six);
		btnSeven = (Button)findViewById(R.id.seven);
		btnEight = (Button)findViewById(R.id.eight);
		btnNine = (Button)findViewById(R.id.nine);
		btnZero = (Button)findViewById(R.id.zero);
		btnPlus = (Button)findViewById(R.id.btnplus);
		btnSub = (Button)findViewById(R.id.btnsub);
		btnDiv = (Button)findViewById(R.id.btndiv);
		btnMul = (Button)findViewById(R.id.btnmul);
		btnClear = (Button)findViewById(R.id.btnclear);
		btnResult = (Button)findViewById(R.id.btnresult);
		calResult = (TextView)findViewById(R.id.calresult);
		
		overridePendingTransition(R.anim.calculator_enter, R.anim.calculator_exit);
	}
	public void mOnClick(View v){
		switch(v.getId()){
		case R.id.one:
			calResultStr += "1";
			calResult.setText(calResultStr);
			break;
		case R.id.two:
			calResultStr += "2";
			calResult.setText(calResultStr);
			break;
		case R.id.three:
			calResultStr += "3";
			calResult.setText(calResultStr);
			break;
		case R.id.four:
			calResultStr += "4";
			calResult.setText(calResultStr);
			break;
		case R.id.five:
			calResultStr += "5";
			calResult.setText(calResultStr);
			break;
		case R.id.six:
			calResultStr += "6";
			calResult.setText(calResultStr);
			break;
		case R.id.seven:
			calResultStr += "7";
			calResult.setText(calResultStr);
			break;
		case R.id.eight:
			calResultStr += "8";
			calResult.setText(calResultStr);
			break;
		case R.id.nine:
			calResultStr += "9";
			calResult.setText(calResultStr);
			break;
		case R.id.zero:
			calResultStr += "0";
			calResult.setText(calResultStr);
			break;
		case R.id.btnplus:
			calResultStr += "+";
			calResult.setText(calResultStr);
			break;
		case R.id.btnsub:
			calResultStr += "-";
			calResult.setText(calResultStr);
			break;
		case R.id.btndiv:
			calResultStr += "/";
			calResult.setText(calResultStr);
			break;
		case R.id.btnmul:
			calResultStr += "*";
			calResult.setText(calResultStr);
			break;
		case R.id.btnclear:
			calResultStr = "";
			calResult.setText(calResultStr);
			break;
		case R.id.btnresult:
			int length = calResultStr.length(); 
			int cnt=0;
			int i, j;
			String sub1 = "";
			String sub2 = "";
			double tempResult=0;
			
			char[] tempArray = calResultStr.toCharArray();
			
			for(i=0; i<length; i++){
				if(tempArray[i] == 42/*'*'*/ || tempArray[i] == 43/*'+'*/ || 
						tempArray[i] == 45/*'-'*/ || tempArray[i] == 47/*'/'*/){
					cnt++;
				}
			}

			if(cnt == 1){
				for(i=0; i<length; i++){
					if((tempArray[i] > 47 && tempArray[i] < 58) || tempArray[i] == 46/*'.'*/){

						sub1 += tempArray[i];
					}
					
					else if(tempArray[i] == 46){
						if(tempArray[i+2] == 42){
							for(j=i+3; j<length; j++){
								if(tempArray[j] == 46)
									break;
								sub2 += tempArray[j];
							}
							tempResult = Double.parseDouble(sub1) * Double.parseDouble(sub2);
							break;
						}
						if(tempArray[i+2] == 43){
							for(j=i+3; j<length; j++){
								if(tempArray[j] == 46)
									break;
								sub2 += tempArray[j];
							}
							tempResult = Double.parseDouble(sub1) + Double.parseDouble(sub2);
							break;
						}
						if(tempArray[i+2] == 45){
							for(j=i+3; j<length; j++){
								if(tempArray[j] == 46)
									break;
								sub2 += tempArray[j];
							}
							tempResult = Double.parseDouble(sub1) - Double.parseDouble(sub2);
							break;
						}
						if(tempArray[i+2] == 47){
							for(j=i+3; j<length; j++){
								if(tempArray[j] == 46)
									break;
								sub2 += tempArray[j];
							}
							tempResult = Double.parseDouble(sub1) / Double.parseDouble(sub2);
							break;
						}
					}

					else if(tempArray[i] == 42){
						for(j=i+1; j<length; j++){
							if(tempArray[j] > 47 && tempArray[j] < 58){
								sub2 += tempArray[j];
							}
						}
						tempResult = Double.parseDouble(sub1) * Double.parseDouble(sub2);
						break;
					}
					else if(tempArray[i] == 43){
						for(j=i+1; j<length; j++){
							if(tempArray[j] > 47 && tempArray[j] < 58){
								sub2 += tempArray[j];
							}
						}
						tempResult = Double.parseDouble(sub1) + Double.parseDouble(sub2);
						break;
					}
					else if(tempArray[i] == 45){
						for(j=i+1; j<length; j++){
							if(tempArray[j] > 47 && tempArray[j] < 58){
								sub2 += tempArray[j];
							}
						}
						tempResult = Double.parseDouble(sub1) - Double.parseDouble(sub2);
						break;
					}
					else if(tempArray[i] == 47){
						for(j=i+1; j<length; j++){
							if(tempArray[j] > 47 && tempArray[j] < 58){
								sub2 += tempArray[j];
							}
						}
						tempResult = Double.parseDouble(sub1) / Double.parseDouble(sub2);
						break;
					}
				}
			} else {
				
				calResult.setText("");
			}
			
			
			calResultStr = String.valueOf(tempResult);
			calResult.setText(calResultStr);
			break;
		}
	}
}
