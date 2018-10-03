package ELRA;
import java.util.ArrayList;
import java.util.List;

public class Interpretador {
	
	private boolean fail;
	private int error;
	private double ans;
	private String out;
	private Pilha buff;
	private Indice variaveis;
	private List<String> prompt;
	private String lastLine;
	private Interpretador test;
	public final Operator op = new Operator();
	public static final double NAN = Double.MIN_VALUE;
	public static final double PI = 3.141592653589793;
	public final String[] ERR = {
			"Perfeito",
			"Parênteses Incorreto",
			"Dígito Inválido",
			"Argumento Ausente",
			"Base Numérica Não Inteira",
			"Base Numérica Inválida",
			"Variável Não Declarada",
			"Argumento em Excesso",
			"Atribuição Falha",
			"Comando Desconhecido",
			"Imperfeição Desconhecida"
	};
	
	private String piDivision (double p) {
		
		float n = (float)op.div(p, PI);
		int c = 1;
		while(op.mod((float)n, (float)PI)==0 && n!=0) {
			n = (float)op.div((float)n, (float)PI);
			c++;
		}
		
		String s = n + "";
		if(n%1==0)
			s = ((long)n) + "";
		
		return s + " pi" + ((c>1)?("^"+c):(""));
	}
	
	public String nStr (double n) {
		if(n==NAN)
			return "NaN";
		
		return (n + " [" + piDivision(n) + "]");
	}
	
	private double execLine (String line) {
		
		if(line==null)
			return NAN;
		
		fail = false;
		error = 0;
		lastLine = line;
		
		List<String> piping = new ArrayList<>();
		
		StringBuffer b = new StringBuffer();
		for(int c=0; c<line.length(); c++) {
			if(line.charAt(c)==',' || (line.charAt(c)<32 && !Character.isWhitespace(line.charAt(c)))) {
				piping.add(b.toString());
				b.delete(0, b.length());
			} else
				b.append(line.charAt(c));
		}
		piping.add(b.toString());
		
		for(int c=0; c<piping.size()-1; c++) {
			
			if(piping.get(c)==null)
				continue;
			
			execLine(piping.get(c));
			lastLine = line;
			
			if(hasError())
				return NAN;
			
		}
		
		line = b.toString();
		
		List<String> command = op.cutString(line);
		
		if(command==null) {
			//out = "Parênteses Incorreto";
			fail = true;
			error = 1;
			return NAN;
		}
		
		if(command.size()==0) {
			out = "res = " + nStr(ans);
			return 0;
		}
		
		buff.reset();
		boolean expect = false;
		int base = 10;
		
		for(int i=command.size()-1; i>=0; i--) {
			
			line = command.get(i);
			if(line==null)
				continue;
			
			double num = 0;
			int mult = 1; //
			for(int c=line.length()-1; c>=0; c--) {
				
				char ch = line.charAt(c);
				
				if(expect) {
					
					buff.get();
					
					if(op.value(ch, base)<0) {
						
						//out = "Dígito Inválido";
						fail = true;
						error = 2;
						return NAN;
						
					}
					
					num += op.value(ch, base)*mult;
					mult *= base;
					
					if(c==0)
						expect = false;
					
				} else {
					
					base = 10;
					
					if(op.eNumero(ch)) {
						
						// leitura de literal decimal
						num += (ch-48)*mult;
						mult *= base;
						
					} else if(ch==':') {
						
						// modificação da base numérica
						
						if(buff.isEmpty()) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						num = buff.get();
						
						if(num%1!=0) {
							
							// Base Numérica Não-Inteira
							//out = "Base Numérica Não Inteira";
							fail = true;
							error = 4;
							return NAN;
							
						}
						
						base = (int) num;
						if(base<2 || base>62) {
							
							// Base Numérica Inválida
							//out = "Base Numérica Inválida";
							fail = true;
							error = 5;
							return NAN;
							
						}
						
						expect = true; // esperar número
						
					} else if(ch=='@') {
						// número não-Real
						num = NAN;
						
					} else if(ch=='\\') {
						// número Pi
						num = PI;
						
					} else if(ch=='&') {
						// coerção do número
						if(buff.isEmpty()) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						num = op.coerc(buff.get());
						
					} else if(ch=='!') {
						// fatorial do número
						if(buff.isEmpty()) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						num = op.fact(buff.get()); 
						
					} else if(ch==(char)39) {
						// valor absoluto (módulo)
						if(buff.isEmpty()) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						num = op.abs(buff.get());
						
					} else if(ch=='"') {
						// valor do cos
						if(buff.isEmpty()) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						num = op.cos(buff.get());
						
					} else if(ch=='_') {
						// valor invertido
						if(buff.isEmpty()) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						num = op.neg(buff.get());
						
					} else if(ch=='$') {
						// MDC
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.mdc(x, y);  
						
					} else if(ch=='.') {
						// Logaritmo
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.log(x, y);
						
					} else if(ch=='~') {
						// Radiciação
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.rad(x, y);
						
					} else if(ch=='^') {
						// Potenciação
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.pow(x, y); 
						
					} else if(ch=='%') {
						// Resto da Divisão (módulo)
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.mod(x, y);
						
					} else if(ch=='/') {
						// Divisão
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.div(x, y);
						
					} else if(ch=='*') {
						// Multiplicação
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.mult(x, y);
						
					} else if(ch=='-') {
						// Subtração
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.sub(x, y);
						
					} else if(ch=='+') {
						// Soma
						if(buff.size()<2) {
							
							//out = "Argumento Ausente";
							fail = true;
							error = 3;
							return NAN;
							
						}
						
						double x = buff.get();
						double y = buff.get();
						num = op.som(x, y);
						
					} else if(op.eLetra(ch)) {
						// Variáveis
						if(!variaveis.varExist(ch)) {
							
							//out = "Variável Não Declarada";
							fail = true;
							error = 6;
							return NAN;
							
						}
						
						num = variaveis.get(ch);
						
					} else if(ch=='#') {
						// ANS
						num = ans;
						
					} else if(ch=='?') {
						
						// rand
						num = Math.random();
						
					} else if(ch=='=') {
						// Atribuição
						if(buff.size()!=1) {
							
							//out = "Argumento em Excesso";
							fail = true;
							error = 7;
							return NAN;
							
						}
						
						if(i!=1) {
							
							//out = "Atribuição Falha";
							fail = true;
							error = 8;
							return NAN;
							
						}
						
						if(command.get(0).length()!=1) {
							
							//out = "Atribuição Falha";
							fail = true;
							error = 8;
							return NAN;
							
						}

						if(!op.eLetra(command.get(0).charAt(0)) && command.get(0).charAt(0)!='#') {
							
							//out = "Atribuição Falha";
							fail = true;
							error = 8;
							return NAN;
							
						}
						
						num = buff.get();
						if(op.eLetra(command.get(0).charAt(0)))
							variaveis.set(command.get(0).charAt(0), num);
						else
							ans = num;
						out = ((command.get(0).charAt(0)=='#')?("res"):(command.get(0).charAt(0))) + " = " + nStr(num);
						return num;
						
					} else {
						
						//out = "Comando Desconhecido";
						fail = true;
						error = 9;
						return NAN;
						
					}
					
				}
				
			} // segue para o caractere anterior
			
			if(num==Double.NaN || num==Double.NEGATIVE_INFINITY || num==Double.POSITIVE_INFINITY || Double.isNaN(num) || Double.isInfinite(num))
				num = NAN;
				
			buff.put(num);
			
		} // segue para a expressão anterior
		
		if(buff.size()>1) {
			
			//out = "Argumento em Excesso";
			fail = true;
			error = 7;
			return NAN;
			
		}
		
		if(buff.isEmpty()) {
			
			//out = "Argumento Ausente";
			fail = true;
			error = 3;
			return NAN;
			
		}
		
		double n = buff.get();
		ans = n;
		out = "res = " + nStr(n);
		return n;
	}
	
