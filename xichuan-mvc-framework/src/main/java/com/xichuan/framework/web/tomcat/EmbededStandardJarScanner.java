package com.xichuan.framework.web.tomcat;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.JarScanFilter;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.ExceptionUtils;
import org.apache.tomcat.util.res.StringManager;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.StandardJarScanFilter;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:18
 * @Description
 */
/**
 * When boot by spring boot loader, WebappClassLoader.getParent() is LaunchedURLClassLoader,
 * Just need to scan WebappClassLoader and LaunchedURLClassLoader.
 *
 * When boot in IDE, WebappClassLoader.getParent() is AppClassLoader,
 * Just need to scan WebappClassLoader and AppClassLoader.
 */
public class EmbededStandardJarScanner implements JarScanner {

	private static final Log log = LogFactory.getLog(EmbededStandardJarScanner.class);

	/**
	 * The string resources for this package.
	 */
	private static final StringManager sm = StringManager.getManager(Constants.Package);

	/**
	 * Controls the classpath scanning extension.
	 */
	private boolean scanClassPath = true;

	public boolean isScanClassPath() {
		return scanClassPath;
	}

	public void setScanClassPath(boolean scanClassPath) {
		this.scanClassPath = scanClassPath;
	}

	/**
	 * Controls the testing all files to see of they are JAR files extension.
	 */
	private boolean scanAllFiles = false;

	public boolean isScanAllFiles() {
		return scanAllFiles;
	}

	public void setScanAllFiles(boolean scanAllFiles) {
		this.scanAllFiles = scanAllFiles;
	}

	/**
	 * Controls the testing all directories to see of they are exploded JAR
	 * files extension.
	 */
	private boolean scanAllDirectories = false;

	public boolean isScanAllDirectories() {
		return scanAllDirectories;
	}

	public void setScanAllDirectories(boolean scanAllDirectories) {
		this.scanAllDirectories = scanAllDirectories;
	}

	/**
	 * Controls the testing of the bootstrap classpath which consists of the
	 * runtime classes provided by the JVM and any installed system extensions.
	 */
	private boolean scanBootstrapClassPath = false;

	public boolean isScanBootstrapClassPath() {
		return scanBootstrapClassPath;
	}

	public void setScanBootstrapClassPath(boolean scanBootstrapClassPath) {
		this.scanBootstrapClassPath = scanBootstrapClassPath;
	}

	/**
	 * Controls the filtering of the results from the scan for JARs
	 */
	private JarScanFilter jarScanFilter = new StandardJarScanFilter();

	@Override
	public JarScanFilter getJarScanFilter() {
		return jarScanFilter;
	}

	@Override
	public void setJarScanFilter(JarScanFilter jarScanFilter) {
		this.jarScanFilter = jarScanFilter;
	}

