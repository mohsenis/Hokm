package controller;

import gameplay.Card;
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
	public Card action(List<Card> legalActions) {
		System.out.println("available moves are:");
		for(Card card: legalActions){
			System.out.println(card.getValueName()+" of "+card.getSuitName());
		}
		System.out.print("your move: ");
		Scanner sc = new Scanner(System.in);
		int action = sc.nextInt();
		Card card=legalActions.get(action);
		return card;
	}

	@Override
	public SuitName hokmDet(List<Card> firstFive) {
		
		for(Card card: firstFive){
			System.out.println(card.getValueName()+" of "+card.getSuitName());
		}
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Select hokm: ");
		int hokm = sc.nextInt();
		SuitName suitName=firstFive.get(hokm).getSuitName();
		return suitName;
	}

}
