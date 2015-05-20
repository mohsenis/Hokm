package gameplay;

import java.util.List;

import controller.Player;

public class State {
	private List<Card> onTable;
	private List<Card> inHand;
	private List<Card> played;
	private List<Player> playedByPlayers;
	
	private int teamScore;
	private int opponentScore;
	private SuitName hokm;
	
	public State(List<Card> onTable, List<Card> inHand, List<Card> played, List<Player> playedByPlayers,
			int teamScore, int opponentScore, SuitName hokm){
		this.onTable = onTable;
		this.inHand = inHand;
		this.played = played;
		this.playedByPlayers = playedByPlayers;
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
	
	public List<Player> getPlayedByPlayer(){
		return this.playedByPlayers;
	}
	
	public int getTeamScore(){
		return this.teamScore;
	}
	
	public int getOpponentScore(){
		return this.opponentScore;
	}
	
	public SuitName getHokm(){
		return this.hokm;
	}
}
