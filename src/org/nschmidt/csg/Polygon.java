/**
 * Polygon.java
 *
 * Copyright 2014-2014 Michael Hoffer <info@michaelhoffer.de>. All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY Michael Hoffer <info@michaelhoffer.de> "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL Michael Hoffer <info@michaelhoffer.de> OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of Michael Hoffer
 * <info@michaelhoffer.de>.
 */
package org.nschmidt.csg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.nschmidt.ldparteditor.data.DatFile;
import org.nschmidt.ldparteditor.data.GColour;
import org.nschmidt.ldparteditor.data.GColourIndex;
import org.nschmidt.ldparteditor.data.GData1;
import org.nschmidt.ldparteditor.data.GData3;
import org.nschmidt.ldparteditor.data.GDataCSG;
import org.nschmidt.ldparteditor.enums.View;

/**
 * Represents a convex polygon.
 *
 * Each convex polygon has a {@code shared} property, which is shared between
 * all polygons that are clones of each other or where split from the same
 * polygon. This can be used to define per-polygon properties (such as surface
 * color).
 */
public final class Polygon {

    /**
     * Polygon vertices
     */
    public final List<Vector3d> vertices;
    /**
     * Shared property (can be used for shared color etc.).
     */
    private final PropertyStorage shared;
    /**
     * The linked DatFile
     */
    final DatFile df;

    public PropertyStorage getShared() {
        return shared;
    }

    /**
     * Plane defined by this polygon.
     *
     * <b>Note:</b> uses first three vertices to define the plane.
     */
    public final Plane plane;

    /**
     * Constructor. Creates a new polygon that consists of the specified
     * vertices.
     *
     * <b>Note:</b> the vertices used to initialize a polygon must be coplanar
     * and form a convex loop.
     * @param vertices
     *            polygon vertices
     * @param shared
     *            shared property
     */
    public Polygon(DatFile df, List<Vector3d> vertices, PropertyStorage shared) {
        this.df = df;
        this.vertices = vertices;
        this.shared = shared;
        this.plane = Plane.createFromPoints(vertices.get(0), vertices.get(1), vertices.get(2));
    }

    /**
     * Constructor. Creates a new polygon that consists of the specified
     * vertices.
     *
     * <b>Note:</b> the vertices used to initialize a polygon must be coplanar
     * and form a convex loop.
     * @param vertices
     *            polygon vertices
     */
    public Polygon(DatFile df, List<Vector3d> vertices) {
        this.df = df;
        this.vertices = vertices;
        this.shared = new PropertyStorage();
        this.plane = Plane.createFromPoints(vertices.get(0), vertices.get(1), vertices.get(2));
    }

    /**
     * Constructor. Creates a new polygon that consists of the specified
     * vertices.
     *
     * <b>Note:</b> the vertices used to initialize a polygon must be coplanar
     * and form a convex loop.
     * @param vertices
     *            polygon vertices
     *
     */
    public Polygon(DatFile df, Vector3d... vertices) {
        this(df, Arrays.asList(vertices));
    }

    @Override
    public Polygon clone() {
        List<Vector3d> newVertices = new ArrayList<Vector3d>(vertices.size());
        for (Vector3d vertex : vertices) {
            newVertices.add(vertex.clone());
        }
        return new Polygon(df, newVertices, new PropertyStorage(shared));
    }

    /**
     * Flips this polygon.
     *
     * @return this polygon
     */
    public Polygon flip() {

        Collections.reverse(vertices);
        plane.flip();

        return this;
    }

    /**
     * Returns a flipped copy of this polygon.
     *
     * <b>Note:</b> this polygon is not modified.
     *
     * @return a flipped copy of this polygon
     */
    public Polygon flipped() {
        return clone().flip();
    }

