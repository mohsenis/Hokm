package ai;

import gameplay.Card;
import gameplay.Deck;
import gameplay.State;

import java.util.List;
import java.util.function.Predicate;

import controller.Player;

public class AI {
	private AI(){
		
	}
	
	private List<Card> possibleActions(State state, Player player) {
		List<Card> remainingCards = new Deck().getDeck();
		remainingCards.removeAll(state.getPlayed());
		remainingCards.removeAll(state.getInHand());

		Predicate<Card> filter = new MyPredicate<Card>(player);
		remainingCards.removeIf(filter);
		return remainingCards;
	}

	public static Card takeAction(List<Card> legalActions, State state,
			List<Player> players, Player currentPlayer) {
		int need = findNeed(state.getTeamScore(),state.getOpponentScore());
		// List<Card> possibleActions()
		return null;

	}
	
	private static int findNeed(int trick1, int trick2){
		int need = 1;
		if(trick2==6){
			need=2;
		}else if(trick2==5 && trick1<5){
			need=2;
		}else if(trick2==4 && trick1<3){
			need=2;
		}else if(trick2==3 && trick1<1){
			need=2;
		}
		return need;
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
