
package connect.four.player;

import connect.four.board.Board;
import connect.four.board.ReadableBoard;
import connect.four.board.ReadWritableBoard;
import connect.four.Game;
import java.util.Arrays;
import java.util.Random;


public class ComputerPlayer implements Player {
    int m_depth;
    public ComputerPlayer() {
        m_depth = 6;
    }
    public ComputerPlayer(int depth) {
        m_depth = depth;
    }
    @Override public String getName() {
        return "Computer";
    }

    @Override public void performPlay(ReadWritableBoard board) {
        int l = board.getWidth();
	int m = board.getHeight();
        if (board.getMoveCount() == 0) {
            board.play((new Random()).nextInt(l), this);
        } 
        
        
        else {
        	
        	System.out.println("Get Here");     	
        	
        	int pos = -1;
        	Player self = getSelf(board);
        	
        	for( int i = 0 ; i < l ; i++){
        		if (board.whoPlayed(i, m-1) != null) continue;
        		Board temp = new Board(board);
        		Board temp2 = new Board(board);        		
        		temp.play(i, getOpponent(board));
        		temp2.play(i, self);
        		
        		if(Game.detectWinner(temp, 4) != null){
        			System.out.println("Check1 blocked" + i);
        			pos = i;
        		}
        		
        		if(Game.detectWinner(temp2, 4) != null){
        			System.out.println("Check2 win" + i);
        			pos = i;
        		}
        		
        	}
        	
        	if(pos != -1){
        		board.play(pos, this);
        		System.out.println("Blocked or win move " + pos);
        	}
        	
        	else{
        	
        		Player opponent = getOpponent(board);
        		int maxMove = (new Random()).nextInt(l);
        		long maxScore = scoreMove(maxMove, m_depth, board, opponent);
        		long[] scores = new long[l];
        		for (int i = 0; i != l; ++i) {
        			if (board.whoPlayed(i, m-1) != null) continue;
        			long iScore = scoreMove(i, m_depth, board, opponent);
        			if (iScore > maxScore) {
        				maxMove = i;
        				maxScore = iScore;
        			}
        			scores[i] = iScore;
        		}
        		while (board.whoPlayed(maxMove, m-1) != null) {
        			maxMove = (maxMove+1)%l;
        		}
        		System.out.println(Arrays.toString(scores));
        		board.play(maxMove, this);
        	}
        }
    }

    private long scoreMove(int x, int depth, ReadableBoard board, Player opponent) {
        int m = board.getHeight();
        Player self = getSelf(board);
        if (board.whoPlayed(x, m-1) != null) return 0;
        Board myMove = new Board(board);
        myMove.play(x, self);
        int l = myMove.getWidth();
        long score = 0;
        if (Game.detectWinner(myMove, 4) == self) {
            score += Math.pow(l, depth);
        } else if (depth != 0) {
            for (int i = 0; i != l; ++i) {
                if (myMove.whoPlayed(i, m-1) != null) continue;
                Board nextMove = new Board(myMove);
                nextMove.play(i, opponent);
                if (Game.detectWinner(nextMove, 4) == opponent) {
		    score -= Math.pow(l, depth-1);
		} else {
                    for (int j = 0; j != l; ++j) {
                        score += scoreMove(j, depth - 2, nextMove, opponent);
                    }
                }
            }
        }
        return score;
    }

    private Player getOpponent(ReadableBoard board) {
        int l = board.getWidth();
        int m = board.getHeight();
        for (int i = 0; i != l; ++i) {
            for (int j = 0; j != m; ++j) {
                Player here = board.whoPlayed(i, j);
                if (here != null && !here.getName().equals("Computer")) {
                    return here;
                }
            }
        }
        throw new Error("Can't call getOpponent on first turn.");
    }
    
    private Player getSelf(ReadableBoard board) {
        int l = board.getWidth();
        int m = board.getHeight();
        for (int i = 0; i != l; ++i) {
            for (int j = 0; j != m; ++j) {
                Player here = board.whoPlayed(i, j);
                if (here != null && here.getName().equals("Computer")) {
                    return here;
                }
            }
        }
        return this;
    }

}
