package edu.wpi.cs3733d18.teamS.data;

import com.kylecorry.lann.NN;
import com.kylecorry.lann.PersistentMachineLearningAlgorithm;
import com.kylecorry.lann.activation.Linear;
import com.kylecorry.lann.activation.ReLU;
import com.kylecorry.matrix.Matrix;
import edu.wpi.cs3733d18.teamS.database.Storage;
import pointConverter.TeamD.API.PointConverter;

import java.awt.*;
import java.util.List;

public class Node3DPredictor implements Runnable {
    private Thread t;
    private Storage storage;
    private static PersistentMachineLearningAlgorithm nn = new NN.Builder()
            .addLayer(2, 8, new ReLU())
            .addLayer(8, 2, new Linear())
            .build();

    public Matrix getNeuralNetPrediction(int x, int y){
        return nn.predict((x + 10) / 5000.0, (y + 30) / 3400.0);
    }

    public double[] getAffinePrediction(int x, int y, String floor){
        return PointConverter.convertTo3D(x, y, floor);
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


        PointConverter.addTransform("Lower Level Two", 4733, 1159, 1796, 1537, 1846, 3115, 3870, 2225, 1588, 1687, 1006, 2511);
        PointConverter.addTransform("Lower Level One", 4733, 1159, 1796, 1537, 1846, 3115, 3870, 2199, 1572, 1651, 1006, 2475);
        PointConverter.addTransform("First Floor", 990, 2843, 1486, 1241, 3386, 535, 593, 2123, 1459, 1396, 3092, 1517);
        PointConverter.addTransform("Second Floor", 981, 2514, 1761, 1498, 4833, 846, 730, 1920, 1534, 1558, 4039, 2052);
        PointConverter.addTransform("Third Floor", 4735, 1160, 1100, 1496, 1705, 3064, 3872, 2174, 1104, 1351, 1016, 2310);
    }

    public void start(Storage _storage){
        storage = _storage;

        if(t == null){
            t = new Thread(this, "Model Generator");
            t.start();
        }
    }
}
