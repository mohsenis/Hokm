package controller;

import gameplay.Card;
import gameplay.CardValue;
import gameplay.State;
import gameplay.SuitName;

import java.util.ArrayList;
import java.util.List;

import ai.CardDist;

public abstract class Player {
	protected String name;
	protected boolean human;
	private Team team;
	protected int index;
	
	private List<Card> inHand;
	private List<State> stateSequence;
	private List<Card> actions;
	private List<Boolean> rewards;
	
	private List<Boolean> suitStatus;
	private CardDist cardsDist;
	
	public Player(){
		this.name = "";
		
		this.inHand = new ArrayList<Card>();
		this.stateSequence = new ArrayList<State>();
		this.actions = new ArrayList<Card>();
		this.rewards = new ArrayList<Boolean>();
		initSuitStatus();	
		
		this.cardsDist = new CardDist();
	}
	
	public CardDist getDist(){
		return this.cardsDist;
	}

	public void initSuitStatus(){
		this.suitStatus = new ArrayList<Boolean>();
		for(@SuppressWarnings("unused") SuitName suit: SuitName.values()){
			this.suitStatus.add(true);
		}
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public boolean isHuman(){
		return this.human;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Team getTeam(){
		return this.team;
	}
	
	public void setTeam(Team team){
		this.team = team;
	}
	
	public List<Card> getInHand(){
		return this.inHand;
	}
	
	public List<State> getStateSequence(){
		return this.stateSequence;
	}
	
	public List<Card> getActions(){
		return this.actions;
	}
	
	public List<Boolean> getRewards(){
		return this.rewards;
	}
	
	public Boolean getSuitStatus(SuitName suit){
		return this.suitStatus.get(suit.getSuit());
	}
	
	/* checks if the player has the suit in hand or not */
	public void updateSuitStatus(SuitName suit){
		int i = suit.getSuit();
		this.suitStatus.set(i, false);
	}
	
	public abstract Card action(List<Card> legalActions, State state, List<Player> players, CardValue cardValue);
	
	public abstract SuitName hokmDet(List<Card> firstFive);
	
}
