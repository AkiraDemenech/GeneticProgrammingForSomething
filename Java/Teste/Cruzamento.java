package Teste;

import java.util.Scanner;

import PG.Seletor;

public class Cruzamento {

	private static void cruzador () {
		
		Seletor s = new Seletor();
		Scanner sc = new Scanner(System.in);
		
		System.out.print("1>");
		String a = s.getLAR().op.normalizeStr(sc.nextLine());
		System.out.print("2>");
		String b = s.getLAR().op.normalizeStr(sc.nextLine());
		
		String dados = Mutacao.getVariables(a) + Mutacao.getVariables(b);
		if(a.indexOf("<")==0 && a.indexOf(">")==a.length()-1)
			a = s.gerar(dados);
		if(b.indexOf("<")==0 && b.indexOf(">")==b.length()-1)
			b = s.gerar(dados);
		
		sc.close();
		
		for(int c=0; c<5; c++) {
			System.out.println(a + "\n");
			System.out.println(b + "\n");
			System.out.println("=");
			a = s.cruzar(a, b);
			b = s.cruzar(a, b);
		}
		
	}
	
	public static void main(String[] args) {
		
		cruzador();

	}

}
