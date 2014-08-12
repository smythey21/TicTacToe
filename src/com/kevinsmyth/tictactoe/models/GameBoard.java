package com.kevinsmyth.tictactoe.models;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Class that models the game board
 */
public class GameBoard implements Cloneable {
	// Global variable to store the actual board (a set of cells and piece types)
	private HashMap<CellType, PlayerPiece> _board;
	
	public GameBoard(){
		_board = new HashMap<CellType, PlayerPiece>();
		
		// Set all cells to empty
		for (CellType cellType: CellType.values()){
			if (cellType != CellType.NOCELL){	// NOCELL is not to be part of our board
				_board.put(cellType, PlayerPiece.EMPTY);
			}
		}
	}
	
	public GameBoard(HashMap<CellType, PlayerPiece> board){
		_board = board;
	}
	
	public void placeMove(CellType cell, Player player){
		_board.put(cell, player.getPiece());
	}
	
	public PlayerPiece getBoardPiece(CellType cell){
		return _board.get(cell);
	}
	
	public boolean isBoardCellEmpty(CellType cell){
		return getBoardPiece(cell) == PlayerPiece.EMPTY;
	}
	
	public boolean isGameOver(){
		boolean _boardStateHasEmptySlots = false;
		
		for (PlayerPiece piece : _board.values()){
			if (piece == PlayerPiece.EMPTY){
				_boardStateHasEmptySlots = true;
			}
		}
		
		return !_boardStateHasEmptySlots || isGameWon(PlayerPiece.X) || isGameWon(PlayerPiece.O);
	}
	
	public boolean isGameLost(PlayerPiece piece){
		switch (piece){
			case X:
				return isGameWon(PlayerPiece.O);
			case O:
				return isGameWon(PlayerPiece.X);
			default:
				return false;
		}
	}
	
	public boolean isGameDraw(){
		return isGameOver() && !isGameWon();
	}
	
	/*
	 * Returns true if any player has won
	 */
	public boolean isGameWon(){
		return isGameWon(PlayerPiece.X) || isGameWon(PlayerPiece.O);
	}
	
	/*
	 * Returns true if given player's piece has won
	 */
	public boolean isGameWon(PlayerPiece piece){
		return  isGameWonByHorizontal(piece) || 
				isGameWonByVertical(piece) ||
				isGameWonByDiagonal(piece);
	}
	
	private boolean isGameWonByHorizontal(PlayerPiece piece){
		return  isGameWonByTopHorizontal(piece) ||
				isGameWonByMiddleHorizontal(piece) ||
				isGameWonByBottomHorizontal(piece);
	}
	
	private boolean isGameWonByVertical(PlayerPiece piece){
		return  isGameWonByLeftVertical(piece) ||
				isGameWonByMiddleVertical(piece) ||
				isGameWonByRightVertical(piece);
	}
	
	private boolean isGameWonByDiagonal(PlayerPiece piece){
		return  isGameWonByTopLeftToBottomRightDiagonal(piece) ||
				isGameWonByBottomLeftToTopRightDiagonal(piece);
	}
	
	private boolean isGameWonByTopHorizontal(PlayerPiece piece){
		return  _board.get(CellType.TOPLEFT) == piece && 
				_board.get(CellType.TOPMIDDLE) == piece && 
				_board.get(CellType.TOPRIGHT) == piece;
	}
	
	private boolean isGameWonByMiddleHorizontal(PlayerPiece piece){
		return  _board.get(CellType.MIDDLELEFT) == piece && 
				_board.get(CellType.MIDDLE) == piece && 
				_board.get(CellType.MIDDLERIGHT) == piece;
	}
	
	private boolean isGameWonByBottomHorizontal(PlayerPiece piece){
		return  _board.get(CellType.BOTTOMLEFT) == piece && 
				_board.get(CellType.BOTTOMMIDDLE) == piece && 
				_board.get(CellType.BOTTOMRIGHT) == piece;
	}
	
	private boolean isGameWonByLeftVertical(PlayerPiece piece){
		return  _board.get(CellType.TOPLEFT) == piece && 
				_board.get(CellType.MIDDLELEFT) == piece && 
				_board.get(CellType.BOTTOMLEFT) == piece;
	}
	
