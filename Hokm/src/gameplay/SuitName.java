package gameplay;

public enum SuitName {
	Diamonds (3),
	Clubs    (2),
    Hearts   (1),
    Spades   (0),
    ;

    private final int suit;

    SuitName(int suit) {
        this.suit = suit;
    }
    
    public int getSuit() {
        return this.suit;
    }
}