package main;

import java.util.Arrays;

public class GPGA {

	int populationSize = 20;
	NeuralNetwork[] networks;
	NeuralNetwork template;
	float tournamentPercentage = .1f;

	public GPGA(int populationSize, NeuralNetwork template) {
		this.populationSize = populationSize;
		this.template = template;
	}

	public void build() {
		networks = new NeuralNetwork[populationSize];
		for (int i = 0; i < networks.length; i++) {
			networks[i] = new NeuralNetwork(template.inputs, template.midLayers, template.midNeurons, template.outputs);
		}
	}

	public float[] getOutputs(int i, float[] inputs) {
		return networks[i].calc(inputs);
	}

	public void generation(float[] scores) {
		for (int i = 0; i < scores.length; i++) {
			networks[i].score = scores[i];
		}
		Arrays.sort(networks);
		NeuralNetwork[] newGen = new NeuralNetwork[networks.length];
		for (int i = 0; i < newGen.length; i++) {
			NeuralNetwork a = tournamentSelection();
			NeuralNetwork b = tournamentSelection();
			NeuralNetwork child = a.crossover(b);
			child.mutate();
			newGen[i] = child;
		}
		networks = newGen;
	}
	
	NeuralNetwork tournamentSelection() {
		NeuralNetwork[] tournament = new NeuralNetwork[(int) (networks.length * tournamentPercentage)];
		for (int i = 0; i < tournament.length; i++) {
			tournament[i] = networks[(int) (Math.random() * (networks.length))];
		}
		Arrays.sort(tournament);
		return tournament[0];
	}
	
	public void setMutationRate(float rate) {
		NeuralNetwork.setMutationRate(rate);
	}
}
