package gameplay;

import java.util.ArrayList;
import java.util.List;

public class Deck {
	private List<Card> deck;
	
	public Deck(){
		deck = new ArrayList<>(52);
		Card card;
		
		for (SuitName suitName : SuitName.values()) {
			for(ValueName valueName : ValueName.values()){
				card = new Card(suitName, valueName);
				deck.add(card);
			}
		}
	}
	
	public List<Card> getDeck(){
		return this.deck;
	}
}
