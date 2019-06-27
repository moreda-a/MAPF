package dcmagamcts;

import main.Game;

public class DCMAGA_Game extends Game {

	@Override
	public void init() {
		// myState = new SPF_State();
		myState = new DCMAGA_State("testcase10.txt");
	}

}
