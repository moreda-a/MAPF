package dcmagamcts;

import main.*;

public class DCMAGA_Action extends Action {

	public DCMAGA_Action(int x, int y, int c) {
		this.x = x;
		this.y = y;
		color = c;
	}

	public int x;
	public int y;
	public int color;
}
