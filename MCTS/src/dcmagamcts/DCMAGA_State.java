package dcmagamcts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import main.*;

public class DCMAGA_State extends State {

	boolean hashed;
	int hash;

	@Override
	public int hashCode() {
		return hash = (hashed ? hash
				: 31 * Objects.hash(parent, nextColor) + (table == null ? 0 : Arrays.deepHashCode(table)));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj.getClass() != DCMAGA_State.class)
			return false;
		DCMAGA_State st = (DCMAGA_State) obj;

		boolean res = nextColor == st.nextColor & (parent == null ? st.parent == null : parent.equals(st.parent));
		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j)
				if (table[i][j] != st.table[i][j])
					res = false;
		return res;
	}

	public DCMAGA_Board board;
	public int table[][];
	// public int size;
	public int height;
	public int width;
	public int playerNumber;
	public int goalNumber;
	public int nextColor;
	public int lastColor;

	public int myNumber = -1;

	public PII[] lastMove;
	public PII[] target;

	class PII {
		public PII(int i, int j) {
			first = i;
			second = j;
		}

		public Integer first;
		public Integer second;

		@Override
		public String toString() {
			return "{" + first + ", " + second + "}";
		}
	}

	@Override
	public void reset() {
		super.reset();
		lastColor = -1;
	}

	public DCMAGA_State(DCMAGA_State st, DCMAGA_Action act) {
		width = st.width;
		height = st.height;
		playerNumber = st.playerNumber;
		table = new int[width][height];
		lastMove = new PII[playerNumber + 1];
		target = new PII[playerNumber + 1];
		for (int i = 1; i <= playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j)
				table[i][j] = st.table[i][j];
		table[act.y][act.x] = act.color;
		lastMove[act.color] = new PII(act.y, act.x);
		parent = st;
		lastColor = st.nextColor;
		setNextColor();
		depth = st.depth + 1;
	}

	private void setNextColor() {
		double best = 1000000;
		int bestColor = lastColor % playerNumber + 1;
		for (int i = 1; i <= playerNumber; ++i) {
			nextColor = (lastColor + i - 1) % playerNumber + 1;
			int cn = childNumber();
			// int cnt = childNumberTarget();
			if (isNear(nextColor) || cn == 0)
				continue;
			if (cn == 1) {
				bestColor = nextColor;
				break;
			}
//			if (cnt == 1) {
//				bestColor = nextColor;
//				PII temp = lastMove[nextColor];
//				lastMove[nextColor] = target[nextColor];
//				target[nextColor] = temp;
//				break;
//			}
			// double gn = cn - dis(nextColor) / size * 2;
			if (best > cn) {
				best = cn;
				bestColor = nextColor;
			} else if (best == cn) {
				// if (dis(nextColor) > dis(bestColor))
				// bestColor = nextColor;

			}
		}
		nextColor = bestColor;
	}

	private int dis(int color) {
		return Math.abs(lastMove[color].first - target[color].first)
				+ Math.abs(lastMove[color].second - target[color].second);
	}

	private boolean isNear(int color) {
		// return Math.abs(lastMove[color].first - target[color].first)
		// + Math.abs(lastMove[color].second - target[color].second) == 1;
		return table[lastMove[color].first][lastMove[color].first] <= 0;
	}

	public DCMAGA_State() {
		// TODO wtf is this ? ? ? ? ?=)))))))))
		// int size = 5;
		int playerNumber = 5;
		this.playerNumber = playerNumber;
		this.width = 5;
		this.height = 5;
		table = new int[width][height];
		lastMove = new PII[playerNumber + 1];
		target = new PII[playerNumber + 1];
		for (int i = 1; i <= playerNumber; ++i) {
			lastMove[i] = null;
			target[i] = null;
		}
		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j)
				table[i][j] = 0;
