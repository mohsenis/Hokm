package gameplay;

import java.util.List;

public class State {
	private List<Card> onTable;
	private List<Card> inHand;
	private List<Card> played;
	
	private int team1Score;
	private int team2Score;
	
	public State(List<Card> onTable, List<Card> inHand, List<Card> played,
			int team1Score, int team2Score){
		this.onTable = onTable;
		this.inHand = inHand;
		this.played = played;
		this.team1Score = team1Score;
		this.team2Score = team2Score;
	}
	
	public List<Card> getOnTable(){
		return this.onTable;
	}
	
	public List<Card> getInHand(){
		return this.inHand;
	}
	
	public List<Card> getPlayed(){
		return this.played;
	}
	
	public int getTeam1Score(){
		return this.team1Score;
	}
	
	public int getTeam2Score(){
		return this.team2Score;
	}
}
