package org.csstudio.dct.ui.editor.copyandpaste;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.csstudio.dct.model.IContainer;
import org.csstudio.dct.model.IElement;
import org.csstudio.dct.model.IFolder;
import org.csstudio.dct.model.IInstance;
import org.csstudio.dct.model.IProject;
import org.csstudio.dct.model.IPrototype;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

/**
 * Copy & Paste strategy for prototypes.
 *
 * @author Sven Wende
 *
 */
public final class PrototypeCopyAndPasteStrategy extends BaseCopyAndPasteStrategy {

    /**
     *{@inheritDoc}
     */
    public Command createPasteCommand(List<IElement> copiedElements, IProject project, List<IElement> selectedElements) {
        assert copiedElements != null;
        assert project != null;
        assert selectedElements != null;

        Map<UUID, IPrototype> tmpPrototypes = new HashMap<UUID, IPrototype>();

        CompoundCommand cmd = new CompoundCommand();

        for (IElement c : selectedElements) {
            assert c instanceof IFolder;

            for (IElement p : copiedElements) {
                if (p instanceof IPrototype) {
                    chainPrototype((IPrototype) p, cmd, tmpPrototypes, project, (IFolder) c);
                } else {
                    chainInstance((IInstance) p, cmd, tmpPrototypes, project, (IFolder) c, null);
                }
            }
        }

        return cmd;
    }

    /**
     *{@inheritDoc}
     */
    public List<Serializable> createCopyElements(List<IElement> selectedElements) {
        Set<IContainer> items = new HashSet<IContainer>();

        for (IElement e : selectedElements) {
            assert e instanceof IContainer;
            IContainer container = (IContainer) e;
            items.add(container);
        }

        return new ArrayList<Serializable>(items);
    }

    /**
     *{@inheritDoc}
     */
    public boolean canCopy(List<IElement> selectedElements) {
        boolean result = false;

        if (!selectedElements.isEmpty()) {
            result = true;
            for (IElement e : selectedElements) {
                result &= e instanceof IPrototype;
            }
        }

        return result;
    }

    /**
     *{@inheritDoc}
     */
    public boolean canPaste(List<IElement> selectedElements) {
        boolean result = false;

        if (!selectedElements.isEmpty()) {
            result = true;
            for (IElement e : selectedElements) {
                result &= (e instanceof IFolder);
            }
        }

        return result;
    }

    /**
     *{@inheritDoc}
     */
    public String getContentDescription() {
        return "Prototypes";
    }

}
