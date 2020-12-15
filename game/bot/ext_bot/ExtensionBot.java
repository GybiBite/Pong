package ext_bot;

import java.util.Random;

public class ExtensionBot {

	static float[] array = new float[2];

	public static void check() {
		System.out.println("Loaded extension \"1 player mode\"");
	}

	public static float[] doMove(float ballDX, float ballY, float paddle, int difficulty, float paddleHeight,
			float acceleration, float speed, float height) {
		Random chance = new Random();

		if (ballY < paddle | (chance.nextInt(10) > (difficulty * 2) & chance.nextBoolean())) {
			paddle = Math.max(paddleHeight / 2, paddle - speed);
			if (difficulty == 6)
				speed = Math.min(speed + acceleration, 5);
			else
				speed = Math.min(speed + acceleration, 4);

		} else if (ballY > paddle | (chance.nextInt(10) > (difficulty * 2) & chance.nextBoolean())) {
			paddle = Math.min(height - paddleHeight / 2, paddle + speed);
			if (difficulty == 6)
				speed = Math.min(speed + acceleration, 5);
			else
				speed = Math.min(speed + acceleration, 4);

		} else if (ballY == paddle) {
			speed = 0;
		} else
			speed = 0;

		if (ballDX < 0)
			speed = 0;

		array[0] = paddle;
		array[1] = speed;

		return array;
	}
}