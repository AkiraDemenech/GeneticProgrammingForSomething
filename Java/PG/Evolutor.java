package PG;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ELRA.Interpretador;

public class Evolutor {
	
	private int geracao;
	
	private double start;
	private double jump;
	private double end;

	private String target;
	
	private List<String> pop;
	private double[] values;
	private int best;
	private List<String> melhores;
	
	public final Seletor op = new Seletor();
	public final Interpretador lar = new Interpretador();
	public final double NAN = Interpretador.NAN;
	public final String variables = "x";
	
	private void avaliar () {
		
		if(values==null)
			values = new double[pop.size()];
			
		if(pop.size()!=values.length)
			values = new double[pop.size()];
		
		int c = 0;
		double[] pontos = new double[1+(int)((end-start)/jump)];
		for(double n = start; n<=end; n+=jump) {
			lar.exec("x = " + ((long)n));
			if(n%1>0)
				lar.exec("x = + x / " + ((int)(n*1000000)) + " 1000000");
			pontos[c] = lar.exec(target);
		}
		
		for(c=0; c<pop.size(); c++) {
			System.out.print("#");
			
			int d = 0;
			double n = 0;
			if(pop.get(c).indexOf(variables)==-1) {
				double b = lar.exec(pop.get(c));
				for(; d<pontos.length; d++) {
					n += Math.abs(pontos[d] - b);
				}
			} else
				for(double i = start; i<=end; i+=jump) {
					
					lar.exec("x = " + ((long)i));
					if(i%1>0)
						lar.exec("x = + x / " + ((int)(i*1000000)) + " 1000000");
					double a = pontos[d++];
					double b = lar.exec(pop.get(c));
					
					if(a==b)
						continue;
					
					//n += lar.op.abs(lar.op.div((a-b), a));
					n += Math.abs(a - b);
				}
			
			if(d==0)
				d = 1;
			
			values[c] = n/d;
		}
		System.out.print("\n");
	}
	private int[] ordenar () {
		List<double[]> l = new ArrayList<>();
		for(int c=0; c<values.length; c++) {
			double[] v = new double[2];
			v[0] = values[c];
			v[1] = c;
			l.add(v);
		}
		Collections.sort(l, new NotaComparador());
		int[] i = new int[values.length];
		for(int c=0; c<i.length; c++) {
			i[c] = (int) l.get(c)[1];
		}
		
		return i;
	}
	private double[] probabilidades () {
		
		double[] pb = new double[pop.size()+1];
		
		for(int c=0; c<values.length; c++) {
			pb[c] = (100/((values.length==0)?(1):(values.length)))/((values[c]==0)?(1/(values.length+1)):(values[c]));
			pb[pop.size()] += pb[c];
		}
		
		return pb;
		
	}
	private int sortear (double[] prb) {
		double r = Math.random()*prb[prb.length-1];
		double n = 0;
		for(int c=0; c<pop.size(); c++) {
			
			if(r>=n && r<n+prb[c])
				return c;
			
			n += prb[c];
		}
		
		return op.rand()%pop.size();
	}
	private int[][] sort () {
		
		double[] prob = probabilidades();
		int[][] ind = new int[2][pop.size()];
		
		for(int c=0; c<pop.size(); c++) {
			
			ind[0][c] = sortear(prob);
			while(ind[1][c]==ind[0][c])
				ind[1][c] = sortear(prob); 
			
		}
		
		return ind;
	}
	private void proximaGeracao () {
//		int[] index = ordenar();
//		
//		if(index!=null) {
//			best = index[0];
//			melhores.add(pop.get(best));
//			geracao++;
//			
//			List<String> b = new ArrayList<>();
//			b.add(pop.get(index[0])); // ELITIZAÇÃO
//			
////			for(int c=0; c<index.length; c++) {
////				
////				if(c<=index.length/2)
////					b.add(op.cruzar(pop.get(index[c]), pop.get(index[c+1])));
////				else
////					if(c==index.length-1) {
////						if(op.rand()%4==0) {
////							b.add(op.mutar(pop.get(index[c-index.length/2]), variables));
////						} else
////							b.add(bestMatch());
////					} else
////						b.add(op.mutar(pop.get(index[c-index.length/2]), variables));
////				
////				
////			}
//			
//			for(int c=0, d=pop.size()-1; d>0; c++) {
//				if(c>=index.length) {
//					b.add(op.gerar(variables));
//					d--;
//					continue;
//				}
//				
//				int ind = index[c];
//				int lim = 1 + (int) (pop.size()/lar.op.pow(2, 1 + values[ind]));
//				
//				for(int i=0; i<lim && d>0; i++) {
//					b.add(op.cruzar(pop.get(ind), pop.get(index[(c+1+i)%index.length])));
//					d--;
//				}
//			}
//			for(int c=0; c<b.size(); c++) {
//				if(op.rand()%9==0)
//					b.set(c, op.mutar(b.get(c), variables));
//			}
//			
//			pop = b;
//		}
		
		geracao++;
		int[] index = ordenar();
		List<String> b = new ArrayList<>();
		b.add(pop.get(index[0]));
		best = index[0];
		melhores.add(pop.get(best));
		
		int[][] in = sort();
		
		for(int c=0; c<pop.size()-1; c++) {
			
			b.add(op.cruzar(pop.get(in[0][c]), pop.get(in[1][c])));
			
			if(op.rand()%11==0)
				b.set(c, op.mutar(b.get(c), variables));
			
		}
		
		pop = b;
		
	}
	
