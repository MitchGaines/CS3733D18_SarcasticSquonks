package edu.wpi.cs3733d18.teamS.data;

import com.kylecorry.lann.NN;
import com.kylecorry.lann.PersistentMachineLearningAlgorithm;
import com.kylecorry.lann.activation.Linear;
import com.kylecorry.lann.activation.ReLU;
import com.kylecorry.matrix.Matrix;
import edu.wpi.cs3733d18.teamS.database.Storage;

import java.util.List;

public class Node3DPredictor implements Runnable {
    private Thread t;
    private Storage storage;
    private static PersistentMachineLearningAlgorithm nn = new NN.Builder()
            .addLayer(2, 8, new ReLU())
            .addLayer(8, 2, new Linear())
            .build();

    public Matrix getPrediction(int x, int y){
        return nn.predict((x + 10) / 5000.0, (y + 30) / 3400.0);
    }


    @Override
    public void run() {
        List<Node> nn_nodes_list = storage.getAllNodes();

        Matrix[] input_data = new Matrix[nn_nodes_list.size()];

        Matrix[] output_data = new Matrix[nn_nodes_list.size()];

        for (int i = 0; i < nn_nodes_list.size(); i++) {
            input_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord() / 5000.0, (double) nn_nodes_list.get(i).getYCoord() / 3400.0); //2D
            output_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord3D(), (double) nn_nodes_list.get(i).getYCoord3D()); //3D
        }

        nn.fit(input_data, output_data, 0.0001, 500);
    }

    public void start(Storage _storage){
        storage = _storage;

        if(t == null){
            t = new Thread(this, "Model Generator");
            t.start();
        }
    }
}
