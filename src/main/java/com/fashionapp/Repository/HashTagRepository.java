package com.fashionapp.Repository;

import org.springframework.data.repository.CrudRepository;

import com.fashionapp.Entity.HashTag;

public interface HashTagRepository extends CrudRepository<HashTag, Long> {

	HashTag findByHashTag(String hashtag);

}