	private boolean isGameWonByMiddleVertical(PlayerPiece piece){
		return  _board.get(CellType.TOPMIDDLE) == piece && 
				_board.get(CellType.MIDDLE) == piece && 
				_board.get(CellType.BOTTOMMIDDLE) == piece;
	}

	private boolean isGameWonByRightVertical(PlayerPiece piece){
		return  _board.get(CellType.TOPRIGHT) == piece && 
				_board.get(CellType.MIDDLERIGHT) == piece && 
				_board.get(CellType.BOTTOMRIGHT) == piece;
	}
	
	private boolean isGameWonByTopLeftToBottomRightDiagonal(PlayerPiece piece){
		return  _board.get(CellType.TOPLEFT) == piece && 
				_board.get(CellType.MIDDLE) == piece && 
				_board.get(CellType.BOTTOMRIGHT) == piece;
	}
	
	private boolean isGameWonByBottomLeftToTopRightDiagonal(PlayerPiece piece){
		return  _board.get(CellType.TOPRIGHT) == piece && 
				_board.get(CellType.MIDDLE) == piece &&
				_board.get(CellType.BOTTOMLEFT) == piece;
	}
	
	/*
	 * Returns true if cell is part of a winning 3 in a row combination
	 */
	public boolean isWinningCell(CellType cell){
		for (PlayerPiece piece : PlayerPiece.values()) {
			switch (cell){
		    	case TOPLEFT:
		    		if (isGameWonByTopHorizontal(piece) || isGameWonByLeftVertical(piece) || 
		    				isGameWonByTopLeftToBottomRightDiagonal(piece)){
		    			return true;
		    		}
		    		break;
		    	case TOPMIDDLE:
		    		if (isGameWonByTopHorizontal(piece) || isGameWonByMiddleVertical(piece)){
		    			return true;
		    		}
		    		break;
		    	case TOPRIGHT:
		    		if (isGameWonByTopHorizontal(piece) || isGameWonByRightVertical(piece) || 
		    				isGameWonByBottomLeftToTopRightDiagonal(piece)){
		    			return true;
		    		}
		    		break;
		    	case MIDDLELEFT:
		    		if (isGameWonByMiddleHorizontal(piece) || isGameWonByLeftVertical(piece)){
		    			return true;
		    		}
		    		break;
		    	case MIDDLE:
		    		if (isGameWonByMiddleHorizontal(piece) || isGameWonByMiddleVertical(piece) || 
		    				isGameWonByTopLeftToBottomRightDiagonal(piece) || 
		    				isGameWonByBottomLeftToTopRightDiagonal(piece)){
		    			return true;
		    		}
		    		break;
		    	case MIDDLERIGHT:
		    		if (isGameWonByMiddleHorizontal(piece) || isGameWonByRightVertical(piece)){
		    			return true;
		    		}
		    		break;
		    	case BOTTOMLEFT:
		    		if (isGameWonByBottomHorizontal(piece) || isGameWonByLeftVertical(piece) || 
		    				isGameWonByBottomLeftToTopRightDiagonal(piece)){
		    			return true;
		    		}
		    		break;
		    	case BOTTOMMIDDLE:
		    		if (isGameWonByBottomHorizontal(piece) || isGameWonByMiddleVertical(piece)){
		    			return true;
		    		}
		    		break;
		    	case BOTTOMRIGHT:
		    		if (isGameWonByBottomHorizontal(piece) || isGameWonByRightVertical(piece) || 
		    				isGameWonByTopLeftToBottomRightDiagonal(piece)){
		    			return true;
		    		}
		    		break;
		    }
		}
		
		return false;
	}
	
	public ArrayList<CellType> getAllEmptyCells(){
		ArrayList<CellType> allEmptyCells = new ArrayList<CellType>();
		for (CellType cell : CellType.values()){
			if (_board.get(cell) == PlayerPiece.EMPTY){
				allEmptyCells.add(cell);
			}
		}
		return allEmptyCells;
	}
	
	@SuppressWarnings("unchecked")
	public GameBoard clone() {
		return new GameBoard((HashMap<CellType, PlayerPiece>)_board.clone());
	}
}
