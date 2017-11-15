package test;

import main.GPGA;
import main.NeuralNetwork;

public class Test {

	public static void main(String[] args) {
		NeuralNetwork template = new NeuralNetwork(5, 1, 5, 5);

		GPGA ga = new GPGA(20, template);
		ga.build();
		float[] inputs = { 1, 2, 3, 4, 5 };
		float[] scores = new float[20];
		int g = 0;
		while (true) {
			g++;
			float best = -5;
			for (int i = 0; i < 20; i++) {
				float[] outputs = ga.getOutputs(i, inputs);
				float diff = 0;
				String out = "[";
				for (int j = 0; j < outputs.length; j++) {
					out += outputs[j] + " ";
					diff -= Math.abs(outputs[j] - inputs[j]/5);
				}
				scores[i] = diff;
				best = Math.max(diff, best);
			}
			System.out.printf("Gen: %d \t%f\n", g, best);
			ga.generation(scores);
		}
	}
}
