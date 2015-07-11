/* MIT - License

Copyright (c) 2012 - this year, Nils Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */
package org.nschmidt.ldparteditor.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import org.nschmidt.csg.CSG;
import org.nschmidt.csg.CSGCircle;
import org.nschmidt.csg.CSGCone;
import org.nschmidt.csg.CSGCube;
import org.nschmidt.csg.CSGCylinder;
import org.nschmidt.csg.CSGQuad;
import org.nschmidt.csg.CSGSphere;
import org.nschmidt.csg.Plane;
import org.nschmidt.ldparteditor.composites.Composite3D;
import org.nschmidt.ldparteditor.enums.MyLanguage;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.helpers.math.MathHelper;
import org.nschmidt.ldparteditor.helpers.math.Vector3d;
import org.nschmidt.ldparteditor.i18n.I18n;
import org.nschmidt.ldparteditor.text.DatParser;

/**
 * @author nils
 *
 */
public final class GDataCSG extends GData {

    final byte type;

    private final static HashMap<String, CSG> linkedCSG = new HashMap<String, CSG>();
    private static boolean deleteAndRecompile = true;

    private final static HashSet<GDataCSG> registeredData = new HashSet<GDataCSG>();
    private final static HashSet<GDataCSG> parsedData = new HashSet<GDataCSG>();

    private static int quality = 16;
    private int global_quality = 16;
    private double global_epsilon = 1e-6;
    private float global_epsilon_t_junction = 0.001f;

    private final String ref1;
    private final String ref2;
    private final String ref3;

    private CSG compiledCSG = null;
    private final GColour colour;
    final Matrix4f matrix;

    public static void forceRecompile() {
        registeredData.add(null);
        Plane.EPSILON = 1e-3;
    }

    public static void fullReset() {
        quality = 16;
        registeredData.clear();
        linkedCSG.clear();
        parsedData.clear();
        Plane.EPSILON = 1e-3;
    }

    public static void resetCSG() {
        quality = 16;
        HashSet<GDataCSG> ref = new HashSet<GDataCSG>(registeredData);
        ref.removeAll(parsedData);
        deleteAndRecompile = !ref.isEmpty();
        if (deleteAndRecompile) {
            registeredData.clear();
            registeredData.add(null);
            linkedCSG.clear();
        }
        parsedData.clear();
    }

    public byte getCSGtype() {
        return type;
    }

