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
	// 用于控制SurfaceView
	private SurfaceHolder sfh;
	// 声明一个画笔
	private Paint paint;
	// 声明一条线程
	private Thread th;
	// 线程消亡的标识位
	private boolean flag;
	// 声明一个画布
	private Canvas canvas;
	// 声明屏幕的宽高
	private static int screenW, screenH;

	// 定义游戏状态常量
	private static final int GAME_INIT = 0; // 游戏初始状态
	private static final int GAMEING = 1; // 游戏中
	private static final int GAME_OVER = -1; // 游戏结束
	// 当前游戏默认状态（默认为游戏初始状态）
	private static int gameState = GAME_INIT;

	// 底部水平线的坐标
	private int bottom_x,bottom_y;
	// 底部水平线的长度
	private int bottom_width = 15;
	// 滚动速度
	private int speed = 3;

	// 当前分数
	private int currentScore = 0;

	// 小球的坐标
	private int ball_x,ball_y;
	// 小球的宽度
	private int ball_width = 10;
	// 小球y轴默认高度
	private int ball_v;
	//没有触摸屏幕情况下，小球在y轴的降落高度
	private int ball_a = 2;
	// 每次触摸，小球在y轴的上升高度
	private int ball_vUp = -16;

	private ArrayList<int[]> walls = new ArrayList<int[]>();
	private ArrayList<int[]> remove_walls = new ArrayList<int[]>();
	private int wall_w = 50; // 添加墙的宽度
	private int wall_h = 100; // 添加墙的高度

	private int wall_step = 30;
	
	 // 声明一个MediaPlayer对象
	private MediaPlayer player;


	/**
	 * GameBirdSurfaceView初始化函数
	 */
	public MySurfaceView(Context context) {

		super(context);
		//实例SurfaceHolder
		sfh = this.getHolder();
		//为SurfaceHolder设置状态监听
		sfh.addCallback(this);
		//实例一个画笔
		paint = new Paint();
		//设置画笔颜色为白色
		paint.setColor(Color.WHITE);
		paint.setTextSize(36);
		paint.setStyle(Style.STROKE); 

		// 实例化MediaPlayer对象
		player = player.create(getContext(), R.drawable.bgm);
		// 设置循环播放
		player.setLooping(true);
		
		//设置焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	/**
	 * SurfaceView视图创建，响应此函数
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
	 * 初始化游戏的函数
	 */
	private void initGame() {

		if (gameState == GAME_INIT) {

			//为底部水平线坐标赋值
			bottom_x = 0;
			bottom_y = screenH-20;

			//默认当前得分为0
			currentScore = 0;
			
			//设置小球的坐标
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

	// dp转换为px
	private int dp2px(float dp) {
		int px = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, dp, getResources()
						.getDisplayMetrics()));
		return px;
	}

	/**
	 * 游戏绘图
	 */
	public void myDraw() {

		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				
				// 绘制小球
				canvas.drawCircle(ball_x, ball_y, ball_width, paint);
				
				switch (gameState) {
				case GAME_INIT:
					initGame();
					canvas.drawText("点击屏幕，开始游戏", screenW/3, screenH/3, paint);
					break;
				case GAMEING:
				case GAME_OVER:
					canvas.drawText("当前得分："+String.valueOf(currentScore), 10, 50, paint);
					// 绘制水平背景
					canvas.drawLine(bottom_x, bottom_y, screenW, bottom_y, paint);
					// 绘制墙阻碍
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
						canvas.drawLines(pts, paint); // 绘制多条直线
					}
					if(gameState==GAME_OVER){//游戏结束
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
	 * 触屏事件监听
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
				gameState = GAME_INIT;//回到初始状态
				break;
			}
		}
		return true;
	}


	private int move_step = 0;

	/**
	 * 游戏逻辑函数
	 */
	private void logic() {
		// 逻辑函数根据游戏状态不同进行不同监听
		switch (gameState) {
		case GAME_INIT:
			if(player.isPlaying()){
				player.pause(); // 停止播放音乐
			}
			break;
		case GAMEING:
			// 播放音乐
			if(!player.isPlaying()){
		    player.start();
			}
			ball_v += ball_a;
			ball_y += ball_v;
			//与地面的碰撞检测
			if (ball_y >bottom_y -ball_width) {
				ball_y =bottom_y - ball_width;
				gameState = GAME_OVER; //游戏状态切换为结束状态
			}

			if (bottom_x < -bottom_width) {
				bottom_x += bottom_width * 2;
			}
			bottom_x -= speed;

			// 与墙的碰撞检测
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
			// 超出屏幕的墙移除
			if (remove_walls.size() > 0) {
				walls.removeAll(remove_walls);
			}

			// 添加新的墙
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
				player.pause(); // 停止播放音乐
			}

			if (ball_y < bottom_y - ball_width) {
				ball_v += ball_a;
				ball_y += ball_v;
				if(ball_y >= bottom_y - ball_width){
					ball_y = bottom_y - ball_width;  //刚好让小球掉落在水平线上
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
	// 监控按键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			gameState=GAME_INIT;
		}
		return super.onKeyDown(keyCode, event);
}
	
}