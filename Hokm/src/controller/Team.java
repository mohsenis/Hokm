package controller;

public class Team {
	private int trickScore;
	private int totalScore;
	private final Player player1;
	private final Player player2;
	
	public Team(Player player1, Player player2){
		this.trickScore=0;
		this.totalScore=0;
		this.player1 = player1;
		this.player2 = player2;
	}
	
	public Player getPlayer1(){
		return this.player1;
	}
	
	public Player getPlayer2(){
		return this.player2;
	}
	
	public int getTrickScore(){
		return this.trickScore;
	}
	
	public void updateTrickScore(){
		this.trickScore ++;
	}
	
	public int getTotalScore(){
		return this.totalScore;
	}
	
	public void updateTotalScore(){
		this.totalScore ++;
	}
	
}
