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
package org.nschmidt.ldparteditor.widgets;

import java.text.DecimalFormat;
import java.text.ParseException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.nschmidt.ldparteditor.enums.View;

/**
 * @author nils
 *
 */
public class IntegerSpinner extends Composite {

    private final Button[] btn_Up = new Button[1];
    private final Button[] btn_Down = new Button[1];
    private final Text[] txt_val = new Text[1];

    private int value;
    private int maximum;
    private int minimum;
    private ValueChangeAdapter myListener;

    private final IntegerSpinner me;

    /**
     * @param parent
     * @param style
     */
    public IntegerSpinner(final Composite parent, int style) {
        super(parent, style);
        me = this;
        GridLayout gl = new GridLayout(3, false);

        gl.marginBottom = 0;
        gl.marginHeight = 0;
        gl.marginLeft = 0;
        gl.marginRight = 0;
        gl.marginTop = 0;
        gl.marginWidth = 0;

        this.setLayout(gl);

        GridData gd1 = new GridData();
        gd1.grabExcessHorizontalSpace = true;
        gd1.horizontalAlignment = SWT.FILL;
        gd1.grabExcessVerticalSpace = true;
        gd1.verticalAlignment = SWT.FILL;

        GridData gd2 = new GridData();
        gd2.grabExcessVerticalSpace = true;
        gd2.verticalAlignment = SWT.FILL;

        GridData gd3 = new GridData();
        gd3.grabExcessVerticalSpace = true;
        gd3.verticalAlignment = SWT.FILL;

        Button dwn = new Button(this, SWT.ARROW | SWT.LEFT);
        this.btn_Down[0] = dwn;
        dwn.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
            }

            @Override
            public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
                setValue(value - 1);
            }

            @Override
            public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
            }
        });

        Text txt = new Text(this, SWT.BORDER);
        this.txt_val[0] = txt;
        txt.setLayoutData(gd1);
        txt.setText("0"); //$NON-NLS-1$
        txt.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseScrolled(MouseEvent e) {
                Composite p = parent;
                while (p != null) {
                    if (p.getHorizontalBar() != null && p.getHorizontalBar().isVisible() || p.getVerticalBar() != null && p.getVerticalBar().isVisible()) {
                        return;
                    }
                    p = p.getParent();
                }
                if (e.count > 0) {
                    setValue(value + 1);
                } else {
                    setValue(value - 1);
                }
            }
        });

        final int[] oldValue = new int[] { 0 };
        final boolean[] invalidInput = new boolean[] { false };
        txt.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (invalidInput[0]) {
                    invalidInput[0] = false;
                    return;
                }

                int caret = txt_val[0].getCaretPosition();

                DecimalFormat df = View.NUMBER_FORMAT0F;
                try {
                    Number val = df.parse(txt_val[0].getText());
                    value = val.intValue();
                    if (value > maximum || value < minimum) {
                        oldValue[0] = value;
                        value = Math.min(value, maximum);
                        value = Math.max(value, minimum);
                    }

                    if (myListener != null)
                        myListener.valueChanged(me);

                    if (oldValue[0] != value) {
                        oldValue[0] = value;
                        invalidInput[0] = true;
                        txt_val[0].setText(df.format(value));
                    }
                } catch (ParseException ex) {
                    if (!invalidInput[0]) {
                        invalidInput[0] = true;
                        txt_val[0].setText(df.format(value));
                        invalidInput[0] = false;
                    }
                }

                txt_val[0].setSelection(caret);
            }
        });

        Button up = new Button(this, SWT.ARROW | SWT.RIGHT);
        this.btn_Up[0] = up;
        up.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(org.eclipse.swt.events.MouseEvent e) {
            }

            @Override
            public void mouseDown(org.eclipse.swt.events.MouseEvent e) {
                setValue(value + 1);
            }

            @Override
            public void mouseDoubleClick(org.eclipse.swt.events.MouseEvent e) {
            }
        });

        this.layout();
        this.pack();
    }

    public void addValueChangeListener(ValueChangeAdapter vcl) {
        this.myListener = vcl;

    }

    public void setValue(int value) {
        this.value = Math.min(value, maximum);
        this.value = Math.max(this.value, minimum);
        DecimalFormat df = View.NUMBER_FORMAT0F;
        txt_val[0].setText(df.format(this.value));
        if (myListener != null)
            myListener.valueChanged(this);
    }

    public int getValue() {
        return value;
    }

    public void setMinimum(int min) {
        minimum = min;
    }

    public void setMaximum(int max) {
        maximum = max;
    }

    @Override
    public void setEnabled(boolean enabled) {
        btn_Down[0].setEnabled(enabled);
        btn_Up[0].setEnabled(enabled);
        txt_val[0].setEditable(enabled);
        txt_val[0].setEnabled(enabled);
        super.setEnabled(enabled);
    }
}