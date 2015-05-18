package gameplay;

import java.util.ArrayList;
import java.util.List;

import controller.PlayType;
import controller.Player;

public class Played {

	private List<Card> cards;
	private List<Player> players;
	private List<List<Card>> onTable;
	private List<PlayType> playTypes;
	
	public Played() {
		this.cards=new ArrayList<Card>();
		this.players=new ArrayList<Player>();
		this.playTypes=new ArrayList<PlayType>();
		this.onTable= new ArrayList<List<Card>>();
	}
	
	public List<Card> getCard(){
		return this.cards;
	}
	
	public void addCard(Card card){
		this.cards.add(card);
	}
	
	public List<Player> getPlayers(){
		return this.players;
	}
	
	public void addPlayer(Player player){
		this.players.add(player);
	}
	
	public List<List<Card>> getOnTable(){
		return this.onTable;
	}
	
	public void addOnTable(List<Card> onTable){
		this.onTable.add(onTable);
	}
	
	public List<PlayType> getPlayType(){
		return this.playTypes;
	}
	
	public void addPlayType(SuitName hokm){
		/*Card card = this.cards.get(this.cards.size()-1);
		if (card.getSuitName()==this.onTable.get(this.onTable.size()-1).get(0).getSuitName()){
			this.playTypes.add(Played);
		}*/
		
	}
}
