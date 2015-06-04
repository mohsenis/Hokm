package ai;

import gameplay.Card;
import gameplay.CardValue;
import gameplay.Deck;
import gameplay.State;
import gameplay.SuitName;
import gameplay.Game;
import gameplay.ValueName;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import controller.AgentPlayer;
import controller.Player;

public class AI {

	private AI() {

	}

	static Card myCard=new Card(SuitName.Clubs, ValueName.Two);
	static Player staticPlayer= new AgentPlayer("",0);	
	
	private final static double[] realValues = { 3.00, 3.00, 3.00, 3.00, 3.00,
			3.00, 3.00, 2.00, 2.00, 2.00, 2.00, 1.00, 1.00 };

	public static List<Integer> track=new ArrayList<Integer>();
	
	private final static double[][][] coEf = getCoEf();

	private static double[][][] getCoEf() {
		double[][] first = new double[][] { { 0.16, 0.3, 0.65, 2 },
				{ 0.01, 0.07, 0.25, 1 } };

		double[][] second = new double[][] { realValues, realValues };

		double[][][] coEf = new double[2][4][13];

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 13; k++) {
					coEf[i][j][k] = new BigDecimal(first[i][j] * second[i][k])
							.setScale(3, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
				}
			}
		}
		return coEf;
	}

	public static int getStateValue(State state, CardValue cardValue, /*boolean win, boolean self,*/ Card card) {
		int stateValue = 0;
		stateValue = getInHandValue(state, cardValue)
				+ getTrickScoreValue(state, /*win, self,*/ card);
		return stateValue;
	}

	/*private static int getTrickScoreValue(State state) {
		int trick1 = state.getTeamScore();
		int trick2 = state.getOpponentScore();
		int trickScoreValue = trick1 * 20;
		if (trick2 > 4) {
			trickScoreValue -= (trick2 - trick1) * 10;
		} else if (trick2 == 4 && trick1 < 2) {
			trickScoreValue -= (trick2 - trick1) * 10;
		} else if (trick2 == 3 && trick1 < 1) {
			trickScoreValue -= (trick2 - trick1) * 10;
		}
		return trickScoreValue;
	}*/
	
	private static int getTrickScoreValue(State state, /*boolean win, boolean self,*/ Card card) {
		int trick1 = state.getTeamScore();
		int trick2 = state.getOpponentScore();
		int trickScoreValue=(trick1-trick2)*20;
		/*if(win){
			trickScoreValue = 20;
			if(self && card.getSuitName()!=state.getHokm()){
				trickScoreValue += 10;
			}
		}else{
			trickScoreValue = -20;
			if (trick2 > 4) {
				trickScoreValue -= 10;
			} else if (trick2 == 4 && trick1 < 2) {
				trickScoreValue -= 10;
			} else if (trick2 == 3 && trick1 < 1) {
				trickScoreValue -= 10;
			}
		}*/
		
		return trickScoreValue;
	}

	private static int getInHandValue(State state, CardValue cardValue) {
		int[] cardValues = new int[] { 1, 1, 1, 1, 1, 2, 2, 2, 2, 4, 6, 8, 10 };
		int inHandValue = 0;
		boolean h = false;
		boolean[] hasSuits = new boolean[] { false, false, false, false };
		for (Card card : state.getInHand()) {
			if (card.getSuit() == state.getHokm().getSuit()) {
				h = true;
				hasSuits[card.getSuit()] = true;
				if(cardValue.getValue(card)>9){
					inHandValue += cardValues[cardValue.getValue(card)] * 4;
				}else{
					inHandValue += cardValues[cardValue.getValue(card)] * 3;
				}
			} else {
				hasSuits[card.getSuit()] = true;
				inHandValue += cardValues[cardValue.getValue(card)];
			}
		}
		if (h) {
			for (boolean b : hasSuits) {
				if (!b) {
					inHandValue += 10;
				}
			}
		}
		return inHandValue;
	}

	private static boolean suitStatus(List<Card> played, List<Card> inHand,
			SuitName suit) {

		int n = 0;
		for (Card card : played) {
			if (card.getSuitName() == suit) {
				n++;
			}
		}
		for (Card card : inHand) {
			if (card.getSuitName() == suit) {
				n++;
			}
		}
		if (n == 13)
			return false;
		return true;
	}

	public static State getNewState(State state, Card oCard,
			Player oPlayer, List<Player> players) {
		State newState = new State(state);
		newState.getPlayed().add(oCard);
		newState.getPlayedBy().add(oPlayer);
		newState.getCardDist().played(oCard, oPlayer);
		if (!newState.getOnTable().isEmpty()) {
			if (newState.getOnTable().get(0).getSuitName() != oCard
					.getSuitName()) {
				newState.getCardDist().passed(newState.getOnTable().get(0),
						oPlayer);
			}
		}

		newState.getOnTable().add(oCard);
		return newState;
	}

	public static Card getAction(List<Card> legalActions, State state,
			List<Player> players, Player player, CardValue cardValue) {
		staticPlayer = player;
		Card bestAction = legalActions.get(0);
		double maxReward = 0.0;
		double actionReward;
		int horizon=2;
		
		System.out.println("\n" + player.getName() + "'s legal actions: ");

		for (Card card : legalActions) {
			myCard = card;
			actionReward = getActionReward(card, state, players, cardValue,
					player, horizon);
			System.out.printf("%-5s" + "%-22s" + "%-18s" + "\t" + track.toString() + "\n",
					(legalActions.indexOf(card) + 1) + ") ", card.toString(),actionReward);

			if (maxReward < actionReward) {
				maxReward = actionReward;
				bestAction = card;
			}
		}
		return bestAction;
	}

	public static double getActionReward(Card myCard, State oldState,
			List<Player> players, CardValue oldCardValue, Player staticPlayer, int horizon) {
//		System.out.println("getActionRewards - ran");
		State newState = new State(oldState);
		newState.getInHand().remove(myCard);
		//int myInd=players.indexOf(me);
		
		double actionReward = 0.0;
		track.clear();
		switch (oldState.getOnTable().size()) {
		case 0:
			actionReward=LookTree.case0(myCard, newState, players, oldCardValue, staticPlayer, horizon);
			break;
			
		case 1:
			actionReward=LookTree.case1(myCard, newState, players, oldCardValue, staticPlayer, horizon);
			break;
			
		case 2:
			actionReward=LookTree.case2(myCard, newState, players, oldCardValue, staticPlayer, horizon);
			break;
			
		case 3:
			actionReward=LookTree.case3(myCard, newState, players, oldCardValue, staticPlayer, horizon);
			break;
		}

		return actionReward;
	}

	public static List<SuitName> getOtherSuits(SuitName[] suitnames) {
		List<SuitName> suitNames = new ArrayList<SuitName>(
				Arrays.asList(suitnames));
		List<SuitName> suits = new ArrayList<SuitName>();
		for (SuitName s : SuitName.values()) {
			if (!suitNames.contains(s)) {
				suits.add(s);
			}
		}
		return suits;
	}

	/*public static List<Card> possibleActions(State state, Player player) {
		List<Card> remainingCards = new Deck().getDeck();
		remainingCards.removeAll(state.getPlayed());
		remainingCards.removeAll(state.getInHand());

		Predicate<Card> filter = new MyPredicate<Card>(player);
		remainingCards.removeIf(filter);
		return remainingCards;
	}*/

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

	public static void shuffleSuit(List<Card> legalActions, List<Integer> rand) {
		Collections.sort(legalActions, new Comparator<Card>() {
			public int compare(Card c1, Card c2) {
				if (c1.getSuit() == c2.getSuit()) {
					return c1.getValue() - c2.getValue();
				}
				return rand.indexOf(c1.getSuit()) - rand.indexOf(c2.getSuit());
			}
		});
	}

	public static Card takeAction(List<Card> legalActions, State state,
			List<Player> players, Player player, CardValue cardValue) {
		List<Integer> rand = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
			rand.add(i);
		}
		long seed = System.nanoTime();
		Collections.shuffle(rand, new Random(seed));
		shuffleSuit(legalActions, rand);

		List<Double> values = getActionsValues(legalActions, state, players,
				player, cardValue);

		Card action = legalActions.get(values.indexOf(Collections.max(values)));
		return action;
	}

	private static List<Double> getActionsValues(List<Card> legalActions,
			State state, List<Player> players, Player player,
			CardValue cardValue) {
		List<Double> values = new ArrayList<Double>(legalActions.size());
		int feature1 = findNeed(state.getTeamScore(), state.getOpponentScore()) - 1;
		int feature2;
		int feature3;
		double coef;
		System.out.println("\n" + player.getName() + "'s legal actions: ");
		for (Card card : legalActions) {
			feature2 = likelihood(card, state, players, player, cardValue) - 1;
			feature3 = cardValue.getValue(card);
			coef = coEf[feature1][feature2][feature3];
			values.add(coef);
			System.out.printf("%-5s" + "%-22s",
					(legalActions.indexOf(card) + 1) + ") ", card.toString());
			System.out.printf((feature1 + 1) + " - " + (feature2 + 1) + " - "
					+ "%-2s" + " - " + coef + "\n", feature3);
		}
		return values;
	}

	/* returns urgency level */
	private static int findNeed(int trick1, int trick2) {
		int need = 1;
		if (trick2 > 4 || trick1 > 4) {
			need = 2;
		} else if (trick2 == 4 && trick1 < 2) {
			need = 2;
		} else if (trick2 == 3 && trick1 < 1) {
			need = 2;
		}
		return need;
	}

	// returns the likelihood of taking the trick with a specific card
	private static int likelihood(Card myCard, State state,
			List<Player> players, Player player, CardValue cardValue) {
		SuitName hokm = state.getHokm();
		int L = 3;
		SuitName firstSuit;

		if (state.getOnTable().isEmpty())
			firstSuit = myCard.getSuitName();
		else
			firstSuit = state.getOnTable().get(0).getSuitName();
		boolean firstSuitStatus = suitStatus(state.getPlayed(),
				player.getInHand(), firstSuit);
		boolean hokmSuitStatus = suitStatus(state.getPlayed(),
				player.getInHand(), hokm);

		Card op1Card;
		Card mateCard;

		Player op1;
		Player op2;
		Player mate;

		/*** h=hokm, s=firstSuit, p=pass ***/
		switch (players.indexOf(player)) {
		case 0:
			op1 = players.get(1);
			mate = players.get(2);
			op2 = players.get(3);

			if (myCard.getSuitName() == hokm) {
				if (op1.getSuitStatus(hokm) && hokmSuitStatus) {
					if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,h,h
																		 * ,h}
																		 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else {/* {h,h,h,p} */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
						}
					} else {/* {h,h,p} */
						if (cardValue.getValue(myCard) == 12)
							L = 4;
						else
							L = 2;
					}
				} else {
					if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,p,h
																		 * ,h}
																		 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else {/* {h,p,h,p} */
							L = 4;
						}
					} else {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,p,p
																		 * ,h}
																		 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else {/* {h,p,p,p} */
							L = 4;
						}
					}
				}

			} else {
				if (op1.getSuitStatus(firstSuit) && firstSuitStatus) {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * s
																			 * ,
																			 * s
																			 * ,
																			 * s
																			 * }
																			 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * }
																				 */
							L = 1;
						} else {/* {s,s,s,p} */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
						}

					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * s
																			 * ,
																			 * h
																			 * ,
																			 * s
																			 * }
																			 */
							L = 4;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * }
																				 */
							L = 2;
						} else {/* {s,s,h,p} */
							L = 4;
						}
					} else {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * s
																			 * ,
																			 * p
																			 * ,
																			 * s
																			 * }
																			 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * h
																				 * }
																				 */
							L = 1;
						} else {/* {s,s,p,p} */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						}
					}

				} else if (op1.getSuitStatus(hokm) && hokmSuitStatus) {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * h
																			 * ,
																			 * s
																			 * }
																			 */
						L = 1;
					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {/*
																			 * {s
																			 * ,
																			 * h
																			 * ,
																			 * h
																			 * }
																			 */
						L = 3;
					} else {/* {s,h,p} */
						L = 1;
					}
				} else {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * s
																			 * ,
																			 * s
																			 * }
																			 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * }
																				 */
							L = 1;
						}
					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * h
																			 * ,
																			 * s
																			 * }
																			 */
							L = 4;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * }
																				 */
							L = 2;
						} else {/* {s,p,h,p} */
							L = 4;
						}
					} else {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * p
																			 * ,
																			 * s
																			 * }
																			 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * p
																				 * ,
																				 * h
																				 * }
																				 */
							L = 1;
						} else {/* {s,p,p,p } */
							L = 4;
						}
					}
				}
			}

			break;
		case 1:
			op1Card = state.getOnTable().get(0); // The opponent player who has
													// laid a card just before
													// the me
			op2 = players.get(2);
			mate = players.get(3);

			if (firstSuit == hokm) {
				if (myCard.getSuitName() == hokm) {
					if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
						if (mate.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,h,h
																		 * ,h}
																		 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (myCard.getValue() < op1Card.getValue()
									&& cardValue.getValue(op1Card) > 10)
								L = 2;
						} else {/* {h,h,h,p} */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (myCard.getValue() < op1Card.getValue())
								L = 1;
							else if (cardValue.getValue(myCard) <= 10)
								L = 2;
						}
					} else {
						if (mate.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,h,p
																		 * ,h}
																		 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (myCard.getValue() < op1Card.getValue()
									&& cardValue.getValue(op1Card) > 10)
								L = 2;
							else if (myCard.getValue() > op1Card.getValue())
								L = 4;
						} else {/* {h,h,p,p} */
							if (myCard.getValue() < op1Card.getValue())
								L = 1;
							else
								L = 4;
						}
					}
				} else {
					if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (cardValue.getValue(op1Card) == 12)/* {h,p,,h} */
							L = 1;
						else if (cardValue.getValue(op1Card) > 10)
							L = 2;
					} else {/* {h,p,,p} */
						L = 1;
					}
				}

			} else {
				if (myCard.getSuitName() == firstSuit) {
					if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * s
																				 * }
																				 */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (myCard.getValue() < op1Card.getValue()
									&& cardValue.getValue(op1Card) > 10)
								L = 2;

						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * }
																				 */
							L = 4;
						} else {/* {s,s,s,p} */
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (myCard.getValue() < op1Card.getValue())
								L = 1;
							else if (myCard.getValue() > op1Card.getValue()
									&& cardValue.getValue(myCard) <= 10)
								L = 2;
						}
					} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * ,
																				 * s
																				 * }
																				 */
							L = 1;
						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * }
																				 */
							L = 3;
						} else {/* {s,s,h,p} */
							L = 1;
						}
					} else {
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * s
																				 * }
																				 */
							if (myCard.getValue() > op1Card.getValue())
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (cardValue.getValue(op1Card) > 10)
								L = 2;
						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * h
																				 * }
																				 */
							L = 4;
						} else {/* {s,s,p,p} */
							if (myCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;
						}
					}
				} else if (myCard.getSuitName() == hokm) {
					if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																		 * {s,h,s
																		 * ,}
																		 */
						L = 4;
					} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * ,
																				 * s
																				 * }
																				 */
							if (cardValue.getValue(myCard) < 9)// this is ok to
																// be < and not
																// <=
								L = 2;
						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * }
																				 */
							L = 3;
						} else {/* {s,h,h,p} */
							if (cardValue.getValue(myCard) < 9)
								L = 2;
						}
					} else {/* {s,h,p,} */
						L = 4;
					}
				} else {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * s
																			 * ,
																			 * s
																			 * }
																			 */
							if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (cardValue.getValue(op1Card) > 10)
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus)/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * h
																			 * ,
																			 * s
																			 * }
																			 */
							L = 1;
						else if (cardValue.getValue(op1Card) == 12)/* {s,p,p,s} */
							L = 1;
						else if (cardValue.getValue(op1Card) > 10)/* {s,p,p,s} */
							L = 2;
					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus)
							L = 3;
						else
							L = 4;
					} else {/* {s,p,p,p} */
						L = 1;
					}
				}
			}

			break;
		case 2:
			op1Card = state.getOnTable().get(1);

			mateCard = state.getOnTable().get(0);
			op2 = players.get(3);

			if (firstSuit == hokm) {
				if (op1Card.getSuitName() == hokm) {
					if (myCard.getSuitName() == hokm) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,h,h
																		 * ,h}
																		 */

							if (cardValue.getValue(myCard) == 12
									|| cardValue.getValue(mateCard) == 12)
								L = 4;
							else if ((myCard.getValue() > op1Card.getValue() && cardValue
									.getValue(myCard) > 10)
									|| (mateCard.getValue() > op1Card
											.getValue() && cardValue
											.getValue(mateCard) > 10))
								L = 3;
							else if ((myCard.getValue() > op1Card.getValue() && cardValue
									.getValue(myCard) <= 10)
									|| (mateCard.getValue() > op1Card
											.getValue() && cardValue
											.getValue(mateCard) <= 10))
								L = 2;

							else if (op1Card.getValue() > myCard.getValue()
									&& op1Card.getValue() > mateCard.getValue())
								L = 1;

						} else {/* {h,h,h,p} */

							if (myCard.getValue() > op1Card.getValue()
									|| mateCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;

						}
					} else {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,h,p
																		 * ,h}
																		 */

							if (cardValue.getValue(mateCard) == 12)
								L = 4;
							else if (mateCard.getValue() < op1Card.getValue())
								L = 1;
							else if (cardValue.getValue(mateCard) <= 10)
								L = 2;
						} else {/* {h,h,p,p} */

							if (mateCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;

						}
					}

				} else {
					if (myCard.getSuitName() == hokm) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,p,h
																		 * ,h}
																		 */

							if (cardValue.getValue(mateCard) == 12
									|| cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(mateCard) <= 10
									&& cardValue.getValue(myCard) <= 10)
								L = 2;

						} else {/* {h,p,h,p} */
							L = 4;
						}
					} else {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																		 * {h,p,p
																		 * ,h}
																		 */

							if (cardValue.getValue(mateCard) == 12)
								L = 4;
							else if (cardValue.getValue(mateCard) <= 10)
								L = 2;

						} else {/* {h,p,p,p} */
							L = 4;
						}
					}
				}
			} else {
				if (op1Card.getSuitName() == firstSuit) {
					if (myCard.getSuitName() == firstSuit) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * s
																			 * ,
																			 * s
																			 * ,
																			 * s
																			 * }
																			 */

							if (cardValue.getValue(myCard) == 12
									|| cardValue.getValue(mateCard) == 12)
								L = 4;

							else if (op1Card.getValue() > myCard.getValue()
									&& op1Card.getValue() > mateCard.getValue())
								L = 1;
							else if ((myCard.getValue() > op1Card.getValue() && cardValue
									.getValue(myCard) > 10)
									|| (mateCard.getValue() > op1Card
											.getValue() && cardValue
											.getValue(mateCard) > 10))
								L = 3;
							else if ((myCard.getValue() > op1Card.getValue() && cardValue
									.getValue(myCard) <= 10)
									|| (mateCard.getValue() > op1Card
											.getValue() && cardValue
											.getValue(mateCard) <= 10))
								L = 2;

						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * }
																				 */

							L = 1;

						} else {/* {s,s,s,p} */

							if (myCard.getValue() > op1Card.getValue()
									|| mateCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;
						}

					} else if (myCard.getSuitName() == hokm) {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus)/*
																			 * {s
																			 * ,
																			 * s
																			 * ,
																			 * h
																			 * ,
																			 * s
																			 * }
																			 */
							L = 4;
						else if (op2.getSuitStatus(hokm) && hokmSuitStatus)/*
																			 * {s
																			 * ,
																			 * s
																			 * ,
																			 * h
																			 * ,
																			 * h
																			 * }
																			 */

							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(myCard) < 9)
								L = 2;

					} else {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * s
																			 * ,
																			 * p
																			 * ,
																			 * s
																			 * }
																			 */

							if (cardValue.getValue(mateCard) == 12)
								L = 4;
							if (op1Card.getValue() > mateCard.getValue())
								L = 1;
							else if (op1Card.getValue() < mateCard.getValue()
									&& cardValue.getValue(mateCard) <= 10)
								L = 2;

						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * h
																				 * }
																				 */

							L = 1;

						} else {/* {s,s,p,p} */

							if (op1Card.getValue() > mateCard.getValue())
								L = 1;
							else
								L = 4;

						}
					}

				} else if (op1Card.getSuitName() == hokm) {
					if (myCard.getSuitName() != hokm) {

						L = 1;

					} else {

						if (myCard.getValue() > op1Card.getValue()) {
							if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * ,
																				 * s
																				 * }
																				 */
								L = 4;
							} else if (op2.getSuitStatus(hokm)
									&& hokmSuitStatus) {/* {s,h,h,h} */
								if (myCard.getValue() < 9)
									L = 2;
							} else {
								L = 4;
							}
						} else {

							L = 1;
						}
					}
				} else {
					if (myCard.getSuitName() == firstSuit) {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * s
																			 * ,
																			 * s
																			 * }
																			 */
							if (cardValue.getValue(myCard) == 12
									|| cardValue.getValue(mateCard) == 12) {
								L = 4;
							} else if (cardValue.getValue(myCard) <= 10
									&& cardValue.getValue(mateCard) <= 10)
								L = 2;

						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * s
																				 * ,
																				 * h
																				 * }
																				 */
							L = 1;
						} else {/* {s,p,s,p} */
							L = 4;
						}

					} else if (myCard.getSuitName() == hokm) {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * h
																			 * ,
																			 * s
																			 * }
																			 */
							L = 4;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * h
																				 * ,
																				 * h
																				 * }
																				 */

							if (cardValue.getValue(myCard) < 9)
								L = 2;
						} else {
							L = 4;
						}

					} else {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {/*
																			 * {s
																			 * ,
																			 * p
																			 * ,
																			 * p
																			 * ,
																			 * s
																			 * }
																			 */
							if (cardValue.getValue(mateCard) == 12)
								L = 4;
							else if (cardValue.getValue(mateCard) <= 10)
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {/*
																				 * {
																				 * s
																				 * ,
																				 * p
																				 * ,
																				 * p
																				 * ,
																				 * h
																				 * }
																				 */
							L = 1;
						} else {/* {s,p,p,p} */
							L = 4;
						}
					}
				}
			}

			break;
		case 3:
			List<Card> tempTable = new ArrayList<Card>();
			tempTable.addAll(state.getOnTable());
			tempTable.add(myCard);
			Player winner = Game.detWinner(players, tempTable, hokm);

			if (winner == player.getTeam().getPlayer1()
					|| winner == player.getTeam().getPlayer2())
				L = 4;
			else
				L = 1;
			tempTable.remove(myCard);
			break;
		}
		return L;
	}

	public static SuitName hokm(List<Card> firstFive) {
		SuitName hokm = SuitName.Spades;
		int suitIndex;
		int valueIndex;
		double[][] nSuit = new double[2][4];
		for (Card card : firstFive) {
			suitIndex = card.getSuit();
			valueIndex = card.getValue() - 2;
			nSuit[0][suitIndex]++;
			nSuit[1][suitIndex] += valueIndex;
		}

		double max = 0;
		for (SuitName suit : SuitName.values()) {
			int i = suit.getSuit();
			if (nSuit[0][i] > 2) {
				return suit;
			} else if (nSuit[0][i] == 2 && nSuit[1][i] > max) {
				max = nSuit[1][i];
				hokm = suit;
			}
		}
		return hokm;
	}
}
