package com.blogsite.blog.service.repository;

import com.blogsite.blog.service.entity.Blog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BlogRepository extends MongoRepository<Blog,String> {
    Blog findByBlogname(String blogname);

    List<Blog> findAllBlogsByUsername(String username);

    List<Blog> findByTimestampBetween(LocalDate startDate, LocalDate endDate);

    List<Blog> findAllBlogsByCategory(String category);
}
