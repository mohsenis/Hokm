package ai;

import gameplay.Card;
import gameplay.CardValue;
import gameplay.Deck;
import gameplay.State;
import gameplay.SuitName;
import gameplay.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import controller.Player;

public class AI {

	private AI() {

	}
	
	private final static double[] realValues = 
		{ 	3.00,
			3.00,
			3.00,
			3.00,
			3.00,
			3.00,
			3.00,
			2.00,
			2.00,
			2.00,
			2.00,
			1.00,
			1.00
		};
	private final static double[][][] coEf = getCoEf();
	
	
	private static double[][][] getCoEf() {
		double[][] first = new double[][] { { 0.16, 0.3, 0.7, 1 },
				{ 0.05, 0.1, 0.5, 1 } };

		double[][] second = new double[][]{realValues,realValues};
		
		/*for (int i=0;i<2;i++){ 
			for (int j=0;j<13;j++){
				System.out.print(second[i][j]+"-");
			}
			System.out.println();
		}*/
		
		double[][][] coEf = new double[2][4][13];

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 4; j++) {
				for (int k = 0; k < 13; k++) {
					coEf[i][j][k] = first[i][j] * second[i][k];
				}
			}
		}
		return coEf;
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
			List<Player> players, Player player, CardValue cardValue) {

		List<Double> values = getActionsValues(legalActions, state, players,
				player, cardValue);
		Card action = legalActions.get(values.indexOf(Collections.max(values)));
		return action;
	}

	private static List<Double> getActionsValues(List<Card> legalActions,
			State state, List<Player> players, Player player,
			CardValue cardValue) {
		List<Double> values = new ArrayList<Double>(legalActions.size());
		int feature1 = findNeed(state.getTeamScore(), state.getOpponentScore())-1;
		int feature2;
		int feature3;
		double coef;
		for (Card card : legalActions) {
			System.out.print((legalActions.indexOf(card)+1)+") "+card.toString());
			feature2 = likelihood(card, state, players, player, cardValue)-1;
			feature3 = card.getValue()-2;
			coef = coEf[feature1][feature2][feature3];
			values.add(coef);
			//System.out.print((legalActions.indexOf(card)+1)+") "+card.toString());
			System.out.println("   "+feature1+"-"+feature2+"-"+feature3+"-"+coef);
		}
		return values;
	}

	/* returns urgency level */
	private static int findNeed(int trick1, int trick2) {
		int need = 1;
		if (trick2 == 6 || trick1 == 6) {
			need = 2;
		} else if (trick2 == 5 && trick1 < 5) {
			need = 2;
		} else if (trick2 == 4 && trick1 < 3) {
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
			firstSuit=myCard.getSuitName();
		else
			firstSuit = state.getOnTable().get(0).getSuitName(); // SuitName
																		// of
																		// the
																		// first
																		// card
																		// on
																		// table
		boolean firstSuitStatus = suitStatus(state.getPlayed(),
				player.getInHand(), firstSuit);
		boolean hokmSuitStatus = suitStatus(state.getPlayed(),
				player.getInHand(), hokm);

		Card op1Card;
		Card mateCard;

		Player op1;
		Player op2;
		Player mate;

		switch (players.indexOf(player)) {
		case 0:
			op1 = players.get(1);
			mate = players.get(2);
			op2 = players.get(3);

			if (myCard.getSuitName() == hokm) {
				if (op1.getSuitStatus(hokm) && hokmSuitStatus) {
					if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								// since op2 is the last who players it is more
								// likely to lose the hand
								L = 2;
						} else {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
						}
					} else {
						if (cardValue.getValue(myCard) == 12)
							L = 4;
						else
							L = 2;
					}
				} else {
					if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								// since op2 is the last who players it is more
								// likely to lose the hand
								L = 2;
						} else {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
						}
					} else {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else {
							L = 4;
						}
					}
				}

			} else {
				if (op1.getSuitStatus(firstSuit) && firstSuitStatus) {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 1;
						} else {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
						}

					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							L = 4;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 2;
						} else {
							L = 4;
						}
					} else {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 1;
						} else {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						}
					}

				} else if (op1.getSuitStatus(hokm) && hokmSuitStatus) {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
						L = 1;
					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						L = 3;
					} else {
						L = 1;
					}
				} else {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 1;
						}
					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							L = 4;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 2;
						} else {
							L = 4;
						}
					} else {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 1;
						} else {
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
						if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (myCard.getValue() < op1Card.getValue()
									&& cardValue.getValue(op1Card) > 9)
								L = 2;
						} else {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (myCard.getValue() < op1Card.getValue())
								L = 1;
							else if (cardValue.getValue(myCard) < 9)
								L = 2;
						}
					} else {
						if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (myCard.getValue() < op1Card.getValue()
									&& cardValue.getValue(op1Card) > 9)
								L = 2;
							else if (myCard.getValue() > op1Card.getValue())
								L = 4;
						} else {
							if (myCard.getValue() < op1Card.getValue())
								L = 1;
							else
								L = 4;
						}
					}
				} else {
					if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (cardValue.getValue(op1Card) == 12)
							L = 1;
						else if (cardValue.getValue(op1Card) > 9)
							L = 2;
					} else {
						L = 1;
					}
				}

			} else {
				if (myCard.getSuitName() == firstSuit) {
					if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (myCard.getValue() < op1Card.getValue()
									&& cardValue.getValue(op1Card) > 9)
								L = 2;

						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 4;
						} else {
							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (myCard.getValue() < op1Card.getValue())
								L = 1;
							else if (myCard.getValue() > op1Card.getValue()
									&& cardValue.getValue(myCard) < 9)
								L = 2;
						}
					} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {// stop
																			// point.
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
							L = 1;
						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 3;
						} else {
							L = 1;
						}
					} else {
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (myCard.getValue() > op1Card.getValue())
								L = 4;
							else if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (cardValue.getValue(op1Card) > 9)
								L = 2;
						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 4;
						} else {
							if (myCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;
						}
					}
				} else if (myCard.getSuitName() == hokm) {
					if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
						L = 4;
					} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
						if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(myCard) < 9)
								L = 2;
						} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 3;
						} else {
							if (cardValue.getValue(myCard) < 9)
								L = 2;
						}
					} else {
						L = 4;
					}
				} else {
					if (mate.getSuitStatus(firstSuit) && firstSuitStatus) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(op1Card) == 12)
								L = 1;
							else if (cardValue.getValue(op1Card) > 9)
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus)
							L = 1;
						else if (cardValue.getValue(op1Card) == 12)
							L = 1;
						else if (cardValue.getValue(op1Card) > 9)
							L = 2;
					} else if (mate.getSuitStatus(hokm) && hokmSuitStatus) {
						if (op2.getSuitStatus(hokm) && hokmSuitStatus)
							L = 3;
						else
							L = 4;
					} else {
						L = 1;
					}
				}
			}

			break;
		case 2:
			op1Card = state.getOnTable().get(1); // The opponent player who has
													// laid a card just before
													// the me

			mateCard = state.getOnTable().get(0); // Teammate's card
			op2 = players.get(3);

			if (firstSuit == hokm) {
				if (op1Card.getSuitName() == hokm) {
					if (myCard.getSuitName() == hokm) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {// can
																				// be
																				// smarter

							if (cardValue.getValue(myCard) == 12
									|| cardValue.getValue(mateCard) == 12)
								L = 4;

							else if ((myCard.getValue() > op1Card.getValue() && cardValue
									.getValue(myCard) < 9)
									|| (mateCard.getValue() > op1Card
											.getValue() && cardValue
											.getValue(mateCard) < 9))
								L = 2;

							else if (op1Card.getValue() > myCard.getValue()
									&& op1Card.getValue() > mateCard.getValue())
								L = 1;

						} else {

							if (myCard.getValue() > op1Card.getValue()
									|| mateCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;

						}
					} else {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {

							if (cardValue.getValue(mateCard) == 12)
								L = 4;
							else if (mateCard.getValue() < op1Card.getValue())
								L = 1;
							else if (cardValue.getValue(mateCard) < 9)
								L = 2;
						} else {

							if (mateCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;

						}
					}

				} else {
					if (myCard.getSuitName() == hokm) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {

							if (cardValue.getValue(mateCard) == 12
									|| cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(mateCard) < 9
									&& cardValue.getValue(myCard) < 9)
								L = 2;

						} else {
							L = 4;
						}
					} else {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {

							if (cardValue.getValue(mateCard) == 12)
								L = 4;
							else if (cardValue.getValue(mateCard) < 9)
								L = 2;

						} else {
							L = 4;
						}
					}
				}
			} else {
				if (op1Card.getSuitName() == firstSuit) {
					if (myCard.getSuitName() == firstSuit) {
						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {

							if (cardValue.getValue(myCard) == 12
									|| cardValue.getValue(mateCard) == 12)
								L = 4;

							else if (op1Card.getValue() > myCard.getValue()
									&& op1Card.getValue() > mateCard.getValue())
								L = 1;

							else if ((myCard.getValue() > op1Card.getValue()
									&& (cardValue.getValue(myCard) < 9) || (mateCard
									.getValue() > op1Card.getValue() && cardValue
									.getValue(mateCard) < 9)))
								L = 2;

						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {

							if (op1Card.getValue() > myCard.getValue()
									&& op1Card.getValue() > mateCard.getValue())
								L = 1;
							else
								L = 2;

						} else {

							if (myCard.getValue() > op1Card.getValue()
									|| mateCard.getValue() > op1Card.getValue())
								L = 4;
							else
								L = 1;
						}

					} else if (myCard.getSuitName() == hokm) {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus)
							L = 4; // It is not completely deterministic though
						else if (op2.getSuitStatus(hokm) && hokmSuitStatus)

							if (cardValue.getValue(myCard) == 12)
								L = 4;
							else if (cardValue.getValue(myCard) < 9)
								L = 2;

					} else {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {

							if (op1Card.getValue() > mateCard.getValue())
								L = 1;

							else if (op1Card.getValue() < mateCard.getValue()
									&& cardValue.getValue(mateCard) < 9)
								L = 2;

						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {

							L = 1;

						} else {

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
							if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
								L = 4;
							} else if (op2.getSuitStatus(hokm)
									&& hokmSuitStatus) {
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

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {

							if (cardValue.getValue(myCard) < 9
									|| cardValue.getValue(mateCard) < 9)
								L = 2;

						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 1;
						} else {
							L = 4;
						}

					} else if (myCard.getSuitName() == hokm) {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							L = 4;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							if (cardValue.getValue(myCard) < 9)
								L = 2;
						} else {
							L = 4;
						}

					} else {

						if (op2.getSuitStatus(firstSuit) && firstSuitStatus) {
							if (cardValue.getValue(mateCard) < 9)
								L = 2;
						} else if (op2.getSuitStatus(hokm) && hokmSuitStatus) {
							L = 1;
						} else {
							L = 4;
						}
					}
				}
			}

			break;
		case 3:
			List<Card> tempTable = state.getOnTable();
			tempTable.add(myCard);
			Player winner = Game.detWinner(players, tempTable, hokm);

			if (winner == player.getTeam().getPlayer1()
					|| winner == player.getTeam().getPlayer2())
				L = 4;
			else
				L = 1;
			break;
		}
		return L;
	}

	public static SuitName hokm(List<Card> firstFive){
		SuitName hokm = SuitName.Spade;
		int suitIndex;
		int valueIndex;
		double[][] nSuit = new double[2][4];
		for(Card card: firstFive){
			suitIndex = card.getSuit();
			valueIndex = card.getValue()-2;
			nSuit[0][suitIndex]++;
			nSuit[1][suitIndex]+=realValues[valueIndex];
		}
		
		double max=0;
		for(SuitName suit: SuitName.values()){
			int i = suit.getSuit();
			if(nSuit[0][i]>2){
				return suit;
			}else if(nSuit[0][i]==2 && nSuit[1][i]>max){
				max=nSuit[1][i];
				hokm=suit;
			}
		}
		return hokm;
	}
}
