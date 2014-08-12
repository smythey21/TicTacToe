package com.kevinsmyth.tictactoe.views;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import com.kevinsmyth.tictactoe.controllers.GameController;
import com.kevinsmyth.tictactoe.controllers.LogController;
import com.kevinsmyth.tictactoe.models.CellType;
import com.kevinsmyth.tictactoe.models.GameBoard;
import com.kevinsmyth.tictactoe.models.GameMode;
import com.kevinsmyth.tictactoe.models.PlayerPiece;

/*
 * Class to display the game
 */
@SuppressWarnings("serial")
public class GameBoardPanel extends JPanel implements MouseListener, MouseMotionListener{
	// Parent form. We will use this to display the menu panel 
	private MainForm _mainForm;
	
	// Controller that handles all the game logic
	private GameController _controller;
	
	// Images needed for display
	private BufferedImage _boardImage;
	private BufferedImage _boardPieceXImage;
	private BufferedImage _boardPieceOImage;
	private BufferedImage _transparentBoardPieceXImage;
	private BufferedImage _transparentBoardPieceOImage;
	private BufferedImage _highlightedBoardPieceXImage;
	private BufferedImage _highlightedBoardPieceOImage;
	private BufferedImage _menuInactiveImage;
	private BufferedImage _menuActiveImage;
	private BufferedImage _player1Image;
	private BufferedImage _player2Image;
	private BufferedImage _computerImage;
	private BufferedImage _winsImage;
	private BufferedImage _drawImage;
	
	// Define image locations for drawing purposes
	private final Point TOPLEFTCELLDRAWLOCATION = new Point(116, 24);
	private final Point TOPMIDDLECELLDRAWLOCATION = new Point(273, 24);
	private final Point TOPRIGHTCELLDRAWLOCATION = new Point(432, 24);
	private final Point MIDDLELEFTCELLDRAWLOCATION = new Point(116, 166);
	private final Point MIDDLECELLDRAWLOCATION = new Point(273, 166);
	private final Point MIDDLERIGHTCELLDRAWLOCATION = new Point(432, 166);
	private final Point BOTTOMLEFTCELLDRAWLOCATION = new Point(116, 317);
	private final Point BOTTOMMIDDLECELLDRAWLOCATION = new Point(273, 317);
	private final Point BOTTOMRIGHTCELLDRAWLOCATION = new Point(432, 317);

	// Define board region coordinates
	private final Rectangle TOPLEFTCELLREGION = new Rectangle(116, 24, 148, 132);
	private final Rectangle TOPMIDDLECELLREGION = new Rectangle(273, 24, 150, 132);
	private final Rectangle TOPRIGHTCELLREGION = new Rectangle(432, 24, 152, 132);
	private final Rectangle MIDDLELEFTCELLREGION = new Rectangle(116, 166, 148, 142);
	private final Rectangle MIDDLECELLREGION = new Rectangle(273, 166, 150, 142);
	private final Rectangle MIDDLERIGHTCELLREGION = new Rectangle(432, 166, 152, 142);
	private final Rectangle BOTTOMLEFTCELLREGION = new Rectangle(116, 317, 148, 124);
	private final Rectangle BOTTOMMIDDLECELLREGION = new Rectangle(273, 317, 150, 124);
	private final Rectangle BOTTOMRIGHTCELLREGION = new Rectangle(432, 317, 152, 124);
	private final Rectangle MENUREGION = new Rectangle(608, 415, 73, 38);
	
	// Global variables used for display logic
	private boolean _isMenuActive;
	private CellType _currentCell;
	
	public GameBoardPanel(GameMode gameMode, MainForm mainForm){
		_mainForm = mainForm;
		_isMenuActive = false;
		_currentCell = CellType.NOCELL;	
		_controller = new GameController(gameMode);
		
		loadImages();
		createTransparentBoardPieceImages();
		addMouseMotionListener(this);
		addMouseListener(this);
		this.setPreferredSize(new Dimension(696, 467));
	}
	
