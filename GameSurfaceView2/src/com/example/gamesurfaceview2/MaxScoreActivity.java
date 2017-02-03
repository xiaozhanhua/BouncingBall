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
		
		//���ر�������״̬��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//���ؽ��沼��
		setContentView(R.layout.max_score);
		
		//ƥ��TextView��������������ʾ��һ�ַ�������߷�
		TextView tv = (TextView) findViewById(R.id.tv);
		//����һ��һά���飬������ȡgetSettingData()�ĵ�ǰ��������߷�
		int[] data = getSettingData();
		tv.setText("��һ�ֵ÷�: "+data[0]+"\n\n ��ʷ��߷�: "+data[1]);
}
	
	
	/**
	 * ������ȡSharedPreferences���ݵķ���
	 */
	private int[] getSettingData() {
		SharedPreferences spf = getSharedPreferences(GameActivity.GameSettingsFile, 0);
		int currentScore = spf.getInt(GameActivity.Settings_CurrentScore, 0);
		int maxScore = spf.getInt(GameActivity.Settings_MaxScore, 0);
		return new int[]{currentScore, maxScore};
	}
	}
