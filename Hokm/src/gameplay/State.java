package gameplay;

import java.util.List;

public class State {
	private List<Card> onTable;
	private List<Card> inHand;
	private List<Card> played;
	
	private int teamScore;
	private int opponentScore;
	
	public State(List<Card> onTable, List<Card> inHand, List<Card> played,
			int team1Score, int team2Score){
		this.onTable = onTable;
		this.inHand = inHand;
		this.played = played;
		this.teamScore = team1Score;
		this.opponentScore = team2Score;
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
		return this.teamScore;
	}
	
	public int getTeam2Score(){
		return this.opponentScore;
	}
}
