package xo;

import main.*;

public class XO_Value extends Value {
	static int modelNumber = 1;

	int num;
	double value;
	double bestValue;
	boolean mark[];

	public XO_Value(int num, double value) {
		this.num = num;
		this.value = value;
		bestValue = value;
	}

	public XO_Value(int i, double d, boolean[] m) {
		this.num = i;
		this.value = d;
		bestValue = value;
		this.mark = m;
	}

	public int compareTo(XO_Value vv) {
		if (value < vv.value)
			return -1;
		else
			return 1;
	}

	public int compareToBest(XO_Value vv) {
		if (bestValue < vv.bestValue)
			return -1;
		else
			return 1;
	}

	public static XO_Value win() {
		return new XO_Value(-1, 1);
	}

	public static XO_Value lose() {
		return new XO_Value(-1, -1);
	}

	public static XO_Value draw() {
		return new XO_Value(-1, 0);
	}

	public int compareTo_UCT(XO_Value vv, int total_number) {
		double u1 = value + Math.sqrt(2 * Math.log(total_number) / num);
		double u2 = vv.value + Math.sqrt(2 * Math.log(total_number) / vv.num);
		if (u1 < u2)
			return -1;
		else
			return 1;
	}

	@Override
	public String toString() {
		return "{num: " + num + ", value: " + value + ", maxValue: " + bestValue + "}";
	}
}