	private boolean compareTarget (Evolutor e) {
		if(e==null)
			return false;
		
		if(e.target==null)
			return target==null;
		
		return e.target.equals(target);
	}
	
	private boolean comparePopulation (Evolutor e) {
		if(e==null)
			return false;
		
		if(e.pop==null)
			return pop==null;
		
		return e.pop.equals(pop);
	}
	
	private boolean compareMelhores (Evolutor e) {
		if(e==null)
			return false;
		
		if(e.melhores==null)
			return melhores==null;
		
		return e.melhores.equals(melhores);
	}
	
	@Override
	public boolean equals (Object obj) {
		
		if(obj==null)
			return false;
		
		Evolutor e = (Evolutor) obj;
		
		if(e.geracao!=geracao)
			return false;
		
		if(e.best!=best)
			return false;
		
		if(e.start!=start)
			return false;
		
		if(e.jump!=jump)
			return false;
		
		if(e.end!=end)
			return false;
		
		if(!values.equals(e.values))
			return false;
		
		if(!compareTarget(e))
			return false;
		
		if(!comparePopulation(e))
			return false;
		
		if(!compareMelhores(e))
			return false;
		
		return true;
	}
	
	@Override
	public String toString () {
		return ("<" + geracao + "> " + bestMatch());
	}
	
	public Evolutor () {
		reset();
	}
	public void reset (boolean all) {
		geracao = 0;
		
		if(all)
			reset();
	}
	public void reset () {
		
		geracao = 0;
		best = 0;
		
		start = 0;
		jump = 1;
		end = 100;
		
		target = "101010:2 ; 42";
		
		melhores = new ArrayList<>();
		popular(100);
	}
	public void popular (int size) {
		
		pop = new ArrayList<>();
		values = new double[size];
		
		for(int c=0; c<size; c++)
			pop.add(op.gerar(variables));
	}
	
	public int generation () { 
		return geracao;
	}
	public void nextGeneration () {
		avaliar();
		proximaGeracao();
	}
	public String next () {
		nextGeneration();
		return last();
	}
	public String last () {
		return (bestMatch() + "; [" + geracao + "] " + dif());
	}
	
	public double dif () {
		return values[best];
	}
	
	public void start (double start) { this.start = start; }
	public double start () { return start; }
	
	public void end (double end) { this.end = end; }
	public double end () { return end; }
	
	public void jump (double jump) { this.jump = jump; }
	public double jump () { return jump; }
	
	public void target (String target) { this.target = target; }
	public String target () { return target; }
	
//	public void variables (String variables) { this.variables = variables; }
//	public String variables () { return variables; }
	
	public List<String> population () {
		return pop;
	}
	public List<String> theBest () {
		return melhores;
	}
	public String bestMatch () {
		if(melhores.isEmpty())
			return "@ ; null";
		return melhores.get(melhores.size()-1);
	}
	public int bestIndex () {
		return best;
	}
}
