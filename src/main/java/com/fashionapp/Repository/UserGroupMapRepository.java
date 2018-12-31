package com.fashionapp.Repository;

 
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.UserGroupMap;

public interface UserGroupMapRepository extends CrudRepository<UserGroupMap, Long>{

	List<UserGroupMap> findByUserId(long userId);

	UserGroupMap findByUserIdAndFollowinguserId(long id, long followingId);

}
