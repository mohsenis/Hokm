package ai;

import java.util.List;

import org.apache.commons.math3.exception.ZeroException;

import controller.Player;
import gameplay.Card;
import gameplay.CardValue;
import gameplay.Game;
import gameplay.GameBuilder;
import gameplay.State;
import gameplay.SuitName;
import gameplay.ValueName;

public class LookTree {

	private LookTree() {
	}

/*	public static double lookTree(int Card oCard, State oldState,
			List<Player> players, CardValue oldCardValue, Player oPlayer) {
		double actionReward = 0;
		switch (oldState.getOnTable().size()) {
		case 0:
			actionReward = LookTree.case0(oCard, oldState, players,
					oldCardValue, oPlayer);
			break;

		case 1:
			actionReward = LookTree.case1(oCard, oldState, players,
					oldCardValue, oPlayer);
			break;

		case 2:
			actionReward = LookTree.case2(oCard, oldState, players,
					oldCardValue, oPlayer);
			break;

		case 3:
			actionReward = LookTree.case3(oCard, oldState, players,
					oldCardValue, oPlayer);
			break;
		}

		return actionReward;
	}
*/
	public static double case0(Card oCard, State oldState,
			List<Player> players, CardValue oldCardValue, Player oPlayer, int horizon) {
		double actionReward = 0;
		double r1, r2, r3, r4;
		Card c1, c2, c3, c4;
		double pr1, pr2, pr3;
		CardValue cv = new CardValue(oldCardValue);
		SuitName hokm = oldState.getHokm();
		SuitName firstSuit = oCard.getSuitName();
		State newState = AI.getNewState(oldState, oCard, oPlayer, players);
		Player op1 = players.get(1);

		if (firstSuit == hokm) {
			AI.track.add(1);
			pr1 = newState.getCardDist().prGreater(oCard, op1);
			c1 = newState.getCardDist().smallestGreater(oCard);
			r1 = case1(c1, newState, players, cv, op1, horizon);
			pr2 = newState.getCardDist().prLess(oCard, op1);
			c2 = newState.getCardDist().smallestLess(oCard);
			r2 = case1(c2, newState, players, cv, op1, horizon);
			c3 = newState.getCardDist().smallestSuits(
					AI.getOtherSuits(new SuitName[] { hokm }), op1);
			r3 = case1(c3, newState, players, cv, op1, horizon);

			actionReward = pr1 * r1 + (1 - pr1) * ((pr2 * r2) + (1 - pr2) * r3);
		} else {
			AI.track.add(2);
			pr1 = newState.getCardDist().prGreater(oCard, op1);
			c1 = newState.getCardDist().smallestGreater(oCard);
			r1 = case1(c1, newState, players, cv, op1, horizon);
			pr2 = newState.getCardDist().prLess(oCard, op1);
			c2 = newState.getCardDist().smallestLess(oCard);
			r2 = case1(c2, newState, players, cv, op1, horizon);
			pr3 = newState.getCardDist().prSuit(hokm, op1);
			c3 = newState.getCardDist().smallestSuit(hokm);
			r3 = case1(c3, newState, players, cv, op1, horizon);
			c4 = newState.getCardDist().smallestSuits(
					AI.getOtherSuits(new SuitName[] { hokm, firstSuit }), op1);
			r4 = case1(c4, newState, players, cv, op1, horizon);

			actionReward = pr1 * r1 + (1 - pr1)
					* (pr2 * r2 + (1 - pr2) * (pr3 * r3 + (1 - pr3) * r4));
		}

		return actionReward;
	}

