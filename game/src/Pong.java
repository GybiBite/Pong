
import java.util.Random;

import ext_bot.ExtensionBot;
import processing.core.PApplet;
import processing.core.PFont;

public class Pong extends PApplet {

	/**
	 * Pong game with one and two player modes, including a simplistic AI for
	 * one player mode.
	 * 
	 * by Daniel DeMarco
	 * 
	 * V1.3
	 */

// TODO: Maybe fix/add comments?

// Settings
	static final float BOARDER_OFFSET = 25; // How far away the paddles are from the side of the screen (25)
	static final float PADDLE_THICKNESS = 5; // How thick the paddles are (10)
	static final float ACCELERATION = 0.2f; // How fast the paddle accelerates (0.2)
	static final float BALL_SIZE = 15; // How wide the ball diameter is (15)
	static final float SPEED_INCREMENT = 0.8f; // How fast the ball speeds up every time it hits a paddle (0.8)

	static int gameLength = 3; // How long the game lasts, in terms of points (3)
	static float paddleHeight = 75; // How long the paddles are (75)
	static int cpuDifficulty = 3; // How difficult the one-player mode is, out of 6 (3)

	static float leftPY;
	static float rightPY;
	static float rSpeed;
	static float lSpeed;
	static float[] bot = new float[2];
	static boolean upPressed, downPressed, wPressed, sPressed, inGame, cpuPlayer, wait, botLoaded;
	static int p1Score, p2Score, countdown, menuLevel, gameState, winner;
	PFont font;
	String player;
	Random bool = new Random();

	String[] settingPL = { "Short", "Medium", "Long" }; // Paddle length names
	String[] settingBD = { "Easy", "Normal", "Hard", "Very Hard", "Expert" }; // Difficulty names
	
	Ball ball = new Ball(BALL_SIZE); // Init ball class

	Button[] menuLv0 = new Button[3];
	Button[] menuLv1 = new Button[7];

	/**
	 * The setup method is called once at the start of the program
	 */
	public void setup() {
		try {
			font = createFont("FSEX300.ttf", 128, true); // Initialize custom font;
			textFont(font); // Set custom font as active
			println("Loaded font object " + font);
		} catch (Exception e) {
			println("Encountered " + e + " while trying to load font");
		}
		
		botLoaded = ExtHandler.loadBot();
		
		menuLv0[0] = new Button(this, 400, 425, 150, 40, "1 Player", "startGame", 1);
		menuLv0[1] = new Button(this, 400, 475, 150, 40, "2 Player", "startGame", 0);
		menuLv0[2] = new Button(this, 400, 525, 150, 40, "Options", "menuToggle", 1);

		menuLv1[0] = new Button(this, 400, 255, 40, 30, "←", "paddleLength", -30);
		menuLv1[1] = new Button(this, 575, 255, 40, 30, "→", "paddleLength", 30);
		menuLv1[2] = new Button(this, 365, 305, 40, 30, "←", "gameLength", -1);
		menuLv1[3] = new Button(this, 445, 305, 40, 30, "→", "gameLength", 1);
		menuLv1[4] = new Button(this, 415, 355, 40, 30, "←", "cpuDifficulty", -1);
		menuLv1[5] = new Button(this, 620, 355, 40, 30, "→", "cpuDifficulty", 1);
		menuLv1[6] = new Button(this, 400, 545, 330, 45, "← Back to main menu", "menuToggle", -1);
		
		if(!botLoaded) {
			menuLv0[0].setEnabled(false);
			menuLv1[4].setEnabled(false);
			menuLv1[5].setEnabled(false);
		}

		gameInit();
	}

	/**
	 * The draw method is called as per the frame rate, typically 60 times a second.
	 */
	public void draw() {

		if (wait == true) {
			// If wait is true, triggers a pause
			delay(1000);
			wait = false;
		}
		if (gameState == 3) {
			// If the game has ended, wait and then return to the menu
			delay(4000);
			inGame = false;
			setState(0);
		}

		background(0);

		if (gameState == 0) {
			// If the game is still at the menu, run the menu sequence
			menu();
		}

		if (inGame) {
			if (gameState == 2) {
				paddleMove(); // Update player input information

				ball.updatePos(); // Update ball location
				renderObjects(); // Draw in-game objects
			}
			if (countdown == 0)
				setState(2);
			if (gameState == 1) {

				if (countdown == 0)
					setState(2);
				renderObjects();
				fill(255);
				text(countdown, width / 2, height / 2);
				countdown--;
				wait = true;

				if (bool.nextBoolean())
					// Randomize starting direction of the ball
					Ball.dy = 2;
				else
					Ball.dy = -2;
				if (bool.nextBoolean())
					Ball.dx = 2;
				else
					Ball.dx = -2;
				Ball.bx = width / 2;
				Ball.by = height / 2;
			}

			if (Ball.bx + BALL_SIZE >= width) {
				// If the ball reaches the boarder, increment the score of the respective player
				p1Score++;
				if (p1Score < gameLength) {
					background(0);
					renderObjects();
					// wait = true;
					setState(1);
				}
			}
			if (Ball.bx - BALL_SIZE <= 0) {
				p2Score++;
				if (p2Score < gameLength) {
					background(0);
					renderObjects();
					// wait = true;
					setState(1);
				}
			}
			if (p2Score == gameLength) {
				if (cpuPlayer)
					winner = 2;
				else
					winner = 1;
				setState(3);
			}
			if (p1Score == gameLength) {
				winner = 0;
				setState(3);
			}
		}
	}

	public void gameInit() {
		leftPY = height / 2; // Center left paddle
		rightPY = height / 2; // Center right paddle
		Ball.resetPos();
		p1Score = 0;
		p2Score = 0;
	}

