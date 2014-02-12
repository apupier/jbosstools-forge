package org.jboss.tools.forge.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.jboss.tools.forge.ui.util.ForgeHelper;

public class StartHandler extends AbstractHandler {

	public Object execute(ExecutionEvent executionEvent) {
		ForgeHelper.startForge();
		return null;		
	}
	
}