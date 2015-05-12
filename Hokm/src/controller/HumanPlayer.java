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
		Scanner sc = new Scanner(System.in);
		Card card=legalActions.get(sc.nextInt());
		sc.close();
		return card;
	}

	@Override
	public SuitName hokmDet(List<Card> firstFive) {
		Scanner sc=new Scanner(System.in);
		SuitName suitName=firstFive.get(sc.nextInt()).getSuitName();
		sc.close();
		return suitName;
	}

}
