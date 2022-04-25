package showSolution;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class GameArea extends JPanel {
	
	private Image trap;
	private Image key;
	private Image stairsDown;
	private Image stairsUp;
	private Image stairsRight;
	private Image stairsLeft;
	private Image scorpion;
	private Image hero;
	private Image beackground;
	private Image mummyWhite;
	private Image mummyRed;
	private Image wallHorizontal;
	private Image wallVertical;
	private Image doorHorizontalOpen;
	private Image doorHorizontalClosed;
	private Image doorVerticalOpen;
	private Image doorVerticalClosed;
	
	private int xStart = 63;
	private int yStart = 79;
	
	private String state = null;
	private boolean showSolutionCost;
	private double solutionCost;
	
	public GameArea(){
		super();
		setPreferredSize(new Dimension(486,474));
		loadImages();
		showSolutionCost = true;
	}
	
	private void loadImages(){
		trap = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/armadilha.png"));
		key = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/chave.png"));
		stairsDown = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/escadaBaixo.png"));
		stairsUp = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/escadaCima.png"));
		stairsRight = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/escadaDireita.png"));
		stairsLeft = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/escadaEsquerda.png"));
		scorpion = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/escorpiao.png"));
		hero = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/explorador.png"));
		beackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/fundo.png"));
		mummyWhite = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/mumiaBranca.png"));
		mummyRed = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/mumiaVermelha.png"));
		wallHorizontal = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/paredeHorizontal.png"));
		wallVertical = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/paredeVertical.png"));
		doorHorizontalOpen = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/portaHorizontalAberta.png"));
		doorHorizontalClosed = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/portaHorizontalFechada.png"));
		doorVerticalOpen = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/portaVerticalAberta.png"));
		doorVerticalClosed = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/sprites/portaVerticalFechada.png"));
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawImage(beackground,0,0,this);
		
		if(state == null){
			return;
		}
		String[] splitString = (state.split("\\n"));
		
		for(int i = 0; i < 13; i++) {
			for(int j = 0; j < 13; j++) {				
				switch(splitString[i].charAt(j)) {
					case '-' : g.drawImage(wallHorizontal,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					case '=' : g.drawImage(doorHorizontalClosed,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					case '_' : g.drawImage(doorHorizontalOpen,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					case '|' : g.drawImage(wallVertical,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					case '"' : g.drawImage(doorVerticalClosed,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					case ')' : g.drawImage(doorVerticalOpen,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					case 'M' : g.drawImage(mummyWhite,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					case 'H' : g.drawImage(hero,j == 0 ? xStart + (j-2)/2 * 60 : xStart + j/2 * 60, i ==0 ? yStart + (i-2)/2 * 60 -6 : yStart + i/2 * 60,this); break;
					case 'V' : g.drawImage(mummyRed,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					case 'A' : g.drawImage(trap,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					case 'E' : g.drawImage(scorpion,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					case 'C' : g.drawImage(key,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					case 'S' : g.drawImage(i == 0 ? stairsUp : i == 12 ? stairsDown : j == 0 ? stairsLeft : stairsRight,j == 0 ? xStart + (j-2)/2 * 60 : xStart + j/2 * 60, i ==0 ? yStart + (i-2)/2 * 60 -6 : yStart + i/2 * 60,this); break;
				}
			}
		}

		if(showSolutionCost) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("Solution cost: " + solutionCost, 10, 20);
		}
		
	}
	
	public void setState(String state){
		this.state = state;
		repaint();		
	}

	public void setShowSolutionCost(boolean showSolutionCost) {
		this.showSolutionCost = showSolutionCost;
	}

	public void setSolutionCost(double solutionCost){
		this.solutionCost = solutionCost;
	}

}
