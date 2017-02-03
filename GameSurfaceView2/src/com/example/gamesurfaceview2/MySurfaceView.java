package com.example.gamesurfaceview2;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	// ���ڿ���SurfaceView
	private SurfaceHolder sfh;
	// ����һ������
	private Paint paint;
	// ����һ���߳�
	private Thread th;
	// �߳������ı�ʶλ
	private boolean flag;
	// ����һ������
	private Canvas canvas;
	// ������Ļ�Ŀ��
	private static int screenW, screenH;

	// ������Ϸ״̬����
	private static final int GAME_INIT = 0; // ��Ϸ��ʼ״̬
	private static final int GAMEING = 1; // ��Ϸ��
	private static final int GAME_OVER = -1; // ��Ϸ����
	// ��ǰ��ϷĬ��״̬��Ĭ��Ϊ��Ϸ��ʼ״̬��
	private static int gameState = GAME_INIT;

	// �ײ�ˮƽ�ߵ�����
	private int bottom_x,bottom_y;
	// �ײ�ˮƽ�ߵĳ���
	private int bottom_width = 15;
	// �����ٶ�
	private int speed = 3;

	// ��ǰ����
	private int currentScore = 0;

	// С�������
	private int ball_x,ball_y;
	// С��Ŀ��
	private int ball_width = 10;
	// С��y��Ĭ�ϸ߶�
	private int ball_v;
	//û�д�����Ļ����£�С����y��Ľ���߶�
	private int ball_a = 2;
	// ÿ�δ�����С����y��������߶�
	private int ball_vUp = -16;

	private ArrayList<int[]> walls = new ArrayList<int[]>();
	private ArrayList<int[]> remove_walls = new ArrayList<int[]>();
	private int wall_w = 50; // ���ǽ�Ŀ��
	private int wall_h = 100; // ���ǽ�ĸ߶�

	private int wall_step = 30;
	
	 // ����һ��MediaPlayer����
	private MediaPlayer player;


	/**
	 * GameBirdSurfaceView��ʼ������
	 */
	public MySurfaceView(Context context) {

		super(context);
		//ʵ��SurfaceHolder
		sfh = this.getHolder();
		//ΪSurfaceHolder����״̬����
		sfh.addCallback(this);
		//ʵ��һ������
		paint = new Paint();
		//���û�����ɫΪ��ɫ
		paint.setColor(Color.WHITE);
		paint.setTextSize(36);
		paint.setStyle(Style.STROKE); 

		// ʵ����MediaPlayer����
		player = player.create(getContext(), R.drawable.bgm);
		// ����ѭ������
		player.setLooping(true);
		
		//���ý���
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	/**
	 * SurfaceView��ͼ��������Ӧ�˺���
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		
		initGame();
		
		flag = true;
		th = new Thread(this);
		th.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}

	/**
	 * ��ʼ����Ϸ�ĺ���
	 */
	private void initGame() {

		if (gameState == GAME_INIT) {

			//Ϊ�ײ�ˮƽ�����긳ֵ
			bottom_x = 0;
			bottom_y = screenH-20;

			//Ĭ�ϵ�ǰ�÷�Ϊ0
			currentScore = 0;
			
			//����С�������
			ball_x = screenW / 3;
			ball_y = screenH / 2;
			

			speed = dp2px(3);

			ball_width = dp2px(10);
			ball_v = 0;
			ball_a = dp2px(2);
			ball_vUp = -dp2px(16);

			wall_w = dp2px(45);
			wall_h = dp2px(100);

			wall_step = wall_w * 4;
			
			walls.clear();
		}
	}

	// dpת��Ϊpx
	private int dp2px(float dp) {
		int px = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
						.getDisplayMetrics()));
		return px;
	}

	/**
	 * ��Ϸ��ͼ
	 */
	public void myDraw() {

		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				
				// ����С��
				canvas.drawCircle(ball_x, ball_y, ball_width, paint);
				
				switch (gameState) {
				case GAME_INIT:
					initGame();
					canvas.drawText("�����Ļ����ʼ��Ϸ", screenW/3, screenH/3, paint);
					break;
				case GAMEING:
				case GAME_OVER:
					canvas.drawText("��ǰ�÷֣�"+String.valueOf(currentScore), 10, 50, paint);
					// ����ˮƽ����
					canvas.drawLine(bottom_x, bottom_y, screenW, bottom_y, paint);
					// ����ǽ�谭
					for (int i = 0; i < walls.size(); i++) {
						int[] wall = walls.get(i);

						float[] pts = { wall[0], 0, wall[0], wall[1], wall[0],
								wall[1] + wall_h, wall[0],bottom_y,
								wall[0] + wall_w, 0, wall[0] + wall_w, wall[1],

								wall[0] + wall_w, wall[1] + wall_h,
								wall[0] + wall_w,bottom_y, wall[0], wall[1],
								wall[0] + wall_w, wall[1], wall[0],
								wall[1] + wall_h, wall[0] + wall_w,
								wall[1] + wall_h };
						canvas.drawLines(pts, paint); // ���ƶ���ֱ��
					}
					if(gameState==GAME_OVER){//��Ϸ����
					Paint paint1 = new Paint();
					paint1.setTextSize(80);
					paint1.setColor(Color.WHITE);
					canvas.drawText("GAME OVER", screenW/4, screenH/2, paint1);
					}
					break;
					
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}

	/**
	 * �����¼�����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			switch (gameState) {
			case GAME_INIT:
				gameState = GAMEING;
				break;
			case GAMEING:
				ball_v = ball_vUp;
				break;
			case GAME_OVER:
				gameState = GAME_INIT;//�ص���ʼ״̬
				break;
			}
		}
		return true;
	}


	private int move_step = 0;

	/**
	 * ��Ϸ�߼�����
	 */
	private void logic() {
		// �߼�����������Ϸ״̬��ͬ���в�ͬ����
		switch (gameState) {
		case GAME_INIT:
			if(player.isPlaying()){
				player.pause(); // ֹͣ��������
			}
			break;
		case GAMEING:
			// ��������
			if(!player.isPlaying()){
		    player.start();
			}
			ball_v += ball_a;
			ball_y += ball_v;
			//��������ײ���
			if (ball_y >bottom_y -ball_width) {
				ball_y =bottom_y - ball_width;
				gameState = GAME_OVER; //��Ϸ״̬�л�Ϊ����״̬
			}

			if (bottom_x < -bottom_width) {
				bottom_x += bottom_width * 2;
			}
			bottom_x -= speed;

			// ��ǽ����ײ���
			remove_walls.clear();
			for (int i = 0; i < walls.size(); i++) {
				int[] wall = walls.get(i);
				wall[0] -= speed;
				if (wall[0] < -wall_w) {
					remove_walls.add(wall);
				} else if (wall[0] - ball_width <= ball_x
						&& wall[0] + wall_w + ball_width >= ball_x
						&& (ball_y <= wall[1] + ball_width || ball_y >= wall[1]
								+ wall_h - ball_width)) {
					gameState = GAME_OVER;
				}

				int pass = wall[0] + wall_w + ball_width -ball_x;
				if (pass < 0 && -pass <= speed) {
					currentScore++;
				}
			}
			// ������Ļ��ǽ�Ƴ�
			if (remove_walls.size() > 0) {
				walls.removeAll(remove_walls);
			}

			// ����µ�ǽ
			move_step += speed;
			if (move_step > wall_step) {
				int[] wall = new int[] {
						screenW,
						(int) (Math.random() * (bottom_y- 2 * wall_h) + 0.5 * wall_h) };
				walls.add(wall);
				move_step = 0;
			}
			break;
		case GAME_OVER:
			
			if(player.isPlaying()){
				player.pause(); // ֹͣ��������
			}

			if (ball_y < bottom_y - ball_width) {
				ball_v += ball_a;
				ball_y += ball_v;
				if(ball_y >= bottom_y - ball_width){
					ball_y = bottom_y - ball_width;  //�պ���С�������ˮƽ����
				}
			}else{
				GameActivity.gameActivity.saveSettingData(currentScore);
			}
			break;
				
		}
	}

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();
			logic();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// ��ذ���
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			gameState=GAME_INIT;
		}
		return super.onKeyDown(keyCode, event);
}
	
}