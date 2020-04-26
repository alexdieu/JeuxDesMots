public class GameThread implements Runnable{

    private WordRecord[] words;
    private WordPanel w;
    private int noWords;
    private Score score;
    private int totalWords;

    GameThread(WordRecord[] words,WordPanel w,Score score, int totalWords){
        this.words=words;
        this.w = w;
        noWords = words.length;
        this.score=score;
        this.totalWords = totalWords;
    }

    public void run() {
        while (true){
            while (!WordApp.done) {   

                for (int i=0;i<noWords;i++){	    	
                    words[i].drop(words[i].getSpeed());
                }                
                
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println("Erreur trouvée ");
                }				
            }
        }
    }

}