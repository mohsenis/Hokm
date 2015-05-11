package gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import controller.Player;

public class GameBuilder {
	private List<Player> players;
	final static int N_PLAYERS = 4;
	private int hakem;
	
	public GameBuilder(){
		players = new ArrayList<Player>(N_PLAYERS);
	}
	
	public List<Player> getPlayers(){
		return this.players;
	}
	
	public void setHakem(Player player){
		this.hakem=player.getIndex();
	}
	
	public int getHakem(){
		return this.hakem;
	}
	
	public void teamBuilder(List<Player> players){
		long seed = System.nanoTime();
		Collections.shuffle(players, new Random(seed));
		
		setHakem(players.get(0));
		
		players.get(0).setTeam(players.get(2));
		players.get(1).setTeam(players.get(3));
		players.get(2).setTeam(players.get(0));
		players.get(3).setTeam(players.get(1));
	}
	
	public List<Player> reorder(List<Player> players, int hakem){
		List<Player> playersNew=new ArrayList<Player>(4);
		for (int i=0; i<players.size();i++){
			int j=i+hakem;
			if (j>=players.size()){
				j=j-players.size();
			}
			playersNew.add(players.get(j));
		}
		return playersNew;
	}
	
	public String startGame(List<Player> players){
		teamBuilder(players);
		Game game = new Game(players);	
		
		while(players.get(0).getTeam().getTotalScore()<7 && 
				players.get(1).getTeam().getTotalScore()<7 && !game.getTerminate()){
			//check which team won last game and change players order if necessary
			game = new Game(players);
			
		}
		
		return "Finish";
	}
}
