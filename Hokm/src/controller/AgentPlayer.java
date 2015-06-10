package controller;

import gameplay.Card;
import gameplay.CardValue;
import gameplay.State;
import gameplay.SuitName;

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
		/*System.out.println("\nThe cards in agent's hand:");
		for (Card card:this.getInHand()){
			System.out.println(this.getInHand().indexOf(card)+1+") "+card.toString());
		}*/
		Card card;
		if(this.index==0||this.index==2){
			card = AI.getAction(legalActions, state, players, this, cardValue);
		}else{
			card = AI.takeAction(legalActions, state, players, this, cardValue);
		}
		/*if(players.indexOf(this)==1true){
			card = AI.getAction(legalActions, state, players, this, cardValue);
		}else{
			card = AI.takeAction(legalActions, state, players, this, cardValue);
		}*/
		System.out.println("\n"+this.getName()+" plays: " + card.toString());
		System.out.println("Press \"Enter\" to continue...");
		Scanner scanner = new Scanner(System.in); 
		scanner.nextLine();
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
		Scanner scanner = new Scanner(System.in); 
		scanner.nextLine();
		return hokm;
	}
	
	 

}