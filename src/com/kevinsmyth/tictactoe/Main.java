package com.kevinsmyth.tictactoe;

import com.kevinsmyth.tictactoe.controllers.LogController;
import com.kevinsmyth.tictactoe.views.MainForm;

/*
 * Main class (starts up the main form)
 */
public class Main {
	public static void main(String [] args){
		try {
			MainForm form = new MainForm();
			form.setVisible(true);
		}
		catch (Exception e){
			LogController.getInstance().error(e);
		}
	}
}
