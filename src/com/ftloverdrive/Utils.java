package com.ftloverdrive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class Utils {
	/**
	 * Registers a suite of protocol handlers for new URL objects.
	 *
	 * The VM-wide system property "java.protocol.handler.pkgs"
	 * is appended to include a custom package. The package's
	 * children must be packages named for various protocols,
	 * each of which must contain a "Handler" class that extends
	 * java.net.URLStreamHandler.
	 *
	 * A handler will be lazily instantiated and cached. It'll
	 * create a fresh URLConnection each new URL.
	 *
	 * Warning: Technically there's a race condition (get then set).
	 * This method could be synchronized, but that wouldn't
	 * protect the property from other methods. *shrug*
	 */
	public static void registerProtocolHandlers(String packageName) {
		String handlers = System.getProperty("java.protocol.handler.pkgs");
		if (handlers != null) {
			handlers += "|"+ packageName;
		} else {
			handlers = packageName;
		}
		System.setProperty("java.protocol.handler.pkgs", handlers);
	}

	private static boolean isDatsDirValid(File d) {
		return (d.exists() && d.isDirectory() && new File(d, "data.dat").exists());
	}

	public static File findFTLDatsDir() {
		String steamPath = "Steam/steamapps/common/FTL Faster Than Light/resources";
		String gogPath = "GOG.com/Faster Than Light/resources";

		String xdgDataHome = System.getenv("XDG_DATA_HOME");
		if (xdgDataHome == null)
			xdgDataHome = System.getProperty("user.home") +"/.local/share";

		File[] paths = new File[] {
			// Windows - Steam
			new File( new File(""+System.getenv("ProgramFiles(x86)")), steamPath ),
			new File( new File(""+System.getenv("ProgramFiles")), steamPath ),
			// Windows - GOG
			new File( new File(""+System.getenv("ProgramFiles(x86)")), gogPath ),
			new File( new File(""+System.getenv("ProgramFiles")), gogPath ),
			// Linux - Steam
			new File( xdgDataHome +"/Steam/SteamApps/common/FTL Faster Than Light/data/resources" ),
			// OSX - Steam
			new File( System.getProperty("user.home") +"/Library/Application Support/Steam/SteamApps/common/FTL Faster Than Light/FTL.app/Contents/Resources" ),
			// OSX
			new File( "/Applications/FTL.app/Contents/Resources" ),
			// TODO: Vhati's non-standard location. Remove this.
			new File( "E:/Games/FasterThanLight/resources" )
		};

		File datsDir = null;

		for ( File path: paths ) {
			if ( isDatsDirValid(path) ) {
				datsDir = path;
				break;
			}
		}

		return datsDir;
	}

/*
	// TODO: port this Swing dialog to TWL.
	// Call after finding fails or is overridden by the user.

	private static File promptForFTLDatsDir() {
		File datsDir = null;

		String message = "Overdrive uses images and data from FTL,\n";
		message += "but the path to FTL's resources could not be guessed.\n\n";
		message += "You will now be prompted to locate FTL manually.\n";
		message += "Select '(FTL dir)/resources/data.dat'.\n";
		message += "Or 'FTL.app', if you're on OSX.";
		JOptionPane.showMessageDialog(null,  message, "FTL Not Found", JOptionPane.INFORMATION_MESSAGE);

		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Find data.dat or FTL.app");
		fc.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "FTL Data File - (FTL dir)/resources/data.dat";
			}
			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().equals("data.dat") || f.getName().equals("FTL.app");
			}
		});
		fc.setMultiSelectionEnabled(false);

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			if (f.getName().equals("data.dat"))
				datsDir = f.getParentFile();
			else if (f.getName().endsWith(".app") && f.isDirectory()) {
				File contentsPath = new File(f, "Contents");
				if(contentsPath.exists() && contentsPath.isDirectory() && new File(contentsPath, "Resources").exists())
					datsDir = new File(contentsPath, "Resources");
			}
			System.out.println( "User selected: " + datsDir.getAbsolutePath() );
		} else {
			System.out.println( "User cancelled FTL dats path selection." );
		}

		if (datsDir != null && isDatsDirValid(datsDir)) {
			return datsDir;
		}

		return null;
	}
*/

	public static File getUserDataDir() {
		//TODO: Return a different location for Android.
		//  (or other platform-specific user-owned paths).

		// Use the first entry in CLASSPATH assumed to be the main app path.
		try {
			return new File( Utils.class.getClassLoader().getResource("").toURI() );
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Following methods are based on http://stackoverflow.com/a/3348150

	/**
	 * Copies all bytes from one file to another.
	 *
	 * @return true if successful, false otherwise
	 */
	public static boolean copyFile(File toCopy, File destFile) {
		FileInputStream is = null;
		FileOutputStream os = null;
		try {
			is = new FileInputStream(toCopy);
			os = new FileOutputStream(destFile);
			return Utils.copyStream(is, os);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {is.close();}
			catch (IOException e) {}

			try {os.close();}
			catch (IOException e) {}
		}
		return false;
	}

	/**
	 * Copies all bytes from a file/dir into a parent directory.
	 *
	 * @return true if successful, false otherwise
	 */
	private static boolean copyFilesRecusively(File toCopy, File destDir) {
		assert destDir.isDirectory();

		if (!toCopy.isDirectory()) {
			return Utils.copyFile(toCopy, new File(destDir, toCopy.getName()));
		}
		else {
			File newDestDir = new File(destDir, toCopy.getName());
			if (!ensureDirectoryExists(newDestDir)) return false;

			for (File child : toCopy.listFiles()) {
				if (!Utils.copyFilesRecusively(child, newDestDir)) return false;
			}
		}
		return true;
	}

	/**
	 * Extracts jar'd files/dirs into a parent directory.
	 *
	 * @return true if successful
	 * @throws IOException if anything goes wrong
	 */
	public static boolean copyResourcesRecursively(JarURLConnection jarCon, File destDir) throws IOException {
		JarFile jarFile = jarCon.getJarFile();

		for (Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
			JarEntry entry = e.nextElement();
			if (entry.getName().startsWith(jarCon.getEntryName())) {
				String filename = removePrefix(entry.getName(), jarCon.getEntryName());

				File f = new File(destDir, filename);
				if (!entry.isDirectory()) {
					InputStream is = jarFile.getInputStream(entry);  // No stream to close if that throws an exception.
					if (!Utils.copyStream(is, f)) return false;      // That method will close the stream.
				}
				else {
					if (!Utils.ensureDirectoryExists(f)) {
						throw new IOException("Could not create directory: "+ f.getCanonicalPath());
					}
				}
			}
		}
		return true;
	}

	/**
	 * Copies resources from a URL into a parent directory.
	 *
	 * The following URL protocols are supported:
	 *   "file:" - A file/dir on the local filesystem.
	 *   "jar:"  - A file/dir inside a jar.
	 *
	 * @return true if successful, false otherwise
	 */
	public static boolean copyResourcesRecursively(URL originUrl, File destDir) {
		try {
			URLConnection urlCon = originUrl.openConnection();
			if (urlCon instanceof JarURLConnection) {
				// Not the most efficient method, but who cares. We do it only once.
				String filename = originUrl.getFile().replaceAll("^.*/(?=.)", "");
				return Utils.copyResourcesRecursively((JarURLConnection)urlCon, new File(destDir, filename));
			}
			else {
				return Utils.copyFilesRecusively(new File(originUrl.toURI()), destDir);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Copies all remaining bytes from a stream to a file.
	 *
	 * @return true if successful, false otherwise
	 * @see #copyStream(InputStream, InputStream)
	 */
	public static boolean copyStream(InputStream is, File destFile) {
		try {
			return Utils.copyStream(is, new FileOutputStream(destFile));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Copies all remaining bytes from one stream to another.
	 *
	 * Both streams will be closed afterward.
	 * @return true if successful, false otherwise
	 */
	public static boolean copyStream(InputStream is, OutputStream os) {
		try {
			byte[] buf = new byte[4096];

			int len;
			while ((len = is.read(buf)) >= 0) {
				os.write(buf, 0, len);
			}
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {is.close();}
			catch (IOException e) {}

			try {os.close();}
			catch (IOException e) {}
		}
		return false;
	}

	/**
	 * Creates a directory (and parents), if necessary, and returns true if it exists.
	 */
	public static boolean ensureDirectoryExists(File d) {
		return d.exists() || d.mkdirs();
	}

	/**
	 * Returns a string, minus a given prefix, if present.
	 */
	public static String removePrefix(String str, String prefix) {
		if (str != null && prefix != null && str.length() * prefix.length() != 0) {
			if (str.startsWith(prefix)) return str.substring(prefix.length());
		}
		return str;
	}

	public static void deleteFolder(File dir) {
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				deleteFolder(f);
			} else {
				f.delete();
			}
		}
		dir.delete();
	}
}
