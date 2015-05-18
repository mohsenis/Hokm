package controller;

import gameplay.Card;
import gameplay.State;
import gameplay.SuitName;

import java.util.List;

import ai.AI;

public class AgentPlayer extends Player {

	public AgentPlayer(String name,int i) {
	    super();
	    
	    this.index = i;
	    this.name = name;
	    this.human = false;
	}

	public Card action(List<Card> legalActions, State state, List<Player> players) {
		Card card = AI.takeAction(legalActions, state, players, this);
		return card;
	}

	@Override
	public SuitName hokmDet(List<Card> firstFive) {
		// To be implemented.
		return null;
	}

	@Override
	public Card action(List<Card> legalActions) {
		// TODO Auto-generated method stub
		return null;
	}

}
