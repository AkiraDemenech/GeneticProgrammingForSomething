'''Chromosome is the Module for Generation, Mutation, Crossover and Fitness calculation.
	Cromossomo é o Módulo para Geração, Mutação, Cruzamento e cálculo do Fitness (Avaliação).'''

from trymathgp.exm import nan, inf, isnan, isinf, getlist
import trymathgp.exm as x

#	A Função Objetivo padrão [target default] é f(x)=x
default = 'x'
ftarget = x.variation(default)
def settarget (t = default):
	global ftarget
	ftarget = x.variation(t)
#settarget()

#	A Taxa de Mutação inicial é 0:1 [0%] e não ocorre durante o Cruzamento [Crossing Over]
mrate = 0
OnCrossing = False

#	Se ocorre permutação aleatória [random swap] no Cruzamento [Crossover]
SwapCross = False
RandomCross = False

#	Todos os valores possíveis [e quantos argumentos que pedem] para a geração estão contidos no dicionário
values = {}
for c in x.functions.keys():
	values[c] = x.functions[c][1]
for c in range(len(x.functions)):
	values[c] = 0
'''
for c in list(x.functions.keys()) + list(range(10)):
	try:
		values[c] = x.functions[c][1]
	except KeyError:
		values[c] = 0
'''
names = list(values)
namesbyargs = [[], [], []]
for n in names:
	namesbyargs[values[n]].append(n)

def value (n):
	try:
		return x.functions[n][1]
	except KeyError:
		return 0

def generation (asChromosome = False, n = None):
	
	b = []
	if(n and type(n)==int):
		for c in range(n):
			b.append(generation(asChromosome))
		return b

	c = 1
	s = len(values)
	while(c > 0):
		n = names[int(x.rand() * s)]
		b.append(n)
		c += values[n] - 1
	
	if(asChromosome):
		b = genotype(b)

	return b

def mutation (g, rate = mrate, asList = True):
	g = getlist(g)
	h = -1
	while(h < len(g) - 1):
		
		h += 1
		if(x.rand() >= rate):
			continue

		t = value(g[h])
		if(t==0):
			k = generation()
			h += len(k) - 1
			k = g[0 : h] + k

			if(h<len(g)-1):
				if(h==len(g)-2):
					g = k + [g[h+1]]
				else:
					g = k + g[h+1:len(g)]

		else:
			if(t == 1 and x.rand() < 1 / len(namesbyargs[1])):
				g.pop(h)
			else:
				g[h] = namesbyargs[t][int(x.rand() * len(namesbyargs[t]))]

	if(not asList):
		g = genotype(g)
	return g

def crossover (a, b, asList = True):

	if(SwapCross and (not RandomCross or x.rand() < 0.5)):
		a, b = b, a 
	a = genotype(a)
	a.crossover(b)
	
	if(OnCrossing):
		a = mutation(a, asList=asList)
	elif(asList):
		return a.chromosome

	return a

def subtree (c):
	c = getlist(c)

	n = int(x.rand() * len(c))
	g = []
	h = 1
	
	while(h > 0):
		g.append(c[n])
		h -= 1 - value(c.pop(n))
	
	return g, c, n

def supertree (a, b, n):
	return b[:n] + getlist(a) + b[n:]

def deviation (c = default, t = ftarget):
	sum = 0
	c = x.variation(c)
	for i in range(len(c)):
		if(str(abs(t[i])) == str(abs(c[i]))):
			continue
			
		try:
			sum += abs(t[i] - c[i])
		except OverflowError:
			return inf
		
	return sum

def fitness (c, t = ftarget):
	d = deviation(c, t)
	if(isnan(d)):
		return 0
	return x.div(1, d)

class genotype:
	
	def __init__ (self, gen = None, target = ftarget, rate = mrate, mcross = OnCrossing):
		if(gen):
			self.chromosome = getlist(gen)
		else:
			self.chromosome = generation()
		
		self.target = target
		self.rate = rate
		self.mcross = mcross
		self.ftn = fitness(self, self.target)

	def __eq__ (self, o):
		return o.ftn == self.ftn
	def __ne__ (self, o):
		return not self.__eq__(o)
	def __lt__ (self, o):
		return self.ftn < o.ftn
	def __le__ (self, o):
		return self.__eq__(o) or self.__lt__(o)
	def __ge__ (self, o):
		return not self.__lt__(o)
	def __gt__ (self, o):
		return not self.__le__(o)

	def __str__ (self):
		return "".join(str(c) + ' ' for c in self.chromosome)
	def __repr__ (self):
		return '"' + self.__str__() + '"'

	def __len__ (self):
		return self.chromosome.__len__()

	def __contains__ (self, o):
		return self.chromosome.__contains__(o)

	def copy (self):
		return genotype(self, self.target, self.rate, self.mcross)

	def mutate (self):
		self.chromosome = mutation(self, self.rate)

	def crossover (self, other):
		self.chromosome = supertree(subtree(other)[0], *subtree(self)[1:])

	def fitness (self):
		self.ftn = fitness(self, self.target)