//		table[0][1] = 1;
//		table[1][0] = 1;
//		table[0][2] = 2;
//		table[2][2] = 2;
//		lastMove[1] = new PII(0, 1);
//		lastMove[2] = new PII(0, 2);
//		target[1] = new PII(1, 0);
//		target[2] = new PII(2, 2);
		// #testcase2:
		table[0][0] = 1;
		table[2][1] = 1;
		table[1][0] = 2;
		table[3][2] = 2;
		table[2][2] = 3;
		table[0][4] = 3;
		table[1][3] = 4;
		table[4][4] = 4;
		table[4][0] = 5;
		table[2][3] = 5;
		lastMove[1] = new PII(0, 0);
		target[1] = new PII(2, 1);
		lastMove[2] = new PII(1, 0);
		target[2] = new PII(3, 2);
		lastMove[3] = new PII(2, 2);
		target[3] = new PII(0, 4);
		lastMove[4] = new PII(1, 3);
		target[4] = new PII(4, 4);
		lastMove[5] = new PII(4, 0);
		target[5] = new PII(2, 3);
		lastColor = -1;
		nextColor = 1;
//		do {
//			nextColor = nextColor % playerNumber + 1;
//		} while (DCMAGA_Simulator.getActionsx(this).isEmpty() && nextColor != st.nextColor);
		parent = null;
	}

	public DCMAGA_State(String str) {
		File file = new File("testcase\\" + str);
		try {
			Scanner sc;
			if (Main.systemInput)
				sc = new Scanner(System.in);
			else
				sc = new Scanner(file);
			width = sc.nextInt();
			height = sc.nextInt();
			playerNumber = sc.nextInt();
			goalNumber = sc.nextInt();
			table = new int[width][height];
			lastMove = new PII[playerNumber + 1];
			target = new PII[playerNumber + 1];
			for (int i = 1; i <= playerNumber; ++i) {
				lastMove[i] = null;
				target[i] = null;
			}
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j) {
					table[i][j] = sc.nextInt();
					if (table[i][j] != 0)
						if (lastMove[table[i][j]] == null)
							lastMove[table[i][j]] = new PII(i, j);
					// else
					// target[table[i][j]] = new PII(i, j);
				}
			lastColor = playerNumber;
			setNextColor();
			parent = null;
			lastColor = -1;
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public DCMAGA_State(String str, int mynum) {
		File file = new File("testcase\\" + str);
		try {
			Scanner sc;
			if (Main.systemInput)
				sc = new Scanner(System.in);
			else
				sc = new Scanner(file);
			width = sc.nextInt();
			height = sc.nextInt();
			playerNumber = sc.nextInt();
			goalNumber = sc.nextInt();
			table = new int[width][height];
			lastMove = new PII[playerNumber + 1];
			target = new PII[playerNumber + 1];
			for (int i = 1; i <= playerNumber; ++i) {
				lastMove[i] = null;
				target[i] = null;
			}
			for (int i = 0; i < width; ++i)
				for (int j = 0; j < height; ++j) {
					table[i][j] = sc.nextInt();
					if (table[i][j] != 0 && table[i][j] != -1)
						if (lastMove[table[i][j]] == null)
							lastMove[table[i][j]] = new PII(i, j);
					// else
					// target[table[i][j]] = new PII(i, j);
				}
			lastColor = playerNumber;
			setNextColor();
			parent = null;
			lastColor = -1;
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		myNumber = mynum;
	}

	public DCMAGA_State(State[] agentState, State[] gg) {
		DCMAGA_State st = (DCMAGA_State) agentState[1];
		width = st.width;
		height = st.height;
		playerNumber = st.playerNumber;
		table = new int[width][height];
		lastMove = new PII[playerNumber + 1];
		target = new PII[playerNumber + 1];
		for (int i = 1; i <= playerNumber; ++i) {
			lastMove[i] = st.lastMove[i];
			target[i] = st.target[i];
		}
		for (int i = 0; i < width; ++i)
			for (int j = 0; j < height; ++j)
				table[i][j] = st.table[i][j];
		for (int i = 1; i <= playerNumber; ++i) {
			int help = table[((DCMAGA_State) gg[i]).lastMove[i].first][((DCMAGA_State) gg[i]).lastMove[i].second];
			if (((DCMAGA_State) gg[i]).lastMove[i] != lastMove[i] && help == 0 || help == -1) {
				table[lastMove[i].first][lastMove[i].second] = 0;
				table[((DCMAGA_State) gg[i]).lastMove[i].first][((DCMAGA_State) gg[i]).lastMove[i].second] = help == -1
						? -i - 1
						: i;
				lastMove[i] = ((DCMAGA_State) gg[i]).lastMove[i];
			}
		}
		// table[act.y][act.x] = act.color;
		// lastMove[act.color] = new PII(act.y, act.x);
		parent = st;
		lastColor = st.nextColor;
		setNextColor();
		depth = st.depth + 1;
	}

	@Override
	public boolean isNotTerminal() {
		int res = 0;
		for (int i = 1; i <= playerNumber; ++i)
			res += isNear(i) ? 1 : 0;
		if (res == playerNumber)
			return false;
		if (!hasChild())
			return false;
		return true;
	}

	@Override
	public Value getValue() {
		if (isNotTerminal())
			return null;
		double res = 0;
		boolean m[] = new boolean[playerNumber + 1];
		for (int i = 1; i <= playerNumber; ++i) {
			res += isNear(i) ? 1 : 0;
			// res -= isNear(i) ? (double) depth / (2 * size * size) : 0;
			m[i] = isNear(i);
		}

		return new DCMAGA_Value(-1, res / playerNumber, m);
	}

	@Override
	public String toString() {
		String s = "{";
		for (int i = 0; i < width; ++i) {
			for (int j = 0; j < height; ++j)
				s += table[i][j] + ", ";
			s += (i != width - 1 ? "\n " : "");
		}
		return s + "}";
	}

	@Override
	public ArrayList<State> refreshChilds() {
		ArrayList<State> childss = new ArrayList<State>();
		for (int i = -1; i < 2; ++i)
			for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
				if (lastMove[nextColor].first + i >= 0 && lastMove[nextColor].first + i < width
						&& lastMove[nextColor].second + j >= 0 && lastMove[nextColor].second + j < height
						&& table[lastMove[nextColor].first + i][lastMove[nextColor].second + j] == 0)
					childss.add(DCMAGA_Simulator.simulateX(this, new DCMAGA_Action(lastMove[nextColor].second + j,
							lastMove[nextColor].first + i, nextColor)));
		return childss;
	}

	private boolean hasChild() {
		for (int i = -1; i < 2; ++i)
			for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
				if (lastMove[nextColor].first + i >= 0 && lastMove[nextColor].first + i < width
						&& lastMove[nextColor].second + j >= 0 && lastMove[nextColor].second + j < height
						&& table[lastMove[nextColor].first + i][lastMove[nextColor].second + j] == 0)
					return true;
		return false;
	}

	private int childNumber() {
		// System.out.println(lastMove[1]);
		int ans = 0;
		for (int i = -1; i < 2; ++i)
			for (int j = (i == 0 ? -1 : 0); j < (i == 0 ? 2 : 1); ++j)
				if (lastMove[nextColor].first + i >= 0 && lastMove[nextColor].first + i < width
						&& lastMove[nextColor].second + j >= 0 && lastMove[nextColor].second + j < height
						&& table[lastMove[nextColor].first + i][lastMove[nextColor].second + j] == 0)
					++ans;
		return ans;
	}

//	private int childNumberTarget() {
//		PII temp = lastMove[nextColor];
//		lastMove[nextColor] = target[nextColor];
//		target[nextColor] = temp;
//		// int res = childNumber();
//		temp = lastMove[nextColor];
//		lastMove[nextColor] = target[nextColor];
//		target[nextColor] = temp;
//		return res;
//	}
}
