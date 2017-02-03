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
 * �˵��������
 */
public class MainActivity extends Activity implements OnClickListener {

	// ����Button����
	private Button btn_start, btn_maxscore, btn_about, btn_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ���ر�������״̬��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		//ƥ��Button����
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_maxscore = (Button) findViewById(R.id.btn_maxscore);
		btn_about = (Button) findViewById(R.id.btn_about);
		btn_exit = (Button) findViewById(R.id.btn_exit);
		
		//�ֱ�ΪButton������Ӽ�����
		btn_start.setOnClickListener(this);
		btn_maxscore.setOnClickListener(this);
		btn_about.setOnClickListener(this);
		btn_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_start: //��ʼ��Ϸ��ť�� 
			Intent intent = new Intent(MainActivity.this, GameActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_maxscore: //��ʷ��߷ְ�ť��
			Intent intent1 = new Intent(MainActivity.this, MaxScoreActivity.class);
			startActivity(intent1);
			break;
			
		case R.id.btn_about://������Ϸ��ť��
			Intent intent2 = new Intent(MainActivity.this, AboutActivity.class);
			startActivity(intent2);
			break;	
		case R.id.btn_exit://�˳���Ϸ��ť��
			MainActivity.this.finish();
			break;
		default:
			break;
		}

	}

}
