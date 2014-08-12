package com.kevinsmyth.tictactoe.models;

/*
 *	Class to store player information
 */
public class Player {
	private PlayerPiece _playerPiece;
	private boolean _isAI;
	
	public Player(PlayerPiece playerPiece, boolean isAI){
		_playerPiece = playerPiece;
		_isAI = isAI;
	}
	
	public boolean isAI(){
		return _isAI;
	}
	
	public PlayerPiece getPiece(){
		return _playerPiece;
	}
}
