package gameplay;

public enum SuitName {
	Dimond  (4),
	Club  (3),
    Heart (2),
    Spade   (1),
    ;

    private final int suit;

    SuitName(int suit) {
        this.suit = suit;
    }
    
    public int getSuit() {
        return this.suit;
    }
}