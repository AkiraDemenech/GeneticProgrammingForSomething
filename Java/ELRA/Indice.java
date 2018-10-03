package ELRA;

public class Indice {
	
	private double[] variaveis;
	private char[] indice;
	
	public Indice() {
		variaveis = new double[0];
		indice = new char[0];
	}
	
	@Override
	public String toString () {

		StringBuffer varList = new StringBuffer();
		varList = varList.append("[ ");
		
		for(int c=0; c<variaveis.length; c++) {
			
			varList.append(indice[c]).append(" = ").append(variaveis[c]);
			
			if(variaveis.length-1>c)
				varList.append(", ");
			
		}
		
		return (varList.append(" ]").toString());
		
	}
	
	@Override
	public boolean equals (Object obj) {
		
		if(obj==null)
			return false;
		Indice index = (Indice) obj;
		return toString().equals(index.toString());
		
	}
	
	public int index (char name) {
		for(int c=0; c<indice.length; c++) {
			if(name==indice[c])
				return c;
		}
		return -1;
	}
	public boolean varExist (char name) {
		return index(name) != -1;
	}
	public double get (char name) {
		return (index(name)==-1)?(0):(variaveis[index(name)]);
	}
	
	public void set (char name, double value) {
		
		if(index(name)==-1) {
			double[] bDouble = new double[variaveis.length+1];
			char[] bChar = new char[indice.length+1];
			for(int c=0; c<indice.length; c++) {
				bDouble[c] = variaveis[c];
				bChar[c] = indice[c];
			}
			variaveis = bDouble;
			indice = bChar;
			indice[indice.length-1] = name;
		}
		
		variaveis[index(name)] = value;
		
	}
	
	public void delete (char name) {
		
		if(index(name)!=-1) {
			
			double[] bDouble = new double[variaveis.length-1];
			char[] bChar = new char[indice.length-1];
			
			int i = 0;
			for(int c=0; c<indice.length; c++) {
				
				if(indice[c]!=name) {
					
					bChar[i] = indice[c];
					bDouble[i] = variaveis[c];
					i++;
					
				}
				
			}
			
			variaveis = bDouble;
			indice = bChar;
			
		}
		
	}

}
