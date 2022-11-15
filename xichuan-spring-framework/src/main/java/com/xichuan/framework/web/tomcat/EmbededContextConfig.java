package com.xichuan.framework.web.tomcat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ContextConfig;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.util.scan.Jar;
import org.apache.tomcat.util.scan.JarFactory;

/**
 * @Author Xichuan
 * @Date 2022/5/11 9:18
 * @Description
 */
/**
 * Support jar in jar. when boot by spring boot loader, jar url will be: fat.jar!/lib/!/test.jar!/ .
 *
 * ContextConfig主要是添加到StandardContext上的监听器，ContextConfig主要是处理web应用的配置文件。
 * 此处是为了扫描templates/ 与 static/ 目录下的静态资源
 */
public class EmbededContextConfig extends ContextConfig {
	private static final Log log = LogFactory.getLog(ContextConfig.class);

	/**
	 * Scan JARs that contain web-fragment.xml files that will be used to
	 * configure this application to see if they also contain static resources.
	 * If static resources are found, add them to the context. Resources are
	 * added in web-fragment.xml priority order.
	 */
	@Override
	protected void processResourceJARs(Set<WebXml> fragments) {
		for (WebXml fragment : fragments) {
			URL url = fragment.getURL();

			try {
				String urlString = url.toString();
				if (isInsideNestedJar(urlString)) {
					// It's a nested jar but we now don't want the suffix
					// because
					// Tomcat
					// is going to try and locate it as a root URL (not the
					// resource
					// inside it)
					urlString = urlString.substring(0, urlString.length() - 2);
				}
				url = new URL(urlString);

				//如果加载的jar
				if ("jar".equals(url.getProtocol())) {
					try (Jar jar = JarFactory.newInstance(url)) {
						jar.nextEntry();
						String entryName = jar.getEntryName();
						//templates的ResourceRoot是否被添加
						boolean isTemplatesLoad = false;
						//static的ResourceRoot是否被添加
						boolean isStaticLoad = false;
						while (entryName != null) {
							if (entryName.startsWith("templates/") && !isTemplatesLoad) {
								context.getResources().createWebResourceSet(
										//如果请求/index.html，会到jar中的/templates/文件夹下找index.html
										WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, "/templates");
								isTemplatesLoad = true;
							}
							if (entryName.startsWith("static/") && !isStaticLoad) {
								context.getResources().createWebResourceSet(
										WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/", url, "/static");
								isStaticLoad = true;
							}
							jar.nextEntry();
							entryName = jar.getEntryName();
						}

					}
				//如果是本地运行
				} else if ("file".equals(url.getProtocol())) {
					File file = new File(url.toURI());
					File resources = new File(file, "templates/");
					if (resources.isDirectory()) {
						context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/",
								resources.getAbsolutePath(), null, "/");
					}
					resources = new File(file, "static/");
					if (resources.isDirectory()) {
						context.getResources().createWebResourceSet(WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/",
								resources.getAbsolutePath(), null, "/");
					}
				}
			} catch (IOException ioe) {
				log.error(sm.getString("contextConfig.resourceJarFail", url, context.getName()));
			} catch (URISyntaxException e) {
				log.error(sm.getString("contextConfig.resourceJarFail", url, context.getName()));
			}
		}
	}

	private static boolean isInsideNestedJar(String dir) {
		return dir.indexOf("!/") < dir.lastIndexOf("!/");
	}
}
