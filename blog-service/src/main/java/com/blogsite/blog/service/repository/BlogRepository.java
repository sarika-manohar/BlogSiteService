package com.blogsite.blog.service.repository;

import com.blogsite.blog.service.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
    Blog findByBlogname(String blogname);
}
