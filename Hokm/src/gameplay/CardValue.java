package gameplay;

import java.util.ArrayList;
import java.util.List;

public class CardValue {
	private List<Card> spades;
	private ArrayList<Card> diamonds;
	private ArrayList<Card> hearts;
	private ArrayList<Card> clubs;

	public CardValue() {
		spades = new ArrayList<Card>();
		diamonds = new ArrayList<Card>();
		hearts = new ArrayList<Card>();
		clubs = new ArrayList<Card>();

		for (ValueName valueName : ValueName.values()) {
			spades.add(new Card(SuitName.Spades, valueName));
			hearts.add(new Card(SuitName.Hearts, valueName));
			clubs.add(new Card(SuitName.Clubs, valueName));
			diamonds.add(new Card(SuitName.Diamonds, valueName));
		}
	}

	public void updateValue(List<Card> onTable) {

		for (Card card : onTable) {
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
				int p3 = this.diamonds.indexOf(card);
				for (int i = (p3 - 1); i >= 0; i--) {
					this.diamonds.set(i + 1, this.diamonds.get(i));
				}
				this.diamonds.set(0, card);
				break;
			}
		}
	}

	/*
	 * private int indexOf(List<Card> cardList, Card myCard){ int out=-1;
	 * 
	 * for (int i=0;i<13;i++){ if
	 * (cardList.get(i).getValue()==myCard.getValue()) return i; } return out; }
	 */

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
