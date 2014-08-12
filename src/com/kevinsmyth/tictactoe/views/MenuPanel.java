package com.kevinsmyth.tictactoe.views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.kevinsmyth.tictactoe.controllers.LogController;
import com.kevinsmyth.tictactoe.models.GameMode;

/*
 *	Class to display the main menu. Directs players to the appropriate game screen.
 */
@SuppressWarnings("serial")
public class MenuPanel extends JPanel implements MouseListener, MouseMotionListener{
	// Parent form. We will use this to display the game panel
	private MainForm _mainForm;
	
	// Images needed for display
	private BufferedImage _backgroundImage;
	private BufferedImage _titleImage;
	private BufferedImage _playerOneImage;
	private BufferedImage _playerOneHighlightedImage;
	private BufferedImage _playerTwoImage;
	private BufferedImage _playerTwoHighlightedImage;
	
	// Global variables to store the player region images to display
	// (Either highlighted or not depending on mouse position)
	private BufferedImage _currentPlayerOneImage;
	private BufferedImage _currentPlayerTwoImage;
	
	// Define image locations for drawing purposes
	private final Point TITLEDRAWLOCATION = new Point(130, 50);
	private final Point ONEPLAYERDRAWLOCATION  = new Point(140, 275);
	private final Point TWOPLAYERDRAWLOCATION = new Point(370, 275);
	
	// Define player region coordinates for mouse action purposes
	private final Rectangle ONEPLAYERREGION = new Rectangle(148, 285, 177, 49);
	private final Rectangle TWOPLAYERREGION = new Rectangle(378, 285, 177, 49);
	
	// Define screen region
	private final Rectangle SCREENREGION = new Rectangle(0, 0, 696, 467);
	
	public MenuPanel(MainForm mainForm){
		_mainForm = mainForm;
		loadImages();
		addMouseMotionListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(SCREENREGION.width, SCREENREGION.height));
	}
	
	private void loadImages(){
		try {
			_backgroundImage = ImageIO.read(getClass().getResource("./graphics/MenuBackground.png"));
			_titleImage = ImageIO.read(getClass().getResource("./graphics/Title.png"));
			_playerOneImage = ImageIO.read(getClass().getResource("./graphics/OnePlayer.png"));
			_playerOneHighlightedImage = ImageIO.read(getClass().getResource("./graphics/OnePlayer_Highlighted.png"));
			_playerTwoImage = ImageIO.read(getClass().getResource("./graphics/TwoPlayer.png"));
			_playerTwoHighlightedImage = ImageIO.read(getClass().getResource("./graphics/TwoPlayer_Highlighted.png"));
		}
		catch (IOException e){
			onLoadImageFailure(e);
		}
		catch (IllegalArgumentException e){
			onLoadImageFailure(e);
		}
		
		// Set current player region images to NOT be highlighted
		_currentPlayerOneImage = _playerOneImage;
		_currentPlayerTwoImage = _playerTwoImage;
	}
	
	private void onLoadImageFailure(Exception e){
		JOptionPane.showMessageDialog(null, "Could not load one or more of required images. Check logfile.txt for more info.", "Image Load Error", JOptionPane.ERROR_MESSAGE);
		LogController.getInstance().error(e);
	}
	
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    g.drawImage(_backgroundImage, SCREENREGION.x, SCREENREGION.y, this);
	    g.drawImage(_titleImage, TITLEDRAWLOCATION.x, TITLEDRAWLOCATION.y, this);
	    g.drawImage(_currentPlayerOneImage, ONEPLAYERDRAWLOCATION.x, ONEPLAYERDRAWLOCATION.y, this);
	    g.drawImage(_currentPlayerTwoImage, TWOPLAYERDRAWLOCATION.x, TWOPLAYERDRAWLOCATION.y, this);
	}
	
	public void mousePressed(MouseEvent e) {
	       
    }

    public void mouseReleased(MouseEvent e) {
       
    }

    public void mouseEntered(MouseEvent e) {
    	
    }

    public void mouseExited(MouseEvent e) {
       
    }

    public void mouseClicked(MouseEvent e) {
    	// 'One Player' selected
		if (isMouseInOnePlayerRegion(e)){
			_mainForm.displayGameBoard(GameMode.ONEPLAYER);
		}
		// 'Two Player' selected
		else if (isMouseInTwoPlayerRegion(e)){
			_mainForm.displayGameBoard(GameMode.TWOPLAYER);
		}
    }
	    
	public void mouseMoved(MouseEvent e) {
		// Mouse is in 'One Player' region
		if (isMouseInOnePlayerRegion(e)){
			if (_currentPlayerOneImage != _playerOneHighlightedImage){
				_currentPlayerOneImage = _playerOneHighlightedImage;
				
				// Make sure only one menu option is highlighted. It's possible to drag your mouse from one
				// player region directly to the other. In this case, need to make sure the last region is NOT highlighted
				_currentPlayerTwoImage = _playerTwoImage; 
				
				this.repaint();
			}
		}
		// Mouse is in 'Two Player' region
		else if (isMouseInTwoPlayerRegion(e)){
			if (_currentPlayerTwoImage != _playerTwoHighlightedImage){
				_currentPlayerTwoImage = _playerTwoHighlightedImage;
				
				// Make sure only one menu option is highlighted. It's possible to drag your mouse from one
				// player region directly to the other. In this case, need to make sure the last region is NOT highlighted
				_currentPlayerOneImage = _playerOneImage; 
				
				this.repaint();
			}
		}
		// Mouse is anywhere else
		else {
			if (_currentPlayerOneImage != _playerOneImage){
				_currentPlayerOneImage = _playerOneImage;
				this.repaint();
			}
			if (_currentPlayerTwoImage != _playerTwoImage){
				_currentPlayerTwoImage = _playerTwoImage;
				this.repaint();
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		
	}
	
	private boolean isMouseInOnePlayerRegion(MouseEvent e){
		return ONEPLAYERREGION.contains(e.getX(), e.getY());
	}
	
	private boolean isMouseInTwoPlayerRegion(MouseEvent e){
		return TWOPLAYERREGION.contains(e.getX(), e.getY());
	}
	
}
