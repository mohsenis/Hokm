package gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import controller.Player;

public class Game {
	private List<Player> players;
	private Deck deck;
	/*
	 * private int team1Tricks; private int team2Tricks;
	 */
	private State state;
	private int winner;
	private boolean terminate;
	private SuitName hokm;
	private List<Card> table;
	private List<Card> played;

	/* private int hakemInd; */

	public Game(List<Player> players) {
		this.players = players;
		this.deck = new Deck();
		/*
		 * team1Tricks = 0; team2Tricks = 0;
		 */
		terminate = false;
		this.table = new ArrayList<Card>();
		this.played = new ArrayList<Card>();
	}

	public void setHokm(SuitName suitName) {
		this.hokm = suitName;
	}

	public SuitName getHokm() {
		return this.hokm;
	}

	public void shuffle(Deck deck) {
		long seed = System.nanoTime();
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
		for (Player player : this.players) {
			for (int i = 2; i <= 14; i++) {
				player.getInHand().add(this.deck.getDeck().get(j));
				j++;
			}
		}
	}

	public List<Card> legalActions(State state) {
		List<Card> actions = new ArrayList<Card>();
		System.out.println("on table: ");
		for(Card card: state.getOnTable()){
			System.out.println(card.getValueName() +" of "+card.getSuitName());
		}
		
		if (this.table.isEmpty()) {
			actions = state.getInHand();
		} else {
			for (Card card : state.getInHand()) {
				if(card.getSuitName()==state.getOnTable().get(0).getSuitName()){
					actions.add(card);
				}
			}
			if(actions.size()==0){
				actions = state.getInHand();
			}
		}
		
		return actions;
	}

	public int detWinner(List<Card> table, SuitName hokm) {
		int winner = 0;
		for (int i=1;i<GameBuilder.N_PLAYERS;i++){
			int value = table.get(i).getValue();
			SuitName suit = table.get(i).getSuitName();
			if (table.get(winner).getSuitName()==hokm){
				if (suit==hokm && value>table.get(winner).getValue()){
					winner=i;
				}
			}else{
				if (suit==hokm){
					winner=i;
				}else if(suit==table.get(winner).getSuitName()){
					if (value>table.get(winner).getValue()){
						winner=i;
					}
				}
			}
		}
		return winner;
	}
	
	public void sortHand(List<Card> hand){
		Collections.sort(hand, new Comparator<Card>(){
            public int compare(Card c1, Card c2) {
            	if(c1.getSuit()==c2.getSuit()){
            		return c1.getValue() - c2.getValue();
            	}
                return c1.getSuit()- c2.getSuit();
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
		List<Card> firstFive = new ArrayList<Card>(5);
		for (int i = 0; i < 5; i++) {
			firstFive.add(this.players.get(0).getInHand().get(i));
		}
		sortHand(firstFive);
		System.out.println(players.get(0).getName()+", select the Hokm:");
		setHokm(this.players.get(0).hokmDet(firstFive));
		
		while (players.get(0).getTeam().getTrickScore() < 7
				&& players.get(1).getTeam().getTrickScore() < 7
				&& !getTerminate()) {

			for (Player player : players) {
				sortHand(player.getInHand());
				System.out.println(player.getName()+" cards:");
				for(Card card: player.getInHand()){
					 System.out.println(card.getValueName()+" of "+card.getSuitName()); 
				}
				this.state = new State(this.table, player.getInHand(),
						this.played, player.getTeam().getTrickScore(), player
								.getInHand().size()
								- player.getTeam().getTrickScore(), this.hokm);

				action = player.action(legalActions(this.state));
				player.getStateSequence().add(state);
				player.getActions().add(action);
				this.table.add(action);
				player.getInHand().remove(action); // update player's cards in
													// hand
				this.played.add(action); // adding card on the table to played-list 
			}

			this.winner = detWinner(this.table, this.hokm);
			players.get(winner).getTeam().updateTrickScore();// update trick-scores
			this.table.clear(); // removing all cards from the table
			
			players = GameBuilder.reorder(players, winner);
			players.get(0).getRewards().add(true);
			players.get(2).getRewards().add(true);
			players.get(1).getRewards().add(false);
			players.get(3).getRewards().add(false);
			
			for(Player player: players){
				System.out.println(player.getName()+": "+player.getTeam().getTrickScore());
			}
			
		}

		return this.players;
	}

}
