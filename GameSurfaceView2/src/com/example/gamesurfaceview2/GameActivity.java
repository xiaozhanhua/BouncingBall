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
	//声明一个GameActivity对象
		public static GameActivity gameActivity;
		//声明一个线性布局对象
	    private LinearLayout gameView;
	    
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//实例化GameBirdActivity对象
			gameActivity = this;
			//隐藏标题栏和状态栏
			this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			//加载界面布局
			setContentView(R.layout.content_view);

			//实例化线性布局对象
			gameView = (LinearLayout)this.findViewById(R.id.game_view);
			//为线性布局对象添加一个视图
			gameView.addView(new MySurfaceView(this));
		}
		
		public static final String GameSettingsFile = "Game_Settings"; //文件名
		public static final String Settings_CurrentScore = "CurrentScore"; //当前分
		public static final String Settings_MaxScore = "MaxScore"; //最高分
		
		/**
		 * 保存当前得分数据
		 * @param level
		 */
		public void saveSettingData(int level) {
			
			SharedPreferences spf = getSharedPreferences(
					GameSettingsFile, 0);

			spf.edit().putInt(Settings_CurrentScore, level).commit(); //保存当前分
			
			int top = spf.getInt(Settings_MaxScore, 0); //获取最高分
			
			if(level>top){ //如果当前分大于最高分，则把当前分赋值给最高分
				spf.edit().putInt(Settings_MaxScore, level).commit();
			}
		}
		
	}
