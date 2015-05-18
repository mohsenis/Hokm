package gameplay;

public enum SuitName {
	Dimonds  (4),
	Clubs    (3),
    Hearts   (2),
    Spades   (1),
    ;

    private final int suit;

    SuitName(int suit) {
        this.suit = suit;
    }
    
    public int getSuit() {
        return this.suit;
    }
}