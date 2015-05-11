package controller;

import gameplay.Card;
import gameplay.State;
import gameplay.SuitName;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	protected String name;
	protected boolean human;
	private Team team;
	protected int index;
	
	private List<Card> inHand;
	private List<State> stateSequence;
	private List<Card> actions;
	private List<Boolean> rewards;
	
	public Player(){
		this.name = "";
		this.index = 0;
		
		this.inHand = new ArrayList<Card>();
		this.stateSequence = new ArrayList<State>();
		this.actions = new ArrayList<Card>();
		this.rewards = new ArrayList<Boolean>();
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
	
	public abstract Card action(List<Card> legalActions);
	
	public abstract SuitName hokmDet(List<Card> firstFive);
	
}
