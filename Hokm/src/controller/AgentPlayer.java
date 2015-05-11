package controller;

import gameplay.Card;
import gameplay.SuitName;

import java.util.List;

public class AgentPlayer extends Player {

	public AgentPlayer(String name,int i) {
	    super();
	    
	    this.index = i;
	    this.name = name;
	    this.human = false;
	}

	@Override
	public Card action(List<Card> legalActions) {
		// To be implemented.
		return null;
	}

	@Override
	public SuitName hokmDet(List<Card> firstFive) {
		// To be implemented.
		return null;
	}

}
