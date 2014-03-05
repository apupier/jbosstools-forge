package org.jboss.tools.aesh.core.internal.ansi;

import org.jboss.tools.aesh.core.ansi.AnsiDocument;


public class EraseInLine extends AbstractControlSequence {

	public EraseInLine(String arguments) {}

	@Override
	public ControlSequenceType getType() {
		return ControlSequenceType.ERASE_IN_LINE;
	}
	
	@Override
	public void handle(AnsiDocument document) {
		document.replace(
				document.getCursorOffset(), 
				document.getLength() - document.getCursorOffset(), 
				"");
		document.setDefaultStyleRange();
	}

}