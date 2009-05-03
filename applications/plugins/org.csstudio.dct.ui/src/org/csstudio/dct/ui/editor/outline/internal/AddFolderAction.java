package org.csstudio.dct.ui.editor.outline.internal;

import java.util.List;

import org.csstudio.dct.model.IElement;
import org.csstudio.dct.model.IFolder;
import org.csstudio.dct.model.commands.AddFolderCommand;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.PlatformUI;

/**
 * Action that adds a folder.
 * 
 * @author Sven Wende
 * 
 */
public final class AddFolderAction extends AbstractOutlineAction {

	/**
	 *{@inheritDoc}
	 */
	@Override
	protected Command createCommand(List<IElement> selection) {
		assert selection!=null;
		assert selection.size()==1;
		assert selection.get(0) instanceof IFolder;
		
		Command result = null;

		if (selection.size()==1 && selection.get(0) instanceof IFolder) {
			InputDialog dialog = new InputDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Enter Folder Name",
					"Please enter a name for the new folder:", "", new IInputValidator() {
						public String isValid(String newText) {
							return null;
						}
					});

			if (dialog.open() == InputDialog.OK) {
				String name = dialog.getValue();
				result = new AddFolderCommand((IFolder) selection.get(0), name);
			}
		}

		return result;
	}

}
