package xo;

import main.*;

public class XO_Game extends Game {

	@Override
	public void init() {
		Centralized = true;
		myState = new XO_State();
	}

}