	public static double case1(Card oCard, State oldState,
			List<Player> players, CardValue oldCardValue, Player oPlayer, int horizon) {
		double actionReward = 0;
		double r1, r2, r3, r4;
		Card c1, c2, c3, c4;
		double pr1, pr2, pr3;
		CardValue cv = new CardValue(oldCardValue);
		SuitName hokm = oldState.getHokm();
		SuitName firstSuit = oldState.getOnTable().get(0).getSuitName();
		State newState = AI.getNewState(oldState, oCard, oPlayer, players);
		Player op1 = players.get(0);
		Player op2 = players.get(2);
		Player winner = Game.detWinner(players, newState.getOnTable(), hokm);
		Card bestCardOnTable = newState.getOnTable().get(
				players.indexOf(winner));

		if (winner == op1) {
			Card betterCard = new Card(hokm,
					ValueName.getValueName(bestCardOnTable.getValue() + 3));
			if (firstSuit == hokm) {
				if (cv.getValue(bestCardOnTable) <= 9) {
					AI.track.add(3);
					pr1 = newState.getCardDist().prGreater(betterCard, op2);
					c1 = newState.getCardDist().smallestGreater(betterCard);
					r1 = case2(c1, newState, players, cv, op2, horizon);
					pr2 = newState.getCardDist().prLess(betterCard, op2);
					c2 = newState.getCardDist().smallestLess(betterCard);
					r2 = case2(c2, newState, players, cv, op2, horizon);
					c3 = newState.getCardDist().smallestSuits(
							AI.getOtherSuits(new SuitName[] { hokm }), op2);
					r3 = case2(c3, newState, players, cv, op2, horizon);

					actionReward = pr1 * r1 + (1 - pr1)
							* ((pr2 * r2) + (1 - pr2) * r3);
				} else {
					AI.track.add(4);
					pr1 = newState.getCardDist().prSuit(hokm, op2);
					c1 = newState.getCardDist().smallestSuit(hokm);
					r1 = case2(c1, newState, players, cv, op2, horizon);
					c2 = newState.getCardDist().smallestSuits(
							AI.getOtherSuits(new SuitName[] { hokm }), op2);
					r2 = case2(c1, newState, players, cv, op2, horizon);

					actionReward = (pr1 * r1 + (1 - pr1) * r2);
				}
			} else {
				if (cv.getValue(bestCardOnTable) <= 9) {
					AI.track.add(5);
					pr1 = newState.getCardDist().prGreater(betterCard, op2);
					c1 = newState.getCardDist().smallestGreater(betterCard);
					r1 = case2(c1, newState, players, cv, op2, horizon);
					pr2 = newState.getCardDist().prLess(betterCard, op2);
					c2 = newState.getCardDist().smallestLess(betterCard);
					r2 = case2(c2, newState, players, cv, op2, horizon);
					pr3 = newState.getCardDist().prSuit(hokm, op2);
					c3 = newState.getCardDist().smallestSuit(hokm);
					r3 = case2(c3, newState, players, cv, op2, horizon);
					c4 = newState.getCardDist()
							.smallestSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					r4 = case2(c4, newState, players, cv, op2, horizon);

					actionReward = pr1
							* r1
							+ (1 - pr1)
							* (pr2 * r2 + (1 - pr2)
									* (pr3 * r3 + (1 - pr3) * r4));
				} else {
					AI.track.add(6);
					pr1 = newState.getCardDist().prSuit(firstSuit, op2);
					c1 = newState.getCardDist().smallestSuit(firstSuit);
					r1 = case2(c1, newState, players, cv, op2, horizon);
					pr2 = newState.getCardDist()
							.prSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					c2 = newState.getCardDist()
							.smallestSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					r2 = case2(c2, newState, players, cv, op2, horizon);
					c3 = newState.getCardDist().smallestSuit(hokm);
					r3 = case2(c3, newState, players, cv, op2, horizon);

					actionReward = pr1 * r1 + (1 - pr1)
							* ((pr2 * r2) + (1 - pr2) * r3);
				}
			}
		} else {
			if (firstSuit == hokm) {
				AI.track.add(7);
				pr1 = newState.getCardDist().prGreater(bestCardOnTable, op2);
				c1 = newState.getCardDist().smallestGreater(bestCardOnTable);
				r1 = case2(c1, newState, players, cv, op2, horizon);
				pr2 = newState.getCardDist().prLess(bestCardOnTable, op2);
				c2 = newState.getCardDist().smallestLess(bestCardOnTable);
				r2 = case2(c2, newState, players, cv, op2, horizon);
				c3 = newState.getCardDist().smallestSuits(
						AI.getOtherSuits(new SuitName[] { hokm }), op2);
				r3 = case2(c3, newState, players, cv, op2, horizon);

				actionReward = pr1 * r1 + (1 - pr1)
						* ((pr2 * r2) + (1 - pr2) * r3);
			} else {
				if (bestCardOnTable.getSuitName() == firstSuit) {
					AI.track.add(8);
					pr1 = newState.getCardDist()
							.prGreater(bestCardOnTable, op2);
					c1 = newState.getCardDist()
							.smallestGreater(bestCardOnTable);
					r1 = case2(c1, newState, players, cv, op2, horizon);
					pr2 = newState.getCardDist().prLess(bestCardOnTable, op2);
					c2 = newState.getCardDist().smallestLess(bestCardOnTable);
					r2 = case2(c2, newState, players, cv, op2, horizon);
					pr3 = newState.getCardDist().prSuit(hokm, op2);
					c3 = newState.getCardDist().smallestSuit(hokm);
					r3 = case2(c3, newState, players, cv, op2, horizon);
					c4 = newState.getCardDist()
							.smallestSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					r4 = case2(c4, newState, players, cv, op2, horizon);

					actionReward = pr1
							* r1
							+ (1 - pr1)
							* (pr2 * r2 + (1 - pr2)
									* (pr3 * r3 + (1 - pr3) * r4));
				} else {
					AI.track.add(9);
					pr1 = newState.getCardDist().prSuit(firstSuit, op2);
					c1 = newState.getCardDist().smallestSuit(firstSuit);
					r1 = case2(c1, newState, players, cv, op2, horizon);
					pr2 = newState.getCardDist()
							.prGreater(bestCardOnTable, op2);
					c2 = newState.getCardDist()
							.smallestGreater(bestCardOnTable);
					r2 = case2(c2, newState, players, cv, op2, horizon);
					pr3 = newState.getCardDist()
							.prSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					c3 = newState.getCardDist()
							.smallestSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					r3 = case2(c3, newState, players, cv, op2, horizon);
					c4 = newState.getCardDist().smallestSuit(hokm);
					r4 = case2(c4, newState, players, cv, op2, horizon);

					actionReward = pr1
							* r1
							+ (1 - pr1)
							* (pr2 * r2 + (1 - pr2)
									* (pr3 * r3 + (1 - pr3) * r4));
				}

			}
		}

		return actionReward;
	}

