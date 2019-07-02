package spf;

import main.*;

public class SPF_Game extends Game {

	@Override
	public void init() {
		Centralized = true;
		// myState = new SPF_State();
		myState = new SPF_State("testcase10.txt");
	}

}
