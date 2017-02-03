package com.example.gamesurfaceview2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;


/**
 * @author xiaozhanhua
 * 菜单界面的类
 */
public class MainActivity extends Activity implements OnClickListener {

	// 声明Button对象
	private Button btn_start, btn_maxscore, btn_about, btn_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 隐藏标题栏和状态栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//匹配Button对象
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_maxscore = (Button) findViewById(R.id.btn_maxscore);
		btn_about = (Button) findViewById(R.id.btn_about);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		
		//分别为Button对象添加监听器
		btn_start.setOnClickListener(this);
		btn_maxscore.setOnClickListener(this);
		btn_about.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start: //开始游戏按钮的 
			Intent intent = new Intent(MainActivity.this, GameActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_maxscore: //历史最高分按钮的
			Intent intent1 = new Intent(MainActivity.this, MaxScoreActivity.class);
			startActivity(intent1);
			break;
			
		case R.id.btn_about://关于游戏按钮的
			Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent2);
			break;	
		case R.id.btn_exit://退出游戏按钮的
			MainActivity.this.finish();
			break;
		default:
			break;
		}

	}

}
