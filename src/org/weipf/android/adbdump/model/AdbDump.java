package org.weipf.android.adbdump.model;

import java.io.IOException;
import java.io.InputStream;

public class AdbDump {
	
	private static AdbDump mInstance = null;
	
	private String deviceId = null;
	
	public enum DumpType {
		ACTIVITES,
		PROCESSES,
		SERVICES,
		MEMINFO
	}
	
	private AdbDump() {
		// TODO Auto-generated constructor stub
	}
	
	public static AdbDump getInstance() {
		if (mInstance == null) {
			mInstance = new AdbDump();
		}
		return mInstance;
	}
	
	public void setDeviceId(String deviceId) {
	    this.deviceId = deviceId;
	}
	
	public void dump(DumpType type, DumpReader reader) {
		switch (type) {
		case ACTIVITES:
			dumpActivities(reader);
			break;
		case PROCESSES:
			break;
		case SERVICES:
			break;
		case MEMINFO:
			break;
			default:
				break;
		}
		
	}
	
	public void getConnectedDevices(DumpReader reader) {
	    Runtime runtime = Runtime.getRuntime();
        InputStream stream = null;
        Process process = null;
        try {
            process = runtime.exec("adb devices");
            // sleep to make process.getErrorStream() to return an available stream.
            // do not know why this is necessary.
            Thread.sleep(200);
            stream = process.getErrorStream();
            System.out.println("getConnectedDevices/error stream:" + process.getErrorStream()
                    + " available:" + process.getErrorStream().available());
            if (stream != null && stream.available() > 0) {
                System.out.println("getConnectedDevices/found error");
                reader.readErrorDump(stream);
            } else {
                stream = process.getInputStream();
                System.out.println("getConnectedDevices/got result:" + process.getInputStream()
                        + " available:" + process.getInputStream().available());
                reader.readDump(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	private void dumpActivities(DumpReader reader) {
		Runtime runtime = Runtime.getRuntime();
		InputStream stream = null;
		Process process = null;
		try {
		    String cmd = "adb shell dumpsys activity activities";
		    if (deviceId != null) {
		        cmd = "adb -s " + deviceId + " shell dumpsys activity activities";
		    }
			process = runtime.exec(cmd);
			// sleep to make process.getErrorStream() to return an available stream.
			// do not know why this is necessary.
			Thread.sleep(200);
			stream = process.getErrorStream();
			System.out.println("dumpActivities/error stream:" + process.getErrorStream()
					+ " available:" + process.getErrorStream().available());
			if (stream != null && stream.available() > 0) {
				System.out.println("dumpActivities/found error");
				reader.readErrorDump(stream);
			} else {
				stream = process.getInputStream();
				System.out.println("dumpActivities/got result:" + process.getInputStream()
						+ " available:" + process.getInputStream().available());
				reader.readDump(stream);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stream.close();
				process.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
