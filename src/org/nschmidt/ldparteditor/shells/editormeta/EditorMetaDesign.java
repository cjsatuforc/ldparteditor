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
package org.nschmidt.ldparteditor.shells.editormeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.nschmidt.ldparteditor.i18n.I18n;
import org.nschmidt.ldparteditor.text.LDParsingException;
import org.nschmidt.ldparteditor.text.StringHelper;
import org.nschmidt.ldparteditor.text.UTF8BufferedReader;
import org.nschmidt.ldparteditor.widgets.NButton;
import org.nschmidt.ldparteditor.win32appdata.AppData;
import org.nschmidt.ldparteditor.workbench.UserSettingState;
import org.nschmidt.ldparteditor.workbench.WorkbenchManager;

/**
 * The text editor window
 * <p>
 * Note: This class should not be instantiated, it defines the gui layout and no
 * business logic.
 *
 * @author nils
 *
 */
class EditorMetaDesign extends ApplicationWindow {

    final NButton[] btn_Create = new NButton[1];

    final Text[] ev_description_txt = new Text[1];
    final NButton[] ev_description_btn = new NButton[1];
    final Text[] ev_name_txt = new Text[1];
    final Text[] ev_author_realName_txt = new Text[1];
    final Text[] ev_author_userName_txt = new Text[1];
    final NButton[] ev_type_unofficial_btn = new NButton[1];
    final Combo[] ev_type_type_cmb = new Combo[1];
    final NButton[] ev_type_update_btn = new NButton[1];
    final Text[] ev_type_update_txt  = new Text[1];
    final Combo[] ev_license_cmb = new Combo[1];
    final Text[] ev_help_txt = new Text[1];
    final Combo[] ev_bfcHeader_cmb = new Combo[1];
    final Combo[] ev_category_cmb = new Combo[1];
    final Text[] ev_keywords_txt = new Text[1];
    final Text[] ev_cmdline_txt = new Text[1];
    final Text[] ev_history11_txt = new Text[1];
    final Text[] ev_history12_txt = new Text[1];
    final Text[] ev_history13_txt = new Text[1];
    final Text[] ev_history21_txt = new Text[1];
    final Text[] ev_history22_txt = new Text[1];
    final Text[] ev_history23_txt = new Text[1];
    final NButton[] ev_comment_btn = new NButton[1];
    final Text[] ev_comment_txt  = new Text[1];
    final Combo[] ev_bfc_cmb = new Combo[1];
    final Combo[] ev_texmapPlanar_cmb = new Combo[1];
    final Text[] ev_texmapPlanar1_txt  = new Text[1];
    final Text[] ev_texmapPlanar2_txt  = new Text[1];
    final Text[] ev_texmapPlanar3_txt  = new Text[1];
    final Text[] ev_texmapPlanar4_txt  = new Text[1];
    final Text[] ev_texmapPlanar5_txt  = new Text[1];
    final Text[] ev_texmapPlanar6_txt  = new Text[1];
    final Text[] ev_texmapPlanar7_txt  = new Text[1];
    final Text[] ev_texmapPlanar8_txt  = new Text[1];
    final Text[] ev_texmapPlanar9_txt  = new Text[1];
    final Text[] ev_texmapPlanar10_txt  = new Text[1];
    final NButton[] ev_texmapPlanar_btn = new NButton[1];
    final Combo[] ev_texmapCyli_cmb = new Combo[1];
    final Text[] ev_texmapCyli1_txt  = new Text[1];
    final Text[] ev_texmapCyli2_txt  = new Text[1];
    final Text[] ev_texmapCyli3_txt  = new Text[1];
    final Text[] ev_texmapCyli4_txt  = new Text[1];
    final Text[] ev_texmapCyli5_txt  = new Text[1];
    final Text[] ev_texmapCyli6_txt  = new Text[1];
    final Text[] ev_texmapCyli7_txt  = new Text[1];
    final Text[] ev_texmapCyli8_txt  = new Text[1];
    final Text[] ev_texmapCyli9_txt  = new Text[1];
    final Text[] ev_texmapCyli10_txt  = new Text[1];
    final Text[] ev_texmapCyli11_txt  = new Text[1];
    final NButton[] ev_texmapCyli_btn = new NButton[1];
    final Combo[] ev_texmapSphere_cmb = new Combo[1];
    final Text[] ev_texmapSphere1_txt  = new Text[1];
    final Text[] ev_texmapSphere2_txt  = new Text[1];
    final Text[] ev_texmapSphere3_txt  = new Text[1];
    final Text[] ev_texmapSphere4_txt  = new Text[1];
    final Text[] ev_texmapSphere5_txt  = new Text[1];
    final Text[] ev_texmapSphere6_txt  = new Text[1];
    final Text[] ev_texmapSphere7_txt  = new Text[1];
    final Text[] ev_texmapSphere8_txt  = new Text[1];
    final Text[] ev_texmapSphere9_txt  = new Text[1];
    final Text[] ev_texmapSphere10_txt  = new Text[1];
    final Text[] ev_texmapSphere11_txt  = new Text[1];
    final Text[] ev_texmapSphere12_txt  = new Text[1];
    final NButton[] ev_texmapSphere_btn = new NButton[1];
    final NButton[] ev_texmapFallback_btn = new NButton[1];
    final Text[] ev_texmapMeta_txt  = new Text[1];
    final NButton[] ev_texmapEnd_btn = new NButton[1];
    final Text[] ev_todo_txt  = new Text[1];
    final Text[] ev_vertex1_txt  = new Text[1];
    final Text[] ev_vertex2_txt  = new Text[1];
    final Text[] ev_vertex3_txt  = new Text[1];
    final Combo[] ev_csgAction_cmb = new Combo[1];
    final Text[] ev_csgAction1_txt  = new Text[1];
    final Text[] ev_csgAction2_txt  = new Text[1];
    final Text[] ev_csgAction3_txt  = new Text[1];
    final Combo[] ev_csgBody_cmb = new Combo[1];
    final Text[] ev_csgBody1_txt  = new Text[1];
    final Text[] ev_csgBody2_txt  = new Text[1];
    final Text[] ev_csgBody3_txt  = new Text[1];
    final Text[] ev_csgBody4_txt  = new Text[1];
    final Text[] ev_csgBody5_txt  = new Text[1];
    final Text[] ev_csgBody6_txt  = new Text[1];
    final Text[] ev_csgBody7_txt  = new Text[1];
    final Text[] ev_csgBody8_txt  = new Text[1];
    final Text[] ev_csgBody9_txt  = new Text[1];
    final Text[] ev_csgBody10_txt  = new Text[1];
    final Text[] ev_csgBody11_txt  = new Text[1];
    final Text[] ev_csgBody12_txt  = new Text[1];
    final Text[] ev_csgBody13_txt  = new Text[1];
    final Text[] ev_csgBody14_txt  = new Text[1];
    final Text[] ev_csgTrans1_txt  = new Text[1];
    final Text[] ev_csgTrans2_txt  = new Text[1];
    final Text[] ev_csgTrans3_txt  = new Text[1];
    final Text[] ev_csgTrans4_txt  = new Text[1];
    final Text[] ev_csgTrans5_txt  = new Text[1];
    final Text[] ev_csgTrans6_txt  = new Text[1];
    final Text[] ev_csgTrans7_txt  = new Text[1];
    final Text[] ev_csgTrans8_txt  = new Text[1];
    final Text[] ev_csgTrans9_txt  = new Text[1];
    final Text[] ev_csgTrans10_txt  = new Text[1];
    final Text[] ev_csgTrans11_txt  = new Text[1];
    final Text[] ev_csgTrans12_txt  = new Text[1];
    final Text[] ev_csgTrans13_txt  = new Text[1];
    final Text[] ev_csgTrans14_txt  = new Text[1];
    final Text[] ev_csgTrans15_txt  = new Text[1];
    final Text[] ev_csgEx1_txt = new Text[1];
    final Text[] ev_csgEx2_txt = new Text[1];
    final Text[] ev_csgEx3_txt = new Text[1];
    final Text[] ev_csgEx4_txt = new Text[1];
    final Text[] ev_csgEx5_txt = new Text[1];
    final Text[] ev_csgEx6_txt = new Text[1];
    final Text[] ev_csgEx7_txt = new Text[1];
    final Text[] ev_csgCompile_txt  = new Text[1];
    final Text[] ev_csgQuality_txt  = new Text[1];
    final Text[] ev_csgEpsilon_txt  = new Text[1];
    final Text[] ev_csgTJunctionEpsilon_txt  = new Text[1];
    final Text[] ev_csgEdgeCollapseEpsilon_txt  = new Text[1];
    final NButton[] ev_csgDontOptimize_btn = new NButton[1];
    final Text[] ev_png1_txt  = new Text[1];
    final Text[] ev_png2_txt  = new Text[1];
    final Text[] ev_png3_txt  = new Text[1];
    final Text[] ev_png4_txt  = new Text[1];
    final Text[] ev_png5_txt  = new Text[1];
    final Text[] ev_png6_txt  = new Text[1];
    final Text[] ev_png7_txt  = new Text[1];
    final Text[] ev_png8_txt  = new Text[1];
    final Text[] ev_png9_txt  = new Text[1];
    final NButton[] ev_png_btn = new NButton[1];

