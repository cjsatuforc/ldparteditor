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
package org.nschmidt.ldparteditor.helpers.math;

import java.math.BigDecimal;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Immutable, absolute precision matrix class
 *
 * @author nils
 *
 */
public class RationalMatrix {

    private final Rational[][] M = new Rational[4][4];

    public RationalMatrix(Matrix4f m) {
        Rational m00 = new Rational(new BigDecimal(Float.toString(m.m00)));
        Rational m01 = new Rational(new BigDecimal(Float.toString(m.m01)));
        Rational m02 = new Rational(new BigDecimal(Float.toString(m.m02)));
        Rational m03 = new Rational(new BigDecimal(Float.toString(m.m03)));
        Rational m10 = new Rational(new BigDecimal(Float.toString(m.m10)));
        Rational m11 = new Rational(new BigDecimal(Float.toString(m.m11)));
        Rational m12 = new Rational(new BigDecimal(Float.toString(m.m12)));
        Rational m13 = new Rational(new BigDecimal(Float.toString(m.m13)));
        Rational m20 = new Rational(new BigDecimal(Float.toString(m.m20)));
        Rational m21 = new Rational(new BigDecimal(Float.toString(m.m21)));
        Rational m22 = new Rational(new BigDecimal(Float.toString(m.m22)));
        Rational m23 = new Rational(new BigDecimal(Float.toString(m.m23)));
        Rational m30 = new Rational(new BigDecimal(Float.toString(m.m30)));
        Rational m31 = new Rational(new BigDecimal(Float.toString(m.m31)));
        Rational m32 = new Rational(new BigDecimal(Float.toString(m.m32)));
        Rational m33 = new Rational(new BigDecimal(Float.toString(m.m33)));
        this.M[0][0] = m00;
        this.M[1][0] = m10;
        this.M[2][0] = m20;
        this.M[3][0] = m30;
        this.M[0][1] = m01;
        this.M[1][1] = m11;
        this.M[2][1] = m21;
        this.M[3][1] = m31;
        this.M[0][2] = m02;
        this.M[1][2] = m12;
        this.M[2][2] = m22;
        this.M[3][2] = m32;
        this.M[0][3] = m03;
        this.M[1][3] = m13;
        this.M[2][3] = m23;
        this.M[3][3] = m33;
    }

    private RationalMatrix(Rational[][] m) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.M[j][i] = m[j][i];
            }
        }
    }

    public RationalMatrix invert() {
        final Rational[][] mn = new Rational[4][4];

        Rational s0 = M[0][0].multiply(M[1][1]).subtract(M[1][0].multiply(M[0][1]));
        Rational s1 = M[0][0].multiply(M[1][2]).subtract(M[1][0].multiply(M[0][2]));
        Rational s2 = M[0][0].multiply(M[1][3]).subtract(M[1][0].multiply(M[0][3]));
        Rational s3 = M[0][1].multiply(M[1][2]).subtract(M[1][1].multiply(M[0][2]));
        Rational s4 = M[0][1].multiply(M[1][3]).subtract(M[1][1].multiply(M[0][3]));
        Rational s5 = M[0][2].multiply(M[1][3]).subtract(M[1][2].multiply(M[0][3]));

        Rational c5 = M[2][2].multiply(M[3][3]).subtract(M[3][2].multiply(M[2][3]));
        Rational c4 = M[2][1].multiply(M[3][3]).subtract(M[3][1].multiply(M[2][3]));
        Rational c3 = M[2][1].multiply(M[3][2]).subtract(M[3][1].multiply(M[2][2]));
        Rational c2 = M[2][0].multiply(M[3][3]).subtract(M[3][0].multiply(M[2][3]));
        Rational c1 = M[2][0].multiply(M[3][2]).subtract(M[3][0].multiply(M[2][2]));
        Rational c0 = M[2][0].multiply(M[3][1]).subtract(M[3][0].multiply(M[2][1]));

        // TODO Should check for 0 determinant

        Rational invdet = Rational.ONE.divide(s0.multiply(c5).subtract(s1.multiply(c4)).add(s2.multiply(c3)).add(s3.multiply(c2)).subtract(s4.multiply(c1)).add(s5.multiply(c0)));

        mn[0][0] = M[1][1].multiply(c5).subtract(M[1][2].multiply(c4)).add(M[1][3].multiply(c3)).multiply(invdet);
        mn[0][1] = M[0][1].negate().multiply(c5).add(M[0][2].multiply(c4)).subtract(M[0][3].multiply(c3)).multiply(invdet);
        mn[0][2] = M[3][1].multiply(s5).subtract(M[3][2].multiply(s4)).add(M[3][3].multiply(s3)).multiply(invdet);
        mn[0][3] = M[2][1].negate().multiply(s5).add(M[2][2].multiply(s4)).subtract(M[2][3].multiply(s3)).multiply(invdet);

        mn[1][0] = M[1][0].negate().multiply(c5).add(M[1][2].multiply(c2)).subtract(M[1][3].multiply(c1)).multiply(invdet);
        mn[1][1] = M[0][0].multiply(c5).subtract(M[0][2].multiply(c2)).add(M[0][3].multiply(c1)).multiply(invdet);
        mn[1][2] = M[3][0].negate().multiply(s5).add(M[3][2].multiply(s2)).subtract(M[3][3].multiply(s1)).multiply(invdet);
        mn[1][3] = M[2][0].multiply(s5).subtract(M[2][2].multiply(s2)).add(M[2][3].multiply(s1)).multiply(invdet);

        mn[2][0] = M[1][0].multiply(c4).subtract(M[1][1].multiply(c2)).add(M[1][3].multiply(c0)).multiply(invdet);
        mn[2][1] = M[0][0].negate().multiply(c4).add(M[0][1].multiply(c2)).subtract(M[0][3].multiply(c0)).multiply(invdet);
        mn[2][2] = M[3][0].multiply(s4).subtract(M[3][1].multiply(s2)).add(M[3][3].multiply(s0)).multiply(invdet);
        mn[2][3] = M[2][0].negate().multiply(s4).add(M[2][1].multiply(s2)).subtract(M[2][3].multiply(s0)).multiply(invdet);

        mn[3][0] = M[1][0].negate().multiply(c3).add(M[1][1].multiply(c1)).subtract(M[1][2].multiply(c0)).multiply(invdet);
        mn[3][1] = M[0][0].multiply(c3).subtract(M[0][1].multiply(c1)).add(M[0][2].multiply(c0)).multiply(invdet);
        mn[3][2] = M[3][0].negate().multiply(s3).add(M[3][1].multiply(s1)).subtract(M[3][2].multiply(s0)).multiply(invdet);
        mn[3][3] = M[2][0].multiply(s3).subtract(M[2][1].multiply(s1)).add(M[2][2].multiply(s0)).multiply(invdet);

        return new RationalMatrix(mn);
    }

    public Vector3r transform(Vector3r relPos) {
        final Rational[] result = new Rational[4];
        for (int row = 0; row < 4; row++) {
            final Rational p1 = this.M[0][row].multiply(relPos.X);
            final Rational p2 = this.M[1][row].multiply(relPos.Y);
            final Rational p3 = this.M[2][row].multiply(relPos.Z);
            final Rational p4 = this.M[3][row];
            result[row] = p1.add(p2).add(p3).add(p4);
        }
        return new Vector3r(result[0], result[1], result[2]);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.append(this.M[j][i].doubleValue());
                result.append(" "); //$NON-NLS-1$
            }
            result.append("\n"); //$NON-NLS-1$
        }
        return result.toString();
    }
}
