package gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import controller.AgentPlayer;
import controller.Player;

public class Game {
	private List<Player> players;
	private Deck deck;
	private CardValue cardValue;
	
	private State state;
	private Player winner;
	private boolean terminate;
	private SuitName hokm;
	private Player hakem;
	private List<Card> table;
	private List<Card> played;
	private List<Player> playedBy;
	
	public void setHakem(Player player) {
		this.hakem = player;
	}

	public Player getHakem() {
		return this.hakem;
	}
		

	public Game(List<Player> players) {
		this.players = players;
		this.players.get(0).getTeam().resetTrickScore();
		this.players.get(1).getTeam().resetTrickScore();
		this.deck = new Deck();
		
		this.cardValue=new CardValue();
		this.hakem = players.get(0);
		this.terminate = false;
		this.table = new ArrayList<Card>();
		this.played = new ArrayList<Card>();
		this.playedBy = new ArrayList<Player>();
		
		
		/*Card tempCard = new Card(SuitName.Dimond, ValueName.Ten);
		System.out.println("the value of "+ tempCard.toString()+"is: "+ this.cardValue.getValue(tempCard));
		System.out.println(cardValue.diamonds.get(8)==(tempCard));*/
		
	}

	public void setHokm(SuitName suitName) {
		this.hokm = suitName;
	}

	public SuitName getHokm() {
		return this.hokm;
	}

	public void shuffle(Deck deck) {
		Long seed = /*154539764302850L;66246619841347L;*/System.nanoTime();
		System.out.println(seed);
		Collections.shuffle(deck.getDeck(), new Random(seed));
	}

	public boolean getTerminate() {
		return this.terminate;
	}

	public void terminate() {
		this.terminate = !this.terminate;
	}

	public void deal() {
		shuffle(this.deck);
		int j = 0;
		Card card;
		for (Player player : this.players) {
			player.getInHand().clear();
			player.initSuitStatus();
			player.newDist();
			for (int i = 2; i <= 14; i++) {
				card=this.deck.getDeck().get(j);
				player.getInHand().add(card);
				player.getDist().played(card, player);
				j++;
			}
		}
	}

	public List<Card> legalActions(State state) {
		List<Card> actions = new ArrayList<Card>();
		System.out.println("Cards on the table: ");
		for (Card card : state.getOnTable()) {
			System.out.println(card.toString());
		}

		if (this.table.isEmpty()) {
			actions = state.getInHand();
		} else {
			for (Card card : state.getInHand()) {
				if (card.getSuitName() == state.getOnTable().get(0)
						.getSuitName()) {
					actions.add(card);
				}
			}
			if (actions.size() == 0) {
				actions = state.getInHand();
			}
		}

		return actions;
	}

	public static Player detWinner(List<Player> players, List<Card> table, SuitName hokm) {
		int winner = 0;
		for (int i = 1; i < table.size(); i++) {
			int value = table.get(i).getValue();
			SuitName suit = table.get(i).getSuitName();
			if (table.get(winner).getSuitName() == hokm) {
				if (suit == hokm && value > table.get(winner).getValue()) {
					winner = i;
				}
			} else {
				if (suit == hokm) {
					winner = i;
				} else if (suit == table.get(winner).getSuitName()) {
					if (value > table.get(winner).getValue()) {
						winner = i;
					}
				}
			}
		}
		return players.get(winner);
	}

	public void sortHand(List<Card> hand) {
		Collections.sort(hand, new Comparator<Card>() {
			public int compare(Card c1, Card c2) {
				if (c1.getSuit() == c2.getSuit()) {
					return c1.getValue() - c2.getValue();
				}
				return c1.getSuit() - c2.getSuit();
			}
		});
	}

	public List<Player> play() {
		Card action;
		deal();
		/*
		 * for(Player player: players){ System.out.println(player.getName());
		 * for(Card card: player.getInHand()){
		 * System.out.println(card.getValueName()+" of "+card.getSuitName()); }
		 * }
		 */
		List<Card> firstFive = new ArrayList<Card>();
		for (int i = 0; i < 5; i++) {
			firstFive.add(this.players.get(0).getInHand().get(i));
		}
		sortHand(firstFive);
		System.out.println(players.get(0).getName() + " selects the Hokm... \n");
		setHokm(this.players.get(0).hokmDet(firstFive));

		while (players.get(0).getTeam().getTrickScore() < 7
				&& players.get(1).getTeam().getTrickScore() < 7
				&& !getTerminate()) {

			for (Player player : players) {
				sortHand(player.getInHand());
				this.state = new State(this.table, player.getInHand(),this.played, this.playedBy, 
						player.getDist(), player.getTeam().getTrickScore(), 
						13 - player.getInHand().size() - player.getTeam().getTrickScore(), this.hokm);
				
				action = player.action(legalActions(this.state), this.state, this.players, this.cardValue);
				
				for(Player p: this.players){
					p.getDist().played(action, player);
				}
				if(!this.table.isEmpty() && this.table.get(0).getSuitName()!=action.getSuitName()){
					player.updateSuitStatus(this.table.get(0).getSuitName());
					for(Player p: this.players){
						p.getDist().passed(this.table.get(0), player);
					}
				}
				
				player.getStateSequence().add(state);
				player.getActions().add(action);
				this.table.add(action);
				player.getInHand().remove(action); // update player's cards in
													// hand
				this.played.add(action); 
				this.playedBy.add(player);
			}

			this.winner = detWinner(this.players, this.table, this.hokm);
			winner.getTeam().updateTrickScore();// update trick-scores
			this.cardValue.updateValue(this.table);
			this.table.clear(); // removing all cards from the table

			players = GameBuilder.reorder(players, winner);
			players.get(0).getRewards().add(true);
			players.get(2).getRewards().add(true);
			players.get(1).getRewards().add(false);
			players.get(3).getRewards().add(false);

			for (Player player : players) {
				System.out.println(player.getName() + ": "
						+ player.getTeam().getTrickScore());
			}

		}

		Player tmpHakem = hakem;
		if (hakem.getTeam().getTrickScore() < 7) {
			tmpHakem = players.get((players.indexOf(hakem) + 1) % 4);
		}
		players = GameBuilder.reorder(players, tmpHakem);

		return this.players;
	}

}
