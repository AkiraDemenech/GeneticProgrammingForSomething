package Teste;

import java.util.Scanner;

import ELRA.Interpretador;
import PG.Seletor;

public class Mutacao {
	
	public static String getVariables (String raiz) {
		
		if(raiz==null)
			return "";
		
		StringBuffer sb = new StringBuffer();
		
		for(int c=0; c<raiz.length(); c++) {
			if(Character.isAlphabetic(raiz.charAt(c)) || raiz.charAt(c)=='@' || raiz.charAt(c)=='#' || raiz.charAt(c)=='?')
				sb.append(raiz.charAt(c));
		}
		
		return sb.toString();
	}
	public static void declareVariables (String var, Interpretador i) {
		if(var!=null && i!=null)
			for(int c=0; c<var.length(); c++)
				i.exec(var.charAt(c) + " = 0");
	}
	private static void geradorMutantes () {
		System.out.print(">>");
		
		Seletor s = new Seletor();
		
		Scanner sc = new Scanner(System.in);
		String start = sc.nextLine();
		String dados = getVariables(start);
		System.out.println("<" + dados + ">");
		sc.close();
		
		if(start.indexOf("<")==0 && start.indexOf(">")==start.length()-1)
			start = s.gerar(dados);
		
		start = s.getLAR().op.normalizeStr(start);
		
		for(int c=0; c<20; c++) {
			System.out.println(start + "\n");
			start = s.mutar(start, dados);
			if(start==null)
				start = s.gerar(dados);
		}
	}
	
	public static void main (String[] args) {
		
		geradorMutantes();
		
	}

}
