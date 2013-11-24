package LYN;

public class Menu {
    String choice;
    private String name;
	
   
   String Steak(){
	   return  "Steak";
   }
   
   String Chicken() {
	   return  "Chicken";
   }
   
   String Salad() {
	   return  "Salad";
   }
   
   String Pizza() {
	   return "Pizza";
   }
   
   public Menu(String name) {
		super();
        this.name = name;
		
	}
	
	
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
}
