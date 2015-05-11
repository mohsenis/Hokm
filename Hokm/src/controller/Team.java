package controller;

public class Team {
	private int trickScore;
	private int totalScore;
	private final Player teammate;
	
	public Team(Player teammate){
		this.trickScore=0;
		this.totalScore=0;
		this.teammate = teammate;
	}
	
	public Player getTeammate(){
		return this.teammate;
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
