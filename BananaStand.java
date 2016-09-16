/**
* Holla at yo boi
*
* @version 12039
* @author Eh Steve
*/
public class BananaStand{
//JSHIUHSFIOUHS	
	public static final String EMPLOYEE_UN = "sjmoon";
	public static final double BANANA_COST = 2.5;
	public static final String SLOGAN = "There's always money in the Banana Stand";

	/*
	* Howdy Partner
	* @param args THE arguments passed in during execution
	*/
	public static void main(String[] args){
		greetCustomer ();
		checkoutCustomer();
	}
	
	//DSFIJODSF
	public static void greetCustomer(){
		String customerName = "Tony";
		System.out.println("Hello, "+customerName+", my name is "+EMPLOYEE_UN);
		System.out.println("Each banana costs $"+BANANA_COST);
	}

	public static void checkoutCustomer(){
		System.out.print("Thank you for shopping, your total is ");
		calculateBananaCostLoop();
	}

	public static void calculateBananaCost(){
		int numBananas = 10;
		System.out.println(numBananas*BANANA_COST);
	}

	public static void calculateBananaCostLoop(){
		int numBananas = 10;
		double sum = 0;
		for(int i = 0; i < numBananas; ++i){
			sum+=BANANA_COST;
		}
		System.out.println(sum);
	}
}
