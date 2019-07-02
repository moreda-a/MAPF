package dcmagamcts;

import main.*;

public class DCMAGA_Game extends Game {

	public State[] agentState;

	@Override
	public void init() {
		Centralized = false;
		myState = new DCMAGA_State("dcmagamcts\\testcase-1.txt", 1);
		DCMAGA_State ms = (DCMAGA_State) myState;
		agentState = new State[ms.playerNumber + 1];
		agentState[1] = myState;
		for (int i = 2; i <= ms.playerNumber; ++i) {
			agentState[i] = new DCMAGA_State("dcmagamcts\\testcase-1.txt", i);
		}
	}

}