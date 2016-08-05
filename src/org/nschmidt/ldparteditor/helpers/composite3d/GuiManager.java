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
package org.nschmidt.ldparteditor.helpers.composite3d;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.NoSuchElementException;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.nschmidt.ldparteditor.composites.Composite3D;
import org.nschmidt.ldparteditor.data.Vertex;
import org.nschmidt.ldparteditor.data.VertexManager;
import org.nschmidt.ldparteditor.enums.MyLanguage;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.i18n.I18n;
import org.nschmidt.ldparteditor.shells.editor3d.Editor3DWindow;

/**
 * Manages GUI Actions, which are triggered by the {@linkplain Composite3D} and
 * changing the {@linkplain Editor3DWindow}
 *
 * @author nils
 *
 */
public enum GuiManager {
    INSTANCE;

    private static DecimalFormat df = new java.text.DecimalFormat(View.NUMBER_FORMAT4F, new DecimalFormatSymbols(MyLanguage.LOCALE));
    private static DecimalFormat df2 = new java.text.DecimalFormat(View.NUMBER_FORMAT1F, new DecimalFormatSymbols(MyLanguage.LOCALE));

    /**
     * Updates the status text from {@linkplain Editor3DWindow}
     *
     * @param c3d
     *            the active {@linkplain Composite3D}
     */
    public static synchronized void updateStatus(Composite3D c3d) {
        final StringBuilder sb = new StringBuilder();
        if (c3d.isClassicPerspective()) {
            sb.append("["); //$NON-NLS-1$
            sb.append(c3d.getPerspectiveCalculator().getPerspectiveString(c3d.getPerspectiveIndex()));
            sb.append("] "); //$NON-NLS-1$
        }

        final VertexManager vm = c3d.getVertexManager();
        final Set<Vertex> vs;
        if ((vs = vm.getSelectedVertices()).size() == 1) {            
            try {
                Vertex v = vs.iterator().next();
                sb.append(" Vertex @  ["); //$NON-NLS-1$
                sb.append(df.format(v.X.multiply(View.unit_factor)));
                sb.append("; "); //$NON-NLS-1$
                sb.append(df.format(v.Y.multiply(View.unit_factor)));
                sb.append("; "); //$NON-NLS-1$
                sb.append(df.format(v.Z.multiply(View.unit_factor)));
                sb.append("]"); //$NON-NLS-1$
            } catch (NoSuchElementException consumed) {}
        }

        sb.append(" "); //$NON-NLS-1$
        sb.append(c3d.getLockableDatFileReference().getShortName());
        sb.append(", "); //$NON-NLS-1$
        sb.append(I18n.PERSPECTIVE_Zoom);
        sb.append(": "); //$NON-NLS-1$
        sb.append(df2.format(Math.round(c3d.getZoom() * 10000000) / 100f));
        sb.append("% ["); //$NON-NLS-1$
        BigDecimal[] cursor3D = c3d.getCursorSnapped3Dprecise();
        sb.append(df.format(cursor3D[0].multiply(View.unit_factor)));
        sb.append("; "); //$NON-NLS-1$
        sb.append(df.format(cursor3D[1].multiply(View.unit_factor)));
        sb.append("; "); //$NON-NLS-1$
        sb.append(df.format(cursor3D[2].multiply(View.unit_factor)));
        sb.append("] "); //$NON-NLS-1$

        if (Editor3DWindow.getWindow().isMovingAdjacentData()) {
            sb.append(I18n.E3D_AdjacentWarningStatus);
        }
        
        Editor3DWindow.getStatusLabel().setText(sb.toString());
        Editor3DWindow.getStatusLabel().setSize(Editor3DWindow.getStatusLabel().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        // TODO Linux only??? Editor3DWindow.getStatusLabel().update();
    }

    public static void updateStatus() {
        Editor3DWindow.getStatusLabel().setText(I18n.E3D_NoFileSelected);
        Editor3DWindow.getStatusLabel().setSize(Editor3DWindow.getStatusLabel().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        Editor3DWindow.getStatusLabel().update();
    }

}
