package com.traelo.delivery.util;

public class Util {
	public static String getImageMimeType(byte[] data) {
		if (data.length >= 3 && data[0] == (byte) 0xFF && data[1] == (byte) 0xD8 && data[2] == (byte) 0xFF) {
			return "image/jpeg";
		}

		if (data.length >= 8 && data[0] == (byte) 0x89 && data[1] == (byte) 0x50 && data[2] == (byte) 0x4E
				&& data[3] == (byte) 0x47 && data[4] == (byte) 0x0D && data[5] == (byte) 0x0A && data[6] == (byte) 0x1A
				&& data[7] == (byte) 0x0A) {
			return "image/png";
		}

		return null;
	}

}
