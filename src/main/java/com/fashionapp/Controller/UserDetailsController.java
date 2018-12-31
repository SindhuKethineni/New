package com.fashionapp.Controller;

import java.io.IOException;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.fashionapp.Entity.Comments;
import com.fashionapp.Entity.FileInfo;
import com.fashionapp.Entity.FollowersGroup;
import com.fashionapp.Entity.FollowingGroup;
import com.fashionapp.Entity.HashTag;
import com.fashionapp.Entity.HashtagVideoMap;
import com.fashionapp.Entity.Likes;
import com.fashionapp.Entity.Share;
import com.fashionapp.Entity.Status;
import com.fashionapp.Entity.UserInfo;
import com.fashionapp.Entity.UserGroupMap;
import com.fashionapp.Repository.CommentsRepository;
import com.fashionapp.Repository.FileInfoRepository;
import com.fashionapp.Repository.FollowersGroupRepository;
import com.fashionapp.Repository.FollowingGroupRepository;
import com.fashionapp.Repository.HashTagRepository;
import com.fashionapp.Repository.HashtagVideoMapRepository;
import com.fashionapp.Repository.LikeRepository;
import com.fashionapp.Repository.ShareRepository;
import com.fashionapp.Repository.UserDetailsRepository;
import com.fashionapp.Repository.UserGroupMapRepository;
import com.fashionapp.filestorage.FileStorage;
import com.fashionapp.securityconfiguration.JwtTokenGenerator;
import com.fashionapp.util.MailSender;
import com.fashionapp.util.PasswordEncryptDecryptor;
import com.fashionapp.util.ServerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/userdetails")
@Api(value = "UserDetailsController")

public class UserDetailsController {

	Logger log = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private UserDetailsRepository userDetailsRepository;

	@Autowired
	private FileInfoRepository fileInfoRepository;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private CommentsRepository commentsRepository;

	@Autowired
	private ShareRepository shareRepository;

	@Autowired
	FileStorage fileStorage;

	@Autowired
	private FollowingGroupRepository followingGroupRepository;

	@Autowired
	private FollowersGroupRepository followersGroupRepository;

	@Autowired
	private UserGroupMapRepository userGroupMapRepository;

	@Autowired
	private HashTagRepository hashTagRepository;

	@Autowired
	private HashtagVideoMapRepository hashtagVideoMapRepository;

	@Autowired
	private JwtTokenGenerator jwtTokenGenerator;

	@Autowired
	@Qualifier("mailsender")
	MailSender sender;
	
	

	private final static String default_url ="home/chaitanya/chaitanya-workspace/workspace/java-webapi/profileimages/male.png";
	private final static String default_image ="male.png";

	
	
	/*
	 * TO:DO
	 * 
	 * if profile gets uploaded with image should display that ,or else default
	 * images should be displayed by category
	 * 
	 */

