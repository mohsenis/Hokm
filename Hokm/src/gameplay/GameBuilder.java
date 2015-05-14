package gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import controller.Player;
import controller.Team;

public class GameBuilder {
	private List<Player> players;
	final static int N_PLAYERS = 4;
	//private int hakem;
	
	public GameBuilder(){
		players = new ArrayList<Player>(N_PLAYERS);
	}
	
	public List<Player> getPlayers(){
		return this.players;
	}
	
	/*public void setHakem(Player player){
		this.hakem=player.getIndex();
	}
	
	public int getHakem(){
		return this.hakem;
	}*/
	
	public void teamBuilder(List<Player> players){
		Team team;
		long seed = System.nanoTime();
		Collections.shuffle(players, new Random(seed));
		
		//setHakem(players.get(0));
		
		team = new Team(players.get(0), players.get(2));
		players.get(0).setTeam(team);
		players.get(2).setTeam(team);
		team = new Team(players.get(1), players.get(3));
		players.get(1).setTeam(team);
		players.get(3).setTeam(team);
	}
	
	public static List<Player> reorder(List<Player> players, Player player){
		int hakem = players.indexOf(player);
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
						
			players = game.play();
			if(players.get(1).getTeam().getTrickScore()==0){
				if(players.get(0).equals(game.getHakem())){
					players.get(0).getTeam().updateTotalScore(false);
				}else{
					players.get(0).getTeam().updateTotalScore(true);
				}
			}else{
				players.get(0).getTeam().updateTotalScore();
			}
			
			System.out.println("Total score is "+players.get(0).getTeam().getTotalScore()+" to "+players.get(1).getTeam().getTotalScore());
			game = new Game(players);
		}
		
		return "Winner is "+players.get(0).getName()+" and "+players.get(2).getName();
	}
}
