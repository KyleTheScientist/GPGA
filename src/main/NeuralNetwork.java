package main;

public class NeuralNetwork implements Comparable {

	private static float mutationRate = .01f;
	final float uniformRate = .5f;
	private static float bias = 1f;

	final float T = 0;
	int inputs = 0;
	int midLayers = 0;
	int midNeurons = 0;
	int outputs = 0;
	float[][] network;
	float[][] weights;

	public float score;

	String saveDirectory = "C:/Users/Kylw/Desktop/Code stuff/Assets/NNTraining/Networks/";

	public NeuralNetwork(int inputs, int midLayers, int midNeurons, int outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
		this.midLayers = midLayers;
		this.midNeurons = midNeurons;

		build();
	}

	public int mutate() {
		int mutations = 0;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				if (Math.random() < mutationRate) {
					int operation = (int) random(0, 2);
					switch (operation) {
					case (0):
						weights[i][j] = random(-1, 1);
						break;
					case (1):
						weights[i][j] *= map((float) Math.random(), 0, 1, .5f, 1.5f);
						break;
					case (2):
						weights[i][j] *= -1;
						break;
					}
					mutations++;
				}
			}
		}
		return mutations;
	}

	public NeuralNetwork crossover(NeuralNetwork p) {
		NeuralNetwork n = new NeuralNetwork(inputs, midLayers, midNeurons, outputs);
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				if (Math.random() < uniformRate) {
					n.weights[i][j] = weights[i][j];
				} else {
					n.weights[i][j] = p.weights[i][j];
				}
			}
		}
		return n;
	}

	private void build() {
		int total = 1 + midLayers + 1;
		network = new float[total][]; // init network[]
		network[0] = new float[inputs]; // set input[] size
		network[total - 1] = new float[outputs]; // set output[] size

		for (int i = 1; i <= midLayers; i++) { // set midlayer size
			network[i] = new float[midNeurons];
		}

		weights = new float[total - 1][]; // init weight[]
		for (int i = 0; i < weights.length; i++) {
			float[] n1 = network[i], n2 = network[i + 1]; // each value needs to
															// connect to each
															// value in the
															// layer above it
			weights[i] = new float[n1.length * n2.length + 1]; // therefore
																// there are
																// n1.length *
																// n2.length
																// connections
																// between each
																// layer

			for (int j = 0; j < weights[i].length; j++) { // set random values
															// for each weight
															// between 0 and 1
				weights[i][j] = random(-1, 1);
			}
		}
	}

	public float[] calc(float[] input) {
		if (input.length != network[0].length) { // check for invalid [] length
			System.out.println(
					"Invalid input array length.\n\tInput: " + input.length + "| Expected: " + network[0].length);
			return null;
		}
		// println(networkToString());
		for (int i = 0; i < network[0].length; i++) { // set inputs
			network[0][i] = input[i];
		}
		for (int layer = 1; layer < network.length; layer++) { // iterate
																// through each
																// layer
			for (int j = 0; j < network[layer].length; j++) { // iterate through
																// each neuron
				float sum = 0;
				for (int k = 0; k < network[layer - 1].length; k++) { // iterate
																		// through
																		// each
																		// neuron
																		// in
																		// previous
																		// row
																		// and
																		// add
																		// weighted
																		// sum
																		// of
																		// outputs
					float val = network[layer - 1][k]; // get neuron value from
														// previous layer
					float weight = correspondingWeight(layer, j, k); // get
																		// correct
																		// weight
					sum += weight * val;
				}
				sum += bias * weights[layer - 1][weights[layer - 1].length - 1]; // add
																					// the
																					// value
																					// of
																					// a
																					// bias
																					// neuron
																					// to
																					// each
																					// layer
				network[layer][j] = tanh(sum);
			}
		}
		return network[network.length - 1];
	}

	private float correspondingWeight(int topLayer, int topLayerNeuron, int botLayerNeuron) {
		return weights[topLayer - 1][botLayerNeuron + topLayerNeuron * network[topLayer - 1].length];
	}

	public float sigma(float input) {
		return (float) (1 / (1 + (float) Math.pow(0.5772156649f, -input)));
	}

	public float tanh(float input) {
		return 2f / (1 + (float) Math.exp(-2 * input)) - 1;
	}

	public NeuralNetwork Copy() {
		NeuralNetwork n = new NeuralNetwork(inputs, midLayers, midNeurons, outputs);
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				n.weights[i][j] = weights[i][j];
			}
		}
		return n;
	}

	public float[][] GetWeights() {
		return weights;
	}

	public int GetInputCount() {
		return inputs;
	}

	public int GetOutputCount() {
		return outputs;
	}

	public int GetMidLayerCount() {
		return midLayers;
	}

	public int GetMidLayerSize() {
		return midNeurons;
	}

	public String toString() {
		String s = "";
		for (int i = 0; i < weights.length; i++) {
			s += "\n";
			for (int j = 0; j < weights[i].length; j++) {
				s += weights[i][j] + " ";
			}
		}
		return s;
	}

	public String networkToString() {
		String s = "";
		for (int i = 0; i < network.length; i++) {
			s += "\n";
			for (int j = 0; j < network[i].length; j++) {
				s += network[i][j] + "\t\t";
			}
		}
		return s;
	}

	private float random(float min, float max) {
		return (float) (Math.random() * (max - min)) + min;
	}

	private float map(float x, float oMin, float oMax, float nMin, float nMax) {
		return ((nMax - nMin) * (x - oMin) / (oMax - oMin)) + nMin;
	}

	public static float getMutationRate() {
		return mutationRate;
	}

	public static void setMutationRate(float mutationRate) {
		if (mutationRate < 0)
			mutationRate = 0;
		NeuralNetwork.mutationRate = mutationRate;
	}

	public static float getBias() {
		return bias;
	}

	public static void setBias(float bias) {
		NeuralNetwork.bias = bias;
	}

	public int compareTo(Object o) {
		NeuralNetwork n = (NeuralNetwork) o;
		if (n.score < score) {
			return -1;
		}
		if (n.score > score) {
			return 1;
		}
		return 0;
	}

}