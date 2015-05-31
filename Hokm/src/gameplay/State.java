package gameplay;

import java.util.ArrayList;
import java.util.List;

import controller.Player;

public class State {
	private List<Card> onTable;
	private List<Card> inHand;
	private List<Card> played;
	private List<Player> playedBy;
	private CardDist cardsDist;
	
	private int teamScore;
	private int opponentScore;
	private SuitName hokm;
	
	public State(List<Card> onTable, List<Card> inHand, List<Card> played, List<Player> playedBy, CardDist cardDist,
			int teamScore, int opponentScore, SuitName hokm){
		this.onTable = onTable;
		this.inHand = inHand;
		this.played = played;
		this.playedBy = playedBy;
		this.cardsDist = cardDist;
		this.teamScore = teamScore;
		this.opponentScore = opponentScore;
		this.hokm = hokm;
	}
	
	public State(State other){
		this.onTable = new ArrayList<Card>();
		this.inHand = new ArrayList<Card>();
		this.played = new ArrayList<Card>();
		this.playedBy = new ArrayList<Player>();
		this.cardsDist = new CardDist(other.getCardDist());
		for(Card c: other.getOnTable()){
			this.onTable.add(c);
		}
		for(Card c: other.getInHand()){
			this.inHand.add(c);
		}
		for(Card c: other.getPlayed()){
			this.played.add(c);
		}
		for(Player p: other.getPlayedBy()){
			this.playedBy.add(p);
		}
		this.teamScore=other.getTeamScore();
		this.opponentScore=other.getOpponentScore();
		this.hokm = other.getHokm();
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
	
	public List<Player> getPlayedBy(){
		return this.playedBy;
	}
	
	public CardDist getCardDist(){
		return this.cardsDist;
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
	
	public void updateTeamScore(){
		this.teamScore++;
	}
	
	public void updateOpponentScore(){
		this.opponentScore++;
	}
	
}
