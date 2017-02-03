package com.example.gamesurfaceview2;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;


public class GameActivity extends Activity{
	//����һ��GameActivity����
		public static GameActivity gameActivity;
		//����һ�����Բ��ֶ���
	    private LinearLayout gameView;
	    
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//ʵ����GameBirdActivity����
			gameActivity = this;
			//���ر�������״̬��
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			//���ؽ��沼��
			setContentView(R.layout.content_view);

			//ʵ�������Բ��ֶ���
			gameView = (LinearLayout)this.findViewById(R.id.game_view);
			//Ϊ���Բ��ֶ������һ����ͼ
			gameView.addView(new MySurfaceView(this));
		}
		
		public static final String GameSettingsFile = "Game_Settings"; //�ļ���
		public static final String Settings_CurrentScore = "CurrentScore"; //��ǰ��
		public static final String Settings_MaxScore = "MaxScore"; //��߷�
		
		/**
		 * ���浱ǰ�÷�����
		 * @param level
		 */
		public void saveSettingData(int level) {
			
			SharedPreferences spf = getSharedPreferences(
					GameSettingsFile, 0);

			spf.edit().putInt(Settings_CurrentScore, level).commit(); //���浱ǰ��
			
			int top = spf.getInt(Settings_MaxScore, 0); //��ȡ��߷�
			
			if(level>top){ //�����ǰ�ִ�����߷֣���ѵ�ǰ�ָ�ֵ����߷�
				spf.edit().putInt(Settings_MaxScore, level).commit();
			}
		}
		
	}
