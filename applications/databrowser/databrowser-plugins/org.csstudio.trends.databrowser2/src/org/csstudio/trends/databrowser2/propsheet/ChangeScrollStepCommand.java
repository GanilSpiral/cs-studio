/*******************************************************************************
 * Copyright (c) 2014 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.trends.databrowser2.propsheet;

import java.time.Duration;
import java.util.logging.Level;

import org.csstudio.swt.rtplot.undo.UndoableAction;
import org.csstudio.swt.rtplot.undo.UndoableActionManager;
import org.csstudio.trends.databrowser2.Activator;
import org.csstudio.trends.databrowser2.Messages;
import org.csstudio.trends.databrowser2.model.Model;

/** Undo-able command to change scroll step size
 *  @author Kay Kasemir
 */
@SuppressWarnings("nls")
public class ChangeScrollStepCommand extends UndoableAction
{
    final private Model model;
    final private Duration old_step, new_step;

    /** Register and perform the command
     *  @param item Model item to configure
     *  @param operations_manager OperationsManager where command will be reg'ed
     *  @param period New value
     */
    public ChangeScrollStepCommand(final Model model,
            final UndoableActionManager operations_manager,
            final Duration step)
    {
        super(Messages.ScrollStepLbl);
        this.model = model;
        this.old_step = model.getScrollStep();
        this.new_step = step;
        operations_manager.execute(this);
    }

    /** {@inheritDoc} */
    @Override
    public void run()
    {
        set(new_step);
    }

    /** {@inheritDoc} */
    @Override
    public void undo()
    {
        set(old_step);
    }

    private void set(final Duration step)
    {
        try
        {
            model.setScrollStep(step);
        }
        catch (Exception ex)
        {
            Activator.getLogger().log(Level.WARNING, "Failed to update scroll step", ex);
        }
    }
}
