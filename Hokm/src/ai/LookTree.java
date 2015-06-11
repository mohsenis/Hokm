package ai;

import java.util.ArrayList;
import java.util.Collections;
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
	
	private static double PRUNE = 0.1;
	private static int[] VALUES = new int[]{1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 6, 8, 10 };

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
		
		if(op1.getIndex()==AI.staticPlayer.getIndex()){
			c1 = AI.getAction(AI.legalActions(newState), newState, players, AI.staticPlayer, cv, 0);
			r1 = case1(c1, newState, players, cv, op1, horizon);
			return r1;
		}
		
		if (firstSuit == hokm) {
			AI.track.add(1);
			
			pr1 = newState.getCardDist().prGreater(oCard, op1);
			AI.prs.add(pr1);
			if(pr1<PRUNE){
				r1=0;
			}else{
				c1 = newState.getCardDist().smallestGreater(oCard);
				r1 = case1(c1, newState, players, cv, op1, horizon);
			}
			
			pr2 = newState.getCardDist().prLess(oCard, op1);
			AI.prs.add((1-pr1)*pr2);
			if((1-pr1)*pr2<PRUNE){
				r2=0;
			}else{
				c2 = newState.getCardDist().smallestLess(oCard);
				r2 = case1(c2, newState, players, cv, op1, horizon);
			}
			
			AI.prs.add((1-pr1)*(1-pr2));
			if((1-pr1)*(1-pr2)<PRUNE){
				r3=0;
			}else{
				c3 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm }), op1);
				r3 = case1(c3, newState, players, cv, op1, horizon);
			}
			

			actionReward = pr1 * r1 + (1 - pr1) * ((pr2 * r2) + (1 - pr2) * r3);
		} else {
			
			if(cv.getValue(oCard)==12){
				AI.track.add(21);
				pr1 = newState.getCardDist().prSuit(firstSuit, op1);
				AI.prs.add(pr1);
				if(pr1<PRUNE){
					r1=0;
				}else{
					c1 = newState.getCardDist().smallestSuit(firstSuit);
					r1 = case1(c1, newState, players, cv, op1, horizon);
				}
				
				pr3 = newState.getCardDist().prSuit(hokm, op1);
				AI.prs.add((1-pr1)*pr3);
				if((1-pr1)*pr3<PRUNE){
					r3=0;
				}else{
					c3 = newState.getCardDist().smallestSuit(hokm);
					r3 = case1(c3, newState, players, cv, op1, horizon);
				}
				
				AI.prs.add((1-pr1)*(1-pr3));
				if((1-pr1)*(1-pr3)<PRUNE){
					r4=0;
				}else{
					c4 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm, firstSuit }), op1);
					r4 = case1(c4, newState, players, cv, op1, horizon);
				}
				
				actionReward = pr1 * r1 + (1 - pr1) * ((pr3 * r3) + (1 - pr3) * r4);
			}else{
				AI.track.add(22);
				pr1 = newState.getCardDist().pHighestCard(firstSuit, op1);
				AI.prs.add(pr1);
				if(pr1<PRUNE){
					r1=0;
				}else{
					c1 = newState.getCardDist().highestCard(firstSuit);
					r1 = case1(c1, newState, players, cv, op1, horizon);
				}
				
				pr2 = newState.getCardDist().prSuit(firstSuit, op1);
				AI.prs.add((1-pr1)*pr2);
				if((1-pr1)*pr2<PRUNE){
					r2=0;
				}else{
					c2 = newState.getCardDist().smallestSuit(firstSuit);
					r2 = case1(c2, newState, players, cv, op1, horizon);
				}
				
				pr3 = newState.getCardDist().prSuit(hokm, op1);
				AI.prs.add((1-pr1)*(1-pr2)*pr3);
				if((1-pr1)*(1-pr2)*pr3<PRUNE){
					r3=0;
				}else{
					c3 = newState.getCardDist().smallestSuit(hokm);
					r3 = case1(c3, newState, players, cv, op1, horizon);
				}
				
				AI.prs.add((1-pr1)*(1-pr2)*(1-pr3));
				if((1-pr1)*(1-pr2)*(1-pr3)<PRUNE){
					r4=0;
				}else{
					c4 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm, firstSuit }), op1);
					r4 = case1(c4, newState, players, cv, op1, horizon);
				}

				actionReward = pr1 * r1 + (1 - pr1) * (pr2 * r2 + (1 - pr2) * (pr3 * r3 + (1 - pr3) * r4));
			}
		}

		return actionReward;
	}

	public static double case1(Card oCard, State oldState,
			List<Player> players, CardValue oldCardValue, Player oPlayer, int horizon) {
		double actionReward = 0;
		double r0, r1, r2, r3, r4;
		Card c0, c1, c2, c3, c4;
		double pr0, pr1, pr2, pr3;
		CardValue cv = new CardValue(oldCardValue);
		SuitName hokm = oldState.getHokm();
		SuitName firstSuit = oldState.getOnTable().get(0).getSuitName();
		State newState = AI.getNewState(oldState, oCard, oPlayer, players);
		Player op1 = players.get(0);
		Player op2 = players.get(2);
		if(op2.getIndex()==AI.staticPlayer.getIndex()){
			c1 = AI.getAction(AI.legalActions(newState), newState, players, AI.staticPlayer, cv, 0);
			r1 = case2(c1, newState, players, cv, op2, horizon);
			return r1;
		}
		Player winner = Game.detWinner(players, newState.getOnTable(), hokm);
		Card bestCardOnTable = newState.getOnTable().get(
				players.indexOf(winner));

		if (winner == op1) {
			Card betterCard = new Card(bestCardOnTable.getSuitName(),
					ValueName.getValueName(bestCardOnTable.getValue() + 3));
			if (firstSuit == hokm) {
				if (cv.getValue(bestCardOnTable) <= 9) {
					AI.track.add(3);
					pr1 = newState.getCardDist().prGreater(betterCard, op2);
					AI.prs.add(pr1);
					if(pr1<PRUNE){
						r1=0;
					}else{
						c1 = newState.getCardDist().smallestGreater(betterCard);
						r1 = case2(c1, newState, players, cv, op2, horizon);
					}
					
					pr2 = newState.getCardDist().prLess(betterCard, op2);
					AI.prs.add((1-pr1)*pr2);
					if((1-pr1)*pr2<PRUNE){
						r2=0;
					}else{
						c2 = newState.getCardDist().smallestLess(betterCard);
						r2 = case2(c2, newState, players, cv, op2, horizon);
					}
					
					AI.prs.add((1-pr1)*(1-pr2));
					if((1-pr1)*(1-pr2)<PRUNE){
						r3=0;
					}else{
						c3 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm }), op2);
						r3 = case2(c3, newState, players, cv, op2, horizon);
					}

					actionReward = pr1 * r1 + (1 - pr1) * ((pr2 * r2) + (1 - pr2) * r3);
				} else {
					AI.track.add(4);
					pr1 = newState.getCardDist().prSuit(hokm, op2);
					AI.prs.add(pr1);
					if(pr1<PRUNE){
						r1=0;
					}else{
						c1 = newState.getCardDist().smallestSuit(hokm);
						r1 = case2(c1, newState, players, cv, op2, horizon);
					}
					
					AI.prs.add((1-pr1));
					if((1-pr1)<PRUNE){
						r2=0;
					}else{
						c2 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm }), op2);
						r2 = case2(c2, newState, players, cv, op2, horizon);
					}

					actionReward = (pr1 * r1 + (1 - pr1) * r2);
				}
			} else {
				if (cv.getValue(bestCardOnTable) <= 9) {
					AI.track.add(5);
					pr0 = newState.getCardDist().pHighestCard(firstSuit, op2);
					AI.prs.add(pr0);
					if(pr0<PRUNE){
						r0=0;
					}else{
						c0 = newState.getCardDist().highestCard(firstSuit);
						r0 = case2(c0, newState, players, cv, op2, horizon);
					}
					
					pr1 = newState.getCardDist().prGreater(betterCard, op2);
					AI.prs.add((1-pr0)*pr1);
					if((1-pr0)*pr1<PRUNE){
						r1=0;
					}else{
						c1 = newState.getCardDist().smallestGreater(betterCard);
						r1 = case2(c1, newState, players, cv, op2, horizon);
					}
					
					pr2 = newState.getCardDist().prLess(betterCard, op2);
					AI.prs.add((1-pr0)*(1-pr1)*pr2);
					if((1-pr0)*(1-pr1)*pr2<PRUNE){
						r2=0;
					}else{
						c2 = newState.getCardDist().smallestLess(betterCard);
						r2 = case2(c2, newState, players, cv, op2, horizon);
					}
					
					pr3 = newState.getCardDist().prSuit(hokm, op2);
					AI.prs.add((1-pr0)*(1-pr1)*(1-pr2)*pr3);
					if((1-pr0)*(1-pr1)*(1-pr2)*pr3<PRUNE){
						r3=0;
					}else{
						c3 = newState.getCardDist().smallestSuit(hokm);
						r3 = case2(c3, newState, players, cv, op2, horizon);
					}
					
					AI.prs.add((1-pr0)*(1-pr1)*(1-pr2)*(1-pr3));
					if((1-pr0)*(1-pr1)*(1-pr2)*(1-pr3)<PRUNE){
						r4=0;
					}else{
						c4 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
						r4 = case2(c4, newState, players, cv, op2, horizon);
					}

					actionReward = pr0*r0 + (1-pr0)*(pr1* r1+ (1 - pr1)	* (pr2 * r2 + (1 - pr2)	* (pr3 * r3 + (1 - pr3) * r4)));
				} else {
					AI.track.add(6);
					pr1 = newState.getCardDist().prSuit(firstSuit, op2);
					AI.prs.add(pr1);
					if(pr1<PRUNE){
						r1=0;
					}else{
						c1 = newState.getCardDist().smallestSuit(firstSuit);
						r1 = case2(c1, newState, players, cv, op2, horizon);
					}
					
					pr2 = newState.getCardDist().prSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
					AI.prs.add((1-pr1)*pr2);
					if((1-pr1)*pr2<PRUNE){
						r2=0;
					}else{
						c2 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
						r2 = case2(c2, newState, players, cv, op2, horizon);
					}
					
					AI.prs.add((1-pr1)*(1-pr2));
					if((1-pr1)*(1-pr2)<PRUNE){
						r3=0;
					}else{
						c3 = newState.getCardDist().smallestSuit(hokm);
						r3 = case2(c3, newState, players, cv, op2, horizon);
					}

					actionReward = pr1 * r1 + (1 - pr1)	* ((pr2 * r2) + (1 - pr2) * r3);
				}
			}
		} else {
			if (firstSuit == hokm) {
				AI.track.add(7);
				pr1 = newState.getCardDist().prGreater(bestCardOnTable, op2);
				AI.prs.add(pr1);
				if(pr1<PRUNE){
					r1=0;
				}else{
					c1 = newState.getCardDist().smallestGreater(bestCardOnTable);
					r1 = case2(c1, newState, players, cv, op2, horizon);
				}
				
				pr2 = newState.getCardDist().prLess(bestCardOnTable, op2);
				AI.prs.add((1-pr1)*pr2);
				if((1-pr1)*pr2<PRUNE){
					r2=0;
				}else{
					c2 = newState.getCardDist().smallestLess(bestCardOnTable);
					r2 = case2(c2, newState, players, cv, op2, horizon);
				}
				
				AI.prs.add((1-pr1)*(1-pr2));
				if((1-pr1)*(1-pr2)<PRUNE){
					r3=0;
				}else{
					c3 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm }), op2);
					r3 = case2(c3, newState, players, cv, op2, horizon);
				}

				actionReward = pr1 * r1 + (1 - pr1)	* ((pr2 * r2) + (1 - pr2) * r3);
			} else {
				if (bestCardOnTable.getSuitName() == firstSuit) {
					
					if(cv.getValue(oCard)==12){
						AI.track.add(81);
						pr1 = newState.getCardDist().prSuit(firstSuit, op2);
						AI.prs.add(pr1);
						if(pr1<PRUNE){
							r1=0;
						}else{
							c1 = newState.getCardDist().smallestSuit(firstSuit);
							r1 = case2(c1, newState, players, cv, op2, horizon);
						}
						
						pr3 = newState.getCardDist().prSuit(hokm, op2);
						AI.prs.add((1-pr1)*pr3);
						if((1-pr1)*pr3<PRUNE){
							r3=0;
						}else{
							c3 = newState.getCardDist().smallestSuit(hokm);
							r3 = case2(c3, newState, players, cv, op2, horizon);
						}
						
						AI.prs.add((1-pr1)*(1-pr3));
						if((1-pr1)*(1-pr3)<PRUNE){
							r4=0;
						}else{
							c4 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm, firstSuit }), op2);
							r4 = case2(c4, newState, players, cv, op2, horizon);
						}

						actionReward = pr1 * r1 + (1 - pr1) * ((pr3 * r3) + (1 - pr3) * r4);
					}else{
						AI.track.add(82);
						pr1 = newState.getCardDist().pHighestCard(firstSuit, op2);
						AI.prs.add(pr1);
						if(pr1<PRUNE){
							r1=0;
						}else{
							c1 = newState.getCardDist().highestCard(firstSuit);
							r1 = case2(c1, newState, players, cv, op2, horizon);
						}
						
						pr2 = newState.getCardDist().prSuit(firstSuit, op2);
						AI.prs.add((1-pr1)*pr2);
						if((1-pr1)*pr2<PRUNE){
							r2=0;
						}else{
							c2 = newState.getCardDist().smallestSuit(firstSuit);
							r2 = case2(c2, newState, players, cv, op2, horizon);
						}
						
						pr3 = newState.getCardDist().prSuit(hokm, op2);
						AI.prs.add((1-pr1)*(1-pr2)*pr3);
						if((1-pr1)*(1-pr2)*pr3<PRUNE){
							r3=0;
						}else{
							c3 = newState.getCardDist().smallestSuit(hokm);
							r3 = case2(c3, newState, players, cv, op2, horizon);
						}
						
						AI.prs.add((1-pr1)*(1-pr2)*(1-pr3));
						if((1-pr1)*(1-pr2)*(1-pr3)<PRUNE){
							r4=0;
						}else{
							c4 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm, firstSuit }), op2);
							r4 = case2(c4, newState, players, cv, op2, horizon);
						}

						actionReward = pr1 * r1 + (1 - pr1)	* (pr2 * r2 + (1 - pr2) * (pr3 * r3 + (1 - pr3) * r4));
					}
				} else {
					AI.track.add(9);
					pr1 = newState.getCardDist().prSuit(firstSuit, op2);
					AI.prs.add(pr1);
					if(pr1<PRUNE){
						r1=0;
					}else{
						c1 = newState.getCardDist().smallestSuit(firstSuit);
						r1 = case2(c1, newState, players, cv, op2, horizon);
					}
					
					pr2 = newState.getCardDist().prGreater(bestCardOnTable, op2);
					AI.prs.add((1-pr1)*pr2);
					if((1-pr1)*pr2<PRUNE){
						r2=0;
					}else{
						c2 = newState.getCardDist().smallestGreater(bestCardOnTable);
						r2 = case2(c2, newState, players, cv, op2, horizon);
					}
					
					pr3 = newState.getCardDist().prSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
					AI.prs.add((1-pr1)*(1-pr2)*pr3);
					if((1-pr1)*(1-pr2)*pr3<PRUNE){
						r3=0;
					}else{
						c3 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
						r3 = case2(c3, newState, players, cv, op2, horizon);
					}
					
					AI.prs.add((1-pr1)*(1-pr2)*(1-pr3));
					if((1-pr1)*(1-pr2)*(1-pr3)<PRUNE){
						r4=0;
					}else{
						c4 = newState.getCardDist().smallestSuit(hokm);
						r4 = case2(c4, newState, players, cv, op2, horizon);
					}

					actionReward = pr1* r1+ (1 - pr1)* (pr2 * r2 + (1 - pr2)* (pr3 * r3 + (1 - pr3) * r4));
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
		
		if(op2.getIndex()==AI.staticPlayer.getIndex()){
			c1 = AI.getAction(AI.legalActions(newState), newState, players, AI.staticPlayer, cv, 0);
			r1 = case3(c1, newState, players, cv, op2, horizon);
			return r1;
		}

		Player winner = Game.detWinner(players, newState.getOnTable(), hokm);
		Card bestCardOnTable = newState.getOnTable().get(
				players.indexOf(winner));

		if (winner == op1) {
			if (firstSuit == hokm) {
				AI.track.add(10);
				pr1 = newState.getCardDist().prSuit(hokm, op2);
				AI.prs.add(pr1);
				if(pr1<PRUNE){
					r1=0;
				}else{
					c1 = newState.getCardDist().smallestSuit(hokm);
					r1 = case3(c1, newState, players, cv, op2, horizon);
				}
				
				AI.prs.add(1-pr1);
				if(1-pr1<PRUNE){
					r2=0;
				}else{
					c2 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm }), op2);
					r2 = case3(c2, newState, players, cv, op2,horizon);
				}
				
				actionReward = (pr1 * r1 + (1 - pr1) * r2);

			} else {
				AI.track.add(11);
				pr1 = newState.getCardDist().prSuit(firstSuit, op2);
				AI.prs.add(pr1);
				if(pr1<PRUNE){
					r1=0;
				}else{
					c1 = newState.getCardDist().smallestSuit(firstSuit);
					r1 = case3(c1, newState, players, cv, op2, horizon);
				}
				
				pr2 = newState.getCardDist().prSuits(AI.getOtherSuits(new SuitName[] { hokm, firstSuit }),op2);
				AI.prs.add((1-pr1)*pr2);
				if((1-pr1)*pr2<PRUNE){
					r2=0;
				}else{
					c2 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm, firstSuit }),	op2);
					r2 = case3(c2, newState, players, cv, op2, horizon);
				}
				
				AI.prs.add((1-pr1)*(1-pr2));
				if((1-pr1)*(1-pr2)<PRUNE){
					r3=0;
				}else{
					c3 = newState.getCardDist().smallestSuit(hokm);
					r3 = case3(c3, newState, players, cv, op2, horizon);
				}

				actionReward = pr1 * r1 + (1 - pr1)	* ((pr2 * r2) + (1 - pr2) * r3);
			}
		} else {
			if (firstSuit == hokm) {
				AI.track.add(12);
				pr1 = newState.getCardDist().prGreater(bestCardOnTable, op2);
				AI.prs.add(pr1);
				if(pr1<PRUNE){
					r1=0;
				}else{
					c1 = newState.getCardDist().smallestGreater(bestCardOnTable);
					r1 = case3(c1, newState, players, cv, op2, horizon);
				}
				
				pr2 = newState.getCardDist().prLess(bestCardOnTable, op2);
				AI.prs.add((1-pr1)*pr2);
				if((1-pr1)*pr2<PRUNE){
					r2=0;
				}else{
					c2 = newState.getCardDist().smallestLess(bestCardOnTable);
					r2 = case3(c2, newState, players, cv, op2, horizon);
				}
				
				AI.prs.add((1-pr1)*(1-pr2));
				if((1-pr1)*(1-pr2)<PRUNE){
					r3=0;
				}else{
					c3 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm }), op2);
					r3 = case3(c3, newState, players, cv, op2, horizon);
				}

				actionReward = pr1 * r1 + (1 - pr1)	* ((pr2 * r2) + (1 - pr2) * r3);
			} else {
				if (bestCardOnTable.getSuitName() == firstSuit) {
					AI.track.add(13);
					pr1 = newState.getCardDist().prGreater(bestCardOnTable, op2);
					AI.prs.add(pr1);
					if(pr1<PRUNE){
						r1=0;
					}else{
						c1 = newState.getCardDist().smallestGreater(bestCardOnTable);
						r1 = case3(c1, newState, players, cv, op2, horizon);
					}
					
					pr2 = newState.getCardDist().prLess(bestCardOnTable, op2);
					AI.prs.add((1-pr1)*pr2);
					if((1-pr1)*pr2<PRUNE){
						r2=0;
					}else{
						c2 = newState.getCardDist().smallestLess(bestCardOnTable);
						r2 = case3(c2, newState, players, cv, op2, horizon);
					}
					
					pr3 = newState.getCardDist().prSuit(hokm, op2);
					AI.prs.add((1-pr1)*(1-pr2)*pr3);
					if((1-pr1)*(1-pr2)*pr3<PRUNE){
						r3=0;
					}else{
						c3 = newState.getCardDist().smallestSuit(hokm);
						r3 = case3(c3, newState, players, cv, op2, horizon);
					}
					
					AI.prs.add((1-pr1)*(1-pr2)*(1-pr3));
					if((1-pr1)*(1-pr2)*(1-pr3)<PRUNE){
						r4=0;
					}else{
						c4 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
						r4 = case3(c4, newState, players, cv, op2, horizon);
					}

					actionReward = pr1* r1+ (1 - pr1)* (pr2 * r2 + (1 - pr2)* (pr3 * r3 + (1 - pr3) * r4));
				} else {
					AI.track.add(14);
					pr1 = newState.getCardDist().prSuit(firstSuit, op2);
					AI.prs.add(pr1);
					if(pr1<PRUNE){
						r1=0;
					}else{
						c1 = newState.getCardDist().smallestSuit(firstSuit);
						r1 = case3(c1, newState, players, cv, op2, horizon);
					}
					
					pr2 = newState.getCardDist().prGreater(bestCardOnTable, op2);
					AI.prs.add((1-pr1)*pr2);
					if((1-pr1)*pr2<PRUNE){
						r2=0;
					}else{
						c2 = newState.getCardDist().smallestGreater(bestCardOnTable);
						r2 = case3(c2, newState, players, cv, op2, horizon);
					}
					
					pr3 = newState.getCardDist().prSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
					AI.prs.add((1-pr1)*(1-pr2)*pr3);
					if((1-pr1)*(1-pr2)*pr3<PRUNE){
						r3=0;
					}else{
						c3 = newState.getCardDist().smallestSuits(AI.getOtherSuits(new SuitName[] { hokm,firstSuit }), op2);
						r3 = case3(c3, newState, players, cv, op2, horizon);
					}
					
					AI.prs.add((1-pr1)*(1-pr2)*(1-pr3));
					if((1-pr1)*(1-pr2)*(1-pr3)<PRUNE){
						r4=0;
					}else{
						c4 = newState.getCardDist().smallestSuit(hokm);
						r4 = case3(c4, newState, players, cv, op2, horizon);
					}
					
					actionReward = pr1* r1+ (1 - pr1)* (pr2 * r2 + (1 - pr2)* (pr3 * r3 + (1 - pr3) * r4));
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
		boolean win=false;
		/*boolean self=false;*/
		if (winner == AI.staticPlayer.getTeam().getPlayer1()
				|| winner == AI.staticPlayer.getTeam().getPlayer2()) {
			newState.updateTeamScore();
			win=true;
			
			/*if(winner == AI.staticPlayer){
				self = true;
			}*/
		} else {
			newState.updateOpponentScore();
		}
		List<Card> possibleMoves;
		if (winner == AI.staticPlayer){
			possibleMoves = newState.getInHand();
			if(AI.b1 && AI.myCard.getSuitName()!=newState.getHokm()){
				AI.b2 = true;
			}
		}else{
			possibleMoves = newState.getCardDist().possibleActions(winner);
		}

		players = GameBuilder.reorder(players, winner);
		newState.getOnTable().clear();
		
		while (horizon > 0) {
			AI.b1 = false;
			List<Double> rewards = new ArrayList<Double>();
			double tmpR = 0;
			horizon--;
			
			if(possibleMoves.size()==0){
				break;
			}else{
				/*tmpR = AI.getActionReward(possibleMoves.get(possibleMoves.size()-1), newState, players, cv,players.get(0), horizon);
				rewards.add(tmpR);
				for (int j=possibleMoves.size()-2; j>-1;j--) {
					if(winner != AI.staticPlayer &&
							possibleMoves.get(j).getSuit()==possibleMoves.get(j+1).getSuit()&&VALUES[cv.getValue(possibleMoves.get(j))]==VALUES[cv.getValue(possibleMoves.get(j+1))]){
						continue;
					}else{
						tmpR = AI.getActionReward(possibleMoves.get(j), newState, players, cv,players.get(0), horizon);
						rewards.add(tmpR);
					}
					
				}*/
				for(Card card:possibleMoves){
					rewards.add(AI.getActionReward(card, newState, players, cv,players.get(0), horizon));
				}
			}	
			
			if(win){
				return Collections.max(rewards);
			}else{
				return Collections.min(rewards);
			}
		}
		
		actionReward += AI.getStateValue(newState, cv, /*win, self, */AI.myCard);
		AI.rs.add(actionReward);
		return actionReward;
	}
}