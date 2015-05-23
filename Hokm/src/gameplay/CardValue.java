package gameplay;

import java.util.ArrayList;

public class CardValue {
	ArrayList<Card> spades;
	ArrayList<Card> diamonds;
	ArrayList<Card> hearts;
	ArrayList<Card> clubs;

	public CardValue() {
		for (ValueName value : ValueName.values()) {
			spades.add(new Card(SuitName.Spade, value));
			diamonds.add(new Card(SuitName.Dimond, value));
			hearts.add(new Card(SuitName.Heart, value));
			clubs.add(new Card(SuitName.Club, value));
		}
	}

	public void updateValue(Card card) {
		int s = card.getSuit();
		switch (s) {
		case 0:
			int p0 = this.spades.indexOf(card);
			for (int i = (p0 - 1); i >= 0; i--) {
				this.spades.set(i + 1, this.spades.get(i));
			}
			this.spades.set(0, card);
			break;
		case 1:
			int p1 = this.hearts.indexOf(card);
			for (int i = (p1 - 1); i >= 0; i--) {
				this.hearts.set(i + 1, this.hearts.get(i));
			}
			this.hearts.set(0, card);
			break;
		case 2:
			int p2 = this.clubs.indexOf(card);
			for (int i = (p2 - 1); i >= 0; i--) {
				this.clubs.set(i + 1, this.clubs.get(i));
			}
			this.clubs.set(0, card);
			break;
		case 3:
			this.diamonds.remove(card);
			int p3 = this.diamonds.indexOf(card);
			for (int i = (p3 - 1); i >= 0; i--) {
				this.diamonds.set(i + 1, this.diamonds.get(i));
			}
			this.diamonds.set(0, card);
			break;
		}
	}

	public int getValue(Card card) {
		int s = card.getSuit();
		int val;
		switch (s) {
		case 0:
			val = this.spades.indexOf(card);
			break;
		case 1:
			val = this.hearts.indexOf(card);
			break;
		case 2:
			val = this.clubs.indexOf(card);
			break;
		case 3:
			val = this.diamonds.indexOf(card);
			break;
		default:
			val = 0;
		}
		return val;
	}
}
