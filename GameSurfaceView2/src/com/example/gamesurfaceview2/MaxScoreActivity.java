package com.example.gamesurfaceview2;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MaxScoreActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//隐藏标题栏和状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//加载界面布局
		setContentView(R.layout.max_score);
		
		//匹配TextView对象，这里用来显示上一轮分数和最高分
		TextView tv = (TextView) findViewById(R.id.tv);
		//定义一个一维数组，用来获取getSettingData()的当前分数和最高分
		int[] data = getSettingData();
		tv.setText("上一轮得分: "+data[0]+"\n\n 历史最高分: "+data[1]);
}
	
	
	/**
	 * 用来获取SharedPreferences数据的方法
	 */
	private int[] getSettingData() {
		SharedPreferences spf = getSharedPreferences(GameActivity.GameSettingsFile, 0);
		int currentScore = spf.getInt(GameActivity.Settings_CurrentScore, 0);
		int maxScore = spf.getInt(GameActivity.Settings_MaxScore, 0);
		return new int[]{currentScore, maxScore};
	}
	}
