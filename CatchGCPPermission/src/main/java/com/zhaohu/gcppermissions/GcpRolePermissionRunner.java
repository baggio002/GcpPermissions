package com.zhaohu.gcppermissions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

	private static final String URL = "https://cloud-dot-devsite-v2-prod.appspot.com/iam/docs/permissions-reference_775b924a5ef85079035e9937fd7ddd5ff0c1522673f07cb7da6c4f1db7b72405.frame";
	// private static final String URL =
	// "https://cloud.google.com/iam/docs/permissions-reference";
	/*
	 * private static final String HEADER_UPGRADE_INSECURE_REQUESTS_NAME =
	 * "Upgrade-Insecure-Requests"; private static final String
	 * HEADER_UPGRADE_INSECURE_REQUESTS_VALUE = "1"; private static final String
	 * HEADER_USER_AGENT_NAME = "User-Agent"; private static final String
	 * HEADER_USER_AGENT_VALUE =
	 * "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.114 Safari/537.36"
	 * ; private static final String FILE_NAME = "permission-doc.html";
	 */

	private static Map<String, Role> roles = new HashMap<String, Role>();
	private static Set<Permission> permissions = new HashSet<Permission>();
	
	
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionReopsitory permissionRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(GcpRolePermissionRunner.class, args);
		/*
		 * System.out.println("permission size = " + permissions.size() +
		 * " roles size = " + roles.size()); Role role =
		 * roles.get("Owner (roles/owner)");
		 * System.out.println("owner permission size = " +
		 * role.getPermissions().size()); System.out.println("permission size = " +
		 * permissions.size()); permissions.forEach(permission -> {
		 * System.out.println("-------------");
		 * System.out.println(permission.getRoles().size()); });
		 */
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("================springboot run!=================");
		getPermissions(URL);
		System.out.println("roles = " + roles.size());
		System.out.println("permissions = " + permissions.size());
		
		roleRepository.deleteAll();
		permissionRepository.deleteAll();
		roleRepository.saveAll(roles.values());
		permissionRepository.saveAll(permissions);
		System.out.println("================springboot complete!=================");

	}
	

	private static void getPermissions(String url) {
		Document doc;
		doc = Jsoup.parse(readWebSite(url));
		// writeFile(doc.html());
		Elements trs = doc.select("tr");
		boolean skip = true;
		for (Element tr : trs) {
			if (skip) {
				// System.out.println(tr.toString());
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
				// System.out.println(td.select("code").text());
				permission.setPermissionName(td.select("code").text());
				permissions.add(permission);
				isPermission = !isPermission;
			}
			loopLis(td.select("li"), permission);
		}
	}

	private static void loopLis(Elements lis, Permission permission) {
		for (Element li : lis) {
			// System.out.println(li.text());
			String roleName = li.text();
			if (roles.containsKey(roleName)) {
				roles.get(roleName).addPermission(permission);
			} else {
				roles.put(roleName, new Role(roleName));
			}
			permission.getRoles().add(roles.get(roleName));	
		}
	}


	/*
	 * private static void getPermissions() { Document doc; // readWebSite(URL); try
	 * { doc = Jsoup.connect(URL) .header(HEADER_UPGRADE_INSECURE_REQUESTS_NAME,
	 * HEADER_UPGRADE_INSECURE_REQUESTS_VALUE) .header(HEADER_USER_AGENT_NAME,
	 * HEADER_USER_AGENT_VALUE).get(); // writeFile(doc.html()); Elements
	 * newsHeadlines = doc.select("tr"); System.out.println("===================");
	 * int i = 0; for (Element headline : newsHeadlines) {
	 * System.out.println(headline.toString()); System.out.println("======" + i++);
	 * } System.out.println("=========end=========="); } catch (IOException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } }
	 */

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

	/*
	 * private static void writeFile(String file) { File myObj = new
	 * File("permission-doc.html"); try { if (myObj.createNewFile()) {
	 * System.out.println("File created: " + myObj.getName()); } } catch
	 * (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * try { FileWriter myWriter = new FileWriter(FILE_NAME); myWriter.write(file);
	 * myWriter.close(); } catch (IOException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); }
	 * System.out.println("Successfully wrote to the file."); }
	 */

}
