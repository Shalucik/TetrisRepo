package com.youngcapital.tetris.complete.websocket;

public class MoveMessage extends Message {
		private int x;
		private int y;
		private boolean move;
		
		public boolean isMove() {
			return move;
		}

		public void setMove(boolean move) {
			this.move = move;
		}

		public MoveMessage() {}

		public MoveMessage(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}
		
		
}