    public HashMap<GData3, Integer> toLDrawTriangles(GData1 parent) {
        HashMap<GData3, Integer> result = new HashMap<GData3, Integer>();
        if (this.vertices.size() >= 3) {
            int dID = CSGPrimitive.id_counter.getAndIncrement();
            final GColour c16 = View.getLDConfigColour(16);
            for (int i = 0; i < this.vertices.size() - 2; i++) {
                org.nschmidt.ldparteditor.data.Vertex v1 = new org.nschmidt.ldparteditor.data.Vertex((float) this.vertices.get(0).x, (float) this.vertices.get(0).y,
                        (float) this.vertices.get(0).z);
                org.nschmidt.ldparteditor.data.Vertex v2 = new org.nschmidt.ldparteditor.data.Vertex((float) this.vertices.get(i + 1).x, (float) this.vertices.get(i + 1).y,
                        (float) this.vertices.get(i + 1).z);
                org.nschmidt.ldparteditor.data.Vertex v3 = new org.nschmidt.ldparteditor.data.Vertex((float) this.vertices.get(i + 2).x, (float) this.vertices.get(i + 2).y,
                        (float) this.vertices.get(i + 2).z);
                GColourIndex colour = null;
                if ((colour = (GColourIndex) this.shared.getFirstValue()) == null) {
                    result.put(new GData3(v1, v2, v3, parent, c16, true), dID);
                } else {
                    // result.put(new GData3(v1, v2, v3, parent, View.getLDConfigColour(colour.getIndex() % 16), true), colour.getIndex());
                    result.put(new GData3(v1, v2, v3, parent, colour.getColour(), true), colour.getIndex());
                }
            }
        }
        return result;
    }

    public HashMap<GData3, Integer> toLDrawTriangles2(GData1 parent) {
        HashMap<GData3, Integer> result = new HashMap<GData3, Integer>();
        final int size = this.vertices.size();
        if (size >= 3) {
            int dID = CSGPrimitive.id_counter.getAndIncrement();
            final GColour c16 = View.getLDConfigColour(16);
            double mx = 0.0;
            double my = 0.0;
            double mz = 0.0;
            for (int i = 0; i < size; i++) {
                mx = mx + this.vertices.get(i).x;
                my = my + this.vertices.get(i).y;
                mz = mz + this.vertices.get(i).z;
            }

            mx = mx / size;
            my = my / size;
            mz = mz / size;

            org.nschmidt.ldparteditor.data.Vertex v1 = new org.nschmidt.ldparteditor.data.Vertex((float) mx, (float) my, (float) mz);
            for (int i = 0; i < size; i++) {
                org.nschmidt.ldparteditor.data.Vertex v2 = new org.nschmidt.ldparteditor.data.Vertex((float) this.vertices.get(i).x, (float) this.vertices.get(i).y,
                        (float) this.vertices.get(i).z);
                org.nschmidt.ldparteditor.data.Vertex v3 = new org.nschmidt.ldparteditor.data.Vertex((float) this.vertices.get((i + 1) % size).x, (float) this.vertices.get((i + 1) % size).y,
                        (float) this.vertices.get((i + 1) % size).z);
                GColourIndex colour = null;
                if ((colour = (GColourIndex) this.shared.getFirstValue()) == null) {
                    result.put(new GData3(v1, v2, v3, parent, c16, true), dID);
                } else {
                    // result.put(new GData3(v1, v2, v3, parent, View.getLDConfigColour(colour.getIndex() % 16), true), colour.getIndex());
                    result.put(new GData3(v1, v2, v3, parent, colour.getColour(), true), colour.getIndex());
                }
            }
        }
        return result;
    }

    /**
     * Translates this polygon.
     *
     * @param v
     *            the vector that defines the translation
     * @return this polygon
     */
    public Polygon translate(Vector3d v) {
        for (Vector3d vertex : vertices) {
            vertex = vertex.plus(v);
        }
        return this;
    }

    /**
     * Returns a translated copy of this polygon.
     *
     * <b>Note:</b> this polygon is not modified
     *
     * @param v
     *            the vector that defines the translation
     *
     * @return a translated copy of this polygon
     */
    public Polygon translated(Vector3d v) {
        return clone().translate(v);
    }

