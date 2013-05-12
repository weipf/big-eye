package org.weipf.android.adbdump.model;

import java.io.InputStream;

public interface DumpReader {
	public void readDump(InputStream inpuStream);
	
	public void readErrorDump(InputStream inputStream);
}
