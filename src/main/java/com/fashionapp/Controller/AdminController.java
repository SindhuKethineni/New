package com.fashionapp.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fashionapp.Entity.Admin;
import com.fashionapp.Entity.BlockedData;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.Status;
import com.fashionapp.Entity.Type;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Repository.AdminRepository;
import com.fashionapp.Repository.BlockedDataRepository;
import com.fashionapp.Repository.FileInfoRepository;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
class Employee{
	int id;
	String fname;
	String lname;
	int Salary;
	public Employee(int id,String fname,String lname,int Salary)
	{
		this.id=id;
		this.fname=fname;
		this.lname=lname;
		this.Salary=Salary;
	}
}
@Controller
@RequestMapping(value = "/admin")
@Api(value = "AdminController")
public class AdminController {
	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private UserDetailsRepository userDetailsRepository;
	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private FileInfoRepository fileInfoRepository;
	@Autowired
	private BlockedDataRepository blockedDataRepository;
	public static void main(String args[])
	{
		List<Employee> list = new ArrayList<Employee>();
		list.add(new Employee(223,"john","jane",49000));
		list.add(new Employee(594,"vikas","venu",59040));
		list.add(new Employee(594,"sindu","kethineni",40300));
		list.add(new Employee(504,"aditya","siddu",59340));
		List<Employee> list2 = list.stream().collect(Collectors.toList());
		for(Employee lst:list2)
		{
			System.out.println(lst.id+" "+lst.fname+" "+lst.lname+" "+lst.Salary);
		}
		List<Employee> list3 = list.stream().filter(n->n.id>300).collect(Collectors.toList());
		for(Employee ls:list3)
		{
			System.out.println(ls.id+" "+ls.fname+" "+ls.lname+" "+ls.Salary);
		}
		LocalDateTime ldt = LocalDateTime.now();
		LocalDate ld = LocalDate.now();
		LocalTime lt = LocalTime.now();
		int dd= ld.getDayOfMonth();int mm=ld.getMonthValue();int yyyy=ld.getYear();
		System.out.println(dd+"-"+mm+"-"+yyyy);
		int h=lt.getHour();int m=lt.getMinute();int s=lt.getSecond();int n=lt.getNano();
		System.out.println(h+":"+m+":"+s+":"+n);
		System.out.println(ldt);
	}
	@ApiOperation(value = "create-admin", response = Admin.class)
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Createsection(@RequestBody String data)
			throws IOException, ParseException {
		System.out.println("admin save is calling");
		Admin admin = new Admin();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			admin = new ObjectMapper().readValue(data, Admin.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		admin.setIsactive(true);
		Admin adminData = adminRepository.save(admin);
		response = server.getSuccessResponse("Uploded Successfully", adminData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "list_of_admins", response = Admin.class)
	@RequestMapping(value = "/getadmins", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllAdmins() throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterable<Admin> fecthed = adminRepository.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}
	

	@ApiOperation(value = "list_of_users", response = UserInfo.class)
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAllUsers() throws IOException, ParseException {
		Map<String, Object> map = new HashMap<String, Object>();
		Iterable<UserInfo> fecthed = userDetailsRepository.findAll();
		map.put("Data", fecthed);
		map.put("message", "Successfull !.");
		map.put("status", true);
		return ResponseEntity.ok().body(map);
	}

	@ApiOperation(value = "fetch by userid", response = UserInfo.class)
	@RequestMapping(value = "/find-user-by-id", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUser(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<UserInfo> fecthed = userDetailsRepository.findById(id);
		response = server.getSuccessResponse("Uploded Successfully", fecthed);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "delete-user", response = UserInfo.class)
	@RequestMapping(value = "/delete-user", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		userDetailsRepository.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", null);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "delete-file-by-id", response = FileInfo.class)
	@RequestMapping(value = "/delete-file", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> deletefile(@RequestParam long id) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		fileInfoRepository.deleteById(id);
		response = server.getSuccessResponse("deleted successfully", "video" + id);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "blockuser", response = BlockedData.class)
	@RequestMapping(value = "/blockuser", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> blockuserordata(@RequestParam long userId, @RequestParam long adminid)
			throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData blockedDetails = new BlockedData();

		Optional<UserInfo> fetchedName = userDetailsRepository.findById(userId);
		blockedDetails.setAdminId(adminid);
		blockedDetails.setUserId(userId);
		blockedDetails.setUserName(fetchedName.get().getUserName());
		blockedDetails.setReason("bad utilizer!");
		blockedDetails.setType(Type.User);
		blockedDetails.setActive(true);
		blockedDetails.setFileId(0);

		BlockedData blockeData = null;
		BlockedData fetched = blockedDataRepository.findByUserId(userId);

 		String status = null;
 		if (fetched == null) {
			status = "blocked";
			blockeData = blockedDataRepository.save(blockedDetails);

		} else {
			status = "unblocked";
			blockedDataRepository.deleteById(fetched.getId());
		}

		response = server.getSuccessResponse(status, blockeData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "blockfile", response = BlockedData.class)
	@RequestMapping(value = "/blockfile", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> blockFile(@RequestParam  long fileid, @RequestParam long adminid
			 ) throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData blockedDetails = new BlockedData();
		BlockedData blockeData = null;

		Optional<FileInfo> fetchedFile = fileInfoRepository.findById(fileid);
		Optional<UserInfo> fetchedUser = userDetailsRepository.findById(fetchedFile.get().getUserId());
		blockedDetails.setAdminId(adminid);
		blockedDetails.setUserId(fetchedFile.get().getUserId());
		blockedDetails.setUserName(fetchedUser.get().getUserName());
		blockedDetails.setFileId(fileid);
		blockedDetails.setReason("bad content!");
		blockedDetails.setType(Type.File);
		blockedDetails.setActive(true);
		BlockedData fetched = blockedDataRepository.findByFileId(fileid);
		String status = null;
		 
		if (fetched == null) {
			status = "blocked";
			blockeData = blockedDataRepository.save(blockedDetails);

		} else {
			status = "unblocked";
			blockedDataRepository.deleteById(fetched.getId());
		}

		response = server.getSuccessResponse(status, blockeData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "unblock", response = BlockedData.class)
	@RequestMapping(value = "/unblock", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> Unblockuserordata(@RequestParam long userid)
			throws IOException, ParseException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		BlockedData fetchedByUser = blockedDataRepository.findByUserId(userid);
		if (fetchedByUser == null) {
			response = server.getNotAceptableResponse("No Data found", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

		}

		blockedDataRepository.deleteById(fetchedByUser.getId());

		response = server.getSuccessResponse("User un-blocked", userid);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*
	 * TO-DO:
	 * 
	 * 1.admin can block user 2.admin can block data 3.admin can uplod files 4.admin
	 * can update files 5.admin can delete user 6.admin can delete data
	 * 
	 */

}
