package com.cgvsu.model;

import com.cgvsu.math.Vector3f;

import java.util.ArrayList;

import static com.cgvsu.math.Vector3f.*;

public class ModelUtils {

    public static void recalculateNormals(Model model) {
        model.normals.clear();
        for (int i = 0; i < model.vertices.size(); i++) {
            model.normals.add(calculateNormalForVertexInModel(model.vertices.get(i), model, i));
        }

    }

    protected static Vector3f calculateNormalForVertexInPolygon(final Vector3f vertex, final Polygon polygon, final Model model) {
        int vertexIndexInPolygon = -1;
        ArrayList<Integer> vertexIndices = polygon.getVertexIndices();
        int verticesCount = vertexIndices.size();
        for (int i = 0; i < vertexIndices.size(); i++) {
            if (model.vertices.get(vertexIndices.get(i)).equals(vertex)) {
                vertexIndexInPolygon = i;
                break;
            }
        }

        if (vertexIndexInPolygon == -1) {
            return null;
        }

        Vector3f vector1;
        Vector3f vector2;
        if (vertexIndexInPolygon == 0) {
            vector1 = fromTwoPoints(model.vertices.get(vertexIndices.get(0)), model.vertices.get(vertexIndices.get(1)));
            vector2 = fromTwoPoints(model.vertices.get(vertexIndices.get(0)), model.vertices.get(vertexIndices.get(verticesCount - 1)));
        } else if (vertexIndexInPolygon == (verticesCount - 1)) {
            vector1 = fromTwoPoints(model.vertices.get(vertexIndices.get(verticesCount - 1)), model.vertices.get(vertexIndices.get(0)));
            vector2 = fromTwoPoints(model.vertices.get(vertexIndices.get(verticesCount - 1)), model.vertices.get(vertexIndices.get(verticesCount - 2)));
        } else {
            vector1 = fromTwoPoints(model.vertices.get(vertexIndices.get(vertexIndexInPolygon)), model.vertices.get(vertexIndices.get(vertexIndexInPolygon - 1)));
            vector2 = fromTwoPoints(model.vertices.get(vertexIndices.get(vertexIndexInPolygon)), model.vertices.get(vertexIndices.get(vertexIndexInPolygon + 1)));
        }
        return calculateCrossProduct(vector1, vector2);
    }

    protected static Vector3f calculateNormalForVertexInModel(final Vector3f vertex, final Model model, final int vertexIndex) {
        ArrayList<Vector3f> saved = new ArrayList<>();
        for (Polygon polygon : model.polygons) {
            if (polygon.getVertexIndices().contains(vertexIndex)) {
                saved.add(calculateNormalForVertexInPolygon(vertex, polygon, model));
            }
        }
        return sum(saved).divide(saved.size());
    }
}
