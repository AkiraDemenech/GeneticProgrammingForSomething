'''Exm is the Execution Module for a representation of functions
	é o Módulo de Execução para uma representação de funções'''

#   para a Numpy, a constante Pi é 3.141592653589793 e o Número Neperiano é 2.718281828459045
from numpy.random import random as rand
from numpy import add, cos, around, nan, inf
from numpy import subtract as sub 
from numpy import multiply as mul 
from numpy import divide as div
from numpy import log as ln 

import numpy, math
numpy.seterr(all="ignore")

def isnan (n):
	try:
		return numpy.isnan(n)
	except TypeError:
		return False

def isinf (n):
	try:
		return numpy.isinf(n)
	except TypeError:
		return False

def log (a, b):
	try:
		try:
			return div(ln(a), ln(b))
		except AttributeError:
			return div(math.log(a), math.log(b))
	except ValueError:
		return nan

def power (a, b):
	try:
		return pow(a, b)
	except ValueError:
		return 1/pow(a, -b)
	except ZeroDivisionError:
		return inf * a
	except OverflowError:
		return inf

def circa (n):
	try:
		return around(n)
	except AttributeError:
		return n
	
x_value = 0
def x ():
	return x_value

functions = {
	'+': (add, 2),
	'-': (sub, 2),
	'*': (mul, 2),
	'/': (div, 2),
	'^': (power, 2),
	'log': (log, 2),
	'~': (circa, 1),
	'abs': (abs, 1),
	'cos': (cos, 1),
	"x": [x, 0]
}

def getlist (y):
	if(y.__class__.__name__ == 'genotype'):
		return y.chromosome.copy()

	if(type(y) == str):
		y = y.split()
		for c in range(len(y)):
			try:

				try:
					
					try:
						y[c] = int(y[c])
					except ValueError:
						y[c] = float(y[c])	

				except ValueError:
					y[c] = complex(y[c])

			except ValueError:
				pass
		return y

	return list(y)

def variation (f, begin = -26, end = 27, c=1):
	if(not 'x' in f):
		return (run(f),) *( (end - begin) // c)

	y = []
	global x_value
	for w in range(begin, end, c):
		x_value = w
		y.append(run(f))
	x_value = 0
	return tuple(y)

def run (fn):
	'''This lonely method run the functions (list, tuple, str or chromosome)
	Este método solitário roda as funções [listas, tuplas, strings ou cromossomos]'''
	
	f = getlist(fn)

	#for c in range(len(fn) - 1, -1, -1):
	c = len(f) - 1
	while(c >= 0):
		v = f[c]
		if(v.__class__ == str):
			args = []
			for b in range(functions[v][1]):
				args.append(f.pop(c + 1))
			try:
				try:
					f[c] = functions[v][0](*args)
				except ZeroDivisionError:
					f[c] = args[0]*inf
			except OverflowError:
				f[c] = inf
			except AttributeError:
				f[c] = nan
		
		c -= 1
	if(f[0].__class__ != int):
		f[0] = around(f[0], 12)
	return f[0]