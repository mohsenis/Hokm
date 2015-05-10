package gameplay;

public class Card {
	final private int suit;
	final private int value;
	private SuitName suitName;
	
	public Card(int suit, int value){
		this.suit = suit;
		this.value = value;
		switch (suit){
		case 1:
			suitName = SuitName.Spade;
			break;
		case 2:
			suitName = SuitName.Heart;
			break;
		case 3:
			suitName = SuitName.Club;
			break;
		case 4:
			suitName = SuitName.Dimond;
			break;
		}
	}
	
	public Card(SuitName suitName, int value){
		this.suit = suitName.getSuit();
		this.value = value;
		this.suitName = suitName;
	}
	
	public int getSuit(){
		return this.suit;
	}
	
	public SuitName getSuitName(){
		return this.suitName;
	}
	
	public int getValue(){
		return this.value;
	} 
	
	
	
}