    /**
     * Applies the specified transformation to this polygon.
     *
     * <b>Note:</b> if the applied transformation performs a mirror operation
     * the vertex order of this polygon is reversed.
     *
     * @param transform
     *            the transformation to apply
     *
     * @return this polygon
     */
    public Polygon transform(Transform transform) {

        for (Vector3d v : vertices) {
            transform.transform(v);
        }

        if (transform.isMirror()) {
            // the transformation includes mirroring. flip polygon
            flip();
        }
        return this;
    }

    /**
     * Returns a transformed copy of this polygon.
     *
     * <b>Note:</b> if the applied transformation performs a mirror operation
     * the vertex order of this polygon is reversed.
     *
     * <b>Note:</b> this polygon is not modified
     *
     * @param transform
     *            the transformation to apply
     * @return a transformed copy of this polygon
     */
    public Polygon transformed(Transform transform) {
        return clone().transform(transform);
    }

    public Polygon transformed(Transform transform, GColour c, int ID) {
        Polygon result = clone().transform(transform);
        GColourIndex colour = null;
        if ((colour = (GColourIndex) this.shared.getFirstValue()) != null) {
            GColour c2;
            if ((c2 = colour.getColour()) != null) {
                if (c2.getColourNumber() == 16) {
                    result.shared.set("colour", new GColourIndex(c.clone(), ID)); //$NON-NLS-1$ colour.getIndex()
                } else {
                    result.shared.set("colour", new GColourIndex(c2.clone(), ID)); //$NON-NLS-1$
                }
            }
        }
        return result;
    }

    /**
     * Creates a polygon from the specified point list.
     * @param points
     *            the points that define the polygon
     * @param shared
     *            shared property storage
     *
     * @return a polygon defined by the specified point list
     */
    public static Polygon fromPoints(GDataCSG csg, DatFile df, List<Vector3d> points, PropertyStorage shared) {
        return fromPoints(csg, df, points, shared, null);
    }

    /**
     * Creates a polygon from the specified point list.
     * @param points
     *            the points that define the polygon
     *
     * @return a polygon defined by the specified point list
     */
    public static Polygon fromPoints(GDataCSG csg, DatFile df, List<Vector3d> points) {
        return fromPoints(csg, df, points, new PropertyStorage(), null);
    }

    /**
     * Creates a polygon from the specified points.
     * @param points
     *            the points that define the polygon
     *
     * @return a polygon defined by the specified point list
     */
    public static Polygon fromPoints(GDataCSG csg, DatFile df, Vector3d... points) {
        return fromPoints(csg, df, Arrays.asList(points), new PropertyStorage(), null);
    }

    /**
     * Creates a polygon from the specified point list.
     * @param points
     *            the points that define the polygon
     * @param shared
     * @param plane
     *            may be null
     *
     * @return a polygon defined by the specified point list
     */
    private static Polygon fromPoints(GDataCSG csg, DatFile df, List<Vector3d> points, PropertyStorage shared, Plane plane) {

        List<Vector3d> vertices = new ArrayList<Vector3d>();

        for (Vector3d p : points) {
            vertices.add(p.clone());
        }

        return new Polygon(df, vertices, shared);
    }

    /**
     * Returns the bounds of this polygon.
     *
     * @return bouds of this polygon
     */
    public Bounds getBounds() {
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;

        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < vertices.size(); i++) {

            Vector3d vert = vertices.get(i);

            if (vert.x < minX) {
                minX = vert.x;
            }
            if (vert.y < minY) {
                minY = vert.y;
            }
            if (vert.z < minZ) {
                minZ = vert.z;
            }

            if (vert.x > maxX) {
                maxX = vert.x;
            }
            if (vert.y > maxY) {
                maxY = vert.y;
            }
            if (vert.z > maxZ) {
                maxZ = vert.z;
            }

        } // end for vertices

        return new Bounds(new Vector3d(minX, minY, minZ), new Vector3d(maxX, maxY, maxZ));
    }
}