    // CASE 1 0 !LPE [CSG TAG] [ID] [COLOUR] [MATRIX] 17
    // CASE 2 0 !LPE [CSG TAG] [ID] [ID2] [ID3] 6
    // CASE 3 0 !LPE [CSG TAG] [ID] 4
    public GDataCSG(byte type, String csgLine, GData1 parent) {
        registeredData.add(this);
        String[] data_segments = csgLine.trim().split("\\s+"); //$NON-NLS-1$
        this.type = type;
        this.text = csgLine;
        switch (type) {
        case CSG.COMPILE:
            if (data_segments.length == 4) {
                ref1 = data_segments[3] + "#>" + parent.shortName; //$NON-NLS-1$
            } else {
                ref1 = null;
            }
            ref2 = null;
            ref3 = null;
            colour = null;
            matrix = null;
            break;
        case CSG.QUAD:
        case CSG.CIRCLE:
        case CSG.ELLIPSOID:
        case CSG.CUBOID:
        case CSG.CYLINDER:
        case CSG.CONE:
            if (data_segments.length == 17) {
                ref1 = data_segments[3] + "#>" + parent.shortName; //$NON-NLS-1$
                GColour c = DatParser.validateColour(data_segments[4], .5f, .5f, .5f, 1f);
                if (c != null) {
                    colour = c.clone();
                } else {
                    colour = null;
                }
                matrix = MathHelper.matrixFromStrings(data_segments[5], data_segments[6], data_segments[7], data_segments[8], data_segments[11], data_segments[14], data_segments[9],
                        data_segments[12], data_segments[15], data_segments[10], data_segments[13], data_segments[16]);
            } else {
                colour = null;
                matrix = null;
                ref1 = null;
            }
            ref2 = null;
            ref3 = null;
            break;
        case CSG.DIFFERENCE:
        case CSG.INTERSECTION:
        case CSG.UNION:
            if (data_segments.length == 6) {
                ref1 = data_segments[3] + "#>" + parent.shortName; //$NON-NLS-1$
                ref2 = data_segments[4] + "#>" + parent.shortName; //$NON-NLS-1$
                ref3 = data_segments[5] + "#>" + parent.shortName; //$NON-NLS-1$
            } else {
                ref1 = null;
                ref2 = null;
                ref3 = null;
            }
            colour = null;
            matrix = null;
            break;
        case CSG.QUALITY:
            if (data_segments.length == 4) {
                try {
                    int q = Integer.parseInt(data_segments[3]);
                    if (q > 0 && q < 49) {
                        global_quality = q;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                }
                ref1 = data_segments[3] + "#>" + parent.shortName; //$NON-NLS-1$
            } else {
                ref1 = null;
            }
            ref2 = null;
            ref3 = null;
            colour = null;
            matrix = null;
            break;
        case CSG.EPSILON:
            if (data_segments.length == 4) {
                try {
                    double q = Double.parseDouble(data_segments[3]);
                    if (q > 0d) {
                        global_epsilon = q;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                }
                ref1 = data_segments[3] + "#>" + parent.shortName; //$NON-NLS-1$
            } else {
                ref1 = null;
            }
            ref2 = null;
            ref3 = null;
            colour = null;
            matrix = null;
            break;
        case CSG.EPSILON_T_JUNCTION:
            if (data_segments.length == 4) {
                try {
                    float q = Float.parseFloat(data_segments[3]);
                    if (q > 0f) {
                        global_epsilon_t_junction = q;
                    } else {
                        throw new Exception();
                    }
                } catch (Exception e) {
                }
                ref1 = data_segments[3] + "#>" + parent.shortName; //$NON-NLS-1$
            } else {
                ref1 = null;
            }
            ref2 = null;
            ref3 = null;
            colour = null;
            matrix = null;
            break;
        default:
            ref1 = null;
            ref2 = null;
            ref3 = null;
            colour = null;
            matrix = null;
            break;
        }
    }

    private void drawAndParse(Composite3D c3d) {
        parsedData.add(this);
        if (deleteAndRecompile) {
            registeredData.remove(null);
            try {
                compiledCSG = null;
                registeredData.add(this);
                if (ref1 != null) {
                    switch (type) {
                    case CSG.QUAD:
                    case CSG.CIRCLE:
                    case CSG.ELLIPSOID:
                    case CSG.CUBOID:
                    case CSG.CYLINDER:
                    case CSG.CONE:
                        if (matrix != null) {
                            switch (type) {
                            case CSG.QUAD:
                                CSGQuad quad = new CSGQuad();
                                CSG csgQuad = quad.toCSG(colour).transformed(matrix);
                                linkedCSG.put(ref1, csgQuad);
                                break;
                            case CSG.CIRCLE:
                                CSGCircle circle = new CSGCircle(quality);
                                CSG csgCircle = circle.toCSG(colour).transformed(matrix);
                                linkedCSG.put(ref1, csgCircle);
                                break;
                            case CSG.ELLIPSOID:
                                CSGSphere sphere = new CSGSphere(quality, quality / 2);
                                CSG csgSphere = sphere.toCSG(colour).transformed(matrix);
                                linkedCSG.put(ref1, csgSphere);
                                break;
                            case CSG.CUBOID:
                                CSGCube cube = new CSGCube();
                                CSG csgCube = cube.toCSG(colour).transformed(matrix);
                                linkedCSG.put(ref1, csgCube);
                                break;
                            case CSG.CYLINDER:
                                CSGCylinder cylinder = new CSGCylinder(quality);
                                CSG csgCylinder = cylinder.toCSG(colour).transformed(matrix);
                                linkedCSG.put(ref1, csgCylinder);
                                break;
                            case CSG.CONE:
                                CSGCone cone = new CSGCone(quality);
                                CSG csgCone = cone.toCSG(colour).transformed(matrix);
                                linkedCSG.put(ref1, csgCone);
                                break;
                            default:
                                break;
                            }
                        }
                        break;
                    case CSG.COMPILE:
                        if (linkedCSG.containsKey(ref1)) {
                            compiledCSG = linkedCSG.get(ref1);
                            compiledCSG.compile();
                        } else {
                            compiledCSG = null;
                        }
                        break;
                    case CSG.DIFFERENCE:
                        if (linkedCSG.containsKey(ref1) && linkedCSG.containsKey(ref2)) {
                            linkedCSG.put(ref3, linkedCSG.get(ref1).difference(linkedCSG.get(ref2)));
                        }
                        break;
                    case CSG.INTERSECTION:
                        if (linkedCSG.containsKey(ref1) && linkedCSG.containsKey(ref2)) {
                            linkedCSG.put(ref3, linkedCSG.get(ref1).intersect(linkedCSG.get(ref2)));
                        }
                        break;
                    case CSG.UNION:
                        if (linkedCSG.containsKey(ref1) && linkedCSG.containsKey(ref2)) {
                            linkedCSG.put(ref3, linkedCSG.get(ref1).union(linkedCSG.get(ref2)));
                        }
                        break;
                    case CSG.QUALITY:
                        quality = global_quality;
                        break;
                    case CSG.EPSILON:
                        Plane.EPSILON = global_epsilon;
                        break;
                    case CSG.EPSILON_T_JUNCTION:
                        Plane.EPSILON_T_JUNCTION = global_epsilon_t_junction;
                        break;
                    default:
                        break;
                    }
                }
            } catch (StackOverflowError e) {
                registeredData.clear();
                linkedCSG.clear();
                parsedData.clear();
                deleteAndRecompile = false;
                registeredData.add(null);
                Plane.EPSILON = Plane.EPSILON * 10d;
            } catch (Exception e) {

            }
        }
        if (compiledCSG != null) {
            if (c3d.getRenderMode() != 5) {
                compiledCSG.draw(c3d);
            } else {
                compiledCSG.draw_textured(c3d);
            }
        }
    }

    @Override
    public void draw(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public void drawRandomColours(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public void drawBFC(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public void drawBFCuncertified(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public void drawBFC_backOnly(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public void drawBFC_Colour(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public void drawBFC_Textured(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public void drawWhileAddCondlines(Composite3D c3d) {
        drawAndParse(c3d);
    }

    @Override
    public int type() {
        return 8;
    }

    @Override
    String getNiceString() {
        return text;
    }

    @Override
    public String inlinedString(final byte bfc, final GColour colour) {
        switch (type) {
        case CSG.COMPILE:
            if (compiledCSG != null) {

                final StringBuilder sb = new StringBuilder();

                Object[] messageArguments = {getNiceString()};
                MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
                formatter.setLocale(MyLanguage.LOCALE);
                formatter.applyPattern(I18n.DATFILE_Inlined);

                sb.append(formatter.format(messageArguments) + "<br>"); //$NON-NLS-1$

                HashMap<GData3, Integer> result = compiledCSG.getResult();

                // FIXME Needs T-Junction Elimination / Mesh Reduction!


                // Create data array

                int vertexIDcounter = 0;
                int triangleIDcounter = 0;
                int triangleCount = result.size();
                int reserveCount = triangleCount * 2;
                int vertexCount = triangleCount * 3;

                float[][] vertices = new float[vertexCount][3];

                float[][] triangles = new float[reserveCount][9];

                HashSet<Integer> skip = new HashSet<Integer>();

                HashMap<Integer, HashSet<Integer>> adjacentTriangles = new HashMap<Integer, HashSet<Integer>>();

                for (GData3 g3 : result.keySet()) {

                    triangles[triangleIDcounter][3] = g3.colourNumber;
                    triangles[triangleIDcounter][4] = g3.r;
                    triangles[triangleIDcounter][5] = g3.g;
                    triangles[triangleIDcounter][6] = g3.b;
                    triangles[triangleIDcounter][7] = g3.a;
                    triangles[triangleIDcounter][8] = result.get(g3);

                    triangles[triangleIDcounter][0] = vertexIDcounter;
                    vertices[vertexIDcounter][0] = g3.x1;
                    vertices[vertexIDcounter][1] = g3.y1;
                    vertices[vertexIDcounter][2] = g3.z1;
                    vertexIDcounter++;
                    triangles[triangleIDcounter][1] = vertexIDcounter;
                    vertices[vertexIDcounter][0] = g3.x2;
                    vertices[vertexIDcounter][1] = g3.y2;
                    vertices[vertexIDcounter][2] = g3.z2;
                    vertexIDcounter++;
                    triangles[triangleIDcounter][2] = vertexIDcounter;
                    vertices[vertexIDcounter][0] = g3.x3;
                    vertices[vertexIDcounter][1] = g3.y3;
                    vertices[vertexIDcounter][2] = g3.z3;
                    vertexIDcounter++;
                    triangleIDcounter++;
                }

                result.clear();

                {

                    int mergeCount = 0;
                    int[][] vertexMerges = new int[vertexCount][2];

                    // Remove near vertices

                    for (int i = 0; i < vertexCount; i++) {
                        if (skip.contains(i)) continue;
                        for (int j = i + 1; j < vertexCount; j++) {
                            if (skip.contains(j)) continue;
                            if (0.1f >
                            Math.pow(vertices[i][0] - vertices[j][0], 2) +
                            Math.pow(vertices[i][1] - vertices[j][1], 2) +
                            Math.pow(vertices[i][2] - vertices[j][2], 2)) {
                                vertexMerges[mergeCount][0] = i;
                                vertexMerges[mergeCount][1] = j;
                                skip.add(j);
                                mergeCount++;
                            }
                        }
                    }

                    // Apply merges to triangles

                    for (int i = 0; i < triangleCount; i++) {
                        for (int j = 0; j < mergeCount; j++) {
                            for (int k = 0; k < 3; k++) {
                                if (triangles[i][k] == vertexMerges[j][1]) {
                                    triangles[i][k] = vertexMerges[j][0];
                                }
                            }
                        }
                    }

                }

                // Detect T-Junction Cases

                for (int v = 0; v < vertexCount; v++) {
                    if (skip.contains(v)) continue;

                    System.out.println(v + " / " + vertexCount); //$NON-NLS-1$

                    Vector4f vp = new Vector4f(vertices[v][0], vertices[v][1], vertices[v][2], 1f);
                    for (int t = 0; t < triangleCount; t++) {
                        if (triangles[t][0] != v && triangles[t][1] != v && triangles[t][2] != v) {

                            int junctionMode = 0;

                            Vector4f v1 = MathHelper.getNearestPointToLineSegment2(
                                    vertices[(int) triangles[t][0]][0],
                                    vertices[(int) triangles[t][0]][1],
                                    vertices[(int) triangles[t][0]][2],
                                    vertices[(int) triangles[t][1]][0],
                                    vertices[(int) triangles[t][1]][1],
                                    vertices[(int) triangles[t][1]][2],
                                    vertices[v][0],
                                    vertices[v][1],
                                    vertices[v][2]);
                            float d1 = Vector4f.sub(v1, vp, null).lengthSquared();

                            if (d1 < Plane.EPSILON_T_JUNCTION) {
                                junctionMode = 1;
                            } else {

                                Vector4f v2 = MathHelper.getNearestPointToLineSegment2(
                                        vertices[(int) triangles[t][1]][0],
                                        vertices[(int) triangles[t][1]][1],
                                        vertices[(int) triangles[t][1]][2],
                                        vertices[(int) triangles[t][2]][0],
                                        vertices[(int) triangles[t][2]][1],
                                        vertices[(int) triangles[t][2]][2],
                                        vertices[v][0],
                                        vertices[v][1],
                                        vertices[v][2]);
                                float d2 = Vector4f.sub(v2, vp, null).lengthSquared();

                                if (d2 < Plane.EPSILON_T_JUNCTION) {
                                    junctionMode = 2;
                                } else {

                                    Vector4f v3 = MathHelper.getNearestPointToLineSegment2(
                                            vertices[(int) triangles[t][2]][0],
                                            vertices[(int) triangles[t][2]][1],
                                            vertices[(int) triangles[t][2]][2],
                                            vertices[(int) triangles[t][0]][0],
                                            vertices[(int) triangles[t][0]][1],
                                            vertices[(int) triangles[t][0]][2],
                                            vertices[v][0],
                                            vertices[v][1],
                                            vertices[v][2]);
                                    float d3 = Vector4f.sub(v3, vp, null).lengthSquared();

                                    if (d3 < Plane.EPSILON_T_JUNCTION) {
                                        junctionMode = 3;
                                    }
                                }
                            }

                            if (junctionMode > 0) {
                                if (triangleCount + 1 > reserveCount) {

                                    reserveCount = reserveCount * 2;

                                    float[][] triangles2 = new float[reserveCount][8];

                                    for (int i = 0; i < triangleCount; i++) {
                                        for (int j = 0; j < 9; j++) {
                                            triangles2[i][j] = triangles[i][j];
                                        }
                                    }

                                    triangles = triangles2;

                                }

                                for (int j = 0; j < 9; j++) {
                                    triangles[triangleCount][j] = triangles[t][j];
                                }

                                switch (junctionMode) {
                                case 1:
                                    triangles[triangleCount][0] = triangles[t][0];
                                    triangles[triangleCount][1] = v;
                                    triangles[triangleCount][2] = triangles[t][2];
                                    triangles[t][0] = v;
                                    triangles[t][1] = triangles[t][1];
                                    triangles[t][2] = triangles[t][2];
                                    break;
                                case 2:
                                    triangles[triangleCount][0] = triangles[t][1];
                                    triangles[triangleCount][1] = v;
                                    triangles[triangleCount][2] = triangles[t][0];
                                    triangles[t][1] = triangles[t][2];
                                    triangles[t][2] = triangles[t][0];
                                    triangles[t][0] = v;
                                    break;
                                case 3:
                                    triangles[triangleCount][0] = triangles[t][2];
                                    triangles[triangleCount][1] = v;
                                    triangles[triangleCount][2] = triangles[t][1];
                                    triangles[t][2] = triangles[t][1];
                                    triangles[t][1] = triangles[t][0];
                                    triangles[t][0] = v;
                                    break;
                                }
                                triangleCount++;
                            }

                        }

                    }

                }

                // Create adjacency map

                for (int v = 0; v < vertexCount; v++) {
                    if (skip.contains(v)) continue;
                    System.out.println(v + " / " + vertexCount); //$NON-NLS-1$
                    HashSet<Integer> adjSet = new HashSet<Integer>();
                    adjacentTriangles.put(v, adjSet);
                    for (int t = 0; t < triangleCount; t++) {
                        if (triangles[t][0] == v || triangles[t][1] == v || triangles[t][2] == v) {
                            adjSet.add(t);
                        }
                    }
                }


                // Iterative Simplyfication
                boolean foundTJunction = false;

                boolean foundSolution = true;
                HashSet<Integer> planeCount = new HashSet<Integer>();
                HashSet<Integer> verticesAround = new HashSet<Integer>();

                boolean[] skipTriangle = new boolean[triangleCount];
                boolean[] skipVertex = new boolean[vertexCount];

                while (foundSolution) {
                    foundSolution = false;
                    for (int t = 0; t < triangleCount; t++) {
                        if (skipTriangle[t]) continue;
                        // NLogger.debug(getClass(), t + " / " + triangleCount); //$NON-NLS-1$

                        for (int e0 = 0; e0 < 6; e0++) {
                            int e2 = e0 < 3 ? (e0 + 1) % 3 : (e0 + 2) % 3;
                            int e1 = e0 > 2 ? e0 - 3 : e0;

                            // if (skipVertex[(int) triangles[t][e1]]) continue;

                            final int centerVertexID = (int) triangles[t][e1];

                            planeCount.clear();
                            for (Integer tri : adjacentTriangles.get(centerVertexID)) {
                                planeCount.add((int) triangles[tri][8]);
                            }

                            final int planes = planeCount.size();

                            if (planes > 0 && planes < 3) {
                                // NLogger.debug(getClass(), "Found possible match. Planes: " + planeCount.size()); //$NON-NLS-1$
                                verticesAround.clear();

                                boolean hasJunction = false;

                                {
                                    HashMap<Integer, Integer> allVertices = new HashMap<Integer, Integer>();

                                    for (Integer tri : adjacentTriangles.get(centerVertexID)) {
                                        verticesAround.add((int) triangles[tri][0]);
                                        verticesAround.add((int) triangles[tri][1]);
                                        verticesAround.add((int) triangles[tri][2]);

                                        int key1 = (int) triangles[tri][0];
                                        if (allVertices.containsKey(key1)) {
                                            allVertices.put(key1, 2);
                                        } else {
                                            allVertices.put(key1, 1);
                                        }

                                        int key2 = (int) triangles[tri][1];
                                        if (allVertices.containsKey(key2)) {
                                            allVertices.put(key2, 2);
                                        } else {
                                            allVertices.put(key2, 1);
                                        }

                                        int key3 = (int) triangles[tri][2];
                                        if (allVertices.containsKey(key3)) {
                                            allVertices.put(key3, 2);
                                        } else {
                                            allVertices.put(key3, 1);
                                        }
                                    }

                                    for (Integer key : allVertices.keySet()) {
                                        if (allVertices.get(key) < 2) {
                                            // hasJunction = true;
                                            foundTJunction = true;
                                            break;
                                        }
                                    }

                                    verticesAround.remove(centerVertexID);


                                }

                                if (hasJunction) {

                                    // NLogger.debug(getClass(), "Possible T-junction!"); //$NON-NLS-1$
                                    skipVertex[centerVertexID] = true;

                                } else {

                                    final int adjacentCount = verticesAround.size();

                                    if (adjacentCount > 15) {

                                        // NLogger.debug(getClass(), "Too much adjacent points (polar vertex)!"); //$NON-NLS-1$
                                        skipVertex[centerVertexID] = true;

                                    } else {

                                        int[] orderedVertices = new int[adjacentCount];

                                        // NLogger.debug(getClass(), adjacentCount  + " adjacent vertices."); //$NON-NLS-1$

                                        HashSet<Integer> commonTriangles = new HashSet<>();

                                        HashSet<Integer> connectedTriangles = adjacentTriangles.get(centerVertexID);

                                        commonTriangles.addAll(connectedTriangles);
                                        commonTriangles.retainAll(adjacentTriangles.get((int) triangles[t][e2]));

                                        if (commonTriangles.size() == 2) {

                                            // FIXME Create Fingerprint

                                            // 1. adjacentCount

                                            // 2. angles

                                            // 3. length


                                            Iterator<Integer> cit = commonTriangles.iterator();

                                            final int first = cit.next();

                                            final int last = cit.next();

                                            {
                                                orderedVertices[0] = (int) triangles[t][e2];

                                                HashSet<Integer> ind = new HashSet<Integer>();
                                                ind.add((int) triangles[first][0]);
                                                ind.add((int) triangles[first][1]);
                                                ind.add((int) triangles[first][2]);

                                                ind.remove(centerVertexID);
                                                ind.remove((int) triangles[t][e2]);

                                                if (ind.size() == 1) {
                                                    orderedVertices[1] = ind.iterator().next();

                                                    int i = 1;

                                                    ind.clear();
                                                    ind.addAll(connectedTriangles);
                                                    ind.remove(first);
                                                    ind.remove(last);
                                                    while (!ind.isEmpty()) {
                                                        int removeCandidate = -1;
                                                        for (Integer tri : ind) {

                                                            HashSet<Integer> ind2 = new HashSet<Integer>();
                                                            ind2.add((int) triangles[tri][0]);
                                                            ind2.add((int) triangles[tri][1]);
                                                            ind2.add((int) triangles[tri][2]);

                                                            ind2.remove(orderedVertices[i]);

                                                            if (ind2.size() == 2) {
                                                                ind2.remove(orderedVertices[i - 1]);
                                                                if (ind2.size() == 2) {
                                                                    ind2.remove(centerVertexID);
                                                                    if (ind2.size() == 1) {
                                                                        i++;
                                                                        orderedVertices[i] = ind2.iterator().next();
                                                                        removeCandidate = tri;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (removeCandidate > -1) {
                                                            ind.remove(removeCandidate);
                                                        } else {
                                                            break;
                                                        }
                                                    }

                                                    double[] length = new double[adjacentCount];

                                                    double[] angles = new double[adjacentCount];

                                                    double max = -1.0;
                                                    i = 0;
                                                    for (Integer v : orderedVertices) {
                                                        double dist = Math.sqrt(
                                                                Math.pow(vertices[v][0] - vertices[centerVertexID][0], 2.0) +
                                                                Math.pow(vertices[v][1] - vertices[centerVertexID][1], 2.0) +
                                                                Math.pow(vertices[v][2] - vertices[centerVertexID][2], 2.0));
                                                        if (dist > max) max = dist;
                                                        length[i] = dist;
                                                        i++;
                                                    }

                                                    for (i = 0; i < adjacentCount; i++) {
                                                        float x1 = vertices[orderedVertices[i]][0] - vertices[centerVertexID][0];
                                                        float y1 = vertices[orderedVertices[i]][1] - vertices[centerVertexID][1];
                                                        float z1 = vertices[orderedVertices[i]][2] - vertices[centerVertexID][2];
                                                        int next = (i + 1) % adjacentCount;
                                                        float x2 = vertices[orderedVertices[next]][0] - vertices[centerVertexID][0];
                                                        float y2 = vertices[orderedVertices[next]][1] - vertices[centerVertexID][1];
                                                        float z2 = vertices[orderedVertices[next]][2] - vertices[centerVertexID][2];

                                                        angles[i] = Math.ceil(Vector3d.angle(
                                                                new Vector3d(new BigDecimal(x1), new BigDecimal(y1), new BigDecimal(z1)),
                                                                new Vector3d(new BigDecimal(x2), new BigDecimal(y2), new BigDecimal(z2))));

                                                    }

                                                    for (i = 0; i < adjacentCount; i++) {
                                                        length[i] = Math.ceil(Math.abs(length[i] / max) * 100);
                                                    }

                                                    System.out.print(adjacentCount + " =>   "); //$NON-NLS-1$
                                                    for (i = 0; i < adjacentCount; i++) {
                                                        System.out.print(length[i] + " ");//$NON-NLS-1$
                                                    }
                                                    System.out.print(" ANGLE: "); //$NON-NLS-1$
                                                    for (i = 0; i < adjacentCount; i++) {
                                                        System.out.print(angles[i] + " ");//$NON-NLS-1$
                                                    }

                                                    System.out.println();

                                                }
                                            }


                                            //                                        if (planes == 2) {
                                            //
                                            //                                        } else {
                                            //
                                            //                                            // Remove merged triangles from adjacency list
                                            //
                                            //                                            for (Integer v : verticesAround) {
                                            //                                                adjacentTriangles.get(v).removeAll(commonTriangles);
                                            //                                            }
                                            //                                            connectedTriangles.removeAll(commonTriangles);
                                            //                                            adjacentTriangles.get((int) triangles[t][e2]).addAll(connectedTriangles);
                                            //
                                            //                                            for (Integer t2 : commonTriangles) {
                                            //                                                skipTriangle[t2] = true;
                                            //                                            }
                                            //
                                            //                                            for (Integer t2 : connectedTriangles) {
                                            //                                                if (triangles[t2][0] == triangles[t][e1]) triangles[t2][0] = triangles[t][e2];
                                            //                                                if (triangles[t2][1] == triangles[t][e1]) triangles[t2][1] = triangles[t][e2];
                                            //                                                if (triangles[t2][2] == triangles[t][e1]) triangles[t2][2] = triangles[t][e2];
                                            //                                            }
                                            //
                                            //                                            foundSolution = true;
                                            //
                                            //                                        }

                                        }
                                    }
                                }

                            } else {
                                skipVertex[(int) triangles[t][e1]] = true;
                            }

                        }

                    }
                }

                Matrix4f id = new Matrix4f();
                Matrix4f.setIdentity(id);
                GData1 parent = new GData1(-1, .5f, .5f, .5f, 1f, id, View.ACCURATE_ID, new ArrayList<String>(), null, null, 1, false, id, View.ACCURATE_ID, null, View.DUMMY_REFERENCE, true, false,
                        new HashSet<String>(), View.DUMMY_REFERENCE);

                for (int t = 0; t < triangleCount; t++) {
                    if (skipTriangle[t]) continue;
                    GColour col = new GColour((int) triangles[t][3], triangles[t][4], triangles[t][5], triangles[t][6], triangles[t][7]);
                    Vertex v1 = new Vertex(
                            vertices[(int) triangles[t][0]][0],
                            vertices[(int) triangles[t][0]][1],
                            vertices[(int) triangles[t][0]][2]);
                    Vertex v2 = new Vertex(
                            vertices[(int) triangles[t][1]][0],
                            vertices[(int) triangles[t][1]][1],
                            vertices[(int) triangles[t][1]][2]);
                    Vertex v3 = new Vertex(
                            vertices[(int) triangles[t][2]][0],
                            vertices[(int) triangles[t][2]][1],
                            vertices[(int) triangles[t][2]][2]);

                    result.put(new GData3(v1, v2, v3, parent, col), 0);
                }

                if (foundTJunction) {
                    sb.append(I18n.DATFILE_FoundTJunction + "<br>"); //$NON-NLS-1$
                }

                for (GData3 g3 : result.keySet()) {
                    StringBuilder lineBuilder3 = new StringBuilder();
                    lineBuilder3.append("3 "); //$NON-NLS-1$
                    if (g3.colourNumber == -1) {
                        lineBuilder3.append("0x2"); //$NON-NLS-1$
                        lineBuilder3.append(MathHelper.toHex((int) (255f * g3.r)).toUpperCase());
                        lineBuilder3.append(MathHelper.toHex((int) (255f * g3.g)).toUpperCase());
                        lineBuilder3.append(MathHelper.toHex((int) (255f * g3.b)).toUpperCase());
                    } else {
                        lineBuilder3.append(g3.colourNumber);
                    }
                    Vector4f g3_v1 = new Vector4f(g3.x1, g3.y1, g3.z1, 1f);
                    Vector4f g3_v2 = new Vector4f(g3.x2, g3.y2, g3.z2, 1f);
                    Vector4f g3_v3 = new Vector4f(g3.x3, g3.y3, g3.z3, 1f);
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v1.x / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v1.y / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v1.z / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v2.x / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v2.y / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v2.z / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v3.x / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v3.y / 1000f));
                    lineBuilder3.append(" "); //$NON-NLS-1$
                    lineBuilder3.append(floatToString(g3_v3.z / 1000f));
                    sb.append(lineBuilder3.toString() + "<br>"); //$NON-NLS-1$
                }

                return sb.toString();
            } else {
                return getNiceString();
            }
        default:
            return getNiceString();
        }
    }

    private String floatToString(float flt) {
        String result;
        if (flt == (int) flt) {
            result = String.format("%d", (int) flt); //$NON-NLS-1$
        } else {
            result = String.format("%s", flt); //$NON-NLS-1$
            if (result.equals("0.0"))result = "0"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (result.startsWith("-0.")) return "-" + result.substring(2); //$NON-NLS-1$ //$NON-NLS-2$
        if (result.startsWith("0.")) return result.substring(1); //$NON-NLS-1$
        return result;
    }

    @Override
    public String transformAndColourReplace(String colour2, Matrix matrix) {
        boolean notChoosen = true;
        String t = null;
        switch (type) {
        case CSG.QUAD:
            if (notChoosen) {
                t = " CSG_QUAD "; //$NON-NLS-1$
                notChoosen = false;
            }
        case CSG.CIRCLE:
            if (notChoosen) {
                t = " CSG_CIRCLE "; //$NON-NLS-1$
                notChoosen = false;
            }
        case CSG.ELLIPSOID:
            if (notChoosen) {
                t = " CSG_ELLIPSOID "; //$NON-NLS-1$
                notChoosen = false;
            }
        case CSG.CUBOID:
            if (notChoosen) {
                t = " CSG_CUBOID "; //$NON-NLS-1$
                notChoosen = false;
            }
        case CSG.CYLINDER:
            if (notChoosen) {
                t = " CSG_CYLINDER "; //$NON-NLS-1$
                notChoosen = false;
            }
        case CSG.CONE:
            if (notChoosen) {
                t = " CSG_CONE "; //$NON-NLS-1$
                notChoosen = false;
            }
            StringBuilder colourBuilder = new StringBuilder();
            if (colour == null) {
                colourBuilder.append(16);
            } else if (colour.getColourNumber() == -1) {
                colourBuilder.append("0x2"); //$NON-NLS-1$
                colourBuilder.append(MathHelper.toHex((int) (255f * colour.getR())).toUpperCase());
                colourBuilder.append(MathHelper.toHex((int) (255f * colour.getG())).toUpperCase());
                colourBuilder.append(MathHelper.toHex((int) (255f * colour.getB())).toUpperCase());
            } else {
                colourBuilder.append(colour.getColourNumber());
            }
            Matrix4f newMatrix = new Matrix4f(this.matrix);
            Matrix4f newMatrix2 = new Matrix4f(matrix.getMatrix4f());
            Matrix4f.transpose(newMatrix, newMatrix);
            newMatrix.m30 = newMatrix.m03;
            newMatrix.m31 = newMatrix.m13;
            newMatrix.m32 = newMatrix.m23;
            newMatrix.m03 = 0f;
            newMatrix.m13 = 0f;
            newMatrix.m23 = 0f;
            Matrix4f.mul(newMatrix2, newMatrix, newMatrix);
            String col = colourBuilder.toString();
            if (col.equals(colour2))
                col = "16"; //$NON-NLS-1$
            String tag = ref1.substring(0, ref1.lastIndexOf("#>")); //$NON-NLS-1$
            return "0 !LPE" + t + tag + " " + col + " " + MathHelper.matrixToString(newMatrix); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        default:
            return text;
        }
    }

    @Override
    public void getBFCorientationMap(HashMap<GData, Byte> map) {}
    @Override
    public void getBFCorientationMapNOCERTIFY(HashMap<GData, Byte> map) {}
    @Override
    public void getBFCorientationMapNOCLIP(HashMap<GData, Byte> map) {}
    @Override
    public void getVertexNormalMap(TreeMap<Vertex, float[]> vertexLinkedToNormalCACHE, HashMap<GData, float[]> dataLinkedToNormalCACHE, VertexManager vm) {}
    @Override
    public void getVertexNormalMapNOCERTIFY(TreeMap<Vertex, float[]> vertexLinkedToNormalCACHE, HashMap<GData, float[]> dataLinkedToNormalCACHE, VertexManager vm) {}
    @Override
    public void getVertexNormalMapNOCLIP(TreeMap<Vertex, float[]> vertexLinkedToNormalCACHE, HashMap<GData, float[]> dataLinkedToNormalCACHE, VertexManager vm) {}

}