	public double exec (String line) {
		double d = execLine(line);
		if(hasError())
			out = "ERRO == " + ERR[error];
		prompt.add(lastLine);
		prompt.add(out);
		return d;
	}
	
	//	private double execFunc (String[] function) {
	//		
	//		// iteração da EXECLINE
	//		
	//		return 0;
	//	}
		
	public List<String> toScreen (boolean hasLineNumber) {
		List<String> copy = new ArrayList<>();
		for(int c=0; c<prompt.size(); c++) {
			StringBuffer b = new StringBuffer();
			if(prompt.get(c)!=null)
				for(int i=0; i<prompt.get(c).length(); i++)
					b.append(prompt.get(c).charAt(i));
			if(hasLineNumber)
				b = new StringBuffer("<" + c + "> " + b); 
			copy.add(b.toString());
		}
		return copy;
	}

	public Interpretador getLAR () {
		if(test==null)
			test = new Interpretador();
		return test;
	}
	
	public int whichError () {
		return error; 
	}
	public boolean hasError () {
		return fail;
	}
	public boolean hasError (String com) {
		
		getLAR().reset();
		
		if(test.exec(com)==NAN)
			return test.hasError();
		
		return false;
	}
	
	public double answer () {
		return ans;
	}
	public String lastCommand () {
		return lastLine;
	}
	public String varList () {
		return variaveis.toString();
	}
	
	public void declare (String var) {
		
		StringBuffer sb = new StringBuffer();
		
		if(var!=null)
			for(int c=0; c<var.length(); c++) {
				char ch = var.charAt(c);
				execLine(ch + "");
				if(hasError())
					sb.append(ch + " = 0, ");
			}
		
		exec(sb.append("# = 0 ; |||").toString());
		
	}
	public String getVar (String source) {
		if(source==null)
			return "";
		
		StringBuffer sb = new StringBuffer();
		
		for(int c=0; c<source.length(); c++) {
			char ch = source.charAt(c);
			if(Character.isAlphabetic(ch) || ch=='@' || ch=='#' || ch=='?')
				sb.append(ch);
		}
		
		return sb.toString();
	}
	
	public void reset () {
		
		fail = false;
		error = 0;
		ans = 0;
		out = "res = 0.0";
		buff = new Pilha();
		variaveis = new Indice();
		prompt = new ArrayList<>();
		lastLine = "";
		
	}
	public Interpretador () {
		
		// Ao modificar, atualizar também o Método reset()
		
		reset();
		
		// Ao modificar, atualizar também o Método reset()
		
	}
	
	@Override
	public String toString () {
		return out;
	}
	
	@Override
	public boolean equals (Object obj) {
		
		Interpretador lar = (Interpretador) obj;
		
		if(lar==null)
			return false;
		
		if(lar.hasError()!=hasError())
			return false;
		
		if(lar.whichError()!=whichError())
			return false;
		
		if(lar.answer()!=answer())
			return false;
		
		if(!lar.toString().equals(toString()))
			return false;
		
		if(!lar.lastCommand().equals(lastCommand()))
			return false;
		
		if(!lar.varList().equals(varList()))
			return false;
		
		if(!lar.toScreen(true).equals(toScreen(true)))
			return false;
		
		if(!lar.getLAR().equals(test))
			return false;
		
		return true;
	}

}