    final Label[] lbl_lineToInsert  = new Label[1];

    EditorMetaDesign() {
        super(null);
    }

    /**
     * Create contents of the application window.
     *
     * @param parent
     */
    @Override
    protected Control createContents(Composite parent) {
        final UserSettingState userSettings = WorkbenchManager.getUserSettingState();
        setStatus(I18n.E3D_READY_STATUS);
        Composite container = new Composite(parent, SWT.BORDER);
        GridLayout gridLayout = new GridLayout(1, true);
        container.setLayout(gridLayout);
        {
            CTabFolder tabFolderSettings = new CTabFolder(container, SWT.BORDER);
            tabFolderSettings.setMRUVisible(true);
            tabFolderSettings.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
            GridData gridData = new GridData();
            gridData.horizontalAlignment = SWT.FILL;
            gridData.minimumHeight = 200;
            gridData.minimumWidth = 160;
            gridData.heightHint = 200;

            gridData.verticalAlignment = SWT.FILL;
            gridData.grabExcessVerticalSpace = true;

            gridData.grabExcessHorizontalSpace = true;
            tabFolderSettings.setLayoutData(gridData);
            tabFolderSettings.setSize(1024, 768);

            final CTabItem tItem = new CTabItem(tabFolderSettings, SWT.NONE);
            tItem.setText(I18n.META_LDRAW_HEADER);
            {
                final ScrolledComposite cmpScroll = new ScrolledComposite(tabFolderSettings, SWT.H_SCROLL | SWT.V_SCROLL);
                tItem.setControl(cmpScroll);

                Composite cmpMetaArea = new Composite(cmpScroll, SWT.NONE);
                cmpScroll.setContent(cmpMetaArea);
                cmpScroll.setExpandHorizontal(true);
                cmpScroll.setExpandVertical(true);
                cmpScroll.setMinSize(600, 800);

                GridData gdm = new GridData();
                gdm.grabExcessHorizontalSpace = true;
                gdm.grabExcessVerticalSpace = true;
                cmpScroll.setLayoutData(gdm);

                cmpMetaArea.setLayout(new GridLayout(1, false));

                {
                    Composite grpMeta = cmpMetaArea;
                    grpMeta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                    grpMeta.setLayout(new GridLayout(1, false));

                    {
                        Composite cmpDescription = new Composite(grpMeta, SWT.NONE);
                        cmpDescription.setLayout(new GridLayout(3, false));

                        Label lblDescription = new Label(cmpDescription, SWT.NONE);
                        lblDescription.setText("Description:"); //$NON-NLS-1$

                        Text txtDescription = new Text(cmpDescription, SWT.SEARCH);
                        txtDescription.setMessage(I18n.META_DESCRIPTION);
                        ev_description_txt[0] = txtDescription;

                        NButton btnNeedsWork = new NButton(cmpDescription, SWT.TOGGLE);
                        btnNeedsWork.setText("(Needs Work)"); //$NON-NLS-1$
                        ev_description_btn[0] = btnNeedsWork;
                    }

                    {
                        Composite cmpName = new Composite(grpMeta, SWT.NONE);
                        cmpName.setLayout(new GridLayout(2, false));

                        Label lblName = new Label(cmpName, SWT.NONE);
                        lblName.setText("0 Name: "); //$NON-NLS-1$

                        Text txtName = new Text(cmpName, SWT.SEARCH);
                        txtName.setMessage(I18n.META_FILENAME);
                        ev_name_txt[0] = txtName;
                    }

                    {
                        Composite cmpAuthor = new Composite(grpMeta, SWT.NONE);
                        cmpAuthor.setLayout(new GridLayout(5, false));

                        Label lblAuthor = new Label(cmpAuthor, SWT.NONE);
                        lblAuthor.setText("0 Author: "); //$NON-NLS-1$

                        Text txtRealName = new Text(cmpAuthor, SWT.SEARCH);
                        txtRealName.setMessage(I18n.META_AUTHOR);
                        ev_author_realName_txt[0] = txtRealName;
                        if (userSettings.getRealUserName() != null) {
                            txtRealName.setText(userSettings.getRealUserName());
                        }

                        Label lblAuthor2 = new Label(cmpAuthor, SWT.NONE);
                        lblAuthor2.setText("["); //$NON-NLS-1$

                        Text txtUserName = new Text(cmpAuthor, SWT.SEARCH);
                        txtUserName.setMessage(I18n.META_USERNAME);
                        ev_author_userName_txt[0] = txtUserName;

                        if (userSettings.getLdrawUserName() != null) {
                            txtUserName.setText(userSettings.getLdrawUserName());
                        }

                        Label lblAuthor3 = new Label(cmpAuthor, SWT.NONE);
                        lblAuthor3.setText("]"); //$NON-NLS-1$
                    }

                    {
                        Composite cmpType = new Composite(grpMeta, SWT.NONE);
                        cmpType.setLayout(new GridLayout(5, false));

                        Label lblType = new Label(cmpType, SWT.NONE);
                        lblType.setText("0 !LDRAW_ORG "); //$NON-NLS-1$

                        NButton btnUnofficial = new NButton(cmpType, SWT.TOGGLE);
                        btnUnofficial.setText("Unofficial"); //$NON-NLS-1$
                        ev_type_unofficial_btn[0] = btnUnofficial;

                        Combo cmbType = new Combo(cmpType, SWT.NONE);
                        cmbType.setItems(new String[] { "Part", "Subpart", "Primitive", "8_Primitive", "48_Primitive", "Shortcut", "Part Alias", "Part Physical_Colour",  "Part Physical_Colour Alias", "Part Flexible_Section", "Shortcut Alias", "Shortcut Physical_Colour",  "Shortcut Physical_Colour Alias"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$
                        cmbType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                        cmbType.setText("Part"); //$NON-NLS-1$
                        cmbType.select(0);
                        ev_type_type_cmb[0] = cmbType;

                        NButton btnUpdate = new NButton(cmpType, SWT.TOGGLE);
                        btnUpdate.setText("UPDATE"); //$NON-NLS-1$
                        ev_type_update_btn[0] = btnUpdate;

                        Text txtUpdate = new Text(cmpType, SWT.SEARCH);
                        txtUpdate.setMessage(I18n.META_YEAR_RELEASE);
                        txtUpdate.setEnabled(false);
                        ev_type_update_txt[0] = txtUpdate;
                    }

                    {
                        Composite cmpLicense = new Composite(grpMeta, SWT.NONE);
                        cmpLicense.setLayout(new GridLayout(2, false));

                        Label lblLicense = new Label(cmpLicense, SWT.NONE);
                        lblLicense.setText("0 !LICENSE "); //$NON-NLS-1$

                        Combo cmbLicense = new Combo(cmpLicense, SWT.NONE);
                        cmbLicense.setItems(new String[] { "Redistributable under CCAL version 2.0 : see CAreadme.txt", "Not redistributable : see NonCAreadme.txt" }); //$NON-NLS-1$ //$NON-NLS-2$
                        cmbLicense.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                        cmbLicense.setText(userSettings.getLicense() != null ? userSettings.getLicense() : "Redistributable under CCAL version 2.0 : see CAreadme.txt"); //$NON-NLS-1$
                        cmbLicense.select(0);
                        ev_license_cmb[0] = cmbLicense;
                    }

                    {
                        Composite cmpHelp = new Composite(grpMeta, SWT.NONE);
                        cmpHelp.setLayout(new GridLayout(2, false));

                        Label lblHelp = new Label(cmpHelp, SWT.NONE);
                        lblHelp.setText("0 !HELP "); //$NON-NLS-1$

                        Text txtHelp = new Text(cmpHelp, SWT.SEARCH);
                        txtHelp.setMessage(I18n.META_HELP);
                        ev_help_txt[0] = txtHelp;
                    }

                    {
                        Composite cmpBfc = new Composite(grpMeta, SWT.NONE);
                        cmpBfc.setLayout(new GridLayout(2, false));

                        Label lblBfc = new Label(cmpBfc, SWT.NONE);
                        lblBfc.setText("0 BFC "); //$NON-NLS-1$

                        Combo cmbBfc = new Combo(cmpBfc, SWT.NONE);
                        cmbBfc.setItems(new String[] { "NOCERTIFY", "CERTIFY CW", "CERTIFY CCW"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                        cmbBfc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                        cmbBfc.setText("NOCERTIFY"); //$NON-NLS-1$
                        cmbBfc.select(0);
                        ev_bfcHeader_cmb[0] = cmbBfc;
                    }

                    {
                        Composite cmpCategory = new Composite(grpMeta, SWT.NONE);
                        cmpCategory.setLayout(new GridLayout(2, false));

                        Label lblCategory = new Label(cmpCategory, SWT.NONE);
                        lblCategory.setText("0 !CATEGORY "); //$NON-NLS-1$

                        Combo cmbCategory = new Combo(cmpCategory, SWT.NONE);
                        ev_category_cmb[0] = cmbCategory;
                        File categoryFile = new File(AppData.getPath() + "categories.txt"); //$NON-NLS-1$
                        if (!categoryFile.exists() || !categoryFile.isFile()) {
                            categoryFile = new File("categories.txt"); //$NON-NLS-1$
                        }
                        if (categoryFile.exists() && categoryFile.isFile()) {
                            UTF8BufferedReader reader = null;
                            try {
                                ArrayList<String> categories = new ArrayList<>();
                                categories.add(""); //$NON-NLS-1$
                                reader = new UTF8BufferedReader(categoryFile.getAbsolutePath());
                                String line ;
                                while ((line = reader.readLine()) != null) {
                                    line = line.trim();
                                    if (StringHelper.isNotBlank(line)) {
                                        categories.add(line);
                                    }
                                }
                                ev_category_cmb[0].setItems(categories.toArray(new String[categories.size()]));
                            } catch (LDParsingException e) {
                                setDefaultCategories();
                            } catch (FileNotFoundException e) {
                                setDefaultCategories();
                            } catch (UnsupportedEncodingException e) {
                                setDefaultCategories();
                            } finally {
                                try {
                                    if (reader != null)
                                        reader.close();
                                } catch (LDParsingException e1) {
                                }
                            }
                        } else {
                            setDefaultCategories();
                        }

                        cmbCategory.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                        cmbCategory.setText(""); //$NON-NLS-1$
                        cmbCategory.select(0);
                    }

                    {
                        Composite cmpKeywords = new Composite(grpMeta, SWT.NONE);
                        cmpKeywords.setLayout(new GridLayout(3, false));

                        Label lblKeywords = new Label(cmpKeywords, SWT.NONE);
                        lblKeywords.setText("0 !KEYWORDS "); //$NON-NLS-1$

                        Text txtKeywords = new Text(cmpKeywords, SWT.SEARCH);
                        txtKeywords.setMessage(I18n.META_KEYWORDS_1);
                        ev_keywords_txt[0] = txtKeywords;

                        Label lblKeywords2 = new Label(cmpKeywords, SWT.NONE);
                        lblKeywords2.setText(I18n.META_KEYWORDS_2);
                    }

                    {
                        Composite cmpCmdline = new Composite(grpMeta, SWT.NONE);
                        cmpCmdline.setLayout(new GridLayout(3, false));

                        Label lblCmdline = new Label(cmpCmdline, SWT.NONE);
                        lblCmdline.setText("0 !CMDLINE "); //$NON-NLS-1$

                        Text txtCmdline = new Text(cmpCmdline, SWT.SEARCH);
                        txtCmdline.setMessage(I18n.META_COMMAND_LINE);
                        ev_cmdline_txt[0] = txtCmdline;
                    }

                    {
                        Composite cmpHistory1 = new Composite(grpMeta, SWT.NONE);
                        cmpHistory1.setLayout(new GridLayout(6, false));

                        Label lblHistory11 = new Label(cmpHistory1, SWT.NONE);
                        lblHistory11.setText("0 !HISTORY "); //$NON-NLS-1$

                        Text txtHistory11 = new Text(cmpHistory1, SWT.SEARCH);
                        txtHistory11.setMessage(I18n.META_HISTORY_1);
                        ev_history11_txt[0] = txtHistory11;

                        Label lblHistory12 = new Label(cmpHistory1, SWT.NONE);
                        lblHistory12.setText(" ["); //$NON-NLS-1$

                        Text txtHistory12 = new Text(cmpHistory1, SWT.SEARCH);
                        txtHistory12.setMessage(I18n.META_HISTORY_2);
                        ev_history12_txt[0] = txtHistory12;

                        Label lblHistory13 = new Label(cmpHistory1, SWT.NONE);
                        lblHistory13.setText("] "); //$NON-NLS-1$

                        Text txtHistory13 = new Text(cmpHistory1, SWT.SEARCH);
                        txtHistory13.setMessage(I18n.META_HISTORY_4);
                        ev_history13_txt[0] = txtHistory13;
                    }

                    {
                        Composite cmpHistory2 = new Composite(grpMeta, SWT.NONE);
                        cmpHistory2.setLayout(new GridLayout(6, false));

                        Label lblHistory21 = new Label(cmpHistory2, SWT.NONE);
                        lblHistory21.setText("or 0 !HISTORY "); //$NON-NLS-1$

                        Text txtHistory21 = new Text(cmpHistory2, SWT.SEARCH);
                        txtHistory21.setMessage(I18n.META_HISTORY_1);
                        ev_history21_txt[0] = txtHistory21;

                        Label lblHistory22 = new Label(cmpHistory2, SWT.NONE);
                        lblHistory22.setText(" {"); //$NON-NLS-1$

                        Text txtHistory22 = new Text(cmpHistory2, SWT.SEARCH);
                        txtHistory22.setMessage(I18n.META_HISTORY_3);
                        ev_history22_txt[0] = txtHistory22;

                        Label lblHistory23 = new Label(cmpHistory2, SWT.NONE);
                        lblHistory23.setText("} "); //$NON-NLS-1$

                        Text txtHistory23 = new Text(cmpHistory2, SWT.SEARCH);
                        txtHistory23.setMessage(I18n.META_HISTORY_4);
                        ev_history23_txt[0] = txtHistory23;
                    }

                    {
                        Composite cmpComment = new Composite(grpMeta, SWT.NONE);
                        cmpComment.setLayout(new GridLayout(5, false));

                        Label lblType = new Label(cmpComment, SWT.NONE);
                        lblType.setText("0 // "); //$NON-NLS-1$

                        NButton btnNeedsWork2 = new NButton(cmpComment, SWT.TOGGLE);
                        btnNeedsWork2.setText("Needs work:"); //$NON-NLS-1$
                        ev_comment_btn[0] = btnNeedsWork2;

                        Text txtComment = new Text(cmpComment, SWT.SEARCH);
                        txtComment.setMessage(I18n.META_COMMENT);
                        ev_comment_txt[0] = txtComment;
                    }
                }
            }

            final CTabItem tItem2 = new CTabItem(tabFolderSettings, SWT.NONE);
            tItem2.setText(I18n.META_BACK_FACE_CULLING);
            {

                final ScrolledComposite cmpScroll = new ScrolledComposite(tabFolderSettings, SWT.H_SCROLL | SWT.V_SCROLL);
                tItem2.setControl(cmpScroll);

                Composite cmpMetaArea = new Composite(cmpScroll, SWT.NONE);
                cmpScroll.setContent(cmpMetaArea);
                cmpScroll.setExpandHorizontal(true);
                cmpScroll.setExpandVertical(true);
                cmpScroll.setMinSize(600, 800);

                GridData gdm = new GridData();
                gdm.grabExcessHorizontalSpace = true;
                cmpScroll.setLayoutData(gdm);

                cmpMetaArea.setLayout(new GridLayout(1, false));

                {
                    Composite grpMeta = cmpMetaArea;
                    grpMeta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                    grpMeta.setLayout(new GridLayout(1, false));

                    Composite cmpBfc = new Composite(grpMeta, SWT.NONE);
                    cmpBfc.setLayout(new GridLayout(2, false));

                    Label lblBfc = new Label(cmpBfc, SWT.NONE);
                    lblBfc.setText("0 BFC "); //$NON-NLS-1$

                    Combo cmbBfc = new Combo(cmpBfc, SWT.NONE);
                    cmbBfc.setItems(new String[] { "INVERTNEXT", "NOCLIP", "CW", "CCW", "CLIP", "CLIP CW", "CLIP CCW"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
                    cmbBfc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                    cmbBfc.setText("INVERTNEXT"); //$NON-NLS-1$
                    cmbBfc.select(0);
                    ev_bfc_cmb[0] = cmbBfc;
                }
            }

            final CTabItem tItem3 = new CTabItem(tabFolderSettings, SWT.NONE);
            tItem3.setText(I18n.META_TEXTURE_MAPPING);
            {

                final ScrolledComposite cmpScroll = new ScrolledComposite(tabFolderSettings, SWT.H_SCROLL | SWT.V_SCROLL);
                tItem3.setControl(cmpScroll);

                Composite cmpMetaArea = new Composite(cmpScroll, SWT.NONE);
                cmpScroll.setContent(cmpMetaArea);
                cmpScroll.setExpandHorizontal(true);
                cmpScroll.setExpandVertical(true);
                cmpScroll.setMinSize(600, 800);

                GridData gdm = new GridData();
                gdm.grabExcessHorizontalSpace = true;
                cmpScroll.setLayoutData(gdm);

                cmpMetaArea.setLayout(new GridLayout(1, false));

                {
                    Composite grpMeta = cmpMetaArea;
                    grpMeta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                    grpMeta.setLayout(new GridLayout(1, false));
                    {
                        Composite cmpTexmap = new Composite(grpMeta, SWT.NONE);
                        cmpTexmap.setLayout(new GridLayout(14, false));

                        Label lblTexmap = new Label(cmpTexmap, SWT.NONE);
                        lblTexmap.setText("0 !TEXMAP "); //$NON-NLS-1$

                        Combo cmbTexmap = new Combo(cmpTexmap, SWT.NONE);
                        cmbTexmap.setItems(new String[] { "START", "NEXT" }); //$NON-NLS-1$ //$NON-NLS-2$
                        cmbTexmap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                        cmbTexmap.setText("START"); //$NON-NLS-1$
                        cmbTexmap.select(0);
                        ev_texmapPlanar_cmb[0] = cmbTexmap;

                        Label lblPlanar = new Label(cmpTexmap, SWT.NONE);
                        lblPlanar.setText(" PLANAR "); //$NON-NLS-1$
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_X1);
                            ev_texmapPlanar1_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_Y1);
                            ev_texmapPlanar2_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_Z1);
                            ev_texmapPlanar3_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_X2);
                            ev_texmapPlanar4_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_Y2);
                            ev_texmapPlanar5_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_Z2);
                            ev_texmapPlanar6_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_X3);
                            ev_texmapPlanar7_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_Y3);
                            ev_texmapPlanar8_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPlanar = new Text(cmpTexmap, SWT.SEARCH);
                            txtPlanar.setMessage(I18n.META_TEXTURE_Z3);
                            ev_texmapPlanar9_txt[0] = txtPlanar;
                        }
                        {
                            Text txtPng = new Text(cmpTexmap, SWT.SEARCH);
                            txtPng.setMessage(I18n.META_TEXTURE_PNG);
                            ev_texmapPlanar10_txt[0] = txtPng;
                        }
                        {
                            NButton btnBrowse = new NButton(cmpTexmap, SWT.NONE);
                            btnBrowse.setText(I18n.DIALOG_BROWSE);
                            ev_texmapPlanar_btn[0] = btnBrowse;
                        }
                    }
                    {
                        Composite cmpTexmap = new Composite(grpMeta, SWT.NONE);
                        cmpTexmap.setLayout(new GridLayout(15, false));

                        Label lblTexmap = new Label(cmpTexmap, SWT.NONE);
                        lblTexmap.setText("0 !TEXMAP "); //$NON-NLS-1$

                        Combo cmbTexmap = new Combo(cmpTexmap, SWT.NONE);
                        cmbTexmap.setItems(new String[] { "START", "NEXT" }); //$NON-NLS-1$ //$NON-NLS-2$
                        cmbTexmap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                        cmbTexmap.setText("START"); //$NON-NLS-1$
                        cmbTexmap.select(0);
                        ev_texmapCyli_cmb[0] = cmbTexmap;

                        Label lblCylindrical = new Label(cmpTexmap, SWT.NONE);
                        lblCylindrical.setText(" CYLINDRICAL "); //$NON-NLS-1$
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_X1);
                            txtCylindrical.setToolTipText(I18n.META_CYLINDER_BOTTOM_CENTER);
                            ev_texmapCyli1_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_Y1);
                            txtCylindrical.setToolTipText(I18n.META_CYLINDER_BOTTOM_CENTER);
                            ev_texmapCyli2_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_Z1);
                            txtCylindrical.setToolTipText(I18n.META_CYLINDER_BOTTOM_CENTER);
                            ev_texmapCyli3_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_X2);
                            txtCylindrical.setToolTipText(I18n.META_CYLINDER_TOP_CENTER);
                            ev_texmapCyli4_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_Y2);
                            txtCylindrical.setToolTipText(I18n.META_CYLINDER_TOP_CENTER);
                            ev_texmapCyli5_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_Z2);
                            txtCylindrical.setToolTipText(I18n.META_CYLINDER_TOP_CENTER);
                            ev_texmapCyli6_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_X3);
                            txtCylindrical.setToolTipText(I18n.META_TEXTURE_BOTTOM_CENTER);
                            ev_texmapCyli7_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_Y3);
                            txtCylindrical.setToolTipText(I18n.META_TEXTURE_BOTTOM_CENTER);
                            ev_texmapCyli8_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_Z3);
                            txtCylindrical.setToolTipText(I18n.META_TEXTURE_BOTTOM_CENTER);
                            ev_texmapCyli9_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtCylindrical = new Text(cmpTexmap, SWT.SEARCH);
                            txtCylindrical.setMessage(I18n.META_TEXTURE_ANGLE_1);
                            ev_texmapCyli10_txt[0] = txtCylindrical;
                        }
                        {
                            Text txtPng = new Text(cmpTexmap, SWT.SEARCH);
                            txtPng.setMessage(I18n.META_TEXTURE_PNG);
                            ev_texmapCyli11_txt[0] = txtPng;
                        }
                        {
                            NButton btnBrowse = new NButton(cmpTexmap, SWT.NONE);
                            btnBrowse.setText(I18n.DIALOG_BROWSE);
                            ev_texmapCyli_btn[0] = btnBrowse;
                        }
                    }
                    {
                        Composite cmpTexmap = new Composite(grpMeta, SWT.NONE);
                        cmpTexmap.setLayout(new GridLayout(16, false));

                        Label lblTexmap = new Label(cmpTexmap, SWT.NONE);
                        lblTexmap.setText("0 !TEXMAP "); //$NON-NLS-1$

                        Combo cmbTexmap = new Combo(cmpTexmap, SWT.NONE);
                        cmbTexmap.setItems(new String[] { "START", "NEXT" }); //$NON-NLS-1$ //$NON-NLS-2$
                        cmbTexmap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                        cmbTexmap.setText("START"); //$NON-NLS-1$
                        cmbTexmap.select(0);
                        ev_texmapSphere_cmb[0] = cmbTexmap;

                        Label lblSpherical = new Label(cmpTexmap, SWT.NONE);
                        lblSpherical.setText(" SPHERICAL "); //$NON-NLS-1$
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_X1);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_SPHERE_CENTER);
                            ev_texmapSphere1_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_Y1);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_SPHERE_CENTER);
                            ev_texmapSphere2_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_Z1);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_SPHERE_CENTER);
                            ev_texmapSphere3_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_X2);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_CENTER);
                            ev_texmapSphere4_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_Y2);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_CENTER);
                            ev_texmapSphere5_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_Z2);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_CENTER);
                            ev_texmapSphere6_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_X3);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_TOP_CENTER);
                            ev_texmapSphere7_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_Y3);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_TOP_CENTER);
                            ev_texmapSphere8_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_Z3);
                            txtSpherical.setToolTipText(I18n.META_TEXTURE_TOP_CENTER);
                            ev_texmapSphere9_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_ANGLE_1);
                            ev_texmapSphere10_txt[0] = txtSpherical;
                        }
                        {
                            Text txtSpherical = new Text(cmpTexmap, SWT.SEARCH);
                            txtSpherical.setMessage(I18n.META_TEXTURE_ANGLE_2);
                            ev_texmapSphere11_txt[0] = txtSpherical;
                        }
                        {
                            Text txtPng = new Text(cmpTexmap, SWT.SEARCH);
                            txtPng.setMessage(I18n.META_TEXTURE_PNG);
                            ev_texmapSphere12_txt[0] = txtPng;
                        }
                        {
                            NButton btnBrowse = new NButton(cmpTexmap, SWT.NONE);
                            btnBrowse.setText(I18n.DIALOG_BROWSE);
                            ev_texmapSphere_btn[0] = btnBrowse;
                        }
                    }
                    {
                        NButton btnTexmap = new NButton(grpMeta, SWT.NONE);
                        btnTexmap.setText("0 !TEXMAP FALLBACK"); //$NON-NLS-1$
                        ev_texmapFallback_btn[0] = btnTexmap;
                    }
                    {
                        Composite cmpTexmap = new Composite(grpMeta, SWT.NONE);
                        cmpTexmap.setLayout(new GridLayout(2, false));
                        Label lblTexmap = new Label(cmpTexmap, SWT.NONE);
                        lblTexmap.setText("0 !: "); //$NON-NLS-1$
                        {
                            Text txtMeta = new Text(cmpTexmap, SWT.SEARCH);
                            txtMeta.setMessage(I18n.META_TEXTURE_GEOM_1);
                            txtMeta.setToolTipText(I18n.META_TEXTURE_GEOM_2);
                            ev_texmapMeta_txt[0] = txtMeta;
                        }
                    }
                    {
                        NButton btnTexmap = new NButton(grpMeta, SWT.NONE);
                        btnTexmap.setText("0 !TEXMAP END"); //$NON-NLS-1$
                        ev_texmapEnd_btn[0] = btnTexmap;
                    }
                }
            }

            final CTabItem tItem4 = new CTabItem(tabFolderSettings, SWT.NONE);
            tItem4.setText(I18n.META_LPE);
            {

                final ScrolledComposite cmpScroll = new ScrolledComposite(tabFolderSettings, SWT.H_SCROLL | SWT.V_SCROLL);
                tItem4.setControl(cmpScroll);

                Composite cmpMetaArea = new Composite(cmpScroll, SWT.NONE);
                cmpScroll.setContent(cmpMetaArea);
                cmpScroll.setExpandHorizontal(true);
                cmpScroll.setExpandVertical(true);
                cmpScroll.setMinSize(600, 800);

                GridData gdm = new GridData();
                gdm.grabExcessHorizontalSpace = true;
                cmpScroll.setLayoutData(gdm);

                cmpMetaArea.setLayout(new GridLayout(1, false));

                {
                    Composite grpMeta = cmpMetaArea;
                    grpMeta.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                    grpMeta.setLayout(new GridLayout(1, false));

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(2, false));
                        Label lblTodo = new Label(cmpLpe, SWT.NONE);
                        lblTodo.setText("0 !LPE TODO "); //$NON-NLS-1$
                        {
                            Text txtTodo = new Text(cmpLpe, SWT.SEARCH);
                            txtTodo.setMessage(I18n.META_TODO);
                            ev_todo_txt[0] = txtTodo;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(4, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE VERTEX "); //$NON-NLS-1$
                        {
                            Text txtX = new Text(cmpLpe, SWT.SEARCH);
                            txtX.setMessage(I18n.META_VERTEX_X);
                            txtX.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_vertex1_txt[0] = txtX;
                        }
                        {
                            Text txtY = new Text(cmpLpe, SWT.SEARCH);
                            txtY.setMessage(I18n.META_VERTEX_Y);
                            txtY.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_vertex2_txt[0] = txtY;
                        }
                        {
                            Text txtZ = new Text(cmpLpe, SWT.SEARCH);
                            txtZ.setMessage(I18n.META_VERTEX_Z);
                            txtZ.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_vertex3_txt[0] = txtZ;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(5, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_"); //$NON-NLS-1$
                        {
                            Combo cmbCsg = new Combo(cmpLpe, SWT.NONE);
                            cmbCsg.setItems(new String[] { "UNION ", "DIFFERENCE ", "INTERSECTION "}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            cmbCsg.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                            cmbCsg.setText("UNION "); //$NON-NLS-1$
                            cmbCsg.select(0);
                            ev_csgAction_cmb[0] = cmbCsg;
                        }
                        {
                            Text txtCsgid1 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid1.setMessage(I18n.META_CSG_SOURCE_1);
                            ev_csgAction1_txt[0] = txtCsgid1;
                        }
                        {
                            Text txtCsgid2 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid2.setMessage(I18n.META_CSG_SOURCE_2);
                            ev_csgAction2_txt[0] = txtCsgid2;
                        }
                        {
                            Text txtCsgid3 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid3.setMessage(I18n.META_CSG_TARGET_1);
                            ev_csgAction3_txt[0] = txtCsgid3;
                        }
                    }
                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(16, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_"); //$NON-NLS-1$
                        {
                            Combo cmbCsg = new Combo(cmpLpe, SWT.NONE);
                            cmbCsg.setItems(new String[] { "CUBOID ", "ELLIPSOID ", "QUAD ", "CYLINDER ", "CONE ", "CIRCLE ", "MESH ", "EXTRUDE "}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
                            cmbCsg.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
                            cmbCsg.setText("CUBOID "); //$NON-NLS-1$
                            cmbCsg.select(0);
                            ev_csgBody_cmb[0] = cmbCsg;
                        }
                        {
                            Text txtCsgid1 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid1.setMessage(I18n.META_CSG_UNIQUE);
                            txtCsgid1.setToolTipText(I18n.META_CSG_UNIQUE_HINT);
                            ev_csgBody1_txt[0] = txtCsgid1;
                        }
                        {
                            Text txtCsgid2 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid2.setMessage(I18n.META_COLOUR);
                            txtCsgid2.setToolTipText(I18n.META_COLOUR_HINT);
                            ev_csgBody2_txt[0] = txtCsgid2;

                        }
                        {
                            Text txtX = new Text(cmpLpe, SWT.SEARCH);
                            txtX.setMessage(I18n.META_VERTEX_X);
                            txtX.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_csgBody3_txt[0] = txtX;
                        }
                        {
                            Text txtY = new Text(cmpLpe, SWT.SEARCH);
                            txtY.setMessage(I18n.META_VERTEX_Y);
                            txtY.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_csgBody4_txt[0] = txtY;
                        }
                        {
                            Text txtZ = new Text(cmpLpe, SWT.SEARCH);
                            txtZ.setMessage(I18n.META_VERTEX_Z);
                            txtZ.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_csgBody5_txt[0] = txtZ;
                        }

                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M00);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody6_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M01);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody7_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M02);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody8_txt[0] = txtM;
                        }

                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M10);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody9_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M11);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody10_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M12);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody11_txt[0] = txtM;
                        }

                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M20);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody12_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M21);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody13_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M22);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgBody14_txt[0] = txtM;
                        }
                    }
                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(16, false));
                        Label lblCsgTransform = new Label(cmpLpe, SWT.NONE);
                        lblCsgTransform.setText("0 !LPE CSG_TRANSFORM "); //$NON-NLS-1$
                        {
                            Text txtCsgid1 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid1.setMessage(I18n.META_CSG_SOURCE_1);
                            txtCsgid1.setToolTipText(I18n.META_CSG_UNIQUE_HINT);
                            ev_csgTrans1_txt[0] = txtCsgid1;
                        }
                        {
                            Text txtCsgid2 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid2.setMessage(I18n.META_CSG_TARGET_1);
                            txtCsgid2.setToolTipText(I18n.META_CSG_UNIQUE_HINT);
                            ev_csgTrans2_txt[0] = txtCsgid2;

                        }
                        {
                            Text txtCsgcol = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgcol.setMessage(I18n.META_COLOUR);
                            txtCsgcol.setToolTipText(I18n.META_COLOUR_HINT);
                            ev_csgTrans3_txt[0] = txtCsgcol;
                        }
                        {
                            Text txtX = new Text(cmpLpe, SWT.SEARCH);
                            txtX.setMessage(I18n.META_VERTEX_X);
                            txtX.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_csgTrans4_txt[0] = txtX;
                        }
                        {
                            Text txtY = new Text(cmpLpe, SWT.SEARCH);
                            txtY.setMessage(I18n.META_VERTEX_Y);
                            txtY.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_csgTrans5_txt[0] = txtY;
                        }
                        {
                            Text txtZ = new Text(cmpLpe, SWT.SEARCH);
                            txtZ.setMessage(I18n.META_VERTEX_Z);
                            txtZ.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_csgTrans6_txt[0] = txtZ;
                        }

                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M00);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans7_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M01);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans8_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M02);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans9_txt[0] = txtM;
                        }

                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M10);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans10_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M11);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans11_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M12);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans12_txt[0] = txtM;
                        }

                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M20);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans13_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M21);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans14_txt[0] = txtM;
                        }
                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_M22);
                            txtM.setToolTipText(I18n.META_TRANS_MATRIX);
                            ev_csgTrans15_txt[0] = txtM;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(16, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_EXT_CFG "); //$NON-NLS-1$
                        {
                            Text txtCsgid1 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid1.setMessage(I18n.META_CSG_EXTRUDE_1_A);
                            txtCsgid1.setToolTipText(I18n.META_CSG_EXTRUDE_1_B);
                            ev_csgEx1_txt[0] = txtCsgid1;
                        }
                        {
                            Text txtCsgid2 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid2.setMessage(I18n.META_CSG_EXTRUDE_2_A);
                            txtCsgid2.setToolTipText(I18n.META_CSG_EXTRUDE_2_B);
                            ev_csgEx2_txt[0] = txtCsgid2;

                        }
                        {
                            Text txtCsgcol = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgcol.setMessage(I18n.META_CSG_EXTRUDE_3_A);
                            txtCsgcol.setToolTipText(I18n.META_CSG_EXTRUDE_3_B);
                            ev_csgEx3_txt[0] = txtCsgcol;

                        }
                        {
                            Text txtX = new Text(cmpLpe, SWT.SEARCH);
                            txtX.setMessage(I18n.META_CSG_EXTRUDE_4_A);
                            txtX.setToolTipText(I18n.META_CSG_EXTRUDE_4_B);
                            ev_csgEx4_txt[0] = txtX;
                        }
                        {
                            Text txtY = new Text(cmpLpe, SWT.SEARCH);
                            txtY.setMessage(I18n.META_CSG_EXTRUDE_5_A);
                            txtY.setToolTipText(I18n.META_CSG_EXTRUDE_5_B);
                            ev_csgEx5_txt[0] = txtY;
                        }
                        {
                            Text txtZ = new Text(cmpLpe, SWT.SEARCH);
                            txtZ.setMessage(I18n.META_CSG_EXTRUDE_6_A);
                            txtZ.setToolTipText(I18n.META_CSG_EXTRUDE_6_B);
                            ev_csgEx6_txt[0] = txtZ;
                        }

                        {
                            Text txtM = new Text(cmpLpe, SWT.SEARCH);
                            txtM.setMessage(I18n.META_CSG_EXTRUDE_7_A);
                            txtM.setToolTipText(I18n.META_CSG_EXTRUDE_7_B);
                            ev_csgEx7_txt[0] = txtM;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(2, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_COMPILE"); //$NON-NLS-1$
                        {
                            Text txtCsgid1 = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgid1.setMessage(I18n.META_CSG_SOURCE_3);
                            txtCsgid1.setToolTipText(I18n.META_CSG_COMPILE);
                            ev_csgCompile_txt[0] = txtCsgid1;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(2, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_QUALITY"); //$NON-NLS-1$
                        {
                            Text txtCsgQuality = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgQuality.setMessage(I18n.META_QUALITY);
                            ev_csgQuality_txt[0] = txtCsgQuality;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(2, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_EPSILON"); //$NON-NLS-1$
                        {
                            Text txtCsgEpsilon = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgEpsilon.setMessage(I18n.META_CSG_EPSILON_1);
                            txtCsgEpsilon.setToolTipText(I18n.META_CSG_EPSILON_2);
                            ev_csgEpsilon_txt[0] = txtCsgEpsilon;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(2, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_TJUNCTION_EPSILON"); //$NON-NLS-1$
                        {
                            Text txtCsgTJunctionEpsilon = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgTJunctionEpsilon.setMessage(I18n.META_CSG_JUNCTION_EPSILON_1);
                            txtCsgTJunctionEpsilon.setToolTipText(I18n.META_CSG_JUNCTION_EPSILON_2);
                            ev_csgTJunctionEpsilon_txt[0] = txtCsgTJunctionEpsilon;
                        }
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(2, false));
                        Label lblVertex = new Label(cmpLpe, SWT.NONE);
                        lblVertex.setText("0 !LPE CSG_EDGE_COLLAPSE_EPSILON"); //$NON-NLS-1$
                        {
                            Text txtCsgEdgeCollapseEpsilon = new Text(cmpLpe, SWT.SEARCH);
                            txtCsgEdgeCollapseEpsilon.setMessage(I18n.META_CSG_COLLAPSE_1);
                            txtCsgEdgeCollapseEpsilon.setToolTipText(I18n.META_CSG_COLLAPSE_2);
                            ev_csgEdgeCollapseEpsilon_txt[0] = txtCsgEdgeCollapseEpsilon;
                        }
                    }

                    {
                        NButton btnDontoptimize = new NButton(grpMeta, SWT.NONE);
                        btnDontoptimize.setText("0 !LPE CSG_DONT_OPTIMISE"); //$NON-NLS-1$
                        ev_csgDontOptimize_btn[0] = btnDontoptimize;
                    }

                    {
                        Composite cmpLpe = new Composite(grpMeta, SWT.NONE);
                        cmpLpe.setLayout(new GridLayout(11, false));
                        Label lblPng = new Label(cmpLpe, SWT.NONE);
                        lblPng.setText("0 !LPE PNG "); //$NON-NLS-1$
                        {
                            Text txtX = new Text(cmpLpe, SWT.SEARCH);
                            txtX.setMessage(I18n.META_VERTEX_X);
                            txtX.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png1_txt[0] = txtX;
                        }
                        {
                            Text txtY = new Text(cmpLpe, SWT.SEARCH);
                            txtY.setMessage(I18n.META_VERTEX_Y);
                            txtY.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png2_txt[0] = txtY;
                        }
                        {
                            Text txtZ = new Text(cmpLpe, SWT.SEARCH);
                            txtZ.setMessage(I18n.META_VERTEX_Z);
                            txtZ.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png3_txt[0] = txtZ;
                        }

                        {
                            Text txtX = new Text(cmpLpe, SWT.SEARCH);
                            txtX.setMessage(I18n.META_ROTATION_X);
                            txtX.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png4_txt[0] = txtX;
                        }
                        {
                            Text txtY = new Text(cmpLpe, SWT.SEARCH);
                            txtY.setMessage(I18n.META_ROTATION_Y);
                            txtY.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png5_txt[0] = txtY;
                        }
                        {
                            Text txtZ = new Text(cmpLpe, SWT.SEARCH);
                            txtZ.setMessage(I18n.META_ROTATION_Z);
                            txtZ.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png6_txt[0] = txtZ;
                        }

                        {
                            Text txtX = new Text(cmpLpe, SWT.SEARCH);
                            txtX.setMessage(I18n.META_SCALE_X);
                            txtX.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png7_txt[0] = txtX;
                        }
                        {
                            Text txtY = new Text(cmpLpe, SWT.SEARCH);
                            txtY.setMessage(I18n.META_SCALE_Y);
                            txtY.setToolTipText(I18n.META_DECIMAL_MARK);
                            ev_png8_txt[0] = txtY;
                        }
                        {
                            Text txtPng = new Text(cmpLpe, SWT.SEARCH);
                            txtPng.setMessage(I18n.META_TEXTURE_PNG);
                            ev_png9_txt[0] = txtPng;
                        }
                        {
                            NButton btnBrowse = new NButton(cmpLpe, SWT.NONE);
                            btnBrowse.setText(I18n.DIALOG_BROWSE);
                            ev_png_btn[0] = btnBrowse;
                        }
                    }
                }
            }

        }

        Label lblOnlyFor3D = new Label(container, SWT.NONE);
        lblOnlyFor3D.setText(I18n.META_NEW_LINE_NOTE);

        Label lblPreview = new Label(container, SWT.BORDER);
        lblPreview.setText("0 BFC CERTIFY CCW"); //$NON-NLS-1$
        lbl_lineToInsert[0] = lblPreview;
        GridData gdl = new GridData();
        gdl.horizontalAlignment = SWT.CENTER;
        lblPreview.setLayoutData(gdl);


        NButton btnCreate = new NButton(container, SWT.NONE);
        btnCreate.setText(I18n.DIALOG_CREATE_META_COMMAND);
        GridData gdt = new GridData();
        gdt.horizontalAlignment = SWT.RIGHT;
        btnCreate.setLayoutData(gdt);
        this.btn_Create[0] = btnCreate;

        return container;
    }

    private void setDefaultCategories() {
        ev_category_cmb[0].setItems(new String[] {
                "", //$NON-NLS-1$
                "Animal", //$NON-NLS-1$
                "Antenna", //$NON-NLS-1$
                "Arch", //$NON-NLS-1$
                "Arm", //$NON-NLS-1$
                "Bar", //$NON-NLS-1$
                "Baseplate", //$NON-NLS-1$
                "Belville", //$NON-NLS-1$
                "Boat", //$NON-NLS-1$
                "Bracket", //$NON-NLS-1$
                "Brick", //$NON-NLS-1$
                "Canvas", //$NON-NLS-1$
                "Car", //$NON-NLS-1$
                "Clikits", //$NON-NLS-1$
                "Cockpit", //$NON-NLS-1$
                "Cone", //$NON-NLS-1$
                "Container", //$NON-NLS-1$
                "Conveyor", //$NON-NLS-1$
                "Crane", //$NON-NLS-1$
                "Cylinder", //$NON-NLS-1$
                "Dish", //$NON-NLS-1$
                "Door", //$NON-NLS-1$
                "Electric", //$NON-NLS-1$
                "Exhaust", //$NON-NLS-1$
                "Fence", //$NON-NLS-1$
                "Figure", //$NON-NLS-1$
                "Figure Accessory", //$NON-NLS-1$
                "Flag", //$NON-NLS-1$
                "Forklift", //$NON-NLS-1$
                "Freestyle", //$NON-NLS-1$
                "Garage", //$NON-NLS-1$
                "Glass", //$NON-NLS-1$
                "Grab", //$NON-NLS-1$
                "Hinge", //$NON-NLS-1$
                "Homemaker", //$NON-NLS-1$
                "Hose", //$NON-NLS-1$
                "Ladder", //$NON-NLS-1$
                "Lever", //$NON-NLS-1$
                "Magnet", //$NON-NLS-1$
                "Minifig", //$NON-NLS-1$
                "Minifig Accessory", //$NON-NLS-1$
                "Minifig Footwear", //$NON-NLS-1$
                "Minifig Headwear", //$NON-NLS-1$
                "Minifig Hipwear", //$NON-NLS-1$
                "Minifig Neckwear", //$NON-NLS-1$
                "Monorail", //$NON-NLS-1$
                "Panel", //$NON-NLS-1$
                "Plane", //$NON-NLS-1$
                "Plant", //$NON-NLS-1$
                "Plate", //$NON-NLS-1$
                "Platform", //$NON-NLS-1$
                "Propellor", //$NON-NLS-1$
                "Rack", //$NON-NLS-1$
                "Roadsign", //$NON-NLS-1$
                "Rock", //$NON-NLS-1$
                "Scala", //$NON-NLS-1$
                "Screw", //$NON-NLS-1$
                "Sheet", //$NON-NLS-1$
                "Slope", //$NON-NLS-1$
                "Sphere", //$NON-NLS-1$
                "Staircase", //$NON-NLS-1$
                "Sticker", //$NON-NLS-1$
                "Support", //$NON-NLS-1$
                "Tail", //$NON-NLS-1$
                "Tap", //$NON-NLS-1$
                "Technic", //$NON-NLS-1$
                "Tile", //$NON-NLS-1$
                "Tipper", //$NON-NLS-1$
                "Tractor", //$NON-NLS-1$
                "Trailer", //$NON-NLS-1$
                "Train", //$NON-NLS-1$
                "Turntable", //$NON-NLS-1$
                "Tyre", //$NON-NLS-1$
                "Vehicle", //$NON-NLS-1$
                "Wedge", //$NON-NLS-1$
                "Wheel", //$NON-NLS-1$
                "Winch", //$NON-NLS-1$
                "Window", //$NON-NLS-1$
                "Windscreen", //$NON-NLS-1$
                "Wing", //$NON-NLS-1$
                "Znap", //$NON-NLS-1$
        });
    }

    /**
     * Return the initial size of the window.
     */
    @Override
    protected Point getInitialSize() {
        return super.getInitialSize();
    }
}
