package controller;

import gameplay.Card;
import gameplay.State;
import gameplay.SuitName;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
	protected String name;
	private Player teammate;
	protected boolean human;
	
	private List<Card> inHand;
	private List<State> stateSequence;
	private List<Card> actions;
	
	public Player(){
		this.name = "";
		
		this.inHand = new ArrayList<Card>();
		this.stateSequence = new ArrayList<State>();
		this.actions = new ArrayList<Card>();
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
	
	public Player getTeammate(){
		return this.teammate;
	}
	
	public void setTeammate(Player teammate){
		this.teammate = teammate;
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
	
	public abstract Card action(List<Card> legalActions);
	
	public abstract SuitName hokmDet(List<Card> firsFive);
	
}
