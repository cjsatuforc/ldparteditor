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
package org.nschmidt.ldparteditor.dialogs.smooth;

import java.math.BigDecimal;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.nschmidt.ldparteditor.widgets.NButton;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.i18n.I18n;
import org.nschmidt.ldparteditor.widgets.BigDecimalSpinner;
import org.nschmidt.ldparteditor.widgets.IntegerSpinner;

/**
 * The scale dialog
 * <p>
 * Note: This class should not be instantiated, it defines the gui layout and no
 * business logic.
 *
 * @author nils
 *
 */
class SmoothDesign extends Dialog {


    final NButton[] cb_Xaxis = new NButton[1];
    final NButton[] cb_Yaxis = new NButton[1];
    final NButton[] cb_Zaxis = new NButton[1];
    final IntegerSpinner[] spn_pX = new IntegerSpinner[1];
    final BigDecimalSpinner[] spn_pY = new BigDecimalSpinner[1];
    
    private final String NUMBER_FORMAT = View.NUMBER_FORMAT4F;

    // Use final only for subclass/listener references!

    SmoothDesign(Shell parentShell) {
        super(parentShell);
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
        gridLayout.verticalSpacing = 5;
        gridLayout.horizontalSpacing = 10;

        Label lbl_specify = new Label(cmp_container, SWT.NONE);
        lbl_specify.setText(I18n.SMOOTH_Title);

        Label lbl_separator = new Label(cmp_container, SWT.SEPARATOR | SWT.HORIZONTAL);
        lbl_separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        Label lbl_preview = new Label(cmp_container, SWT.NONE);
        lbl_preview.setText("This dialog supports a realtime preview of the new mesh."); //$NON-NLS-1$ FIXME !i18n!
        
        {
            Composite cmp_txt = new Composite(cmp_container, SWT.NONE);
            cmp_txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            cmp_txt.setLayout(new GridLayout(3, true));

            NButton cb_Xaxis = new NButton(cmp_txt, SWT.CHECK);
            this.cb_Xaxis[0] = cb_Xaxis;
            cb_Xaxis.setText(I18n.COORDINATESDIALOG_X);
            cb_Xaxis.setSelection(true);
            
            NButton cb_Yaxis = new NButton(cmp_txt, SWT.CHECK);
            this.cb_Yaxis[0] = cb_Yaxis;
            cb_Yaxis.setText(I18n.COORDINATESDIALOG_Y);
            cb_Yaxis.setSelection(true);
            
            NButton cb_Zaxis = new NButton(cmp_txt, SWT.CHECK);
            this.cb_Zaxis[0] = cb_Zaxis;
            cb_Zaxis.setText(I18n.COORDINATESDIALOG_Z);
            cb_Zaxis.setSelection(true);
        }
        
        Label lbl_iter = new Label(cmp_container, SWT.NONE);
        lbl_iter.setText("Iterations:"); //$NON-NLS-1$ FIXME !i18n!      
        
        {
            Composite cmp_txt = new Composite(cmp_container, SWT.NONE);
            cmp_txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            cmp_txt.setLayout(new GridLayout(1, true));

            IntegerSpinner spn_pX = new IntegerSpinner(cmp_txt, SWT.NONE);
            this.spn_pX[0] = spn_pX;
            spn_pX.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            spn_pX.setMaximum(9999);
            spn_pX.setMinimum(1);
            spn_pX.setValue(1);
        }

        Label lbl_factor = new Label(cmp_container, SWT.NONE);
        lbl_factor.setText("Factor:"); //$NON-NLS-1$ FIXME !i18n!
      
        {
            Composite cmp_txt = new Composite(cmp_container, SWT.NONE);
            cmp_txt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            cmp_txt.setLayout(new GridLayout(1, true));

            BigDecimalSpinner spn_pY = new BigDecimalSpinner(cmp_txt, SWT.NONE, NUMBER_FORMAT);
            this.spn_pY[0] = spn_pY;
            spn_pY.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            spn_pY.setMaximum(new BigDecimal(1));
            spn_pY.setMinimum(new BigDecimal(-1));
            spn_pY.setValue(new BigDecimal(1));
        }

        cmp_container.pack();
        return cmp_container;
    }

    /**
     * Create contents of the button bar.
     *
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, I18n.DIALOG_OK, false);
        createButton(parent, IDialogConstants.CANCEL_ID, I18n.DIALOG_Cancel, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return super.getInitialSize();
    }

}
