package gameplay;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import controller.Player;

public class Game {
	private List<Player> players;
	private Deck deck;
	private int team1Tricks;
	private int team2Tricks;
	private State state;
	private int winner;
	private boolean terminate;
	private int hakemInd;
	
	public Game(List<Player> players){
		this.players = players;
		this.deck = new Deck();
		team1Tricks = 0;
		team2Tricks = 0;
		terminate = false;
	}
	
	public void shuffle(Deck deck){
		long seed = System.nanoTime();
		Collections.shuffle(deck.getDeck(), new Random(seed));
	}
	
	public boolean getTerminate(){
		return this.terminate;
	}
	
	public void Terminate(){
		this.terminate = !this.terminate;
	}
		
}
