package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.Comments;

public interface CommentsRepository extends CrudRepository<Comments, Long>{

	Comments findByUserIdAndVideoId(long userId, long fileId);

}
