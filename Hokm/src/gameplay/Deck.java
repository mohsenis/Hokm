package gameplay;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	private List<Card> deck;
	
	public Deck(){
		deck = new ArrayList<>(52);
		Card card;
		
		for (SuitName suitName : SuitName.values()) {
			for(int i=2;i<=14;i++){
				card = new Card(suitName, i);
				deck.add(card);
			}
		}
	}
	
	public List<Card> getDeck(){
		return this.deck;
	}
}
