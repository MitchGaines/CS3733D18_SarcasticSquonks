package edu.wpi.cs3733d18.teamS.data;

import com.kylecorry.lann.NN;
import com.kylecorry.lann.PersistentMachineLearningAlgorithm;
import com.kylecorry.lann.activation.Linear;
import com.kylecorry.lann.activation.ReLU;
import com.kylecorry.matrix.Matrix;
import edu.wpi.cs3733d18.teamS.database.Storage;
import pointConverter.TeamD.API.PointConverter;

import java.util.List;

public class Node3DPredictor implements Runnable {
    private Thread t;
    private Storage storage;
    private static PersistentMachineLearningAlgorithm nn = new NN.Builder()
            .addLayer(2, 8, new ReLU())
            .addLayer(8, 2, new Linear())
            .build();
    private PointConverter affine_transform = new PointConverter();

    public Matrix getNeuralNetPrediction(int x, int y){
        return nn.predict((x + 10) / 5000.0, (y + 30) / 3400.0);
    }

    public double[] getAffinePrediction(int x, int y, String floor){
        return affine_transform.convertTo3D(x, y, floor);
    }

    @Override
    @SuppressWarnings("Duplicates")
    public void run() {
        List<Node> nn_nodes_list = storage.getAllNodes();

        double l2_affine_nodes_2d[] = new double[6];
        double l1_affine_nodes_2d[] = new double[6];
        double m1_affine_nodes_2d[] = new double[6];
        double m2_affine_nodes_2d[] = new double[6];
        double m3_affine_nodes_2d[] = new double[6];

        double l2_affine_nodes_3d[] = new double[6];
        double l1_affine_nodes_3d[] = new double[6];
        double m1_affine_nodes_3d[] = new double[6];
        double m2_affine_nodes_3d[] = new double[6];
        double m3_affine_nodes_3d[] = new double[6];

        Matrix[] input_data = new Matrix[nn_nodes_list.size()];

        Matrix[] output_data = new Matrix[nn_nodes_list.size()];

        for (int i = 0; i < nn_nodes_list.size(); i++) {
            input_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord() / 5000.0, (double) nn_nodes_list.get(i).getYCoord() / 3400.0); //2D
            output_data[i] = new Matrix((double) nn_nodes_list.get(i).getXCoord3D(), (double) nn_nodes_list.get(i).getYCoord3D()); //3D


            if(nn_nodes_list.get(i).getNodeFloor().equals("L2")){
                if(l2_affine_nodes_2d[0] == 0) {
                    l2_affine_nodes_2d[0] = nn_nodes_list.get(i).getXCoord();
                    l2_affine_nodes_2d[1] = nn_nodes_list.get(i).getYCoord();
                    l2_affine_nodes_3d[0] = nn_nodes_list.get(i).getXCoord3D();
                    l2_affine_nodes_3d[1] = nn_nodes_list.get(i).getYCoord3D();
                } else if(l2_affine_nodes_2d[2] == 0) {
                    l2_affine_nodes_2d[2] = nn_nodes_list.get(i).getXCoord();
                    l2_affine_nodes_2d[3] = nn_nodes_list.get(i).getYCoord();
                    l2_affine_nodes_3d[2] = nn_nodes_list.get(i).getXCoord3D();
                    l2_affine_nodes_3d[3] = nn_nodes_list.get(i).getYCoord3D();
                } else if(l2_affine_nodes_2d[4] == 0) {
                    l2_affine_nodes_2d[4] = nn_nodes_list.get(i).getXCoord();
                    l2_affine_nodes_2d[5] = nn_nodes_list.get(i).getYCoord();
                    l2_affine_nodes_3d[4] = nn_nodes_list.get(i).getXCoord3D();
                    l2_affine_nodes_3d[5] = nn_nodes_list.get(i).getYCoord3D();
                }
            } else if(nn_nodes_list.get(i).getNodeFloor().equals("L1")){
                if(l1_affine_nodes_2d[0] == 0) {
                    l1_affine_nodes_2d[0] = nn_nodes_list.get(i).getXCoord();
                    l1_affine_nodes_2d[1] = nn_nodes_list.get(i).getYCoord();
                    l1_affine_nodes_3d[0] = nn_nodes_list.get(i).getXCoord3D();
                    l1_affine_nodes_3d[1] = nn_nodes_list.get(i).getYCoord3D();
                } else if(l1_affine_nodes_2d[2] == 0) {
                    l1_affine_nodes_2d[2] = nn_nodes_list.get(i).getXCoord();
                    l1_affine_nodes_2d[3] = nn_nodes_list.get(i).getYCoord();
                    l1_affine_nodes_3d[2] = nn_nodes_list.get(i).getXCoord3D();
                    l1_affine_nodes_3d[3] = nn_nodes_list.get(i).getYCoord3D();
                } else if(l1_affine_nodes_2d[4] == 0) {
                    l1_affine_nodes_2d[4] = nn_nodes_list.get(i).getXCoord();
                    l1_affine_nodes_2d[5] = nn_nodes_list.get(i).getYCoord();
                    l1_affine_nodes_3d[4] = nn_nodes_list.get(i).getXCoord3D();
                    l1_affine_nodes_3d[5] = nn_nodes_list.get(i).getYCoord3D();
                }
            } else if(nn_nodes_list.get(i).getNodeFloor().equals("1")) {
                if (m1_affine_nodes_2d[0] == 0) {
                    m1_affine_nodes_2d[0] = nn_nodes_list.get(i).getXCoord();
                    m1_affine_nodes_2d[1] = nn_nodes_list.get(i).getYCoord();
                    m1_affine_nodes_3d[0] = nn_nodes_list.get(i).getXCoord3D();
                    m1_affine_nodes_3d[1] = nn_nodes_list.get(i).getYCoord3D();
                } else if (m1_affine_nodes_2d[2] == 0) {
                    m1_affine_nodes_2d[2] = nn_nodes_list.get(i).getXCoord();
                    m1_affine_nodes_2d[3] = nn_nodes_list.get(i).getYCoord();
                    m1_affine_nodes_3d[2] = nn_nodes_list.get(i).getXCoord3D();
                    m1_affine_nodes_3d[3] = nn_nodes_list.get(i).getYCoord3D();
                } else if (m1_affine_nodes_2d[4] == 0) {
                    m1_affine_nodes_2d[4] = nn_nodes_list.get(i).getXCoord();
                    m1_affine_nodes_2d[5] = nn_nodes_list.get(i).getYCoord();
                    m1_affine_nodes_3d[4] = nn_nodes_list.get(i).getXCoord3D();
                    m1_affine_nodes_3d[5] = nn_nodes_list.get(i).getYCoord3D();
                }
            } else if(nn_nodes_list.get(i).getNodeFloor().equals("2")) {
                if (m2_affine_nodes_2d[0] == 0) {
                    m2_affine_nodes_2d[0] = nn_nodes_list.get(i).getXCoord();
                    m2_affine_nodes_2d[1] = nn_nodes_list.get(i).getYCoord();
                    m2_affine_nodes_3d[0] = nn_nodes_list.get(i).getXCoord3D();
                    m2_affine_nodes_3d[1] = nn_nodes_list.get(i).getYCoord3D();
                } else if (m2_affine_nodes_2d[2] == 0) {
                    m2_affine_nodes_2d[2] = nn_nodes_list.get(i).getXCoord();
                    m2_affine_nodes_2d[3] = nn_nodes_list.get(i).getYCoord();
                    m2_affine_nodes_3d[2] = nn_nodes_list.get(i).getXCoord3D();
                    m2_affine_nodes_3d[3] = nn_nodes_list.get(i).getYCoord3D();
                } else if (m2_affine_nodes_2d[4] == 0) {
                    m2_affine_nodes_2d[4] = nn_nodes_list.get(i).getXCoord();
                    m2_affine_nodes_2d[5] = nn_nodes_list.get(i).getYCoord();
                    m2_affine_nodes_3d[4] = nn_nodes_list.get(i).getXCoord3D();
                    m2_affine_nodes_3d[5] = nn_nodes_list.get(i).getYCoord3D();
                }
            } else if(nn_nodes_list.get(i).getNodeFloor().equals("3")) {
                if (m3_affine_nodes_2d[0] == 0) {
                    m3_affine_nodes_2d[0] = nn_nodes_list.get(i).getXCoord();
                    m3_affine_nodes_2d[1] = nn_nodes_list.get(i).getYCoord();
                    m3_affine_nodes_3d[0] = nn_nodes_list.get(i).getXCoord3D();
                    m3_affine_nodes_3d[1] = nn_nodes_list.get(i).getYCoord3D();
                } else if (m3_affine_nodes_2d[2] == 0) {
                    m3_affine_nodes_2d[2] = nn_nodes_list.get(i).getXCoord();
                    m3_affine_nodes_2d[3] = nn_nodes_list.get(i).getYCoord();
                    m3_affine_nodes_3d[2] = nn_nodes_list.get(i).getXCoord3D();
                    m3_affine_nodes_3d[3] = nn_nodes_list.get(i).getYCoord3D();
                } else if (m3_affine_nodes_2d[4] == 0) {
                    m3_affine_nodes_2d[4] = nn_nodes_list.get(i).getXCoord();
                    m3_affine_nodes_2d[5] = nn_nodes_list.get(i).getYCoord();
                    m3_affine_nodes_3d[4] = nn_nodes_list.get(i).getXCoord3D();
                    m3_affine_nodes_3d[5] = nn_nodes_list.get(i).getYCoord3D();
                }
            }

        }

