package com.sunix.game.paperplane;

public class SpeedControler {
	
	public static final int FLY_ANGLE_LEFT3 = 1;
	public static final int FLY_ANGLE_LEFT2 = 2;
	public static final int FLY_ANGLE_LEFT1 = 3;
	public static final int FLY_ANGLE_MID = 4;
	public static final int FLY_ANGLE_RIGHT1 = 5;
	public static final int FLY_ANGLE_RIGHT2 = 6;
	public static final int FLY_ANGLE_RIGHT3 = 7;
	
	public static final float FLY_SPEED_LEFT3 = -3f;
	public static final float FLY_SPEED_LEFT2 = -2f;
	public static final float FLY_SPEED_LEFT1 = -1f;
	public static final float FLY_SPEED_MID = 0f;
	public static final float FLY_SPEED_RIGHT1 = 1f;
	public static final float FLY_SPEED_RIGHT2 = 2f;
	public static final float FLY_SPEED_RIGHT3 = 3f;
	
	public static float FALL_SPEED3 = 1f;
	public static float FALL_SPEED2 = 3f;
	public static float FALL_SPEED1 = 5f;
	public static float FALL_SPEED0 = 7f;
	
	public static final float BASE_FALL_SPEED3 = FALL_SPEED3;
	public static final float BASE_FALL_SPEED2 = FALL_SPEED2;
	public static final float BASE_FALL_SPEED1 = FALL_SPEED1;
	public static final float BASE_FALL_SPEED0 = FALL_SPEED0;
	
	public static final float FALL_SPEED_LEVEL2 = 1.15f;
	public static final float FALL_SPEED_LEVEL3 = 1.25f;
	public static final float FALL_SPEED_LEVEL4 = 1.35f;
	
	public static int sAngle = FLY_ANGLE_RIGHT3;
	
	public static float sFallSpeed = FALL_SPEED0;
	
	public static float getFlySpeed(int angle) {
		sAngle = angle;
		float speed = 0;
		switch (angle) {
		case FLY_ANGLE_LEFT3:
			speed = FLY_SPEED_LEFT3;
			sFallSpeed = FALL_SPEED3;
			break;
		case FLY_ANGLE_LEFT2:
			speed = FLY_SPEED_LEFT2;
			sFallSpeed = FALL_SPEED2;
			break;
		case FLY_ANGLE_LEFT1:
			speed = FLY_SPEED_LEFT1;
			sFallSpeed = FALL_SPEED1;
			break;
		case FLY_ANGLE_MID:
			speed = FLY_SPEED_MID;
			sFallSpeed = FALL_SPEED0;
			break;
		case FLY_ANGLE_RIGHT1:
			speed = FLY_SPEED_RIGHT1;
			sFallSpeed = FALL_SPEED1;
			break;
		case FLY_ANGLE_RIGHT2:
			speed = FLY_SPEED_RIGHT2;
			sFallSpeed = FALL_SPEED2;
			break;
		case FLY_ANGLE_RIGHT3:
			speed = FLY_SPEED_RIGHT3;
			sFallSpeed = FALL_SPEED3;
			break;
		default:
			break;
		}
		return speed;
	}
	
	public static float getFallSpeed() {
		return sFallSpeed;
	}
	
	public static void resetSpeed() {
		sFallSpeed = FALL_SPEED0;
	}
	
	public static void setLevel(int level) {
		switch (level) {
		case 1:
			FALL_SPEED3 = BASE_FALL_SPEED3 * 1f;
			FALL_SPEED2 = BASE_FALL_SPEED2 * 1f;
			FALL_SPEED1 = BASE_FALL_SPEED1 * 1f;
			FALL_SPEED0 = BASE_FALL_SPEED0 * 1f;
			break;
		case 2:
			FALL_SPEED3 = BASE_FALL_SPEED3 * FALL_SPEED_LEVEL2;
			FALL_SPEED2 = BASE_FALL_SPEED2 * FALL_SPEED_LEVEL2;
			FALL_SPEED1 = BASE_FALL_SPEED1 * FALL_SPEED_LEVEL2;
			FALL_SPEED0 = BASE_FALL_SPEED0 * FALL_SPEED_LEVEL2;
			break;
		case 3:
			FALL_SPEED3 = BASE_FALL_SPEED3 * FALL_SPEED_LEVEL3;
			FALL_SPEED2 = BASE_FALL_SPEED2 * FALL_SPEED_LEVEL3;
			FALL_SPEED1 = BASE_FALL_SPEED1 * FALL_SPEED_LEVEL3;
			FALL_SPEED0 = BASE_FALL_SPEED0 * FALL_SPEED_LEVEL3;
			break;
		case 4:
			FALL_SPEED3 = BASE_FALL_SPEED3 * FALL_SPEED_LEVEL4;
			FALL_SPEED2 = BASE_FALL_SPEED2 * FALL_SPEED_LEVEL4;
			FALL_SPEED1 = BASE_FALL_SPEED1 * FALL_SPEED_LEVEL4;
			FALL_SPEED0 = BASE_FALL_SPEED0 * FALL_SPEED_LEVEL4;
			break;
		default:
			break;
		}
		
		// 更新下载速度
		getFlySpeed(sAngle);
	}
}
