import processing.core.PApplet;

public class Ball extends PApplet {

	static float bx;
	static float by;
	static float dx;
	static float dy;
	static float size;
	static float width = 800;
	static float height = 600;

	Ball(float size) {
		Ball.size = size;
	}

	public static void resetPos() {
		bx = width / 2; // Center ball
		by = height / 2; // Center ball
	}

	public void updatePos() {
		detectCollide();

		if (dx < 0)
			dx = Pong.max(-60, dx);
		if (dx > 0)
			dx = Pong.min(60, dx);

		by += dy; // Update ball Y coordinate based on velocity
		bx += dx; // Update ball X coordinate based on velocity

	}

	public void detectCollide() {
		if (by <= size / 2 & dy < 0)
			dy = -dy; // If the ball hits the top of the screen make it bounce
		if (by >= height - size / 2 & dy > 0)
			dy = -dy; // If the ball hits the bottom of the screen make it bounce

		// Calculate each pixel-wise intermediate position of ballX and ballY
		if ((by >= (Pong.leftPY - Pong.paddleHeight / 2 - size / 2)) & // If the ball is below the upper side of the
																		// left
																		// paddle
				(by <= (Pong.leftPY + Pong.paddleHeight / 2 + size / 2)) & // and above the lower side
				((bx + dx) <= (Pong.BOARDER_OFFSET + Pong.PADDLE_THICKNESS + size / 2)) & (dx < 0)) // and moving toward
																									// the left paddle
		{
			bx += dx;
			dx = -dx;
			if (dx < 0)
				dx -= Pong.SPEED_INCREMENT;
			else
				dx += Pong.SPEED_INCREMENT;
			float angleDelta = by - Pong.leftPY;
			dy = 1 * angleDelta / 10; // Set the ball vertical velocity to the angleDelta
		}

		// The if statement below does the exact same thing as above, but for the right
		// paddle

		if ((by >= (Pong.rightPY - Pong.paddleHeight / 2 - size / 2))
				& (by <= (Pong.rightPY + Pong.paddleHeight / 2 + size / 2))
				& ((bx + dx) >= (width - Pong.BOARDER_OFFSET - Pong.PADDLE_THICKNESS - size / 2)) & (dx > 0)) {
			bx += dx;
			dx = -dx;
			if (dx < 0)
				dx -= Pong.SPEED_INCREMENT;
			else
				dx = +Pong.SPEED_INCREMENT;
			float angleDelta = by - Pong.rightPY;
			dy = 1 * angleDelta / 10;
		}

		if ((bx >= (width - Pong.BOARDER_OFFSET - Pong.PADDLE_THICKNESS - size / 2)) & // If the ball is further towards
																						// the right of the screen than
																						// the left boarder of the right
																						// paddle
				(bx <= (width - Pong.BOARDER_OFFSET))) { // and it's not behind the right paddle

			if ((by >= Pong.rightPY - Pong.paddleHeight / 2 - size / 2) & // If it's at the upper side of the right
																			// paddle
					(by < Pong.rightPY - Pong.paddleHeight / 2 - size / 2 + 40) & // and not farther than 20 pixels into
																					// the paddle
					(dy > 0)) // and the ball is moving down
			{
				dy = -dy; // bounce the ball up
			} else if ((by <= Pong.rightPY + Pong.paddleHeight / 2 + size / 2) & // or if it's at the bottom side of the
																					// right paddle
					(by >= Pong.rightPY + Pong.paddleHeight / 2 + size / 2 - 40) & // and not farther than 20 pixels
																					// into the paddle
					(dy < 0)) // and the ball is moving up
			{
				dy = -dy; // bounce the ball down
			}
		}

		// Below is identical to the chunk of code above, but it applies to the left
		// paddle

		if ((bx <= Pong.BOARDER_OFFSET + Pong.PADDLE_THICKNESS + size / 2) & (bx > Pong.BOARDER_OFFSET)) {

			if ((by >= Pong.leftPY - Pong.paddleHeight / 2 - size / 2)
					& (by < Pong.leftPY - Pong.paddleHeight / 2 - size / 2 + 40) & (dy > 0)) {
				dy = -dy;
			} else if ((by <= Pong.leftPY + Pong.paddleHeight / 2 + size / 2)
					& (by > Pong.leftPY + Pong.paddleHeight / 2 + size / 2 - 40) & (dy < 0)) {
				dy = -dy;
			}
		}
	}

	public String toString() {
		return "Ball X: " + bx + "\nBall Y: " + by + "\nBall Velocity X: " + dx + "\nBall Velocity Y: " + dy;
	}
}
