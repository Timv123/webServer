import java.util.HashMap;
import java.util.Map.Entry;


public class testHash {
	
public static void main(String args[]){
		
		HashMap <Character,Integer> userInput = new HashMap <Character,Integer>();
		String strCharacter = "I want to quit this class but I need to pass";
		strCharacter = strCharacter.replaceAll("\\s+","");
		
		for(int i=0; i <strCharacter.length(); i++){
			
			

			
			if(userInput.get(strCharacter.charAt(i)) != null){
				int total = userInput.get(strCharacter.charAt(i));
				userInput.put(strCharacter.charAt(i), total +1);		
			}
			else
				userInput.put(strCharacter.charAt(i),1 );
							
		}
		
		for(Entry<Character, Integer> s : userInput.entrySet()){
			
			System.out.println(s);
			
		}

}
}
