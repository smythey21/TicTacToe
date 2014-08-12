package com.kevinsmyth.tictactoe.models;

/*
 * Class to store results of a MiniMax call
 */
public class MiniMaxResult {
	private int _score;
	private CellType _cell;
	
	public MiniMaxResult(int score, CellType cell){
		_score = score;
		_cell = cell;
	}
	
	public int getScore(){
		return _score;
	}
	
	public CellType getCell(){
		return _cell;
	}
	
	public void setScore(int score){
		_score = score;
	}
	
	public void setCell(CellType cell){
		_cell = cell;
	}
}