	public static double case2(Card oCard, State oldState,
			List<Player> players, CardValue oldCardValue, Player oPlayer, int horizon) {

		double r1, r2, r3, r4;
		Card c1, c2, c3, c4;
		double pr1, pr2, pr3;

		CardValue cv = new CardValue(oldCardValue);
		SuitName hokm = oldState.getHokm();
		SuitName firstSuit = oldState.getOnTable().get(0).getSuitName();

		double actionReward = 0;
		State newState = AI.getNewState(oldState, oCard, oPlayer, players);

		Player op1 = players.get(1);
		Player op2 = players.get(3);

		Player winner = Game.detWinner(players, newState.getOnTable(), hokm);
		Card bestCardOnTable = newState.getOnTable().get(
				players.indexOf(winner));

		if (winner == op1) {
			if (firstSuit == hokm) {
				AI.track.add(10);
				pr1 = newState.getCardDist().prSuit(hokm, op2);
				c1 = newState.getCardDist().smallestSuit(hokm);
				r1 = case3(c1, newState, players, cv, op2, horizon);
				c2 = newState.getCardDist().smallestSuits(
						AI.getOtherSuits(new SuitName[] { hokm }), op2);
				r2 = case3(c2, newState, players, cv, op2,horizon);

				actionReward = (pr1 * r1 + (1 - pr1) * r2);

			} else {
				AI.track.add(11);
				pr1 = newState.getCardDist().prSuit(firstSuit, op2);
				c1 = newState.getCardDist().smallestSuit(firstSuit);
				r1 = case3(c1, newState, players, cv, op2, horizon);
				pr2 = newState.getCardDist().prSuits(
						AI.getOtherSuits(new SuitName[] { hokm, firstSuit }),
						op2);
				c2 = newState.getCardDist().smallestSuits(
						AI.getOtherSuits(new SuitName[] { hokm, firstSuit }),
						op2);
				r2 = case3(c2, newState, players, cv, op2, horizon);
				c3 = newState.getCardDist().smallestSuit(hokm);
				r3 = case3(c3, newState, players, cv, op2, horizon);

				actionReward = pr1 * r1 + (1 - pr1)
						* ((pr2 * r2) + (1 - pr2) * r3);
			}
		} else {
			if (firstSuit == hokm) {
				AI.track.add(12);
				pr1 = newState.getCardDist().prGreater(bestCardOnTable, op2);
				c1 = newState.getCardDist().smallestGreater(bestCardOnTable);
				r1 = case3(c1, newState, players, cv, op2, horizon);
				pr2 = newState.getCardDist().prLess(bestCardOnTable, op2);
				c2 = newState.getCardDist().smallestLess(bestCardOnTable);
				r2 = case3(c2, newState, players, cv, op2, horizon);
				c3 = newState.getCardDist().smallestSuits(
						AI.getOtherSuits(new SuitName[] { hokm }), op2);
				r3 = case3(c3, newState, players, cv, op2, horizon);

				actionReward = pr1 * r1 + (1 - pr1)
						* ((pr2 * r2) + (1 - pr2) * r3);
			} else {
				if (bestCardOnTable.getSuitName() == firstSuit) {
					AI.track.add(13);
					pr1 = newState.getCardDist()
							.prGreater(bestCardOnTable, op2);
					c1 = newState.getCardDist()
							.smallestGreater(bestCardOnTable);
					r1 = case3(c1, newState, players, cv, op2, horizon);
					pr2 = newState.getCardDist().prLess(bestCardOnTable, op2);
					c2 = newState.getCardDist().smallestLess(bestCardOnTable);
					r2 = case3(c2, newState, players, cv, op2, horizon);
					pr3 = newState.getCardDist().prSuit(hokm, op2);
					c3 = newState.getCardDist().smallestSuit(hokm);
					r3 = case3(c3, newState, players, cv, op2, horizon);
					c4 = newState.getCardDist()
							.smallestSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					r4 = case3(c4, newState, players, cv, op2, horizon);

					actionReward = pr1
							* r1
							+ (1 - pr1)
							* (pr2 * r2 + (1 - pr2)
									* (pr3 * r3 + (1 - pr3) * r4));
				} else {
					AI.track.add(14);
					pr1 = newState.getCardDist().prSuit(firstSuit, op2);
					c1 = newState.getCardDist().smallestSuit(firstSuit);
					r1 = case3(c1, newState, players, cv, op2, horizon);
					pr2 = newState.getCardDist()
							.prGreater(bestCardOnTable, op2);
					c2 = newState.getCardDist()
							.smallestGreater(bestCardOnTable);
					r2 = case3(c2, newState, players, cv, op2, horizon);
					pr3 = newState.getCardDist()
							.prSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					c3 = newState.getCardDist()
							.smallestSuits(
									AI.getOtherSuits(new SuitName[] { hokm,
											firstSuit }), op2);
					r3 = case3(c3, newState, players, cv, op2, horizon);
					c4 = newState.getCardDist().smallestSuit(hokm);
					r4 = case3(c4, newState, players, cv, op2, horizon);

					actionReward = pr1
							* r1
							+ (1 - pr1)
							* (pr2 * r2 + (1 - pr2)
									* (pr3 * r3 + (1 - pr3) * r4));
				}
			}
		}
		return actionReward;
	}

