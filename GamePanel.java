package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;


import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {
		
		
	static final int LARGURA_ARENA = 800;
	static final int ALTURA_ARENA = 600;
	static final int UNIDADE_GERAL = 25;
	static final int UNIDADE_JOGO = (LARGURA_ARENA*ALTURA_ARENA) / UNIDADE_GERAL;
	static final int DELAY = 75;
	
	
	static final int LARGURA_RAQUETE = 20;
	static final int ALTURA_RAQUETE = 120;
	
	int xRaquete = 20;
	int yRaquete = (ALTURA_ARENA - ALTURA_RAQUETE)/2;

	
	int xRaqueteInimiga = (LARGURA_ARENA - (xRaquete*2));
	int yRaqueteInimiga = (ALTURA_ARENA - ALTURA_RAQUETE)/2;

	
	int pontos,pontosInimigos = 0;
	
	int xBola =  LARGURA_ARENA / 2;
	int yBola = ALTURA_ARENA / 2;
	
	int movimentacaoYInimigo = 0;
	
	int velocidadeXBola =  15;
	int velocidadeYBola =  15;
	
	Timer timer;
	Random random;
	
	boolean jogoRodando = false;
	boolean moveUp = false;
	boolean moveDown = false;
	boolean pontoMarcado = false;
	
	
	
    JButton startButton;
    JPanel startPanel;

    public GamePanel() {
        this.setPreferredSize(new Dimension(LARGURA_ARENA, ALTURA_ARENA));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startButton = new JButton("Play");
        startButton.setFont(new Font("Ink Free", Font.BOLD, 40));
        startButton.setForeground(Color.red);
        startButton.setPreferredSize(new Dimension(LARGURA_ARENA, ALTURA_ARENA));
        startButton.setBorderPainted(false);
        startButton.setContentAreaFilled(false);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runGame();
            }
        });

        startPanel = new JPanel();
        startPanel.setLayout(new BorderLayout());
        startPanel.setPreferredSize(new Dimension(LARGURA_ARENA, ALTURA_ARENA));
        startPanel.setBackground(Color.black);

        startPanel.add(startButton, BorderLayout.CENTER);

        this.add(startPanel);
    }


    private void runGame() {
        remove(startPanel);
        revalidate();
        repaint();
        start();
    }
	
	public boolean start() {
		jogoRodando = true;
		timer = new Timer(DELAY,this);
		timer.start();
		return jogoRodando;
	}
	
	protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    desenhar(g);
	}
	
	public void desenhar(Graphics g) {
		
		//minha raquete
		
		g.setColor(Color.white);
		g.fillRect(xRaquete, yRaquete, LARGURA_RAQUETE, ALTURA_RAQUETE);
		
		//bolinha
		g.setColor(Color.red);
		g.fillOval(xBola, yBola, UNIDADE_GERAL, UNIDADE_GERAL);
		
		
		//raquete inimiga
		g.setColor(Color.white);
		g.fillRect(xRaqueteInimiga, yRaqueteInimiga, LARGURA_RAQUETE, ALTURA_RAQUETE);
		
		//Desenhar placar
		g.setColor(Color.green);
		g.setFont(new Font("Ink Free",Font.BOLD,40));
		FontMetrics fm = getFontMetrics(g.getFont());
		
		g.drawString("" + pontos, 20,40);
		g.drawString("" + pontosInimigos, (LARGURA_ARENA - (20*2)),40);
		
		
		
	}
	
	public void moverRaquete () {
		
		 if(moveUp && yRaquete > 0) {
			 yRaquete -= UNIDADE_GERAL;
		 }
		 
		 if(moveDown && yRaquete < ALTURA_ARENA - ALTURA_RAQUETE) {
			 yRaquete += UNIDADE_GERAL;
		 }
	}
	
	public void moverBola () {
		
		if(jogoRodando) {
			xBola += velocidadeXBola;
			yBola += velocidadeYBola;
			
			if(xBola > LARGURA_ARENA || xBola  < 0 ) {
				velocidadeXBola *= -1;
			}
			
			if(yBola > ALTURA_ARENA || yBola < 0) {
				velocidadeYBola *=-1;
			}
			
			verificarColisao ();
			marcarPonto();
		
		}
	}
	
	public void verificarColisao() {
	    if (xBola + UNIDADE_GERAL >= xRaquete && xBola <= xRaquete + LARGURA_RAQUETE) {
	        if (yBola + UNIDADE_GERAL >= yRaquete && yBola <= yRaquete + ALTURA_RAQUETE) {

	            velocidadeXBola *= -1; 
	        }
	    }
	    
	    if (xBola + UNIDADE_GERAL >= xRaqueteInimiga && xBola <= xRaqueteInimiga + LARGURA_RAQUETE) {
	        if (yBola + UNIDADE_GERAL >= yRaqueteInimiga && yBola <= yRaqueteInimiga + ALTURA_RAQUETE) {
	            velocidadeXBola *= -1;
	        }
	    }
		
	}

	public void moverRaqueteInimiga () {
		
		movimentacaoYInimigo = yBola - yRaqueteInimiga - LARGURA_RAQUETE / 2 - 100;
		yRaqueteInimiga += movimentacaoYInimigo;

	}
	
	public void marcarPonto () {
		
		   if (xBola >= LARGURA_ARENA) {
			      pontos += 1;
		    } 
		   if (xBola <= 0) {
		         pontosInimigos += 1;
		    }
	
	}

		
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(jogoRodando) {
			moverRaquete();
			moverRaqueteInimiga();
			moverBola();
			
	
			
		
		
		}
		repaint();
		
	}
	
	
	public class MyKeyAdapter extends KeyAdapter {
		
		
		//apertar tecla
		@Override
		public void keyPressed (KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_W: 
				moveUp = true;
				break;
			case KeyEvent.VK_S: 
				moveDown = true;
				break;
				
			}
			
		}
		
		//soltar tecla
		@Override
		public void keyReleased (KeyEvent e) {
			switch(e.getKeyCode()) {
			
			case KeyEvent.VK_W: 
				moveUp = false;
				break;
			case KeyEvent.VK_S: 
				moveDown = false;
				break;
				
			}
			
		}
	}

}
