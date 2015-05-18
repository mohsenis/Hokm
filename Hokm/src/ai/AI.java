package ai;

import gameplay.Card;
import gameplay.Deck;
import gameplay.State;

import java.util.List;
import java.util.function.Predicate;

import controller.Player;

public class AI {
	public List<Card> possibleActions(State state, Player player) {
		List<Card> remainingCards = new Deck().getDeck();
		remainingCards.removeAll(state.getPlayed());
		remainingCards.removeAll(state.getInHand());

		Predicate<Card> filter = new MyPredicate<Card>(player);
		remainingCards.removeIf(filter);
		return remainingCards;
	}

	public static Card takeAction(List<Card> legalActions, State state,
			List<Player> players, Player currentPlayer) {
		// List<Card> possibleActions()

		return null;

	}

	class MyPredicate<T> implements Predicate<Card> {
		Card card;
		Player player;

		public MyPredicate(Player player) {
			this.player = player;
		}

		public boolean test(Card card){
			return !player.getSuitStatus().get(card.getSuit());
		}
	}
}
