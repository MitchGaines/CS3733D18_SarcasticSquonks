package nnmodel;


import java.io.File;
import java.util.List;

import com.kylecorry.lann.NN;
import com.kylecorry.lann.PersistentMachineLearningAlgorithm;
import com.kylecorry.lann.activation.Linear;
import com.kylecorry.lann.activation.Sigmoid;
import com.kylecorry.matrix.Matrix;
import data.Node;
import database.Storage;

public class NeuralNetModelCreator {

    public static void main(String[] args) {
        PersistentMachineLearningAlgorithm nn = new NN.Builder()
                .addLayer(2, 4, new Sigmoid())
                .addLayer(4, 2, new Linear())
                .build();

        Storage storage = Storage.getInstance();

        nn.load(new File("model.txt")); // load from a file, don't need to do training if you do this

        List<Node> nodes_list  = storage.getAllNodes();

        Matrix[] input_data = new Matrix[nodes_list.size()];

        Matrix[] output_data = new Matrix[nodes_list.size()];

        for(int i = 0; i < nodes_list.size(); i++){ // TODO make all these values end in d somehow...
                input_data[i] = new Matrix(nodes_list.get(i).getXCoord(), nodes_list.get(i).getYCoord()); //2D
                output_data[i] = new Matrix(nodes_list.get(i).getXCoord3D(), nodes_list.get(i).getYCoord3D()); //3D
        }


        Matrix[] inputData = { // 2D coordinates
                new Matrix(2150d, 1025d),
                new Matrix(2279d, 786d),
                new Matrix(2166d, 1039d)
        };

        Matrix[] outputData = { // 3D coordinates
                new Matrix(2014d, 1423d),
                new Matrix(2193d, 1323d),
                new Matrix(2013d, 1435d)
        };

        nn.fit(inputData, outputData, 0.0001);

        Matrix prediction = nn.predict(2945, 775);
        System.out.println(prediction);

        // Save to a file, do this at the end of training and copy that file into your top level project (or where ever the csv folder is)
        nn.save(new File("model.txt"));
    }

}
