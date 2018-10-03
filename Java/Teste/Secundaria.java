package Teste;

import java.util.Scanner;

import PG.Evolutor;
//import jcurses.classes.jcurses.system.CharColor;

public class Secundaria {

	private static final Scanner sc = new Scanner(System.in);
	private static final Evolutor e = new Evolutor();
//	private static CharColor cc = new CharColor(CharColor.WHITE, CharColor.BLACK);
	
//	public static double read (String head) {
//		
//		if(head==null)
//			head = "";
//		
//		String s = null;
//		
//		while(true) {
//			
//			System.out.print(head+">>");
//			s = sc.nextLine();
//			
//			if(e.op.getLAR().exec(s)!=e.NAN)
//				break;
//			
//			System.out.println("ERROR: try again");
//		}
//		
//		return e.op.getLAR().exec(s);
//	}
//	public static double read () {
//		return read("");
//	}
	
	public static void setup () {
//		e.popular((int)read("Population Size"));
//		e.start(read("Start Value"));
//		e.end(read("End Value"));
//		e.jump(read("Increment Value"));
		System.out.println("Set the target function");
		
		String s = "";
		while(true) {
			System.out.print(">>");
			s = sc.nextLine();
			
			if(s.length()!=0) {
				e.op.getLAR().exec("x = 0");
				e.op.getLAR().exec(s);
				if(!e.op.getLAR().hasError())
					break;
			}
		}
		e.target(s);
	}
	
	public static void evolution () {
		
		String s = e.op.gerar("x");
		String p = e.target();
		e.target(e.op.getLAR().op.normalizeStr(p));
		int c = 0;
		
		do {
			
			System.out.println("; \n");
			
			if(e.op.rand()%4==0)
				s = e.op.cruzar(s, e.target());
			
			if(e.op.rand()%4>0)
				s = e.op.mutar(s, "x");
			
			System.out.print((c++) + "; \n" + s);
			
		} while(0!=avaliar(s, e.target()));
		
//		do {
//			System.out.println(e.next());
//			if(e.op.rand()%11==0)
//				e.population().set(e.op.rand()%e.population().size(), e.op.cruzar(e.target(), e.population().get(e.op.rand()%e.population().size())));
//		} while(e.dif()>0);
		
		System.out.println("\n");
		System.out.println("Result found: \n" + s);
		System.out.println("Target: \n" + p);
	}
	
	public static double avaliar (String teste, String controle) {
		
		double n = 0;
		
		if(((teste.indexOf("^")==-1)!=(controle.indexOf("^")==-1)) || ((teste.indexOf("~")==-1)!=(controle.indexOf("~")==-1)) || ((teste.indexOf("!")==-1)!=(controle.indexOf("!")==-1)) || ((teste.indexOf(".")==-1)!=(controle.indexOf(".")==-1)) || ((teste.indexOf("$")==-1)!=(controle.indexOf("$")==-1)))
			return 101010; 
		
		double a = e.op.getLAR().exec(teste);
		double b = e.op.getLAR().exec(controle);
		
		for(int c=-10; c<10; c++) {
			e.op.getLAR().exec("x = " + c);
			
			if(teste.indexOf("x")!=-1)
				a = e.op.getLAR().exec(teste);
			
			if(controle.indexOf("x")!=-1)
				b = e.op.getLAR().exec(controle);
			
			n += Math.abs(a-b);
		}
			
		return n;
	}
	
	public static void main(String[] args) {
		
		while(true) {
			
			System.out.println("\n");
			
			setup();
			evolution();
			
			System.out.println("\n");
			System.out.println("Can we continue?");
			System.out.print(">>");
			if(sc.nextLine().toLowerCase().indexOf("n")==0)
				break;
		}

	}

}
