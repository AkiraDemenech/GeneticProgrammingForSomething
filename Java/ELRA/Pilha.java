package ELRA;

public class Pilha {
	
	private double[] valores;
	private int tamanho;
	
	public Pilha () {
		valores = new double[16];
		tamanho = 0;
	}
	
	public void reset () {
		tamanho = 0;
	}
	
	public double[] copyTo (double[] vector) {
		
		if(vector==null || valores==null)
			return new double[0];
		
		for(int c=0; c<vector.length && c<valores.length; c++) {
			vector[c] = valores[c];
		}
		
		return vector;
	}
	
	public void put (double v) {
		
		if(tamanho>=valores.length)
			valores = copyTo(new double[valores.length*2]);
		
		valores[tamanho++] = v; 
	}
	public double get () {
		
		if(tamanho<=0)
			return Double.MIN_VALUE;
		
		return valores[--tamanho];
	}
	public int size () {
		return tamanho;
	}
	public boolean isEmpty () {
		return tamanho==0;
	}

}
