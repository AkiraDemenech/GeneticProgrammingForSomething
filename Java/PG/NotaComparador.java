package PG;

import java.util.Comparator;

public class NotaComparador implements Comparator<double[]> {

	@Override
	public int compare(double[] arg0, double[] arg1) {
		
		if(arg0[0]==arg1[0])
			return 0;
		
		if(arg0[0]==Double.MIN_VALUE)
			return 1;
		
		if(arg1[0]==Double.MIN_VALUE)
			return -1;
		
		if(arg0[0]>arg1[0])
			return 1;
		
		if(arg0[0]<arg1[0])
			return -1;
		
		return 0;
	}

}
