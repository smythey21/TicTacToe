package com.kevinsmyth.tictactoe.views;

import java.awt.*;
import javax.swing.*;

import com.kevinsmyth.tictactoe.models.GameMode;

/*
 * Main application form. Displays panels using a card layout.
 */
@SuppressWarnings("serial")
public class MainForm extends JFrame {	
	private JPanel _cards; 
	
	private final static String MENUPANEL = "Menu";
	private final static String GAMEBOARDPANEL = "Game Board";
	private final static String TITLE = "Tic Tac Toe";
    
	public MainForm() {
		MenuPanel menuPanel = new MenuPanel(this);
		menuPanel.setName(MENUPANEL);
		
		_cards = new JPanel(new CardLayout());
		_cards.add(menuPanel, MENUPANEL);
		
		Container contentPane = this.getContentPane();
        contentPane.add(_cards);
        
        this.pack();
        this.setVisible(true);
        this.setTitle(TITLE);
	}
	
	public void displayGameBoard(GameMode gameMode){
		GameBoardPanel gameBoard = new GameBoardPanel(gameMode, this);
		gameBoard.setName(GAMEBOARDPANEL);
		_cards.add(gameBoard, GAMEBOARDPANEL);
		this.pack();
		CardLayout cardLayout = (CardLayout)(_cards.getLayout());
		cardLayout.show(_cards, GAMEBOARDPANEL);
	}
	
	public void displayMenu(){
		CardLayout cardLayout = (CardLayout)(_cards.getLayout());
		cardLayout.show(_cards, MENUPANEL);
		
		// Remove GameBoardPanel from cards to prevent any memory leaks.
		// (Java's garbage collection will take care of the rest)
		_cards.remove(getCard(GAMEBOARDPANEL));
	}
	
	private Component getCard(String cardName){
		for (Component card : _cards.getComponents()){
			if (card.getName() == cardName){
				return card;
			}
		}
		
		return null;
	}
}

