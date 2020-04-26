import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class WordApp {
	static int noWords =4;
	static int totalWords =0;

   	static int frameX =1000;
	static int frameY =600;
	static int yLimit =480;

	static WordDictionary dict = new WordDictionary();

	static WordRecord[] words;
	static volatile boolean done;
	static 	Score score = new Score();

	static WordPanel w;	

	static JLabel missed;
	static JLabel scr;
	
	public static void setupGUI(int frameX,int frameY,int yLimit) {
    	JFrame frame = new JFrame("JeuxDesMots");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	
		w = new WordPanel(words,yLimit,score);
		w.setSize(frameX,yLimit+100);
	    g.add(w);
	    
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    JLabel caught =new JLabel("Attrapés : " + score.getCaught() + "    ");
	    missed =new JLabel("Manqués :" + score.getMissed()+ "    ");
	    scr =new JLabel("Total :" + score.getScore()+ "    ");
	    txt.add(caught);
	    txt.add(missed);
	    txt.add(scr);

  
	    final JTextField textEntry = new JTextField("",20);
	   textEntry.addActionListener(new ActionListener()
	    {                  
	      public void actionPerformed(ActionEvent evt) {
	          String text = textEntry.getText();
	          textEntry.setText("");
			  textEntry.requestFocus();
			  System.out.println(text); //Testing

			  for (int i=0;i<noWords;i++){	    	
				if (text.equals(words[i].getWord())){
					score.caughtWord(words[i].getWord().length());
					caught.setText("Réussis : " + score.getCaught() + "    ");
					scr.setText("Score total :" + score.getScore()+ "    ");
					if ((totalWords - score.getTotal()-noWords) > -1) words[i].resetWord(); 
					else {
						words[i].resetWord();
						words[i].setWord("");
						words[i].setSpeed(0);
					}
				}
			}
	      }
	    });
	   
	   txt.add(textEntry);
	   txt.setMaximumSize( txt.getPreferredSize() );
	   g.add(txt);
	    
	    JPanel b = new JPanel();
        b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS)); 
	   	JButton startB = new JButton("Commencer ");;

			startB.addActionListener(new ActionListener()
		    {
		      public void actionPerformed(ActionEvent e)
		      {


				if(done==true){
					caught.setText("Attrapés : " + score.getCaught() + "    ");
					scr.setText("Score:" + score.getScore()+ "    "); 
					for (int i=0;i<noWords;i++){	    	
						words[i].resetWord();
					}
					score.resetScore();
					missed.setText("Manqués :" + score.getMissed()+ "    ");
					caught.setText("Attrapés : " + score.getCaught() + "    ");
					scr.setText("Score TOTAL :" + score.getScore()+ "    ");
					done=false;
				}

				textEntry.requestFocus();
		      }
		    });
		JButton endB = new JButton("FINI ");;

				endB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
					  //[snip]
					score.resetScore();
					missed.setText("Manqués :" + score.getMissed()+ "    ");
					caught.setText("Attrapés  : " + score.getCaught() + "    ");
					scr.setText("Score total :" + score.getScore()+ "    ");
					done=true;	

			      }
				});

		JButton quitB = new JButton("Sortir");;

				quitB.addActionListener(new ActionListener()
			    {
			      public void actionPerformed(ActionEvent e)
			      {
					System.exit(0);
			      }
				});
				
				
		
		b.add(startB);
		b.add(endB);
		b.add(quitB);
		
		g.add(b);

      	frame.setLocationRelativeTo(null);
      	frame.add(g);
        frame.setContentPane(g);
        frame.setVisible(true);

		
	}

	
public static String[] getDictFromFile(String filename) {
	    ArrayList<String> dictStr = new ArrayList<String>();
	    FileInputStream f = null;
	    String [] l= null;

		try {
			f = new FileInputStream(filename);
			Scanner dictReader = new Scanner(f);

            int i=0;
			while(dictReader.hasNext()) {
				String nextToken = dictReader.next();
				dictStr.add(nextToken);
				String nextLine = dictReader.nextLine();
				i++;
			}

			dictReader.close();
			l = dictStr.toArray(new String[dictStr.size()]);


		} catch (IOException e) {
	        System.err.println("PB en lisant le fichier " + filename + " Le dictionnaire par défault (FR) sera utilisé ");
	    }

		return l;

	}

	public static void main(String[] args) throws Exception{
		try {
			totalWords = Integer.parseInt(args[0]);
			noWords = Integer.parseInt(args[1]);
			assert (totalWords >= noWords);
			if (args[2] == null ) dict = new WordDictionary();
			else {
				dict = new WordDictionary(getDictFromFile(args[2]));
			}


			words = new WordRecord[noWords];
			WordRecord.dict = dict;

			done = true;
			setupGUI(frameX, frameY, yLimit);
			int x_inc = (int) frameX / noWords;

			for (int i = 0; i < noWords; i++) {
				words[i] = new WordRecord(dict.getNewWord(), i * x_inc, yLimit);
				System.out.println(i + " " + words[i].getWord() + " " + words[i].getSpeed());
			}

			Thread wordPanelThread = new Thread(w);
			wordPanelThread.start();

			DroppedThread dThread = new DroppedThread(words, w, score, missed, totalWords);
			Thread droppedThread = new Thread(dThread);
			droppedThread.start();

			Thread gameThread = new Thread(new GameThread(words, w, score, totalWords));
			gameThread.start();
		}

		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Erreur : Merci de mettre les arguments "+args);
		}

		catch (Exception e) {

			System.out.println("Erreur : "+e.toString());

		}


	}

}
