package dcmagamcts;

import main.Game;

public class DCMAGA_Game extends Game {

	@Override
	public void init() {
		myState = new DCMAGA_State("dcmagamcts\\testcase-1.txt");
	}

}