	@ApiOperation(value = "user-signup", response = UserInfo.class)
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> usersignup(@RequestParam("data") String data,
			@RequestParam(value = "file", required = false) MultipartFile profileImage) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		UserInfo userDetails = null;
		try {
			userDetails = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserInfo isemailExists = userDetailsRepository.findByEmail(userDetails.getEmail());
		if (isemailExists != null) {
			log.info("Email Id already exists, please choose another email id");
			response = server.getDuplicateResponse("Email Id already exists, please choose another email id", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CONFLICT);
		}
		userDetails.setPassword(PasswordEncryptDecryptor.encrypt(userDetails.getPassword()));

		UserInfo userData = null;
		if (profileImage == null || profileImage.isEmpty()) {
			userDetails.setProfileImageName(default_image);
			userDetails.setProfileImageUrl(default_url);
			userDetails.setActive(true);
			userData = userDetailsRepository.save(userDetails);

		} else {
			try {
				fileStorage.storeUserProfileImage(profileImage);
				log.info("File uploaded successfully! -> filename = " + profileImage.getOriginalFilename());
			} catch (Exception e) {
				log.info("Fail! -> uploaded filename: = " + profileImage.getOriginalFilename());
			}

			Resource path = fileStorage.loadprofileImage(profileImage.getOriginalFilename());
			System.out.println("PATH :=" + path.toString());
			userDetails.setProfileImageName(profileImage.getOriginalFilename());
			userDetails.setActive(true);
			userDetails.setProfileImageUrl(path.toString());

			userData = userDetailsRepository.save(userDetails);
		}

		// sender.sendmail(userDetails.getEmail(), "You have succesfully Registered with FashionApp");

		System.out.println("creating default group");
		DefaultfollowingGroup(userDetails.getId(), userDetails.getEmail());
		// DefaultfollowersGroup(userDetails.getId(), userDetails.getEmail());
		response = server.getSuccessResponse("SignUp Successful", userData);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	/*
	 * TO:DO
	 * 
	 * 1.Separate logins for admin and users
	 * 
	 * 2. Separate login response based on logged in category if male, male related
	 * content should occur if female ,female-content,kids ...
	 */

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAuthtoken(@RequestBody String data) throws Exception {
		log.info("login calling!!..");
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		UserInfo userDetails = null;

		try {
			userDetails = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		final UserInfo user = userDetailsRepository.findByEmail(userDetails.getEmail());
		if (user == null) {
			response = server.getNotFoundResponse("invalid email", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}

		String pwd = PasswordEncryptDecryptor.encrypt(userDetails.getPassword());

		if (!pwd.equalsIgnoreCase(user.getPassword())) {
			log.info("Invalid password");
			response = server.getNotFoundResponse("invalid password", null);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		} else {
			log.info("password is valid");
		}

		final String token = jwtTokenGenerator.generateToken(user);

		response = server.getSuccessResponse("login succesful", token);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}

	@ApiOperation(value = "user-forgotpwd", response = UserInfo.class)
	@RequestMapping(value = "/forgotpwd", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> userforgotpwd(@RequestBody String data) throws Exception {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		UserInfo userInfo = null;
		try {
			userInfo = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		UserInfo userInfoObj = userDetailsRepository.findByEmail(userInfo.getEmail());
		if (userInfoObj != null) {
			userInfoObj.setPassword(PasswordEncryptDecryptor.encrypt(userInfo.getPassword()));
			Date date = new Date(System.currentTimeMillis());
			userInfoObj.setCreationDate(date);
			UserInfo userData = userDetailsRepository.save(userInfoObj);
			response = server.getSuccessResponse("Password changed Successfully..", userInfoObj.getEmail());
			sender.sendmail(userInfoObj.getEmail(), "Changed Password Successfully");
		} else {
			response = server.getNotFoundResponse("User is not registered", null);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "list_of_users", response = UserInfo.class)
	@RequestMapping(value = "/getusers", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getAll() throws IOException, ParseException {
		Map<String, Object> response = new HashMap<String, Object>();
		ServerResponse<Object> server = new ServerResponse<Object>();

		Iterable<UserInfo> fecthed = userDetailsRepository.findAll();
		response = server.getSuccessResponse("fetched", fecthed);
		log.info("fetched all users");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	@ApiOperation(value ="getUserById",response = UserInfo.class)
	@RequestMapping(value="/getUserById",method = RequestMethod.GET)
	@ResponseBody
	public Optional<UserInfo> getById(@RequestParam(value="id") long id){
		return userDetailsRepository.findById(id);
	}
	@ApiOperation(value = "updating userdetails", response = UserInfo.class)
	@RequestMapping(value = "/update-user", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> update(@RequestParam long id, @RequestBody String data)
			throws IOException, ParseException {
		UserInfo userdetails = null;
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		try {
			userdetails = new ObjectMapper().readValue(data, UserInfo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		userdetails.setId(id);
		if(data ==null) {
			log.info("data is null");
			response = server.getNotFoundResponse("please enter the data", data);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			
		}
		else {
		UserInfo fecthed = userDetailsRepository.save(userdetails);
		log.info("updated successfully");
		response = server.getSuccessResponse("Successful", fecthed);
		}
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "retreieving by userid", response = UserInfo.class)
	@RequestMapping(value = "/findbyid", method = RequestMethod.GET)
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
		response = server.getSuccessResponse("deleted successfully", id);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	
	
	
	@RequestMapping(value = "/uploadvideo", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> upload(@RequestParam("id") long id,
			@RequestParam("file") MultipartFile file, @RequestParam("tag") String tag) throws IOException {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		FileInfo fileInfo = new FileInfo();
		UserInfo userdetails = new UserInfo();
		Date date = new Date(System.currentTimeMillis());
		fileInfo.setDate(date);
		fileInfo.setFileName(file.getOriginalFilename());
		fileInfo.setUserId(id);
		userdetails.setId(id);
		try {
			fileStorage.store(file);
			log.info("File uploaded successfully! -> filename = " + file.getOriginalFilename());
		} catch (Exception e) {
			log.info("Fail! -> uploaded filename: = " + file.getOriginalFilename());
		}
		Resource path = fileStorage.loadFile(file.getOriginalFilename());
		System.out.println("PATH :=" + path.toString());
		fileInfo.setUrl(path.toString());
		FileInfo fileinserted = fileInfoRepository.save(fileInfo);

		HashTag hashtag = null;
		try {
			hashtag = new ObjectMapper().readValue(tag, HashTag.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (tag != null) {
		    /*Note:- we can remove userID from the hastag entity*/
			hashtag.setUserId(id);
			HashTag tagData = hashTagRepository.save(hashtag);

			HashtagVideoMap mappingtag = new HashtagVideoMap();
			log.info("ineinserted.getId() :+=" + fileinserted.getId());
			mappingtag.setVideoId(fileinserted.getId());

			log.info("tagData.getId() :+=" + tagData.getId());

			mappingtag.setTagId(tagData.getId());
			mappingtag.setMapped(true);

			HashtagVideoMap mappedData = hashtagVideoMapRepository.save(mappingtag);
			log.info("mapped Successfully");

		}

		response = server.getSuccessResponse("Uploded Successfully", fileinserted);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}


	@RequestMapping(value = "/uploadmultiple", method = RequestMethod.POST, headers = ("content-type=multipart/*"))
	@ResponseBody
	public ResponseEntity<Map<String, Object>> uplodVideos(@RequestParam("id") long id,
			@RequestParam("file") List<MultipartFile> files, @RequestParam("tag") List<String> tag) throws IOException {
		List<FileInfo> fileinsertedlist = new ArrayList<>();
		FileInfo fileinserted = null;
		HashTag hashtag = new HashTag();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		int i = 0;
		for (MultipartFile file : files) {

			try {
				fileStorage.storemultiple(files);
				log.info("File uploaded successfully! -> filename = " + file.getOriginalFilename());
			} catch (Exception e) {
				log.info("Fail! -> uploaded filename: = " + file.getOriginalFilename());
			}

			FileInfo fileInfo = new FileInfo();
			UserInfo userdetails = new UserInfo();
			fileInfo.setFileName(file.getOriginalFilename());
			fileInfo.setUserId(id);
			userdetails.setId(id);
			Date date = new Date(System.currentTimeMillis());
			fileInfo.setDate(date);
			Resource path = fileStorage.loadFile(file.getOriginalFilename());
			System.out.println("PATH :=" + path.toString());
			fileInfo.setUrl(path.toString());
			fileinserted = fileInfoRepository.save(fileInfo);
			fileinsertedlist.add(fileinserted);
			System.out.println("fileinserted : =" + fileinserted.getId());

		}

		System.out.println("FileID :=" + fileinserted.getId());
		
		
		
		for (String values : tag) {
			try {
				hashtag = new ObjectMapper().readValue(values, HashTag.class);
				System.out.println("Values :=" + values);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (tag != null) {
				// Note:- we can remove userID from the hastag entity
				hashtag.setUserId(id);
				HashTag tagData = hashTagRepository.save(hashtag);
				log.info("hastage userID := " + tagData.getUserId());

				HashtagVideoMap mappingtag = new HashtagVideoMap();

				log.info("ineinserted.getId() :+=" + fileinserted.getId());
				mappingtag.setVideoId(fileinsertedlist.get(i).getId());

				log.info("tagData.getId() :+=" + tagData.getId());

				mappingtag.setTagId(tagData.getId());
				mappingtag.setMapped(true);

				HashtagVideoMap mappedData = hashtagVideoMapRepository.save(mappingtag);
				log.info("mapped Successfully");
				i++;
			}
		}
		
		
		response = server.getSuccessResponse("Uploded Successfully", fileinsertedlist);

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}


	/***
	 * to get the uploaded data by individual user
	 */

	
	@RequestMapping(value = "/view-videos-by-userId", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fetchfilesuplodedbyUser(@RequestParam("id") long id) throws IOException {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<FileInfo> files = (List<FileInfo>) fileInfoRepository.findByUserId(id);
		response = server.getSuccessResponse("fetched", files);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
 
	/*
	 * 
	 * works for both like and dislike
	 * 
	 */
	@ApiOperation(value = "like_file", response = Likes.class)
	@RequestMapping(value = "/likes", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileLike(@RequestParam("userId") long userId,
			@RequestParam("fileId") long fileId, @RequestBody String data) throws IOException, ParseException {
		Likes likesObject = new Likes();
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Likes likesData = null;
		try {
			likesObject = new ObjectMapper().readValue(data, Likes.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		likesObject.setUserId(userId);
		likesObject.setVideoId(fileId);
		Likes likesObj = likeRepository.findByUserIdAndVideoId(userId, fileId);
		String status = null;
		if (likesObject.getStatus() == Status.Liked) {
			status = "Liked";
		} else {
			status = "DisLiked";
		}
		if (likesObj == null) {
			likesData = likeRepository.save(likesObject);

		} else {
			likesObj.setStatus(likesObject.getStatus());
			likesData = likeRepository.save(likesObj);

		}
		response = server.getSuccessResponse(status, likesData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*
	 * works for both comment and uncomment
	 * 
	 */
	@SuppressWarnings("unused")
	@ApiOperation(value = "comment", response = Comments.class)
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> fileComments(@RequestParam("userId") long userId,
			@RequestParam("fileId") long fileId, @RequestBody String data) throws IOException, ParseException {
		Comments comentsObject = null;
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		try {
			comentsObject = new ObjectMapper().readValue(data, Comments.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		comentsObject.setUserId(userId);
		comentsObject.setVideoId(fileId);

		Comments commentsData = new Comments();
		Comments fetchedComments = commentsRepository.findByUserIdAndVideoId(userId, fileId);
		if (fetchedComments.getComment() == null || fetchedComments.getComment().isEmpty()) {

			log.info("cant write empty comment!");
			response = server.getSuccessResponse("cant write empty comment ", commentsData);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			
		} else {

			if (fetchedComments == null) {
				commentsData = commentsRepository.save(comentsObject);

			} else {

				fetchedComments.setComment(comentsObject.getComment());
				commentsData = commentsRepository.save(fetchedComments);
			}
		}
		response = server.getSuccessResponse("commented", commentsData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "share_file", response = Comments.class)
	@RequestMapping(value = "/share", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> sharefile(@RequestParam("userId") long userId,
			@RequestParam("fileId") long fileId, @RequestParam long shareId, @RequestBody String data) throws IOException, ParseException {
		Share shareObject = new Share();
		Optional<FileInfo> fileInfo = fileInfoRepository.findById(fileId);
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		System.out.println("sample");

		try {
			shareObject = new ObjectMapper().readValue(data, Share.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		shareObject.setUserId(userId);
		shareObject.setVideoId(fileId);
		shareObject.setShareId(shareId);
		shareObject.setFileName(fileInfo.get().getFileName());
		Share sharedData = shareRepository.save(shareObject);
		response = server.getSuccessResponse("Successfully share file", sharedData);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	/*
	 * works for both follow/unfollow else we can use unfollow seperatly
	 */

	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> followUser(@RequestParam("id") long id,
			@RequestParam("followingId") long followingId) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		Optional<FollowingGroup> userData = followingGroupRepository.findByUserId(id);

		UserGroupMap fetchedMapData = userGroupMapRepository.findByUserIdAndFollowinguserId(id, followingId);
		if (fetchedMapData == null) {
			mapUsertoUsergroup(id, followingId, userData.get().getId(), userData.get().getUseremail());
			response = server.getSuccessResponse("following-user", followingId);

		} else {
			unmapuserfromUsergroup(id, followingId, userData.get().getId(), userData.get().getUseremail());
			response = server.getSuccessResponse("un-followed user", followingId);

		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> unfollowUser(@RequestParam("id") long id,
			@RequestParam("followingId") long followingId) {
		Map<String, Object> response = new HashMap<String, Object>();
		ServerResponse<Object> server = new ServerResponse<Object>();

		Optional<FollowingGroup> userData = followingGroupRepository.findByUserId(id);

		unmapuserfromUsergroup(id, followingId, userData.get().getId(), userData.get().getUseremail());
		response = server.getSuccessResponse("unfollowed-Successfull", followingId);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/getall-in-group", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findUsersInGroup(@RequestParam("id") long id) {
		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		List<UserGroupMap> fetchedUsers =  userGroupMapRepository.findByUserId(id);

		response = server.getSuccessResponse("fecthed users from group", fetchedUsers);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
	
	 

	@RequestMapping(value = "/fecthliked", method = RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<Map<String, Object>> listoflikedfiles(@RequestParam("videoId") long videoId)
		{
			ServerResponse<Object> server = new ServerResponse<Object>();
			Map<String, Object> response = new HashMap<String, Object>();
			List<Likes> likesData =null;
			 if(Status.Liked != null) {
		 	 likesData=likeRepository.findByVideoId(videoId);
			 }
			
			response = server.getSuccessResponse("liked", likesData);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}

	
	
	

	public void mapUsertoUsergroup(long userId, long followinguserId, long groupId,String email) {
		log.info("to follow user");
 
		UserGroupMap userGroupMap = new UserGroupMap();
		userGroupMap.setGroupId(groupId);
		userGroupMap.setUserId(userId);
		userGroupMap.setUserEmail(email);
		userGroupMap.setFollowinguserId(followinguserId);
		userGroupMap.setMapped(true);

		UserGroupMap usermappedData = userGroupMapRepository.save(userGroupMap);
		log.info("mapped := " +followinguserId + "to the := " +userId+ "group");
	}

	public void unmapuserfromUsergroup(long userId, long followinguserId, long groupId, String email) {
		UserGroupMap fetchedMapData = userGroupMapRepository.findByUserIdAndFollowinguserId(userId, followinguserId);
		userGroupMapRepository.deleteById(fetchedMapData.getId()); 
		log.info("unmapped := " +followinguserId + "from the" +userId+ "group");
  
	}

	public void DefaultfollowingGroup(long userId, String email) {
		FollowingGroup groupData = new FollowingGroup();
		groupData.setUserId(userId);
		groupData.setUseremail(email);
		groupData.setGroupname("following");
		groupData.setDefault(true);
		FollowingGroup usergroup = followingGroupRepository.save(groupData);
		log.info("default followingGroup created for the userId := " +usergroup.getId());
	}

	public void DefaultfollowersGroup(long userId, String email) {
		FollowersGroup groupData = new FollowersGroup();
		groupData.setUserId(userId);
		groupData.setUseremail(email);
		groupData.setGroupname("followers");
		groupData.setDefault(true);
		FollowersGroup usergroup = followersGroupRepository.save(groupData);
		log.info("default followersGroup created for the userId := " +usergroup.getId());
  	}

	@ApiOperation(value = "block-user", response = UserInfo.class)
	@RequestMapping(value = "/block", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> blockuser(@RequestParam("data") String username) throws Exception {

		ServerResponse<Object> server = new ServerResponse<Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		/*
		 * TO:DO search from list of user either from the main page or individial user
		 * followers group and follwing group and block user
		 * 
		 * Take BlockedUsers group and
		 */

		return null;

	}

}
