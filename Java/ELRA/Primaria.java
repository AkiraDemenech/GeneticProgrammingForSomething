package ELRA;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import PG.Seletor;

public class Primaria {
	
	private static Seletor select;
	
	public static int unCommand (String s) { 
		int n = 0;
		for(int c=0; c<s.length(); c++) {
			if(s.charAt(c)=='|')
				n++;
		}
		return n;
	}

	public static void main(String[] args) {
		
		Interpretador lar = new Interpretador();
		Scanner sc = new Scanner(System.in);
		select = new Seletor();
		
		boolean print = true;
		boolean continuar = true;
		while(continuar) {
			
			if(print)
				System.out.println(lar);
			
			System.out.print(">>");
			print = false;
			
			String str = sc.nextLine();
			
			if(str==null)
				continue;
			
			if(str.isEmpty())
				continue;
			
			switch (unCommand(str)) {
			
			case 0:
				lar.exec(str);
				print = true;
				break;
				
			case 1:
				Iterator<String> scr = lar.toScreen(true).iterator();
				while(scr.hasNext())
					System.out.println(scr.next());
				break;
				
			case 2:
				System.out.print("\n");
				List<String> b = lar.toScreen(true);
				int len = (b.size()<=0)?(0):(b.get(b.size()-1).length());
				for(int c=0; c<len; c++)
					System.out.print("_");
				System.out.println("\n");
				lar.reset();
				break;
				
			case 3:
				continuar = false;
				break;
				
			default:
				String temp = lar.getVar(str);
				str = select.gerar(temp);
				lar.declare(temp);
				System.out.println(str);
				lar.exec(str);
				print = true;
				break;
			}				
			
		}
		
		sc.close();
		
	}

}
