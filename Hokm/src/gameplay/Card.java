package gameplay;

public class Card implements Comparable<Card> {
	final private int suit;
	final private int value;
	private SuitName suitName;
	private ValueName valueName;
	
	public Card(SuitName suitName, ValueName valueName){
		this.suit = suitName.getSuit();
		this.value = valueName.getValue();
		this.suitName = suitName;
		this.valueName = valueName;
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
	
	public ValueName getValueName(){
		return this.valueName;
	} 
	
	public String toString(){
		 //this.getValueName()+" of "+this.getSuitName();
		 return String.format("%-5s of %s", this.getValueName(), this.getSuitName());
	}

	@Override
	public int compareTo(Card another) {
		if(this.suit==another.suit && this.value==another.value)
			return 0;
		else if(this.suit!=another.suit)
			return this.suit - another.suit;
		else
			return this.value - another.value;
	}
	
	@Override
	public boolean equals(Object other){
		if (!(other instanceof Card)) return false;
		
	    Card another = (Card) other;
		if(this.suit==another.getSuit() && this.value==another.getValue())
			return true;
		else
			return false;
	}
	
}
