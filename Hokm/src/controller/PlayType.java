package controller;

public enum PlayType {
	regular(0), 
	cut(1), 
	pass(2)
	;

	private int type;

	PlayType(int type) {
		this.type = type;
	}
	
	public int getType(){
		return this.type;
	}
}
