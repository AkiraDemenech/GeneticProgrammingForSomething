package PG;

import java.util.ArrayList;
import java.util.List;

import ELRA.Interpretador;

public class Seletor {
	
	private Interpretador tester;
	private List<String> op;
	
	public Interpretador getLAR () {
		if(tester==null)
			tester = new Interpretador();
		return tester;
	}
	
	private List<String> subtree (List<String> tree) {
		
		if(tree==null)
			return null;
		if(tree.size()<2)
			return tree;
		
		List<String> sub = new ArrayList<>();
		
		int p = 1 + (rand()%(tree.size()-1));
		
		for(int c=p, n=p+1; c<n; c++) {
			
			if(tree.get(c)==null)
				continue;
			
			n += valor(tree.get(c).charAt(0));
			
			sub.add(tree.get(c));
			
		}
		
		return sub;
	}
	private List<String> revSubtree (List<String> tree) {
		
		if(tree==null)
			return null;
		if(tree.size()<2)
			return tree;
		
		List<String> sub = new ArrayList<>();
		
		int p = 1 + (rand()%(tree.size()-1));
		int n = p + 1;
		
		for(int c=0; c<p; c++) {
			
			if(tree.get(c)==null)
				continue;
			
			sub.add(tree.get(c));
		}
		
		sub.add(null);
		for(int c=p; c<n; c++) {
			
			if(tree.get(c)==null)
				continue;
			
			n += valor(tree.get(c).charAt(0));
			
		}
		
		for(int c=n; c<tree.size(); c++) {
			
			if(tree.get(c)==null)
				continue;
			
			sub.add(tree.get(c));
		}
		
		return sub;
	}
	
	public int rand () {
		return ((int)(Math.random()*Integer.MAX_VALUE));
	}
	private int valor (char value) {
		
		if(Character.isWhitespace(value))
			return -1;
		
		if(op==null) {
			op = new ArrayList<>();
			op.add("?#\\@0123456789QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
			op.add("_'" + '"' + "!&");
			op.add("+-*/%^~.$");
		}
		
		for(int i=0; i<op.size(); i++)
			for(int c=0; c<op.get(i).length(); c++)
				if(value==op.get(i).charAt(c))
					return i;
		
		return -2;
	}
	private String gerarValor (int type, String varList) {
		
		if(type==0) {
			
			if(varList==null)
				varList = "";
			
			varList = varList + "\\";
			int lim = varList.length();
			
			if(rand()%lim==0) {
				
				if(rand()%11<0)
					return ("" + rand()%11);
				
				return ("" + rand()%111);
				
			}
			char c = varList.charAt(rand()%(varList.length()));
			return ("" + c);
			
		}
		
		if(type==1) {
			
			switch (rand()%5) {
			
			case 0:
				return "_";
				
			case 1:
				return "'";
				
			case 2:
				return "" + '"';
				
			case 3:
				return "!";
				
			case 4:
				return "&";
				
			}
			
			return " ";
			
		}
		
		if(type==2) {
			
			while(true)
				switch (rand()%9) {
				
				case 0:
					return "+";
					
				case 1:
					return "-";
					
				case 2:
					return "*";
					
				case 3:
					return "/";
					
				case 4:
					return "%";
					
				case 5:
					return "^";
					
				case 6:
					return "~";
					
				case 7:
					return ".";
					
				case 8:
					return "$";
				
				}
			
		}
			
		return "";
	}
	public List<String> strings (String s) {
		
		if(s==null)
			return null;
		
		List<String> l = new ArrayList<>();
		
		StringBuffer b = new StringBuffer();
		char ch = ';';
		for(int c=0; c<s.length(); c++) {
			
			ch = s.charAt(c);
			
			if(!Character.isWhitespace(ch))
				b.append(ch);
			else {
				if(b.length()>0) {
					l.add(b.toString());
					b = new StringBuffer();
				}
				while(c<s.length()) {
					if(!Character.isWhitespace(s.charAt(c))) {
						c--;
						break;
					}
					c++;
				}
			}
				
		}
		if(b.length()>0)
			l.add(b.toString());
		
		return l;
	}
	
	public String gerar (String var) {
		
		StringBuffer proto = new StringBuffer();
		
		for(int c=0, n=1, t=0; c<n; c++) {
			t = rand()%((2) + ((rand()%(proto.length()+1)<123)?(1):(0)));
			proto.append(gerarValor(t, var) + " ");
			n += t;
		}
		
		return proto.toString();
	}
	
	public String mutar (String x, String var) {

		if(x==null)
			return null;
		
		List<String> blocos = strings(x);
		if(blocos==null)
			return null;
		if(blocos.isEmpty())
			return null;
		
		StringBuffer bf = new StringBuffer();
		int m = rand()%blocos.size();
		
		for(int c=0; c<blocos.size(); c++) {
			
			String temp = blocos.get(c);
			
			if(c==m) {
				
				switch(valor(blocos.get(c).charAt(0))) {
				
				case 0:
					if(rand()%blocos.size()<rand()%8)
						temp = gerar(var);
					else
						temp = gerarValor(0, var);
					break;
					
				case 1:
					if(rand()%6==0)
						temp = "";
					else
						temp = gerarValor(1, var);
					break;
					
				case 2:
					temp = gerarValor(2, var);
					break;
					
				}
				
			}
				
			
			if(!temp.isEmpty())
				bf.append(temp).append(" ");
		}
		
		return bf.toString();
	}
	
	public String cruzar (String x, String y) {

		if(x==null || y==null)
			return null;
		
		if(rand()%2==0) {
			String s = x;
			x = y;
			y = s;
		}
		
		List<String> bx = revSubtree(strings(x));
		List<String> by = subtree(strings(y));
		
		if(bx==null || by==null)
			return null;
		if(bx.isEmpty() || by.isEmpty())
			return null;
		
		StringBuffer sb = new StringBuffer();
		
		for(int c=0; c<bx.size(); c++) {
			if(bx.get(c)==null)
				for(int i=0; i<by.size(); i++)
					sb.append(by.get(i) + " ");
			else
				sb.append(bx.get(c) + " ");
		}
		
		return sb.toString();
		
	}

}
