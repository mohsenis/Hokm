package gameplay;

import java.util.List;

public class State {
	private List<Card> onTable;
	private List<Card> inHand;
	private List<Card> played;
	
	private int teamScore;
	private int opponentScore;
	private SuitName hokm;
	
	public State(List<Card> onTable, List<Card> inHand, List<Card> played,
			int teamScore, int opponentScore, SuitName hokm){
		this.onTable = onTable;
		this.inHand = inHand;
		this.played = played;
		this.teamScore = teamScore;
		this.opponentScore = opponentScore;
		this.hokm = hokm;
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
	
	public SuitName getHokm(){
		return this.hokm;
	}
}
