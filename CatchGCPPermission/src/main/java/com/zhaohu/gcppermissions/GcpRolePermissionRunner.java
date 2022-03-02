package com.zhaohu.gcppermissions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URL;
import java.net.URLConnection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.zhaohu.gcppermissions.entity.Permission;
import com.zhaohu.gcppermissions.entity.Role;
import com.zhaohu.gcppermissions.repository.PermissionReopsitory;
import com.zhaohu.gcppermissions.repository.RoleRepository;

@SpringBootApplication
public class GcpRolePermissionRunner implements ApplicationRunner {

	// private static final String URL = "https://cloud-dot-devsite-v2-prod.appspot.com/iam/docs/permissions-reference_775b924a5ef85079035e9937fd7ddd5ff0c1522673f07cb7da6c4f1db7b72405.frame";

	private static final String URL = "https://cloud.google.com/iam/docs/permissions-reference";
	private static final String GCP_URL = "https://cloud.google.com";

	private static Map<String, Role> roles = new HashMap<String, Role>();
	private static Set<Permission> permissions = new HashSet<Permission>();
	
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionReopsitory permissionRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(GcpRolePermissionRunner.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("================springboot run!=================");
		getPermissions(getPermissionsURL(URL));
		System.out.println("roles = " + roles.size());
		System.out.println("permissions = " + permissions.size());	
		roleRepository.deleteAll();
		permissionRepository.deleteAll();
		roleRepository.saveAll(roles.values());
		permissionRepository.saveAll(permissions);
		System.out.println("================springboot complete!=================");
		System.exit(0);
	}
	
	@SuppressWarnings("finally")
	private static String getFinalURL(String url) {
		
		URLConnection con;
		InputStream is = null;
		String finalUrl = "";
		try {
			con = new URL(url).openConnection();
			System.out.println( "orignal url: " + con.getURL() );
			con.connect();
			is = con.getInputStream();
			finalUrl = con.getURL().toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("===final url===" + finalUrl);
			if (is != null) {
				try {
					is.close();
				} catch (final IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			} 
			return finalUrl;
		}
	}
	
	private static String getPermissionsURL(String url) {
		Document doc;
		doc = Jsoup.parse(readWebSite(url));
		Elements iframes = doc.select("iframe");
		
		return getFinalURL(new StringBuffer(GCP_URL).append(iframes.get(0).attr("src")).toString());
	}
	
	private static void getPermissions(String url) {
		Document doc;
		doc = Jsoup.parse(readWebSite(url));
		Elements trs = doc.select("tr");
		boolean skip = true;
		for (Element tr : trs) {
			if (skip) {
				skip = !skip;
				continue;
			}
			loopTds(tr.select("td"));
		}
	}

	private static void loopTds(Elements tds) {
		boolean isPermission = true;
		Permission permission = new Permission();
		for (Element td : tds) {
			if (isPermission) {
				permission.setPermissionName(td.select("code").text());
				permissions.add(permission);
				isPermission = !isPermission;
			}
			loopLis(td.select("li"), permission);
		}
	}

	private static void loopLis(Elements lis, Permission permission) {
		for (Element li : lis) {
			String roleName = li.text();
			if (roles.containsKey(roleName)) {
				roles.get(roleName).addPermission(permission);
			} else {
				roles.put(roleName, new Role(roleName));
			}
			permission.getRoles().add(roles.get(roleName));	
		}
	}

	private static String readWebSite(String url) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET() // GET is default
				.build();

		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return response.body();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Nothing here";
	}

}
