package org.csstudio.config.savevalue.ui;
import org.csstudio.platform.data.IDoubleValue;
import org.csstudio.platform.data.ILongValue;
import org.csstudio.platform.data.INumericMetaData;
import org.csstudio.platform.data.IStringValue;
import org.csstudio.platform.data.IValue;
import org.csstudio.platform.model.IProcessVariableWithSamples;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/*
 * Copyright (c) 2008 Stiftung Deutsches Elektronen-Synchrotron,
 * Member of the Helmholtz Association, (DESY), HAMBURG, GERMANY.
 *
 * THIS SOFTWARE IS PROVIDED UNDER THIS LICENSE ON AN "../AS IS" BASIS.
 * WITHOUT WARRANTY OF ANY KIND, EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
 * THE USE OR OTHER DEALINGS IN THE SOFTWARE. SHOULD THE SOFTWARE PROVE DEFECTIVE
 * IN ANY RESPECT, THE USER ASSUMES THE COST OF ANY NECESSARY SERVICING, REPAIR OR
 * CORRECTION. THIS DISCLAIMER OF WARRANTY CONSTITUTES AN ESSENTIAL PART OF THIS LICENSE.
 * NO USE OF ANY SOFTWARE IS AUTHORIZED HEREUNDER EXCEPT UNDER THIS DISCLAIMER.
 * DESY HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 * THE FULL LICENSE SPECIFYING FOR THE SOFTWARE THE REDISTRIBUTION, MODIFICATION,
 * USAGE AND OTHER RIGHTS AND OBLIGATIONS IS INCLUDED WITH THE DISTRIBUTION OF THIS
 * PROJECT IN THE FILE LICENSE.HTML. IF THE LICENSE IS NOT INCLUDED YOU MAY FIND A COPY
 * AT HTTP://WWW.DESY.DE/LEGAL/LICENSE.HTM
 */

/**
 * Handler for the save value command.
 * 
 * @author Joerg Rathlev
 */
public class SaveValueCommandHandler extends AbstractHandler {
	
	/**
	 * The id of the PV parameter.
	 */
	private static final String PV_PARAM_ID = "org.csstudio.platform.ui.commands.saveValue.pv";
	
	/**
	 * The id of the value paramter.
	 */
	private static final String VALUE_PARAM_ID = "org.csstudio.platform.ui.commands.saveValue.value";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Object execute(final ExecutionEvent event) throws ExecutionException {
		String pv;
		String value;
		
		// The command can either be called with parameters or on a selection.
		// If the command is called with parameters, the parameters are used,
		// otherwise, the pv and value are determined based on the selection on
		// which the command was executed.
		pv = event.getParameter(PV_PARAM_ID);
		if (pv != null) {
			// called with paramters
			value = event.getParameter(VALUE_PARAM_ID);
			if (value == null) {
				throw new ExecutionException("Invalid parameterization: no value parameter provided");
			}
		} else {
			// called on a selection
			ISelection selection = HandlerUtil.getActiveMenuSelectionChecked(event);
			IProcessVariableWithSamples pvWithSamples = getSelectedProcessVariable(selection);
			pv = pvWithSamples.getName();
			try {
				IValue iv = pvWithSamples.getSample(0);
				value = valueToString(iv);
			} catch (IllegalStateException e) {
				// This happens if the _pv is actually a TextInputEditPart with
				// the value type set to "double", but the text input cannot be
				// parsed as a double value.
				MessageDialog.openError(null, Messages.SaveValueDialog_DIALOG_TITLE, Messages.SaveValueDialog_ERRMSG_TEXT_IS_NOT_A_DOUBLE);
				return null;
			}
		}
		
		SaveValueDialog dialog = new SaveValueDialog(null, pv, value);
		dialog.open();
		
		return null;
	}

	/**
	 * Converts the given value into a string representation suitable for
	 * writing into a CA file.
	 * 
	 * @param value
	 *            the value.
	 * @return the string representation of the value.
	 */
	private String valueToString(final IValue value) {
		
		// TODO: replace with ValueUtil#getString ?
		
		if (value instanceof IStringValue) {
			return ((IStringValue) value).getValue();
		} else if (value instanceof IDoubleValue) {
			IDoubleValue idv = (IDoubleValue) value;
			double dv = idv.getValue();
			int precision = ((INumericMetaData) idv.getMetaData()).getPrecision();
			return SaveValueClient.formatForCaFile(dv, precision);
		} else if (value instanceof ILongValue) {
			ILongValue lv = (ILongValue) value;
			return Long.toString(lv.getValue());
		} else {
			return value.format();
		}
	}

	/**
	 * Returns the selected <code>IProcessVariableWithSamples</code>.
	 * 
	 * @param selection
	 *            the selection.
	 * @return the selected <code>IProcessVariableWithSamples</code>, or
	 *         <code>null</code> if the selected object does not implement
	 *         <code>IProcessVariableWithSamples</code> or if the selection is
	 *         empty or not a structured selection.
	 */
	private IProcessVariableWithSamples getSelectedProcessVariable(final ISelection selection) {
		if (!(selection instanceof IStructuredSelection)) {
			return null;
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) selection;
		Object selectedObject = structuredSelection.getFirstElement();
		if (selectedObject instanceof IProcessVariableWithSamples) {
			return (IProcessVariableWithSamples) selectedObject;
		} else if (selectedObject instanceof IAdaptable) {
			return (IProcessVariableWithSamples) ((IAdaptable) selectedObject)
					.getAdapter(IProcessVariableWithSamples.class);
		} else {
			return null;
		}
	}
}
