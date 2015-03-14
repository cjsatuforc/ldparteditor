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
package org.nschmidt.ldparteditor.composites.primitive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.SWTResourceManager;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.nschmidt.ldparteditor.data.BFC;
import org.nschmidt.ldparteditor.data.DatFile;
import org.nschmidt.ldparteditor.data.GColour;
import org.nschmidt.ldparteditor.data.GData;
import org.nschmidt.ldparteditor.data.GData1;
import org.nschmidt.ldparteditor.data.GData2;
import org.nschmidt.ldparteditor.data.GData3;
import org.nschmidt.ldparteditor.data.GData4;
import org.nschmidt.ldparteditor.data.GData5;
import org.nschmidt.ldparteditor.data.GDataBFC;
import org.nschmidt.ldparteditor.data.GDataCSG;
import org.nschmidt.ldparteditor.data.GDataInit;
import org.nschmidt.ldparteditor.data.Vertex;
import org.nschmidt.ldparteditor.dnd.MyDummyTransfer2;
import org.nschmidt.ldparteditor.dnd.MyDummyType2;
import org.nschmidt.ldparteditor.enums.MouseButton;
import org.nschmidt.ldparteditor.enums.Threshold;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.i18n.I18n;
import org.nschmidt.ldparteditor.logger.NLogger;
import org.nschmidt.ldparteditor.opengl.OpenGLRenderer;
import org.nschmidt.ldparteditor.opengl.OpenGLRendererPrimitives;
import org.nschmidt.ldparteditor.project.Project;
import org.nschmidt.ldparteditor.shells.editor3d.Editor3DWindow;
import org.nschmidt.ldparteditor.text.LDParsingException;
import org.nschmidt.ldparteditor.text.StringHelper;
import org.nschmidt.ldparteditor.text.UTF8BufferedReader;
import org.nschmidt.ldparteditor.workbench.WorkbenchManager;

public class CompositePrimitive extends Composite {

    /** The {@linkplain OpenGLRenderer} instance */
    private final OpenGLRendererPrimitives openGL = new OpenGLRendererPrimitives(this);

    /** the {@linkplain GLCanvas} */
    final GLCanvas canvas;

    /** The view zoom level */
    private float zoom = 0.0039810717f;
    private float zoom_exponent = 6f; // Start with 0.4% zoom

    /** The transformation matrix of the view */
    private final Matrix4f viewport_matrix = new Matrix4f();
    /** The inverse transformation matrix of the view */
    private Matrix4f viewport_matrix_inv = new Matrix4f();
    /** The translation matrix of the view */
    private final Matrix4f viewport_translation = new Matrix4f();

    private final Matrix4f viewport_rotation = new Matrix4f();

    private ArrayList<Primitive> primitives = new ArrayList<Primitive>();
    private Primitive selectedPrimitive = null;
    private Primitive focusedPrimitive = null;
    private int mouse_button_pressed;
    private final Vector2f old_mouse_position = new Vector2f();
    /** The old translation matrix of the view [NOT PUBLIC YET] */
    private final Matrix4f old_viewport_translation = new Matrix4f();
    private final Matrix4f old_viewport_rotation = new Matrix4f();

    /** Resolution of the viewport at n% zoom */
    private float viewport_pixel_per_ldu;

    private AtomicBoolean dontRefresh = new AtomicBoolean(false);
    private AtomicBoolean hasDrawn = new AtomicBoolean(false);
    private AtomicBoolean stopDraw = new AtomicBoolean(true);

    private float maxY = 0f;