	private void loadImages(){
		try {
			_boardImage = ImageIO.read(getClass().getResource("./graphics/TicTacToeBoard.png"));
			_boardPieceXImage = ImageIO.read(getClass().getResource("./graphics/X.png"));
			_boardPieceOImage = ImageIO.read(getClass().getResource("./graphics/O.png"));
			_highlightedBoardPieceXImage = ImageIO.read(getClass().getResource("./graphics/X_Highlighted.png"));
			_highlightedBoardPieceOImage = ImageIO.read(getClass().getResource("./graphics/O_Highlighted.png"));
			_menuInactiveImage = ImageIO.read(getClass().getResource("./graphics/Menu_Inactive.png"));
			_menuActiveImage = ImageIO.read(getClass().getResource("./graphics/Menu_Active.png"));
			_player1Image = ImageIO.read(getClass().getResource("./graphics/Player1.png"));
			_player2Image = ImageIO.read(getClass().getResource("./graphics/Player2.png"));
			_computerImage = ImageIO.read(getClass().getResource("./graphics/Computer.png"));
			_winsImage = ImageIO.read(getClass().getResource("./graphics/Wins.png"));
			_drawImage = ImageIO.read(getClass().getResource("./graphics/Draw.png"));
		}
		catch (IOException e){
			onLoadImageFailure(e);
		}
		catch (IllegalArgumentException e){
			onLoadImageFailure(e);
		}
	}
	
	private void onLoadImageFailure(Exception e){
		JOptionPane.showMessageDialog(null, "Could not load one or more of required images. Check logfile.txt for more info.", "Image Load Error", JOptionPane.ERROR_MESSAGE);
		LogController.getInstance().error(e);
	}
	
