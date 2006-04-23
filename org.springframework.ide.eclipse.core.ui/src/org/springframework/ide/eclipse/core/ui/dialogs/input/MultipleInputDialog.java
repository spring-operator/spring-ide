/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.ide.eclipse.core.ui.dialogs.input;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.springframework.ide.eclipse.core.ui.fields.StringDialogField;

/**
 * A simple input dialog with one field.
 * 
 * @author Pierre-Antoine Grégoire
 * 
 */
public class MultipleInputDialog extends AbstractInputDialog {
	private final class FieldModifyListener implements ModifyListener {
		private String fieldId;

		public FieldModifyListener(String fieldId) {
			this.fieldId = fieldId;
		}

		public void modifyText(ModifyEvent e) {
			results.put(fieldId, ((StringDialogField) fields.get(fieldId))
					.getText());
		}
	}

	private Map results;

	private Map fields;

	private String[] fieldLabels;

	public Map getResults() {
		return results;
	}

	private StringDialogField stringDialogField;

	public MultipleInputDialog(Shell parentShell, String dialogTitle,
			String dialogSubTitle, Image dialogTitleImage,
			String dialogMessage, String[] fieldLabels) {
		super(parentShell, dialogTitle, dialogSubTitle, dialogTitleImage,
				dialogMessage);
		results = new HashMap();
		fields = new HashMap();
		this.fieldLabels = fieldLabels;
		for (int i = 0; i < fieldLabels.length; i++) {
			results.put(fieldLabels[i], "");
		}
	}

	protected void createInputPart(Composite composite) {
		Composite container = getFormToolkit().createComposite(composite,
				SWT.WRAP);
		GridData data = new GridData(SWT.FILL,SWT.FILL,true,true);
		data.widthHint=300;
		container.setLayoutData(data);
		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth=true;
		container.setLayout(layout);
		for (int i = 0; i < fieldLabels.length; i++) {
			stringDialogField = new StringDialogField(getFormToolkit());
			fields.put(fieldLabels[i], stringDialogField);
			stringDialogField.setLabelText(fieldLabels[i]);
			stringDialogField.doFillIntoTable(container, 2);
			stringDialogField.getTextControl(null).addModifyListener(
					new FieldModifyListener(fieldLabels[i]));
		}
	}

}
