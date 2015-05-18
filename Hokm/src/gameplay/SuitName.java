package gameplay;

public enum SuitName {
	Dimond  (3),
	Club    (2),
    Heart   (1),
    Spade   (0),
    ;

    private final int suit;

    SuitName(int suit) {
        this.suit = suit;
    }
    
    public int getSuit() {
        return this.suit;
    }
}