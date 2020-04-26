public class WordDictionary {
	int size;
	static String [] theDict= {"Macaron","veste","pomme","France","Gouvernement","souris","chat",
		"fraise","peche","abricot","raisin","gilet","oiseau","kiwi","seize","plume","prune",
		"panneau","Ordinateur","onze","fruit","vingt","quarante","tomate","dix",
		"soixante","foret","avocat"};
	
	WordDictionary(String [] tmp) {
		size = tmp.length;
		theDict = new String[size];
		for (int i=0;i<size;i++) {
			theDict[i] = tmp[i];
		}
		
	}
	
	WordDictionary() {
		size=theDict.length;
		
	}
	
	public synchronized String getNewWord() {
		int wdPos= (int)(Math.random() * size);
		return theDict[wdPos];
	}
	
}
