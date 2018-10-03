'''genetic is the main Module to build the aplication of Genetic Programming
	é o módulo Principal para edificar a aplicação da Programação Genética (PG)'''

import chromosome as y

gmax = 10
pop_size = 100
mutation_rate = 144/1024
population = []

def evolution (target = y.ftarget):
	y.ftarget = target
	y.mrate = mutation_rate
	y.OnCrossing = True
	best = []

	try:
		global population
		population = y.generation(True, pop_size)

		g = 0
		while(True):
			print('Generation #%02d <>' %g, end=' ')
			population.sort()
			population.reverse()
			print(population[0])
			
			g += 1
			if(g>gmax):
				return tuple(best)

			best = []
			generation = [population[0]]
			for c in range(1, len(population)):
				try:
					generation.append(y.crossover(population[c], population[c - 1], False))
				except KeyboardInterrupt:
					generation.append(best[len(best) - 1])
				if(population[c].ftn != 0):
					best.append(population[c])

			population = generation
	
	except KeyboardInterrupt:
		return best