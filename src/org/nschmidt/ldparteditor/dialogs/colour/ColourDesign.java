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
package org.nschmidt.ldparteditor.dialogs.colour;

import java.text.MessageFormat;
import java.util.TreeSet;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;
import org.nschmidt.ldparteditor.data.GColour;
import org.nschmidt.ldparteditor.enums.MyLanguage;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.i18n.I18n;
import org.nschmidt.ldparteditor.resources.ResourceManager;
import org.nschmidt.ldparteditor.widgets.NButton;

/**
 * This first dialog - shown on startup - asks for mandatory information about
 * the user.
 * <p>
 * Note: This class should not be instantiated, it defines the gui layout and no
 * business logic.
 *
 * @author nils
 *
 */
class ColourDesign extends Dialog {

    final GColour[] refCol;
    final ColourDesign me;
    final boolean randomColours;

    // Use final only for subclass/listener references!
    final NButton[] btn_colourChoose = new NButton[1];
    final NButton[] btn_colourTable = new NButton[1];
    final NButton[] btn_randomColours = new NButton[1];

    ColourDesign(Shell parentShell, GColour[] refCol, final boolean randomColours) {
        super(parentShell);
        this.refCol = refCol;
        this.randomColours = randomColours;
        me = this;
    }

    /**
     * Create contents of the dialog.
     *
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite cmp_container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) cmp_container.getLayout();
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 10;

        Label lbl_welcome = new Label(cmp_container, SWT.NONE);
        lbl_welcome.setText(I18n.COLOURDIALOG_ColourTitle);

        {
            Label lbl_separator = new Label(cmp_container, SWT.SEPARATOR | SWT.HORIZONTAL);
            lbl_separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        }

        NButton btn_pickDirectColour = new NButton(cmp_container, SWT.NONE);
        btn_colourChoose[0] = btn_pickDirectColour;
        btn_pickDirectColour.setText(I18n.COLOURDIALOG_DirectColour);

        if (randomColours) {
            NButton btn_randomColour = new NButton(cmp_container, SWT.NONE);
            btn_randomColours[0] = btn_randomColour;
            btn_randomColour.setText(I18n.COLOURDIALOG_RandomColours);
            btn_randomColour.setImage(ResourceManager.getImage("icon8_randomColours.png")); //$NON-NLS-1$
        }

        {
            Label lbl_separator = new Label(cmp_container, SWT.SEPARATOR | SWT.HORIZONTAL);
            lbl_separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        }

        Label lbl_stdColour = new Label(cmp_container, SWT.NONE);
        lbl_stdColour.setText(I18n.COLOURDIALOG_StandardColours);

        NButton btn_showTable = new NButton(cmp_container, SWT.NONE);
        btn_colourTable[0] = btn_showTable;
        btn_showTable.setText(I18n.COLOURDIALOG_ShowColourTable);

        Composite cmpStdColours = new Composite(cmp_container, SWT.BORDER);
        GridLayout gridLayout2 = new GridLayout(17, true);
        gridLayout2.verticalSpacing = 0;
        gridLayout2.horizontalSpacing = 0;
        cmpStdColours.setLayout(gridLayout2);

        TreeSet<Integer> ldConfIndices = new TreeSet<Integer>(View.getColourMap().keySet());

        final int btnSize = (int) (lbl_stdColour.computeSize(SWT.DEFAULT, SWT.DEFAULT).y * 1.5f);

        for (Integer index : ldConfIndices) {
            final NButton btn_stdColour = new NButton(cmpStdColours, SWT.FLAT);
            btn_stdColour.setBounds(0, 0, btnSize, btnSize);
            GridData gd = new GridData();
            gd.grabExcessHorizontalSpace = true;
            gd.grabExcessVerticalSpace = true;
            gd.minimumHeight = btnSize;
            gd.minimumWidth = btnSize;
            gd.heightHint = btnSize;
            gd.widthHint = btnSize;
            btn_stdColour.setLayoutData(gd);
            Object[] messageArguments = {index, View.getLDConfigColourName(index)};
            MessageFormat formatter = new MessageFormat(""); //$NON-NLS-1$
            formatter.setLocale(MyLanguage.LOCALE);
            formatter.applyPattern(I18n.COLOURDIALOG_Colour);
            btn_stdColour.setToolTipText(formatter.format(messageArguments));
            final GColour gColour2 = View.getLDConfigColour(index);
            btn_stdColour.setData(gColour2);
            final Color col = SWTResourceManager.getColor((int) (gColour2.getR() * 255f), (int) (gColour2.getG() * 255f), (int) (gColour2.getB() * 255f));
            final int x = (int) (btnSize / 5f);
            final int y = (int) (btnSize / 5f);
            final int w = (int) (btnSize * (3f / 5f));
            final int h = (int) (btnSize * (3f / 5f));
            btn_stdColour.addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent e) {
                    e.gc.setBackground(col);
                    e.gc.fillRectangle(x, y, w, h);
                    if (gColour2.getA() >= .99f) {
                        e.gc.drawImage(ResourceManager.getImage("icon16_transparent.png", 0), 0, 0, 16, 16, x, y, w, h); //$NON-NLS-1$
                    } else {
                        e.gc.drawImage(ResourceManager.getImage("icon16_halftrans.png", 0), 0, 0, 16, 16, x, y, w, h); //$NON-NLS-1$
                    }
                }
            });
            btn_stdColour.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    refCol[0] = (GColour) btn_stdColour.getData();
                    me.close();
                }
            });
        }
        cmpStdColours.pack();
        cmp_container.pack();
        return cmp_container;
    }

    /**
     * Create contents of the button bar (no buttons)
     *
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return super.getInitialSize();
    }

}
