/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.display.pvtable.ui;

import org.csstudio.display.pvtable.Messages;
import org.csstudio.display.pvtable.model.PVTableModel;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;

/** {@link Action} to save value snapshots
 *  @author Kay Kasemir
 */
public class SnapshotAction extends PVTableAction {
    public SnapshotAction(final TableViewer viewer) {
        super(Messages.Snapshot, "icons/snapshot.png", viewer); //$NON-NLS-1$
        setToolTipText(Messages.Snapshot_TT);
    }

    @Override
    public void run() {
        final PVTableModel model = (PVTableModel) viewer.getInput();
        if (model == null) {
            return;
        }
        model.save();
    }
}