	private void createTransparentBoardPieceImages(){
		_transparentBoardPieceXImage = new BufferedImage(_boardPieceXImage.getWidth(), 
														 _boardPieceXImage.getHeight(), 
														 BufferedImage.TYPE_INT_ARGB);
		Graphics2D transparentXGraphics = (Graphics2D) _transparentBoardPieceXImage.getGraphics();
		
		// Set to 50% transparent
		transparentXGraphics.setComposite(AlphaComposite.SrcOver.derive(0.5f)); 
		transparentXGraphics.drawImage(_boardPieceXImage, 0, 0, null);
		
		_transparentBoardPieceOImage = new BufferedImage(_boardPieceOImage.getWidth(), 
														 _boardPieceOImage.getHeight(), 
														 BufferedImage.TYPE_INT_ARGB);
		Graphics2D transparentOGraphics = (Graphics2D) _transparentBoardPieceOImage.getGraphics();
		
		// Set to 50% transparent
		transparentOGraphics.setComposite(AlphaComposite.SrcOver.derive(0.5f)); 
		transparentOGraphics.drawImage(_boardPieceOImage, 0, 0, null);
	}
	
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    drawGameBoard(g);
	    drawBoardPieces(g);
	    drawMenu(g);
	    drawPlayerStatus(g);
	}
	
	private void drawGameBoard(Graphics g){
		g.drawImage(_boardImage, 0, 0, this);
	}
	
	private void drawMenu(Graphics g){
		if (_isMenuActive){
			g.drawImage(_menuActiveImage, 600, 411, this);
		} 
		else {
			g.drawImage(_menuInactiveImage, 600, 411, this);
		}
	}
	
	private void drawBoardPieces(Graphics g){
		GameBoard board = _controller.getBoard();
		
		if (!board.isGameOver() && board.isBoardCellEmpty(_currentCell)){
	    	drawTransparentGamePiece(g, _currentCell, _controller.getCurrentPlayer().getPiece());
	    }
	    for (CellType cell : CellType.values()){
	    	if (board.isWinningCell(cell)){
	    		drawHighlightedGamePiece(g, cell, board.getBoardPiece(cell));
	    	}
	    	else {
	    		drawSolidGamePiece(g, cell, board.getBoardPiece(cell));
	    	}
	    }
	}
	
	/*
	 *  Draws player status on top left portion of screen
	 */
	private void drawPlayerStatus(Graphics g){
		GameBoard board = _controller.getBoard();
	    
	    if (board.isGameDraw()){
	    	drawDraw(g);
	    }
	    else {
	    	drawPlayer(g);
	    }
	    
	    if (board.isGameWon()){
	    	drawWins(g);
	    }
	}
	
	private void drawPlayer(Graphics g){
		if (_controller.getCurrentPlayer().isAI()){
			g.drawImage(_computerImage, 0, 0, this);
		}
		else if (_controller.isPlayer1Turn()){
			g.drawImage(_player1Image, 0, 0, this);
		}
		else if (_controller.isPlayer2Turn()){
			g.drawImage(_player2Image, 0, 0, this);
		}
	}
	
	private void drawWins(Graphics g){
		g.drawImage(_winsImage, 0, 50, this);
	}
	
	private void drawDraw(Graphics g){
		g.drawImage(_drawImage, 0, 0, this);
	}
	
	private void drawTransparentGamePiece(Graphics g, CellType cell, PlayerPiece boardPiece){
		if (boardPiece == PlayerPiece.EMPTY){
			return;
		}
		
		BufferedImage boardPieceImage = boardPiece == PlayerPiece.X ? _transparentBoardPieceXImage : _transparentBoardPieceOImage;
		drawGamePiece(g, cell, boardPieceImage);
	}
	
	private void drawSolidGamePiece(Graphics g, CellType cell, PlayerPiece boardPiece){
		if (boardPiece == PlayerPiece.EMPTY){
			return;
		}
		
		BufferedImage boardPieceImage = boardPiece == PlayerPiece.X ? _boardPieceXImage : _boardPieceOImage;
		drawGamePiece(g, cell, boardPieceImage);
	}
	
	private void drawHighlightedGamePiece(Graphics g, CellType cell, PlayerPiece boardPiece){
		if (boardPiece == PlayerPiece.EMPTY){
			return;
		}
		
		BufferedImage boardPieceImage = boardPiece == PlayerPiece.X ? _highlightedBoardPieceXImage : _highlightedBoardPieceOImage;
		drawGamePiece(g, cell, boardPieceImage);
	}
	
	private void drawGamePiece(Graphics g, CellType cell, BufferedImage boardPieceImage){
		switch (cell){
	    	case TOPLEFT:
	    		g.drawImage(boardPieceImage, TOPLEFTCELLDRAWLOCATION.x, TOPLEFTCELLDRAWLOCATION.y, this);
	    		break;
	    	case TOPMIDDLE:
	    		g.drawImage(boardPieceImage, TOPMIDDLECELLDRAWLOCATION.x, TOPMIDDLECELLDRAWLOCATION.y, this);
	    		break;
	    	case TOPRIGHT:
	    		g.drawImage(boardPieceImage, TOPRIGHTCELLDRAWLOCATION.x, TOPRIGHTCELLDRAWLOCATION.y, this);
	    		break;
	    	case MIDDLELEFT:
	    		g.drawImage(boardPieceImage, MIDDLELEFTCELLDRAWLOCATION.x, MIDDLELEFTCELLDRAWLOCATION.y, this);
	    		break;
	    	case MIDDLE:
	    		g.drawImage(boardPieceImage, MIDDLECELLDRAWLOCATION.x, MIDDLECELLDRAWLOCATION.y, this);
	    		break;
	    	case MIDDLERIGHT:
	    		g.drawImage(boardPieceImage, MIDDLERIGHTCELLDRAWLOCATION.x, MIDDLERIGHTCELLDRAWLOCATION.y, this);
	    		break;
	    	case BOTTOMLEFT:
	    		g.drawImage(boardPieceImage, BOTTOMLEFTCELLDRAWLOCATION.x, BOTTOMLEFTCELLDRAWLOCATION.y, this);
	    		break;
	    	case BOTTOMMIDDLE:
	    		g.drawImage(boardPieceImage, BOTTOMMIDDLECELLDRAWLOCATION.x, BOTTOMMIDDLECELLDRAWLOCATION.y, this);
	    		break;
	    	case BOTTOMRIGHT:
	    		g.drawImage(boardPieceImage, BOTTOMRIGHTCELLDRAWLOCATION.x, BOTTOMRIGHTCELLDRAWLOCATION.y, this);
	    		break;
	    }
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
    	GameBoard board = _controller.getBoard();
    	
    	if (!board.isGameOver()){
	    	if (isMouseInBoardRegion(e, TOPLEFTCELLREGION) && board.isBoardCellEmpty(CellType.TOPLEFT)) {
	    		_controller.placeMove(CellType.TOPLEFT);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, TOPMIDDLECELLREGION) && board.isBoardCellEmpty(CellType.TOPMIDDLE)) {
				_controller.placeMove(CellType.TOPMIDDLE);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, TOPRIGHTCELLREGION) && board.isBoardCellEmpty(CellType.TOPRIGHT)) {
				_controller.placeMove(CellType.TOPRIGHT);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, MIDDLELEFTCELLREGION) && board.isBoardCellEmpty(CellType.MIDDLELEFT)) {
				_controller.placeMove(CellType.MIDDLELEFT);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, MIDDLECELLREGION) && board.isBoardCellEmpty(CellType.MIDDLE)) {
				_controller.placeMove(CellType.MIDDLE);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, MIDDLERIGHTCELLREGION) && board.isBoardCellEmpty(CellType.MIDDLERIGHT)) {
				_controller.placeMove(CellType.MIDDLERIGHT);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, BOTTOMLEFTCELLREGION) && board.isBoardCellEmpty(CellType.BOTTOMLEFT)) {
				_controller.placeMove(CellType.BOTTOMLEFT);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, BOTTOMMIDDLECELLREGION) && board.isBoardCellEmpty(CellType.BOTTOMMIDDLE)) {
				_controller.placeMove(CellType.BOTTOMMIDDLE);
				this.repaint();
			}
			else if (isMouseInBoardRegion(e, BOTTOMRIGHTCELLREGION) && board.isBoardCellEmpty(CellType.BOTTOMRIGHT)) {
				_controller.placeMove(CellType.BOTTOMRIGHT);
				this.repaint();
			}
    	}
    	
    	if (isMouseInBoardRegion(e, MENUREGION)){
    		_mainForm.displayMenu();
    	}
    }
	    
	public void mouseMoved(MouseEvent e) {
		if (isMouseInBoardRegion(e, TOPLEFTCELLREGION) && _currentCell != CellType.TOPLEFT) {
			_currentCell = CellType.TOPLEFT;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, TOPMIDDLECELLREGION) && _currentCell != CellType.TOPMIDDLE) {
			_currentCell = CellType.TOPMIDDLE;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, TOPRIGHTCELLREGION) && _currentCell != CellType.TOPRIGHT) {
			_currentCell = CellType.TOPRIGHT;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, MIDDLELEFTCELLREGION) && _currentCell != CellType.MIDDLELEFT) {
			_currentCell = CellType.MIDDLELEFT;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, MIDDLECELLREGION) && _currentCell != CellType.MIDDLE) {
			_currentCell = CellType.MIDDLE;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, MIDDLERIGHTCELLREGION) && _currentCell != CellType.MIDDLERIGHT) {
			_currentCell = CellType.MIDDLERIGHT;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, BOTTOMLEFTCELLREGION) && _currentCell != CellType.BOTTOMLEFT) {
			_currentCell = CellType.BOTTOMLEFT;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, BOTTOMMIDDLECELLREGION) && _currentCell != CellType.BOTTOMMIDDLE) {
			_currentCell = CellType.BOTTOMMIDDLE;
			this.repaint();
		}
		else if (isMouseInBoardRegion(e, BOTTOMRIGHTCELLREGION) && _currentCell != CellType.BOTTOMRIGHT) {
			_currentCell = CellType.BOTTOMRIGHT;
			this.repaint();
		} 
		else if (isMouseInNoCell(e) && _currentCell != CellType.NOCELL) {
			_currentCell = CellType.NOCELL;
			this.repaint();
		}
		
		if (isMouseInBoardRegion(e, MENUREGION) && !_isMenuActive){
			_isMenuActive = true;
			this.repaint();
		}
		else if (!isMouseInBoardRegion(e, MENUREGION) && _isMenuActive){
			_isMenuActive = false;
			this.repaint();
		}
	}

	public void mouseDragged(MouseEvent e) {
		
	}
	
	private boolean isMouseInBoardRegion(MouseEvent e, Rectangle cellRegion){
		return cellRegion.contains(e.getX(), e.getY());
	}
	
	private boolean isMouseInNoCell(MouseEvent e){
		return  !isMouseInBoardRegion(e, TOPLEFTCELLREGION) && !isMouseInBoardRegion(e, TOPMIDDLECELLREGION) && !isMouseInBoardRegion(e, TOPRIGHTCELLREGION) &&
				!isMouseInBoardRegion(e, MIDDLELEFTCELLREGION) && !isMouseInBoardRegion(e, MIDDLECELLREGION) && !isMouseInBoardRegion(e, MIDDLERIGHTCELLREGION) &&
				!isMouseInBoardRegion(e, BOTTOMLEFTCELLREGION) && !isMouseInBoardRegion(e, BOTTOMMIDDLECELLREGION) && !isMouseInBoardRegion(e, BOTTOMRIGHTCELLREGION);
	}
}