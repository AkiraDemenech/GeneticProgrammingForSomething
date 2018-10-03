package ELRA;

import java.util.ArrayList;
import java.util.List;

public class Operator {
	
	public static final double NAN = Double.MIN_VALUE;
	private static final String base62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public double som (double a, double b) {
		if(a==NAN || b==NAN)
			return NAN;
		return (a+b);
	}
	public double sub (double a, double b) {
		if(a==NAN || b==NAN)
			return NAN;
		return (a-b);
	}
	public double mult (double a, double b) {
		if(a==0 || b==0)
			return 0;
		if(a==NAN || b==NAN)
			return NAN;
		return (a*b);
	}
	public double div (double a, double b) {
		if(b==0)
			return NAN;
		if(a==0)
			return 0;
		if(a==NAN || b==NAN)
			return NAN;
		return (a/b);
	}
	public double mod (double a, double b) {
		if(a==NAN || b==NAN || b==0)
			return NAN;
		
		boolean negA = (a<0);
		boolean negB = (b<0);
		
		if((a/b)%1==0)
			return 0;
		
		a = abs(a);
		b = abs(b);
		
		if(b%1>0)
			while(a>=b)
				a -= b;
		else
			a = ((a)%((long)(b)));
		
		if((a%1)>0) {
			
			if((a%1)<0.0000001)
				a = (a - (a%1));
			
			if((a%1)>0.9999999)
				a = (a - (a%1)) + 1;
		}
		
		if(negA && negB)
			return (-a);
		if(negA || negB) {
			
			double ans = (b - a);
			if(negB)
				ans = -ans;
			return ans;
			
		}
		return (a);
			
	}
	public double pow (double a, double b) {
		if(a==NAN || b==NAN || (a==0 && b==0))
			return NAN;
		return Math.pow(a, b);
	}
	public double rad (double a, double b) {
		if(a==NAN || b==NAN || b==0 || (a<0 && b%2==0))
			return NAN;
		return pow(a, 1/b);
	}
	public double log (double a, double b) {
		if(a==NAN || b==NAN)
			return NAN;
		if(a==b)
			return 1;
		if(b==1 || b==0)
			return NAN;
		
		return (Math.log(a)/Math.log(b));
	}
	public double mdc (double a, double b) {
		if(a==NAN || b==NAN || (a==0 && b==0))
			return NAN;
		if(mod(a, b)==0)
			return b;
		if(mod(b, a)==0)
			return a;
		
		long d = 0;
		for(long c=1; c<=a && c<=b; c++) {
			if(mod(a, c)==0 && mod(b, c)==0)
				d = c;
		}
		if(d>0)
			return d;
		
		return 1;
		
	}
	public double neg (double a) {
		if(a==NAN)
			return NAN;
		return -a;
	}
	public double abs (double a) {
		if(a==NAN)
			return NAN;
		return ((a<0)?(-a):(a));
	}
	public double fact (double a) {
		if(a==NAN)
			return NAN;
		
		double n = 1;
		for(int c=1; c<=a; c++) {
			n *= c;
			if(isNaN(n) || n<0)
				return NAN;
		}
		
		return n;
		
	}
	public double coerc (double a) {
		if(a==NAN)
			return NAN;
		
		if(a%1<0.5)
			a -= a%1;
		else
			a = (a - (a%1))+1;
		
		return a;
	}
	public double cos (double a) {
		if(a==NAN)
			return NAN;
		return (Math.cos(a));
	}
	
	public boolean eLetra (char ch) {
		return ((ch>=65 && ch<91) || (ch>=97 && ch<123));
	}
	public boolean eNumero (char ch) {
		return (ch>=48 && ch<58);
	}
	public int digitValue (char digit) {
		for(int c=0; c<base62.length(); c++) {
			if(digit==base62.charAt(c))
				return c;
		}	
		return -1;
	}
	public int value (char digit, int base) {
		
		if(base<2 || base>62)
			return -1;
		
		if(base<=36 && digit>90)
			digit -= 32;
		
		int a = digitValue(digit);
		if(a==-1 || a>=base)
			return -2;
		
		return a;
		
	}
	
	public boolean eDigito (char ch) { 
		return (digitValue(ch)!=-1);
	}
	
	public String normalizeStr (String anorm) {
		if(anorm==null)
			return ("");
		
		int b = 0;
		StringBuffer buff = new StringBuffer();
		for(int c=0; c<anorm.length(); c++) {
			
			if(anorm.charAt(c)==';')
				break; // para de gravar
			
			if(anorm.charAt(c)=='(' || anorm.charAt(c)==')') {
				
				if(anorm.charAt(c)=='(')
					b++;
				else {
					b--;
					if(b<0)
						return null;
				}
				
				continue; // não grava esses caracteres
			}
			
			if(c>0) {
				if(eDigito(anorm.charAt(c)) && eDigito(anorm.charAt(c-1))) {
					buff.append(anorm.charAt(c));
				} else {
					buff.append(" " + anorm.charAt(c));
				}
			} else 
				buff.append(anorm.charAt(c));
			
			
		}
		
		if(b!=0)
			return null;
		
		return buff.toString();
	}
	
	public List<String> cutString (String command) {
		
		command = normalizeStr(command);
		
		if(command==null)
			return null;
		
		List<String> blocks = new ArrayList<>();
		
		for(int c=0; c<command.length(); c++) {
			
			while(Character.isWhitespace(command.charAt(c)) && c<command.length()) {
				c++;
				if(c>=command.length())
					break;
			}
			
			if(c<command.length()) {
				if(command.charAt(c)==';')
					break;
				
				StringBuffer com = new StringBuffer();
				while((!Character.isWhitespace(command.charAt(c))) && c<command.length()) {
					com.append(command.charAt(c++));
					if(c>=command.length())
						break;
				}
				
				blocks.add(com.toString());
			}
			
		}
		
		return blocks;
	}
	
	public boolean isNaN (double num) {
		return (num==Double.NaN || num==Double.NEGATIVE_INFINITY || num==Double.POSITIVE_INFINITY || Double.isNaN(num) || Double.isInfinite(num));
	}

}