	/**
	 * Valid states - 0: Menu, 1: Round intermission (Count down), 2: In game, 3:
	 * Game results (Winner display)
	 */
	void setState(int state) {

		switch (state) {
		case 0:
			gameState = 0;
			inGame = false;
			gameInit();
			break;

		case 1:
			gameState = 1;
			fill(150);
			countdown = 3;
			renderObjects();
			break;

		case 2:
			gameState = 2;
			break;

		case 3:
			gameState = 3;
			background(0);
			fill(150);
			renderObjects();
			textSize(64);
			fill(255);
			if (winner == 0)
				player = "Player one";
			else if (winner == 1)
				player = "Player two";
			else
				player = "CPU player";

			text(player + " wins!", width / 2, height / 2);
			break;
		}
	}

	public void menu() {
		background(0);
		stroke(255);
		fill(255);
		strokeWeight(3);
		if (menuLevel == 0) {
			textAlign(CENTER, CENTER);
			textSize(128);
			text("PONG", width / 2, height / 2 - 50);
			textSize(32);
			fill(255);
			noFill();

			for (Button b : menuLv0) {
				b.drawButton();
			}
		}

		if (menuLevel == 1) {
			textAlign(LEFT, CENTER); // Make sure text aligns to center

			textSize(32);
			text("Paddle length:", 150, height / 2 - 50);
			text("Game length:", 150, height / 2);
			text("CPU difficulty:", 150, height / 2 + 50);

			textAlign(CENTER, CENTER);
			text(settingPL[((int) paddleHeight - 44) / 30], 485, height / 2 - 50);
			text(gameLength, 407, height / 2);
			if (cpuDifficulty == 6)
				fill(255, 0, 0);
			text(settingBD[cpuDifficulty - 2], 515, height / 2 + 50);
			fill(255);
			noFill();

			for (Button b : menuLv1) {
				b.drawButton();
			}
		}
	}

	public void keyPressed() {
		// if(keyCode == 192) Console key (`)
		if (keyCode == UP)
			upPressed = true; // If the UP key is pressed, set the corresponding variable
		if (keyCode == DOWN)
			downPressed = true; // Same for DOWN key
		if (keyCode == 'W')
			wPressed = true; // W key
		if (keyCode == 'S')
			sPressed = true; // S key

	}

	public void keyReleased() { // This method is the same as above, but checks if the keys *aren't* being held
								// down
		if (keyCode == UP)
			upPressed = false;
		if (keyCode == DOWN)
			downPressed = false;
		if (keyCode == 'W')
			wPressed = false;
		if (keyCode == 'S')
			sPressed = false;
	}

	public void mousePressed() {
		if (!inGame) {
			switch (menuLevel) {
			case 0:
				for (Button b : menuLv0) {
					b.checkClick();
				}
				break;

			case 1:
				for (Button b : menuLv1) {
					b.checkClick();
				}
				break;
			}
		}
	}

	public void paddleMove() {
		if (!cpuPlayer) {
			if (upPressed & !downPressed) { // if (player is holding up and not down)
				rightPY = max(paddleHeight / 2, rightPY - rSpeed); // set right paddle to move up, or stop at the height
																	// limit
				rSpeed += ACCELERATION; // increase paddle speed
			} else if (downPressed & !upPressed) { // if (player is holding down and not up)
				rightPY = min(height - paddleHeight / 2, rightPY + rSpeed); // set right paddle to move down, or stop at
																			// the height limit
				rSpeed += ACCELERATION; // increase paddle speed
			} else if (downPressed & upPressed) { // if (player is holding both keys)
				rSpeed = 0; // set speed to zero
			} else
				rSpeed = 0; // if the player isn't holding any keys, reset speed
		} else { // ===================
			if(botLoaded) {
				bot = ExtensionBot.doMove(Ball.dx, Ball.by, rightPY, cpuDifficulty, paddleHeight, ACCELERATION, rSpeed, height);
				rightPY = bot[0];
				rSpeed = bot[1];
			}
		} // ===============

		// For the next code block, all previous comments apply but for the left paddle
		// with keys W and S

		if (wPressed & !sPressed) {
			leftPY = max(paddleHeight / 2, leftPY - lSpeed);
			lSpeed += ACCELERATION;
		} else if (sPressed & !wPressed) {
			leftPY = min(height - paddleHeight / 2, leftPY + lSpeed);
			lSpeed += ACCELERATION;
		} else if (sPressed & wPressed) {
			lSpeed = 0;
		} else
			lSpeed = 0;
	}

	public void renderObjects() {
		background(0);
		fill(255);
		stroke(255);
		if (gameState != 2) {
			fill(150);
			stroke(150);
		}
		if (cpuDifficulty == 6 && cpuPlayer)
			fill(255, 0, 0);
		rect(width - BOARDER_OFFSET - PADDLE_THICKNESS, rightPY - (paddleHeight / 2), PADDLE_THICKNESS, paddleHeight); // Draw
																														// right
																														// paddle
		if (gameState != 2)
			fill(150);
		else
			fill(255);
		rect(BOARDER_OFFSET, leftPY - (paddleHeight / 2), PADDLE_THICKNESS, paddleHeight); // Draw left paddle
		circle(Ball.bx, Ball.by, BALL_SIZE); // Draw ball
		textSize(128);
		text(p1Score, width / 2 - 200, 50); // Draw player one's score (left)
		text(p2Score, width / 2 + 200, 50); // Draw player two's score (right)
	}

	public void settings() {
		size(800, 600);
	}

	static public void main(String[] passedArgs) {
		String[] appletArgs = new String[] { "Pong" };
		if (passedArgs != null) {
			PApplet.main(concat(appletArgs, passedArgs));
		} else {
			PApplet.main(appletArgs);
		}
	}
}
