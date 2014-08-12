package com.kevinsmyth.tictactoe.controllers;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class LogController {
	private static Logger _logger = null;
	
	private LogController(){
	}
	
	public static Logger getInstance( ) {
		if (_logger == null){
			_logger = LogManager.getLogger("tictactoe"); 
		}
		
		return _logger;
	}
}