	/**
	 * Scan the provided ServletContext and class loader for JAR files. Each JAR
	 * file found will be passed to the callback handler to be processed.
	 *
	 * @param scanType
	 *            The type of JAR scan to perform. This is passed to the filter
	 *            which uses it to determine how to filter the results
	 * @param context
	 *            The ServletContext - used to locate and access WEB-INF/lib
	 * @param callback
	 *            The handler to process any JARs found
	 */
	@Override
	public void scan(JarScanType scanType, ServletContext context, JarScannerCallback callback) {

		if (log.isTraceEnabled()) {
			log.trace(sm.getString("jarScan.webinflibStart"));
		}

		Set<URL> processedURLs = new HashSet<>();

		// Scan WEB-INF/lib
		Set<String> dirList = context.getResourcePaths(Constants.WEB_INF_LIB);
		if (dirList != null) {
			Iterator<String> it = dirList.iterator();
			while (it.hasNext()) {
				String path = it.next();
				if (path.endsWith(Constants.JAR_EXT)
						&& getJarScanFilter().check(scanType, path.substring(path.lastIndexOf('/') + 1))) {
					// Need to scan this JAR
					if (log.isDebugEnabled()) {
						log.debug(sm.getString("jarScan.webinflibJarScan", path));
					}
					URL url = null;
					try {
						url = context.getResource(path);
						processedURLs.add(url);
						process(scanType, callback, url, path, true);
					} catch (IOException e) {
						log.warn(sm.getString("jarScan.webinflibFail", url), e);
					}
				} else {
					if (log.isTraceEnabled()) {
						log.trace(sm.getString("jarScan.webinflibJarNoScan", path));
					}
				}
			}
		}

		// Scan WEB-INF/classes
		if (isScanAllDirectories()) {
			try {
				URL url = context.getResource("/WEB-INF/classes/META-INF");
				if (url != null) {
					// Class path scanning will look at WEB-INF/classes since
					// that is the URL that Tomcat's web application class
					// loader returns. Therefore, it is this URL that needs to
					// be added to the set of processed URLs.
					URL webInfURL = context.getResource("/WEB-INF/classes");
					if (webInfURL != null) {
						processedURLs.add(webInfURL);
					}
					try {
						callback.scanWebInfClasses();
					} catch (IOException e) {
						log.warn(sm.getString("jarScan.webinfclassesFail"), e);
					}
				}
			} catch (MalformedURLException e) {
				// Ignore
			}
		}

		// Scan the classpath
		if (isScanClassPath()) {
			if (log.isTraceEnabled()) {
				log.trace(sm.getString("jarScan.classloaderStart"));
			}

			ClassLoader classLoader = context.getClassLoader();

			ClassLoader stopLoader = null;
			if (classLoader.getParent() != null) {
				// there are two cases:
				// 1. boot by spring boot loader
				// 2. boot in IDE
				// in two case, just need to scan WebappClassLoader and
				// WebappClassLoader.getParent()
				stopLoader = classLoader.getParent().getParent();
			}

			// JARs are treated as application provided until the common class
			// loader is reached.
			boolean isWebapp = true;

			while (classLoader != null && classLoader != stopLoader) {
				if (classLoader instanceof URLClassLoader) {
					URL[] urls = ((URLClassLoader) classLoader).getURLs();
					for (int i = 0; i < urls.length; i++) {
						if (processedURLs.contains(urls[i])) {
							// Skip this URL it has already been processed
							continue;
						}

						ClassPathEntry cpe = new ClassPathEntry(urls[i]);

						// JARs are scanned unless the filter says not to.
						// Directories are scanned for pluggability scans or
						// if scanAllDirectories is enabled unless the
						// filter says not to.
						if ((cpe.isJar() || scanType == JarScanType.PLUGGABILITY || isScanAllDirectories())
								&& getJarScanFilter().check(scanType, cpe.getName())) {
							if (log.isDebugEnabled()) {
								log.debug(sm.getString("jarScan.classloaderJarScan", urls[i]));
							}
							try {
								process(scanType, callback, urls[i], null, isWebapp);
							} catch (IOException ioe) {
								log.warn(sm.getString("jarScan.classloaderFail", urls[i]), ioe);
							}
						} else {
							// JAR / directory has been skipped
							if (log.isTraceEnabled()) {
								log.trace(sm.getString("jarScan.classloaderJarNoScan", urls[i]));
							}
						}
					}
				}
				classLoader = classLoader.getParent();
			}
		}
	}

	/*
	 * Scan a URL for JARs with the optional extensions to look at all files and
	 * all directories.
	 */
	private void process(JarScanType scanType, JarScannerCallback callback, URL url, String webappPath,
			boolean isWebapp) throws IOException {

		if (log.isTraceEnabled()) {
			log.trace(sm.getString("jarScan.jarUrlStart", url));
		}

		URLConnection conn = url.openConnection();
		if (conn instanceof JarURLConnection) {
			callback.scan((JarURLConnection) conn, webappPath, isWebapp);
		} else {
			String urlStr = url.toString();
			if (urlStr.startsWith("file:") || urlStr.startsWith("http:") || urlStr.startsWith("https:")) {
				if (urlStr.endsWith(Constants.JAR_EXT)) {
					URL jarURL = new URL("jar:" + urlStr + "!/");
					callback.scan((JarURLConnection) jarURL.openConnection(), webappPath, isWebapp);
				} else {
					File f;
					try {
						f = new File(url.toURI());
						if (f.isFile() && isScanAllFiles()) {
							// Treat this file as a JAR
							URL jarURL = new URL("jar:" + urlStr + "!/");
							callback.scan((JarURLConnection) jarURL.openConnection(), webappPath, isWebapp);
						} else if (f.isDirectory()) {
							if (scanType == JarScanType.PLUGGABILITY) {
								callback.scan(f, webappPath, isWebapp);
							} else {
								File metainf = new File(f.getAbsoluteFile() + File.separator + "META-INF");
								if (metainf.isDirectory()) {
									callback.scan(f, webappPath, isWebapp);
								}
							}
						}
					} catch (Throwable t) {
						ExceptionUtils.handleThrowable(t);
						// Wrap the exception and re-throw
						IOException ioe = new IOException();
						ioe.initCause(t);
						throw ioe;
					}
				}
			}
		}

	}

	private static class ClassPathEntry {

		private final boolean jar;
		private final String name;

		public ClassPathEntry(URL url) {
			String path = url.getPath();
			int end = path.indexOf(Constants.JAR_EXT);
			if (end != -1) {
				jar = true;
				int start = path.lastIndexOf('/', end);
				name = path.substring(start + 1, end + 4);
			} else {
				jar = false;
				if (path.endsWith("/")) {
					path = path.substring(0, path.length() - 1);
				}
				int start = path.lastIndexOf('/');
				name = path.substring(start + 1);
			}

		}

		public boolean isJar() {
			return jar;
		}

		public String getName() {
			return name;
		}
	}
}