	public static double case3 (Card oCard, State oldState,
			List<Player> players, CardValue oldCardValue, Player oPlayer, int horizon) {
		AI.track.add(15);
		
		CardValue cv = new CardValue(oldCardValue);
		SuitName hokm = oldState.getHokm();

		double actionReward = 0;
		State newState = AI.getNewState(oldState, oCard, oPlayer, players);
		cv.updateValue(newState.getOnTable());

		Player winner = Game.detWinner(players, newState.getOnTable(), hokm);
		/*boolean win=false;
		boolean self=false;*/
		if (winner == AI.staticPlayer.getTeam().getPlayer1()
				|| winner == AI.staticPlayer.getTeam().getPlayer2()) {
			newState.updateTeamScore();
			/*win=true;
			
			if(winner == AI.staticPlayer){
				self = true;
			}*/
		} else {
			newState.updateOpponentScore();
		}
		List<Card> possibleMoves;
		if (winner == AI.staticPlayer)
			possibleMoves = newState.getInHand();
		else
			possibleMoves = newState.getCardDist().possibleActions(winner);

		players = GameBuilder.reorder(players, winner);
		newState.getOnTable().clear();
		
		while (horizon > 1) {
			double tmoReward = 0;
			horizon--;
			for (Card card : possibleMoves) {
				tmoReward += AI.getActionReward(card, newState, players, cv,
						players.get(0), horizon);
			}
			if(possibleMoves.size()>0){
				tmoReward=(tmoReward)/possibleMoves.size();
			}
			return tmoReward;
		}
		
		actionReward += AI.getStateValue(newState, cv, /*win, self, */AI.myCard);
		return actionReward;
	}
}