        nn.fit(input_data, output_data, 0.0001, 500);



        affine_transform.addTransform("L2"
                , l2_affine_nodes_2d[0], l2_affine_nodes_2d[1]
                , l2_affine_nodes_2d[2], l2_affine_nodes_2d[3]
                , l2_affine_nodes_2d[4], l2_affine_nodes_2d[5]
                , l2_affine_nodes_3d[0], l2_affine_nodes_3d[1]
                , l2_affine_nodes_3d[2], l2_affine_nodes_3d[3]
                , l2_affine_nodes_3d[4], l2_affine_nodes_3d[5]);
        affine_transform.addTransform("L1"
                , l1_affine_nodes_2d[0], l1_affine_nodes_2d[1]
                , l1_affine_nodes_2d[2], l1_affine_nodes_2d[3]
                , l1_affine_nodes_2d[4], l1_affine_nodes_2d[5]
                , l1_affine_nodes_3d[0], l1_affine_nodes_3d[1]
                , l1_affine_nodes_3d[2], l1_affine_nodes_3d[3]
                , l1_affine_nodes_3d[4], l1_affine_nodes_3d[5]);
        affine_transform.addTransform("1"
                , m1_affine_nodes_2d[0], m1_affine_nodes_2d[1]
                , m1_affine_nodes_2d[2], m1_affine_nodes_2d[3]
                , m1_affine_nodes_2d[4], m1_affine_nodes_2d[5]
                , m1_affine_nodes_3d[0], m1_affine_nodes_3d[1]
                , m1_affine_nodes_3d[2], m1_affine_nodes_3d[3]
                , m1_affine_nodes_3d[4], m1_affine_nodes_3d[5]);
        affine_transform.addTransform("2"
                , m2_affine_nodes_2d[0], m2_affine_nodes_2d[1]
                , m2_affine_nodes_2d[2], m2_affine_nodes_2d[3]
                , m2_affine_nodes_2d[4], m2_affine_nodes_2d[5]
                , m2_affine_nodes_3d[0], m2_affine_nodes_3d[1]
                , m2_affine_nodes_3d[2], m2_affine_nodes_3d[3]
                , m2_affine_nodes_3d[4], m2_affine_nodes_3d[5]);
        affine_transform.addTransform("3"
                , m3_affine_nodes_2d[0], m3_affine_nodes_2d[1]
                , m3_affine_nodes_2d[2], m3_affine_nodes_2d[3]
                , m3_affine_nodes_2d[4], m3_affine_nodes_2d[5]
                , m3_affine_nodes_3d[0], m3_affine_nodes_3d[1]
                , m3_affine_nodes_3d[2], m3_affine_nodes_3d[3]
                , m3_affine_nodes_3d[4], m3_affine_nodes_3d[5]);
    }

    public void start(Storage _storage){
        storage = _storage;

        if(t == null){
            t = new Thread(this, "Model Generator");
            t.start();
        }
    }
}
