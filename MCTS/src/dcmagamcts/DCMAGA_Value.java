package dcmagamcts;

import main.*;

public class DCMAGA_Value extends Value {
	static int modelNumber = 1;

	int num;
	double value;
	double bestValue;
	boolean mark[];

	public DCMAGA_Value(int num, double value) {
		this.num = num;
		this.value = value;
		bestValue = value;
	}

	public DCMAGA_Value(int i, double d, boolean[] m) {
		this.num = i;
		this.value = d;
		bestValue = value;
		this.mark = m;
	}

	public int compareTo(DCMAGA_Value vv) {
		if (value < vv.value)
			return -1;
		else
			return 1;
	}

	public int compareToBest(DCMAGA_Value vv) {
		if (bestValue < vv.bestValue)
			return -1;
		else
			return 1;
	}

	public int compareTo_UCT(DCMAGA_Value vv, int total_number) {
		double u1 = value + Math.sqrt(2 * Math.log(total_number) / num);
		double u2 = vv.value + Math.sqrt(2 * Math.log(total_number) / vv.num);
		if (u1 < u2)
			return -1;
		else
			return 1;
	}

	@Override
	public DCMAGA_Value update(State state, Value simulationResult) {
		DCMAGA_State st = (DCMAGA_State) state;
		DCMAGA_Value simulation_result = (DCMAGA_Value) simulationResult;
		++num;
		bestValue = Math.max(value, simulation_result.value - (st.lastColor != -1
				? simulation_result.mark[st.lastColor] ? (double) (3 - modelNumber / 2) * (1 / st.playerNumber) : 0
				: 0));
		switch (modelNumber) {
		case 1:
		case 2:
		case 3:
			value = (value * (num - 1) + simulation_result.value
					- (st.lastColor != -1
							? simulation_result.mark[st.lastColor]
									? (double) (1.5 - modelNumber / 2) * (1 / st.playerNumber)
									: 0
							: 0))
					/ num;
			break;
		case 4:
		case 5:
		case 6:
			value = bestValue;
			break;
		default:
			break;
		}
		return this;
	}

	@Override
	public String toString() {
		return "{num: " + num + ", value: " + value + ", maxValue: " + bestValue + "}";
	}
}
