import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
		public static volatile boolean done;
		private WordRecord[] words;
		private int noWords;
		private int maxY;
		private Score score;

		
		public void paintComponent(Graphics g) {
			Toolkit.getDefaultToolkit().sync();
		    int width = getWidth();
		    int height = getHeight();
		    g.clearRect(0,0,width,height);
		    g.setColor(Color.red);
		    g.fillRect(0,maxY-10,width,height);

		    g.setColor(Color.black);
		    g.setFont(new Font("Helvetica", Font.PLAIN, 26));
			if (WordApp.done && !score.getGameStatus()){
				g.drawString("",0,0);
			}

		   else if (!score.getGameStatus()&&!WordApp.done) {
			for (int i=0;i<noWords;i++){
				g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);

			}			   
		   }
		   else if (score.getGameStatus()){
			if(score.getMissed()==0){
				g.drawString("GAGNANT!",450,250);
				g.drawString("Total : "+score.getScore(),450,270);

			}
			else{
				g.drawString("Perdu ",450,250);
				g.drawString("Ton score : "+score.getScore(),450,270);
			}
			
		   }		
		   
		  }
		
		WordPanel(WordRecord[] words, int maxY, Score score) {
			this.words=words;
			noWords = words.length;
			done=false;
			this.maxY=maxY;	
			this.score = score;	
		}
		
		public void run() {
			while (true){
				while (!done) {
					repaint();
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						System.out.println("Erreur trouvée !");
					}				
				}
			}
			
		}

	}


