package ai;

import gameplay.Card;
import gameplay.CardValue;
import gameplay.Deck;
import gameplay.State;
import gameplay.SuitName;
import gameplay.Game;

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
	
	class MyPredicate<T> implements Predicate<Card> {
		Card card;
		Player player;

		public MyPredicate(Player player) {
			this.player = player;
		}

		public boolean test(Card card) {
			return !player.getSuitStatus(card.getSuitName());
		}
	}

	public static Card takeAction(List<Card> legalActions, State state,
			List<Player> players, Player currentPlayer) {
		int need = findNeed(state.getTeamScore(),state.getOpponentScore());
		// List<Card> possibleActions()
		return null;
	}

	/* returns card absolute value */
	private static double cardAbsValue(Card card) {
		double val = Math.pow(card.getValue() / 14, 2);
		return val;
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

	// returns the likelihood of taking the trick with a specific card
	private static int likelihood(Card myCard, State state,List<Player> players,Player player,
			SuitName hokm, CardValue cardValue){
		int L=3;

		SuitName firstSuit=state.getOnTable().get(0).getSuitName(); // SuitName of the first card on table
		Card op1Card;
		Card op2Card;
		Card mateCard;
		
		Player op2;
		Player mate;
		
		switch (players.indexOf(player)){
		case 0:
			
			break;
		case 1:
			op1Card=state.getOnTable().get(1);	// The opponent player who has laid a card just before the me
			op2=players.get(2);
			mate=players.get(3);
			
			if (firstSuit==hokm){
				if (myCard.getSuitName()==hokm){
					
				}else{
					
				}
				
			}else{
				if (myCard.getSuitName()==firstSuit){
					
				}else if (myCard.getSuitName()==hokm){
					
				}else{
					
				}
			}
			
			break;
		case 2:
			op1Card=state.getOnTable().get(1);	// The opponent player who has laid a card just before the me
//			Card op2Card=state.getOnTable().get(3);
			mateCard=state.getOnTable().get(0);	// Teammate's card
			
			
			if (firstSuit==hokm){
				if (op1Card.getSuitName()==hokm){
					if (myCard.getSuitName()==hokm){
						if (players.get(3).getSuitStatus(firstSuit)){
							
							if (cardValue.getValue(myCard)==14
								|| cardValue.getValue(mateCard)==14)
								L=4;
							
							if ((myCard.getValue()>op1Card.getValue()
									&& cardValue.getValue(myCard)>10)
									|| (mateCard.getValue()>op1Card.getValue()
									&& cardValue.getValue(mateCard)<10))
								L=2;
							
							if (op1Card.getValue()>myCard.getValue()
									&& op1Card.getValue()>mateCard.getValue())
								L=1;
							
						}else{
							
							if (myCard.getValue()>op1Card.getValue()
									|| mateCard.getValue()>op1Card.getValue())
								L=4;
								if (myCard.getValue()<op1Card.getValue()
										&& mateCard.getValue()<op1Card.getValue())
									L=1;
								
						}
					}else{
						if (players.get(3).getSuitStatus(firstSuit)){
							
							if (cardValue.getValue(mateCard)==14)
								L=4;
							else if (mateCard.getValue()<op1Card.getValue())
								L=1;
							else if (cardValue.getValue(mateCard)<10)
								L=2;
						}else{
							if (mateCard.getValue()>op1Card.getValue())
								L=4;
							else
								L=1;
							
						}
					}
					
				}else{
					if (myCard.getSuitName()==hokm){
						if (players.get(3).getSuitStatus(firstSuit)){
							
							if (cardValue.getValue(mateCard)==14
									|| cardValue.getValue(myCard)==14)
								L=4;
							else if (cardValue.getValue(mateCard)<10
									&& cardValue.getValue(myCard)<10)
								L=2;
								
						}else{
							L=4;
						}
					}else{
						if (players.get(3).getSuitStatus(firstSuit)){
							
							if (cardValue.getValue(mateCard)==14)
								L=4;
							else if(cardValue.getValue(mateCard)<10)
								L=2;
							
						}else{
							L=4;
						}
					}
				}
			}else{
				if (op1Card.getSuitName()==firstSuit){
					if (myCard.getSuitName()==firstSuit){
						
						if (cardValue.getValue(op1Card)==14)
							L=4;							
							else if (cardValue.getValue(myCard)==14
								|| cardValue.getValue(mateCard)==14)
							L=4;
							else if (myCard.getValue()>op1Card.getValue()
								|| mateCard.getValue()>op1Card.getValue()
								&& players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==false)
							L=4;
						else if (op1Card.getValue()>myCard.getValue()
								&& op1Card.getValue()>mateCard.getValue())
							L=1;
						else if (players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==true)
							L=1;
						else if ((myCard.getValue()>op1Card.getValue()
								|| mateCard.getValue()>op1Card.getValue())
								&& (cardValue.getValue(myCard)<10
										&& cardValue.getValue(mateCard)<10))
							L=2;
								
					}else if (myCard.getSuitName()==hokm){
						
						if (players.get(3).getSuitStatus(firstSuit)==true)
						L=4;
						
					}else{
						
						if (op1Card.getValue()>mateCard.getValue())
							L=1;
						else if (op1Card.getValue()<mateCard.getValue()
								&& players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==false)
							L=4;
						else if (op1Card.getValue()<mateCard.getValue()
								&& cardValue.getValue(mateCard)<10)
							L=2;
						
					}
					
				}else if (op1Card.getSuitName()==hokm){
					if (myCard.getSuitName()==firstSuit){
						
						L=1;
						
					}else if (myCard.getSuitName()==hokm){
						
						if (myCard.getValue()>op1Card.getValue()
								&& players.get(3).getSuitStatus(firstSuit)==true)
							L=4;
						else if (myCard.getValue()<op1Card.getValue())
							L=1;
						else if (myCard.getValue()>op1Card.getValue()
								&& players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==true)
							L=2;
						
					}else{
						
						L=1;
					}
				}else{
					if (myCard.getSuitName()==firstSuit){
						
						if (players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==false)
							L=4;
						else if (players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==true)
						L=1;
						else if (cardValue.getValue(myCard)==14
								|| cardValue.getValue(mateCard)==14)
							L=4;
						else if (cardValue.getValue(myCard)<10
								&& cardValue.getValue(myCard)<10)
							L=2;
						
					}else if (myCard.getSuitName()==hokm){
						
						if (players.get(3).getSuitStatus(firstSuit)==true)
							L=4;
						else if (players.get(3).getSuitStatus(hokm)==false)
							L=4;
						else if (players.get(3).getSuitStatus(hokm)==true
								&& cardValue.getValue(myCard)<10)
							L=2;
						
					}else{
						
						if (players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==false)
							L=4;
						else if (players.get(3).getSuitStatus(firstSuit)==false
								&& players.get(3).getSuitStatus(hokm)==true)
							L=1;
						else if (players.get(3).getSuitStatus(firstSuit)==true
								&& cardValue.getValue(mateCard)<10)
							L=2;
						
					}
				}
			}
		
			break;
		case 3:
			List<Card> tempTable=state.getOnTable();
			tempTable.add(myCard);
			Player winner=Game.detWinner(players, tempTable,hokm);
			
			if (winner==player.getTeam().getPlayer1() || winner==player.getTeam().getPlayer2())
				L=4;
			else
				L=1;
			break;
		}
		return L;
	}


}
