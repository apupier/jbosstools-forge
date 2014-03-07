package org.jboss.tools.aesh.core.internal.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jboss.tools.aesh.core.ansi.ControlSequence;
import org.jboss.tools.aesh.core.internal.ansi.ControlSequenceFactory;
import org.jboss.tools.aesh.core.internal.ansi.DefaultControlSequenceFactory;

public abstract class ControlSequenceOutputStream extends FilterOutputStream {
	
	private OutputStream target = null;
	private StringBuffer escapeSequence = new StringBuffer();
	private StringBuffer targetBuffer = new StringBuffer();
	
	private ControlSequenceFactory controlSequenceFactory = DefaultControlSequenceFactory.INSTANCE;
	
	public ControlSequenceOutputStream(OutputStream out) {
		super(out);
		this.target = out;
	}
	
	public abstract void onControlSequence(ControlSequence controlSequence);

	@Override
	public void write(int i) throws IOException {
		outputAvailable(new String( new char[] { (char)i }));
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		outputAvailable(new String(b).substring(off, off + len));
	}
	
	private void outputAvailable(String output) throws IOException {
		for (int i = 0; i < output.length(); i++) {
			charAppended(output.charAt(i));
		}
		if (targetBuffer.length() > 0) {
			String targetString = targetBuffer.toString();
			targetBuffer.setLength(0);
			target.write(targetString.getBytes());
		}
	}
	
	private void charAppended(char c) throws IOException {
		if (c == 27 && escapeSequence.length() == 0) {
			if (targetBuffer.length() > 0) {
				String targetString = targetBuffer.toString();
				targetBuffer.setLength(0);
				target.write(targetString.getBytes());
			}
			escapeSequence.append(c);
		} else if (c == '[' && escapeSequence.length() == 1) {
			escapeSequence.append(c);
		} else if (escapeSequence.length() > 1) {
			escapeSequence.append(c);
			ControlSequence controlSequence = controlSequenceFactory.create(escapeSequence.toString());
			if (controlSequence != null) {
				escapeSequence.setLength(0);
				onControlSequence(controlSequence);
			}
		} else {
			targetBuffer.append(c);
		}
	}
	
	void setControlSequenceFactory(ControlSequenceFactory controlSequenceFactory) {
		this.controlSequenceFactory = controlSequenceFactory;
	}
	
	ControlSequenceFactory getControlSequenceFactory() {
		return controlSequenceFactory;
	}
	
}