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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.eclipse.swt.custom.CTabItem;
import org.nschmidt.ldparteditor.composites.Composite3D;
import org.nschmidt.ldparteditor.composites.compositetab.CompositeTab;
import org.nschmidt.ldparteditor.logger.NLogger;
import org.nschmidt.ldparteditor.opengl.OpenGLRenderer;
import org.nschmidt.ldparteditor.project.Project;
import org.nschmidt.ldparteditor.shells.editor3d.Editor3DWindow;
import org.nschmidt.ldparteditor.shells.editortext.EditorTextWindow;
import org.nschmidt.ldparteditor.text.LDParsingException;
import org.nschmidt.ldparteditor.text.UTF8BufferedReader;
import org.nschmidt.ldparteditor.widgets.TreeItem;
import org.nschmidt.ldparteditor.workbench.WorkbenchManager;

/**
 * This manager reads the local library contents of the following folders: <br>
 * <br>
 * <ul>
 * <li>The official parts folder (e.g. D:\LDRAW\)</li>
 * <li>The unofficial parts folder (e.g. D:\LDRAW\Unofficial\)</li>
 * <li>The current project</li>
 * </ul>
 *
 * @author nils TODO Needs documentation
 */
public class LibraryManager {

    // TODO Needs error handling!

    /**
     * Reads all paths to project library parts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readProjectParts(TreeItem treeItem) {
        readLibraryFolder(Project.getProjectPath(), "PARTS", "", treeItem, false, false, DatType.PART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to project library parts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readProjectPartsParent(TreeItem treeItem) {
        readLibraryFolder(Project.getProjectPath(), "", "", treeItem, false, false, DatType.PART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to project library subparts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readProjectSubparts(TreeItem treeItem) {
        readLibraryFolder(Project.getProjectPath(), "PARTS", "S", treeItem, false, false, DatType.SUBPART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to project library primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readProjectPrimitives(TreeItem treeItem) {
        readLibraryFolder(Project.getProjectPath(), "P", "", treeItem, true, false, DatType.PRIMITIVE); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to project library hi-res primirives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readProjectHiResPrimitives(TreeItem treeItem) {
        readLibraryFolder(Project.getProjectPath(), "P", "48", treeItem, true, false, DatType.PRIMITIVE48); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to project library low-res primirives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readProjectLowResPrimitives(TreeItem treeItem) {
        readLibraryFolder(Project.getProjectPath(), "P", "8", treeItem, true, false, DatType.PRIMITIVE8); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to unofficial library parts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readUnofficialParts(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "PARTS", "", treeItem, false, false, DatType.PART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to unofficial library subparts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readUnofficialSubparts(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "PARTS", "S", treeItem, false, false, DatType.SUBPART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to unofficial library primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readUnofficialPrimitives(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "P", "", treeItem, true, false, DatType.PRIMITIVE); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to unofficial library hi-res primirives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readUnofficialHiResPrimitives(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "P", "48", treeItem, true, false, DatType.PRIMITIVE48); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to unofficial library low-res primirives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readUnofficialLowResPrimitives(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "P", "8", treeItem, true, false, DatType.PRIMITIVE8); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to official library parts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readOfficialParts(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "PARTS", "", treeItem, false, true, DatType.PART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to official library subparts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readOfficialSubparts(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "PARTS", "S", treeItem, false, true, DatType.SUBPART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to official library primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readOfficialPrimitives(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "P", "", treeItem, true, true, DatType.PRIMITIVE); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to official library hi-res primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readOfficialHiResPrimitives(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "P", "48", treeItem, true, true, DatType.PRIMITIVE48); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Reads all paths to official library low-res primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static void readOfficialLowResPrimitives(TreeItem treeItem) {
        readLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "P", "8", treeItem, true, true, DatType.PRIMITIVE8); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to project library objects case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     * @return
     */
    public static int[] syncProjectElements(TreeItem treeItem) {
        return syncLibraryFolder(Project.getProjectPath(), "", "", treeItem, false, false, DatType.PART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to unofficial library parts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncUnofficialParts(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "PARTS", "", treeItem, false, false, DatType.PART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to unofficial library subparts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncUnofficialSubparts(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "PARTS", "S", treeItem, false, false, DatType.SUBPART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to unofficial library primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncUnofficialPrimitives(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "P", "", treeItem, true, false, DatType.PRIMITIVE); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to unofficial library hi-res primirives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncUnofficialHiResPrimitives(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "P", "48", treeItem, true, false, DatType.PRIMITIVE48); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to unofficial library low-res primirives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncUnofficialLowResPrimitives(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getUnofficialFolderPath(), "P", "8", treeItem, true, false, DatType.PRIMITIVE8); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to official library parts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncOfficialParts(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "PARTS", "", treeItem, false, true, DatType.PART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to official library subparts case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncOfficialSubparts(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "PARTS", "S", treeItem, false, true, DatType.SUBPART); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to official library primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncOfficialPrimitives(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "P", "", treeItem, true, true, DatType.PRIMITIVE); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to official library hi-res primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncOfficialHiResPrimitives(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "P", "48", treeItem, true, true, DatType.PRIMITIVE48); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Synchronises all paths to official library low-res primitives case insensitive <br>
     * <b>NOTE:</b> The base path is still case sensitive!
     *
     * @param treeItem
     *            the target {@code TreeItem} from the Parts Tree of the 3D
     *            editor.
     */
    public static int[] syncOfficialLowResPrimitives(TreeItem treeItem) {
        return syncLibraryFolder(WorkbenchManager.getUserSettingState().getLdrawFolderPath(), "P", "8", treeItem, true, true, DatType.PRIMITIVE8); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * This is a helper class, which provides a comparator for DAT file names.
     *
     * @author nils
     *
     */
    private static class DatFileName implements Comparable<DatFileName> {
        /** The DAT file name to compare */
        private final String name;
        private final String fullname;
        private final String description;
        private final boolean comparePrimitives;

        /**
         * Creates a DAT file name object
         *
         * @param name
         *            the DAT file name (e.g. 973p7u.dat)
         */
        public DatFileName(String name, String description, boolean comparePrimitives) {
            this.name = name;
            this.fullname = ""; //$NON-NLS-1$
            this.description = description;
            this.comparePrimitives = comparePrimitives;
        }

        public DatFileName(String fullname, String name, String description, boolean comparePrimitives) {
            this.name = name;
            this.fullname = fullname;
            this.description = description;
            this.comparePrimitives = comparePrimitives;
        }

        public String getName() {
            return this.name;
        }

        public String getFullName() {
            return this.fullname;
        }

        public String getDescription() {
            return this.description;
        }

        @Override
        public int compareTo(DatFileName other) {

            String[] segsThis = this.name.split(Pattern.quote(".")); //$NON-NLS-1$
            String[] segsOther = other.name.split(Pattern.quote(".")); //$NON-NLS-1$

            // Special cases: unknown parts numbers "u[Number]" and unknown
            // stickers "s[Number]"
            if (segsThis[0].charAt(0) == 'u' && segsOther[0].charAt(0) == 'u' || segsThis[0].charAt(0) == 's' && segsOther[0].charAt(0) == 's') {
                segsThis[0] = segsThis[0].substring(1, segsThis[0].length());
                segsOther[0] = segsOther[0].substring(1, segsOther[0].length());
            }

            // Special cases: Primitive fractions
            if (this.comparePrimitives && other.comparePrimitives) {
                if (segsThis[0].length() > 2 && segsOther[0].length() > 2 && (segsThis[0].charAt(1) == '-' || segsThis[0].charAt(2) == '-')
                        && (segsOther[0].charAt(1) == '-' || segsOther[0].charAt(2) == '-')) {
                    String upperThis = ""; //$NON-NLS-1$
                    String upperOther = ""; //$NON-NLS-1$
                    String lowerThis = ""; //$NON-NLS-1$
                    String lowerOther = ""; //$NON-NLS-1$
                    String suffixThis = ""; //$NON-NLS-1$
                    String suffixOther = ""; //$NON-NLS-1$
                    boolean readUpper = true;
                    int charCount = 0;
                    char[] charsThis = segsThis[0].toCharArray();
                    for (char c : charsThis) {
                        if (Character.isDigit(c)) {
                            if (readUpper) {
                                upperThis = upperThis + c;
                            } else {
                                lowerThis = lowerThis + c;
                            }
                        } else {
                            if (readUpper) {
                                readUpper = false;
                            } else {
                                suffixThis = segsThis[0].substring(charCount, segsThis[0].length());
                                break;
                            }
                        }
                        charCount++;
                    }
                    readUpper = true;
                    charCount = 0;
                    char[] charsOther = segsOther[0].toCharArray();
                    for (char c : charsOther) {
                        if (Character.isDigit(c)) {
                            if (readUpper) {
                                upperOther = upperOther + c;
                            } else {
                                lowerOther = lowerOther + c;
                            }
                        } else {
                            if (readUpper) {
                                readUpper = false;
                            } else {
                                suffixOther = segsOther[0].substring(charCount, segsOther[0].length());
                                break;
                            }
                        }
                        charCount++;
                    }
                    try {

                        float fractionThis = Float.parseFloat(upperThis) / Float.parseFloat(lowerThis);
                        float fractionOther = Float.parseFloat(upperOther) / Float.parseFloat(lowerOther);

                        if (!suffixThis.equals(suffixOther)) {
                            return suffixThis.compareTo(suffixOther);
                        } else {
                            if (fractionThis > fractionOther) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    } catch (NumberFormatException consumed) {
                    }
                }
            }

            String numThis = ""; //$NON-NLS-1$
            String numOther = ""; //$NON-NLS-1$
            char[] charsThis = segsThis[0].toCharArray();
            for (char c : charsThis) {
                if (Character.isDigit(c)) {
                    numThis = numThis + c;
                } else {
                    break;
                }
            }
            char[] charsOther = segsOther[0].toCharArray();
            for (char c : charsOther) {
                if (Character.isDigit(c)) {
                    numOther = numOther + c;
                } else {
                    break;
                }
            }
            if (numThis.isEmpty() || numOther.isEmpty() || numThis.equals(numOther)) {
                return this.name.compareTo(other.name);
            } else {
                int intThis = Integer.parseInt(numThis, 10);
                int intOther = Integer.parseInt(numOther, 10);
                if (intThis == intOther) {
                    return 0;
                } else {
                    if (intThis > intOther) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (fullname == null ? 0 : fullname.hashCode());
            result = prime * result + (name == null ? 0 : name.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            DatFileName other = (DatFileName) obj;
            if (fullname == null) {
                if (other.fullname != null)
                    return false;
            } else if (!fullname.equals(other.fullname))
                return false;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            return true;
        }
    }

    /**
     * Reads the contents (DAT files) from the folder case insensitive and sorts
     * the entries alphabetically and by number
     *
     * @param basePath
     *            this path was already validated before
     * @param prefix1
     *            the case insensitive name of the first subfolder
     * @param prefix2
     *            the case insensitive name of the second subfolder
     * @param treeItem
     *            the target {@code TreeItem} which lists all DAT files from the
     *            folder
     * @param isPrimitiveFolder
     *            {@code true} if the folder contains primitives
     */
    private static void readLibraryFolder(String basePath, String prefix1, String prefix2, TreeItem treeItem, boolean isPrimitiveFolder, boolean isReadOnlyFolder, DatType type) {
        String folderPath = basePath;
        boolean canSearch = true;
        final File baseFolder = new File(basePath);
        if (prefix1.isEmpty()) {
            HashMap<DatFileName, TreeItem> parentMap = new HashMap<>();
            HashMap<DatFileName, DatType> typeMap = new HashMap<>();
            ArrayList<DatFileName> datFiles = new ArrayList<>();
            File libFolder = new File(folderPath);
            StringBuilder titleSb = new StringBuilder();
            File[] files = libFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile() && f.getName().matches(".*.dat")) { //$NON-NLS-1$
                        titleSb.setLength(0);
                        UTF8BufferedReader reader = null;
                        try {
                            reader = new UTF8BufferedReader(f.getAbsolutePath());
                            String title = reader.readLine();
                            if (title != null) {
                                title = title.trim();
                                if (title.length() > 0) {
                                    titleSb.append(" -"); //$NON-NLS-1$
                                    titleSb.append(title.substring(1));
                                }
                            }
                            // Detect type
                            while (true) {
                                String typ = reader.readLine();
                                if (typ != null) {
                                    typ = typ.trim();
                                    if (!typ.startsWith("0")) { //$NON-NLS-1$
                                        break;
                                    } else {
                                        int i1 = typ.indexOf("!LDRAW_ORG"); //$NON-NLS-1$
                                        if (i1 > -1) {
                                            int i2;
                                            i2 = typ.indexOf("Subpart"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.SUBPART;
                                                break;
                                            }
                                            i2 = typ.indexOf("Part"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PART;
                                                break;
                                            }
                                            i2 = typ.indexOf("48_Primitive"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PRIMITIVE48;
                                                break;
                                            }
                                            i2 = typ.indexOf("8_Primitive"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PRIMITIVE8;
                                                break;
                                            }
                                            i2 = typ.indexOf("Primitive"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PRIMITIVE;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }
                            if (type == null) type = DatType.PART;
                            // Change treeItem according to type
                            switch (type) {
                            case PART:
                                treeItem = Editor3DWindow.getWindow().getProjectParts();
                                break;
                            case SUBPART:
                                treeItem = Editor3DWindow.getWindow().getProjectSubparts();
                                break;
                            case PRIMITIVE:
                                treeItem = Editor3DWindow.getWindow().getProjectPrimitives();
                                break;
                            case PRIMITIVE48:
                                treeItem = Editor3DWindow.getWindow().getProjectPrimitives48();
                                break;
                            default:
                                break;
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
                        DatFileName name = new DatFileName(f.getName(), titleSb.toString(), type == DatType.PRIMITIVE || type == DatType.PRIMITIVE48  || type == DatType.PRIMITIVE8);
                        datFiles.add(name);
                        parentMap.put(name, treeItem);
                        typeMap.put(name, type);
                    }
                }
            } else {
                NLogger.error(LibraryManager.class, "readLibraryFolder: Can't open directory" + folderPath);  //$NON-NLS-1$
            }
            // Sort the file list
            Collections.sort(datFiles);
            // Create the file entries
            for (DatFileName dat : datFiles) {
                TreeItem finding = new TreeItem(parentMap.get(dat));
                // Save the path
                DatFile path = new DatFile(folderPath + File.separator + dat.getName(), dat.getDescription(), isReadOnlyFolder, typeMap.get(dat));
                finding.setData(path);
                // Set the filename
                if (Project.getUnsavedFiles().contains(path)) {
                    // Insert asterisk if the file was modified
                    finding.setText("* " + dat.getName() + dat.getDescription()); //$NON-NLS-1$
                } else {
                    finding.setText(dat.getName() + dat.getDescription());
                }
            }
        } else {
            final File[] baseFolderFiles = baseFolder.listFiles();
            if (baseFolderFiles == null) {
                return;
            }

            for (File sub : baseFolderFiles) {
                // Check if the sub-folder exist
                if (sub.isDirectory() && sub.getName().equalsIgnoreCase(prefix1)) {
                    folderPath = folderPath + File.separator + sub.getName();
                    if (!prefix2.equals("")) { //$NON-NLS-1$
                        // We can not search now. It is not guaranteed that the
                        // sub-sub-folder exist (e.g. D:\LDRAW\PARTS\S)
                        canSearch = false;
                        File subFolder = new File(basePath + File.separator + sub.getName());
                        File[] subFolderFiles = subFolder.listFiles();
                        if (subFolderFiles != null) {
                            for (File subsub : subFolderFiles) {
                                if (subsub.isDirectory() && subsub.getName().equalsIgnoreCase(prefix2)) {
                                    folderPath = folderPath + File.separator + subsub.getName();
                                    canSearch = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (canSearch) {
                        // Do the search for DAT files
                        ArrayList<DatFileName> datFiles = new ArrayList<>();
                        File libFolder = new File(folderPath);
                        final File[] libFolderFiles = libFolder.listFiles();
                        if (libFolderFiles != null) {
                            for (File f : libFolderFiles) {
                                if (f.isFile() && f.getName().matches(".*.dat")) { //$NON-NLS-1$
                                    datFiles.add(new DatFileName(f.getName(), "", isPrimitiveFolder)); //$NON-NLS-1$
                                }
                            }
                        }

                        // Sort the file list
                        Collections.sort(datFiles);
                        // Create the file entries
                        for (DatFileName dat : datFiles) {
                            TreeItem finding = new TreeItem(treeItem);
                            // Save the path
                            DatFile path = new DatFile(folderPath + File.separator + dat.getName(), "", isReadOnlyFolder, type); //$NON-NLS-1$
                            finding.setData(path);
                            // Set the filename
                            if (Project.getUnsavedFiles().contains(path)) {
                                // Insert asterisk if the file was modified
                                finding.setText("* " + dat.getName()); //$NON-NLS-1$
                            } else {
                                finding.setText(dat.getName());
                            }
                            DescriptionManager.registerDescription(finding);
                        }
                    }
                    break;
                }
            }
        }
    }


    /**
     * Synchronises the contents (DAT files) from the folder case insensitive and sorts
     * the entries alphabetically and by number
     *
     * @param basePath
     *            this path was already validated before
     * @param prefix1
     *            the case insensitive name of the first subfolder
     * @param prefix2
     *            the case insensitive name of the second subfolder
     * @param treeItem
     *            the target {@code TreeItem} which lists all DAT files from the
     *            folder
     * @param isPrimitiveFolder
     *            {@code true} if the folder contains primitives
     * @return An array which contains how many files were added [0], deleted [1], and can't be replaced [2]
     */
    private static int[] syncLibraryFolder(String basePath, String prefix1, String prefix2, TreeItem treeItem, boolean isPrimitiveFolder, boolean isReadOnlyFolder, DatType type) {

        int[] result = new int[3];

        HashMap<String, TreeItem> parentMap = new HashMap<>();
        HashMap<String, DatType> typeMap = new HashMap<>();
        HashMap<String, DatFileName> dfnMap = new HashMap<>();
        HashSet<String> locked = new HashSet<>();
        HashSet<String> loaded = new HashSet<>();
        HashMap<String, DatFile> existingMap = new HashMap<>();

        HashMap<String, Boolean> readOnly = new HashMap<>();

        HashMap<String, HashSet<Composite3D>> openIn3DMap = new HashMap<>();
        HashMap<String, CompositeTab> openInTextMap = new HashMap<>();

        HashMap<String, HashSet<Composite3D>> unsavedIn3DMap = new HashMap<>();
        HashMap<String, CompositeTab> unsavedInTextMap = new HashMap<>();

        HashMap<String, TreeItem> newParentMap = new HashMap<>();
        HashMap<String, DatType> newTypeMap = new HashMap<>();
        HashMap<String, DatFileName> newDfnMap = new HashMap<>();

        HashMap<String, HistoryManager> historyMap = new HashMap<>();
        HashMap<String, DuplicateManager> duplicateMap = new HashMap<>();

        if (prefix1.isEmpty()) {

            // Sync project root.

            // 1. Read and store all unsaved project files, since we want to keep them in the project

            final TreeItem treeItemProjectParts = treeItem.getItems().get(0);
            final TreeItem treeItemProjectSubparts = treeItem.getItems().get(1);
            final TreeItem treeItemProjectPrimitives = treeItem.getItems().get(2);
            final TreeItem treeItemProjectPrimitives48 = treeItem.getItems().get(3);
            final TreeItem treeItemProjectPrimitives8 = treeItem.getItems().get(4);

            readVirtualDataFromFolder(result, parentMap, typeMap, locked, loaded, existingMap, dfnMap, treeItemProjectParts, openIn3DMap, openInTextMap, unsavedIn3DMap, unsavedInTextMap, historyMap, duplicateMap, readOnly, true);
            readVirtualDataFromFolder(result, parentMap, typeMap, locked, loaded, existingMap, dfnMap, treeItemProjectSubparts, openIn3DMap, openInTextMap, unsavedIn3DMap, unsavedInTextMap, historyMap, duplicateMap, readOnly, true);
            readVirtualDataFromFolder(result, parentMap, typeMap, locked, loaded, existingMap, dfnMap, treeItemProjectPrimitives, openIn3DMap, openInTextMap, unsavedIn3DMap, unsavedInTextMap, historyMap, duplicateMap, readOnly, true);
            readVirtualDataFromFolder(result, parentMap, typeMap, locked, loaded, existingMap, dfnMap, treeItemProjectPrimitives48, openIn3DMap, openInTextMap, unsavedIn3DMap, unsavedInTextMap, historyMap, duplicateMap, readOnly, true);
            readVirtualDataFromFolder(result, parentMap, typeMap, locked, loaded, existingMap, dfnMap, treeItemProjectPrimitives8, openIn3DMap, openInTextMap, unsavedIn3DMap, unsavedInTextMap, historyMap, duplicateMap, readOnly, true);

            readAllUnsavedFiles(parentMap, typeMap, dfnMap, locked, existingMap);


            // 2. Clear all project trees
            treeItemProjectParts.getItems().clear();
            treeItemProjectSubparts.getItems().clear();
            treeItemProjectPrimitives.getItems().clear();
            treeItemProjectPrimitives48.getItems().clear();
            treeItemProjectPrimitives8.getItems().clear();

            // 3. Scan for new files

            readActualDataFromFolder(result, basePath, null, "", "", locked, loaded, newParentMap, newTypeMap, newDfnMap, null, false, readOnly, false); //$NON-NLS-1$ //$NON-NLS-2$

            readActualDataFromFolder(result, basePath, DatType.PART, "PARTS", "", locked, loaded, newParentMap, newTypeMap, newDfnMap, treeItemProjectParts, false, readOnly, false); //$NON-NLS-1$ //$NON-NLS-2$
            readActualDataFromFolder(result, basePath, DatType.SUBPART, "PARTS", "S", locked, loaded, newParentMap, newTypeMap, newDfnMap, treeItemProjectSubparts, false, readOnly, false); //$NON-NLS-1$ //$NON-NLS-2$
            readActualDataFromFolder(result, basePath, DatType.PRIMITIVE, "P", "", locked, loaded, newParentMap, newTypeMap, newDfnMap, treeItemProjectPrimitives, true, readOnly, false); //$NON-NLS-1$ //$NON-NLS-2$
            readActualDataFromFolder(result, basePath, DatType.PRIMITIVE48, "P", "48", locked, loaded, newParentMap, newTypeMap, newDfnMap, treeItemProjectPrimitives48, true, readOnly, false); //$NON-NLS-1$ //$NON-NLS-2$
            readActualDataFromFolder(result, basePath, DatType.PRIMITIVE8, "P", "8", locked, loaded, newParentMap, newTypeMap, newDfnMap, treeItemProjectPrimitives8, true, readOnly, false); //$NON-NLS-1$ //$NON-NLS-2$

        } else {

            // 1. Read and store all unsaved project files, since we want to keep them in the editor
            readVirtualDataFromFolder(result, parentMap, typeMap, locked, loaded, existingMap, dfnMap, treeItem, openIn3DMap, openInTextMap, unsavedIn3DMap, unsavedInTextMap, historyMap, duplicateMap, readOnly, !isReadOnlyFolder);

            if (!isReadOnlyFolder) readAllUnsavedFiles(parentMap, typeMap, dfnMap, locked, existingMap);

            // 2. Clear the tree
            treeItem.getItems().clear();

            // 3. Scan for new files
            readActualDataFromFolder(result, basePath, type, prefix1, prefix2, locked, loaded, newParentMap, newTypeMap, newDfnMap, treeItem, isPrimitiveFolder, readOnly, isReadOnlyFolder);

        }

        // 4. Rebuilt the tree arrays

        ArrayList<DatFileName> datFiles = new ArrayList<>();
        HashMap<TreeItem, ArrayList<DatFile>> lists = new HashMap<>();
        for (String key : dfnMap.keySet()) {
            datFiles.add(dfnMap.get(key));
        }
        for (String key : newDfnMap.keySet()) {
            datFiles.add(newDfnMap.get(key));
        }
        Collections.sort(datFiles);

        for (DatFileName dfn : datFiles) {

            String path = dfn.getFullName();
            TreeItem ti;

            if (parentMap.containsKey(path)) {
                ti = parentMap.get(path);
            } else if (newParentMap.containsKey(path)) {
                ti = newParentMap.get(path);
            } else {
                // Very defensive, but anyway..
                continue;
            }

            if (!Editor3DWindow.getWindow().getUnsaved().equals(ti)) {
                ArrayList<DatFile> fileList;
                if (lists.containsKey(ti)) {
                    fileList = lists.get(ti);
                } else {
                    fileList = new ArrayList<>();
                    lists.put(ti, fileList);
                }
                if (existingMap.containsKey(path)) {
                    fileList.add(existingMap.get(path));
                } else {

                    // Create new DatFile

                    boolean readOnly2 = false;

                    if (readOnly.containsKey(path)) {
                        readOnly2 = readOnly.get(path);
                    }

                    if (typeMap.containsKey(path)) {
                        type = typeMap.get(path);
                    } else if (newTypeMap.containsKey(path)) {
                        type = newTypeMap.get(path);
                    }

                    DatFile newDf = new DatFile(dfn.getFullName(), dfn.getDescription(), readOnly2, type);

                    if (historyMap.containsKey(path)) {
                        // Copies the undo/redo history if there is one
                        newDf.setHistory(historyMap.get(path));
                        newDf.getHistory().setDatFile(newDf);
                        // Also do this for possible duplicated data
                        newDf.getDuplicate().setDatFile(newDf);
                        // and the DatHeader check
                        newDf.getDatHeader().setDatFile(newDf);
                    }

                    if (openIn3DMap.containsKey(path) || openInTextMap.containsKey(path)) {
                        newDf.parseForData(true);
                        if (openIn3DMap.containsKey(path)) {
                            for (Composite3D c3d : openIn3DMap.get(path)) {
                                c3d.setLockableDatFileReference(newDf);
                            }
                        }
                        if (openInTextMap.containsKey(path)) {
                            openInTextMap.get(path).getState().setFileNameObj(newDf);
                        }
                    }

                    fileList.add(newDf);
                }
            }
        }

        for (TreeItem key : lists.keySet()) {
            key.setData(lists.get(key));
        }

        // Add unsaved files wich are not anymore on the file system, but were opened in the text editor or in the 3D view

        {
            HashSet<DatFile> newUnsavedFiles = new HashSet<>();
            for (String key : unsavedInTextMap.keySet()) {
                DatFile df = unsavedInTextMap.get(key).getState().getFileNameObj();
                if (!newUnsavedFiles.contains(df)) {
                    newUnsavedFiles.add(df);
                    Project.addUnsavedFile(df);
                    result[2] = result[2] + 1;
                    result[1] = result[1] - 1;
                }
            }
            for (String key : unsavedIn3DMap.keySet()) {
                for (Composite3D c3d : unsavedIn3DMap.get(key)) {
                    DatFile df = c3d.getLockableDatFileReference();
                    if (!newUnsavedFiles.contains(df)) {
                        newUnsavedFiles.add(df);
                        Project.addUnsavedFile(df);
                        result[2] = result[2] + 1;
                        result[1] = result[1] - 1;
                    }
                    break;
                }
            }
        }

        return result;
    }

    private static void readAllUnsavedFiles(HashMap<String, TreeItem> parentMap, HashMap<String, DatType> typeMap, HashMap<String, DatFileName> dfnMap, HashSet<String> locked,
            HashMap<String, DatFile> existingMap) {
        for (DatFile df : Project.getUnsavedFiles()) {
            final String old = df.getOldName();
            if (!locked.contains(old)) {
                locked.add(df.getNewName());
                locked.add(old);
                parentMap.put(old, Editor3DWindow.getWindow().getUnsaved());
                typeMap.put(old, df.getType());
                existingMap.put(old, df);
                dfnMap.put(old, new DatFileName(old, new File(df.getNewName()).getName(), df.getDescription(), df.getType() == DatType.PRIMITIVE || df.getType() == DatType.PRIMITIVE48 || df.getType() == DatType.PRIMITIVE8));
            }
        }
    }

    private static void readVirtualDataFromFolder(
            int[] result,
            HashMap<String, TreeItem> parentMap,
            HashMap<String, DatType> typeMap,
            HashSet<String> locked,
            HashSet<String> loaded,
            HashMap<String, DatFile> existingMap,
            HashMap<String, DatFileName> dfnMap,
            final TreeItem treeItem,
            HashMap<String, HashSet<Composite3D>> openIn3DMap,
            HashMap<String, CompositeTab> openInTextMap,
            HashMap<String, HashSet<Composite3D>> unsavedIn3DMap,
            HashMap<String, CompositeTab> unsavedInTextMap,
            HashMap<String, HistoryManager> historyMap,
            HashMap<String, DuplicateManager> duplicateMap,
            HashMap<String, Boolean> readOnly,
            boolean checkForUnsaved
            ) {

        for (TreeItem ti : treeItem.getItems()) {
            DatFile df = (DatFile) ti.getData();
            final String old = df.getOldName();
            historyMap.put(old, df.getHistory());
            duplicateMap.put(old, df.getDuplicate());
            readOnly.put(old, !checkForUnsaved);
            if (checkForUnsaved && Project.getUnsavedFiles().contains(df)) {
                locked.add(df.getNewName());
                locked.add(df.getOldName());
                parentMap.put(old, treeItem);
                typeMap.put(old, df.getType());
                existingMap.put(old, df);
                dfnMap.put(old, new DatFileName(old, new File(df.getNewName()).getName(), df.getDescription(), df.getType() == DatType.PRIMITIVE || df.getType() == DatType.PRIMITIVE48 || df.getType() == DatType.PRIMITIVE8));
            } else {
                if (!new File(old).exists()) {
                    // 2. Check which "saved" files are not on the disk anymore (only for the statistic)
                    result[1] = result[1] + 1;

                    // 3. Displayed, but deleted files will become unsaved files
                    HashSet<Composite3D> c3ds = new HashSet<>();
                    for (OpenGLRenderer r : Editor3DWindow.getRenders()) {
                        Composite3D c3d = r.getC3D();
                        if (df.equals(c3d.getLockableDatFileReference())) {
                            c3ds.add(c3d);
                        }
                    }
                    if (!c3ds.isEmpty()) unsavedIn3DMap.put(old, c3ds);
                    for (EditorTextWindow w : Project.getOpenTextWindows()) {
                        for (CTabItem t : w.getTabFolder().getItems()) {
                            CompositeTab tab = (CompositeTab) t;
                            if (df.equals(tab.getState().getFileNameObj())) {
                                unsavedInTextMap.put(old, tab);
                            }
                        }
                    }
                } else {

                    // 2.5 Check which "saved" files are on the disk (only for the statistic) items in this set will not count for add
                    loaded.add(df.getNewName());
                    loaded.add(old);

                    // 3.5 Displayed, but unmodified files (Text+3D) need a remapping
                    HashSet<Composite3D> c3ds = new HashSet<>();
                    for (OpenGLRenderer r : Editor3DWindow.getRenders()) {
                        Composite3D c3d = r.getC3D();
                        if (df.equals(c3d.getLockableDatFileReference())) {
                            c3ds.add(c3d);
                        }
                    }
                    if (!c3ds.isEmpty()) openIn3DMap.put(old, c3ds);
                    for (EditorTextWindow w : Project.getOpenTextWindows()) {
                        for (CTabItem t : w.getTabFolder().getItems()) {
                            CompositeTab tab = (CompositeTab) t;
                            if (df.equals(tab.getState().getFileNameObj())) {
                                openInTextMap.put(old, tab);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void readActualDataFromFolder(
            int[] result,
            String basePath,
            DatType type,
            String prefix1,
            String prefix2,
            HashSet<String> locked,
            HashSet<String> loaded,
            HashMap<String, TreeItem> newParentMap,
            HashMap<String, DatType> newTypeMap,
            HashMap<String, DatFileName> newDfnMap,
            final TreeItem treeItem,
            boolean isPrimitiveFolder,
            HashMap<String, Boolean> readOnly,
            boolean isReadOnlyFolder) {

        final File baseFolder = new File(basePath);

        if (prefix1.isEmpty() && prefix2.isEmpty()) {
            StringBuilder titleSb = new StringBuilder();
            File[] files = baseFolder.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isFile() && f.getName().matches(".*.dat")) { //$NON-NLS-1$
                        final String path = f.getAbsolutePath();
                        if (locked.contains(path)) {
                            // File is locked by LPE, so don't parse it twice
                            result[2] = result[2] + 1;
                            continue;
                        }
                        if (!loaded.contains(path)) {
                            // The file is new
                            result[0] = result[0] + 1;
                        }
                        titleSb.setLength(0);
                        TreeItem treeItem2 = Editor3DWindow.getWindow().getProjectParts();
                        UTF8BufferedReader reader = null;
                        try {
                            reader = new UTF8BufferedReader(path);
                            String title = reader.readLine();
                            if (title != null) {
                                title = title.trim();
                                if (title.length() > 0) {
                                    titleSb.append(" -"); //$NON-NLS-1$
                                    titleSb.append(title.substring(1));
                                }
                            }
                            // Detect type
                            while (true) {
                                String typ = reader.readLine();
                                if (typ != null) {
                                    typ = typ.trim();
                                    if (!typ.startsWith("0")) { //$NON-NLS-1$
                                        break;
                                    } else {
                                        int i1 = typ.indexOf("!LDRAW_ORG"); //$NON-NLS-1$
                                        if (i1 > -1) {
                                            int i2;
                                            i2 = typ.indexOf("Subpart"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.SUBPART;
                                                break;
                                            }
                                            i2 = typ.indexOf("Part"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PART;
                                                break;
                                            }
                                            i2 = typ.indexOf("48_Primitive"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PRIMITIVE48;
                                                break;
                                            }
                                            i2 = typ.indexOf("8_Primitive"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PRIMITIVE8;
                                                break;
                                            }
                                            i2 = typ.indexOf("Primitive"); //$NON-NLS-1$
                                            if (i2 > -1 && i1 < i2) {
                                                type = DatType.PRIMITIVE;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    break;
                                }
                            }

                            if (type == null) type = DatType.PART;

                            // Change treeItem according to type
                            switch (type) {
                            case PART:
                                treeItem2 = Editor3DWindow.getWindow().getProjectParts();
                                break;
                            case SUBPART:
                                treeItem2 = Editor3DWindow.getWindow().getProjectSubparts();
                                break;
                            case PRIMITIVE:
                                treeItem2 = Editor3DWindow.getWindow().getProjectPrimitives();
                                break;
                            case PRIMITIVE48:
                                treeItem2 = Editor3DWindow.getWindow().getProjectPrimitives48();
                                break;
                            case PRIMITIVE8:
                                treeItem2 = Editor3DWindow.getWindow().getProjectPrimitives8();
                                break;
                            default:
                                break;
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

                        newDfnMap.put(path, new DatFileName(path, f.getName(), titleSb.toString(), type == DatType.PRIMITIVE || type == DatType.PRIMITIVE48 || type == DatType.PRIMITIVE8));
                        newParentMap.put(path, treeItem2);
                        newTypeMap.put(path, type);
                        readOnly.put(path, isReadOnlyFolder);
                    }
                }
            } else {
                NLogger.error(LibraryManager.class, "readActualDataFromFolder: Can't open directory" + basePath);  //$NON-NLS-1$
            }
        } else {
            boolean canSearch = true;
            final File[] baseFolderFiles = baseFolder.listFiles();
            if (baseFolderFiles == null) {
                return;
            }

            for (File sub : baseFolderFiles) {
                // Check if the sub-folder exist
                if (sub.isDirectory() && sub.getName().equalsIgnoreCase(prefix1)) {
                    String folderPath = basePath + File.separator + sub.getName();
                    if (!prefix2.equals("")) { //$NON-NLS-1$
                        // We can not search now. It is not guaranteed that the
                        // sub-sub-folder exist (e.g. D:\LDRAW\PARTS\S)
                        canSearch = false;
                        File subFolder = new File(basePath + File.separator + sub.getName());
                        final File[] files = subFolder.listFiles();
                        if (files != null) {
                            for (File subsub : files) {
                                if (subsub.isDirectory() && subsub.getName().equalsIgnoreCase(prefix2)) {
                                    folderPath = folderPath + File.separator + subsub.getName();
                                    canSearch = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (canSearch) {
                        // Do the search for DAT files
                        File libFolder = new File(folderPath);
                        UTF8BufferedReader reader = null;
                        StringBuilder titleSb = new StringBuilder();
                        final File[] files = libFolder.listFiles();
                        if (files == null) {
                            break;
                        }

                        for (File f : files) {
                            if (f.isFile() && f.getName().matches(".*.dat")) { //$NON-NLS-1$
                                final String path = f.getAbsolutePath();
                                if (locked.contains(path)) {
                                    // File is locked by LPE, so don't parse it twice
                                    result[2] = result[2] + 1;
                                    continue;
                                }
                                if (!loaded.contains(path)) {
                                    // The file is new
                                    result[0] = result[0] + 1;
                                }
                                titleSb.setLength(0);
                                try {
                                    reader = new UTF8BufferedReader(f.getAbsolutePath());
                                    String title = reader.readLine();
                                    if (title != null) {
                                        title = title.trim();
                                        if (title.length() > 0) {
                                            titleSb.append(" -"); //$NON-NLS-1$
                                            titleSb.append(title.substring(1));
                                        }
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

                                newDfnMap.put(path, new DatFileName(path,f.getName(), titleSb.toString(), isPrimitiveFolder));
                                newParentMap.put(path, treeItem);
                                newTypeMap.put(path, type);
                                readOnly.put(path, isReadOnlyFolder);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
