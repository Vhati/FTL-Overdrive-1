package com.ftloverdrive;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;


public class Archive {

	private HashMap<String, InnerFileInfo> innerFilesMap = new HashMap<String, InnerFileInfo>();
	private File datFile;

	public Archive(File datFile) throws IOException {
		this.datFile = datFile;
		if (!datFile.exists()) throw new FileNotFoundException("No such file or directory: "+ datFile);

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(datFile, "r");

			// Read the header
			int headerSize = readLittleInt(raf);

			// Avoid allocating a rediculous array size.
			long position = raf.getChannel().position();
			if ( headerSize > raf.getChannel().size() - position)
				throw new RuntimeException("Expected header size ("+ headerSize +") would extend beyond the end of the stream, from current position ("+ position +")");

			int[] header = new int[headerSize];

			// Read the offsets
			for (int i = 0; i < header.length; i++) {
				header[i] = readLittleInt(raf);
				if (header[i] == 0) break;  // No more interesting offsets.
			}

			// Read the file entries
			for (int i = 0; i < header.length && header[i] != 0; i++) {
				raf.getChannel().position(header[i]);

				long dataSize = readLittleInt(raf);
				String innerPath = readLittleString(raf);
				long dataOffset = raf.getChannel().position();

				InnerFileInfo info = new InnerFileInfo(dataOffset, dataSize);
				innerFilesMap.put(innerPath, info);
			}
		} finally {
			try {if (raf != null) raf.close();}
			catch (IOException e) {}
		}
	}

	/**
	 * Reads a little-endian int from the current position in a file.
	 */
	private int readLittleInt(RandomAccessFile raf) throws IOException {
		int x = raf.readInt();
		return ((x >> 24) & 0xFF) | ((x >> 8) & 0xFF00) | ((x << 8) & 0xFF0000) | ((x << 24) & 0xFF000000);
	}

	/**
	 * Reads a little-endian int length + ascii string from a stream.
	 */
	private String readLittleString(RandomAccessFile raf) throws IOException {
		int length = readLittleInt(raf);

		// Avoid allocating a rediculous array size.
		long position = raf.getChannel().position();
		if ( length > raf.getChannel().size() - position)
			throw new RuntimeException("Expected string length ("+ length +") would extend beyond the end of the stream, from current position ("+ position +")");

		int numRead = 0;
		int offset = 0;
		byte[] strBytes = new byte[length];
		while ( offset < strBytes.length && (numRead = raf.read(strBytes, offset, strBytes.length)) >= 0 )
			offset += numRead;

		if ( offset < strBytes.length )
			throw new RuntimeException("End of stream reached before reading enough bytes for string of length "+ length);

		return new String(strBytes, "US-ASCII");
	}

	/**
	 * Returns the content of an inner file.
	 *
	 * The dat is briefly opened, and the entire region is read into a buffer.
	 * And a stream is returned that wraps that buffer.
	 *
	 * TODO: Port memory mapping from the Profile/SavedGame Editor.
	 */
	public InputStream getInputStream(String innerPath) throws IOException {
		InnerFileInfo info = innerFilesMap.get(innerPath);
		if (info == null) throw new FileNotFoundException("The path ("+ innerPath +") was not found in "+ datFile.getName());

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(datFile, "r");
			raf.seek(info.dataOffset);
			byte[] data = new byte[(int)info.dataSize];  // Loss of precision long > int.
			raf.read(data);
			return new ByteArrayInputStream(data);
		}
		finally {
			try {if (raf != null) raf.close();}
			catch (IOException e) {}
		}
	}

	/**
	 * Extracts all inner files into a parent directory.
	 *
	 * @return true if successful, false otherwise
	 */
	public boolean extractToFolder(File destDir) throws IOException {
		int i = 0;
		for (Map.Entry<String, InnerFileInfo> entry : innerFilesMap.entrySet()) {
			String innerPath = entry.getKey();
			InnerFileInfo info = entry.getValue();

			File destFile = new File(destDir, innerPath);

			if (!Utils.ensureDirectoryExists(destFile.getParentFile()))
				throw new IOException("Could not create directory: "+ destFile.getParentFile().getAbsolutePath());

			InputStream is = getInputStream(innerPath);
			if (!Utils.copyStream(is, destFile)) return false;
			// By now, either the stream was never created (exception), or the copy method closed it.

			i++;
			if (i % 100 == 0) {
				System.out.println("Extracted "+ i +" out of "+ innerFilesMap.size() +" files.");
			}
		}
		return true;
	}



	private class InnerFileInfo {
		public long dataOffset = 0;
		public long dataSize = 0;

		public InnerFileInfo(long dataOffset, long dataSize) {
			this.dataOffset = dataOffset;
			this.dataSize = dataSize;
		}
	}
}