    public CompositePrimitive(Composite parent) {
        super(parent, I18n.I18N_NON_BIDIRECT() | SWT.BORDER);
        // TODO Auto-generated constructor stub

        this.viewport_pixel_per_ldu = this.zoom * View.PIXEL_PER_LDU;

        this.setLayout(new FillLayout());
        GLData data = new GLData();
        data.doubleBuffer = true;
        data.depthSize = 24;
        data.alphaSize = 8;
        data.blueSize = 8;
        data.redSize = 8;
        data.greenSize = 8;
        data.stencilSize = 8;
        canvas = new GLCanvas(this, I18n.I18N_NON_BIDIRECT(), data);
        canvas.setCurrent();
        canvas.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_HAND));
        canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        this.setBackgroundMode(SWT.INHERIT_FORCE);

        Transfer[] types = new Transfer[] { MyDummyTransfer2.getInstance() };
        int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

        final DragSource source = new DragSource(canvas, operations);
        source.setTransfer(types);
        source.addDragListener(new DragSourceListener() {
            @Override
            public void dragStart(DragSourceEvent event) {
                event.doit = true;
            }

            @Override
            public void dragSetData(DragSourceEvent event) {
                event.data = new MyDummyType2();
            }

            @Override
            public void dragFinished(DragSourceEvent event) {

            }
        });

        try {
            GLContext.useContext(canvas);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        // MARK Resize
        canvas.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event) {
                canvas.setCurrent();
                try {
                    GLContext.useContext(canvas);
                } catch (LWJGLException e) {
                    e.printStackTrace();
                }
                Display.getCurrent().timerExec(500, new Runnable() {
                    @Override
                    public void run() {
                        openGL.drawScene(-1, -1);
                    }
                });
            }
        });

        canvas.addListener(SWT.MouseDown, new Listener() {
            @Override
            // MARK MouseDown
            public void handleEvent(Event event) {
                mouse_button_pressed = event.button;
                old_mouse_position.set(event.x, event.y);
                switch (event.button) {
                case MouseButton.LEFT:
                    setSelectedPrimitive(getFocusedPrimitive());
                    break;
                case MouseButton.MIDDLE:
                    Matrix4f.load(getRotation(), old_viewport_rotation);
                    break;
                case MouseButton.RIGHT:
                    Matrix4f.load(getTranslation(), old_viewport_translation);
                    break;
                default:
                }
                openGL.drawScene(event.x, event.y);
            }
        });

        canvas.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            // MARK MouseDown
            public void handleEvent(Event event) {
                mouse_button_pressed = event.button;
                old_mouse_position.set(event.x, event.y);
                switch (event.button) {
                case MouseButton.LEFT:
                    setSelectedPrimitive(getFocusedPrimitive());
                    getSelectedPrimitive().toggle();
                    break;
                case MouseButton.MIDDLE:
                    break;
                case MouseButton.RIGHT:
                    Matrix4f.load(getTranslation(), old_viewport_translation);
                    break;
                default:
                }
                openGL.drawScene(event.x, event.y);
            }
        });

        canvas.addListener(SWT.MouseMove, new Listener() {
            @Override
            // MARK MouseMove
            public void handleEvent(Event event) {
                dontRefresh.set(true);
                switch (mouse_button_pressed) {
                case MouseButton.LEFT:
                    break;
                case MouseButton.MIDDLE:
                    Point cSize = getSize();
                    float rx = 0;
                    float ry = 0;
                    rx = (event.x - old_mouse_position.x) / cSize.x * (float) Math.PI;
                    ry = (old_mouse_position.y - event.y) / cSize.y * (float) Math.PI;
                    Vector4f xAxis4f_rotation = new Vector4f(1.0f, 0, 0, 1.0f);
                    Vector4f yAxis4f_rotation = new Vector4f(0, 1.0f, 0, 1.0f);
                    Matrix4f ovr_inverse = Matrix4f.invert(old_viewport_rotation, null);
                    Matrix4f.transform(ovr_inverse, xAxis4f_rotation, xAxis4f_rotation);
                    Matrix4f.transform(ovr_inverse, yAxis4f_rotation, yAxis4f_rotation);
                    Vector3f xAxis3f_rotation = new Vector3f(xAxis4f_rotation.x, xAxis4f_rotation.y, xAxis4f_rotation.z);
                    Vector3f yAxis3f_rotation = new Vector3f(yAxis4f_rotation.x, yAxis4f_rotation.y, yAxis4f_rotation.z);
                    Matrix4f.rotate(rx, yAxis3f_rotation, old_viewport_rotation, viewport_rotation);
                    Matrix4f.rotate(ry, xAxis3f_rotation, viewport_rotation, viewport_rotation);
                    break;
                case MouseButton.RIGHT:
                    float dx = 0;
                    float dy = 0;
                    dx = (event.x - old_mouse_position.x) / viewport_pixel_per_ldu;
                    dy = (event.y - old_mouse_position.y) / viewport_pixel_per_ldu;
                    Vector4f xAxis4f_translation = new Vector4f(dx, 0, 0, 1.0f);
                    Vector4f yAxis4f_translation = new Vector4f(0, dy, 0, 1.0f);
                    Vector3f xAxis3 = new Vector3f(xAxis4f_translation.x, xAxis4f_translation.y, xAxis4f_translation.z);
                    Vector3f yAxis3 = new Vector3f(yAxis4f_translation.x, yAxis4f_translation.y, yAxis4f_translation.z);
                    Matrix4f.load(old_viewport_translation, viewport_translation);
                    Matrix4f.translate(xAxis3, old_viewport_translation, viewport_translation);
                    Matrix4f.translate(yAxis3, viewport_translation, viewport_translation);

                    // if (viewport_translation.m30 > 0f) viewport_translation.m30 = 0f;

                    viewport_translation.m30 = 0f;
                    if (viewport_translation.m31 > 0f) viewport_translation.m31 = 0f;
                    if (-viewport_translation.m31 > maxY) viewport_translation.m31 = -maxY;
                    break;
                default:
                }
                openGL.drawScene(event.x, event.y);
            }
        });

        canvas.addListener(SWT.MouseUp, new Listener() {
            @Override
            // MARK MouseUp
            public void handleEvent(Event event) {
                mouse_button_pressed = 0;
                switch (event.button) {
                case MouseButton.LEFT:
                    break;
                case MouseButton.MIDDLE:
                    break;
                case MouseButton.RIGHT:
                    break;
                default:
                }
            }
        });

        canvas.addListener(SWT.MouseVerticalWheel, new Listener() {
            @Override
            // MARK MouseVerticalWheel
            public void handleEvent(Event event) {

                if ((event.stateMask & SWT.CTRL) == SWT.CTRL) {
                    if (event.count < 0)
                        zoomIn();
                    else
                        zoomOut();
                } else {
                    float dy = 0;

                    Matrix4f.load(getTranslation(), old_viewport_translation);

                    if (event.count < 0) {
                        dy = -17f /  viewport_pixel_per_ldu;
                    } else {
                        dy = 17f /  viewport_pixel_per_ldu;
                    }

                    Vector4f yAxis4f_translation = new Vector4f(0, dy, 0, 1.0f);
                    Vector3f yAxis3 = new Vector3f(yAxis4f_translation.x, yAxis4f_translation.y, yAxis4f_translation.z);
                    Matrix4f.load(old_viewport_translation, viewport_translation);
                    Matrix4f.translate(yAxis3, old_viewport_translation, viewport_translation);

                    if (viewport_translation.m31 > 0f) viewport_translation.m31 = 0f;
                    if (-viewport_translation.m31 > maxY) viewport_translation.m31 = -maxY;
                }

                openGL.drawScene(event.x, event.y);


            }
        });

        openGL.init();
        Display.getCurrent().timerExec(3000, new Runnable() {
            @Override
            public void run() {
                try {
                    if (!stopDraw.get()) openGL.drawScene(-1, -1);
                    hasDrawn.set(true);
                    if (dontRefresh.get()) return;
                    if (stopDraw.get()) {
                        Display.getCurrent().timerExec(100, this);
                    } else {
                        Display.getCurrent().timerExec(3000, this);
                    }
                } catch (SWTException consumed) {}
            }
        });
    }
    // FIXME Needs implementation!

    public GLCanvas getCanvas() {
        return canvas;
    }

    /**
     * @return the view zoom level exponent
     */
    public float getZoom() {
        return zoom;
    }

    /**
     * Set the view zoom level exponent
     *
     * @param zoom
     *            value between -10.0 and 10.0
     */
    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    /**
     * @return The translation matrix of the view
     */
    public Matrix4f getTranslation() {
        return viewport_translation;
    }

    public Matrix4f getRotation() {
        return viewport_rotation;
    }

    /**
     * @return The transformation matrix of the viewport which was last drawn
     */
    public Matrix4f getViewport() {
        return viewport_matrix;
    }

    public Matrix4f getViewport_Inverse() {
        return viewport_matrix_inv;
    }

    /**
     * Sets the transformation matrix of the viewport
     *
     * @param matrix
     *            the matrix to set.
     */
    public void setViewport(Matrix4f matrix) {
        GData.CACHE_viewByProjection.clear();
        viewport_matrix.load(matrix);
        viewport_matrix_inv = (Matrix4f) matrix.invert();
    }

    public ArrayList<Primitive> getPrimitives() {
        return primitives;
    }

    public void setPrimitives(ArrayList<Primitive> primitives) {
        this.primitives = primitives;
    }

    /**
     * Zooming in
     */
    public void zoomIn() {
        float old = getZoom();
        zoom_exponent++;
        if (zoom_exponent > 20) {
            zoom_exponent = 20;
        }
        setZoom((float) Math.pow(10.0d, zoom_exponent / 10 - 3));
        this.viewport_pixel_per_ldu = this.zoom * View.PIXEL_PER_LDU;
        adjustTranslate(old, getZoom());
    }

    /**
     * Zooming out
     */
    public void zoomOut() {
        float old = getZoom();
        zoom_exponent--;
        if (zoom_exponent < 3) {
            zoom_exponent = 3;
        }
        setZoom((float) Math.pow(10.0d, zoom_exponent / 10 - 3));
        this.viewport_pixel_per_ldu = this.zoom * View.PIXEL_PER_LDU;
        adjustTranslate(old, getZoom());
    }

    private void adjustTranslate(float old, float zoom2) {
        float dx = 0;
        float dy = 0;
        dx = 0f / viewport_pixel_per_ldu;
        dy = 0f / viewport_pixel_per_ldu;
        Vector4f xAxis4f_translation = new Vector4f(dx, 0, 0, 1.0f);
        Vector4f yAxis4f_translation = new Vector4f(0, dy, 0, 1.0f);
        Vector3f xAxis3 = new Vector3f(xAxis4f_translation.x, xAxis4f_translation.y, xAxis4f_translation.z);
        Vector3f yAxis3 = new Vector3f(yAxis4f_translation.x, yAxis4f_translation.y, yAxis4f_translation.z);

        Matrix4f.load(old_viewport_translation, viewport_translation);
        Matrix4f.translate(xAxis3, old_viewport_translation, viewport_translation);
        Matrix4f.translate(yAxis3, viewport_translation, viewport_translation);

        viewport_translation.m30 = 0f;
        if (viewport_translation.m13 > 0f) viewport_translation.m13 = 0f;
        if (-viewport_translation.m31 > maxY) viewport_translation.m31 = -maxY;
    }

    public float getViewport_pixel_per_ldu() {
        return viewport_pixel_per_ldu;
    }

    public void setViewport_pixel_per_ldu(float viewport_pixel_per_ldu) {
        this.viewport_pixel_per_ldu = viewport_pixel_per_ldu;

    }

    public Primitive getSelectedPrimitive() {
        return selectedPrimitive;
    }

    public void setSelectedPrimitive(Primitive selectedPrimitive) {
        this.selectedPrimitive = selectedPrimitive;
        Editor3DWindow.getWindow().updatePrimitiveLabel(selectedPrimitive);
    }

    public Primitive getFocusedPrimitive() {
        return focusedPrimitive;
    }

    public void setFocusedPrimitive(Primitive focusedPrimitive) {
        this.focusedPrimitive = focusedPrimitive;
        Editor3DWindow.getWindow().updatePrimitiveLabel(focusedPrimitive);
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public void loadPrimitives() {
        try {
            new ProgressMonitorDialog(Editor3DWindow.getWindow().getShell()).run(true, true, new IRunnableWithProgress() {
                @Override
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    monitor.beginTask("Loading Primitives...", IProgressMonitor.UNKNOWN); //$NON-NLS-1$ I18N

                    // Pause primitive renderer
                    if (!stopDraw.get()) {
                        stopDraw.set(true);
                        hasDrawn.set(false);
                        while (!hasDrawn.get()) {
                            Thread.sleep(100);
                        }
                        hasDrawn.set(false);
                        while (!hasDrawn.get()) {
                            Thread.sleep(100);
                        }
                    }

                    setFocusedPrimitive(null);
                    setSelectedPrimitive(null);
                    primitives.clear();

                    ArrayList<String> searchPaths = new ArrayList<String>();
                    String ldrawPath = WorkbenchManager.getUserSettingState().getLdrawFolderPath();
                    if (ldrawPath != null) {
                        searchPaths.add(ldrawPath + File.separator + "p" + File.separator); //$NON-NLS-1$
                        searchPaths.add(ldrawPath + File.separator + "P" + File.separator); //$NON-NLS-1$
                        searchPaths.add(ldrawPath + File.separator + "p" + File.separator + "48" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$
                        searchPaths.add(ldrawPath + File.separator + "P" + File.separator + "48" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    String unofficial = WorkbenchManager.getUserSettingState().getLdrawFolderPath();
                    if (unofficial != null) {
                        searchPaths.add(unofficial + File.separator + "p" + File.separator); //$NON-NLS-1$
                        searchPaths.add(unofficial + File.separator + "P" + File.separator); //$NON-NLS-1$
                        searchPaths.add(unofficial + File.separator + "p" + File.separator + "48" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$
                        searchPaths.add(unofficial + File.separator + "P" + File.separator + "48" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                    String project = Project.getProjectPath();
                    if (project != null) {
                        searchPaths.add(project + File.separator + "p" + File.separator); //$NON-NLS-1$
                        searchPaths.add(project + File.separator + "P" + File.separator); //$NON-NLS-1$
                        searchPaths.add(project + File.separator + "p" + File.separator + "48" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$
                        searchPaths.add(project + File.separator + "P" + File.separator + "48" + File.separator); //$NON-NLS-1$ //$NON-NLS-2$
                    }

                    String hiResSuffix = File.separator + "48" + File.separator; //$NON-NLS-1$
                    try {

                        // Creating the categories / Rules
                        File rulesFile = new File("primitive_rules.txt"); //$NON-NLS-1$
                        if (rulesFile.exists()) {
                            UTF8BufferedReader reader = null;
                            try {
                                reader = new UTF8BufferedReader(rulesFile.getAbsolutePath());
                                String line ;
                                while ((line = reader.readLine()) != null) {
                                    NLogger.debug(getClass(), line);
                                }
                            } catch (LDParsingException e) {
                            } catch (FileNotFoundException e) {
                            } catch (UnsupportedEncodingException e) {
                            } finally {
                                try {
                                    if (reader != null)
                                        reader.close();
                                } catch (LDParsingException e1) {
                                }
                            }
                        }

                        HashMap<String, String> descriptionMap = new HashMap<String, String>();
                        HashMap<String, Primitive> primitiveMap = new HashMap<String, Primitive>();
                        HashMap<String, String> fileNameMap = new HashMap<String, String>();

                        for (String folderPath : searchPaths) {
                            File libFolder = new File(folderPath);
                            if (!libFolder.isDirectory()) continue;
                            UTF8BufferedReader reader = null;
                            for (File f : libFolder.listFiles()) {
                                final String fileName = f.getName();
                                if (f.isFile() && fileName.matches(".*.dat")) { //$NON-NLS-1$
                                    try {
                                        Primitive newPrimitive = new Primitive();
                                        ArrayList<GData> data = new ArrayList<GData>();
                                        final String path = f.getAbsolutePath();
                                        String description = ""; //$NON-NLS-1$
                                        reader = new UTF8BufferedReader(path);
                                        String line;
                                        line = reader.readLine();
                                        if (line != null) {
                                            data.add(new GDataInit(View.DUMMY_REFERENCE));
                                            GData gd = parseLine(line, 0, 0.5f, 0.5f, 0.5f, 1.1f, View.DUMMY_REFERENCE, View.ID, new HashSet<String>());
                                            if (line.trim().startsWith("0")) { //$NON-NLS-1$
                                                description = line.trim();
                                                if (description.length() > 2) {
                                                    description = description.substring(1).trim();
                                                }
                                            } else if (gd != null) {
                                                data.add(gd);
                                            }
                                            while ((line = reader.readLine()) != null) {
                                                gd = parseLine(line, 0, 0.5f, 0.5f, 0.5f, 1f, View.DUMMY_REFERENCE, View.ID, new HashSet<String>());
                                                if (gd != null && gd.type() != 0) data.add(gd);
                                            }
                                            newPrimitive.getGraphicalData().addAll(data);
                                            primitives.add(newPrimitive);
                                            descriptionMap.put(path, description);
                                            primitiveMap.put(path, newPrimitive);
                                            fileNameMap.put(path, fileName);
                                            if (folderPath.endsWith(hiResSuffix)) {
                                                newPrimitive.setName("48\\" + fileName); //$NON-NLS-1$
                                            } else {
                                                newPrimitive.setName(fileName);
                                            }
                                            newPrimitive.setDescription(description);
                                        }
                                    } catch (LDParsingException e) {
                                    } catch (FileNotFoundException e) {
                                    } catch (UnsupportedEncodingException e) {
                                    } finally {
                                        try {
                                            if (reader != null)
                                                reader.close();
                                        } catch (LDParsingException e1) {
                                        }
                                    }
                                }
                            }
                        }
                    } catch (SecurityException consumed) {

                    }
                    stopDraw.set(false);
                }
            });
        } catch (InvocationTargetException consumed) {
        } catch (InterruptedException consumed) {
        }
    }

    public static GData parseLine(String line, int depth, float r, float g, float b, float a, GData1 gData1, Matrix4f pMatrix, Set<String> alreadyParsed) {
        String[] data_segments = line.trim().split("\\s+"); //$NON-NLS-1$
        return parseLine(data_segments, line, depth, r, g, b, a, gData1, pMatrix, alreadyParsed);
    }

    // What follows now is a very minimalistic DAT file parser (<500LOC)

    private static GColour cValue = new GColour();
    private static final Vector3f start = new Vector3f();
    private static final Vector3f end = new Vector3f();
    private static final Vector3f vertexA = new Vector3f();
    private static final Vector3f vertexB = new Vector3f();
    private static final Vector3f vertexC = new Vector3f();
    private static final Vector3f vertexD = new Vector3f();
    private static final Vector3f controlI = new Vector3f();
    private static final Vector3f controlII = new Vector3f();

    public static GData parseLine(String[] data_segments, String line, int depth, float r, float g, float b, float a, GData1 parent, Matrix4f productMatrix, Set<String> alreadyParsed) {
        // Get the linetype
        int linetype = 0;
        char c;
        if (!(data_segments.length > 0 && data_segments[0].length() == 1 && Character.isDigit(c = data_segments[0].charAt(0)))) {
            return null;
        }
        linetype = Character.getNumericValue(c);
        // Parse the line according to its type
        switch (linetype) {
        case 0:
            return parse_Comment(line, data_segments, depth, r, g, b, a, parent, productMatrix, alreadyParsed);
        case 1:
            return parse_Reference(data_segments, depth, r, g, b, a, parent, productMatrix, alreadyParsed);
        case 2:
            return parse_Line(data_segments, r, g, b, a, parent);
        case 3:
            return parse_Triangle(data_segments, r, g, b, a, parent);
        case 4:
            return parse_Quad(data_segments, r, g, b, a, parent);
        case 5:
            return parse_Condline(data_segments, r, g, b, a, parent);
        }
        return null;
    }

    private static GColour validateColour(String arg, float r, float g, float b, float a) {
        int colourValue;
        try {
            colourValue = Integer.parseInt(arg);
            switch (colourValue) {
            case 16:
                cValue.set(16, r, g, b, a);
                break;
            case 24:
                cValue.set(24, View.line_Colour_r[0], View.line_Colour_g[0], View.line_Colour_b[0], 1f);
                break;
            default:
                if (View.hasLDConfigColour(colourValue)) {
                    GColour colour = View.getLDConfigColour(colourValue);
                    cValue.set(colour);
                } else {
                    return null;
                }
                break;
            }
        } catch (NumberFormatException nfe) {
            if (arg.length() == 9 && arg.substring(0, 3).equals("0x2")) { //$NON-NLS-1$
                cValue.setA(1f);
                try {
                    cValue.setR(Integer.parseInt(arg.substring(3, 5), 16) / 255f);
                    cValue.setG(Integer.parseInt(arg.substring(5, 7), 16) / 255f);
                    cValue.setB(Integer.parseInt(arg.substring(7, 9), 16) / 255f);
                } catch (NumberFormatException nfe2) {
                    return null;
                }
                cValue.setColourNumber(-1);
            } else {
                return null;
            }
        }
        return cValue;
    }

    private static GData parse_Comment(String line, String[] data_segments, int depth, float r, float g, float b, float a, GData1 parent, Matrix4f productMatrix, Set<String> alreadyParsed) {
        line = line.replaceAll("\\s+", " ").trim(); //$NON-NLS-1$ //$NON-NLS-2$
        if (line.startsWith("0 BFC ")) { //$NON-NLS-1$
            if (line.startsWith("INVERTNEXT", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.INVERTNEXT);
            } else if (line.startsWith("CERTIFY CCW", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CCW_CLIP);
            } else if (line.startsWith("CERTIFY CW", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CW_CLIP);
            } else if (line.startsWith("CERTIFY", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CCW_CLIP);
            } else if (line.startsWith("NOCERTIFY", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.NOCERTIFY);
            } else if (line.startsWith("CCW", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CCW);
            } else if (line.startsWith("CW", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CW);
            } else if (line.startsWith("NOCLIP", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.NOCLIP);
            } else if (line.startsWith("CLIP CCW", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CCW_CLIP);
            } else if (line.startsWith("CLIP CW", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CW_CLIP);
            } else if (line.startsWith("CCW CLIP", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CCW_CLIP);
            } else if (line.startsWith("CW CLIP", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CW_CLIP);
            } else if (line.startsWith("CLIP", 6)) { //$NON-NLS-1$
                return new GDataBFC(BFC.CLIP);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private static GData parse_Reference(String[] data_segments, int depth, float r, float g, float b, float a, GData1 parent, Matrix4f productMatrix, Set<String> alreadyParsed) {
        if (data_segments.length < 15) {
            return null;
        } else {
            GColour colour = validateColour(data_segments[1], r, g, b, a);
            if (colour == null)
                return null;
            Matrix4f tMatrix = new Matrix4f();
            float det = 0;
            try {
                tMatrix.m30 = Float.parseFloat(data_segments[2]);
                tMatrix.m31 = Float.parseFloat(data_segments[3]);
                tMatrix.m32 = Float.parseFloat(data_segments[4]);
                tMatrix.m00 = Float.parseFloat(data_segments[5]);
                tMatrix.m10 = Float.parseFloat(data_segments[6]);
                tMatrix.m20 = Float.parseFloat(data_segments[7]);
                tMatrix.m01 = Float.parseFloat(data_segments[8]);
                tMatrix.m11 = Float.parseFloat(data_segments[9]);
                tMatrix.m21 = Float.parseFloat(data_segments[10]);
                tMatrix.m02 = Float.parseFloat(data_segments[11]);
                tMatrix.m12 = Float.parseFloat(data_segments[12]);
                tMatrix.m22 = Float.parseFloat(data_segments[13]);
            } catch (NumberFormatException nfe) {
                return null;
            }
            tMatrix.m33 = 1f;
            det = tMatrix.determinant();
            if (Math.abs(det) < Threshold.singularity_determinant)
                return null;
            // [WARNING] Check file existance
            boolean fileExists = true;
            StringBuilder sb = new StringBuilder();
            for (int s = 14; s < data_segments.length - 1; s++) {
                sb.append(data_segments[s]);
                sb.append(" "); //$NON-NLS-1$
            }
            sb.append(data_segments[data_segments.length - 1]);
            String shortFilename = sb.toString();
            shortFilename = shortFilename.toLowerCase(Locale.ENGLISH);
            try {
                shortFilename = shortFilename.replaceAll("s\\\\", "S" + File.separator).replaceAll("\\\\", File.separator); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            } catch (Exception e) {
                // Workaround for windows OS / JVM BUG
                shortFilename = shortFilename.replace("s\\\\", "S" + File.separator).replace("\\\\", File.separator); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            if (alreadyParsed.contains(shortFilename)) {
                if (!View.DUMMY_REFERENCE.equals(parent))
                    parent.firstRef.setRecursive(true);
                return null;
            } else {
                alreadyParsed.add(shortFilename);
            }
            String shortFilename2 = shortFilename.startsWith("S" + File.separator) ? "s" + shortFilename.substring(1) : shortFilename; //$NON-NLS-1$ //$NON-NLS-2$
            File fileToOpen = null;
            String[] prefix = new String[]{WorkbenchManager.getUserSettingState().getLdrawFolderPath(), WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), Project.getProjectPath()};
            String[] middle = new String[]{File.separator + "PARTS", File.separator + "parts", File.separator + "P", File.separator + "p"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            String[] suffix = new String[]{File.separator + shortFilename, File.separator + shortFilename2};
            for (int a1 = 0; a1 < prefix.length; a1++) {
                String s1 = prefix[a1];
                for (int a2 = 0; a2 < middle.length; a2++) {
                    String s2 = middle[a2];
                    for (int a3 = 0; a3 < suffix.length; a3++) {
                        String s3 = suffix[a3];
                        File f = new File(s1 + s2 + s3);
                        fileExists = f.exists() && f.isFile();
                        if (fileExists) {
                            fileToOpen = f;
                            break;
                        }
                    }
                    if (fileExists) break;
                }
                if (fileExists) break;
            }

            LinkedList<String> lines = null;
            String absoluteFilename = null;
            // MARK Virtual file check for project files...
            boolean isVirtual = false;
            for (DatFile df : Project.getUnsavedFiles()) {
                String fn = df.getNewName();
                for (int a1 = 0; a1 < prefix.length; a1++) {
                    String s1 = prefix[a1];
                    for (int a2 = 0; a2 < middle.length; a2++) {
                        String s2 = middle[a2];
                        for (int a3 = 0; a3 < suffix.length; a3++) {
                            String s3 = suffix[a3];
                            if (fn.equals(s1 + s2 + s3)) {
                                lines = new LinkedList<String>();
                                lines.addAll(Arrays.asList(df.getText().split(StringHelper.getLineDelimiter())));
                                absoluteFilename = fn;
                                isVirtual = true;
                                break;
                            }
                        }
                        if (isVirtual) break;
                    }
                    if (isVirtual) break;
                }
                if (isVirtual) break;
            }
            if (isVirtual) {
                Matrix4f destMatrix = new Matrix4f();
                Matrix4f.mul(productMatrix, tMatrix, destMatrix);
                GDataCSG.forceRecompile();
                final GData1 result = new GData1(colour.getColourNumber(), colour.getR(), colour.getG(), colour.getB(), colour.getA(), tMatrix, lines, absoluteFilename, sb.toString(), depth, det < 0,
                        destMatrix, alreadyParsed, parent.firstRef);
                if (result != null && result.firstRef.isRecursive()) {
                    return null;
                }
                alreadyParsed.remove(shortFilename);
                return result;
            } else if (!fileExists) {
                return null;
            } else {
                absoluteFilename = fileToOpen.getAbsolutePath();
                UTF8BufferedReader reader;
                String line = null;
                lines = new LinkedList<String>();
                try {
                    reader = new UTF8BufferedReader(absoluteFilename);
                    while (true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        lines.add(line);
                    }
                    reader.close();
                } catch (FileNotFoundException e1) {
                    return null;
                } catch (LDParsingException e1) {
                    return null;
                } catch (UnsupportedEncodingException e1) {
                    return null;
                }
                Matrix4f destMatrix = new Matrix4f();
                Matrix4f.mul(productMatrix, tMatrix, destMatrix);
                GDataCSG.forceRecompile();
                final GData1 result = new GData1(colour.getColourNumber(), colour.getR(), colour.getG(), colour.getB(), colour.getA(), tMatrix, lines, absoluteFilename, sb.toString(), depth, det < 0,
                        destMatrix, alreadyParsed, parent.firstRef);
                alreadyParsed.remove(shortFilename);
                if (result != null && result.firstRef.isRecursive()) {
                    return null;
                }
                return result;
            }
        }
    }

    private static GData parse_Line(String[] data_segments, float r, float g, float b, float a, GData1 parent) {
        if (data_segments.length != 8) {
            return null;
        } else {
            GColour colour = validateColour(data_segments[1], r, g, b, a);
            if (colour == null)
                return null;
            try {
                start.setX(Float.parseFloat(data_segments[2]));
                start.setY(Float.parseFloat(data_segments[3]));
                start.setZ(Float.parseFloat(data_segments[4]));
                end.setX(Float.parseFloat(data_segments[5]));
                end.setY(Float.parseFloat(data_segments[6]));
                end.setZ(Float.parseFloat(data_segments[7]));
            } catch (NumberFormatException nfe) {
                return null;
            }
            if (Vector3f.sub(start, end, null).length() < Threshold.identical_vertex_distance.floatValue()) {
                return null;
            }
            return new GData2(new Vertex(start.x, start.y, start.z, true), new Vertex(end.x, end.y, end.z, true), colour, parent);
        }
    }

    private static GData parse_Triangle(String[] data_segments, float r, float g, float b, float a, GData1 parent) {
        if (data_segments.length != 11) {
            return null;
        } else {
            GColour colour = validateColour(data_segments[1], r, g, b, a);
            if (colour == null)
                return null;
            try {
                vertexA.setX(Float.parseFloat(data_segments[2]));
                vertexA.setY(Float.parseFloat(data_segments[3]));
                vertexA.setZ(Float.parseFloat(data_segments[4]));
                vertexB.setX(Float.parseFloat(data_segments[5]));
                vertexB.setY(Float.parseFloat(data_segments[6]));
                vertexB.setZ(Float.parseFloat(data_segments[7]));
                vertexC.setX(Float.parseFloat(data_segments[8]));
                vertexC.setY(Float.parseFloat(data_segments[9]));
                vertexC.setZ(Float.parseFloat(data_segments[10]));
            } catch (NumberFormatException nfe) {
                return null;
            }
            return new GData3(new Vertex(vertexA.x, vertexA.y, vertexA.z, true), new Vertex(vertexB.x, vertexB.y, vertexB.z, true), new Vertex(vertexC.x,
                    vertexC.y, vertexC.z, true), parent, colour);
        }
    }

    private static GData parse_Quad(String[] data_segments, float r, float g, float b, float a, GData1 parent) {
        if (data_segments.length != 14) {
            return null;
        } else {
            GColour colour = validateColour(data_segments[1], r, g, b, a);
            if (colour == null)
                return null;
            try {
                vertexA.setX(Float.parseFloat(data_segments[2]));
                vertexA.setY(Float.parseFloat(data_segments[3]));
                vertexA.setZ(Float.parseFloat(data_segments[4]));
                vertexB.setX(Float.parseFloat(data_segments[5]));
                vertexB.setY(Float.parseFloat(data_segments[6]));
                vertexB.setZ(Float.parseFloat(data_segments[7]));
                vertexC.setX(Float.parseFloat(data_segments[8]));
                vertexC.setY(Float.parseFloat(data_segments[9]));
                vertexC.setZ(Float.parseFloat(data_segments[10]));
                vertexD.setX(Float.parseFloat(data_segments[11]));
                vertexD.setY(Float.parseFloat(data_segments[12]));
                vertexD.setZ(Float.parseFloat(data_segments[13]));
            } catch (NumberFormatException nfe) {
                return null;
            }
            return new GData4(new Vertex(vertexA.x, vertexA.y, vertexA.z, true), new Vertex(vertexB.x, vertexB.y, vertexB.z, true), new Vertex(vertexC.x,
                    vertexC.y, vertexC.z, true), new Vertex(vertexD.x, vertexD.y, vertexD.z, true), parent, colour);
        }
    }

    private static GData parse_Condline(String[] data_segments, float r, float g, float b, float a, GData1 parent) {
        if (data_segments.length != 14) {
            return null;
        } else {
            GColour colour = validateColour(data_segments[1], r, g, b, a);
            if (colour == null)
                return null;
            try {
                start.setX(Float.parseFloat(data_segments[2]));
                start.setY(Float.parseFloat(data_segments[3]));
                start.setZ(Float.parseFloat(data_segments[4]));
                end.setX(Float.parseFloat(data_segments[5]));
                end.setY(Float.parseFloat(data_segments[6]));
                end.setZ(Float.parseFloat(data_segments[7]));
                controlI.setX(Float.parseFloat(data_segments[8]));
                controlI.setY(Float.parseFloat(data_segments[9]));
                controlI.setZ(Float.parseFloat(data_segments[10]));
                controlII.setX(Float.parseFloat(data_segments[11]));
                controlII.setY(Float.parseFloat(data_segments[12]));
                controlII.setZ(Float.parseFloat(data_segments[13]));
            } catch (NumberFormatException nfe) {
                return null;
            }
            final float epsilon = Threshold.identical_vertex_distance.floatValue();
            if (Vector3f.sub(start, end, null).length() < epsilon || Vector3f.sub(controlI, controlII, null).length() < epsilon) {
                return null;
            }
            return new GData5(new Vertex(start.x, start.y, start.z, true), new Vertex(end.x, end.y, end.z, true), new Vertex(controlI.x,
                    controlI.y, controlI.z, true), new Vertex(controlII.x, controlII.y, controlII.z, true), colour, parent);
        }
    }
}
