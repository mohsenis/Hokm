package gameplay;

import java.util.Scanner;

import controller.AgentPlayer;
import controller.HumanPlayer;

public class Start {
	public static void main(String[] args){
		GameBuilder gb = new GameBuilder();
		
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter number of human player(s): ");  
		int nHuman=sc.nextInt(); 
		sc.close();
		//Check for number of player no greater than 4
		
		for(int i=0;i<nHuman;i++){
			gb.getPlayers().add(new HumanPlayer("human"+i,i));
		}
		
		for(int i=nHuman;i<GameBuilder.N_PLAYERS;i++){
			gb.getPlayers().add(new AgentPlayer("agent"+i,i));
		}
		
		gb.startGame(gb.getPlayers());
		
		/*for(Player player:gb.getPlayers()){
			System.out.println(player.getName());
		}*/
		
	}
}
