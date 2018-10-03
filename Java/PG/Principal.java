package PG;

import java.util.Scanner;

public class Principal {

	private static final Scanner sc = new Scanner(System.in);
	private static final Evolutor e = new Evolutor();
	
	public static double read (String head) {
		
		if(head==null)
			head = "";
		
		String s = null;
		
		while(true) {
			
			System.out.print(head+">>");
			s = sc.nextLine();
			
			if(!e.op.getLAR().hasError(s))
				break;
			
			System.out.println("ERROR: try again");
		}
		
		return e.op.getLAR().exec(s);
	}
	public static double read () {
		return read("");
	}
	
	public static void evolution () {
		System.out.println("Set the target function");
		System.out.print(">>");
		e.target(sc.nextLine());
		
		do {
			System.out.println(e.next());
		} while(e.dif()>0);
		
		System.out.println("\n");
		System.out.println("Exact result found: \n" + e.bestMatch());
		System.out.println("Target: \n" + e.target());
	}
	
	public static void coevolution () {
		Evolutor x = new Evolutor();
		Evolutor y = new Evolutor();
		
		x.popular(e.population().size());
		y.popular(e.population().size());
		
		x.start(e.start());
		y.start(e.start());
		
		x.end(e.end());
		y.end(e.end());
		
		x.jump(e.jump());
		y.jump(e.jump());
		
		x.target(e.op.gerar("x"));
		y.target(e.op.gerar("x"));
		
		do {
			
			x.target(y.next());
			y.target(x.next());
			
			System.out.println(x);
			System.out.println(y);
			
		} while(x.dif()>0 && y.dif()>0);
		
		System.out.println("\n");
		System.out.println("Exact result found:");
		System.out.println(x.bestMatch());
		System.out.println(y.bestMatch());
	}
	
	public static void main(String[] args) {
		
		while(true) {
			
			System.out.println("\n");
			System.out.println("Can we start?");
			System.out.print(">>");
			if(sc.nextLine().toLowerCase().indexOf("n")==0)
				break;
			
			e.popular((int)read("Population Size"));
			e.start(read("Start Value"));
			e.end(read("End Value"));
			e.jump(read("Increment Value"));
			
			System.out.println("Coevolution ?");
			if(sc.nextLine().toLowerCase().indexOf("n")==0)
				evolution();
			else
				coevolution();
			
		}

	}

}
