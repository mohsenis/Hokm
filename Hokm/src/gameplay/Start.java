package gameplay;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;

import controller.AgentPlayer;
import controller.HumanPlayer;

public class Start {
	public static void main(String[] args){
		
		
		//GameBuilder gb = new GameBuilder();
		
		/*Scanner sc = new Scanner(System.in);
		System.out.print("Enter number of human player(s): ");  
		int nHuman=sc.nextInt(); 
		sc.close();*/
		
		int nHuman = 0;
		
		/*for(int i=0;i<nHuman;i++){
			gb.getPlayers().add(new HumanPlayer("human"+i,i));
		}
		
		for(int i=nHuman;i<GameBuilder.N_PLAYERS;i++){
			gb.getPlayers().add(new AgentPlayer("agent"+i,i));
		}*/
		
		for(int j=0; j<500; j++){
			GameBuilder gb = new GameBuilder();
			for(int i=0;i<GameBuilder.N_PLAYERS;i++){
				gb.getPlayers().add(new AgentPlayer("agent"+i,i));
			}
			
			String result = gb.startGame(gb.getPlayers());
			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("C:/hokm/hokmResults.txt", true)))) {
				out.print("("+GameBuilder.r[0]+","+GameBuilder.r[1]+")\t[");
				for(Integer[] r:GameBuilder.rs){
					out.print("("+r[0]+","+r[1]+"),");
				}
				out.println("]");
				out.close();
				
				System.out.print("("+GameBuilder.r[0]+","+GameBuilder.r[1]+")\t[");
				for(Integer[] r:GameBuilder.rs){
					System.out.print("("+r[0]+","+r[1]+"),");
				}
				out.println("]");
				
			}catch (IOException e) {
			    
			}
		}
		
		
		/*for(Player player:gb.getPlayers()){
			System.out.println(player.getName());
		}*/
		
	}
}
