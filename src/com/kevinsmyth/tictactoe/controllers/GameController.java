package com.kevinsmyth.tictactoe.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.kevinsmyth.tictactoe.models.CellType;
import com.kevinsmyth.tictactoe.models.GameBoard;
import com.kevinsmyth.tictactoe.models.GameMode;
import com.kevinsmyth.tictactoe.models.MiniMaxResult;
import com.kevinsmyth.tictactoe.models.Player;
import com.kevinsmyth.tictactoe.models.PlayerPiece;

public class GameController {
	
	private GameBoard _board;
	private Player _player1; // X
	private Player _player2; // O
	private Player _currentPlayer;
	
	public GameController(GameMode gameMode){
		_board = new GameBoard();
		_player1 = new Player(PlayerPiece.X, false);
		_currentPlayer = _player1;
		
		switch (gameMode){
			case ONEPLAYER:
				_player2 = new Player(PlayerPiece.O, true);
				break;
			case TWOPLAYER:
			default:
				_player2 = new Player(PlayerPiece.O, false);
				break;
		}
	}
	
	public GameBoard getBoard(){
		return _board;
	}
	
	public Player getCurrentPlayer(){
		return _currentPlayer;
	}
	
	public boolean isPlayer1Turn(){
		return _currentPlayer == _player1;
	}
	
	public boolean isPlayer2Turn(){
		return _currentPlayer == _player2;
	}
	
	public void placeMove(CellType cell){
		// Place move for current player
		_board.placeMove(cell, _currentPlayer);
		
		if (!_board.isGameOver()){
			// Make it the other player's turn
			_currentPlayer = getOpposingPlayer(_currentPlayer);
			
			// If it's now the AI's turn, place its optimal move
			if (_currentPlayer.isAI()){
				MiniMaxResult optimalMove = miniMax(_board, _currentPlayer);
				_board.placeMove(optimalMove.getCell(), _currentPlayer);
				
				if (!_board.isGameOver()){
					_currentPlayer = getOpposingPlayer(_currentPlayer);
				}
			}
		}
	}
	
	private Player getOpposingPlayer(Player player){
		if (player == _player1) {
			return _player2;
		}
		else {
			return _player1;
		}
	}

	/*
	 * Calculates and returns the optimal move using a MiniMax algorithm
	 */
	private MiniMaxResult miniMax(GameBoard board, Player currentPlayer){
		
		// Base case: game is over. Return the board value of this state.
		if (board.isGameOver()){
			int score = getBoardValue(board);
			return new MiniMaxResult(score, CellType.NOCELL);
		}
		
		// If game is not over, determine the optimal move
		return getOptimalMove(board, currentPlayer);
	}
	
	/*
	 * Determines optimal move for current player in given GameBoard
	 */
	private MiniMaxResult getOptimalMove(GameBoard board, Player currentPlayer){
		HashMap<CellType, GameBoard> allPossibleMoves = getAllPossibleMoves(board, currentPlayer);
		HashMap<CellType, Integer> allPossibleMoveScores = new HashMap<CellType, Integer>();
		
		for (CellType move : allPossibleMoves.keySet()){
			MiniMaxResult possibleMoveResult = miniMax(allPossibleMoves.get(move), getOpposingPlayer(currentPlayer));
			allPossibleMoveScores.put(move, possibleMoveResult.getScore());
		}
		
		// For the AI, we are trying to maximize our score
		if (currentPlayer.isAI()){
			int maxScore = Collections.max(allPossibleMoveScores.values());
			CellType optimalMove = getAnyCellTypeWithScore(allPossibleMoveScores, maxScore);
			return new MiniMaxResult(maxScore, optimalMove);
		}
		// For the user, we are trying to minimize the score
		else {
			int minScore = Collections.min(allPossibleMoveScores.values());
			CellType optimalMove = getAnyCellTypeWithScore(allPossibleMoveScores, minScore);
			return new MiniMaxResult(minScore, optimalMove);
		}
	}
	
	/*
	 * Determines board value 
	 * (positive if AI wins, negative if user wins, zero otherwise)
	 */
	private int getBoardValue(GameBoard board){
		PlayerPiece aiPiece = getAIPiece();
		if (board.isGameWon(aiPiece)){
			return 1;
		}
		else if (board.isGameLost(aiPiece)){
			return -1;
		}
		else {
			return 0;
		}
	}
	
	/*
	 * Returns the player piece associated with the AI
	 */
	private PlayerPiece getAIPiece(){
		if (_player1.isAI()){
			return _player1.getPiece();
		}
		else if (_player2.isAI()){
			return _player2.getPiece();
		}
		else {
			return null;
		}
	}
	
	/*
	 * Returns a HashMap of all possible moves, with CellType being the actual move and
	 * GameBoard being the resulting board state.
	 */
	private HashMap<CellType, GameBoard> getAllPossibleMoves(GameBoard board, Player currentPlayer) {
		HashMap<CellType, GameBoard> allPossibleMoves = new HashMap<CellType, GameBoard>();
		ArrayList<CellType> allEmptyCells = board.getAllEmptyCells();
		
		for (CellType cell : allEmptyCells){
			GameBoard possibleMove = board.clone();
			possibleMove.placeMove(cell, currentPlayer);
			allPossibleMoves.put(cell, possibleMove);
		}
		
		return allPossibleMoves;
	}
	
	/*
	 * Returns any CellType with the given score. If no CellType exists, returns null
	 */
	private CellType getAnyCellTypeWithScore(HashMap<CellType, Integer> allPossibleMoveScores, int score){
		ArrayList<CellType> optimalMoves = new ArrayList<CellType>();
		for (CellType cell : allPossibleMoveScores.keySet()){
			if (allPossibleMoveScores.get(cell) == score){
				optimalMoves.add(cell);
			}
		}
		
		// Return one of the optimal moves at random
		if (optimalMoves.size() > 0){
			int randomIndex = (int) Math.random() * optimalMoves.size();
			return optimalMoves.get(randomIndex);
		}
		
		return null;
	}
}

