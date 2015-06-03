package ai;

import gameplay.Card;
import gameplay.SuitName;
import gameplay.ValueName;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.CombinatoricsUtils;

import controller.Player;

public class CardDist {
	private int[][] cards;
	private int[][] places;
	
	public CardDist(CardDist other){
		cards = new int[4][13];
		for(int i=0;i<4;i++){
			for(int j=0;j<13;j++){
				cards[i][j]=other.cards[i][j];
			}
		}
		
		places = new int[4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				places[i][j]=other.places[i][j];
			}
		}
	}
	
	public CardDist(){
		cards = new int[4][13];
		for(int i=0;i<4;i++){
			for(int j=0;j<13;j++){
				cards[i][j]=1;
			}
		}
		
		places = new int[4][4];
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++){
				places[i][j]=13;
			}
		}
	}
	
	public void played(Card card, Player player){
		int s = card.getSuit();
		int v = card.getValue()-2;
		int p = player.getIndex();
		
		this.cards[s][v]=0;
		for(int i=0;i<4;i++){
			if (this.places[i][p]!=0){
				this.places[i][p] -= 1;
			}
		}
		
		int sum=0;
		for (int i=0;i<13;i++)
			sum+=this.cards[s][i];
		
		if (sum==0)
			for (int i=0;i<4;i++)
				this.places[s][i]=0;
	}
	
	public void passed(Card card, Player player){
		int s = card.getSuit();
		int p = player.getIndex();
		
		this.places[s][p]=0;
	}
	
	public double prSuit(SuitName suit, Player player){
		double pr = 0;
		int s = suit.getSuit();
		int p = player.getIndex();
		int playerPlaces = this.places[s][p];
		
		int sumCards = 0;
		for(int i=0;i<13;i++){
			sumCards += this.cards[s][i];
		}
		int sumPlaces = 0;
		for(int i=0;i<4;i++){
			sumPlaces += this.places[s][i];
		}
		
		pr = 1 - ((double)choose((sumPlaces-sumCards),playerPlaces))/choose(sumPlaces,playerPlaces);
		return pr;
	}
	
	public double prSuits(List<SuitName> suits, Player player){
		double pr = 0;
		if(suits.size()==2){
			double pr1 = prSuit(suits.get(0),player);
			double pr2 = prSuit(suits.get(1),player);
			pr = pr1 + pr2 - pr1*pr2;
		}else if(suits.size()==3){
			double pr1 = prSuit(suits.get(0),player);
			double pr2 = prSuit(suits.get(1),player);
			double pr3 = prSuit(suits.get(2),player);
			pr = pr1 + pr2 + pr3 - pr1*pr2 - pr1*pr3 - pr2*pr3 + pr1*pr2*pr3;
		}
		return pr;
	}
	
	public double prGreater(Card card, Player player){
		double pr = 0;
		int s = card.getSuit();
		int v = card.getValue()-2;
		int p = player.getIndex();
		int playerPlaces = this.places[s][p];
		
		int sumCards = 0;
		for(int i=v+1;i<13;i++){
			sumCards += this.cards[s][i];
		}
		int sumPlaces = 0;
		for(int i=0;i<4;i++){
			sumPlaces += this.places[s][i];
		}
		
		pr = 1 - ((double)choose((sumPlaces-sumCards),playerPlaces))/choose(sumPlaces,playerPlaces);
		return pr;
	}
	
	public double prLess(Card card, Player player){
		double pr = 0;
		int s = card.getSuit();
		int v = card.getValue()-2;
		int p = player.getIndex();
		int playerPlaces = this.places[s][p];
		
		int sumCards = 0;
		for(int i=v-1;i>=0;i--){
			sumCards += this.cards[s][i];
		}
		int sumPlaces = 0;
		for(int i=0;i<4;i++){
			sumPlaces += this.places[s][i];
		}
		
		pr = 1 - ((double)choose((sumPlaces-sumCards),playerPlaces))/choose(sumPlaces,playerPlaces);
		return pr;
	}
	
	public long choose(int n, int k){
		long c = 0;
		if(k==0){
			return 1;
		}
		if(n<k){
			return 0;
		}
		
		c = CombinatoricsUtils.binomialCoefficient(n,k);
		return c;
	}
	
	public Card smallestSuit(SuitName suit){
		Card card;
		int s = suit.getSuit();
		int value = 2;
		for(int i=0;i<13;i++){
			if(this.cards[s][i]==1){
				value = i+2;
				break;
			}
		}
		card = new Card(suit, ValueName.getValueName(value));
		return card;
	}
	
	public Card smallestLess(Card c){
		Card card;
		int s = c.getSuit();
		int v = c.getValue()-2;
		int value = 2;
		for(int i=0;i<v;i++){
			if(this.cards[s][i]==1){
				value = i+2;
				break;
			}
		}
		card = new Card(c.getSuitName(), ValueName.getValueName(value));
		return card;
	}
	
	public Card smallestGreater(Card c){
		Card card;
		int s = c.getSuit();
		int v = c.getValue()-2;
		int value = 2;
		for(int i=v+1;i<13;i++){
			if(this.cards[s][i]==1){
				value = i+2;
				break;
			}
		}
		card = new Card(c.getSuitName(), ValueName.getValueName(value));
		return card;
	}
	
	public Card smallestSuits(List<SuitName> suits, Player player){
		Card card = new Card(SuitName.Clubs, ValueName.Two);
		Card c;
		int value = 14;
		for(SuitName suit: suits){
			if(prSuit(suit, player)==0){
				continue;
			}
			c = smallestSuit(suit);
			if(c.getValue()<value){
				value = c.getValue();
				card = c;
			}
		}
		return card;
	}
	
	public List<Card> possibleActions(Player player){
		int p=player.getIndex();
		List<Card> remainingCards=new ArrayList<Card>();
		for(SuitName suit:SuitName.values()){
			int i=suit.getSuit();
			if (this.places[i][p]==0)
				continue;
			for (int j=0;j<13;j++){
				if (this.cards[i][j]==1){
					remainingCards.add(new Card(suit,ValueName.getValueName(j+2)));
				}
			}
		}
		return remainingCards;
	}
}
