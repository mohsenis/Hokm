package controller;

import gameplay.Card;
import gameplay.CardValue;
import gameplay.State;
import gameplay.SuitName;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends Player {

	public HumanPlayer(String name,int i) {
	    super();
	    
	    this.index=i;
	    this.name = name;
	    this.human = true;
	}

	@Override
	public Card action(List<Card> legalActions, State state, List<Player> players, CardValue cardValue) {
		System.out.println("available moves are:");
		for(Card card: legalActions){
			System.out.println((legalActions.indexOf(card)+1)+") "+card.toString());
		}
		System.out.print("your move: ");
		Scanner sc = new Scanner(System.in);
		int action = sc.nextInt()-1;
		Card card=legalActions.get(action);
//		sc.close();
		return card;
	}

	@Override
	public SuitName hokmDet(List<Card> firstFive) {
		
		for(Card card: firstFive){
			System.out.println((firstFive.indexOf(card)+1)+") "+card.toString());
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Select hokm: ");
		System.out.println("1: Spades, 2: Hearts, 3: Clubs, 4: Diamonds");
		int hokm = sc.nextInt()-1;
		SuitName suitName=firstFive.get(hokm).getSuitName();
//		sc.close();
		return suitName;
	}

}
