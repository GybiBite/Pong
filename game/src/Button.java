
import processing.core.PApplet;
import processing.core.PConstants;

public class Button {

	private Pong p;

	/** Center X position of the button */
	float x;
	/** Center Y position of the button */
	float y;
	/** Width of the button, in pixels */
	float bWidth;
	/** Height of the button, in pixels */
	float bHeight;
	/** Display text for the button */
	String buttonText;
	/** What the button does (see constructor for details) */
	String buttonAction;
	/** Does the button raise or lower the value (if applicable) */
	int switchType;

	boolean enabled = true;

	/** Defines a new button object centered at x and y */
	public Button(Pong p, float x, float y, float bWidth, float bHeight, String buttonText, String buttonAction,
			int switchType) {

		this.p = p;
		this.x = x;
		this.y = y;
		this.bWidth = bWidth;
		this.bHeight = bHeight;
		this.buttonText = buttonText;
		this.buttonAction = buttonAction;
		this.switchType = switchType;
	}

	void drawButton() {
		p.fill(150);
		if (enabled) {
			p.fill(255);
			hoverOver();
		}

		p.text(buttonText, x, y - 5);

	}

	public void checkClick() {
		if (enabled) {
			if (hoverOver()) {
				System.out.println(this + " clicked!");
				if (buttonAction.contentEquals("menuToggle")) {
					Pong.menuLevel += switchType;
				}

				if (buttonAction.contentEquals("paddleLength")) {
					Pong.paddleHeight = PApplet.max(45, PApplet.min(Pong.paddleHeight + switchType, 105));
				}

				if (buttonAction.contentEquals("gameLength")) {
					Pong.gameLength = PApplet.max(1, PApplet.min(Pong.gameLength + switchType, 10));
				}

				if (buttonAction.contentEquals("cpuDifficulty")) {
					Pong.cpuDifficulty = PApplet.max(2, PApplet.min(Pong.cpuDifficulty + switchType, 6));
				}

				if (buttonAction.contentEquals("startGame")) {
					Pong.inGame = true;
					Pong.cpuPlayer = (switchType == 1);
					p.setState(1);
				}
			}
		}
	}

	public boolean hoverOver() {
		if (PApplet.max(x - bWidth / 2, p.mouseX) == PApplet.min(p.mouseX, x + bWidth / 2)
				&& PApplet.max(y - bHeight / 2, p.mouseY) == PApplet.min(p.mouseY, y + bHeight / 2)) {
			p.noFill();
			p.rectMode(PConstants.CENTER);
			p.rect(x, y, bWidth, bHeight);
			p.rectMode(PConstants.CORNER);
			return true;
		} else
			return false;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public String toString() {
		return "Button \"" + buttonText + "\" at " + x + ", " + y;
	}
}