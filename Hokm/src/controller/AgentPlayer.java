package controller;

import gameplay.Card;
import gameplay.CardValue;
import gameplay.State;
import gameplay.SuitName;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import ai.AI;

public class AgentPlayer extends Player {

	public AgentPlayer(String name,int i) {
	    super();
	    
	    this.index = i;
	    this.name = name;
	    this.human = false;
	}

	@Override
	public Card action(List<Card> legalActions, State state, List<Player> players, CardValue cardValue) {
		System.out.println("\nThe cards in agent's hand:");
		for (Card card:this.getInHand()){
			System.out.println(this.getInHand().indexOf(card)+1+") "+card.toString());
		}
		Card card = AI.takeAction(legalActions, state, players, this, cardValue);
		System.out.println("\n"+this.getName()+" plays: " + card.toString());
		System.out.println("Press \"Enter\" to continue...");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return card;
	}

	@Override
	public SuitName hokmDet(List<Card> firstFive) {
		for(Card card: firstFive){
			System.out.println((firstFive.indexOf(card)+1)+") "+card.toString());
		}
		SuitName hokm = AI.hokm(firstFive);
		System.out.println("\nHokm: "+ hokm);
		System.out.println("\nPress \"Enter\" to continue...");
			try {
				System.in.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return hokm;
	}

}
